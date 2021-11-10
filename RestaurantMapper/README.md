# Restaurant backend microservice

Just a backend API built using [express.js](https://expressjs.com/).

## Installation

```bash
npm install
npm start

# If you are in windows
npm start-windows
```

## About

This is a project built for G2T4, CS203 project.

## API Documentation

The following endpoints are available and exposed for users to retrieve related, current and relevant news.

The prefix for all the API URI is `/api/v1`.

| Method | Endpoint     | Body               | Description                       |
| ------ | ------------ | ------------------ | --------------------------------- |
| POST   | /restaurants | Restaurant in JSON | Need to have an API key to insert |

## Testing

To run the test suites, run the command `npm test`. Both unit and integration tests will run.

## Production

To run it in production mode, run the command `npm run prod`. Else, you may follow the instructions to spin up a docker instance.

```bash
docker build .
```
