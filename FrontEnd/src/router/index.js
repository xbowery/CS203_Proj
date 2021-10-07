import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    redirect: 'home',
  },
  {
    path: '/home',
    name: 'home',
    component: () => import('@/views/Home.vue'),
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('@/views/user-management/Login.vue'),
    meta: {
      layout: 'blank',
    },
  },
  {
    path: '/ForgotPassword',
    name: 'login',
    component: () => import('@/views/user-management/ForgotPassword.vue'),
    meta: {
      layout: 'blank',
    },
  },
  {
    path: '/register',
    name: 'register',
    component: () => import('@/views/user-management/Register.vue'),
    meta: {
      layout: 'blank',
    },
  },
  {
    path: '/error-404',
    name: 'error-404',
    component: () => import('@/views/Error.vue'),
    meta: {
      layout: 'blank',
    },
  },
  {
    path: '*',
    redirect: 'error-404',
  },
  {
    path: '/restaurants',
    name: 'restaurants',
    component: () => import('@/views/business/Restaurants.vue'),
  },
  {
    path: '/settings',
    name: 'settings',
    component: () => import('@/views/user-management/user-settings/AccountSettings.vue'),
  },
  {
    path: '/bDashboard',
    name: 'bDashboard',
    component: () => import('@/views/business/BDashboard.vue'),
  },
  {
    path: '/crowdControl',
    name: 'crowdControl',
    component: () => import('@/views/business/CrowdControl.vue'),
  },
  {
    path: '/Dashboard',
    name: 'Dashboard',
    component: () => import('@/views/dashboard/Dashboard.vue'),
  },
  {
    path: '/Employees',
    name: 'Employees',
    component: () => import('@/views/business/EmployeeUsers.vue'),
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes,
})

export default router
