# News API backend microservice

Just a backend API built using [express.js](https://expressjs.com/), powered by the [News API](https://newsapi.org).

## Installation

```bash
npm install
npm start
```

## About

This is a project built for G2T4, CS203 project.

## API Documentation

The following endpoints are available and exposed for users to retrieve related, current and relevant news.

| Method | Endpoint                    | Description                                                                                 |
| ------ | --------------------------- | ------------------------------------------------------------------------------------------- |
| GET    | /api/v1/news                | Get the top 8 news each for the "Restaurant" related news and "General" news                |
| GET    | /api/v1/news/search?q=query | Top 5 news with either the title or content body containing the keyword in the query string |
