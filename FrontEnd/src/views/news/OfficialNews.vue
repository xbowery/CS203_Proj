<template>
  <div>
    <h1>Press Releases</h1>
    <v-row dense>
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
import { ref, onMounted } from '@vue/composition-api'

export default {
  setup() {
    const newsData = ref([])

    const getNewsData = async () => {
      const res = await UserService.getNews('Gov')
      let { latestNews } = res.data

      latestNews.forEach(news => {
        news.createdAt = parseDate(news.createdAt)
      })
      newsData.value = latestNews
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

    onMounted(getNewsData)

    return {
      newsData,
      getNewsData,
    }
  },
}
</script>

<style lang="scss" scoped>
.title-nob {
  word-break: normal;
}
</style>
