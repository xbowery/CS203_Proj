const mongoose = require("mongoose");

const caseSchema = new mongoose.Schema(
  {
    region: {
      type: String,
      required: true,
    },
    numCase: Number,
  },
  {
    timestamps: true,
    minimize: false,
  }
);

module.exports = mongoose.model("Case", caseSchema);
