const express = require("express");
const router = express.Router();
const newsController = require("./newsController");

router.get("/news", newsController.getNews);
router.get("/news/search", newsController.searchNews);

// For dev use only
if (process.env.NODE_ENV !== "prod") {
  router.get("/dev", newsController.devFetch);

  router.get("/rssFetch", newsController.rssFetch);
}

module.exports = router;
