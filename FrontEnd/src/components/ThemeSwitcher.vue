<template>
  <v-tooltip bottom>
    <template v-slot:activator="{ on, attrs }">
      <div v-bind="attrs" v-on="on">
        <v-fade-transition mode="out-in">
          <v-icon :key="$vuetify.theme.dark" @click="$vuetify.theme.dark = !$vuetify.theme.dark">
            {{ $vuetify.theme.dark ? icons.mdiWeatherSunny : icons.mdiWeatherNight }}
          </v-icon>
        </v-fade-transition>
      </div>
    </template>
    <span>Switch to {{ $vuetify.theme.dark ? 'Light' : 'Dark' }}</span>
  </v-tooltip>
</template>

<script>
import { mdiWeatherNight, mdiWeatherSunny } from '@mdi/js'

export default {
  setup() {
    return {
      icons: {
        mdiWeatherNight,
        mdiWeatherSunny,
      },
    }
  },
}
</script>

<style></style>
