import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  // 첫페이지 == 전체 목록 페이지
  {
    path: '/',
    alias: '/dept',
    name: 'dept',
    component: () => import('../components/dept/DeptList.vue')
  },
  // 부서 추가 페이지 
  {
    path: '/add/dept',
    name: 'add-dept',
    component: () => import('../components/dept/AddDept.vue')
  },
  // 부서 상세 페이지 
  {
    path: '/dept/:dno',
    name: 'dept-detail',
    component: () => import('../components/dept/DeptDetail.vue')
  },  

  // 전체 사원 목록 페이지
  {
    path: '/emp',
    name: 'emp',
    component: () => import('../components/emp/EmpList.vue')
  },
  // 사원 추가 페이지 
  {
    path: '/add/emp',
    name: 'add-emp',
    component: () => import('../components/emp/AddEmp.vue')
  },
  // 사원 상세 페이지 
  {
    path: '/emp/:eno',
    name: 'emp-detail',
    component: () => import('../components/emp/EmpDetail.vue')
  },  
  // 전체 Faq 목록 페이지
  {
    path: '/faq',
    name: 'faq',
    component: () => import('../components/faq/FaqList.vue')
  },
  // Faq 추가 페이지 
  {
    path: '/add/faq',
    name: 'add-faq',
    component: () => import('../components/faq/AddFaq.vue')
  },
  // Faq 상세 페이지 
  {
    path: '/faq/:no',
    name: 'faq-detail',
    component: () => import('../components/faq/FaqDetail.vue')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
