import api from './api'

const JSON_HEADER = {
  headers: {
    'Content-Type': 'application/json',
  },
}

class UserService {
  getUsers() {
    return api.get('users')
  }
  getRestaurants() {
    return api.get('restaurants')
  }
  getRestaurant(username) {
    const request = `restaurants/user/${username}`
    return api.get(request)
  }
  getCrowdLevels() {
    return api.get('restaurants/crowdLevels')
  }
  getCrowdLevel(username) {
    const request = `restaurants/${username}/crowdLevel`
    return api.get(request)
  }
  postCrowdLevel(id, crowdLevel) {
    const request = `restaurants/${id}/crowdLevel`
    return api.post(request, crowdLevel)
  }
  updateCrowdLevel(restaurantId, crowdLevelId, crowdLevel) {
    const request = `/restaurants/${restaurantId}/crowdLevel/${crowdLevelId}`
    return api.put(request, crowdLevel)
  }
  getCtests() {
    const request = `employee/ctests`
    return api.get(request)
  }
  postCtest(ctest) {
    const request = `employee/ctests`
    return api.post(request, ctest)
  }
  getRegistrationConfirm(token) {
    const request = `registrationConfirm?token=${token}`
    return api.get(request)
  }
  deleteCtest(ctestId) {
    const request = `/employee/ctests/${ctestId}`
    return api.delete(request)
  }
  updateCtest(ctestId, ctest) {
    const request = `/employee/ctests/${ctestId}`
    return api.put(request, ctest)
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
      JSON_HEADER,
    )
  }
  deleteUser(user) {
    return api.delete(`users/${user.username}`, JSON_HEADER)
  }
  postRestaurant(restaurant) {
    return api.post(
      'restaurants',
      {
        ...restaurant,
      },
      JSON_HEADER,
    )
  }
  updateRestaurant(restaurant) {
    return api.put(
      `restaurants/${restaurant.id}`,
      {
        ...restaurant,
      },
      JSON_HEADER,
    )
  }
  deleteRestaurant(id) {
    return api.delete(`restaurants/${id}`)
  }
  getMeasures() {
    return api.get('measures')
  }
  getEmployees(username) {
    return api.get('employees/' + username)
  }
  updateMeasure(measure) {
    return api.put(
      'measures',
      {
        ...measure,
      },
      JSON_HEADER,
    )
  }
}

export default new UserService()
