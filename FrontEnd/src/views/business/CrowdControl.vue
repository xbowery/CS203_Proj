<template>
  <v-item-group>
    <v-container>
      <v-row justify="center" align="center">
        <v-col v-for="n in 1" :key="n" cols="8" md="4">
          <h1>Capacity: {{ current }}/{{ maxCapacity }}</h1>
        </v-col>
        <v-col v-for="n in 1" :key="n" cols="8" md="4">
          <h1>Crowd level: {{ crowdLvl }}</h1>
        </v-col>
      </v-row>

      <v-row justify="center" align="center">
        <v-col v-for="n in 1" :key="n" cols="8" md="4" justify="center" align="center">
          <v-btn depressed color="primary" large @click="increment(1)"><h2>+1</h2></v-btn>
        </v-col>
        <v-col v-for="n in 1" :key="n" cols="8" md="4" justify="center" align="center">
          <v-btn depressed color="error" large @click="increment(-1)"><h2>-1</h2></v-btn>
        </v-col>
      </v-row>

      <v-row justify="center" align="center">
        <v-col v-for="n in 1" :key="n" cols="8" md="4" justify="center" align="center">
          <v-btn depressed color="primary" large @click="increment(2)"><h2>+2</h2></v-btn>
        </v-col>
        <v-col v-for="n in 1" :key="n" cols="8" md="4" justify="center" align="center">
          <v-btn depressed color="error" large @click="increment(-2)"><h2>-2</h2></v-btn>
        </v-col>
      </v-row>
    </v-container>
  </v-item-group>
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
