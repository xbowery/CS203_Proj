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
              <v-form @submit.prevent="handleSubmit">
                <v-card-title class="text-h5 grey lighten-2"> New Test </v-card-title>

                <v-container>
                  <v-row>
                    <v-col cols="12" md="6">
                      <validation-provider name="type" rules="required" v-slot="{ errors }">
                        <v-select
                          :items="dropdown_type"
                          v-model="ctest.type"
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
                          v-model="ctest.result"
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
                        :return-value.sync="ctest.date"
                        transition="scale-transition"
                      >
                        <template v-slot:activator="{ on, attrs }">
                          <validation-provider name="Date" rules="required">
                            <v-text-field
                              v-model="ctest.date"
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
                        <v-date-picker v-model="ctest.date" no-title scrollable :allowed-dates="disableFutureDates">
                          <v-spacer></v-spacer>
                          <v-btn text color="primary" @click="menu = false" v-bind="attrs" v-on="on"> Cancel </v-btn>
                          <v-btn text color="primary" @click="$refs.menu.save(ctest.date)"> OK </v-btn>
                        </v-date-picker>
                      </v-menu>
                    </v-col>
                  </v-row>
                </v-container>

                <v-divider></v-divider>

                <v-card-actions>
                  <v-spacer></v-spacer>
                  <v-btn color="primary" text @click="dialog = false" type="button"> Cancel </v-btn>
                  <v-btn color="primary" type="submit" :disabled="invalid" @click="handleSubmit"> Save </v-btn>
                </v-card-actions>
              </v-form>
            </v-card>
          </validation-observer>
        </v-dialog>
      </div>
    </v-card-text>

    <v-data-table
      :headers="headers"
      :items="items"
      :search="search"
      item-key="date"
      class="table-rounded"
      hide-default-footer
    >
    </v-data-table>
  </v-card>
</template>

<script>
import { mdiSquareEditOutline, mdiDotsVertical } from '@mdi/js'
import { ValidationProvider, ValidationObserver } from 'vee-validate'
import '@/validators'
import UserService from '@/services/user.service'

export default {
  components: { ValidationProvider, ValidationObserver },
  props:{
    username: String,
  },
  data: () => ({
    dropdown_type: [{ text: 'ART' }, { text: 'PCR' }],
    dropdown_result: [{ text: 'Pending' }, { text: 'Negative' }, { text: 'Positive' }],
    menu: false,
    items: [],
    errors:'',
    on:'',
    attrs:'',
    isAddNewUserSidebarActive:'',



    ctest:{
      type: '',
      result: '',
      date: new Date(Date.now() - new Date().getTimezoneOffset() * 60000).toISOString().substr(0, 10),
    }

  }),

  async mounted() {
    console.log(this.username)
    try {
      const res = await UserService.getCtests(this.username)
      this.items = res.data
      console.log(this.items)
    } catch (error) {
      console.error(error)
    }
  },

  methods: {
    disableFutureDates(val) {
      return val <= new Date().toISOString().substr(0, 10)
    },
    async handleSubmit() {
      console.log("SUBMITTED")
      try {
          const res = await UserService.postCtest(this.username, this.ctest)
          console.log(res)
        } catch (error) {
          console.log(error)
        } finally {
          this.ctest.type = ''
          this.ctest.result = ''
          this.ctest.date = new Date(Date.now() - new Date().getTimezoneOffset() * 60000).toISOString().substr(0, 10)
          this.dialog = false
        }
    },
    async reloadTable(){
      try {
        const res = await UserService.getCtests(this.username)
        this.items = res.data
        console.log(this.items)
      } catch (error) {
        console.error(error)
      }
    }
  },

  setup() {
    return {
      dialog: false,
      search: '',
      headers: [
        { text: 'Date', value: 'date' },
        { text: 'Type of test (PCR/ART)', value: 'type' },
        { text: 'Result', value: 'result' },
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
