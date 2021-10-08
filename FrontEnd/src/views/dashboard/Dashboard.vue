<template>
  <v-row>
    <v-col cols="12" md="4" sm="6">
      <dashboard-congratulation-john></dashboard-congratulation-john>
    </v-col>
    <v-col cols="12" md="8" sm="6">
      <crowd-level></crowd-level>
    </v-col>

    <v-col cols="12" sm="6" md="8">
      <dashboard-weekly-overview></dashboard-weekly-overview>
    </v-col>

    <v-col cols="12" md="4">
      <v-row class="match-height">
        <v-col cols="12" sm="6">
          <statistics-card-vertical
            :change="totalNumEmployees.change"
            :color="totalNumEmployees.color"
            :icon="totalNumEmployees.icon"
            :statistics="totalNumEmployees.statistics"
            :stat-title="totalNumEmployees.statTitle"
            :subtitle="totalNumEmployees.subtitle"
          ></statistics-card-vertical>
        </v-col>
        <v-col cols="12" sm="6">
          <statistics-card-vertical
            :change="employeesQuarantine.change"
            :color="employeesQuarantine.color"
            :icon="employeesQuarantine.icon"
            :statistics="employeesQuarantine.statistics"
            :stat-title="employeesQuarantine.statTitle"
            :subtitle="employeesQuarantine.subtitle"
          ></statistics-card-vertical>
        </v-col>
        <v-col cols="12" sm="6">
          <statistics-card-vertical
            :change="employeesInfected.change"
            :color="employeesInfected.color"
            :icon="employeesInfected.icon"
            :statistics="employeesInfected.statistics"
            :stat-title="employeesInfected.statTitle"
            :subtitle="employeesInfected.subtitle"
          ></statistics-card-vertical>
        </v-col>

        <v-col cols="12" sm="6">
          <statistics-card-vertical
            :change="employeesRecovered.change"
            :color="employeesRecovered.color"
            :icon="employeesRecovered.icon"
            :statistics="employeesRecovered.statistics"
            :stat-title="employeesRecovered.statTitle"
            :subtitle="employeesRecovered.subtitle"
          ></statistics-card-vertical>
        </v-col>
      </v-row>
    </v-col>

    <v-col cols="12">
      <dashboard-datatable></dashboard-datatable>
    </v-col>
  </v-row>
</template>

<script>
// eslint-disable-next-line object-curly-newline
import { mdiAccountGroup, mdiNeedle, mdiHospitalBoxOutline, mdiThumbUp } from '@mdi/js'
import StatisticsCardVertical from '@/components/statistics-card/StatisticsCardVertical.vue'

// demos
import DashboardCongratulationJohn from './DashboardCongratulationJohn.vue'
import CrowdLevel from './CrowdLevel.vue'
import DashboardWeeklyOverview from './DashboardWeeklyOverview.vue'
import DashboardDatatable from './DashboardDatatable.vue'

export default {
  components: {
    StatisticsCardVertical,
    DashboardCongratulationJohn,
    CrowdLevel,
    DashboardWeeklyOverview,
    DashboardDatatable,
  },
  setup() {
    const totalNumEmployees = {
      statTitle: 'Num of Employees',
      icon: mdiAccountGroup,
      color: 'success',
      subtitle: 'in total',
      statistics: '100',
      change: '+5%',
    }

    const employeesQuarantine = {
      statTitle: 'Num of employees',
      icon: mdiHospitalBoxOutline,
      color: 'secondary',
      subtitle: 'on quarantine currently',
      statistics: '2',
      change: '+0.02%',
    }

    // vertical card options
    const employeesInfected = {
      statTitle: 'Num of employees',
      icon: mdiNeedle,
      color: 'primary',
      subtitle: 'infected currently',
      statistics: '0',
      change: '',
    }

    const employeesRecovered = {
      statTitle: 'Num of employees',
      icon: mdiThumbUp,
      color: 'warning',
      subtitle: 'recovered from Covid',
      statistics: '1',
      change: '',
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
