import '@/plugins/vue-composition-api'
import '@/styles/styles.scss'
import Vue from 'vue'
import App from './App.vue'
import vuetify from '@/plugins/vuetify'
import router from '@/router'
import store from '@/store'
import * as VeeValidate from 'vee-validate'

Vue.config.productionTip = false

Vue.use(VeeValidate)

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App),
}).$mount('#app')
