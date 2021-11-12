<template>
  <div class="auth-wrapper auth-v1">
    <div class="auth-inner">
      <v-card class="auth-card">
        <v-card-title class="d-flex align-center justify-center py-7">
          <router-link to="/" class="d-flex align-center">
            <v-img :src="appLogo" max-height="30px" max-width="30px" alt="logo" contain class="me-3"></v-img>

            <h2 class="text-2xl font-weight-semibold">
              {{ appName }}
            </h2>
          </router-link>
        </v-card-title>

        <v-card-text>
          <p class="text-2xl font-weight-semibold text--primary mb-2">Forgot Password? 🔒</p>
          <p class="mb-2">Enter your email and we'll send you instructions to reset your password</p>
        </v-card-text>

        <!-- login form -->
        <v-card-text>
          <v-alert elevation="2" type="error" v-if="this.message">{{ this.message }}</v-alert>
          <validation-observer v-slot="{ invalid }">
            <v-form @submit.prevent="handleForget">
              <validation-provider name="Email" rules="required|email" v-slot="{ errors }">
                <v-text-field
                  v-model="user.email"
                  outlined
                  label="Email"
                  placeholder="john@example.com"
                  hide-details
                  class="mb-4"
                  :error-messages="errors[0]"
                ></v-text-field>
              </validation-provider>

              <v-btn block color="primary" type="submit" :disabled="invalid"> Send reset link </v-btn>
            </v-form>
          </validation-observer>
        </v-card-text>

        <v-card-actions class="d-flex justify-center align-center">
          <router-link :to="{ name: 'login' }" class="d-flex align-center text-sm">
            <v-icon size="24" color="primary">
              {{ icons.mdiChevronLeft }}
            </v-icon>
            <span>Back to login</span>
          </router-link>
        </v-card-actions>
      </v-card>
    </div>

    <!-- background triangle shape  -->
    <img class="auth-mask-bg" height=100% :src="require('@/assets/images/misc/f&b background.jpg')" />

  </div>
</template>

<script>
import { mdiChevronLeft } from '@mdi/js'
import { ref } from '@vue/composition-api'
import { ValidationProvider, ValidationObserver } from 'vee-validate'
import themeConfig from '/themeConfig'
import User from '@/model/user'

export default {
  components: { ValidationProvider, ValidationObserver },
  setup() {
    const isPasswordVisible = ref(false)
    const user = new User('')

    return {
      isPasswordVisible,
      user,

      // themeConfig
      appName: themeConfig.app.name,
      appLogo: themeConfig.app.logo,

      icons: {
        mdiChevronLeft,
      },
    }
  },
  methods: {
    async handleForget() {
      this.loading = true
      if (this.user.email) {
        try {
          await this.$store.dispatch('auth/forgetPassword', this.user.email)
          // this.$router.push('/')
        } catch (error) {
          this.message = error.response?.data?.message || error.message || error.toString()
        } finally {
          this.loading = false
        }
      }
    },
  },
}
</script>

<style lang="scss">
@import '~@/plugins/vuetify/default-preset/preset/pages/auth.scss';
</style>
