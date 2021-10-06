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
            :change="totalProfit.change"
            :color="totalProfit.color"
            :icon="totalProfit.icon"
            :statistics="totalProfit.statistics"
            :stat-title="totalProfit.statTitle"
            :subtitle="totalProfit.subtitle"
          ></statistics-card-vertical>
        </v-col>
        <v-col cols="12" sm="6">
          <statistics-card-vertical
            :change="totalSales.change"
            :color="totalSales.color"
            :icon="totalSales.icon"
            :statistics="totalSales.statistics"
            :stat-title="totalSales.statTitle"
            :subtitle="totalSales.subtitle"
          ></statistics-card-vertical>
        </v-col>
        <v-col cols="12" sm="6">
          <statistics-card-vertical
            :change="newProject.change"
            :color="newProject.color"
            :icon="newProject.icon"
            :statistics="newProject.statistics"
            :stat-title="newProject.statTitle"
            :subtitle="newProject.subtitle"
          ></statistics-card-vertical>
        </v-col>

        <v-col cols="12" sm="6">
          <statistics-card-vertical
            :change="salesQueries.change"
            :color="salesQueries.color"
            :icon="salesQueries.icon"
            :statistics="salesQueries.statistics"
            :stat-title="salesQueries.statTitle"
            :subtitle="salesQueries.subtitle"
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
import { mdiPoll, mdiLabelVariantOutline, mdiCurrencyUsd, mdiHelpCircleOutline } from '@mdi/js'
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
    const totalProfit = {
      statTitle: 'Number of Employees',
      icon: mdiPoll,
      color: 'success',
      subtitle: 'in total',
      statistics: '100',
      change: '+5%',
    }

    const totalSales = {
      statTitle: 'Number of employees',
      icon: mdiCurrencyUsd,
      color: 'secondary',
      subtitle: 'on quarantine currently',
      statistics: '2',
      change: '+0.02%',
    }

    // vertical card options
    const newProject = {
      statTitle: 'Number of employees',
      icon: mdiLabelVariantOutline,
      color: 'primary',
      subtitle: 'infected currently',
      statistics: '0',
      change: '',
    }

    const salesQueries = {
      statTitle: 'Number of employees',
      icon: mdiHelpCircleOutline,
      color: 'warning',
      subtitle: 'recovered from Covid',
      statistics: '1',
      change: '',
    }

    return {
      totalProfit,
      totalSales,
      newProject,
      salesQueries,
    }
  },
}
</script>
