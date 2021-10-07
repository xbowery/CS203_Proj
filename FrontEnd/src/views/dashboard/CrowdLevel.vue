<template>
  <v-card>
    <v-item-group>
      <v-container>
        <v-row justify="center" align="center">
          <v-col v-for="n in 1" :key="n" cols="8" md="4">
            <h2>Capacity: {{ current }}/{{ maxCapacity }}</h2>
          </v-col>
          <v-col v-for="n in 1" :key="n" cols="8" md="4">
            <h2>Crowd level: {{ crowdLvl }}</h2>
          </v-col>
        </v-row>

        <v-row justify="center" align="center">
          <v-col v-for="n in 1" :key="n" cols="6" md="4" justify="center" align="center">
            <v-btn depressed color="primary" large @click="increment(1)">+1</v-btn>
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
            <v-btn depressed color="error" large @click="increment(-2)">-2</v-btn>
          </v-col>
        </v-row>
      </v-container>
    </v-item-group>
  </v-card>
</template>

<script>
export default {
  data() {
    return {
      current: 0,
      maxCapacity: 100,
      crowdLvl: 'low',
    }
  },
  methods: {
    increment(count) {
      if (this.current + count >= 0) {
        this.current += count
      }
    },
  },
}
</script>
