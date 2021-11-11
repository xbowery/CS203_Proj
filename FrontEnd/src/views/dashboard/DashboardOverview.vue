<template>
  <v-card height = "405">
    <v-card-title class="align-start">
      <span>Overview of Customers for Past 7 Days</span>
      <v-spacer></v-spacer>
    </v-card-title>

    <v-card-text class="d-flex justify-center">
      <apexchart type="bar" width="500" height="300" :options="chartOptions" :series="series"></apexchart>
    </v-card-text>
  </v-card>
</template>


<script>
import VueApexCharts from 'vue-apexcharts'
import UserService from '@/services/user.service'
import { mapGetters } from 'vuex'
import { getCurrentInstance } from '@vue/composition-api'

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
      series: [{
        name: 'Max number of customers',
        data:[]
      }],
      
    }
  },

  mounted() {
    this.getDays()  
    this.getCrowdLevel()
  },
  methods:{
    async getCrowdLevel(){
      try {
        const res = await UserService.getCrowdLevel(this.user.username)
        this.chartOptions.xaxis.categories.forEach(day => {
          var currentDay = day
          var currentMax = 0
          var maxElement = ''
          res.data.forEach(element => {
            if(currentDay === this.dateToString(element.datetime) && element.noOfCustomers > currentMax){
                currentMax = element.noOfCustomers
                maxElement = element
            }
          });
          this.series[0].data.push(currentMax)
          this.getColor(maxElement)
        })

      } catch (error) {
        console.error(error)
      }
    },

    getDays(){
      var today = new Date()
      for(let i = 6; i >= 0; --i){
        var tempDate = new Date(today.getTime() - i*24*60*60*1000)
        var dd = tempDate.getDate()
        var mm = tempDate.getMonth() + 1
        tempDate = dd + '/' + mm 
        this.chartOptions.xaxis.categories.push(tempDate)
      }
    },
    dateToString(date){
      var curDate = new Date(date).toLocaleString('en-US', { timeZone: 'Asia/Singapore' })
      var str = curDate.split("/")
      return str[1] + '/' + str[0]
    },

    getColor(element){
        if(element.latestCrowd === 'Low'){
          this.chartOptions.colors.push(this.colors.success)  
        } else if(element.latestCrowd === 'Medium'){
          this.chartOptions.colors.push(this.colors.pending)  
        } else if (element.latestCrowd === 'High'){
          this.chartOptions.colors.push(this.colors.error)  
        } else{
          this.chartOptions.colors.push(this.colors.success)  
        }
      }
      // this.chartOptions.colors = colors
    },
  setup() {
    const ins = getCurrentInstance()?.proxy
    const $vuetify = ins && ins.$vuetify ? ins.$vuetify : null

    const colors = {
      success: $vuetify.theme.currentTheme.success,
      error: $vuetify.theme.currentTheme.error,
      pending: $vuetify.theme.currentTheme.warning,
    }

    const chartOptions = {
        colors: [
          // $vuetify.theme.currentTheme.primary,
          // $vuetify.theme.currentTheme.primary,
          // $vuetify.theme.currentTheme.primary,
          // $vuetify.theme.currentTheme.primary,
          // $vuetify.theme.currentTheme.primary,
          // $vuetify.theme.currentTheme.primary,
          // $vuetify.theme.currentTheme.primary,
        ],
        chart: {
          width: 380,
          type: 'bar',
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
        xaxis:{
          categories:[],
          axisBorder: {
            show: false,
          },
        },
        
      }

    return {
      chartOptions,
      colors
    }
  },
}
</script>
