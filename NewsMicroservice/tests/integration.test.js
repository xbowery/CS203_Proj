const chai = require("chai");
const assert = chai.assert;
const chaiHttp = require("chai-http");
const server = require("../server");
const News = require("../newsSchema");

chai.use(chaiHttp);

const URI = "/api/v1";
const NEWS_LIMIT = 8;

// Waits 1.5 seconds for everything to initialise
beforeAll((done) => {
  setTimeout(() => {
    done();
  }, 1500);
});

// Clean up all the prior entries.
afterAll(async () => {
  return News.deleteMany({});
});

test("getInitialNews_ReturnsEmptyObject", function (done) {
  chai
    .request(server)
    .get(URI + "/news/all")
    .end(function (err, res) {
      assert.equal(res.status, 200);
      const { generalNews, restaurantNews, officialGovNews } = res.body;
      assert.deepEqual(generalNews, []);
      assert.deepEqual(restaurantNews, []);
      assert.deepEqual(officialGovNews, []);
      done();
    });
});

// Make sure that during a normal API call, 40 entries will be updated or inserted
test("InsertNews_Success", function (done) {
  chai
    .request(server)
    .get(URI + "/dev")
    .end(function (err, res) {
      assert.equal(res.status, 200);
      assert.equal(res.body.nUpserted + res.body.nModified, 40);
      done();
    });
});

test("RetrieveAllNews_ReturnsEightOfEachTypeExceptGov", function (done) {
  chai
    .request(server)
    .get(URI + "/news/all")
    .end(function (err, res) {
      assert.equal(res.status, 200);

      const { generalNews, restaurantNews, officialGovNews } = res.body;
      assert.equal(generalNews.length, 8);
      assert.equal(restaurantNews.length, 8);
      // We have yet to insert any news from MOH for the RSS
      assert.equal(officialGovNews.length, 0);
      done();
    });
});

// A simple function to check if there are any Gov articles in the array of news
const checkType = (type, article) => article.type === type;

test("RetrieveNewsWithEmptySearchQuery_ReturnsLatestFiveWithoutGov", function (done) {
  chai
    .request(server)
    .get(URI + "/news")
    .end(function (err, res) {
      assert.equal(res.status, 200);

      const { news } = res.body;
      assert.equal(news.length, 5);

      assert.isFalse(news.some(checkType.bind(null, "Gov")));
      done();
    });
});

test("RetrieveNewsWithInvalidType_ReturnsErrorMessage", function (done) {
  chai
    .request(server)
    .get(URI + "/news")
    .query({ q: "covid", type: "doesNotExist" })
    .end(function (err, res) {
      assert.equal(res.status, 400);

      const { error } = res.body;
      assert.equal(
        error,
        "Please select one of the following types: 'General', 'Gov', 'Restaurant'"
      );

      done();
    });
});

test("RetrieveNewsWithGivenSearchQuery_ReturnsAtMostFiveWithoutGov", function (done) {
  chai
    .request(server)
    .get(URI + "/news")
    .query({ q: "covid" })
    .end(function (err, res) {
      assert.equal(res.status, 200);

      const { news } = res.body;
      assert.isAtLeast(news.length, 0);
      assert.isAtMost(news.length, 5);

      assert.isFalse(news.some(checkType.bind(null, "Gov")));
      done();
    });
});

test("RetrieveNewsWithGivenSearchQueryAndType_ReturnsAtMostOfType", function (done) {
  chai
    .request(server)
    .get(URI + "/news")
    .query({ q: "covid", type: "General" })
    .end(function (err, res) {
      assert.equal(res.status, 200);

      const { news } = res.body;
      assert.isAtLeast(news.length, 0);
      assert.isAtMost(news.length, 5);

      assert.isTrue(news.every(checkType.bind(null, "General")));
      done();
    });
});

// Store a global variable for checking later
let numOfficialInserted = 0;
test("RetrieveRSSFeed_ReturnsSuccess", function (done) {
  chai
    .request(server)
    .get(URI + "/rssFetch")
    .end(function (err, res) {
      assert.equal(res.status, 200);

      numOfficialInserted = res.body.nUpserted + res.body.nModified;
      assert.isAtLeast(numOfficialInserted, 1);
      done();
    });
});

// Test the endpoint again after fetching RSS news. If it fetch fewer than the limit,
// we expect it to return everything. Else, return the NEWS_LIMIT.
test("RetrieveAllNews_ReturnsEightOfEachType", function (done) {
  chai
    .request(server)
    .get(URI + "/news/all")
    .end(function (err, res) {
      assert.equal(res.status, 200);

      const { generalNews, restaurantNews, officialGovNews } = res.body;
      const numGovExpected =
        numOfficialInserted < NEWS_LIMIT ? numOfficialInserted : NEWS_LIMIT;
      assert.equal(generalNews.length, NEWS_LIMIT);
      assert.equal(restaurantNews.length, NEWS_LIMIT);
      assert.equal(officialGovNews.length, numGovExpected);
      done();
    });
});

describe("Retrieve News of specific types", () => {
  test("RetrieveNewsOfUnknownType_ReturnsErrorMessage", function (done) {
    chai
      .request(server)
      .get(URI + "/news/doesNotExist")
      .end(function (err, res) {
        assert.equal(res.status, 400);

        const { error } = res.body;
        assert.equal(
          error,
          "Please select one of the following types: 'General', 'Gov', 'Restaurant'"
        );
        done();
      });
  });

  test("RetrieveNewsOfAllowedTypeGeneral_ReturnsMaxEightOfThatType", function (done) {
    chai
      .request(server)
      .get(URI + "/news/General")
      .end(function (err, res) {
        assert.equal(res.status, 200);

        const { latestNews } = res.body;
        assert.equal(latestNews.length, 8);

        assert.isTrue(latestNews.every(checkType.bind(null, "General")));
        assert.isFalse(latestNews.some(checkType.bind(null, "Gov")));
        assert.isFalse(latestNews.some(checkType.bind(null, "Restaurant")));
        done();
      });
  });

  test("RetrieveNewsOfAllowedTypeRestaurant_ReturnsMaxEightOfThatType", function (done) {
    chai
      .request(server)
      .get(URI + "/news/Restaurant")
      .end(function (err, res) {
        assert.equal(res.status, 200);

        const { latestNews } = res.body;
        assert.equal(latestNews.length, 8);

        assert.isTrue(latestNews.every(checkType.bind(null, "Restaurant")));
        assert.isFalse(latestNews.some(checkType.bind(null, "Gov")));
        assert.isFalse(latestNews.some(checkType.bind(null, "General")));
        done();
      });
  });

  test("RetrieveNewsOfAllowedTypeGov_ReturnsMaxEightOfThatType", function (done) {
    chai
      .request(server)
      .get(URI + "/news/Gov")
      .end(function (err, res) {
        assert.equal(res.status, 200);

        const { latestNews } = res.body;
        assert.isAtLeast(latestNews.length, 1);
        assert.isAtMost(latestNews.length, 8);

        assert.isTrue(latestNews.every(checkType.bind(null, "Gov")));
        assert.isFalse(latestNews.some(checkType.bind(null, "Restaurant")));
        assert.isFalse(latestNews.some(checkType.bind(null, "General")));
        done();
      });
  });
});
