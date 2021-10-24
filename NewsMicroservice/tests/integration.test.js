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

afterAll(async () => {
  return News.deleteMany({});
});

test("getInitialNews_ReturnsEmptyObject", function (done) {
  chai
    .request(server)
    .get(URI + "/news")
    .end(function (err, res) {
      assert.equal(res.status, 200);
      const { generalNews, restaurantNews } = res.body;
      assert.deepEqual(generalNews, []);
      assert.deepEqual(restaurantNews, []);
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

test("RetrieveNews_ReturnsEightOfEachType", function (done) {
  chai
    .request(server)
    .get(URI + "/news")
    .end(function (err, res) {
      assert.equal(res.status, 200);

      const { generalNews, restaurantNews } = res.body;
      assert.equal(generalNews.length, 8);
      assert.equal(restaurantNews.length, 8);
      done();
    });
});

test("RetrieveNewsWithEmptySearchQuery_ReturnsLatestFive", function (done) {
  chai
    .request(server)
    .get(URI + "/news/search")
    .end(function (err, res) {
      assert.equal(res.status, 200);

      const { news } = res.body;
      assert.equal(news.length, 5);
      done();
    });
});

test("RetrieveNewsWithGivenSearchQuery_ReturnsAtMostFive", function (done) {
  chai
    .request(server)
    .get(URI + "/news/search")
    .query({ q: "covid" })
    .end(function (err, res) {
      assert.equal(res.status, 200);

      const { news } = res.body;
      assert.isAtLeast(news.length, 0);
      assert.isAtMost(news.length, 5);
      done();
    });
});
