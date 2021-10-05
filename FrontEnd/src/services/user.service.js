import axios from 'axios'

class UserService {
  getPublicContent() {
    return axios.get('all')
  }

  getUserBoard() {
    return axios.get('user')
  }

  getModeratorBoard() {
    return axios.get('mod')
  }

  getAdminBoard() {
    return axios.get('admin')
  }
}

export default new UserService()
