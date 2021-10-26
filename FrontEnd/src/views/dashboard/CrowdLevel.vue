<template>
  <v-card>
    <v-item-group>
      <v-container>
        <v-row justify="center" align="center">
          <v-col v-for="n in 1" :key="n" cols="8" md="4">
            <!-- <h2>Capacity: {{ current }}/{{ maxCapacity }}</h2> -->
            <h2>Capacity: {{ lastItem.noOfCustomers }}/{{ lastItem.restaurant.maxCapacity }}</h2>
          </v-col>

          <v-col v-for="n in 1" :key="n" cols="9" md="5">
            <!-- <h2>Crowd level: {{ crowdLvl }}</h2> -->
            <h2>Crowd level: {{ lastItem.latestCrowd }}</h2>
          </v-col>
        </v-row>

        <v-row justify="center" align="center">
          <v-col v-for="n in 1" :key="n" cols="6" md="4" justify="center" align="center">
            <v-btn depressed color="primary" large @click="save(), handleNewCrowdLevel(+1)">+1</v-btn>
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
            <v-btn depressed color="error" large @click="save(editedItem.noOfCustomers)">-2</v-btn>
          </v-col>
        </v-row>
      </v-container>
    </v-item-group>
  </v-card>
</template>

<script>
import UserService from '@/services/user.service'

export default {
  data() {
    return {
      // current: 0,
      // maxCapacity: 100,
      // crowdLvl: 'Low',
      items: [],
      keys: ['latestCrowd', 'noOfCustomers', 'id', 'maxCapacity'],
      restaurantId: 4,
      crowdLevelId: 6,
      crowdLevel: {
        date: new Date(Date.now() - new Date().getTimezoneOffset() * 60000).toISOString().substr(0, 10),
        latestCrowd: '',
        noOfCustomers: '',
      },

      editedItem: {
        date: new Date(Date.now() - new Date().getTimezoneOffset() * 60000).toISOString().substr(0, 10),
        noOfCustomers: '',
      },
    }
  },

  async mounted() {
    // console.log(this.id)
    try {
      const res = await UserService.getCrowdLevel(4)
      this.items = res.data
    } catch (error) {
      console.error(error)
    }
  },

  computed: {
    lastItem() {
      return this.items.slice(-1)[0]
    },
  },

  methods: {
    // editItem(item){
    //   this.editedIndex = this.items.indexOf(item)
    //   this.editedItem = Object.assign({}, item)
    // },

    save() {
      const crowdLevel = new crowdLevel('', '')
      crowdLevel.date = this.editedItem.date
      crowdLevel.noOfCustomers = this.editedItem.noOfCustomers

      this.handleNewCrowdLevel(crowdLevel)
      this.items.push(this.editedItem)
    },

    // increment(count) {
    //   if (
    //     this.lastItem.noOfCustomers + count >= 0 &&
    //     this.lastItem.noOfCustomers + count <= this.lastItem.maxCapacity
    //   ) {
    //     this.lastItem.noOfCustomers += count
    //   }
    // },

    async handleNewCrowdLevel(count) {
      try {
        const res = await UserService.postCrowdLevel(this.lastItem.restaurant.id, this.lastItem)
        console.log(res)
      } catch (error) {
        console.log(error)
      } finally {
        this.editedItem.date = new Date()
        this.editedItem.noOfCustomers += count
        this.reloadTable()
      }
    },

    // async handleEditCrowdLevel(restaurantId, crowdLevelId, crowdLevel) {
    //   try {
    //     const res = await UserService.updateCrowdLevel(restaurantId, crowdLevelId, crowdLevel)
    //     console.log(res.data)
    //   } catch (error) {
    //     this.message = error.response?.data?.message || error.message || error.toString()
    //     console.log(error)
    //   }
    // },
  },
}
</script>
