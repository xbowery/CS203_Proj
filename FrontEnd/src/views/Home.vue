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
              <v-btn @click="toggleModal">
                Gatherings
              </v-btn>
              <v-btn>
                Gym
              </v-btn>
              <v-btn>
                Events
              </v-btn>
              <v-btn>
                Dining
              </v-btn>
            </v-btn-toggle>
          </v-row>
        </v-card-text>
      </v-card>

    <div v-if="showModal">
      <Modal :heading="heading" :text="text" theme="sale" @close="toggleModal" />
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
      heading: 'Gatherings',
      text: 'Max of 2 pax',
      showModal: false,
    }
  },
  methods: {
    toggleModal() {
      this.showModal = !this.showModal
    }
  },
}
</script>
