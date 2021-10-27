<template>
  <v-container fluid>
    <v-row>
      <!-- congratulations flora -->
      <v-col cols="10" md="12" order="1" order-md="2" class="align-self-end mb-10 mt-sm-10 mt-md-0">
        <v-card class="align-center">
          <v-row>
            <v-col cols="8" sm="6">
              <v-card-title class="page-title flex-nowrap text-2xl">
                <span class="text-no-wrap">Hello </span>
                <span class="text-no-wrap font-weight-bold mx-1"> {{ user.username }} </span>
                <span>🎉</span>
              </v-card-title>
              <v-card-text> What would you like to know more about today? </v-card-text>
            </v-col>
            <v-col cols="4" sm="6">
              <div>
                <v-img
                  width="128"
                  src="@/assets/images/3d-characters/pose-2.png"
                  class="gamification-john-pose-2"
                ></v-img>
              </div>
            </v-col>
          </v-row>
        </v-card>
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="12" sm="6" md="4" lg="3" v-for="(item, index) in items" :key="index">
        <v-card>
          <v-img class="misc-tree" :src="images[item.businessType.toLowerCase()]" align="center" justify="center"></v-img>
          <v-card-title class="subheading font-weight-bold">
            {{ item.businessType }}
          </v-card-title>

          <v-divider></v-divider>

          <v-list dense>
            <v-list-item v-for="(header, index) in headers" :key="index">
              <v-list-item-content> {{ header.text }}: </v-list-item-content>
              <v-list-item-content class="align-end">
                {{ item[header.value] }}
              </v-list-item-content>
            </v-list-item>
          </v-list>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import UserService from '@/services/user.service'
import { mapGetters } from 'vuex'

export default {
  name: 'Home',
  computed: {
    ...mapGetters({
      user: 'auth/user',
    }),
  },
  data() {
    return {
      items: [],
      headers: [
        // { text: 'Date of creation', value: 'creationDateTime' },
        { text: 'Max capacity', value: 'maxCapacity' },
        { text: 'Vaccinated?', value: 'vaccinationStatus' },
        { text: 'Mask required?', value: 'maskStatus' },
      ],
      images: {
        restaurant: 'https://cdn.pixabay.com/photo/2015/03/26/10/28/restaurant-691397_1280.jpg',
        gym: 'https://cdn.pixabay.com/photo/2015/01/09/11/22/fitness-594143_1280.jpg',
        gathering: 'https://cdn.pixabay.com/photo/2015/09/02/12/29/pedestrians-918471_1280.jpg',
        events: 'https://cdn.pixabay.com/photo/2016/11/23/15/48/audience-1853662_1280.jpg',
      },
    }
  },
  async mounted() {
    try {
      const res = await UserService.getMeasures()
      this.items = res.data
    } catch (error) {
      console.error(error)
    }
  },
}
</script>
