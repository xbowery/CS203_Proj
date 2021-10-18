const express = require("express");
const router = express.Router();
const newsController = require("./newsController");

router.get("/news", newsController.getNews);
router.get("/news/search", newsController.searchNews);
// For dev use only
router.get("/dev", newsController.fetchNews);

module.exports = router;
