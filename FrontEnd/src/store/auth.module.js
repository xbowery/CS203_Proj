import AuthService from '@/services/auth.service'

const user = JSON.parse(localStorage.getItem('user'))
const initialState = user ? { status: { loggedIn: true }, user } : { status: { loggedIn: false }, user: null }

export const auth = {
  namespaced: true,
  state: initialState,
  getters: {
    user: state => state.user,
  },
  actions: {
    login({ commit }, user) {
      return AuthService.login(user).then(
        user => {
          user.password = null
          commit('loginSuccess', user)
          return Promise.resolve(user)
        },
        error => {
          commit('loginFailure')
          return Promise.reject(error)
        },
      )
    },
    logout({ commit }) {
      AuthService.logout()
      commit('logout')
    },
    register({ commit }, user) {
      return AuthService.register(user).then(
        response => {
          commit('registerSuccess')
          return Promise.resolve(response.data)
        },
        error => {
          commit('registerFailure')
          return Promise.reject(error)
        },
      )
    },
    forgetPassword({ commit }, email) {
      return AuthService.forgetPassword(email).then(
        response => {
          commit('forgetPasswordSuccess')
          return Promise.resolve(response.data)
        },
        error => {
          commit('forgetPasswordFailure')
          return Promise.reject(error)
        },
      )
    },
    refreshToken({ commit }, accessToken) {
      commit('refreshToken', accessToken)
    },
  },
  mutations: {
    loginSuccess(state, user) {
      state.status.loggedIn = true
      state.user = user
    },
    loginFailure(state) {
      state.status.loggedIn = false
      state.user = null
    },
    logout(state) {
      state.status.loggedIn = false
      state.user = null
    },
    registerSuccess(state) {
      state.status.loggedIn = false
    },
    registerFailure(state) {
      state.status.loggedIn = false
    },
    forgetPasswordSuccess(state) {
      state.status.loggedIn = false
    },
    forgetPasswordFailure(state) {
      state.status.loggedIn = false
    },
    refreshToken(state, accessToken) {
      state.status.loggedIn = true
      state.user = { ...state.user, accessToken: accessToken }
    },
  },
}
