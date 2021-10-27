<template>
  <v-row>
    <v-col cols="12" sm="6" md="4" lg="3" v-for="(item, index) in items" :key="index">
      <v-card>
        <v-img class="misc-tree" :src="images[item.businessType]" align="center" justify="center"></v-img>
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

        <v-dialog v-model="dialog" max-width="500px">
          <template v-slot:activator="{ on, attrs }">
            <div class="d-flex justify-center">
              <v-btn
                color="primary"
                dark
                class="mb-4 me-3"
                @click.stop="isAddNewUserSidebarActive = !isAddNewUserSidebarActive"
                v-bind="attrs"
                v-on="on"
              >
                Edit
              </v-btn>
            </div>
          </template>
          <v-spacer></v-spacer>
          <v-card>
            <validation-observer v-slot="{ handleSubmit, invalid }">
              <v-form @submit.prevent="handleSubmit(save)">
                <v-alert elevation="2" type="error" v-if="message">{{ message }}</v-alert>
                <v-card-title>
                  <span class="text-h5">{{ formTitle }}</span>
                </v-card-title>

                <v-card-text>
                  <v-container>
                    <v-row>
                      <v-col cols="12" sm="6" md="4">
                        <validation-provider name="Max Capacity" rules="required" v-slot="{ errors }">
                          <v-text-field
                            v-model="editedItem.maxCapacity"
                            :error-messages="errors[0]"
                            label="Max Capacity"
                          ></v-text-field>
                        </validation-provider>
                      </v-col>
                      <v-col cols="12" sm="6" md="4">
                        <validation-provider name="Vaccinated?" rules="required" v-slot="{ errors }">
                          <v-text-field
                            v-model="editedItem.vaccinationStatus"
                            :error-messages="errors[0]"
                            label="Vaccinated?"
                          ></v-text-field>
                        </validation-provider>
                      </v-col>
                      <v-col cols="12" sm="6" md="4">
                        <validation-provider name="Mask Required?" rules="required" v-slot="{ errors }">
                          <v-text-field
                            v-model="editedItem.maskStatus"
                            :error-messages="errors[0]"
                            label="Mask Required?"
                            :disabled="editedIndex > -1"
                          ></v-text-field>
                        </validation-provider>
                      </v-col>
                    </v-row>
                  </v-container>
                </v-card-text>
                <v-card-actions class="justify-center">
                  <v-spacer></v-spacer>
                  <v-btn color="blue darken-1" text @click="dialog = false" type="button"> Cancel </v-btn>
                  <v-btn color="blue darken-1" text type="submit" :disabled="invalid"> Save </v-btn>
                </v-card-actions>
              </v-form>
            </validation-observer>
          </v-card>
        </v-dialog>
      </v-card>
    </v-col>
  </v-row>
</template>

<script>
import User from '@/model/user'
import Measure from '@/model/measure'
import UserService from '@/services/user.service'
import { ValidationProvider, ValidationObserver } from 'vee-validate'
import '@/validators'

export default {
  components: { ValidationProvider, ValidationObserver },
  data: () => ({
    dialog: false,
    dialogDelete: false,
    search: '',
    errors: '',
    isAddNewUserSidebarActive: '',
    headers: [
      { text: 'Max capacity', value: 'maxCapacity' },
      { text: 'Vaccinated?', value: 'vaccinationStatus' },
      { text: 'Mask required?', value: 'maskStatus' },
    ],
    items: [],
    editedIndex: -1,
    editedItem: {
      maxCapacity: '',
      vaccinationStatus: '',
      maskStatus: '',
    },
    defaultItem: {
      maxCapacity: '',
      vaccinationStatus: '',
      maskStatus: '',
    },
  }),

  async mounted() {
    try {
      const res = await UserService.getMeasures()
      this.items = res.data
    } catch (error) {
      console.error(error)
    }
  },

  computed: {
    formTitle() {
      return this.editedIndex === -1 ? 'New Measure' : 'Edit Measure'
    },
  },

  watch: {
    dialog(val) {
      val || this.close()
    },
    dialogDelete(val) {
      val || this.closeDelete()
    },
  },

  setup() {
    const measure = new Measure('', '', '', '')

    return {
      measure,
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

  methods: {
    editItem(item) {
      this.editedIndex = this.items.indexOf(item)
      this.editedItem = Object.assign({}, item)
      this.dialog = true
    },

    deleteItem(item) {
      this.editedIndex = this.items.indexOf(item)
      this.editedItem = Object.assign({}, item)
      this.dialogDelete = true
    },

    deleteItemConfirm() {
      const user = new User('')
      user.username = this.items[this.editedIndex].username

      this.items.splice(this.editedIndex, 1)
      this.handleDeleteUser(user)

      if (!this.message) {
        this.closeDelete()
      }
    },

    close() {
      this.dialog = false
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem)
        this.editedIndex = -1
      })
    },

    closeDelete() {
      this.dialogDelete = false
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem)
        this.editedIndex = -1
      })
    },

    save() {
      if (this.editedIndex > -1) {
        Object.assign(this.items[this.editedIndex], this.editedItem)

        const measure = new Measure('', '', '', '')
        measure.maxCapacity = this.items[this.editedIndex].maxCapacity
        measure.vaccinationStatus = this.items[this.editedIndex].vaccinationStatus
        measure.maskStatus = this.items[this.editedIndex].maskStatus
        this.handleEditUser(measure)
      } else {
        this.items.push(this.editedItem)
        // this.handleSaveUser(this.editedItem)
      }
      if (!this.message) {
        this.close()
      }
    },

    async handleEditMeasure(measure) {
      try {
        const res = await UserService.updateMeasure(measure)
        console.log(res.data)
      } catch (error) {
        this.message = error.response?.data?.message || error.message || error.toString()
        console.error(error)
      }
    },
  },
}
</script>
