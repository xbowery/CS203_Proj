const News = require("./newsSchema");
const cron = require("node-cron");
require("dotenv").config();

const NewsAPI = require("newsapi");
const newsapi = new NewsAPI(process.env.NEWSAPI_KEY);

module.exports.getNews = async (req, res, next) => {};

/**
 * Scheduled cron job that is ran every 15 mins to fetch the latest news
 * It will update the database which automatically checks if the entry exists
 * If the entry exists, it will update the entry. Else, it will add a new entry.
 */
cron.schedule("*/15 * * * *", () => {
  fetchLatestNewsFromExternal("Covid AND Singapore");
});

const fetchLatestNewsFromExternal = (title) => {
  newsapi.v2
    .everything({
      q: title,
      domains: "straitstimes.com,channelnewsasia.com,news.google.com",
      language: "en",
      sortBy: "publishedAt",
    })
    .then((response) => {
      console.log(response);
    });
};

// fetchLatestNewsFromExternal("Covid AND Singapore");
