"use strict";

const express = require("express");
const router = express.Router();
const restaurantController = require("./restaurantController");

router.get("/", restaurantController.loadWebpage);
router.post("/api/v1/restaurant", restaurantController.insertRestaurant);

module.exports = router;
