<template>
  <div class="auth-wrapper auth-v1">
    <div class="auth-inner">
      <v-card class="auth-card">
        <!-- logo and title -->
        <v-card-title class="d-flex align-center justify-center py-7">
          <router-link to="/" class="d-flex align-center">
            <v-img :src="appLogo" max-height="30px" max-width="30px" alt="logo" contain class="me-3"></v-img>

            <h2 class="text-2xl font-weight-semibold">
              {{ appName }}
            </h2>
          </router-link>
        </v-card-title>

        <div v-if="validToken">
          <v-img src="@/assets/images/3d-characters/launching-soon.png" height="300" />
          <v-card-text>
            <p class="text-2xl font-weight-semibold text--primary mb-2">Great! Everything is ready.</p>
            <!-- <p class="mb-2">Your registration is complete</p> -->
          </v-card-text>
          <v-card-text> Your registration is complete. </v-card-text>

          <!-- back to login -->
          <v-card-actions class="d-flex justify-center align-center">
            <router-link :to="{ name: 'login' }" class="d-flex align-center text-sm">
              <v-icon size="24" color="primary">
                {{ icons.mdiChevronLeft }}
              </v-icon>
              <span>Back to login</span>
            </router-link>
          </v-card-actions>
        </div>

        <div v-if="invalidToken">
          <v-img src="@/assets/images/3d-characters/launching-soon.png" height="300" />
          <v-card-text>
            <p class="text-2xl font-weight-semibold text--primary mb-2">Oh no!</p>
            <!-- <p class="mb-2">Your registration is complete</p> -->
          </v-card-text>
          <v-card-text> Your token is invalid. </v-card-text>

          <!-- back to login -->
          <v-card-actions class="d-flex justify-center align-center">
            <router-link :to="{ name: 'login' }" class="d-flex align-center text-sm">
              <v-icon size="24" color="primary">
                {{ icons.mdiChevronLeft }}
              </v-icon>
              <span>Back to login</span>
            </router-link>
          </v-card-actions>
        </div>

        <div v-if="expiredToken">
          <v-img src="@/assets/images/3d-characters/launching-soon.png" height="300" />
          <v-card-text>
            <p class="text-2xl font-weight-semibold text--primary mb-2">Oh no!</p>
            <!-- <p class="mb-2">Your registration is complete</p> -->
          </v-card-text>
          <v-card-text> Your token has expired. Please check your email again for a new link. </v-card-text>

          <!-- back to login -->
          <v-card-actions class="d-flex justify-center align-center">
            <router-link :to="{ name: 'login' }" class="d-flex align-center text-sm">
              <v-icon size="24" color="primary">
                {{ icons.mdiChevronLeft }}
              </v-icon>
              <span>Back to login</span>
            </router-link>
          </v-card-actions>
        </div>
      </v-card>
    </div>

    <!-- background triangle shape  -->
    <img class="auth-mask-bg" height="100%" :src="require('@/assets/images/misc/f&b background.jpg')" />
  </div>
</template>

<script>
// eslint-disable-next-line object-curly-newline
import { mdiChevronLeft, mdiEyeOutline, mdiEyeOffOutline } from '@mdi/js'
import themeConfig from '/themeConfig'
import UserService from '@/services/user.service'

export default {
  async beforeCreate() {
    const token = this.$route.query.token
    const res = await UserService.getRegistrationConfirm(token)
    if (res['data'] == 'valid') {
      this.validToken = true
    } else if (res['data'] == 'invalidToken') {
      this.invalidToken = true
    } else if (res['data'] == 'expired') {
      this.expiredToken = true
    }
  },

  setup() {
    const validToken = false
    const invalidToken = false
    const expiredToken = false

    return {
      validToken,
      invalidToken,
      expiredToken,

      // themeConfig
      appName: themeConfig.app.name,
      appLogo: themeConfig.app.logo,

      icons: {
        mdiChevronLeft,
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
