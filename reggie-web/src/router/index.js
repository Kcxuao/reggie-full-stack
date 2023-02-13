import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/home/employee'
    },
    {
      path: '/login',
      component: () => import("@/views/Login/Login.vue")
    },
    {
      path: '/home',
      component: () => import('@/views/Home/Home.vue'),
      children: [
        {
          path: 'employee',
          component: () => import('@/views/Home/Employee/Employee.vue')
        },
        {
          path: 'addOrUpdateEmployee',
          component: () => import('@/views/AddOrUpdate/Employee.vue')
        },
        {
          path: 'category',
          component: () => import('@/views/Home/Category/Category.vue')
        },
        {
          path: 'dish',
          component: () => import('@/views/Home/Dish/Dish.vue')
        },
        {
          path: 'addDish',
          component: () => import('@/views/AddOrUpdate/Dish.vue')
        },
        {
          path: 'setmeal',
          component: () => import('@/views/Home/SetMeal/SetMeal.vue')
        },
        {
          path: 'addSetMeal',
          component: () => import('@/views/AddOrUpdate/SetMeal.vue')
        },
        {
          path: 'orders',
          component: () => import('@/views/Home/Orders/Orders.vue')
        },
      ]
    }
  ]
})

router.beforeEach((to, from, next) => {
  if (to.path === '/login') {
    return next()
  }

  const token = localStorage.getItem('token')
  if (!token) {
    return next('/login')
  }

  next()

})

export default router
