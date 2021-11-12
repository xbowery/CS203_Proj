import axios from 'axios'

const instance = axios.create({
  baseURL: process.env.VUE_APP_RESTAURANT_MAPPER_MICROSERVICE_ENDPOINT,
})

export default instance
