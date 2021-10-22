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
          <p class="text-2xl font-weight-semibold text--primary mb-2">"New Normal" starts here 🚀</p>
          <p class="mb-2">Make your "Normal" easy and fun!</p>
        </v-card-text>

        <div class = "success" v-if="successful">
          <p>Success! Please check your email and click on the confirmation link to activate your account!</p>
        </div>


        <!-- login form -->
        <v-card-text>
          <v-alert elevation="2" type="error" v-if="this.message">{{ this.message }}</v-alert>
          <validation-observer v-slot="{ invalid }">
            <v-form @submit.prevent="handleRegister">
              <validation-provider name="Username " rules="required|min:8" v-slot="{ errors }">
                <v-text-field
                  v-model="user.username"
                  outlined
                  label="Username"
                  placeholder="JohnDoe"
                  hide-details="auto"
                  class="mb-3"
                  :error-messages="errors[0]"
                ></v-text-field>
              </validation-provider>

              <validation-provider name="Email" rules="required|email" v-slot="{ errors }">
                <v-text-field
                  v-model="user.email"
                  outlined
                  label="Email"
                  placeholder="john@example.com"
                  hide-details="auto"
                  class="mb-3"
                  :error-messages="errors[0]"
                ></v-text-field>
              </validation-provider>

              <validation-provider name="First Name" rules="required" v-slot="{ errors }">
                <v-text-field
                  v-model="user.firstName"
                  outlined
                  label="First Name"
                  placeholder="John"
                  hide-details="auto"
                  class="mb-3"
                  :error-messages="errors[0]"
                ></v-text-field>
              </validation-provider>

              <validation-provider name="Last Name" rules="required" v-slot="{ errors }">
                <v-text-field
                  v-model="user.lastName"
                  outlined
                  label="Last Name"
                  placeholder="Doe"
                  hide-details="auto"
                  class="mb-3"
                  :error-messages="errors[0]"
                ></v-text-field>
              </validation-provider>

              <validation-observer>
                <!-- Commented out strong password validation for dev purposes -->
                <!-- <validation-provider name="Password" rules="required|password" v-slot="{ errors }"> -->
                <validation-provider name="Password" rules="required" v-slot="{ errors }">
                  <v-text-field
                    v-model="user.password"
                    outlined
                    :type="isPasswordVisible ? 'text' : 'password'"
                    label="Password"
                    placeholder="············"
                    :append-icon="isPasswordVisible ? icons.mdiEyeOffOutline : icons.mdiEyeOutline"
                    hide-details="auto"
                    class="mb-3"
                    @click:append="isPasswordVisible = !isPasswordVisible"
                    :error-messages="errors[0]"
                  ></v-text-field>
                </validation-provider>

                <validation-provider name="Confirm Password" rules="cfmPassword:@Password|required" v-slot="{ errors }">
                  <v-text-field
                    v-model="cfmPassword"
                    outlined
                    :type="isPasswordVisible ? 'text' : 'password'"
                    label="Confirm Password"
                    placeholder="············"
                    :append-icon="isPasswordVisible ? icons.mdiEyeOffOutline : icons.mdiEyeOutline"
                    hide-details="auto"
                    @click:append="isPasswordVisible = !isPasswordVisible"
                    :error-messages="errors[0]"
                  ></v-text-field>
                </validation-provider>
              </validation-observer>

              <v-btn block color="primary" class="mt-6" type="submit" :disabled="invalid"> Sign Up </v-btn>
            </v-form>
          </validation-observer>
        </v-card-text>

        <!-- create new account  -->
        <v-card-text class="d-flex align-center justify-center flex-wrap mt-2">
          <span class="me-2"> Already have an account? </span>
          <router-link :to="{ name: 'login' }"> Sign in instead </router-link>
        </v-card-text>
      </v-card>
    </div>

    <!-- background triangle shape  -->
    <img
      class="auth-mask-bg"
      height="190"
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
import '@/validators'
import User from '@/model/user'

export default {
  components: { ValidationProvider, ValidationObserver },
  methods: {
    handleRegister() {
      // this.message = ''
      this.submitted = true

      this.$store.dispatch('auth/register', this.user).then(
        data => {
          this.message = data.message
          this.successful = true
        },
        error => {
          this.message = (error.response && error.response.data && error.response.data.message) || error.toString()
          this.successful = false
        },
      )
    },
  },
  setup() {
    const isPasswordVisible = ref(false)
    const user = new User('', '', '', '', '')
    const successful = false
    const submitted = false
    const cfmPassword = ref('')
    const message = ref('')
    return {
      isPasswordVisible,
      user,
      successful,
      submitted,
      cfmPassword,
      message,
      icons: {
        mdiEyeOutline,
        mdiEyeOffOutline,
      },
    }
  },
}
</script>

<style lang="scss">
@import '~@/plugins/vuetify/default-preset/preset/pages/auth.scss';
</style>
