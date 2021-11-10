<template>
  <v-card flat class="mt-5">
    <validation-observer v-slot="{ invalid }">
      <v-form @submit.prevent="handleChangePassword">
        <div class="px-3">
          <v-card-text class="pt-5">
            <v-row>
              <v-col cols="12" sm="8" md="6">
                <v-alert v-if="message" dense type="error"> {{ message }}</v-alert>
                <!-- current password -->
                <validation-provider name="Current Password" rules="required|min:8" v-slot="{ errors }">
                  <v-text-field
                    v-model="currentPassword"
                    :type="isCurrentPasswordVisible ? 'text' : 'password'"
                    :append-icon="isCurrentPasswordVisible ? icons.mdiEyeOffOutline : icons.mdiEyeOutline"
                    label="Current Password"
                    outlined
                    @click:append="isCurrentPasswordVisible = !isCurrentPasswordVisible"
                    :disabled="loading"
                    :error-messages="errors[0]"
                    dense
                  ></v-text-field>
                </validation-provider>

                <!-- new password -->
                <validation-observer>
                  <!-- Commented out strong password validation for dev purposes -->
                  <!-- <validation-provider name="Password" rules="required|password" v-slot="{ errors }"> -->
                  <validation-provider name="Password" rules="required" v-slot="{ errors }">
                    <v-text-field
                      v-model="newPassword"
                      :type="isNewPasswordVisible ? 'text' : 'password'"
                      :append-icon="isNewPasswordVisible ? icons.mdiEyeOffOutline : icons.mdiEyeOutline"
                      label="New Password"
                      outlined
                      hint="Make sure it's at least 8 characters."
                      persistent-hint
                      @click:append="isNewPasswordVisible = !isNewPasswordVisible"
                      :disabled="loading"
                      :error-messages="errors[0]"
                      dense
                    ></v-text-field>
                  </validation-provider>

                  <!-- confirm password -->
                  <validation-provider
                    name="Confirm Password"
                    rules="cfmPassword:@Password|required"
                    v-slot="{ errors }"
                  >
                    <v-text-field
                      v-model="cfmPassword"
                      :type="isCPasswordVisible ? 'text' : 'password'"
                      :append-icon="isCPasswordVisible ? icons.mdiEyeOffOutline : icons.mdiEyeOutline"
                      label="Confirm New Password"
                      outlined
                      class="mt-3"
                      @click:append="isCPasswordVisible = !isCPasswordVisible"
                      :disabled="loading"
                      :error-messages="errors[0]"
                      dense
                    ></v-text-field>
                  </validation-provider>
                </validation-observer>
              </v-col>

              <v-col cols="12" sm="4" md="6" class="d-none d-sm-flex justify-center position-relative">
                <v-img
                  contain
                  max-width="170"
                  src="@/assets/images/3d-characters/pose-m-1.png"
                  class="security-character"
                ></v-img>
              </v-col>
            </v-row>
          </v-card-text>
        </div>

        <!-- divider -->
        <v-divider></v-divider>

        <div class="pa-3">
          <v-card-text class="d-inline-flex align-center mb-1 mt-3">
            <!-- loading animation -->
            <v-progress-circular v-if="loading" :size="40" color="primary" indeterminate></v-progress-circular>

            <!-- action buttons -->
            <div v-else>
              <v-btn color="primary" class="me-3" type="submit" :disabled="loading || invalid"> Save changes </v-btn>
              <v-btn color="secondary" outlined :disabled="loading"> Cancel </v-btn>
            </div>
          </v-card-text>
        </div>
      </v-form>
    </validation-observer>
  </v-card>
</template>

<script>
import { mdiKeyOutline, mdiLockOpenOutline, mdiEyeOffOutline, mdiEyeOutline } from '@mdi/js'
import { ref } from '@vue/composition-api'
import UserService from '@/services/user.service'
import { ValidationProvider, ValidationObserver } from 'vee-validate'
import '@/validators'

export default {
  components: { ValidationProvider, ValidationObserver },
  setup() {
    const isCurrentPasswordVisible = ref(false)
    const isNewPasswordVisible = ref(false)
    const isCPasswordVisible = ref(false)
    const currentPassword = ref('')
    const newPassword = ref('')
    const cfmPassword = ref('')
    const message = ref('')
    const loading = ref(false)

    return {
      isCurrentPasswordVisible,
      isNewPasswordVisible,
      currentPassword,
      isCPasswordVisible,
      newPassword,
      cfmPassword,
      message,
      loading,
      icons: {
        mdiKeyOutline,
        mdiLockOpenOutline,
        mdiEyeOffOutline,
        mdiEyeOutline,
      },
    }
  },
  methods: {
    async handleChangePassword() {
      this.loading = true
      this.message = ''
      try {
        await UserService.changePassword({
          currentPassword: this.currentPassword,
          newPassword: this.newPassword,
          cfmPassword: this.cfmPassword,
        })
      } catch (error) {
        this.message = error.response?.data?.message || error.message || error.toString()
      } finally {
        this.loading = false
      }
    },
  },
}
</script>

<style lang="scss" scoped>
.two-factor-auth {
  max-width: 25rem;
}
.security-character {
  position: absolute;
  bottom: -0.5rem;
}
</style>
