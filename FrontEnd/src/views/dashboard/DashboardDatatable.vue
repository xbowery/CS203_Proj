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
      :items="this.processedItems"
      :search="search"
      item-key="id"
      class="table-rounded"
      hide-default-footer
    >
      <!-- name -->
      <!-- <template #[`item.full_name`]="{ item }">
        <div class="d-flex flex-column">
          <span class="d-block font-weight-semibold text--primary text-truncate">{{ item.firstName }}</span>
          <small>{{ item.post }}</small>
        </div>
      </template> -->

      <!-- <template #[`item.salary`]="{ item }">
        {{ `$${item.salary}` }}
      </template> -->
      <!-- status -->
      <!-- <template #[`item.status`]="{ item }">
        <v-chip small :color="statusColor[status[item.status]]" class="font-weight-medium">
          {{ status[item.status] }}
        </v-chip>
      </template> -->
    </v-data-table>
  </v-card>
</template>

<script>
import { mdiSquareEditOutline, mdiDotsVertical } from '@mdi/js'
import UserService from '@/services/user.service'

export default {
  data: () => ({
    items: [],
    processedItems: [],
    currentDate:'10',
  }),
  props: {
    username: String,
  },

  mounted() {
    this.getTableData()
  },

  methods:{
    async getTableData(){
      try {
        const res = await UserService.getEmployees(this.username)
        this.items = res.data
        this.items.forEach(async (user) => {
          if (user.employee.ctests.length > 0) {
            var latestDate = user.employee.ctests[0].date
            var latestResult = user.employee.ctests[0].result
            user.employee.ctests.forEach(ctest => {
              if (latestDate < ctest.date) {
                latestDate = ctest.date
                latestResult = ctest.result
              }
            })
          }
          // var curDate = ''
          // this.getNextDate(user.username).then(function(result){
          //   curDate = result
          // })
          var dict = {
            id: user.id,
            full_name: user.firstName + ' ' + user.lastName,
            isVaccinated: user.isVaccinated,
            latestCtestDate: latestDate,
            latestCtestResult: latestResult,
            next_covid_date: await this.getNextDate(user.username),
          }
          this.processedItems.push(dict)
        })
      } catch (error) {
        console.error(error)
      }
    },

    async getNextDate(username) {
      try {
        const res = await UserService.getNextCtest(username)
        console.log(res.data)
        return res.data
      } catch (error) {
        console.log(error)
      }
    },
    
  },

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
        { text: 'EMPLOYEE ID', value: 'id' },
        { text: 'FULL NAME ', value: 'full_name' },
        { text: 'VACCINATED?', value: 'isVaccinated' },
        { text: 'LATEST COVID TEST DATE', value: 'latestCtestDate' },
        { text: 'TEST RESULT', value: 'latestCtestResult' },
        { text: 'NEXT COVID TEST DATE', value: 'next_covid_date' },
      ],
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
