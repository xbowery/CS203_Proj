<template>
  <v-card>
    <v-item-group>
      <v-container>
        <v-row justify="end" align="left">
          <v-btn depressed color="normal" large @click="reset()">Reset</v-btn>
        </v-row>

        <v-row justify="center" align="center">
          <v-col v-for="n in 1" :key="n" cols="8" md="4">
            <!-- <h2>Capacity: {{ current }}/{{ maxCapacity }}</h2> -->
            <h2>Capacity: {{ updatedCrowd.noOfCustomers }}/{{ restaurant.maxCapacity }}</h2>
          </v-col>

          <v-col v-for="n in 1" :key="n" cols="9" md="5">
            <!-- <h2>Crowd level: {{ crowdLvl }}</h2> -->
            <h2>Crowd level: {{ updatedCrowd.latestCrowd }}</h2>
          </v-col>
        </v-row>

        <v-row justify="center" align="center">
          <v-col v-for="n in 1" :key="n" cols="6" md="4" justify="center" align="center">
            <v-btn depressed color="primary" large @click="increment(1)">+1</v-btn>
          </v-col>
          <v-col v-for="n in 1" :key="n" cols="6" md="4" justify="center" align="center">
            <v-btn small color="error" large @click="increment(-1)">-1</v-btn>
          </v-col>
        </v-row>

        <v-row justify="center" align="center">
          <v-col v-for="n in 1" :key="n" cols="6" md="4" justify="center" align="center">
            <v-btn depressed color="primary" large @click="increment(2)">+2</v-btn>
          </v-col>
          <v-col v-for="n in 1" :key="n" cols="6" md="4" justify="center" align="center">
            <v-btn depressed color="error" large @click="increment(-2)">-2</v-btn>
          </v-col>
        </v-row>
      </v-container>
    </v-item-group>
  </v-card>
</template>

<script>
import UserService from '@/services/user.service'
import CrowdLevel from '@/model/crowdLevel'
export default {
  props: {
    username: String,
  },
  data: () => ({
    error: '',
    items: [],

    restaurant: {
      name: '',
      location: '',
      cuisine: '',
      description: '',
      currentCapacity: '',
      maxCapacity: '',
      crowdLevel: [],
    },

    updatedCrowd: {
      date: 'nil',
      latestCrowd: 'nil',
      noOfCustomers: 'nil',
    },
  }),

  async mounted() {
    try {
      const res = await UserService.getRestaurant(this.username)
      this.restaurant = res.data
      if (this.restaurant.crowdLevel != null) {
        var crowdLevel = this.restaurant.crowdLevel
        var latestCrowd = crowdLevel[0]
        crowdLevel.forEach(item => {
          if (item.datetime > latestCrowd.datetime) {
            latestCrowd = item
          }
        })
        this.updatedCrowd = latestCrowd
      }

      console.log(this.restaurant)
    } catch (error) {
      console.error(error)
    }
  },

  methods: {
    reset(){
      this.items.length = 0
      this.updatedCrowd.noOfCustomers = 0
    },

    increment(count) {
      if (
        this.updatedCrowd.noOfCustomers + count >= 0 &&
        this.updatedCrowd.noOfCustomers + count <= this.restaurant.maxCapacity
      ) {
        // send a post request
        const newCrowd = new CrowdLevel('', '', '')
        newCrowd.datetime = new Date(Date.now())
        newCrowd.noOfCustomers = this.updatedCrowd.noOfCustomers + count
        newCrowd.latestCrowd = this.getNewCrowd(this.updatedCrowd.noOfCustomers + count)
        this.handlePostCrowdlevel(newCrowd)
        //then a new get request
        this.handleGetCrowdlevel()
      }
    },
    getNewCrowd(count) {
      var utilization = count / this.restaurant.maxCapacity
      if (utilization <= 0.4) {
        return 'Low'
      } else if (utilization <= 0.7) {
        return 'Medium'
      } else {
        return 'High'
      }
    },

    async handlePostCrowdlevel(newCrowd) {
      try {
        console.log(newCrowd)
        const res = await UserService.postCrowdLevel(newCrowd)
        console.log(res)
      } catch (error) {
        console.error(error)
      }
    },

    async handleGetCrowdlevel() {
      try {
        const res = await UserService.getCrowdLevel()
        this.items = res.data
        if (this.items.length != 0) {
          var latestCrowd = this.items[0]
          this.items.forEach(item => {
            if (item.datetime > latestCrowd.datetime) {
              latestCrowd = item
            }
          })
          this.updatedCrowd = latestCrowd
        }
      } catch (error) {
        console.error(error)
      }
    },
  },
}
</script>
