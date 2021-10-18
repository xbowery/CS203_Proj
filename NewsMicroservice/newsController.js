const News = require("./newsSchema");
const cron = require("node-cron");
require("dotenv").config();

const NewsAPI = require("newsapi");
const newsapi = new NewsAPI(process.env.NEWSAPI_KEY);

/**
 * This function is the main functionality of this microservice - to return the top 8 news of each category
 * These articles are sorted by the date of creation (descending time of insertion)
 *
 * @param {*} req
 * @param {*} res
 * @param {*} next
 * @return JSON object of the top 8 news of each category
 */
module.exports.getNews = async (req, res, next) => {
  try {
    const latestGeneralNews = await News.find({ type: "Regular" })
      .lean()
      .sort("createdAt")
      .select("-_id -createdAt")
      .limit(8)
      .exec();
    const latestRestaurantNews = await News.find({ type: "Restaurant" })
      .lean()
      .sort("createdAt")
      .select("-_id -createdAt")
      .limit(8)
      .exec();

    const returnObj = {
      success: true,
      generalNews: latestGeneralNews,
      restaurantNews: latestRestaurantNews,
    };

    return res.status(200).json(returnObj);
  } catch (err) {
    console.error(err);
    next(err);
  }
};

/**
 * This function allows user to search for news based on a single search string to return all related news
 * For instances where no query is sent or the query is simply and empty string, it will default to
 * return latest 5 news.
 *
 * @param {*} req
 * @param {*} res
 * @param {*} next
 * @return JSON object of the top 8 news of each category
 */
module.exports.searchNews = async (req, res, next) => {
  const searchStr = req.query.q;

  // Crafts an object that searches for that string in either the title or the content or both (case insensitive)
  let queryObj = {};
  if (searchStr !== "") {
    const regex = new RegExp(searchStr, "i");
    queryObj = {
      $or: [
        {
          title: regex,
        },
        {
          content: regex,
        },
      ],
    };
  }

  // queryObj will be empty if user does not specify any query parameters
  try {
    const news = await News.find(queryObj)
      .lean()
      .sort("createdAt")
      .select("-_id -createdAt")
      .limit(5)
      .exec();

    const returnObj = {
      success: true,
      news,
    };

    return res.status(200).json(returnObj);
  } catch (err) {
    console.error(err);
    next(err);
  }
};

/**
 * Internal function to retrieve the news and save to the DB
 *
 * @param {*} req
 * @param {*} res
 * @param {*} next
 */
module.exports.fetchNews = async (req, res, next) => {
  try {
    const { general, restaurant } = await fetchLatestNewsFromExternal();

    /**
     * Generate a map of updates that will be passed to MongoDB at once using
     * the bulkWrite function. It will create a new entry if an article is not found.
     * In this case, the criteria to check (filter) is the url itself
     */
    const craftedBulkWriteObjectA = general.map((news) => {
      news.source = news.source.name;
      return {
        updateOne: {
          filter: {
            url: news.url,
          },
          update: {
            $set: news,
          },
          upsert: true,
          timestamps: true,
        },
      };
    });

    const craftedBulkWriteObjectB = restaurant.map((news) => {
      news.source = news.source.name;
      news.type = "Restaurant";
      return {
        updateOne: {
          filter: {
            url: news.url,
          },
          update: {
            $set: news,
          },
          upsert: true,
          timestamps: true,
        },
      };
    });

    const mergedBulkWriteArr = [
      ...craftedBulkWriteObjectA,
      ...craftedBulkWriteObjectB,
    ];

    const dbResp = await News.bulkWrite(mergedBulkWriteArr);
    const { nUpserted, nModified } = dbResp;

    console.log(`Num upserted: ${nUpserted}. Num modified: ${nModified}`);
    res.status(200).json({ success: true });
  } catch (err) {
    console.error(err);
    next(err);
  }
};

/**
 * Scheduled cron job that is ran every 60 mins to fetch the latest news
 * It will update the database which automatically checks if the entry exists
 * If the entry exists, it will update the entry. Else, it will add a new entry.
 */
cron.schedule("0 * * * *", () => {
  fetchLatestNewsFromExternal();
});

const fetchLatestNewsFromExternal = async () => {
  const restaurantTerms = ["Restaurant", "Food", "Dining"].join(" OR ");
  const generalQuery = `Singapore AND Covid NOT (${restaurantTerms})`;
  const restaurantQuery = `Singapore AND Covid AND (${restaurantTerms})`;

  try {
    const generalNews = await apiQuery(generalQuery);
    const restaurantNews = await apiQuery(restaurantQuery);

    return {
      general: generalNews.articles,
      restaurant: restaurantNews.articles,
    };
  } catch (err) {
    console.error("News API fetch error");
  }
};

const apiQuery = async (query) => {
  try {
    return await newsapi.v2.everything({
      q: query,
      domains: "straitstimes.com,channelnewsasia.com,nytimes.com,bbc.co.uk",
      language: "en",
      sortBy: "publishedAt",
      pageSize: 20,
    });
  } catch (err) {
    console.error(err);
  }
};

// Run it once upon init to populate database
// fetchLatestNewsFromExternal("Covid AND Singapore");
