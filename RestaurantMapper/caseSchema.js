"use strict";

const mongoose = require("mongoose");

const caseSchema = new mongoose.Schema(
  {
    name: {
      type: String,
      required: true,
    },
    location: 
    numCase: Number,
  },
  {
    timestamps: true,
    minimize: false,
  }
);

const polygonSchema = new mongoose.Schema({
  
})



module.exports = mongoose.model("Case", caseSchema);
