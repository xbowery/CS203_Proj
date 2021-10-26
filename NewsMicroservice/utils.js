const Parser = require("rss-parser");
const parser = new Parser();

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
    if (!newsObj || !category || Object.keys(newsObj[0]).length === 0) {
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
   * Craft a query string for the news API when fetching data. This only fetches news and not government
   * related press releases.
   *
   * @param {*} searchStr
   * @return object containing a regex, or an empty object if the searchStr is empty
   */
  this.craftQueryObj = (searchStr = "") => {
    if (searchStr === "") {
      return {
        type: {
          $ne: "Gov",
        },
      };
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
      type: {
        $ne: "Gov",
      },
    };
  };

  /**
   * Fetch the data from the MOH website RSS feed and create an object to insert into the database
   * This function will effectively ignore any entries older than 3 days to save processing time
   *
   * The RSS is sorted based on date of publish, hence it will stop immediately upon reaching the
   * first document that is older than 3 days
   */
  this.parseRss = async () => {
    const feed = await parser.parseURL(process.env.MOHRSS);
    let updateArr = [];

    let dateThresh = new Date();
    dateThresh.setDate(dateThresh.getDate() - 3);

    for (const item of feed.items) {
      if (new Date(item.pubDate) < dateThresh) {
        break;
      }

      const feedItem = this.craftFeedObject(item);
      updateArr.push(this.createRssUpdateObj(feedItem));
    }

    return updateArr;
  };

  /**
   * Intermediate function to craft a specialised object only with the required fields
   *
   * @param {*} item
   * @returns
   */
  this.craftFeedObject = (item) => {
    const { title, link: url } = item;
    return {
      title,
      url,
      source: "MOH",
      content: "-",
      imageUrl: "-",
      type: "Gov",
    };
  };

  this.createRssUpdateObj = (feedItem) => {
    return {
      updateOne: {
        filter: {
          url: feedItem.url,
        },
        update: {
          $set: feedItem,
        },
        upsert: true,
        timestamps: true,
      },
    };
  };
}

module.exports = Utils;
