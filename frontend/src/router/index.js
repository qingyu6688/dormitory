import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/layout/index.vue'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

/**
 * 路由配置
 * meta 参数说明：
 * - title: 页面标题，用于面包屑和标签页
 * - icon: 菜单图标（Element Plus Icons）
 * - requiresAuth: 是否需要登录（默认true）
 * - roles: 允许访问的角色数组
 * - keepAlive: 是否缓存页面
 * - hidden: 是否在菜单中隐藏
 */

const routes = [
  // ==================== 前台页面 ====================
  {
    path: '/front',
    component: () => import('@/layout/FrontLayout.vue'),
    meta: { 
      requiresAuth: false 
    },
    children: [
      {
        path: '',
        name: 'FrontHome',
        component: () => import('@/views/front/index.vue'),
        meta: { 
          title: '前台首页'
        }
      },
      {
        path: 'profile',
        name: 'FrontProfile',
        component: () => import('@/views/front/profile.vue'),
        meta: { 
          title: '个人中心',
          requiresAuth: true
        }
      },
      {
        path: 'room',
        name: 'FrontRoom',
        component: () => import('@/views/front/room.vue'),
        meta: { 
          title: '我的宿舍',
          requiresAuth: true
        }
      },
      {
        path: 'repair',
        name: 'FrontRepair',
        component: () => import('@/views/front/repair.vue'),
        meta: { 
          title: '在线报修',
          requiresAuth: true
        }
      },
      {
        path: 'visitor',
        name: 'FrontVisitor',
        component: () => import('@/views/front/visitor.vue'),
        meta: { 
          title: '访客登记',
          requiresAuth: true
        }
      },
      {
        path: 'bill',
        name: 'FrontBill',
        component: () => import('@/views/front/bill.vue'),
        meta: { 
          title: '费用查询',
          requiresAuth: true
        }
      }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/auth/Login.vue'),
    meta: { 
      title: '登录',
      requiresAuth: false 
    }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/auth/Register.vue'),
    meta: { 
      title: '注册',
      requiresAuth: false 
    }
  },
  {
    path: '/forgot-password',
    name: 'ForgotPassword',
    component: () => import('@/views/auth/ForgotPassword.vue'),
    meta: { 
      title: '找回密码',
      requiresAuth: false 
    }
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    meta: { title: '首页' },
    children: [
      // ==================== 首页 ====================
      {
        path: '/dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { 
          title: '首页',
          icon: 'HomeFilled',
          keepAlive: true
        }
      },

      // ==================== 系统管理 ====================
      {
        path: '/system',
        redirect: '/system/user',
        meta: { 
          title: '系统管理',
          icon: 'Setting'
        }
      },
      {
        path: '/system/user',
        name: 'User',
        component: () => import('@/views/system/user/index.vue'),
        meta: { 
          title: '用户管理',
          icon: 'User',
          keepAlive: true
        }
      },
      {
        path: '/system/role',
        name: 'Role',
        component: () => import('@/views/system/role/index.vue'),
        meta: { 
          title: '角色管理',
          icon: 'Avatar',
          keepAlive: true
        }
      },
      {
        path: '/system/menu',
        name: 'Menu',
        component: () => import('@/views/system/menu/index.vue'),
        meta: { 
          title: '菜单管理',
          icon: 'Menu',
          keepAlive: true
        }
      },

      // ==================== 数据管理 ====================
      {
        path: '/data',
        redirect: '/data/file',
        meta: { 
          title: '数据管理',
          icon: 'DataLine'
        }
      },
      {
        path: '/data/file',
        name: 'File',
        component: () => import('@/views/data/file/index.vue'),
        meta: { 
          title: '文件管理',
          icon: 'Folder',
          keepAlive: true
        }
      },
      {
        path: '/data/dict',
        name: 'Dict',
        component: () => import('@/views/data/dict/index.vue'),
        meta: { 
          title: '字典管理',
          icon: 'Collection',
          keepAlive: true
        }
      },

      // ==================== 日志管理 ====================
      {
        path: '/log',
        redirect: '/log/login',
        meta: { 
          title: '日志管理',
          icon: 'Document'
        }
      },
      {
        path: '/log/login',
        name: 'LoginLog',
        component: () => import('@/views/system/log/LoginLog.vue'),
        meta: { 
          title: '登录日志',
          icon: 'UserFilled',
          keepAlive: true
        }
      },
      {
        path: '/log/operation',
        name: 'OperationLog',
        component: () => import('@/views/system/log/OperationLog.vue'),
        meta: { 
          title: '操作日志',
          icon: 'Operation',
          keepAlive: true
        }
      },

      // ==================== 宿舍管理 ====================
      {
        path: '/dormitory',
        redirect: '/dormitory/building',
        meta: {
          title: '宿舍管理',
          icon: 'School'
        }
      },
      {
        path: '/dormitory/building',
        name: 'Building',
        component: () => import('@/views/dormitory/building/index.vue'),
        meta: {
          title: '宿舍楼管理',
          icon: 'OfficeBuilding',
          keepAlive: true
        }
      },
      {
        path: '/dormitory/room',
        name: 'Room',
        component: () => import('@/views/dormitory/room/index.vue'),
        meta: {
          title: '宿舍管理',
          icon: 'House',
          keepAlive: true
        }
      },
      {
        path: '/dormitory/checkin',
        name: 'CheckIn',
        component: () => import('@/views/dormitory/checkin/index.vue'),
        meta: {
          title: '入住管理',
          icon: 'UserFilled',
          keepAlive: true
        }
      },
      {
        path: '/dormitory/transfer',
        name: 'Transfer',
        component: () => import('@/views/dormitory/transfer/index.vue'),
        meta: {
          title: '调宿申请',
          icon: 'Switch',
          keepAlive: true
        }
      },
      {
        path: '/dormitory/repair',
        name: 'Repair',
        component: () => import('@/views/dormitory/repair/index.vue'),
        meta: {
          title: '报修管理',
          icon: 'Tools',
          keepAlive: true
        }
      },
      {
        path: '/dormitory/visitor',
        name: 'Visitor',
        component: () => import('@/views/dormitory/visitor/index.vue'),
        meta: {
          title: '访客登记',
          icon: 'Postcard',
          keepAlive: true
        }
      },
      {
        path: '/dormitory/hygiene',
        name: 'Hygiene',
        component: () => import('@/views/dormitory/hygiene/index.vue'),
        meta: {
          title: '卫生检查',
          icon: 'MagicStick',
          keepAlive: true
        }
      },
      {
        path: '/dormitory/utilitybill',
        name: 'UtilityBill',
        component: () => import('@/views/dormitory/utilitybill/index.vue'),
        meta: {
          title: '水电费管理',
          icon: 'Coin',
          keepAlive: true
        }
      },
      {
        path: '/dormitory/violation',
        name: 'Violation',
        component: () => import('@/views/dormitory/violation/index.vue'),
        meta: {
          title: '违纪记录',
          icon: 'Warning',
          keepAlive: true
        }
      },
      {
        path: '/dormitory/notice',
        name: 'DormNotice',
        component: () => import('@/views/dormitory/notice/index.vue'),
        meta: {
          title: '通知公告',
          icon: 'Bell',
          keepAlive: true
        }
      },
      {
        path: '/dormitory/statistics',
        name: 'DormStatistics',
        component: () => import('@/views/dormitory/statistics/index.vue'),
        meta: {
          title: '统计分析',
          icon: 'DataAnalysis',
          keepAlive: true
        }
      },

      // ==================== 个人中心 ====================
      {
        path: '/profile',
        name: 'Profile',
        component: () => import('@/views/profile/index.vue'),
        meta: { 
          title: '个人中心',
          icon: 'User',
          hidden: true
        }
      }
    ]
  },

  // ==================== 404 页面 ====================
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/error/404.vue'),
    meta: { 
      title: '页面不存在',
      requiresAuth: false,
      hidden: true
    }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  // 路由跳转后滚动到顶部
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      return { top: 0 }
    }
  }
})

// 白名单：不需要登录就可以访问的页面
const whiteList = ['/login', '/register']

// 全局前置守卫
router.beforeEach(async (to, from, next) => {
  // 设置页面标题
  document.title = to.meta.title ? `${to.meta.title} - TechFlow管理系统` : 'TechFlow管理系统'

  const token = localStorage.getItem('token')
  const userStore = useUserStore()

  // 判断是否需要登录
  const requiresAuth = to.meta.requiresAuth !== false

  if (token) {
    // 已登录
    if (to.path === '/login') {
      // 已登录访问登录页，重定向到首页
      next('/dashboard')
    } else {
      // 检查是否有用户信息
      if (!userStore.userInfo) {
        try {
          // 获取用户信息
          await userStore.fetchUserInfo()
          next()
        } catch (error) {
          // 获取用户信息失败，清除token并跳转到登录页
          console.error('获取用户信息失败:', error)
          userStore.token = ''
          localStorage.removeItem('token')
          ElMessage.error('登录状态已过期，请重新登录')
          next(`/login?redirect=${to.path}`)
        }
      } else {
        // 已有用户信息，直接放行
        next()
      }
    }
  } else {
    // 未登录
    if (whiteList.includes(to.path) || !requiresAuth) {
      // 在白名单中或不需要登录，直接放行
      next()
    } else {
      // 需要登录，重定向到登录页
      next(`/login?redirect=${to.path}`)
    }
  }
})

// 全局后置守卫
router.afterEach((to, from) => {
  // 可以在这里添加页面访问统计等逻辑
  console.log(`路由跳转: ${from.path} -> ${to.path}`)
})

// 路由错误处理
router.onError((error) => {
  console.error('路由错误:', error)
  ElMessage.error('页面加载失败，请刷新重试')
})

export default router
