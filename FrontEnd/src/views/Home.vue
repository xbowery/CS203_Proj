<template>
  <div class="home">
    <!-- <h1>Home</h1> -->
    <v-row></v-row>
    <v-row>
    <!-- congratulations flora -->
    <v-col
      cols="10"
      md="12"
      order="1"
      order-md="2"
      class="align-self-end mb-10 mt-sm-10 mt-md-0"
      
    >
      <v-card class="align-center" >
        <v-row>
          <v-col
            cols="8"
            sm="6"
          >
            <v-card-title class="page-title flex-nowrap text-2xl">
              <span class="text-no-wrap">Good Morning</span>
              <span class="text-no-wrap font-weight-bold mx-1">John!</span>
              <span>🎉</span>
            </v-card-title>
            <v-card-text>
              What would you like to know more about today?
            </v-card-text>
          </v-col>
          <v-col
            cols="4"
            sm="6"
          >
            <div>
              <v-img
                width="120"
                src="@/assets/images/misc/tree-4.png"
                class="gamification-tree-4"
              ></v-img>
              <v-img
                width="128"
                src="@/assets/images/3d-characters/pose-2.png"
                class="gamification-john-pose-2"
              ></v-img>
              <v-img
                width="100"
                src="@/assets/images/misc/tree.png"
                class="gamification-tree"
              ></v-img>
            </div>
          </v-col>
        </v-row>
      </v-card>
    </v-col>

  </v-row>

    <v-item-group>
      <v-container>
        <v-row>
          <v-item>
            <v-card class="mx-auto my-12" max-width="260">
              <template slot="progress">
                <v-progress-linear height="10"></v-progress-linear>
              </template>

              <v-img
                height="250"
                src="https://cdn.pixabay.com/photo/2015/09/02/12/29/pedestrians-918471_1280.jpg"
              ></v-img>
              <v-card-title>{{ headingGathering }}</v-card-title>
              <v-card-text>
                <div>{{ textGathering }}</div>
              </v-card-text>
            </v-card>
          </v-item>

          <v-item>
            <v-card class="mx-auto my-12" max-width="270">
              <template slot="progress">
                <v-progress-linear height="10"></v-progress-linear>
              </template>

              <v-img height="250" src="https://cdn.pixabay.com/photo/2015/01/09/11/22/fitness-594143_1280.jpg"></v-img>
              <v-card-title>{{ headingGym }}</v-card-title>
              <v-card-text>
                <div>{{ textGym }}</div>
              </v-card-text>
            </v-card>
          </v-item>
          <v-item>
            <v-card class="mx-auto my-12" max-width="270">
              <template slot="progress">
                <v-progress-linear height="10"></v-progress-linear>
              </template>

              <v-img
                height="250"
                src="https://cdn.pixabay.com/photo/2016/11/23/15/48/audience-1853662_1280.jpg"
              ></v-img>
              <v-card-title>{{ headingEvents }}</v-card-title>
              <v-card-text>
                <div>{{ textEvents }}</div>
              </v-card-text>
            </v-card>
          </v-item>
          <v-item>
            <v-card class="mx-auto my-12" max-width="270">
              <template slot="progress">
                <v-progress-linear height="10"></v-progress-linear>
              </template>

              <v-img
                height="260"
                src="https://cdn.pixabay.com/photo/2015/03/26/10/28/restaurant-691397_1280.jpg"
              ></v-img>
              <v-card-title>{{ headingDining }}</v-card-title>
              <v-card-text>
                <div>{{ textDining }}</div>
              </v-card-text>
            </v-card>
          </v-item>
        </v-row>
      </v-container>
    </v-item-group>
  </div>
</template>

<script>
export default {
  name: 'Home',
  data() {
    return {
      headingGathering: 'Gatherings',
      textGathering: 'Max of 2 pax',
      headingGym: 'Gym',
      textGym: 'Max of 2 pax',
      headingEvents: 'Events',
      textEvents: 'Up to vaccinated 100 persons',
      headingDining: 'Dining',
      textDining: 'Max of 2 vaccinated pax per table',
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
