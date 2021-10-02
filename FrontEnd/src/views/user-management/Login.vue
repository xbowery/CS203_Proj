<template>
  <div class="auth-wrapper auth-v1">
    <div class="auth-inner">
      <v-card class="auth-card">
        <!-- logo -->
        <v-card-title class="d-flex align-center justify-center py-7">
          <router-link to="/" class="d-flex align-center">
            <v-img
              :src="require('@/assets/images/logos/logo.svg')"
              max-height="30px"
              max-width="30px"
              alt="logo"
              contain
              class="me-3"
            ></v-img>

            <h2 class="text-2xl font-weight-semibold">Covid Suisse</h2>
          </router-link>
        </v-card-title>

        <!-- title -->
        <v-card-text>
          <p class="text-2xl font-weight-semibold text--primary mb-2">Welcome to Covid Suisse!</p>
          <p class="mb-2">Sign-in to experience the new normal!</p>
        </v-card-text>

        <!-- login form -->
        <v-card-text>
          <v-alert elevation="2" type="error" v-if="this.message">{{ this.message }}</v-alert>
          <validation-observer v-slot="{ handleSubmit, invalid }">
            <v-form @submit.prevent="handleSubmit(handleLogin)">
              <validation-provider name="Email" rules="required" v-slot="{ errors }">
                <!-- <validation-provider name="Email" rules="required|email" v-slot="{ errors }"> -->
                <v-text-field
                  v-model="user.email"
                  outlined
                  label="Email"
                  placeholder="john@example.com"
                  hide-details="auto"
                  class="mb-3"
                  :error-messages="errors[0]"
                  requred
                ></v-text-field>
              </validation-provider>

              <validation-provider name="Password" rules="required" v-slot="{ errors }">
                <v-text-field
                  v-model="user.password"
                  outlined
                  :type="isPasswordVisible ? 'text' : 'password'"
                  label="Password"
                  placeholder="············"
                  :append-icon="isPasswordVisible ? icons.mdiEyeOffOutline : icons.mdiEyeOutline"
                  hide-details="auto"
                  @click:append="isPasswordVisible = !isPasswordVisible"
                  :error-messages="errors[0]"
                  required
                ></v-text-field>
              </validation-provider>

              <div class="d-flex align-center justify-space-between flex-wrap">
                <v-checkbox label="Remember Me" hide-details class="me-3 mt-1"> </v-checkbox>

                <!-- forgot link -->
                <a href="javascript:void(0)" class="mt-1"> Forgot Password? </a>
              </div>

              <v-btn block color="primary" class="mt-6" :disabled="invalid" type="submit"> Login </v-btn>
            </v-form>
          </validation-observer>
        </v-card-text>

        <!-- create new account  -->
        <v-card-text class="d-flex align-center justify-center flex-wrap mt-2">
          <span class="me-2"> New on our platform? </span>
          <router-link :to="{ name: 'register' }"> Create an account </router-link>
        </v-card-text>
      </v-card>
    </div>

    <!-- background triangle shape  -->
    <img
      class="auth-mask-bg"
      height="173"
      :src="require(`@/assets/images/misc/mask-${$vuetify.theme.dark ? 'dark' : 'light'}.png`)"
    />

    <!-- tree -->
    <v-img class="auth-tree" width="247" height="185" src="@/assets/images/misc/tree.png"></v-img>

    <!-- tree  -->
    <v-img class="auth-tree-3" width="377" height="289" src="@/assets/images/misc/tree-3.png"></v-img>
  </div>
</template>

<script>
// eslint-disable-next-line object-curly-newline
import { mdiEyeOutline, mdiEyeOffOutline } from '@mdi/js'
import { ref } from '@vue/composition-api'
import { ValidationProvider, ValidationObserver } from 'vee-validate'
import User from '@/model/user'
import '@/validators'

export default {
  components: { ValidationProvider, ValidationObserver },
  computed: {
    loggedIn() {
      return this.$store.state.auth.status.loggedIn
    },
  },
  created() {
    if (this.loggedIn) {
      this.$router.push('/')
    }
  },
  methods: {
    handleLogin() {
      if (this.user.email && this.user.password) {
        this.$store.dispatch('auth/login', this.user).then(
          () => {
            this.$router.push('/')
          },
          error => {
            this.message = (error.response && error.response.data && error.response.data.message) || error.toString()
          },
        )
      }
    },
  },
  setup() {
    const user = new User('', '')
    const isPasswordVisible = ref(false)
    const message = null
    return {
      user,
      isPasswordVisible,
      icons: {
        mdiEyeOutline,
        mdiEyeOffOutline,
      },
      message,
    }
  },
}
</script>

<style lang="scss">
@import '~@/plugins/vuetify/default-preset/preset/pages/auth.scss';
</style>
