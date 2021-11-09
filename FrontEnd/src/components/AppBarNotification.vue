<template>
  <v-menu
    offset-y
    left
    nudge-bottom="22"
    :elevation="$vuetify.theme.dark ? 9 : 8"
    content-class="list-style notification-menu-content"
  >
    <template v-slot:activator="{ on, attrs }">
      <v-badge :content="messages" :value="messages" color="success" overlap>
        <v-icon v-bind="attrs" v-on="on">
          {{ icons.mdiBellOutline }}
        </v-icon>
      </v-badge>
    </template>

    <v-card class="app-bar-notification-content-container">
      <v-list class="py-0">
        <!-- Header -->
        <v-list-item class="d-flex" link>
          <div class="d-flex align-center justify-space-between flex-grow-1">
            <span class="font-weight-semibold">Notifications</span>
            <v-chip class="v-chip-light-bg primary--text font-weight-semibold" small
              >{{ messages }} New Notifications</v-chip
            >
          </div>
        </v-list-item>
        <v-divider></v-divider>

        <!-- Notifications -->
        <template v-for="(notification, index) in notifications">
          <v-list-item :key="notification.id" link @click="markSingleNotificationRead(notification.id)">
            <!-- Content -->
            <v-list-item-content class="d-block">
              <v-list-item-title class="text-sm font-weight-semibold"> Important! </v-list-item-title>
              <v-list-item-subtitle class="text-sm">
                {{ notification.text }}
              </v-list-item-subtitle>
            </v-list-item-content>

            <!-- Item Action/Time -->
            <v-list-item-action>
              <span class="text--secondary text-xs">{{ notification.dateRepr }}</span>
            </v-list-item-action>
          </v-list-item>
          <v-divider :key="index"></v-divider>
        </template>
        <v-list-item key="read-all-btn" class="read-all-btn-list-item">
          <v-btn v-if="messages !== 0" block color="primary" @click="markAllRead()"> Read All Notifications </v-btn>

          <v-btn v-else block color="info" @click="refreshNotifications()"> All caught up! Refresh. </v-btn>
        </v-list-item>
      </v-list>
    </v-card>
  </v-menu>
</template>

<script>
import { mdiBellOutline } from '@mdi/js'
import UserService from '@/services/user.service'
import { ref, onMounted } from '@vue/composition-api'

export default {
  setup() {
    const messages = ref(0)
    const notifications = ref([])

    const getNotification = async () => {
      try {
        const res = await UserService.getNotification()
        let rawData = res.data
        parseNotifications(rawData)

        notifications.value = rawData
        messages.value = notifications.value.length
      } catch (err) {
        console.error(err)
      }
    }

    onMounted(getNotification)

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

    const parseNotifications = notificationsArr => {
      notificationsArr.forEach(notification => {
        notification.dateRepr = parseDate(notification.date)
      })
    }

    const markAllRead = () => {
      messages.value = 0
      notifications.value = []
      UserService.readAllNotification()
    }

    const markSingleNotificationRead = notificationId => {
      UserService.readNotification(notificationId)
      messages.value--
      notifications.value = notifications.value.filter(notification => notification.id !== notificationId)
    }

    const refreshNotifications = () => {
      getNotification()
    }

    return {
      messages,
      notifications,
      getNotification,
      markAllRead,
      markSingleNotificationRead,
      refreshNotifications,
      icons: {
        mdiBellOutline,
      },
    }
  },
}
</script>

<style lang="scss">
@import '~vuetify/src/styles/styles.sass';

.app-bar-notification-content-container {
  .read-all-btn-list-item {
    padding-top: 14px;
    padding-bottom: 14px;
    min-height: unset;
  }
}

.ps-user-notifications {
  max-height: calc(var(--vh, 1vh) * 80);
}

.notification-menu-content {
  @media #{map-get($display-breakpoints, 'xs-only')} {
    min-width: calc(100vw - (1.5rem * 2)) !important;
    left: 50% !important;
    transform: translateX(-50%);
  }
}
</style>
