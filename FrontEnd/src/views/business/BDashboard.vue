<template>
  <v-card>
    <v-card-title>
      Employee Covid Testing Information
      <v-spacer></v-spacer>
      <v-text-field v-model="search" append-icon="mdi-magnify" label="Search" single-line hide-details></v-text-field>
    </v-card-title>
    <v-data-table :headers="headersTest" :items="testingInfo" :search="search"> </v-data-table>
  </v-card>
</template>

<script>
export default {
  data() {
    return {
      search: '',
      headersTest: [
        {
          text: 'Name',
          align: 'start',
          value: 'name',
        },
        { text: 'Vaccinated?', value: 'vaccinated' },
        { text: 'Latest Covid Test (date)', value: 'latestDate' },
        { text: 'Result', value: 'result' },
        { text: 'Next Covid Test (date)', value: 'nextDate' },
      ],
      headersPending: [
        {
          text: 'Name',
          align: 'start',
          value: 'name',
        },
        { text: 'Vaccinated?', value: 'vaccinated' },
        { text: 'Latest Covid Test (date)', value: 'latestDate' },
        { text: 'Result', value: 'result' },
        { text: 'Next Covid Test (date)', value: 'nextDate' },
      ],
      testingInfo: [
        {
          name: 'John',
          vaccinated: true,
          latestDate: '01/10/2021',
          result: false,
          nextDate: '20/10/2021',
        },
        {
          name: 'Bob',
          vaccinated: true,
          latestDate: '01/10/2021',
          result: false,
          nextDate: '20/10/2021',
        },
        {
          name: 'Sally',
          vaccinated: true,
          latestDate: '29/09/2021',
          result: false,
          nextDate: '20/10/2021',
        },
        {
          name: 'Eugene',
          vaccinated: true,
          latestDate: '04/10/2021',
          result: false,
          nextDate: '20/10/2021',
        },
        {
          name: 'Gingerbread',
          vaccinated: true,
          latestDate: '03/10/2021',
          result: false,
          nextDate: '20/10/2021',
        },
        {
          name: 'Karen',
          vaccinated: true,
          latestDate: '01/10/2021',
          result: false,
          nextDate: '20/10/2021',
        },
        {
          name: 'Vincent',
          vaccinated: true,
          latestDate: '01/10/2021',
          result: false,
          nextDate: '20/10/2021',
        },
        {
          name: 'Bill',
          vaccinated: true,
          latestDate: '01/10/2021',
          result: false,
          nextDate: '20/10/2021',
        },
        {
          name: 'Donut',
          vaccinated: true,
          latestDate: '01/10/2021',
          result: false,
          nextDate: '20/10/2021',
        },
        {
          name: 'KitKat',
          vaccinated: true,
          latestDate: '01/10/2021',
          result: false,
          nextDate: '20/10/2021',
        },
      ],
    }
  },
}
</script>
