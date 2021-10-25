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
        :change="frequencyOfTest.change"
        :color="frequencyOfTest.color"
        :icon="frequencyOfTest.icon"
        :statistics="frequencyOfTest.statistics"
        :stat-title="frequencyOfTest.statTitle"
        :subtitle="frequencyOfTest.subtitle"
      ></statistics-card-vertical>
    </v-col>

    <v-col cols="10" sm="3">
      <statistics-card-vertical
        :change="daysToNextTest.change"
        :color="daysToNextTest.color"
        :icon="daysToNextTest.icon"
        :statistics="daysToNextTest.statistics"
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
// demos
import CovidTestingDatatable from './CovidTestingDatatable.vue'

export default {
  data: () => ({
    latest_date: '',
    latest_result: '',
  }),

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
        console.log('new latest')
      }
    },
  },

  setup() {
    const latestTestDate = {
      statTitle: 'Latest Test Date',
      icon: mdiCalendarMonth,
      color: 'success',
    }

    // vertical card options
    const latestResult = {
      statTitle: 'Latest Result',
      icon: mdiHelpCircleOutline,
      color: 'primary',
    }

    const frequencyOfTest = {
      statTitle: 'Frequency of Test',
      icon: mdiClockOutline,
      color: 'warning',
      statistics: 'Every 7 Days',
    }

    const daysToNextTest = {
      statTitle: 'Days To Next Test',
      icon: mdiClockOutline,
      color: 'warning',
      statistics: '3',
    }

    return {
      latestTestDate,
      latestResult,
      frequencyOfTest,
      daysToNextTest,
    }
  },
}
</script>
