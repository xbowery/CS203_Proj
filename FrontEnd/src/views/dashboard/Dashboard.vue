<template>
  <v-row>
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
            :change="totalNumEmployees.change"
            :color="totalNumEmployees.color"
            :icon="totalNumEmployees.icon"
            :statistics="employee_count"
            :stat-title="totalNumEmployees.statTitle"
            :subtitle="totalNumEmployees.subtitle"
          ></statistics-card-vertical>
        </v-col>
        <v-col cols="12" sm="6">
          <statistics-card-vertical
            :change="employeesQuarantine.change"
            :color="employeesQuarantine.color"
            :icon="employeesQuarantine.icon"
            :statistics="pending_count"
            :stat-title="employeesQuarantine.statTitle"
            :subtitle="employeesQuarantine.subtitle"
          ></statistics-card-vertical>
        </v-col>
        <v-col cols="12" sm="6">
          <statistics-card-vertical
            :change="employeesInfected.change"
            :color="employeesInfected.color"
            :icon="employeesInfected.icon"
            :statistics="positive_count"
            :stat-title="employeesInfected.statTitle"
            :subtitle="employeesInfected.subtitle"
          ></statistics-card-vertical>
        </v-col>

        <v-col cols="12" sm="6">
          <statistics-card-vertical
            :change="employeesRecovered.change"
            :color="employeesRecovered.color"
            :icon="employeesRecovered.icon"
            :statistics="negative_count"
            :stat-title="employeesRecovered.statTitle"
            :subtitle="employeesRecovered.subtitle"
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
      // const res = await UserService.getEmployees()
      const res = await UserService.getEmployeesCtests(this.username)
      this.items = res.data
      this.employee_count = this.items.length

      this.items.forEach(item => {
        if (item.ctest.type == 'pending') this.pending_count += 1
        else if (item.ctest.type == 'positive') this.positive_count += 1
        else if (item.ctest.type == 'negative') this.negative_count += 1
      })
    } catch (error) {
      console.error(error)
    }
  },

  methods: {
    set_numbers() {},
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

    //number of employees on quarantine
    const employeesQuarantine = {
      statTitle: 'Num of employees',
      icon: mdiHospitalBoxOutline,
      color: 'secondary',
      subtitle: 'on quarantine currently',
      // statistics: '2',
      // change: '+0.02%',
    }

    //number of employees infected with COVID-19
    const employeesInfected = {
      statTitle: 'Num of employees',
      icon: mdiNeedle,
      color: 'primary',
      subtitle: 'infected currently',
      // statistics: '0',
      // change: '',
    }

    //number of employees recovered from COVID-19
    const employeesRecovered = {
      statTitle: 'Num of employees',
      icon: mdiThumbUp,
      color: 'warning',
      subtitle: 'recovered from Covid',
      // statistics: '1',
      // change: '',
    }

    return {
      totalNumEmployees,
      employeesQuarantine,
      employeesInfected,
      employeesRecovered,
    }
  },
}
</script>
