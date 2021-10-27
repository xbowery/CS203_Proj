import api from './api'
import newsapi from '@/services/newsApi'

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
  async updateCtest(username, ctestId, ctest) {
    const request = `/employee/${username}/ctests/${ctestId}`
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
        name: restaurant.name,
        location: restaurant.location,
        cuisine: restaurant.cuisine,
        description: restaurant.description,
        maxCapacity: restaurant.maxCapacity,
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
      `restaurants/${restaurant.name}/${restaurant.location}`,
      {
        name: restaurant.name,
        location: restaurant.location,
        cuisine: restaurant.cuisine,
        description: restaurant.description,
        maxCapacity: restaurant.maxCapacity,
      },
      {
        headers: {
          'Content-Type': 'application/json',
        },
      },
    )
  }
  deleteRestaurant(restaurant) {
    return api.delete(`restaurants/${restaurant.name}/${restaurant.location}`, {
      headers: {
        'Content-Type': 'application/json',
      },
    })
  }
  async getMeasures() {
    return api.get('measures')
  }
  async getEmployees(username) {
    return api.get('employees/' + username)
  }

  async getNews(newsType) {
    return newsapi.get(`news/${newsType}`)
  }

  async getNewsWithFilters(newsType, queryString) {
    let url = 'news/search/?'
    url += newsType ? `type=${newsType}` : ''
    url += queryString ? `q=${queryString}` : ''

    return newsapi.get(url)
  }
}

export default new UserService()
