const mongoose = require("mongoose");

const newsSchema = new mongoose.Schema({});

module.exports = mongoose.model("News", newsSchema);
