"use strict";

function Util() {
  /**
   * Generate a map of updates that will be passed to MongoDB at once using
   * the bulkWrite function. It will create a new entry if it is not found.
   * In this case, the criteria to check (filter) is the region name
   *
   * @param {*} rawObj object of region-covid number mappings
   * @returns an array-object of the necessary database operations
   */
  this.craftBulkWriteObject = (rawObj) => {
    if (!rawObj || Object.keys(rawObj).length === 0) {
      return [{}];
    }

    return Object.keys(rawObj).map((region) => {
      const regionObj = {
        region,
        numCase: rawObj[region],
      };
      return {
        updateOne: {
          filter: {
            region,
          },
          update: {
            $set: regionObj,
          },
          upsert: true,
          timestamps: true,
        },
      };
    });
  };

  /**
   * Utility to create a feature collection. This is best supported by leaflet js
   * Ref: https://datatracker.ietf.org/doc/html/rfc7946#section-3.3
   *
   * @param {*} restaurant
   * @returns
   */
  this.createFeatureCollection = (restaurants) => {
    const featuresArr = this.createFeatures(restaurants);

    return {
      type: "FeatureCollection",
      features: featuresArr,
    };
  };

  /**
   * Utility to create an array of features.
   * Ref: https://datatracker.ietf.org/doc/html/rfc7946#section-3.2
   * @param {*} restaurant
   */
  this.createFeatures = (restaurants) => {
    return restaurants.map((restaurant) => {
      const { location: geometry, loc, name, region, description } = restaurant;
      return {
        type: "Feature",
        properties: {
          name,
          loc,
          description,
          numCase: region[0].numCase,
        },
        geometry,
      };
    });
  };
}

module.exports = Util;
