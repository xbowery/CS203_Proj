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
      <!-- name -->
      <template #[`item.full_name`]="{ item }">
        <div class="d-flex flex-column">
          <span class="d-block font-weight-semibold text--primary text-truncate">{{ item.full_name }}</span>
          <small>{{ item.post }}</small>
        </div>
      </template>
      <template #[`item.salary`]="{ item }">
        {{ `$${item.salary}` }}
      </template>
      <!-- status -->
      <template #[`item.employee.status`]="{ item }">
        <v-icon small :color="statusColor[status[item.status]]" outlined class="mr-3" @click="statusClick()"> {{item.employee.status}} </v-icon>
      </template>
    
      <template v-slot:top> 
          <v-dialog v-model="dialogApprove" max-width="555px">
            <v-card>
              <v-card-title class="text-h5">Do you want to approve or reject this employee?</v-card-title>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="error" @click="approveEmployee()">Approve</v-btn>
                <v-spacer></v-spacer>       
                <v-btn color="success" @click="rejectEmployee()" >Reject</v-btn>
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
    statusClick(){
      this.dialogApprove = true
    },

    approveEmployee(){

    },
    rejectEmployee(){

    }
  },

  setup() {
    const statusColor = {
      /* eslint-disable key-spacing */
      Inactive: 'error',
      Active: 'success',
      Pending: 'warning',
      /* eslint-enable key-spacing */
    }
    const roleOptions = [
      { title: 'Admin', value: 'admin' },
      { title: 'Author', value: 'author' },
      { title: 'Editor', value: 'editor' },
      { title: 'Maintainer', value: 'maintainer' },
      { title: 'Subscriber', value: 'subscriber' },
    ]

    const statusOptions = [
      { title: 'Pending', value: 'pending' },
      { title: 'Active', value: 'active' },
      { title: 'Inactive', value: 'inactive' },
    ]

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
      ],
      status: {
        'Pending': 'Inactive',
        'Active': 'Pending',
        3: 'Active',
      },
      statusColor,
      statusOptions,
      roleOptions,

      // icons
      icons: {
        mdiSquareEditOutline,
        mdiDotsVertical,
      },
    }
  },
}
</script>
