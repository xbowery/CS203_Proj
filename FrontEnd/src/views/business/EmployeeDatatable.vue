<template>
  <v-card>
    <v-card-title> Employees </v-card-title>
    <v-row class="px-2 ma-0"> </v-row>

    <v-divider class="mt-4"></v-divider>

    <!-- actions -->
    <v-card-text class="d-flex align-center flex-wrap pb-0">
      <!-- search -->
      <v-text-field
        v-model="search"
        placeholder="Search"
        outlined
        hide-details
        dense
        class="user-search me-3 mb-4"
        label="Search"
      >
      </v-text-field>

      <v-spacer></v-spacer>
    </v-card-text>

    <v-data-table
      :headers="headers"
      :items="this.items"
      :search="search"
      item-key="full_name"
      class="table-rounded"
      hide-default-footer
    >
      <template v-slot:[`item.status`]="{ item }">
        <v-chip :color="getColor(item.employee.status)" dark>
          {{ item.employee.status }}
        </v-chip>
      </template>

      <template v-slot:[`item.actions`]="{ item }">
        <v-icon
          small
          outlined
          class="mr-3"
          @click="statusClick(item.username)"
          :disabled="disableButton(item.employee.status)"
        >
          Approve/ Reject</v-icon
        >

        <v-dialog v-model="dialogApprove" max-width="555px">
          <v-card>
            <v-card-title class="text-h5">Do you want to approve or reject this employee?</v-card-title>

            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="success" @click="approveEmployee()">Approve</v-btn>
              <v-spacer></v-spacer>
              <v-btn color="error" @click="rejectEmployee()">Reject</v-btn>
              <v-spacer></v-spacer>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </template>
    </v-data-table>
  </v-card>
</template>

<script>
import { mdiSquareEditOutline, mdiDotsVertical } from '@mdi/js'
import UserService from '@/services/user.service'

export default {
  data: () => ({
    dialogApprove: false,
    items: [],

    curUsername: '',
  }),
  props: {
    username: String,
  },

  async mounted() {
    try {
      const res = await UserService.getEmployees(this.username)
      this.items = res.data
      console.log(this.items)
    } catch (error) {
      console.error(error)
    }
  },

  methods: {
    statusClick(username) {
      this.curUsername = username
      this.dialogApprove = true
    },
    disableButton(status) {
      if (status == 'Pending') {
        return false
      }
      return true
    },
    getColor(status) {
      console.log(status)
      if (status == 'Pending') {
        return 'orange'
      } else if (status == 'Active') {
        return 'green'
      }
    },

    approveEmployee() {
      this.handleApproveEmployee()
      this.dialogApprove = false
      this.getEmployees()
    },

    rejectEmployee() {
      this.handleRejectEmployee()
      this.dialogApprove = false
      this.getEmployees()
    },

    async handleApproveEmployee() {
      try {
        const res = await UserService.approveEmployee(this.curUsername)
        console.log(res)
      } catch (error) {
        console.error(error)
      }
    },
    async handleRejectEmployee() {
      try {
        const res = await UserService.deleteEmployee(this.curUsername)
        console.log(res)
      } catch (error) {
        console.error(error)
      }
    },

    async getEmployees() {
      try {
        const res = await UserService.getEmployees(this.username)
        this.items = res.data
        console.log(this.items)
      } catch (error) {
        console.error(error)
      }
    },
  },

  setup() {
    return {
      dialog: false,
      search: '',
      headers: [
        { text: 'EMPLOYEE ID', value: 'id' },
        { text: 'FIRST NAME', value: 'firstName' },
        { text: 'LAST NAME', value: 'lastName' },
        { text: 'EMAIL', value: 'email' },
        { text: 'DESIGNATION', value: 'employee.designation' },
        { text: 'STATUS', value: 'employee.status' },
        { text: 'ACTIONS', value: 'actions', sortable: false },
      ],

      // icons
      icons: {
        mdiSquareEditOutline,
        mdiDotsVertical,
      },
    }
  },
}
</script>
