<template>
  <v-card>
    <v-card-title>
        Employees 
      </v-card-title>
      <v-row class="px-2 ma-0">
        <!-- role filter -->
        <!-- <v-col
          cols="12"
          sm="4"
        >
          <v-select
            v-model="roleFilter"
            placeholder="Select Role"
            :items="roleOptions"
            item-text="title"
            item-value="value"
            outlined
            clearable
            dense
            hide-details
          ></v-select>
        </v-col> -->

        <!-- plan filter -->
        <!-- <v-col
          cols="12"
          sm="4"
        >
          <v-select
            v-model="planFilter"
            placeholder="Select Plan"
            :items="planOptions"
            item-text="title"
            item-value="value"
            outlined
            dense
            clearable
            hide-details
          ></v-select>
        </v-col> -->

        <!-- status filter -->
        <!-- <v-col
          cols="12"
          sm="4"
        >
          <v-select
            v-model="statusFilter"
            placeholder="Select Status"
            :items="statusOptions"
            item-text="title"
            item-value="value"
            outlined
            dense
            clearable
            hide-details
          ></v-select>
        </v-col> -->
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
          label = "Search"
        >
        </v-text-field>

        <v-spacer></v-spacer>
        

        <div class="d-flex align-center flex-wrap">
          <v-dialog
          v-model="dialog"
          width="500"
          >
          <template v-slot:activator="{ on, attrs }">
          <v-btn
            v-bind="attrs"
            v-on="on"
            color="primary"
            class="mb-4 me-3"
            @click.stop="isAddNewUserSidebarActive = !isAddNewUserSidebarActive"
          >
            <v-icon>{{ icons.mdiPlus }}</v-icon>
            <span>Add New User</span>
          </v-btn>
          </template>
          <v-card>
        <v-card-title class="text-h5 grey lighten-2">
          New User
        </v-card-title>

        <v-card-text>
          Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
        </v-card-text>

        <v-divider></v-divider>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            color="primary"
            text
            @click="dialog = false"
          >
            Cancel
          </v-btn>
          <v-btn
            color="primary"
            text
            @click="dialog = false"
          >
            Save
          </v-btn>
        </v-card-actions>
      </v-card>
        
    </v-dialog>

          <v-btn
            color="secondary"
            outlined
            class="mb-4"
          >
            <v-icon
              size="17"
              class="me-1"
            >
              {{ icons.mdiExportVariant }}
            </v-icon>
            <span>Export</span>
          </v-btn>
        </div>
      </v-card-text>

    <v-data-table
      :headers="headers"
      :items="usreList"
      :search = "search"
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

export default {
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

    // const planOptions = [
    //   { title: 'Basic', value: 'basic' },
    //   { title: 'Company', value: 'company' },
    //   { title: 'Enterprise', value: 'enterprise' },
    //   { title: 'Standard', value: 'standard' },
    // ]

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