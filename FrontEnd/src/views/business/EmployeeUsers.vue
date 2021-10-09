<template>
  <v-row>
    <v-col cols="10" sm="4">
      <statistics-card-vertical
        :change="totalProfit.change"
        :color="totalProfit.color"
        :icon="totalProfit.icon"
        :statistics="totalProfit.statistics"
        :stat-title="totalProfit.statTitle"
        :subtitle="totalProfit.subtitle"
      ></statistics-card-vertical>
    </v-col>

    <v-col cols="12" sm="4">
      <statistics-card-vertical
        :change="newProject.change"
        :color="newProject.color"
        :icon="newProject.icon"
        :statistics="newProject.statistics"
        :stat-title="newProject.statTitle"
        :subtitle="newProject.subtitle"
      ></statistics-card-vertical>
    </v-col>

    <v-col cols="12" sm="4">
      <statistics-card-vertical
        :change="salesQueries.change"
        :color="salesQueries.color"
        :icon="salesQueries.icon"
        :statistics="salesQueries.statistics"
        :stat-title="salesQueries.statTitle"
        :subtitle="salesQueries.subtitle"
      ></statistics-card-vertical>
    </v-col>

    <v-col cols="12">
      <employee-datatable></employee-datatable>
    </v-col>
  </v-row>
</template>
<script>
// eslint-disable-next-line object-curly-newline
import { mdiPoll, mdiLabelVariantOutline, mdiHelpCircleOutline } from '@mdi/js'
import StatisticsCardVertical from '@/components/statistics-card/StatisticsCardVertical.vue'

// demos
import EmployeeDatatable from './EmployeeDatatable.vue'

export default {
  components: {
    StatisticsCardVertical,
    // DashboardCongratulationJohn,
    // DashboardWeeklyOverview,
    EmployeeDatatable,
  },
  setup() {
    const totalProfit = {
      statTitle: 'Total Users',
      icon: mdiPoll,
      color: 'success',
      statistics: '100',
    }

    // vertical card options
    const newProject = {
      statTitle: 'Active Users',
      icon: mdiLabelVariantOutline,
      color: 'primary',
      statistics: '90',
    }

    const salesQueries = {
      statTitle: 'Pending Users',
      icon: mdiHelpCircleOutline,
      color: 'warning',
      statistics: '10',
    }

    return {
      totalProfit,
      newProject,
      salesQueries,
    }
  },
}
</script>
