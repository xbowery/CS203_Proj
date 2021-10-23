function Utils() {
  /**
   * Generate a map of updates that will be passed to MongoDB at once using
   * the bulkWrite function. It will create a new entry if an article is not found.
   * In this case, the criteria to check (filter) is the url itself
   *
   * @param {*} newsObj
   * @param {*} category
   * @returns an array-object of the necessary database operations
   */
  this.craftBulkWriteObject = (newsObj, category) => {
    if (!newsObj || !category) {
      return [{}];
    }

    return newsObj.map((news) => {
      news.source = news.source.name;
      news.imageUrl = news.urlToImage;
      news.type = category;
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
  };

  /**
   * Craft a query string for the news API when fetching data
   *
   * @param {*} searchStr
   * @return object containing a regex, or an empty object if the searchStr is empty
   */
  this.craftQueryObj = (searchStr) => {
    if (searchStr === "") {
      return {};
    }

    const regex = new RegExp(searchStr, "i");
    return {
      $or: [
        {
          title: regex,
        },
        {
          content: regex,
        },
      ],
    };
  };
}

module.exports = Utils;
