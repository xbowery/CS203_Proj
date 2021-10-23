<template>
  <v-card>
    <v-card-title> Employees </v-card-title>
    <v-row class="px-2 ma-0">
    </v-row>

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
      :items="usreList"
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
      <template #[`item.status`]="{ item }">
        <v-chip small :color="statusColor[status[item.status]]" class="font-weight-medium">
          {{ status[item.status] }}
        </v-chip>
      </template>
    </v-data-table>
  </v-card>
</template>

<script>
import { mdiSquareEditOutline, mdiDotsVertical } from '@mdi/js'
import data from './employeedatatable-data.js'
import UserService from '@/services/user.service'

export default {
  data: () => ({
    items:[],
  }),
  props: {
    username: String,
  },

  async mounted(){
    try{
      const res = await UserService.getEmployees(this.username)
      this.items = res.data
      console.log(this.items)
    }catch (error) {
      console.error(error)
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
        { text: 'NAME', value: 'full_name' },
        { text: 'EMAIL', value: 'email' },
        { text: 'DESIGNATION', value: 'designation' },
        { text: 'STATUS', value: 'status' },
      ],
      usreList: data,
      status: {
        1: 'Inactive',
        2: 'Pending',
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
