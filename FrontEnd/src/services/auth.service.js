import { post } from './api'

import TokenService from '@/services/token.service'

class AuthService {
  async login(user) {
    try {
      const response = await post('/login', {
        username: user.username,
        password: user.password,
      })

      if (response.data.accessToken) {
        TokenService.setUser(response.data)
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
    return post('/register', {
      username: user.username,
      firstName: user.firstName,
      lastName: user.lastName,
      email: user.email,
      password: user.password,
    })
  }

  forgetPassword(email) {
    return post('/forgotPassword', {
      email,
    })
  }
}

export default new AuthService()
