// import axios from 'axios'
import api from './api'

class UserService {
  // getPublicContent() {
  //   return axios.get('all')
  // }

  // getUserBoard() {
  //   return axios.get('user')
  // }

  // getModeratorBoard() {
  //   return axios.get('mod')
  // }

  // getAdminBoard() {
  //   return axios.get('admin')
  // }
  async getUsers() {
    return api.get('users')
  }
}

export default new UserService()
