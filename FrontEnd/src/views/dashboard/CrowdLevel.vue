<template>
  <v-card height="250">
    <v-item-group>
      <v-container>
        <v-row justify="center" align="center" v-if="renderComponent">
          <v-col cols="8" md="4">
            <!-- <h2>Capacity: {{ current }}/{{ maxCapacity }}</h2> -->
            <h2>Capacity: {{ updatedCrowd.noOfCustomers }}/{{ restaurant.maxCapacity }}</h2>
          </v-col>

          <v-col cols="9" md="5">
            <!-- <h2>Crowd level: {{ crowdLvl }}</h2> -->
            <h2>Crowd level: {{ updatedCrowd.latestCrowd }}</h2>
          </v-col>
        </v-row>

        <v-row justify="center" align="center">
          <v-col  cols="6" md="4" justify="center" align="center">
            <v-btn depressed color="success" large @click="increment(1)">+1</v-btn>
          </v-col>
          <v-col  cols="6" md="4" justify="center" align="center"> 
            <v-btn small color="error" large @click="increment(-1)">-1</v-btn>
          </v-col>
        </v-row>

        <v-row justify="center" align="center">
          <v-col  cols="6" md="4" justify="center" align="center">
            <v-btn depressed color="success" large @click="increment(2)">+2</v-btn>
          </v-col>
          <v-col cols="6" md="4" justify="center" align="center">
            <v-btn depressed color="error" large @click="increment(-2)">-2</v-btn>
          </v-col>
        </v-row>

        <v-row justify="center" align="center">
          <v-btn depressed color="primary" large @click="reset()">Reset</v-btn>
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
    renderComponent: true,
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
    } catch (error) {
      console.error(error)
    }
  },

  methods: {
    reset(){
      this.increment(-1 * this.updatedCrowd.noOfCustomers)
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
        this.renderComponent = false
        this.renderComponent = true
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
        await UserService.postCrowdLevel(this.restaurant.id, newCrowd)
      } catch (error) {
        console.error(error)
      }
    },

    async handleGetCrowdlevel() {
      try {
        const res = await UserService.getCrowdLevel(this.username)
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
