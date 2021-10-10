<template>
  <v-card>
    <v-card-title class="text-h5 grey lighten-2"> New Test </v-card-title>

    <v-form v-model="valid">
      <v-container>
        <v-row>
          <v-col cols="12" md="6">
            <v-overflow-btn label="Select Type of Test" :items="dropdown_type"></v-overflow-btn>
          </v-col>
          <v-col cols="12" md="6">
            <v-overflow-btn label="Select Result of Test" :items="dropdown_result"></v-overflow-btn>
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
              offset-y
              min-width="auto"
            >
              <template v-slot:activator="{ on, attrs }">
                <v-text-field
                  v-model="date"
                  label="Day of Test"
                  prepend-icon="mdi-calendar"
                  readonly
                  v-bind="attrs"
                  v-on="on"
                ></v-text-field>
              </template>
              <v-date-picker v-model="date" no-title scrollable>
                <v-spacer></v-spacer>
                <v-btn text color="primary" @click="menu = false"> Cancel </v-btn>
                <v-btn text color="primary" @click="$refs.menu.save(date)"> OK </v-btn>
              </v-date-picker>
            </v-menu>
          </v-col>
        </v-row>
      </v-container>
    </v-form>

    <v-divider></v-divider>

    <v-card-actions>
      <v-spacer></v-spacer>
      <v-btn color="primary" text @click="dialog = false"> Cancel </v-btn>
      <v-btn color="primary" text @click="dialog = false"> Save </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
export default {
  data: () => ({
    dropdown_type: [{ text: 'ART' }, { text: 'PCR' }],
    dropdown_result: [{ text: 'Pending' }, { text: 'Negative' }, { text: 'Positive' }],

    date: new Date(Date.now() - new Date().getTimezoneOffset() * 60000).toISOString().substr(0, 10),
    menu: false,
  }),
}
</script>
