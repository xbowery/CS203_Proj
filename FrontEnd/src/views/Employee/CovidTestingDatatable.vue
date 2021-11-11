<template>
  <v-card>
    <v-card-title> Covid Testing History </v-card-title>

    <v-divider class="mt-1"></v-divider>

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

      <div class="d-flex align-center flex-wrap">
        <v-dialog v-model="dialog" width="500">
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              v-bind="attrs"
              v-on="on"
              color="primary"
              class="mb-4 me-3"
              @click.stop="isAddNewUserSidebarActive = !isAddNewUserSidebarActive"
            >
              <v-icon>{{ icons.mdiPlus }}</v-icon>
              <span>Add New Test</span>
            </v-btn>
          </template>

          <validation-observer ref="obs">
            <v-card slot-scope="{ invalid }">
              <v-form @submit.prevent="save">
                <v-card-title class="text-h5 grey lighten-2">{{ formTitle }}</v-card-title>

                <v-container>
                  <v-row>
                    <v-col cols="12" md="6">
                      <validation-provider name="type" rules="required" v-slot="{ errors }">
                        <v-select
                          :items="dropdown_type"
                          v-model="editedItem.type"
                          :error-messages="errors[0]"
                          label="Select Type of Test"
                          required
                        ></v-select>
                      </validation-provider>
                    </v-col>
                    <v-col cols="12" md="6">
                      <validation-provider name="result" rules="required" v-slot="{ errors }">
                        <v-select
                          :items="dropdown_result"
                          v-model="editedItem.result"
                          :error-messages="errors[0]"
                          label="Select result of test"
                          required
                        ></v-select>
                      </validation-provider>
                    </v-col>
                  </v-row>
                  <v-row>
                    <v-col cols="12" sm="6">
                      <v-menu
                        ref="menu"
                        v-model="menu"
                        :close-on-content-click="false"
                        :return-value.sync="editedItem.date"
                        transition="scale-transition"
                      >
                        <template v-slot:activator="{ on, attrs }">
                          <validation-provider name="Date" rules="required">
                            <v-text-field
                              v-model="editedItem.date"
                              label="Day of Test"
                              :error-messages="errors"
                              prepend-icon="mdi-calendar"
                              readonly
                              v-bind="attrs"
                              v-on="on"
                              required
                            ></v-text-field>
                          </validation-provider>
                        </template>
                        <v-date-picker
                          v-model="editedItem.date"
                          no-title
                          scrollable
                          :allowed-dates="disableFutureDates"
                        >
                          <v-spacer></v-spacer>
                          <v-btn text color="primary" @click="menu = false" v-bind="attrs" v-on="on"> Cancel </v-btn>
                          <v-btn text color="primary" @click="$refs.menu.save(editedItem.date)"> OK </v-btn>
                        </v-date-picker>
                      </v-menu>
                    </v-col>
                  </v-row>
                </v-container>

                <v-divider></v-divider>

                <v-card-actions>
                  <v-spacer></v-spacer>
                  <v-btn color="primary" text @click="dialog = false" type="button"> Cancel </v-btn>
                  <v-btn color="primary" type="submit" :disabled="invalid"> Save </v-btn>
                </v-card-actions>
              </v-form>
            </v-card>
          </validation-observer>
        </v-dialog>
      </div>
    </v-card-text>

    <!-- removed item-key="date" from below -->
    <v-data-table :headers="headers" :items="items" :search="search" class="table-rounded" hide-default-footer>
      <template v-slot:top>
        <v-toolbar flat>
          <v-dialog v-model="dialogDelete" max-width="600px">
            <v-card>
              <v-card-title class="text-h5 justify-center">Are you sure you want to delete this Covid Test?</v-card-title>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="error"  @click="closeDelete">Cancel</v-btn>
                <v-btn color="success" @click="deleteItemConfirm">OK</v-btn>
                <v-spacer></v-spacer>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-toolbar>
      </template>
      <template #[`item.actions`]="{ item }">
        <v-icon small color="secondary" outlined class="mr-2" @click="editItem(item)"> Edit </v-icon>

        <v-icon small color="secondary" outlined class="mr-2" @click="deleteItem(item)"> Delete </v-icon>
      </template>
    </v-data-table>
  </v-card>
</template>

<script>
import { mdiSquareEditOutline, mdiDotsVertical } from '@mdi/js'
import { ValidationProvider, ValidationObserver } from 'vee-validate'
import '@/validators'
import UserService from '@/services/user.service'
import Ctest from '@/model/ctest'

export default {
  components: { ValidationProvider, ValidationObserver },
  props: {
    username: String,
  },
  data: () => ({
    dropdown_type: [{ text: 'ART' }, { text: 'PCR' }],
    dropdown_result: [{ text: 'Pending' }, { text: 'Negative' }, { text: 'Positive' }],
    menu: false,
    errors: '',
    on: '',
    attrs: '',
    isAddNewUserSidebarActive: '',

    dialog: false,
    dialogDelete: false,
    editedIndex: -1,
    editedItem: {
      type: '',
      result: '',
      date: new Date(Date.now() - new Date().getTimezoneOffset() * 60000).toISOString().substr(0, 10),
    },

    items: [],

    defaultItem: {
      type: '',
      result: '',
      date: new Date(Date.now() - new Date().getTimezoneOffset() * 60000).toISOString().substr(0, 10),
    },
  }),

  async mounted() {
    try {
      const res = await UserService.getCtests()
      this.items = res.data

      var ctest = this.items[0]
      this.items.forEach(item => {
        if (ctest.date < item.date) {
          ctest = item
        }
      })
      this.$emit('set_latest', ctest)
    } catch (error) {
      console.error(error)
    }
  },

  watch: {
    dialog(val) {
      val || this.close()
    },
    dialogDelete(val) {
      val || this.closeDelete()
    },
  },

  computed: {
    formTitle() {
      return this.editedIndex === -1 ? 'New Covid Test' : 'Edit Covid Test'
    },
  },

  methods: {
    editItem(item) {
      this.editedIndex = this.items.indexOf(item)
      this.editedItem = Object.assign({}, item)
      this.dialog = true
    },

    close() {
      this.dialog = false
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem)
        this.editedIndex = -1
      })
    },

    deleteItem(item) {
      this.editedIndex = this.items.indexOf(item)
      this.editedItem = Object.assign({}, item)
      console.log(this.editedIndex)
      console.log(this.editedItem)
      this.dialogDelete = true
    },

    closeDelete() {
      this.dialogDelete = false
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem)
        this.editedIndex = -1
      })
    },

    deleteItemConfirm() {
      this.handleDeleteCtest(this.editedItem.id)
      if (!this.message) {
        this.closeDelete()
      }
    },

    save() {
      if (this.editedIndex > -1) {
        Object.assign(this.items[this.editedIndex], this.editedItem)

        const ctest = new Ctest('', '', '')
        ctest.type = this.items[this.editedIndex].type
        ctest.result = this.items[this.editedIndex].result
        ctest.date = this.items[this.editedIndex].date

        this.handleEditCtest(ctest, this.editedItem.id)
      } else {
        const ctest = new Ctest('', '', '')
        ctest.type = this.editedItem.type
        ctest.result = this.editedItem.result
        ctest.date = this.editedItem.date

        this.handleNewCtest(ctest)
        this.items.push(this.editedItem)
      }
      this.close()
    },

    async handleDeleteCtest(ctestId) {
      try {
        const res = await UserService.deleteCtest(ctestId)
        console.log(res.data)
        this.reloadTable()
      } catch (error) {
        this.message = error.response?.data?.message || error.message || error.toString()
        console.error(error)
      }
    },

    async handleNewCtest(ctest) {
      try {
        const res = await UserService.postCtest(ctest)
        console.log(res)
      } catch (error) {
        console.log(error)
      } finally {
        this.editedItem.type = ''
        this.editedItem.result = ''
        this.editedItem.date = new Date(Date.now() - new Date().getTimezoneOffset() * 60000).toISOString().substr(0, 10)
        this.dialog = false
        this.reloadTable()
      }
    },

    async handleEditCtest(ctest, ctestId) {
      try {
        const res = await UserService.updateCtest(ctestId, ctest)
        console.log(res.data)
      } catch (error) {
        this.message = error.response?.data?.message || error.message || error.toString()
        console.error(error)
      }
    },

    async reloadTable() {
      try {
        const res = await UserService.getCtests()
        this.items = res.data
        if (this.items.length == 0) {
          this.$emit('set_latest', this.ctestEmpty)
        }
        var ctest = this.items[0]
        this.items.forEach(item => {
          if (ctest.date < item.date) {
            ctest = item
          }
        })
        this.$emit('set_latest', ctest)
      } catch (error) {
        console.error(error)
      }
    },

    disableFutureDates(val) {
      return val <= new Date().toISOString().substr(0, 10)
    },
  },

  setup() {
    return {
      dialog: false,
      search: '',
      headers: [
        { text: 'Date', value: 'date' },
        { text: 'Type of test (PCR/ART)', value: 'type' },
        { text: 'Result', value: 'result' },
        { text: 'ACTIONS', value: 'actions', sortable: true },
      ],
      // icons
      icons: {
        mdiSquareEditOutline,
        mdiDotsVertical,
      },
    }
  },
}
</script>
