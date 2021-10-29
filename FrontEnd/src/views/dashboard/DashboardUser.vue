<template>
  <v-card class="greeting-card">
    <v-row class="ma-0 pa-0">
      <v-col cols="8">
        <v-card-title class="text-no-wrap pt-1 ps-2"> Good Morning, {{ user.username }} 🥳 </v-card-title>
        <v-card-subtitle class="text-no-wrap ps-2"> Covid Suisse Business Dashboard </v-card-subtitle>
        <v-card-text class="d-flex align-center mt-2 pb-2 ps-2">
          <div>
            <p class="text-xl font-weight-semibold primary--text mb-2">{{ time() }}</p>
            <v-btn v-for="(item, index) in items" :key="index" small color="primary">
              {{ items[0].restaurant.name }}
            </v-btn>
          </div>
        </v-card-text>
      </v-col>

      <v-col cols="4">
        <v-img
          contain
          height="180"
          width="159"
          :src="require(`@/assets/images/misc/triangle-${$vuetify.theme.dark ? 'dark' : 'light'}.png`)"
          class="greeting-card-bg"
        ></v-img>
        <v-img
          contain
          height="108"
          max-width="83"
          class="greeting-card-trophy"
          src="@/assets/images/misc/tree-3.png"
        ></v-img>
      </v-col>
    </v-row>
  </v-card>
</template>

<style lang="scss" scoped>
.greeting-card {
  position: relative;
  .greeting-card-bg {
    position: absolute;
    bottom: 0;
    right: 0;
  }
  .greeting-card-trophy {
    position: absolute;
    bottom: 10%;
    right: 8%;
  }
}
// rtl
.v-application {
  &.v-application--is-rtl {
    .greeting-card-bg {
      right: initial;
      left: 0;
      transform: rotateY(180deg);
    }
    .greeting-card-trophy {
      left: 8%;
      right: initial;
    }
  }
}
</style>

<script>
import { defineComponent } from '@vue/composition-api'
import UserService from '@/services/user.service'
import { mapGetters } from 'vuex'

export default defineComponent({
  setup() {},
  props: {
    username: String,
  },

  computed: {
    ...mapGetters({
      user: 'auth/user',
    }),
  },

  data() {
    return {
      items: [],
      keys: ['name'],
    }
  },

  async mounted() {
    try {
      const res = await UserService.getCrowdLevels()
      this.items = res.data
    } catch (error) {
      console.error(error)
    }
  },
  methods: {
    time() {
      const today = new Date()
      const time = today.getHours() + ':' + ('00' + today.getMinutes()).slice(-2)

      return time
    },
  },
})
</script>
