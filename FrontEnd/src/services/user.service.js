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
  async postCtest(username, ctest) {
    var request =  'employee/' + username + '/ctests'
    return api.post(request, ctest)
  }
  async getRegistrationConfirm(token) {
    var request = 'registrationConfirm?token=' + token
    return api.get(request)
  }
  updateUser(user) {
    return api.put(
      `/updateUser/${user.username}`,
      {
        firstName: user.firstName,
        lastName: user.lastName,
        email: user.email,
        authorities: user.authorities,
      },
      {
        headers: {
          'Content-Type': 'application/json',
        },
      },
    )
  }
}

export default new UserService()
