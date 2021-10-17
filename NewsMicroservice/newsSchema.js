const mongoose = require("mongoose");

const newsSchema = new mongoose.Schema(
  {
    source: {
      type: String,
      required: true,
    },
    title: {
      type: String,
      required: true,
    },
    content: {
      type: String,
      required: true,
    },
    url: {
      type: String,
      required: true,
    },
    imageUrl: {
      type: String,
      required: true,
    },
  },
  {
    timestamps: true,
    minimize: false,
  }
);

module.exports = mongoose.model("News", newsSchema);
