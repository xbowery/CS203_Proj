import api from '@/services/api'

import TokenService from '@/services/token.service'

class AuthService {
  async login(user) {
    try {
      const response = await api.post(
        '/login',
        {
          username: user.email,
          password: user.password,
        },
        {
          headers: {
            'Content-Type': 'application/json',
          },
        },
      )

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
    return api.post(
      '/register',
      {
        username: user.username,
        firstName: user.firstName,
        lastName: user.lastName,
        email: user.email,
        password: user.password,
      },
      {
        headers: {
          'Content-Type': 'application/json',
        },
      },
    )
  }
}

export default new AuthService()
