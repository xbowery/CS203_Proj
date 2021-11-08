<template>
  <v-card height = "405">
    <v-card-title class="align-start">
      <span>Daily Overview of Employees' COVID-19 tests</span>
      <v-spacer></v-spacer>
    </v-card-title>

    <v-card-text class="d-flex justify-center">
      <!-- Chart -->
      <apexchart type="pie" width="380" :options="chartOptions" :series="series"></apexchart>

      <!-- <div class="d-flex align-center">
        <h3 class="text-2xl font-weight-semibold me-4">{{ items.length }}</h3>
        <span>individuals have visited your restaurant in the past hour</span>
      </div> -->

      <!-- <v-btn block color="primary" class="mt-6" outlined> Details </v-btn> -->
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
      incomplete_count: 0,
      series: [],
      chartOptions: {
        chart: {
          width: 380,
          type: 'pie',
        },
        labels: ['Pending', 'Incomplete'],
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

      this.items.forEach(item => {
        if (item.result == 'Pending') this.pending_count += 1
        else this.incomplete_count += 1
      })

      this.series.push(this.pending_count, this.incomplete_count)
    } catch (error) {
      console.error(error)
    }
  },
}
</script>
