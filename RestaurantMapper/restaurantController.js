"use strict";

const Restaurant = require("./restaurantSchema");
const Case = require("./caseSchema");
const cron = require("node-cron");
const axios = require("axios").default;

const Util = require("./util.js");
const util = new Util();

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
  await getCovidData();
  res.json({ Success: true });
};

/**
 * Retrieve the covid-19 heatmap data from the Singapore official API.
 */
const getCovidData = async () => {
  try {
    const res = await axios.get(process.env.COVID_API);
    const calcAggOutput = JSON.parse(res.data.split("=")[1].slice(0, -1));
    await updateDBCovidFigures(calcAggOutput.sz.aggregatedObj);
  } catch (err) {
    console.error(err);
  }
};

/**
 * To iterate through the object and update the database accordingly.
 *
 * @param {*} figures
 */
const updateDBCovidFigures = async (figures) => {
  const dbResp = await Case.bulkWrite(util.craftBulkWriteObject(figures));
  const { nUpserted, nModified } = dbResp;
  console.log(`Num upserted: ${nUpserted}. Num modified: ${nModified}`);
  return dbResp;
};

/**
 * CRON process to perform the fetching of official data twice every day. This query is done twice a day
 * since Singapore only updates the data every day, in case we miss the first update.
 */
if (process.env.NODE_ENV !== "test") {
  cron.schedule("0 */12 * * *", () => {
    getCovidData();
  });
}
