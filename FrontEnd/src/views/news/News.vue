<template>
  <section id="knowledge-base">
    <!-- search banner  -->
    <v-card flat class="knowledge-base-bg d-flex align-center justify-center text-center mb-7">
      <v-card-text>
        <p class="kb-title text-2xl font-weight-semibold primary--text mb-2">Hello, how can we help?</p>
        <p class="mb-7">or choose a category to quickly find the help you need</p>

        <v-form class="kb-search-input mx-auto">
          <v-text-field
            v-model="knowledgeBaseSearchQuery"
            outlined
            placeholder="Ask a question...."
            hide-details
            class="kb-search-input"
          >
            <template #prepend-inner>
              <v-icon size="23" class="mx-1">
                {{ icons.mdiMagnify }}
              </v-icon>
            </template>
          </v-text-field>
        </v-form>
      </v-card-text>
    </v-card>

    <!-- kb search content -->
    <div id="knowledge-base-content">
      <v-row class="kb-search-content-info match-height">
        <v-col v-for="(item, index) in filteredKB" :key="index" md="4" sm="6" cols="12" class="kb-search-content">
          <v-card color="text-center cursor-pointer" @click.native="$router.push(item.path)">
            <div class="kb-character-wrapper">
              <v-img contain :max-width="item.characterSize" class="mx-auto" :src="item.character"></v-img>
            </div>

            <!-- title -->
            <v-card-title class="justify-center pb-3">
              {{ item.title }}
            </v-card-title>
            <v-card-text>{{ item.desc }}</v-card-text>
          </v-card>
        </v-col>

        <!-- no result found -->
        <v-col v-show="!filteredKB.length" cols="12" class="text-center">
          <h4 class="mt-4">Search result not found!!</h4>
        </v-col>
      </v-row>
    </div>
  </section>
</template>

<script>
/* eslint-disable implicit-arrow-linebreak */
import { mdiMagnify } from '@mdi/js'
import { computed, ref } from '@vue/composition-api'

export default {
  setup() {
    const knowledgeBaseSearchQuery = ref('')
    const kbContentData = [
      {
        character: require('@/assets/images/3d-characters/pose-f-3.png'),
        category: 'Official News',
        characterSize: '139',
        title: 'Official Gov Press Release',
        desc: 'Be informed of the latest news on direct from the latest press releases from MOH',
        path: { name: 'OfficialNews' },
      },
      {
        character: require('@/assets/images/3d-characters/pose-f-28.png'),
        category: 'F&B related news',
        characterSize: '188',
        title: 'F&B-related news',
        desc: 'Be informed of latest news in the F&B industry',
        path: { name: 'F&BNews' },
      },
      {
        character: require('@/assets/images/3d-characters/pose-m-34.png'),
        category: 'Covid-related news',
        characterSize: '126',
        title: 'Covid-related news',
        desc: 'Be informed of the latest news on the pandemic and safety management measures',
        path: { name: 'CovidNews' },
      },
    ]

    const filteredKB = computed(() => {
      const knowledgeBaseSearchQueryLower = knowledgeBaseSearchQuery.value.toLowerCase()

      return kbContentData.filter(
        item =>
          // eslint-disable-next-line operator-linebreak
          item.title.toLowerCase().includes(knowledgeBaseSearchQueryLower) ||
          item.desc.toLowerCase().includes(knowledgeBaseSearchQueryLower),
      )
    })

    return {
      knowledgeBaseSearchQuery,
      kbContentData,
      filteredKB,
      icons: { mdiMagnify },
    }
  },
}
</script>

<style lang="scss">
@import '~@/plugins/vuetify/default-preset/preset/pages/knowledge-base.scss';
</style>
