import api from './api'

class UserService {
  async getUsers() {
    return api.get('users')
  }
  async getRestaurants() {
    return api.get('restaurants')
  }
  async getCrowdLevels() {
    return api.get('restaurants/crowdLevels')
  }
  async getCrowdLevel(id) {
    var request = 'restaurants/' + id + '/crowdLevel'
    return api.get(request)
  }
  async postCrowdLevel(id, crowdLevel) {
    var request = 'restaurants/' + id + '/crowdLevel'
    return api.post(request, crowdLevel)
  }
  async getCtests(username) {
    const request = `employee/${username}/ctests`
    return api.get(request)
  }
  async postCtest(username, ctest) {
    const request = `employee/${username}/ctests`
    return api.post(request, ctest)
  }
  async getRegistrationConfirm(token) {
    const request = `registrationConfirm?token=${token}`
    return api.get(request)
  }
  async deleteCtest(username, ctestId) {
    const request = `/employee/${username}/ctests/${ctestId}`
    return api.delete(request)
  }
  updateUser(user) {
    return api.put(
      `/users/${user.username}`,
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
  deleteUser(user) {
    return api.delete(`users/${user.username}`, {
      headers: {
        'Content-Type': 'application/json',
      },
    })
  }
  postRestaurant(restaurant) {
    return api.post(
      'restaurants',
      {
        ...restaurant,
      },
      {
        headers: {
          'Content-Type': 'application/json',
        },
      },
    )
  }
  updateRestaurant(restaurant) {
    return api.put(
      `restaurants/${restaurant.id}`,
      {
        ...restaurant,
      },
      {
        headers: {
          'Content-Type': 'application/json',
        },
      },
    )
  }
  deleteRestaurant(id) {
    return api.delete(`restaurants/${id}`)
  }
  async getMeasures() {
    return api.get('measures')
  }
  async getEmployees(username) {
    return api.get('employees/' + username)
  }
}

export default new UserService()
