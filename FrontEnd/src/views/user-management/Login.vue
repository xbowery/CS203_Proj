<template>
  <div class="auth-wrapper auth-v1">
    <div class="auth-inner">
      <v-card class="auth-card">
        <!-- logo -->
        <v-card-title class="d-flex align-center justify-center py-7">
          <router-link to="/" class="d-flex align-center">
            <v-img
              :src="require('@/assets/images/logos/logo.png')"
              max-height="30px"
              max-width="30px"
              alt="logo"
              contain
              class="me-3"
            ></v-img>

            <h2 class="text-2xl font-weight-semibold">Swisshack</h2>
          </router-link>
        </v-card-title>

        <!-- title -->
        <v-card-text>
          <p class="text-2xl font-weight-semibold text--primary mb-2">Welcome to Swisshack!</p>
          <p class="mb-2">Sign-in to experience the new normal!</p>
        </v-card-text>

        <!-- login form -->
        <v-card-text>
          <v-alert elevation="2" type="error" v-if="this.message">{{ this.message }}</v-alert>
          <validation-observer v-slot="{ handleSubmit, invalid }">
            <v-form @submit.prevent="handleSubmit(handleLogin)">
              <!-- Commented out email validation for dev purposes -->
              <!-- <validation-provider name="Username" rules="required" v-slot="{ errors }"> -->
              <validation-provider name="Username" rules="required" v-slot="{ errors }">
                <v-text-field
                  v-model="user.username"
                  outlined
                  label="Username"
                  placeholder="johndoe1"
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
                <a href="/ForgotPassword" class="mt-1"> Forgot Password? </a>
              </div>

              <v-btn block color="primary" class="mt-6" :disabled="invalid" :loading="loading" type="submit">
                Login
              </v-btn>
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
    <img class="auth-mask-bg" height="100%" :src="require('@/assets/images/misc/f&b background.jpg')" />
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
  methods: {
    async handleLogin() {
      this.loading = true
      if (this.user.username && this.user.password) {
        try {
          await this.$store.dispatch('auth/login', this.user)
          this.$router.push('/')
        } catch (error) {
          this.message = error.response?.data?.message || error.message || error.toString()
        } finally {
          this.loading = false
        }
      }
    },
  },
  setup() {
    const user = new User('', '')
    const isPasswordVisible = ref(false)
    const message = null
    const loading = ref(false)
    return {
      user,
      isPasswordVisible,
      icons: {
        mdiEyeOutline,
        mdiEyeOffOutline,
      },
      message,
      loading,
    }
  },
}
</script>

<style lang="scss">
@import '~@/plugins/vuetify/default-preset/preset/pages/auth.scss';
</style>
