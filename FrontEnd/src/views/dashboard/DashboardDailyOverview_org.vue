<template>
  <v-card>
    <v-card-title class="align-start">
      <span>Daily Overview</span>

      <v-spacer></v-spacer>

      <v-btn icon small class="mt-n2 me-n3">
        <v-icon size="22">
          {{ icons.mdiDotsVertical }}
        </v-icon>
      </v-btn>
    </v-card-title>

    <v-card-text>
      <!-- Chart -->
      <apexchart type="pie" width="380" :options="chartOptions" :series="series"></apexchart>

      <div class="d-flex align-center">
        <!-- <h3 class="text-2xl font-weight-semibold me-4">30</h3> -->
        <h3 class="text-2xl font-weight-semibold me-4">{{ restaurant.currentCapacity }}</h3>
        <!-- <h3 class="text-2xl font-weight-semibold me-4"> </h3> -->
        <span>individuals have visited your restaurant in the past hour</span>
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
import UserService from '@/services/user.service'

export default {
  components: {
    apexchart: VueApexCharts,
  },
  props: {
    username: String,
  },

  // data() {
  //   return {
  //     error: '',
  //     items: [],

  //     restaurant: {
  //       name: '',
  //       location: '',
  //       cuisine: '',
  //       description: '',
  //       currentCapacity: '',
  //       maxCapacity: '',
  //       crowdLevel: [],
  //     },
  //   }
  // },

  data: {
    series: [44, 55, 13, 43, 22],
    chartOptions: {
      chart: {
        width: 380,
        type: 'pie',
      },
      labels: ['Team A', 'Team B', 'Team C', 'Team D', 'Team E'],
      responsive: [
        {
          breakpoint: 480,
          options: {
            chart: {
              width: 200,
            },
            legend: {
              position: 'bottom',
            },
          },
        },
      ],
    },
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
        type: 'pie',
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
        categories: ['0900', '1000', '1100', '1200', '1300', '1400', '1500', '1600'],
        axisBorder: {
          show: false,
        },
        axisTicks: {
          show: false,
        },
        tickPlacement: 'on',
        labels: {
          show: true,
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
        // data: [10, 10, 10, 25, 30, 0, 0, 0, 0],
        // data:[currentCapacity],
        data: [],
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
  methods: {
    setDataChart() {
      setInterval(() => {
        this.chartData.push(this.restaurant.currentCapacity)
        this.updateSeriesLine()
      }, 3000)
    },

    updateSeriesLine() {
      this.$refs.realtimeChart.updateSeries(
        [
          {
            data: this.chartData,
          },
        ],
        false,
        true,
      )
    },

    // fetchData() {
    //   let time = 24
    //   const timeValue = setInterval(mounted => {
    //     time = this.time - 1
    //     if (time <= 0) {
    //       clearInterval(timeValue)
    //     }
    //   }, 1000)
    // },
  },

  async mounted() {
    try {
      // let time = 24
      // const timeValue = setInterval(() => {
      //   time = this.time - 1
      //   if (time <= 0) {
      //     clearInterval(timeValue)
      //   }
      // }, 3600000)

      // const res = await UserService.getRestaurant(this.username)
      // this.restaurant = res.data
      // console.log(this.restaurant.crowdLevel)
      // this.chartData = this.restaurant.crowdLevel.map(level => level.noOfCustomers)
      // this.updateSeriesLine()
      // console.log(this.chartData)
      // this.chartData.push(this.restaurant.currentCapacity)
      // this.insertSeries(this.chartData)
      // this.setDataChart()

      // console.log(this.restaurant)
      const res = await UserService.getEmployees(this.username)
      this.items = res.data
      // console.log(this.chartData)
    } catch (error) {
      console.error(error)
    }
  },
}
</script>
