<template>
  <div>
    <!-- <h1>Home</h1> -->
      <!-- Influencing The Influencer -->
    <v-row>
      <v-col
        md="4"
        sm="6"
        cols="12"
      >
        <v-card>
          <v-img
            src="https://onecms-res.cloudinary.com/image/upload/s--K2WvoHkn--/c_fill%2Cg_auto%2Ch_468%2Cw_830/f_auto%2Cq_auto/people-wearing-face-masks-exercise-public-singapore-covid-19--1-.jpg?itok=alMMD0AI"
            height="250"
          />
          <v-card-title>
            'New normal': 7 ways Singaporeans is moving towards living with COVID-19
          </v-card-title>
          <v-card-text>
            Singapore will change testing and quarantine requirements for COVID-19 as Prime Minister Lee Hsien Loong said on Saturday (Oct 9) that people should update their mindsets and not be paralysed by fear of the virus.
          </v-card-text>
          <v-card-actions class="dense">
            <v-btn
              color="primary"
              text
            >
              Details
            </v-btn>
            <v-spacer></v-spacer>
            <v-btn
              icon
              @click="isCardDetailsVisible = !isCardDetailsVisible"
            >
              <v-icon>{{ isCardDetailsVisible ? icons.mdiChevronUp : icons.mdiChevronDown }}</v-icon>
            </v-btn>
          </v-card-actions>
          <v-expand-transition>
            <div v-show="isCardDetailsVisible">
              <v-divider></v-divider>
              <v-card-text>
                The country will also introduce more vaccination-differentiated measures and expand home recovery to include people as it moves towards the new normal of living with COVID-19.
              </v-card-text>
            </div>
          </v-expand-transition>
        </v-card>
      </v-col>
      <!-- send request card -->
      <v-col
        md="4"
        sm="6"
        cols="12"
      >
        <v-card>
          <v-img
            class="white--text align-end"
            src="https://onecms-res.cloudinary.com/image/upload/s--f82gjYjR--/c_fill%2Cg_auto%2Ch_468%2Cw_830/f_auto%2Cq_auto/people-wearing-face-masks-singapore-amid-covid-19-outbreak.jpg?itok=mJWMmYeR"
            height="250"
          />
            <!-- Title, Subtitle & Action Button -->
                <v-card-title>
                  11 COVID-19 deaths as Singapore reports more than 3,000 new cases for fifth straight day
                </v-card-title>
                <v-card-text>
                Singapore reported more than 3,000 new COVID-19 cases for a fifth straight day on Saturday (Oct 9) as 11 more people died from complications due to the virus. 
                </v-card-text>
              <v-card-actions class="dense">
            <v-btn
              color="primary"
              text
            >
              Details
            </v-btn>
            <v-spacer></v-spacer>
            <v-btn
              icon
              @click="isCardDetailsVisible = !isCardDetailsVisible"
            >
              <v-icon>{{ isCardDetailsVisible ? icons.mdiChevronUp : icons.mdiChevronDown }}</v-icon>
            </v-btn>
          </v-card-actions>
          <v-expand-transition>
            <div v-show="isCardDetailsVisible">
              <v-divider></v-divider>
              <v-card-text>
                The fatalities are five men and six women aged between 56 and 90. Four of them were unvaccinated against COVID-19, three were partially vaccinated and four were fully vaccinated.
              </v-card-text>
            </div>
          </v-expand-transition>
        </v-card>
      </v-col>
      <!-- card explore -->
      <v-col
        md="4"
        sm="6"
        cols="12"
        class="align-self-start"
      >
        <v-card>
          <v-img
            src="https://onecms-res.cloudinary.com/image/upload/s--y_hFeCaA--/f_auto%2Cq_auto/c_fill%2Cg_auto%2Ch_676%2Cw_1200/la-changi-t5-13_0.jpg?itok=CRFIz44h"
            height="250"
          ></v-img>
          <v-card-title>
            Singapore to extend vaccinated travel lane scheme to eight more countries, including US, UK, Spain
          </v-card-title>
          <v-card-text>
            Singapore is extending its vaccinated travel lane scheme to eight more countries, allowing those who are fully vaccinated against COVID-19 to enter Singapore without having to serve quarantine.
          </v-card-text>
          <v-card-actions class="dense">
            <v-btn
              color="primary"
              text
            >
              Details
            </v-btn>
            <v-spacer></v-spacer>
            <v-btn
              icon
              @click="isCardDetailsVisible = !isCardDetailsVisible"
            >
              <v-icon>{{ isCardDetailsVisible ? icons.mdiChevronUp : icons.mdiChevronDown }}</v-icon>
            </v-btn>
          </v-card-actions>
          <v-expand-transition>
            <div v-show="isCardDetailsVisible">
              <v-divider></v-divider>
              <v-card-text>
                From Oct 19, fully vaccinated travellers from Canada, Denmark, France, Italy, the Netherlands, Spain, the United Kingdom and the United States will be able to enter Singapore under this arrangement, the Civil Aviation Authority of Singapore (CAAS) announced on Saturday (Oct 9). 
              </v-card-text>
            </div>
          </v-expand-transition>
        </v-card>
      </v-col>
    </v-row>
  </div>
</template>

<script>
import { ref } from '@vue/composition-api'
//import useVuetify from '~@/plugins/vuetify'
import {
  mdiChevronUp,
  mdiChevronDown,
  mdiCartPlus,
  mdiShareVariantOutline,
  mdiLockOpenOutline,
  mdiStarOutline,
  mdiAccountOutline,
  mdiTrendingUp,
  mdiHelpCircleOutline,
} from '@mdi/js'

export default {
  setup() {
    //const { rootThemeClasses } = useVuetify()
    const isCardDetailsVisible = false
    const rating = ref(5)

    return {
      //rootThemeClasses,
      isCardDetailsVisible,
      rating,

      // icons
      icons: {
        mdiChevronUp,
        mdiChevronDown,
        mdiCartPlus,
        mdiShareVariantOutline,
        mdiLockOpenOutline,
        mdiStarOutline,
        mdiAccountOutline,
        mdiTrendingUp,
        mdiHelpCircleOutline,
      },
    }
  },
}
</script>

<style lang="scss" scoped>
@import '~vuetify/src/styles/styles.sass';
.triangle-card {
  position: relative;
  .triangle-bg {
    position: absolute;
    bottom: 0;
    @include ltr() {
      right: 0;
    }
    @include rtl() {
      left: 0;
    }
  }
  .gamification-trophy {
    position: absolute;
    bottom: 10%;
    @include ltr() {
      right: 8%;
    }
    @include rtl() {
      left: 8%;
    }
  }
}
.gamification-tree-4,
.gamification-john-pose-2,
.gamification-tree {
  position: absolute;
}
.gamification-tree {
  top: 10%;
  @include ltr() {
    right: 0;
  }
  @include rtl() {
    left: 0;
  }
}
.gamification-john-pose-2 {
  bottom: 0;
  @include ltr() {
    right: 15%;
  }
  @include rtl() {
    left: 15%;
  }
}
.gamification-tree-4 {
  bottom: 0;
  @include ltr() {
    right: 30%;
  }
  @include rtl() {
    left: 35%;
  }
}

@media (max-width: 600px) {
  .gamification-tree,
  .gamification-tree-4 {
    display: none;
  }
  .gamification-john-pose-2 {
    @include ltr() {
      right: 5%;
    }
    @include rtl() {
      left: 5%;
    }
  }
}

@media (max-width: 500px) {
  .gamification-john-pose-2 {
    max-width: 70px;
  }
}

@media (max-width: 400px) {
  .page-title {
    font-size: 1.2rem !important;
  }
}
</style>