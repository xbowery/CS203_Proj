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
}

module.exports = Util;
