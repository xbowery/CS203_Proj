<template>
<v-card flat class="pa-3 mt-2">
<v-card-text>
      <v-form class="multi-col-validation mt-6">
        <v-row>
          <v-col md="6" cols="12">
            <v-text-field v-model="restaurantId" label="Restaurant ID" dense outlined></v-text-field>
          </v-col>

          <v-col md="6" cols="12">
            <v-text-field v-model="designation" label="Designation" dense outlined></v-text-field>
          </v-col>

        

          <v-col cols="12">
            <v-btn color="primary" class="me-3 mt-4"> Register </v-btn>
            <v-btn color="secondary" outlined class="mt-4" type="reset" @click.prevent="resetForm"> Cancel </v-btn>
          </v-col>
        </v-row>
      </v-form>
    </v-card-text>
  </v-card>
</template>
<script>
export default {
    data() {
        return {
            restaurantId: 0,
            designation: "",
        }
    }
}
</script>