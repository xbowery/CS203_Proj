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
            <v-card slot-scope="{ invalid, handleSubmit }">
              <v-form @submit.prevent="handleSubmit(handleLogin)">
                <v-card-title class="text-h5 grey lighten-2"> New Test </v-card-title>

                <v-container>
                  <v-row>
                    <v-col cols="12" md="6">
                      <validation-provider name="type" rules="required" v-slot="{ errors }">
                        <v-select
                          :items="dropdown_type"
                          v-model="type"
                          :error-messages="errors[0]"
                          :success="valid"
                          label="Select Type of Test"
                          required
                        ></v-select>
                      </validation-provider>
                    </v-col>
                    <v-col cols="12" md="6">
                      <validation-provider name="result" rules="required" v-slot="{ errors }">
                        <v-select
                          :items="dropdown_result"
                          v-model="result"
                          :error-messages="errors[0]"
                          :success="valid"
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
                        :return-value.sync="date"
                        transition="scale-transition"
                      >
                        <template v-slot:activator="{ on, attrs }">
                          <validation-provider name="Date" rules="required">
                            <v-text-field
                              v-model="date"
                              label="Day of Test"
                              :error-messages="errors"
                              :success="valid"
                              prepend-icon="mdi-calendar"
                              readonly
                              v-bind="attrs"
                              v-on="on"
                              required
                            ></v-text-field>
                          </validation-provider>
                        </template>
                        <v-date-picker v-model="date" no-title scrollable :allowed-dates="disableFutureDates">
                          <v-spacer></v-spacer>
                          <v-btn text color="primary" @click="menu = false" v-bind="attrs" v-on="on"> Cancel </v-btn>
                          <v-btn text color="primary" @click="$refs.menu.save(date)"> OK </v-btn>
                        </v-date-picker>
                      </v-menu>
                    </v-col>
                  </v-row>
                </v-container>

                <v-divider></v-divider>

                <v-card-actions>
                  <v-spacer></v-spacer>
                  <v-btn color="primary" text @click="dialog = false"> Cancel </v-btn>
                  <v-btn color="primary" type="submit" :disabled="invalid"> Save </v-btn>
                </v-card-actions>
              </v-form>
            </v-card>
          </validation-observer>
        </v-dialog>
      </div>
    </v-card-text>
<p>{{type}}</p>
<p>{{result}}</p>
<p>{{date}}</p>


    <v-data-table
      :headers="headers"
      :items="usreList"
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
import data from './covidtestingdatatable-data.js'
import { ValidationProvider, ValidationObserver } from 'vee-validate'
import '@/validators'

export default {
  components: { ValidationProvider, ValidationObserver },
  data: () => ({
    dropdown_type: [{ text: 'ART' }, { text: 'PCR' }],
    dropdown_result: [{ text: 'Pending' }, { text: 'Negative' }, { text: 'Positive' }],
    menu: false,

    type: '',
    result: '',
    date: new Date(Date.now() - new Date().getTimezoneOffset() * 60000).toISOString().substr(0, 10),
  }),
  methods: {
    disableFutureDates(val) {
      return val <= new Date().toISOString().substr(0, 10)
    },
    async submit() {
      // const result = await this.$refs.obs.validate();
      await this.$refs.obs.validate()
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
      ],
      usreList: data,
      // icons
      icons: {
        mdiSquareEditOutline,
        mdiDotsVertical,
      },
    }
  },
}
</script>
