<template>
  <v-card>
    <v-card-title>
        Covid Testing History
    </v-card-title>

    <v-divider class="mt-1"></v-divider>

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
            <span>Add New Test</span>
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
        </div>
      </v-card-text>

    <v-data-table
      :headers="headers"
      :items="usreList"
      :search = "search"
      item-key="date"
      class="table-rounded"
      hide-default-footer
    >
    </v-data-table>
  </v-card>
</template>

<script>
import { mdiSquareEditOutline, mdiDotsVertical } from '@mdi/js'
import data from './covidtestingdatatable-data.js'

export default {
  setup() {

    return {
      dialog: false,
      search: '',
      headers: [
        { text: 'Date', value: 'date' },
        { text: 'Type of test (PCR/ART)', value: 'type' },
        { text: 'Result', value: 'result' },
      ],
      usreList: data,
      // icons
      icons: {
        mdiSquareEditOutline,
        mdiDotsVertical,
      },
    }
  },
}
</script>