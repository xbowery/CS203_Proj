<template>
  <div>
    <h1>Press Releases</h1>
    <v-row>
      <v-col cols="8">
        <v-toolbar color="blue darken-3" class="mb-1">
          <v-icon color="primary">
            {{ icons.mdiMagnify }}
          </v-icon>
          &nbsp;
          <v-text-field clearable flat hide-details label="Search" v-model="searchQuery"></v-text-field>
        </v-toolbar>
      </v-col>
    </v-row>
    <br />
    <v-row dense>
      <!-- Shown to inform that there are no results -->
      <v-col cols="8" v-if="!newsData.length">
        <v-card>
          <v-card-title class="title-nob">No news found</v-card-title>
          <v-card-text> Please try a different search term. </v-card-text>
        </v-card>
      </v-col>
      <!-- Loop over the various news -->
      <v-col cols="8" v-for="news in newsData" v-bind:key="news.title">
        <v-card :href="news.url" target="_blank">
          <v-card-title class="title-nob">{{ news.title }}</v-card-title>
          <v-card-text> Date: {{ news.createdAt }} </v-card-text>
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
    const NEWSTYPE = 'Gov'
    const newsData = ref([])
    const searchQuery = ref('')

    const getNewsData = async () => {
      const res = await UserService.getNews(NEWSTYPE)
      let { latestNews } = res.data
      parseNews(latestNews)
      newsData.value = latestNews
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
      let { news } = res.data
      parseNews(news)
      newsData.value = news
    }

    const parseDate = rawDateString => {
      const date = new Date(rawDateString)
      const year = date.getFullYear()
      let month = date.getMonth() + 1
      let dt = date.getDate()

      if (dt < 10) {
        dt = '0' + dt
      }
      if (month < 10) {
        month = '0' + month
      }

      return `${dt}/${month}/${year}`
    }

    const parseNews = newsArr => {
      newsArr.forEach(news => {
        news.createdAt = parseDate(news.createdAt)
      })
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
