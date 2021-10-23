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

test("getInitialNewsReturnsEmptyObject", function (done) {
  chai
    .request(server)
    .get("/api/v1/news")
    .end(function (err, res) {
      assert.equal(res.status, 200);
      const { generalNews, restaurantNews } = res.body;
      assert.deepEqual(generalNews, []);
      assert.deepEqual(restaurantNews, []);
      done();
    });
});
