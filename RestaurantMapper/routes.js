"use strict";

const express = require("express");
const router = express.Router();
const restaurantController = require("./restaurantController");

router.get("/", restaurantController.loadWebpage);
router.get("/api/v1/restaurants", restaurantController.getRestaurants);
router.post("/api/v1/restaurants", restaurantController.insertRestaurant);

module.exports = router;
