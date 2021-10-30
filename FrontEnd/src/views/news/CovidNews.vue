<template>
  <div>
    <h1>General News</h1>
    <v-toolbar color="blue darken-3" class="mb-1">
      <v-icon color="primary">
        {{ icons.mdiMagnify }}
      </v-icon>
      &nbsp;
      <v-text-field clearable flat hide-details label="Search" v-model="searchQuery"></v-text-field>
    </v-toolbar>
    <br />
    <v-row>
      <v-col md="4" sm="6" cols="12" v-for="news in newsData" v-bind:key="news.title">
        <v-card :href="news.url" target="_blank">
          <v-img :src="news.imageUrl" height="250" />
          <v-card-title class="title-nob">{{ news.title }}</v-card-title>
          <v-card-text>
            {{ news.content }}
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </div>
</template>

<script>
import UserService from '@/services/user.service'
import { ref, onMounted, watch } from '@vue/composition-api'
import { mdiMagnify } from '@mdi/js'

export default {
  setup() {
    const NEWSTYPE = 'General'
    const newsData = ref([])
    const searchQuery = ref('')

    const getNewsData = async () => {
      const res = await UserService.getNews(NEWSTYPE)
      newsData.value = res.data.latestNews
    }

    const debounce = (fn, delay = 300) => {
      let timeoutID = null
      return function () {
        clearTimeout(timeoutID)
        let args = arguments
        let that = this
        timeoutID = setTimeout(function () {
          fn.apply(that, args)
        }, delay)
      }
    }

    const getNewsWithSearch = async val => {
      const res = await UserService.getNewsWithFilters(NEWSTYPE, val)
      newsData.value = res.data.news
    }

    watch(
      searchQuery,
      debounce(val => getNewsWithSearch(val)),
    )

    onMounted(getNewsData)

    return {
      newsData,
      getNewsData,
      icons: {
        mdiMagnify,
      },
      searchQuery,
      getNewsWithSearch,
    }
  },
}
</script>

<style lang="scss" scoped>
.title-nob {
  word-break: normal;
}
</style>
