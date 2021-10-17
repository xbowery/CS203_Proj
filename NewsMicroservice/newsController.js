const News = require("./newsSchema");
const cron = require("node-cron");
require("dotenv").config();

const NewsAPI = require("newsapi");
const newsapi = new NewsAPI(process.env.NEWSAPI_KEY);

module.exports.getNews = async (req, res, next) => {
  try {
    const restaurantTerms = ["Restaurant", "Food", "Dining"].join(" OR ");
    const newsArr = await fetchLatestNewsFromExternal(
      `Singapore AND Covid NOT (${restaurantTerms})`
    );

    /**
     * Generate a map of updates that will be passed to MongoDB at once using
     * the bulkWrite function. It will create a new entry if an article is not found.
     * In this case, the criteria to check (filter) is the url itself
     */
    const craftedBulkWriteObject = newsArr.map((news) => {
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

    const dbResp = await News.bulkWrite(craftedBulkWriteObject);
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
cron.schedule("* */1 * * *", () => {
  fetchLatestNewsFromExternal("Covid AND Singapore");
});

const fetchLatestNewsFromExternal = async (title) => {
  try {
    const response = await newsapi.v2.everything({
      q: title,
      domains: "straitstimes.com,channelnewsasia.com,nytimes.com,bbc.co.uk",
      language: "en",
      sortBy: "publishedAt",
      pageSize: 20,
    });
    return response.articles;
  } catch (err) {
    console.error("News API fetch error");
  }
};

// Run it once upon init to populate database
// fetchLatestNewsFromExternal("Covid AND Singapore");
