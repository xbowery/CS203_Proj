"use strict";

const Restaurant = require("./restaurantSchema");
const Case = require("./caseSchema");
const cron = require("node-cron");
const axios = require("axios").default;

const mapper = require("./utils/mapper.js");
const Util = require("./utils/util.js");
const util = new Util();

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
 * Obtain restaurants in geoJSON format for the front-end to parse
 * Ref: https://datatracker.ietf.org/doc/html/rfc7946
 *
 * @param {*} req
 * @param {*} res
 * @param {*} next
 */
module.exports.getRestaurants = async (req, res, next) => {
  try {
    const allRestaurants = await Restaurant.find().populate("region");
    const featureCollection = util.createFeatureCollection(allRestaurants);
    res.status(200).json(featureCollection);
  } catch (err) {
    console.error(err);
    next(err);
  }
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

    if (!name || !loc || !description) {
      throw new Error("Missing restaurant fields");
    }

    const { LATITUDE: lat, LONGITUDE: long } = await getLatLong(loc);
    const region = mapper.getSubzoneAtPoint([long, lat]).properties.name;

    try {
      const _id = await Case.findOne({ region }, "_id").exec();

      if (!_id) {
        return res.status(404).json({ error: "Restaurant location not found" });
      }

      const restaurant = {
        name,
        description,
        loc,
        region: _id,
        location: {
          type: "Point",
          coordinates: [long, lat],
        },
      };

      const restaurantObj = await insertRestaurantIntoDB(restaurant);
      res.status(200).json({ success: true, restaurant: restaurantObj });
    } catch (err) {
      console.error(err);
    }
  } catch (err) {
    console.error(err);
    next(err);
  }
};

/**
 * Insert the restaurant into the DB
 */
const insertRestaurantIntoDB = async (restaurant) => {
  const { name, loc, region } = restaurant;
  return await Restaurant.findOneAndUpdate(
    {
      name,
      loc,
      region,
    },
    restaurant,
    {
      new: true,
      upsert: true,
      runValidators: true,
    }
  );
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
