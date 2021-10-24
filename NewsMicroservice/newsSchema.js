const mongoose = require("mongoose");
const defaultFilePhotoVirus =
  "https://onecms-res.cloudinary.com/image/upload/s--t_acuu19--/c_fill%2Cg_auto%2Ch_468%2Cw_830/f_auto%2Cq_auto/covid-vaccine-file-photo.jpg";

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
      unique: true,
    },
    imageUrl: {
      type: String,
      required: true,
      default: defaultFilePhotoVirus,
    },
    type: {
      type: String,
      required: true,
      enum: ["Regular", "Restaurant"],
      default: "Regular",
    },
  },
  {
    timestamps: true,
    minimize: false,
  }
);

module.exports = mongoose.model("News", newsSchema);
