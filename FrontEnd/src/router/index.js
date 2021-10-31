import Vue from 'vue'
import VueRouter from 'vue-router'
import { Role } from '@/model/role'
import TokenService from '@/services/token.service'

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
    meta: { authorize: [] },
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
    name: 'ForgotPassword',
    component: () => import('@/views/user-management/ForgotPassword.vue'),
    meta: {
      layout: 'blank',
    },
  },
  {
    path: '/ResetPassword',
    name: 'ResetPassword',
    component: () => import('@/views/user-management/ResetPassword.vue'),
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
    path: '/RegisterConfirmation',
    name: 'RegisterConfirmation',
    component: () => import('@/views/user-management/RegisterConfirmation.vue'),
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
    path: '/error-403',
    name: 'error-403',
    component: () => import('@/views/NotAuthorized.vue'),
    meta: {
      layout: 'blank',
    },
  },
  {
    path: '/restaurants',
    name: 'restaurants',
    component: () => import('@/views/business/Restaurants.vue'),
    meta: { authorize: [] },
  },
  {
    path: '/settings',
    name: 'settings',
    component: () => import('@/views/user-management/user-settings/AccountSettings.vue'),
    meta: { authorize: [] },
  },
  {
    path: '/Dashboard',
    name: 'Dashboard',
    component: () => import('@/views/dashboard/Dashboard.vue'),
    meta: { authorize: [Role.business] },
  },
  {
    path: '/Employees',
    name: 'Employees',
    component: () => import('@/views/business/EmployeeUsers.vue'),
    meta: { authorize: [Role.business] },
  },
  {
    path: '/CovidTesting',
    name: 'CovidTesting',
    component: () => import('@/views/Employee/CovidTesting.vue'),
    meta: { authorize: [Role.employee] },
  },
  {
    path: '/News',
    name: 'News',
    component: () => import('@/views/news/News.vue'),
    meta: { authorize: [] },
  },
  {
    path: '/CovidNews',
    name: 'CovidNews',
    component: () => import('@/views/news/NewsDisplay.vue'),
    props: { NEWSTYPE: 'General', header: 'General' },
    meta: { authorize: [] },
  },
  {
    path: '/F&BNews',
    name: 'F&BNews',
    component: () => import('@/views/news/NewsDisplay.vue'),
    props: { NEWSTYPE: 'Restaurant', header: 'F&B Related' },
    meta: { authorize: [] },
  },
  {
    path: '/OfficialNews',
    name: 'OfficialNews',
    component: () => import('@/views/news/OfficialNews.vue'),
    meta: { authorize: [] },
  },
  {
    path: '/UserList',
    name: 'UserList',
    component: () => import('@/views/user-management/admin/UserList.vue'),
    meta: { authorize: [Role.admin] },
  },
  {
    path: '/RestaurantList',
    name: 'RestaurantList',
    component: () => import('@/views/user-management/admin/RestaurantList.vue'),
    meta: { authorize: [Role.admin] },
  },
  {
    path: '/MeasuresList',
    name: 'MeasuresList',
    component: () => import('@/views/user-management/admin/MeasuresList.vue'),
    meta: { authorize: [Role.admin] },
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes,
})

router.beforeEach((to, from, next) => {
  // redirect to login page if not logged in and trying to access a restricted page
  const { authorize } = to.meta
  const currentUser = TokenService.getUser()

  if (authorize) {
    if (!currentUser) {
      // not logged in so redirect to login page with the return url
      return next({ name: 'login', query: { returnUrl: to.name } })
    }

    // check if route is restricted by role
    if (authorize.length && !authorize.includes(currentUser.role)) {
      // role not authorised so redirect to unauthorised page
      return next({ name: 'error-403' })
    }
  }

  next()
})

export default router
