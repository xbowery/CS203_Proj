const express = require("express");
const router = express.Router();
const newsController = require("./newsController");

// Define the home page route
router.get("/news", newsController.getNews);

module.exports = router;
