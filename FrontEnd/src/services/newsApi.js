import axios from 'axios'

const newsApiInstance = axios.create({
  baseURL: process.env.NEWS_MICROSERVICE_ENDPOINT,
})

export default newsApiInstance
