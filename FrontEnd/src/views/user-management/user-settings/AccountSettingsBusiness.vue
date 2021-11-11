<template>
  <v-card flat class="pa-3 mt-2">
    <v-card-text>
      <v-form class="multi-col-validation mt-6" @submit.prevent="submit">
        <v-row>
          <v-col md="6" cols="12">
            <v-text-field v-model="restaurantId" label="Restaurant ID" dense outlined></v-text-field>
          </v-col>

          <v-col md="6" cols="12">
            <v-text-field v-model="designation" label="Designation" dense outlined></v-text-field>
          </v-col>

          <v-col cols="12">
            <v-btn color="primary" class="me-3 mt-4" @click="submit"> Register </v-btn>
            <v-btn color="secondary" outlined class="mt-4" type="reset" @click.prevent="resetForm"> Cancel </v-btn>
          </v-col>
        </v-row>
      </v-form>
    </v-card-text>
    <p>{{ message }}</p>
  </v-card>
</template>
<script>
import { mapGetters } from 'vuex'
import UserService from '@/services/user.service'

export default {
  data() {
    return {
      restaurantId: 0,
      designation: '',
      message: '',
    }
  },

  computed: {
    ...mapGetters({
      user: 'auth/user',
    }),
  },

  methods: {
    submit() {
      this.handlePostEmployee()
      console.log(this.user)
    },

    async handlePostEmployee() {
      try {
        console.log(this.user)
        const res = await UserService.postEmployee(this.restaurantId, this.designation)
        console.log(res)
        this.message = 'Request sent to Business succesfully! Please wait for the business to confirm your request.'
      } catch (error) {
        console.log(error)
        this.message = 'You have already sent a request. Please wait for the Business to confirm your request'
      }
    },
  },
}
</script>
