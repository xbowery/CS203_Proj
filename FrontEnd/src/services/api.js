import axios from 'axios'

const JSON_HEADER = {
  headers: {
    'Content-Type': 'application/json',
  },
}

const axiosInstance = axios.create({
  baseURL: process.env.VUE_APP_API_ENDPOINT,
})

const post = (url, data) => {
  return axiosInstance.post(url, data, JSON_HEADER)
}

const get = url => {
  return axiosInstance.get(url)
}

const del = url => {
  return axiosInstance.delete(url)
}

const put = (url, data) => {
  return axiosInstance.put(url, data, JSON_HEADER)
}

export { axiosInstance, post, get, del, put }
