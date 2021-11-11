"use strict";

const Restaurant = require("./restaurantSchema");
const Case = require("./caseSchema");
const cron = require("node-cron");
const axios = require("axios").default;

const Util = require("./util.js");
const util = new Util();

const TOKEN = process.env.ONEMAP_APIKEY;
const ONEMAP_URI = "https://developers.onemap.sg";

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
  try {
    const { name, location: loc, description } = req.body;
    const latLong = await getLatLong(loc);
    const region = await getRegion(latLong);
    const _id = await Case.findOne({ region }, "_id").exec();
    res.json(_id);
  } catch (err) {
    console.error(err);
    next(err);
  }
};

/**
 * Retrieve from the OneMap API the latitude and longitude of the restaurant. If there are no location found,
 * we will ignore the insert. If found, we will use the first entry returned since it is likely the most accurate
 *
 * @param {*} loc
 */
const getLatLong = async (loc) => {
  try {
    const res = await axios.get(
      `${ONEMAP_URI}/commonapi/search?searchVal=${loc}&returnGeom=Y&getAddrDetails=N`
    );

    const { found, results } = res.data;

    if (found === 0) {
      return;
    }

    return results[0];
  } catch (err) {
    console.error(err);
    throw new Error(err);
  }
};

const getRegion = async (latLong) => {
  try {
    const { LATITUDE: lat, LONGITUDE: long } = latLong;
    const res = await axios.get(
      `${ONEMAP_URI}/privateapi/popapi/getPlanningarea?token=${TOKEN}&lat=${lat}&lng=${long}`
    );
    return res.data[0].pln_area_n;
  } catch (err) {
    console.error(err);
    throw new Error(err);
  }
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
    throw new Error(err);
  }
};

/**
 * To iterate through the object and update the database accordingly.
 * A bulkWrite function call is used to optimise database saving.
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