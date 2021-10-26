const chai = require("chai");
const assert = chai.assert;
const chaiHttp = require("chai-http");
const server = require("../server");
const News = require("../newsSchema");

chai.use(chaiHttp);

const URI = "/api/v1";

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
    .get(URI + "/news")
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

test("RetrieveNews_ReturnsEightOfEachTypeExceptGov", function (done) {
  chai
    .request(server)
    .get(URI + "/news")
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
const gov = (article) => article.type === "Gov";

test("RetrieveNewsWithEmptySearchQuery_ReturnsLatestFiveWithoutGov", function (done) {
  chai
    .request(server)
    .get(URI + "/news/search")
    .end(function (err, res) {
      assert.equal(res.status, 200);

      const { news } = res.body;
      assert.equal(news.length, 5);

      assert.isFalse(news.some(gov));
      done();
    });
});

test("RetrieveNewsWithGivenSearchQuery_ReturnsAtMostFiveWithoutGov", function (done) {
  chai
    .request(server)
    .get(URI + "/news/search")
    .query({ q: "covid" })
    .end(function (err, res) {
      assert.equal(res.status, 200);

      const { news } = res.body;
      assert.isAtLeast(news.length, 0);
      assert.isAtMost(news.length, 5);

      assert.isFalse(news.some(gov));
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

// Test the endpoint again after fetching RSS news
test("RetrieveNews_ReturnsEightOfEachType", function (done) {
  chai
    .request(server)
    .get(URI + "/news")
    .end(function (err, res) {
      assert.equal(res.status, 200);

      const { generalNews, restaurantNews, officialGovNews } = res.body;
      assert.equal(generalNews.length, 8);
      assert.equal(restaurantNews.length, 8);
      assert.equal(officialGovNews.length, numOfficialInserted);
      done();
    });
});
