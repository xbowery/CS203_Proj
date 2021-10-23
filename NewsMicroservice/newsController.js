const News = require("./newsSchema");
const cron = require("node-cron");

const Utils = require("./utils");
const utils = new Utils();

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
    const searchLimit = 8;
    const latestGeneralNews = await fetchNewsFromDB(
      { type: "General" },
      searchLimit
    );
    const latestRestaurantNews = await fetchNewsFromDB(
      { type: "Restaurant" },
      searchLimit
    );

    const returnObj = {
      success: true,
      generalNews: latestGeneralNews,
      restaurantNews: latestRestaurantNews,
    };

    return res.status(200).json(returnObj);
  } catch (err) {
    console.error(err);
    next("An error occurred when fetching the news. Please try again.");
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
  const queryObj = utils.craftQueryObj(searchStr);

  // queryObj will be empty if user does not specify any query parameters
  try {
    const limit = 5;
    const news = await fetchNewsFromDB(queryObj, limit);

    const returnObj = {
      success: true,
      news,
    };

    return res.status(200).json(returnObj);
  } catch (err) {
    console.error(err);
    next("An error occurred when fetching the news. Please try again.");
  }
};

module.exports.devFetch = async (req, res, next) => {
  const dbResp = await fetchNews();
  const { nUpserted, nModified } = dbResp;
  return res.status(200).json({ nUpserted, nModified });
};

/**
 *
 * @param {*} query
 * @param {*} searchLimit
 * @returns an object containing News which are relevant based on the queryObj string
 */
const fetchNewsFromDB = async (query, searchLimit) => {
  return await News.find(query)
    .lean()
    .sort("-updatedAt")
    .select("-_id -createdAt")
    .limit(searchLimit)
    .exec();
};

/**
 * Internal function to retrieve the news and save to the DB
 *
 * @param {*} req
 * @param {*} res
 * @param {*} next
 * @return dbResp a response in terms of an object of the status of insert / update
 */
const fetchNews = async () => {
  try {
    const { general, restaurant } = await fetchLatestNewsFromExternal();

    const craftedBulkWriteObjectGeneral = utils.craftBulkWriteObject(
      general,
      "General"
    );
    const craftedBulkWriteObjectRestaurant = utils.craftBulkWriteObject(
      restaurant,
      "Restaurant"
    );

    const mergedBulkWriteArr = [
      ...craftedBulkWriteObjectGeneral,
      ...craftedBulkWriteObjectRestaurant,
    ];

    const dbResp = await News.bulkWrite(mergedBulkWriteArr);
    const { nUpserted, nModified } = dbResp;

    console.log(`Num upserted: ${nUpserted}. Num modified: ${nModified}`);

    return dbResp;
  } catch (err) {
    console.error(err);
  }
};

// /**
//  * Generate a map of updates that will be passed to MongoDB at once using
//  * the bulkWrite function. It will create a new entry if an article is not found.
//  * In this case, the criteria to check (filter) is the url itself
//  *
//  * @param {*} newsObj
//  * @param {*} category
//  * @returns an array-object of the necessary database operations
//  */
// const craftBulkWriteObject = (newsObj, category) => {
//   if (!newsObj || !category) {
//     return [{}];
//   }

//   return newsObj.map((news) => {
//     news.source = news.source.name;
//     news.imageUrl = news.urlToImage;
//     news.type = category;
//     return {
//       updateOne: {
//         filter: {
//           url: news.url,
//         },
//         update: {
//           $set: news,
//         },
//         upsert: true,
//         timestamps: true,
//       },
//     };
//   });
// };

/**
 * Scheduled cron job that is ran every 60 mins to fetch the latest news
 * It will update the database which automatically checks if the entry exists
 * If the entry exists, it will update the entry. Else, it will add a new entry.
 */
if (process.env.NODE_ENV !== "test") {
  cron.schedule("0 * * * *", () => {
    fetchNews();
  });
}

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
      qInTitle: query,
      domains: "straitstimes.com,channelnewsasia.com,nytimes.com,bbc.co.uk",
      language: "en",
      sortBy: "publishedAt",
      pageSize: 20,
    });
  } catch (err) {
    console.error(err);
  }
};
