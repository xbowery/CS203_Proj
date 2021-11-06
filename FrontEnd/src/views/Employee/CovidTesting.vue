<template>
  <v-row>
    <v-col cols="10" sm="3">
      <statistics-card-vertical
        :change="latestTestDate.change"
        :color="latestTestDate.color"
        :icon="latestTestDate.icon"
        :statistics="latest_date"
        :stat-title="latestTestDate.statTitle"
        :subtitle="latestTestDate.subtitle"
      ></statistics-card-vertical>
    </v-col>

    <v-col cols="10" sm="3">
      <statistics-card-vertical
        :change="latestResult.change"
        :color="latestResult.color"
        :icon="latestResult.icon"
        :statistics="latest_result"
        :stat-title="latestResult.statTitle"
        :subtitle="latestResult.subtitle"
      ></statistics-card-vertical>
    </v-col>

    <v-col cols="10" sm="3">
      <statistics-card-vertical
        :change="dateOfNextTest.change"
        :color="dateOfNextTest.color"
        :icon="dateOfNextTest.icon"
        :statistics="next_date"
        :stat-title="dateOfNextTest.statTitle"
        :subtitle="dateOfNextTest.subtitle"
      ></statistics-card-vertical>
    </v-col>

    <v-col cols="10" sm="3">
      <statistics-card-vertical
        :change="daysToNextTest.change"
        :color="daysToNextTest.color"
        :icon="daysToNextTest.icon"
        :statistics="daysToNextCtest"
        :stat-title="daysToNextTest.statTitle"
        :subtitle="daysToNextTest.subtitle"
      ></statistics-card-vertical>
    </v-col>
    <v-col cols="12">
      <covid-testing-datatable :username="user.username" @set_latest="set_latest"></covid-testing-datatable>
    </v-col>
  </v-row>
</template>
<script>
// eslint-disable-next-line object-curly-newline
import { mdiCalendarMonth, mdiClockOutline, mdiHelpCircleOutline } from '@mdi/js'
import StatisticsCardVertical from '@/components/statistics-card/StatisticsCardVertical.vue'
import { mapGetters } from 'vuex'
import moment from 'moment'
import CovidTestingDatatable from './CovidTestingDatatable.vue'
import UserService from '@/services/user.service'

export default {
  data: () => ({
    latest_date: '',
    latest_result: '',
    next_date: '',
    daysToNextCtest: '',
  }),

  mounted(){
    this.getNextDate();
  },

  components: {
    StatisticsCardVertical,
    CovidTestingDatatable,
  },

  computed: {
    ...mapGetters({
      user: 'auth/user',
    }),
  },

  methods: {
    set_latest(e) {
      if (e != null) {
        this.latest_date = e.date
        this.latest_result = e.result

        let start = moment(e.date);
        let end = moment(start, "YYYY-MM-DD").add(this.test_frequency, 'days')
        let duration = moment.duration(end.diff(start))
        let days = duration.asDays()
        this.days_remaining = Math.round(days)
      }
    },
    async getNextDate(){
      try{
        const res = await UserService.getNextCtest()
        this.next_date = res.data
        this.daysToNextCtest = Math.round((new Date(this.next_date) -  new Date())/86400000)
      } catch(error){
        console.log(error)
      }

    }
  },

  setup() {
    const latestTestDate = {
      statTitle: 'Latest Test Date',
      icon: mdiCalendarMonth,
      color: 'success',
    }

    const latestResult = {
      statTitle: 'Latest Result',
      icon: mdiHelpCircleOutline,
      color: 'primary',
    }

    const dateOfNextTest = {
      statTitle: 'Next Test Date',
      icon: mdiClockOutline,
      color: 'warning',
    }

    const daysToNextTest = {
      statTitle: 'Days To Next Test',
      icon: mdiClockOutline,
      color: 'warning',
    }

    return {
      latestTestDate,
      latestResult,
      dateOfNextTest,
      daysToNextTest,
    }
  },
}
</script>
