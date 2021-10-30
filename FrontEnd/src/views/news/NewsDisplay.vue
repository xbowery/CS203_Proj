<template>
  <div>
    <h1>{{ header }} News</h1>
    <v-toolbar color="blue darken-3" class="mb-1">
      <v-icon color="primary">
        {{ icons.mdiMagnify }}
      </v-icon>
      &nbsp;
      <v-text-field clearable flat hide-details label="Search" v-model="searchQuery"></v-text-field>
    </v-toolbar>
    <br />
    <v-row v-if="error">
      <!-- Shown when there is an error -->
      <v-col md="4" sm="6" cols="12">
        <v-card>
          <v-card-title class="title-nob">An error occurred</v-card-title>
          <v-card-text> Please try again later. </v-card-text>
        </v-card>
      </v-col>
    </v-row>
    <v-row v-if="!error">
      <!-- Shown to inform that there are no results -->
      <v-col md="4" sm="6" cols="12" v-if="!newsData.length">
        <v-card>
          <v-card-title class="title-nob">No news found</v-card-title>
          <v-card-text> Please try a different search term. </v-card-text>
        </v-card>
      </v-col>
      <!-- Loop over the various news -->
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
  props: {
    NEWSTYPE: {
      type: String,
      required: true,
    },
    header: {
      type: String,
      required: true,
    },
  },
  setup(props) {
    const { NEWSTYPE } = props
    const newsData = ref([])
    const searchQuery = ref('')
    const error = ref(false)

    const getNewsData = async () => {
      error.value = false
      try {
        const res = await UserService.getNews(NEWSTYPE)
        newsData.value = res.data.latestNews
      } catch (err) {
        error.value = true
      }
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
      if (!val) {
        await getNewsData()
        return
      }

      error.value = false
      try {
        const res = await UserService.getNewsWithFilters(NEWSTYPE, val)
        newsData.value = res.data.news
      } catch (err) {
        error.value = true
      }
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
      error,
    }
  },
}
</script>

<style lang="scss" scoped>
.title-nob {
  word-break: normal;
}
</style>
