import { axiosInstance } from './api'
import TokenService from './token.service'
import EventBus from '@/common/EventBus'

const setup = store => {
  axiosInstance.interceptors.request.use(
    config => {
      const token = TokenService.getLocalAccessToken()
      if (token) {
        config.headers['Authorization'] = 'Bearer ' + token
      }
      return config
    },
    error => {
      return Promise.reject(error)
    },
  )

  axiosInstance.interceptors.response.use(
    res => {
      return res
    },
    async err => {
      const originalConfig = err.config

      if (originalConfig.url !== '/login' && err.response) {
        if (err.response.status === 401 && !originalConfig._retry) {
          originalConfig._retry = true

          try {
            const rs = await axiosInstance.post('/refreshToken', {
              refreshToken: TokenService.getLocalRefreshToken(),
            })

            const { accessToken } = rs.data

            store.dispatch('auth/refreshToken', accessToken)
            TokenService.updateLocalAccessToken(accessToken)

            return axiosInstance(originalConfig)
          } catch (_error) {
            EventBus.dispatch('logout')
            return Promise.reject(_error)
          }
        }
      }

      return Promise.reject(err)
    },
  )
}

export default setup
