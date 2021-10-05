import api from "@/services/api";

import TokenService from '@/services/token.service'

class AuthService {
  async login(user) {
    try {
      const response = await api.post('/login', {
        username: user.email,
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
    return api.post('/signup', {
      username: user.username,
      email: user.email,
      password: user.password,
    })
  }
}

export default new AuthService()
