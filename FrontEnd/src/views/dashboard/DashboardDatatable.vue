<template>
  <v-card>
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
import data from './datatable-data'

export default {
  setup() {
    const statusColor = {
      /* eslint-disable key-spacing */
      Positive: 'error',
      Negative: 'success',
      Pending: 'warning',
      /* eslint-enable key-spacing */
    }

    return {
      search: '',
      headers: [
        { text: 'NAME', value: 'full_name' },
        { text: 'VACCINATED?', value: 'vaccination_status' },
        { text: 'LATEST COVID TEST DATE', value: 'covid_date' },
        { text: 'TEST RESULT', value: 'status' },
        { text: 'NEXT COVID TEST DATE', value: 'next_covid_date' },
      ],
      usreList: data,
      status: {
        1: 'Postive',
        2: 'Pending',
        3: 'Negative',
      },
      statusColor,

      // icons
      icons: {
        mdiSquareEditOutline,
        mdiDotsVertical,
      },
    }
  },
}
</script>
