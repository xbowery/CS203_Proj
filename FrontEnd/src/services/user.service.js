import api from './api'

class UserService {
  async getUsers() {
    return api.get('users')
  }
}

export default new UserService()
