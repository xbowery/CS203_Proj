import api from './api'

class UserService {
  async getUsers() {
    return api.get('users')
  }
  async getRestaurants() {
    return api.get('restaurants')
  }
  async getCtests(username) {
    var request =  'employee/' + username + '/ctests'
    return api.get(request)
  }
}

export default new UserService()
