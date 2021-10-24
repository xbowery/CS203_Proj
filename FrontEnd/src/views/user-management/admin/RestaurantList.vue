<template>
<v-card>
    <v-card-text class="d-flex align-center flex-wrap pb-0">
    <!-- search -->
      <v-text-field
        v-model="search"
        placeholder="Search"
        outlined
        hide-details
        dense
        class="user-search me-3 mb-4"
        label="Search"
      >
      </v-text-field>

      <v-spacer></v-spacer>
    </v-card-text>
  <v-data-table
    :headers="headers"
    :items="items"
    :search = "search"
    sort-by="location"
    class="elevation-1"
  >
    <template v-slot:top>
      <v-toolbar
        flat
      >
        <v-toolbar-title>Restaurants List</v-toolbar-title>
        <v-divider
          class="mx-4"
          inset
          vertical
        ></v-divider>
        <v-spacer></v-spacer>
        <v-dialog
          v-model="dialog"
          max-width="500px"
        >
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              color="primary"
              dark
              class="mb-2"
              @click.stop="isAddNewUserSidebarActive = !isAddNewUserSidebarActive"
              v-bind="attrs"
              v-on="on"
            >
              Add New Restaurant
            </v-btn>
          </template>
          <validation-observer ref="obs">
          <v-card slot-scope="{ invalid }">
            <v-form @submit.prevent="handleSubmit">
            <v-card-title>
              <span class="text-h5">{{ formTitle }}</span>
            </v-card-title>

            <v-card-text>
              <v-container>
                <v-row>
                  <v-col
                    cols="12"
                    sm="6"
                    md="4"
                  >
                  <validation-provider name="name" rules="required" v-slot="{ errors }">
                    <v-text-field
                      v-model="editedItem.name"
                      :error-messages="errors[0]"
                      label="name"
                    ></v-text-field>
                  </validation-provider>
                  </v-col>
                  <v-col
                    cols="12"
                    sm="6"
                    md="4"
                  >
                  <validation-provider name="location" rules="required" v-slot="{ errors }">
                    <v-text-field
                      v-model="editedItem.location"
                      :error-messages="errors[0]"
                      label="location"
                    ></v-text-field>
                  </validation-provider>
                  </v-col>
                  <v-col
                    cols="12"
                    sm="6"
                    md="4"
                  >
                  <validation-provider name="cuisine" rules="required" v-slot="{ errors }">
                    <v-text-field
                      v-model="editedItem.cuisine"
                      :error-messages="errors[0]"
                      label="cuisine"
                    ></v-text-field>
                  </validation-provider>
                  </v-col>
                  <v-col
                    cols="12"
                    sm="6"
                    md="4"
                  >
                  <validation-provider name="description" rules="required" v-slot="{ errors }">
                    <v-text-field
                      v-model="editedItem.description"
                      :error-messages="errors[0]"
                      label="description"
                    ></v-text-field>
                  </validation-provider>
                  </v-col>
                  <v-col
                    cols="12"
                    sm="6"
                    md="4"
                  >
                  <validation-provider name="max capacity" rules="required" v-slot="{ errors }">
                    <v-text-field
                      v-model="editedItem.maxCapacity"
                      :error-messages="errors[0]"
                      label="max capacity"
                    ></v-text-field>
                  </validation-provider>
                  </v-col>
                </v-row>
              </v-container>
            </v-card-text>

            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn
                color="blue darken-1"
                text
                @click="dialog = false" 
                type="button"
              >
                Cancel
              </v-btn>
              <v-btn
                color="blue darken-1"
                text
                @click="save"
                type="submit" :disabled="invalid"
              >
                Save
              </v-btn>
            </v-card-actions>
            </v-form>
          </v-card>
          </validation-observer>
        </v-dialog>
        <v-dialog v-model="dialogDelete" max-width="500px">  
          <v-card>
            <v-card-title class="text-h5">Are you sure you want to delete this restaurant?</v-card-title>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue darken-1" text @click="closeDelete">Cancel</v-btn>
              <v-btn color="blue darken-1" text @click="deleteItemConfirm">OK</v-btn>
              <v-spacer></v-spacer>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-toolbar>
    </template>
    <template  #[`item.actions`]="{ item }">
        <v-icon
              small
              color="secondary"
              outlined
              class="mr-2"

              @click="editItem(item)"
            >
              Edit
        </v-icon>

        <v-icon
              small
              color="secondary"
              outlined
              class="mr-2"

              @click="deleteItem(item)"
            >
              Delete
        </v-icon>
    </template>
    <template v-slot:no-data>
        <v-text>No data available.</v-text>
    </template>
  </v-data-table>
    </v-card>
</template>

<script>
import UserService from '@/services/user.service'
import { ValidationProvider, ValidationObserver } from 'vee-validate'
import Restaurant from '@/model/restaurant'

  export default {
    components: { ValidationProvider, ValidationObserver },
    data: () => ({
      dialog: false,
      dialogDelete: false,
      search:'',
      errors:'',
      isAddNewUserSidebarActive: '',
      headers: [
        {
          text: 'NAME',
          align: 'start',
          sortable: false,
          value: 'name',
        },
        { text: 'LOCATION', value: 'location' },
        { text: 'CUISINE', value: 'cuisine' },
        { text: 'DESCRIPTION', value: 'description' },
        { text: 'MAXCAPACITY', value: 'maxCapacity' },
        { text: 'ACTIONS', value: 'actions', sortable: true },
      ],
      items: [],
      editedIndex: -1,
      editedItem: {
        name: '',
        location: '',
        cuisine: '',
        description: '',
        maxCapacity: '',
      },
      defaultItem: {
        name: '',
        lastName: '',
        location: '',
        cuisine: '',
        description: '',
        maxCapacity: '',
      },
    }),

    async mounted() {
      try {
        const res = await UserService.getRestaurants()
        this.items = res.data
      } catch (error) {
        console.error(error)
      }
    },

    computed: {
      formTitle () {
        return this.editedIndex === -1 ? 'New Restaurant' : 'Edit Restaurant'
      },
    },

    watch: {
      dialog (val) {
        val || this.close()
      },
      dialogDelete (val) {
        val || this.closeDelete()
      },
    },

    methods: {
      editItem (item) {
        this.editedIndex = this.items.indexOf(item)
        this.editedItem = Object.assign({}, item)
        this.dialog = true
      },

      deleteItem (item) {
        this.editedIndex = this.items.indexOf(item)
        this.editedItem = Object.assign({}, item)
        this.dialogDelete = true
      },

      deleteItemConfirm () {
        const restaurant = new Restaurant('', '')
        restaurant.name = this.items[this.editedIndex].name
        restaurant.location = this.items[this.editedIndex].location
        
        this.items.splice(this.editedIndex, 1)
        this.handleDeleteRestaurant(restaurant)

        if (!this.message) {
          this.closeDelete()
        }
      },

      close () {
        this.dialog = false
        this.$nextTick(() => {
          this.editedItem = Object.assign({}, this.defaultItem)
          this.editedIndex = -1
        })
      },

      closeDelete () {
        this.dialogDelete = false
        this.$nextTick(() => {
          this.editedItem = Object.assign({}, this.defaultItem)
          this.editedIndex = -1
        })
      },

      save () {
        if (this.editedIndex > -1) {
          Object.assign(this.items[this.editedIndex], this.editedItem)

          const restaurant = new Restaurant('', '', '', '', '')
          restaurant.name = this.items[this.editedIndex].name
          restaurant.location = this.items[this.editedIndex].location
          restaurant.cuisine = this.items[this.editedIndex].cuisine
          restaurant.description = this.items[this.editedIndex].description
          restaurant.maxCapacity = this.items[this.editedIndex].maxCapacity

          this.handleEditRestaurant(restaurant)
        } else {
          const restaurant = new Restaurant('', '', '', '', '')
          restaurant.name = this.editedItem.name
          restaurant.location = this.editedItem.location
          restaurant.cuisine = this.editedItem.cuisine
          restaurant.description = this.editedItem.description
          restaurant.maxCapacity = this.editedItem.maxCapacity

          this.handleNewRestaurant(restaurant)
          this.items.push(this.editedItem)
        }
        this.close()
      },
      async handleEditRestaurant(restaurant) {
        try {
          const res = await UserService.updateRestaurant(restaurant)
          console.log(res.data)
        } catch (error) {
          this.message = error.response?.data?.message || error.message || error.toString()
          console.error(error)
        }
      },
      async handleNewRestaurant(restaurant) {
        try {
          const res = await UserService.postRestaurant(restaurant)
          console.log(res.data)
        } catch (error) {
          this.message = error.response?.data?.message || error.message || error.toString()
          console.error(error)
        }
      },
      async handleDeleteRestaurant(restaurant) {
      try {
        const res = await UserService.deleteRestaurant(restaurant)
        console.log(res.data)
      } catch (error) {
        this.message = error.response?.data?.message || error.message || error.toString()
        console.error(error)
      }
    },
    },
  }

</script>