import axios from 'axios'

// const API_URL =
const API_URL = process.env.VUE_APP_API_ENDPOINT + 'auth/'

class AuthService {
  async login(user) {
    try {
      const response = await axios.post(API_URL + 'login', {
        username: user.email,
        password: user.password,
      })
      if (response.data.accessToken) {
        localStorage.setItem('user', JSON.stringify(response.data))
        return Promise.resolve(response.data)
      }
      return Promise.reject(new Error('Malformed request'))
    } catch (error) {
      return Promise.reject(error)
    }
  }

  logout() {
    localStorage.removeItem('user')
  }

  register(user) {
    return axios.post(API_URL + 'signup', {
      username: user.username,
      email: user.email,
      password: user.password,
    })
  }
}

export default new AuthService()
