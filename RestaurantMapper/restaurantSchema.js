"use strict";

const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const restaurantSchema = new mongoose.Schema(
  {
    name: {
      type: String,
      required: true,
    },
    location: {
      type: {
        type: String,
        enum: ["Point"],
        default: "Point",
        required: true,
      },
      coordinates: {
        type: [Number],
        required: true,
      },
    },
    description: {
      type: String,
      required: true,
    },
    region: [
      {
        type: Schema.Types.ObjectId,
        ref: "Case",
      },
    ],
  },
  {
    timestamps: true,
    minimize: false,
  }
);

module.exports = mongoose.model("Restaurant", restaurantSchema);
