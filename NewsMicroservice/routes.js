const express = require("express");
const router = express.Router();
const newsController = require("./newsController");

// Define the home page route
router.get("/news", function (req, res) {
  res.send("news page");
});

module.exports = router;
