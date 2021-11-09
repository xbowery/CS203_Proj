<template>
  <v-row class="match-height">
    <v-col cols="12" md="4" sm="6">
      <dashboard-user :username="user.username"></dashboard-user>
    </v-col>
    <v-col cols="12" md="8" sm="6">
      <crowd-level :username="user.username"></crowd-level>
    </v-col>

    <v-col cols="12" sm="6" md="8">
      <dashboard-daily-overview :username="user.username"></dashboard-daily-overview>
    </v-col>

    <v-col cols="12" md="4">
      <v-row class="match-height">
        <v-col cols="12" sm="6">
          <statistics-card-vertical
            :color="totalNumEmployees.color"
            :icon="totalNumEmployees.icon"
            :statistics="employee_count"
            :stat-title="totalNumEmployees.statTitle"
            :subtitle="totalNumEmployees.subtitle"
          ></statistics-card-vertical>
        </v-col>
        <v-col cols="12" sm="6">
          <statistics-card-vertical
            :color="employeesPending.color"
            :icon="employeesPending.icon"
            :statistics="pending_count"
            :stat-title="employeesPending.statTitle"
            :subtitle="employeesPending.subtitle"
          ></statistics-card-vertical>
        </v-col>
        <v-col cols="12" sm="6">
          <statistics-card-vertical
            :change="employeesPostive.change"
            :color="employeesPostive.color"
            :icon="employeesPostive.icon"
            :statistics="positive_count"
            :stat-title="employeesPostive.statTitle"
            :subtitle="employeesPostive.subtitle"
          ></statistics-card-vertical>
        </v-col>

        <v-col cols="12" sm="6">
          <statistics-card-vertical
            :color="employeesNegative.color"
            :icon="employeesNegative.icon"
            :statistics="negative_count"
            :stat-title="employeesNegative.statTitle"
            :subtitle="employeesNegative.subtitle"
          ></statistics-card-vertical>
        </v-col>
      </v-row>
    </v-col>

    <v-col cols="12">
      <dashboard-datatable :username="user.username"></dashboard-datatable>
    </v-col>
  </v-row>
</template>

<script>
// eslint-disable-next-line object-curly-newline
import { mdiAccountGroup, mdiNeedle, mdiHospitalBoxOutline, mdiThumbUp } from '@mdi/js'
import StatisticsCardVertical from '@/components/statistics-card/StatisticsCardVertical.vue'

// demos
import DashboardUser from './DashboardUser.vue'
import CrowdLevel from './CrowdLevel.vue'
import DashboardDailyOverview from './DashboardDailyOverview.vue'
import DashboardDatatable from './DashboardDatatable.vue'
import { mapGetters } from 'vuex'
import UserService from '@/services/user.service'

export default {
  components: {
    StatisticsCardVertical,
    DashboardUser,
    CrowdLevel,
    DashboardDailyOverview,
    DashboardDatatable,
  },

  computed: {
    ...mapGetters({
      user: 'auth/user',
    }),
  },

  data() {
    return {
      items: [],
      employee_count: '',
      pending_count: '',
      positive_count: '',
      negative_count: '',
    }
  },

  async mounted() {
    try {
      console.log(this.user.username)
      const res = await UserService.getEmployeesCtests(this.user.username)
      this.items = res.data
      this.employee_count = this.items.length.toString(10)

      this.items.forEach(item => {
        if (item.result == 'Pending') this.pending_count += 1
        else if (item.result == 'Positive') this.positive_count += 1
        else if (item.result == 'Negative') this.negative_count += 1
      })
    } catch (error) {
      console.error(error)
    }
  },

  setup() {
    /*providing statistics on status of employees */

    //total number of employees
    const totalNumEmployees = {
      statTitle: 'Num of Employees',
      icon: mdiAccountGroup,
      color: 'success',
      subtitle: 'in total',
    }

    //number of employees with pending test for COVID-19
    const employeesPending = {
      statTitle: 'Num of employees',
      icon: mdiHospitalBoxOutline,
      color: 'secondary',
      subtitle: 'with pending test(s)',
    }

    //number of employees tested positive for COVID-19
    const employeesPostive = {
      statTitle: 'Num of employees',
      icon: mdiNeedle,
      color: 'primary',
      subtitle: 'tested positive',
    }

    //number of employees tested negative for COVID-19
    const employeesNegative = {
      statTitle: 'Num of employees',
      icon: mdiThumbUp,
      color: 'warning',
      subtitle: 'tested negative',
    }

    return {
      totalNumEmployees,
      employeesPending,
      employeesPostive,
      employeesNegative,
    }
  },
}
</script>
