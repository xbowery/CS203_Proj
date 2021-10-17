const express = require("express");
const app = express();

require("dotenv").config();
app.use(express.json());

const mongoose = require("mongoose");
mongoose.set("debug", true);

// Connect to the MongoDB interface and logs for current and future errors
mongoose.connect(process.env.MONGO_URI).then(
  () => {
    console.log("MongoDB server ready for use!");
  },
  (err) => {
    console.error(err);
    console.error("Something went wrong. Please try again.");
  }
);

mongoose.connection.on("error", (err) => {
  console.log(err);
});

// Not found middleware
app.use((req, res, next) => {
  return next({ status: 404, message: "not found" });
});

// Error Handling middleware
app.use((err, req, res, next) => {
  let errCode, errMessage;

  if (err.errors) {
    // mongoose validation error
    errCode = 400; // bad request
    const keys = Object.keys(err.errors);
    // report the first validation error
    errMessage = err.errors[keys[0]].message;
  } else {
    // generic or custom error
    errCode = err.status || 500;
    errMessage = err.message || "Internal Server Error";
  }
  res.status(errCode).type("txt").send(errMessage);
});

const listener = app.listen(process.env.PORT || 3001, () => {
  console.log("Your app is listening on port " + listener.address().port);
});
