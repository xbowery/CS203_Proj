const express = require("express");
const router = express.Router();
const newsController = require("./newsController");

// all endpoint will come before the :type because it might be double matched
router.get("/news", newsController.searchNews);
router.get("/news/all", newsController.getNews);
router.get("/news/:type", newsController.getNewsWithType);

// For dev use only
if (process.env.NODE_ENV !== "prod") {
  router.get("/dev", newsController.devFetch);
  router.get("/rssFetch", newsController.rssFetch);
}

module.exports = router;
