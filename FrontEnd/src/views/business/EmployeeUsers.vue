<template>
  <v-row>
    <v-col cols="10" sm="4">
      <statistics-card-vertical
        :change="totalUsers.change"
        :color="totalUsers.color"
        :icon="totalUsers.icon"
        :statistics="total_users"
        :stat-title="totalUsers.statTitle"
        :subtitle="totalUsers.subtitle"
      ></statistics-card-vertical>
    </v-col>

    <v-col cols="12" sm="4">
      <statistics-card-vertical
        :change="activeUsers.change"
        :color="activeUsers.color"
        :icon="activeUsers.icon"
        :statistics="active_users"
        :stat-title="activeUsers.statTitle"
        :subtitle="activeUsers.subtitle"
      ></statistics-card-vertical>
    </v-col>

    <v-col cols="12" sm="4">
      <statistics-card-vertical
        :change="pendingUsers.change"
        :color="pendingUsers.color"
        :icon="pendingUsers.icon"
        :statistics="pending_users"
        :stat-title="pendingUsers.statTitle"
        :subtitle="pendingUsers.subtitle"
      ></statistics-card-vertical>
    </v-col>

    <v-col cols="12">
      <employee-datatable :username="user.username"></employee-datatable>
    </v-col>
  </v-row>
</template>
<script>

import { mdiPoll, mdiLabelVariantOutline, mdiHelpCircleOutline } from '@mdi/js'
import StatisticsCardVertical from '@/components/statistics-card/StatisticsCardVertical.vue'
import { mapGetters } from 'vuex'
import UserService from '@/services/user.service'
import EmployeeDatatable from './EmployeeDatatable.vue'

export default {
  data() {
    return {
      items: [],
      total_users: 0,
      active_users: 0,
      pending_users: 0,
    }
  },

  components: {
    StatisticsCardVertical,
    EmployeeDatatable,
  },

  computed: {
    ...mapGetters({
      user: 'auth/user',
    }),
  },

  async mounted() {
    try {
      const res = await UserService.getEmployees(this.username)
      this.items = res.data
      this.total_users = this.items.length.toString(10)
      this.items.forEach(item => {
        if (item.employee.status == 'Pending') this.pending_users += 1
        // else if (item.employee.status == 'Active') this.active_users += 1
      })

      this.active_users = (this.total_users - this.pending_users).toString(10)
    } catch (error) {
      console.log(error)
    }
  },

  setup() {
    const totalUsers = {
      statTitle: 'Total Users',
      icon: mdiPoll,
      color: 'success',
    }

    const activeUsers = {
      statTitle: 'Active Users',
      icon: mdiLabelVariantOutline,
      color: 'primary',
    }

    const pendingUsers = {
      statTitle: 'Pending Users',
      icon: mdiHelpCircleOutline,
      color: 'warning',
    }

    return {
      totalUsers,
      activeUsers,
      pendingUsers,
    }
  },
}
</script>
