<template>
  <v-card height = "405">
    <v-card-title class="align-start">
      <span>Overview of Employees' COVID-19 tests</span>
      <v-spacer></v-spacer>
    </v-card-title>

    <v-card-text class="d-flex justify-center">
      <apexchart type="pie" width="500" height="400" :options="chartOptions" :series="series"></apexchart>
    </v-card-text>
  </v-card>
</template>


<script>
import VueApexCharts from 'vue-apexcharts'
import UserService from '@/services/user.service'
import { mapGetters } from 'vuex'

export default {
  components: {
    apexchart: VueApexCharts,
  },

  computed: {
    ...mapGetters({
      user: 'auth/user',
    }),
  },

  data() {
    return {
      items: [],
      pending_count: 0,
      negative_count: 0,
      positive_count:0,
      series: [],
      chartOptions: {
        chart: {
          width: 380,
          type: 'pie',
        },
        labels: ['Pending', 'Negative', 'Positive'],
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
    }
  },

  async mounted() {
    try {
      const res = await UserService.getEmployeesCtests(this.user.username)
      this.items = res.data
      console.log(this.items)
      this.items.forEach(item => {
        if (item.result == 'Pending') this.pending_count += 1
        else if (item.result == 'Negative') this.negative_count +=1
        else this.positive_count += 1
      })

      this.series.push(this.pending_count, this.negative_count, this.positive_count)
    } catch (error) {
      console.error(error)
    }
  },
}
</script>
