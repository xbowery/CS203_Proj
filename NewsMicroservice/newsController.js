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

    const latestGovNews = await fetchNewsFromDB({ type: "Gov" }, searchLimit);

    const returnObj = {
      success: true,
      generalNews: latestGeneralNews,
      restaurantNews: latestRestaurantNews,
      officialGovNews: latestGovNews,
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
  const { q, type } = req.query;
  // Crafts an object that searches for that string in either the title or the content or both (case insensitive)
  let queryObj;

  try {
    queryObj = utils.craftQueryObj(q, type);
  } catch (err) {
    return res.status(400).json({ success: false, error: err.message });
  }

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

/**
 * An outward facing function to fetch news for a specific type of the 3 different allowed types
 * Types: [General, Gov, Restuarant]
 *
 * @param {*} req
 * @param {*} res
 * @param {*} next
 */
module.exports.getNewsWithType = async (req, res, next) => {
  const { type } = req.params;
  if (!utils.validateType(type)) {
    const errorMsg =
      "Please select one of the following types: 'General', 'Gov', 'Restaurant'";
    return res.status(400).json({ success: false, error: errorMsg });
  }

  try {
    const searchLimit = 8;
    const latestNews = await fetchNewsFromDB({ type }, searchLimit);
    res.status(200).json({ success: true, latestNews });
  } catch (err) {
    console.error(err);
    next(err);
  }
};

/**
 * Internal function to fetch news on demand to test the News API service
 *
 * @param {*} req
 * @param {*} res
 * @param {*} next
 * @returns JSON response of the number of entries updated or inserted
 */
module.exports.devFetch = async (req, res, next) => {
  try {
    const dbResp = await fetchNews();
    const { nUpserted, nModified } = dbResp;
    return res.status(200).json({ nUpserted, nModified });
  } catch (err) {
    console.error(err);
    next(err);
  }
};

/**
 * Internal function to force the query and update of news from MOH using their RSS
 *
 * @param {*} req
 * @param {*} res
 * @param {*} next
 * @returns number of entries inserted or updated
 */
module.exports.rssFetch = async (req, res, next) => {
  try {
    const { nUpserted, nModified } = await parseRssAndSaveToDB();

    return res.status(200).json({ nUpserted, nModified });
  } catch (err) {
    console.error(err);
    next(err);
  }
};

const parseRssAndSaveToDB = async () => {
  const rssUpdateObj = await utils.parseRss();
  return await bulkWriteToDB(rssUpdateObj);
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

    const dbResp = bulkWriteToDB(mergedBulkWriteArr);

    return dbResp;
  } catch (err) {
    console.error(err);
  }
};

/**
 * Scheduled cron job that is ran every 60 mins to fetch the latest news
 * It will update the database which automatically checks if the entry exists
 * If the entry exists, it will update the entry. Else, it will add a new entry.
 *
 * The second cron is for RSS Feeds that will run once every day at midnight
 */
if (process.env.NODE_ENV !== "test") {
  cron.schedule("0 * * * *", () => {
    fetchNews();
  });

  cron.schedule("0 0 * * *", () => {
    parseRssAndSaveToDB();
  });
}

const bulkWriteToDB = async (bulkWriteObj) => {
  const dbResp = await News.bulkWrite(bulkWriteObj);
  const { nUpserted, nModified } = dbResp;
  console.log(`Num upserted: ${nUpserted}. Num modified: ${nModified}`);
  return dbResp;
};

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
