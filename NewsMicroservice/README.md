# News API backend microservice

Just a backend API built using [express.js](https://expressjs.com/), powered by the [News API](https://newsapi.org).

## Installation

```bash
npm install
npm start

# If you are in windows
npm start windows
```

## About

This is a project built for G2T4, CS203 project.

## API Documentation

The following endpoints are available and exposed for users to retrieve related, current and relevant news.

The prefix for all the API URI is `/api/v1`.

| Method | Endpoint    | Params                        | Description                                                                                                                                                                                                                                                                                                                                           |
| ------ | ----------- | ----------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| GET    | /news/all   | -                             | Get the top 8 news each for the "Restaurant" related news and "General" news as well as "Gov" official news                                                                                                                                                                                                                                           |
| GET    | /news       | q=SearchStr&#10;type=newsType | Top 5 news with either the title or content body containing the keyword in the query string. The 2 query parameters are optional. The default is 5 latest articles from non-Gov sources. If type is invalid, throw an error. If type is provided, the type of news will be queried ['Gov', 'General', 'Restaurant']. Else, it will default to non-Gov |
| GET    | /news/:type | type                          | Top 8 news of that specific type. For the available types, refer to the API above.                                                                                                                                                                                                                                                                    |

## Testing

To run the test suites, run the command `npm test`. Both unit and integration tests will run.

## Production

To run it in production mode, run the command `npm run prod`. Else, you may follow the instructions to spin up a docker instance.

```bash
# To start
docker-compose up -d --build

# To stop
docker-compose down
```
