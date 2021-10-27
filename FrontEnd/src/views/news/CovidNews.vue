<template>
  <div>
    <h1>General News</h1>
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
import { ref, onMounted } from '@vue/composition-api'

export default {
  setup() {
    const newsData = ref([])

    const getNewsData = async () => {
      const res = await UserService.getNews('General')
      newsData.value = res.data.latestNews
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
