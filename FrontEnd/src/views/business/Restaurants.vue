<template>
  <v-container fluid>
    <v-data-iterator
      :items="items"
      :items-per-page.sync="itemsPerPage"
      :page.sync="page"
      :search="search"
      :sort-by="sortBy.toLowerCase()"
      :sort-desc="sortDesc"
      hide-default-footer
      loading
      loading-text="Loading"
    >
      <template v-slot:header>
        <v-toolbar dark color="blue darken-3" class="mb-1">
          <v-text-field
            v-model="search"
            clearable
            flat
            solo-inverted
            hide-details
            :prepend-inner-icon="icons.mdiMagnify"
            label="Search"
          ></v-text-field>
          <template v-if="$vuetify.breakpoint.mdAndUp">
            <v-spacer></v-spacer>
            <v-select
              v-model="sortBy"
              flat
              solo-inverted
              hide-details
              :items="sortKeys"
              :prepend-inner-icon="icons.mdiMagnify"
              label="Sort by"
            ></v-select>
            <v-spacer></v-spacer>
            <v-btn-toggle v-model="sortDesc" mandatory>
              <v-btn large depressed color="blue" :value="false">
                <v-icon>{{ icons.mdiArrowUp }}</v-icon>
              </v-btn>
              <v-btn large depressed color="blue" :value="true">
                <v-icon>{{ icons.mdiArrowDown }}</v-icon>
              </v-btn>
            </v-btn-toggle>
          </template>
        </v-toolbar>
      </template>
      <template v-slot:default="props">
        <v-row>
          <v-col v-for="item in props.items" :key="item.name" cols="12" sm="6" md="4" lg="3">
            <v-card>
              <v-img
                class="misc-tree"
                src="@/assets/images/misc/restaurant.jpg"
                align="center"
                justify="center"
              ></v-img>
              <v-card-title class="subheading font-weight-bold">
                {{ item.name }}
              </v-card-title>

              <v-divider></v-divider>

              <v-list dense>
                <v-list-item v-for="(key, index) in keys" :key="index">
                  <v-list-item-content :class="{ 'blue--text': sortBy === key }"> {{ key.key }}: </v-list-item-content>
                  <v-list-item-content class="align-end" :class="{ 'blue--text': sortBy === key }">
                    {{ item[key.value] }}
                  </v-list-item-content>
                </v-list-item>
              </v-list>
            </v-card>
          </v-col>
        </v-row>
      </template>

      <template v-slot:footer>
        <v-row class="mt-2" align="center" justify="center">
          <span class="grey--text">Items per page</span>
          <v-menu offset-y>
            <template v-slot:activator="{ on, attrs }">
              <v-btn dark text color="primary" class="ml-2" v-bind="attrs" v-on="on">
                {{ itemsPerPage }}
                <v-icon>{{ icons.mdiChevronDown }}</v-icon>
              </v-btn>
            </template>
            <v-list>
              <v-list-item
                v-for="(number, index) in itemsPerPageArray"
                :key="index"
                @click="updateItemsPerPage(number)"
              >
                <v-list-item-title>{{ number }}</v-list-item-title>
              </v-list-item>
            </v-list>
          </v-menu>

          <v-spacer></v-spacer>

          <span class="mr-4 grey--text"> Page {{ page }} of {{ numberOfPages }} </span>
          <v-btn fab dark color="blue darken-3" class="mr-1" @click="formerPage">
            <v-icon>{{ icons.mdiChevronLeft }}</v-icon>
          </v-btn>
          <v-btn fab dark color="blue darken-3" class="ml-1" @click="nextPage">
            <v-icon>{{ icons.mdiChevronRight }}</v-icon>
          </v-btn>
        </v-row>
      </template>
    </v-data-iterator>
  </v-container>
</template>

<script>
import UserService from '@/services/user.service'
import { mdiChevronRight, mdiChevronLeft, mdiMagnify, mdiArrowUp, mdiArrowDown } from '@mdi/js'

export default {
  setup() {
    return {
      icons: { mdiChevronRight, mdiChevronLeft, mdiMagnify, mdiArrowUp, mdiArrowDown },
    }
  },
  data() {
    return {
      itemsPerPageArray: [4, 8, 12],
      search: '',
      filter: {},
      sortDesc: false,
      page: 1,
      itemsPerPage: 8,
      sortBy: 'name',
      items: [],
      keys: [
        { key: 'Name', value: 'name' },
        { key: 'Location', value: 'location' },
        { key: 'Cuisine', value: 'cuisine' },
        { key: 'Description', value: 'description' },
        { key: 'Crowd Level', value: 'currentCrowdLevel' },
      ],
    }
  },
  async mounted() {
    try {
      const res = await UserService.getRestaurants()
      console.log(res)
      this.items = res.data
    } catch (error) {
      console.error(error)
    }
  },
  computed: {
    sortKeys() {
      return this.keys.map(key => key.key)
    },
    numberOfPages() {
      return Math.ceil(this.items.length / this.itemsPerPage)
    },
    filteredKeys() {
      return this.keys.filter(key => key !== 'Name')
    },
  },
  methods: {
    nextPage() {
      if (this.page + 1 <= this.numberOfPages) this.page += 1
    },
    formerPage() {
      if (this.page - 1 >= 1) this.page -= 1
    },
    updateItemsPerPage(number) {
      this.itemsPerPage = number
    },
  },
}
</script>
