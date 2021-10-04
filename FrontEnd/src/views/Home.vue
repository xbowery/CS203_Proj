<template>
  <div class="home">
    <h1>Home</h1>
      <v-card
        flat
        class="py-12"
      >
        <v-card-text>
          <v-row
            align="center"
            justify="center"
          >
            <v-btn-toggle>
              <v-btn @click="toggleModalGathering">
                Gatherings
              </v-btn>
              <v-btn @click="toggleModalGym">
                Gym
              </v-btn>
              <v-btn @click="toggleModalEvents">
                Events
              </v-btn>
              <v-btn @click="toggleModalDining">
                Dining
              </v-btn>
            </v-btn-toggle>
          </v-row>
        </v-card-text>
      </v-card>

    <div v-if="showModalGathering">
      <Modal :heading="headingGathering" :text="textGathering" theme="sale" @close="toggleModalGathering" />
    </div>

    <div v-if="showModalGym">
      <Modal :heading="headingGym" :text="textGym" theme="sale" @close="toggleModalGym" />
    </div>

    <div v-if="showModalEvents">
      <Modal :heading="headingEvents" :text="textEvents" theme="sale" @close="toggleModalEvents" />
    </div>

    <div v-if="showModalDining">
      <Modal :heading="headingDining" :text="textDining" theme="sale" @close="toggleModalDining" />
    </div>
  </div>
</template>

<script>
import Modal from '../components/Modal'

export default {
  name: 'Home',
  components: {
    Modal
  },
  data(){
      return {
      headingGathering: 'Gatherings',
      textGathering: 'Max of 2 pax',
      headingGym: 'Gym',
      textGym: 'Max of 2 pax',
      headingEvents: 'Events',
      textEvents: 'Up to vaccinated 100 persons',
      headingDining: 'Dining',
      textDining: 'Max of 2 vaccinated pax per table',


      showModalGathering: false,
      showModalGym: false,
      showModalEvents: false,
      showModalDining: false,
    }
  },
  methods: {
    toggleModalGathering() {
      this.showModalGathering = !this.showModalGathering
    },
    toggleModalGym() {
      this.showModalGym = !this.showModalGym
    },
    toggleModalEvents() {
      this.showModalEvents = !this.showModalEvents
    },
    toggleModalDining() {
      this.showModalDining = !this.showModalDining
    },
  },
}
</script>
