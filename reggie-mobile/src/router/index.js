import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      component: () => import('@/views/Login/Login.vue')
    },
    {
      path: '/home',
      component: () => import('@/views/Home/Home.vue')
    },
    {
      path: '/user',
      component: () => import('@/views/User/User.vue')
    },
    {
      path: '/orders',
      component: () => import('@/views/Orders/Orders.vue')
    },
    {
      path: '/addOrders',
      component: () => import('@/views/Orders/AddOrders/AddOrders.vue')
    },
    {
      path: '/address',
      component: () => import('@/views/Address/Address.vue')
    },
    {
      path: '/addressEdit',
      component: () => import('@/views/Address/AddressEdit/AddressEdit.vue')
    },
    {
      path: '/success',
      component: () => import('@/views/Orders/paySuccess/paySuccess.vue')
    }

  ]
})

router.beforeEach((to, from, next) => {

  const user = JSON.parse(sessionStorage.getItem('user'))

  if (to.path === '/login') {
    return next()
  }

  if (!user || !user.token) {
    return next('/login')
  }

  next()
})

export default router
