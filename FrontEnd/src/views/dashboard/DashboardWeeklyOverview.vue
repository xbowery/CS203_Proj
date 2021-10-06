<template>
  <v-card>
    <v-card-title class="align-start">
      <span>Weekly Overview</span>

      <v-spacer></v-spacer>

      <v-btn icon small class="mt-n2 me-n3">
        <v-icon size="22">
          {{ icons.mdiDotsVertical }}
        </v-icon>
      </v-btn>
    </v-card-title>

    <v-card-text>
      <!-- Chart -->
      <vue-apex-charts :options="chartOptions" :series="chartData" height="210"></vue-apex-charts>

      <div class="d-flex align-center">
        <h3 class="text-2xl font-weight-semibold me-4">95%</h3>
        <span>95% of your employees have completed their ART test in the past week</span>
      </div>

      <v-btn block color="primary" class="mt-6" outlined> Details </v-btn>
    </v-card-text>
  </v-card>
</template>

<script>
import VueApexCharts from 'vue-apexcharts'
// eslint-disable-next-line object-curly-newline
import { mdiDotsVertical, mdiTrendingUp, mdiCurrencyUsd } from '@mdi/js'
import { getCurrentInstance } from '@vue/composition-api'

export default {
  components: {
    VueApexCharts,
  },
  setup() {
    const ins = getCurrentInstance()?.proxy
    const $vuetify = ins && ins.$vuetify ? ins.$vuetify : null
    const customChartColor = $vuetify.theme.isDark ? '#3b3559' : '#f5f5f5'

    const chartOptions = {
      colors: [
        customChartColor,
        customChartColor,
        customChartColor,
        customChartColor,
        $vuetify.theme.currentTheme.primary,
        customChartColor,
        customChartColor,
      ],
      chart: {
        type: 'bar',
        toolbar: {
          show: false,
        },
        offsetX: -15,
      },
      plotOptions: {
        bar: {
          columnWidth: '40%',
          distributed: true,
          borderRadius: 8,
          startingShape: 'rounded',
          endingShape: 'rounded',
        },
      },
      dataLabels: {
        enabled: false,
      },
      legend: {
        show: false,
      },
      xaxis: {
        categories: ['S', 'M', 'T', 'W', 'T', 'F', 'S'],
        axisBorder: {
          show: false,
        },
        axisTicks: {
          show: false,
        },
        tickPlacement: 'on',
        labels: {
          show: false,
          style: {
            fontSize: '12px',
          },
        },
      },
      yaxis: {
        show: true,
        tickAmount: 4,
        labels: {
          offsetY: 3,
          formatter: value => `${value}`,
        },
      },
      stroke: {
        width: [2, 2],
      },
      grid: {
        strokeDashArray: 12,
        padding: {
          right: 0,
        },
      },
    }

    const chartData = [
      {
        data: [80, 85, 90, 95, 95, 95, 95, 95],
      },
    ]

    return {
      chartOptions,
      chartData,

      icons: {
        mdiDotsVertical,
        mdiTrendingUp,
        mdiCurrencyUsd,
      },
    }
  },
}
</script>
