import api from './api'

class UserService {
  async getUsers() {
    return api.get('users')
  }
  async getRestaurants() {
    return api.get('restaurants')
  }
  async getCrowdLevels(){
    return api.get('restaurants/crowdLevels')
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
  async getMeasures(){
    return api.get('measures')
  }
  async getEmployees(username){
    return api.get('employees/' + username)
  }
}

export default new UserService()
