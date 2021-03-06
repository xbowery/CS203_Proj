<template>
  <v-navigation-drawer
    :value="isDrawerOpen"
    app
    floating
    width="260"
    class="app-navigation-menu"
    :right="$vuetify.rtl"
    @input="val => $emit('update:is-drawer-open', val)"
  >
    <!-- Navigation Header -->
    <div class="vertical-nav-header d-flex items-center ps-6 pe-5 pt-5 pb-2">
      <router-link to="/" class="d-flex align-center text-decoration-none">
        <v-img
          :src="require('@/assets/images/logos/logo.png')"
          max-height="30px"
          max-width="30px"
          alt="logo"
          contain
          eager
          class="app-logo me-3"
        ></v-img>
        <v-slide-x-transition>
          <h2 class="app-title text--primary">Swisshack</h2>
        </v-slide-x-transition>
      </router-link>
    </div>

    <!-- Navigation Items -->
    <v-list expand shaped class="vertical-nav-menu-items pr-5">
      <nav-menu-link title="Home" :to="{ name: 'home' }" :icon="icons.mdiHomeOutline"></nav-menu-link>
      <nav-menu-link title="Restaurants" :to="{ name: 'restaurants' }" :icon="icons.mdiFood"></nav-menu-link>
      <nav-menu-link
        v-if="isBusinessOwner"
        title="Dashboard"
        :to="{ name: 'Dashboard' }"
        :icon="icons.mdiDomain"
      ></nav-menu-link>
      <nav-menu-link
        v-if="isBusinessOwner"
        title="Employees List"
        :to="{ name: 'Employees' }"
        :icon="icons.mdiAccountGroup"
      ></nav-menu-link>
      <nav-menu-link
        v-if="isEmployee"
        title="Covid Testing"
        :to="{ name: 'CovidTesting' }"
        :icon="icons.mdiHospitalBoxOutline"
      ></nav-menu-link>
      <nav-menu-link title="News" :to="{ name: 'News' }" :icon="icons.mdiDomain"></nav-menu-link>
      <nav-menu-link
        v-if="isAdmin"
        title="Users List"
        :to="{ name: 'UserList' }"
        :icon="icons.mdiAccountGroup"
      ></nav-menu-link>
      <nav-menu-link
        v-if="isAdmin"
        title="Restaurants List"
        :to="{ name: 'RestaurantList' }"
        :icon="icons.mdiFood"
      ></nav-menu-link>
      <nav-menu-link
        v-if="isAdmin"
        title="Measures List"
        :to="{ name: 'MeasuresList' }"
        :icon="icons.mdiFormatListBulletedSquare"
      ></nav-menu-link>
    </v-list>
  </v-navigation-drawer>
</template>

<script>
// eslint-disable-next-line object-curly-newline
import {
  mdiHomeOutline,
  mdiAlphaTBoxOutline,
  mdiEyeOutline,
  mdiCreditCardOutline,
  mdiTable,
  mdiFileOutline,
  mdiFormSelect,
  mdiFormatListBulletedSquare,
  mdiAccountCogOutline,
  mdiFood,
  mdiAccountCog,
  mdiDomain,
  mdiAccountGroup,
  mdiHospitalBoxOutline,
} from '@mdi/js'

import NavMenuLink from './NavMenuLink.vue'
import TokenService from '@/services/token.service'
import { Role } from '@/model/role'
export default {
  computed: {
    isAdmin() {
      const user = TokenService.getUser()
      return user.role === Role.admin
    },

    isEmployee() {
      const user = TokenService.getUser()
      return user.role === Role.employee
    },

    isBusinessOwner() {
      const user = TokenService.getUser()
      return user.role === Role.business
    },
  },
  components: {
    NavMenuLink,
  },
  props: {
    isDrawerOpen: {
      type: Boolean,
      default: null,
    },
  },
  setup() {
    return {
      icons: {
        mdiHomeOutline,
        mdiAlphaTBoxOutline,
        mdiEyeOutline,
        mdiCreditCardOutline,
        mdiTable,
        mdiFileOutline,
        mdiFormSelect,
        mdiFormatListBulletedSquare,
        mdiAccountCogOutline,
        mdiFood,
        mdiAccountCog,
        mdiDomain,
        mdiAccountGroup,
        mdiHospitalBoxOutline,
      },
    }
  },
}
</script>

<style lang="scss" scoped>
.app-title {
  font-size: 1.25rem;
  font-weight: 700;
  font-stretch: normal;
  font-style: normal;
  line-height: normal;
  letter-spacing: 0.3px;
}

// ? Adjust this `translateX` value to keep logo in center when vertical nav menu is collapsed (Value depends on your logo)
.app-logo {
  transition: all 0.18s ease-in-out;
  .v-navigation-drawer--mini-variant & {
    transform: translateX(-4px);
  }
}

@include theme(app-navigation-menu) using ($material) {
  background-color: map-deep-get($material, 'background');
}

.app-navigation-menu {
  .v-list-item {
    &.vertical-nav-menu-link {
      ::v-deep .v-list-item__icon {
        .v-icon {
          transition: none !important;
        }
      }
    }
  }
}

// You can remove below style
// Upgrade Banner
.app-navigation-menu {
  .upgrade-banner {
    position: absolute;
    bottom: 13px;
    left: 50%;
    transform: translateX(-50%);
  }
}
</style>
