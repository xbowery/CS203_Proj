const express = require("express");
const app = express();
const cors = require("cors");
const morgan = require("morgan");
const router = require("./routes");

const mongoose = require("mongoose");

// Set DEV tools
if (process.env.NODE_ENV !== "test") {
  mongoose.set("debug", true);
  app.use(morgan("tiny"));
}

// Connect to the MongoDB interface and logs for current and future errors
mongoose
  .connect(
    process.env.NODE_ENV === "test" ? process.env.DB_TEST : process.env.DB
  )
  .then(
    () => {
      console.log("MongoDB server ready for use!");
    },
    (err) => {
      console.error(err);
      console.error("Something went wrong. Please try again.");
    }
  );

app.use(cors());

app.use(express.urlencoded({ extended: false }));
app.use(express.json());

//Routes with the default path added
app.use("/api/v1", router);

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
    errCode = 400;
    const keys = Object.keys(err.errors);
    // report the first validation error
    errMessage = err.errors[keys[0]].message;
  } else {
    // generic or custom error
    errCode = err.status || 500;
    errMessage = err.message || "Internal Server Error";
  }
  res.status(errCode).json({ error: errMessage });
});

const listener = app.listen(process.env.PORT || 3001, () => {
  console.log("Your app is listening on port " + listener.address().port);
});

module.exports = app;
