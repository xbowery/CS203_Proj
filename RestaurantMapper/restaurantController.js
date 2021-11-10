const Restaurant = require("./restaurantSchema");
const Case = require("./caseSchema");
const cron = require("node-cron");

/**
 * Loads the main webpage to render the restaurants in a nice looking map using OneMap API
 *
 * @param {*} req
 * @param {*} res
 * @param {*} next
 * @return the webpage
 */
module.exports.loadWebpage = async (req, res, next) => {
  res.sendFile(process.cwd() + "/views/index.html");
};

/**
 * This function allows the backend to insert a new restaurant into the collection of restaurants
 * to be displayed in the map.
 *
 * @param {*} req
 * @param {*} res
 * @param {*} next
 */
module.exports.insertRestaurant = async (req, res, next) => {
  res.json("Hello?");
};

/**
 * Retrieve the covid-19 heatmap data from the Singapore official API.
 */
const getCovidData = async () => {};

/**
 * CRON process to perform the fetching of official data twice every day. This query is done twice a day
 * since Singapore only updates the data every day, in case we miss the first update.
 */
if (process.env.NODE_ENV !== "test") {
  cron.schedule("0 */12 * * *", () => {
    getCovidData();
  });
}
