<template>
  <div class="home">
    <h1>Home</h1>
    <v-item-group>
      <v-container>
        <v-row>
          <v-item>
            <v-card class="mx-auto my-12" max-width="260">
              <template slot="progress">
                <v-progress-linear height="10"></v-progress-linear>
              </template>

              <v-img
                height="250"
                src="https://cdn.pixabay.com/photo/2015/09/02/12/29/pedestrians-918471_1280.jpg"
              ></v-img>
              <v-card-title>{{ headingGathering }}</v-card-title>
              <v-card-text>
                <div>{{ textGathering }}</div>
              </v-card-text>
            </v-card>
          </v-item>

          <v-item>
            <v-card class="mx-auto my-12" max-width="270">
              <template slot="progress">
                <v-progress-linear height="10"></v-progress-linear>
              </template>

              <v-img height="250" src="https://cdn.pixabay.com/photo/2015/01/09/11/22/fitness-594143_1280.jpg"></v-img>
              <v-card-title>{{ headingGym }}</v-card-title>
              <v-card-text>
                <div>{{ textGym }}</div>
              </v-card-text>
            </v-card>
          </v-item>
          <v-item>
            <v-card class="mx-auto my-12" max-width="270">
              <template slot="progress">
                <v-progress-linear height="10"></v-progress-linear>
              </template>

              <v-img
                height="250"
                src="https://cdn.pixabay.com/photo/2016/11/23/15/48/audience-1853662_1280.jpg"
              ></v-img>
              <v-card-title>{{ headingEvents }}</v-card-title>
              <v-card-text>
                <div>{{ textEvents }}</div>
              </v-card-text>
            </v-card>
          </v-item>
          <v-item>
            <v-card class="mx-auto my-12" max-width="270">
              <template slot="progress">
                <v-progress-linear height="10"></v-progress-linear>
              </template>

              <v-img
                height="260"
                src="https://cdn.pixabay.com/photo/2015/03/26/10/28/restaurant-691397_1280.jpg"
              ></v-img>
              <v-card-title>{{ headingDining }}</v-card-title>
              <v-card-text>
                <div>{{ textDining }}</div>
              </v-card-text>
            </v-card>
          </v-item>
        </v-row>
      </v-container>
    </v-item-group>
  </div>
</template>

<script>
export default {
  name: 'Home',
  data() {
    return {
      headingGathering: 'Gatherings',
      textGathering: 'Max of 2 pax',
      headingGym: 'Gym',
      textGym: 'Max of 2 pax',
      headingEvents: 'Events',
      textEvents: 'Up to vaccinated 100 persons',
      headingDining: 'Dining',
      textDining: 'Max of 2 vaccinated pax per table',
    }
  },
}
</script>
