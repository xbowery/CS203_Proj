var express = require("express");
var router = express.Router();

// Define the home page route
router.get("/news", function (req, res) {
  res.send("news page");
});

module.exports = router;
