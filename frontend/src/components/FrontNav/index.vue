<template>
  <header class="front-header" :class="{ 'is-scrolled': isScrolled }">
    <div class="header-content">
      <div class="logo-section" @click="goToHome">
        <div class="logo-icon">
          <el-icon><School /></el-icon>
        </div>
        <div class="logo-text">
          <h1 class="logo-title">智慧宿舍</h1>
          <p class="logo-subtitle">管理系统</p>
        </div>
      </div>
      <nav class="nav-menu">
        <a @click.prevent="goToHome" class="nav-item" :class="{ active: currentRoute === '/front' }">
          <el-icon><HomeFilled /></el-icon>
          首页
        </a>
        <a @click.prevent="goToPage('/front/room')" class="nav-item" :class="{ active: currentRoute === '/front/room' }">
          <el-icon><House /></el-icon>
          我的宿舍
        </a>
        <a @click.prevent="goToPage('/front/repair')" class="nav-item" :class="{ active: currentRoute === '/front/repair' }">
          <el-icon><Tools /></el-icon>
          在线报修
        </a>
        <a @click.prevent="goToPage('/front/visitor')" class="nav-item" :class="{ active: currentRoute === '/front/visitor' }">
          <el-icon><User /></el-icon>
          访客登记
        </a>
        <a @click.prevent="goToPage('/front/bill')" class="nav-item" :class="{ active: currentRoute === '/front/bill' }">
          <el-icon><Wallet /></el-icon>
          费用查询
        </a>
      </nav>
      <div class="header-actions">
        <!-- 管理员、宿管员、辅导员显示进入后台按钮 -->
        <el-button v-if="userInfo?.roleId === 1 || userInfo?.roleId === 2 || userInfo?.roleId === 3 || userInfo?.roleId === 5" class="cta-btn" @click="goToAdmin">
          进入后台
        </el-button>
        
        <!-- 用户信息下拉框 -->
        <el-dropdown v-if="userInfo" @command="handleCommand" class="user-dropdown">
          <div class="user-info">
            <el-avatar :size="36" :src="userInfo?.avatar || ''">
              <el-icon v-if="!userInfo?.avatar"><User /></el-icon>
              <span v-else>{{ userInfo?.nickname?.charAt(0) || 'U' }}</span>
            </el-avatar>
            <span class="username">{{ userInfo?.nickname || '用户' }}</span>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">
                <el-icon><User /></el-icon>
                个人中心
              </el-dropdown-item>
              <el-dropdown-item divided command="logout">
                <el-icon><SwitchButton /></el-icon>
                退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
        
        <!-- 未登录状态 -->
        <el-button v-else class="login-btn" @click="goToLogin">
          登录
        </el-button>
      </div>
    </div>
  </header>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessageBox } from 'element-plus'
import { 
  User, SwitchButton, School, HomeFilled, House, Tools, Wallet 
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const isScrolled = ref(false)

// 获取用户信息
const userInfo = computed(() => userStore.userInfo)

// 获取当前路由
const currentRoute = computed(() => route.path)

// 监听滚动事件
const handleScroll = () => {
  isScrolled.value = window.scrollY > 50
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})

// 前往后台管理
const goToAdmin = () => {
  if (userInfo.value) {
    router.push('/dashboard')
  } else {
    router.push('/login')
  }
}

// 前往登录
const goToLogin = () => {
  router.push('/login')
}

// 返回首页
const goToHome = () => {
  router.push('/front')
}

// 前往指定页面
const goToPage = (path) => {
  // 检查是否登录
  if (!userInfo.value) {
    ElMessageBox.confirm('请先登录后使用该功能', '提示', {
      confirmButtonText: '去登录',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      router.push('/login')
    })
    return
  }
  router.push(path)
}

// 下拉菜单命令处理
const handleCommand = async (command) => {
  switch (command) {
    case 'profile':
      router.push('/front/profile')
      break
    case 'logout':
      ElMessageBox.confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        await userStore.logout()
        router.push('/login')
      })
      break
  }
}
</script>

<style scoped>
/* 顶部导航 */
.front-header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 70px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  box-shadow: 0 2px 20px rgba(0, 0, 0, 0.08);
  z-index: 1000;
  transition: all 0.3s;
}

.front-header.is-scrolled {
  box-shadow: 0 4px 30px rgba(0, 0, 0, 0.12);
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
}

.logo-section {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  transition: all 0.3s;
}

.logo-section:hover {
  transform: scale(1.05);
}

.logo-icon {
  width: 48px;
  height: 48px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 24px;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
  transition: all 0.3s;
}

.logo-section:hover .logo-icon {
  transform: rotate(5deg);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.5);
}

.logo-text {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.logo-title {
  font-size: 20px;
  font-weight: 700;
  margin: 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  line-height: 1;
}

.logo-subtitle {
  font-size: 12px;
  color: #6b7280;
  margin: 0;
  line-height: 1;
}

.cta-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  padding: 10px 24px;
  font-weight: 600;
  transition: all 0.3s;
  border-radius: 10px;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
}

.cta-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.5);
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 16px;
}

.login-btn {
  background: transparent;
  color: #667eea;
  border: 2px solid #667eea;
  padding: 8px 20px;
  font-weight: 600;
  transition: all 0.3s;
  border-radius: 10px;
}

.login-btn:hover {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-color: transparent;
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
}

.user-dropdown {
  cursor: pointer;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 6px 16px;
  border-radius: 24px;
  transition: all 0.3s;
  border: 2px solid transparent;
}

.user-info:hover {
  background: rgba(102, 126, 234, 0.1);
  border-color: rgba(102, 126, 234, 0.2);
}

.user-info .el-avatar {
  border: 2px solid rgba(102, 126, 234, 0.3);
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  transition: all 0.3s;
}

.user-info:hover .el-avatar {
  transform: scale(1.1);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.username {
  color: #1f2937;
  font-weight: 500;
  font-size: 14px;
}

.nav-menu {
  display: flex;
  gap: 8px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #4b5563;
  text-decoration: none;
  font-weight: 500;
  font-size: 15px;
  transition: all 0.3s;
  position: relative;
  padding: 10px 16px;
  cursor: pointer;
  border-radius: 10px;
  background: transparent;
}

.nav-item .el-icon {
  font-size: 18px;
  transition: all 0.3s;
}

.nav-item::before {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 0;
  height: 3px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 2px;
  transition: width 0.3s;
}

.nav-item:hover {
  color: #667eea;
  background: rgba(102, 126, 234, 0.08);
}

.nav-item:hover .el-icon {
  transform: translateY(-2px);
}

.nav-item.active {
  color: #667eea;
  background: rgba(102, 126, 234, 0.12);
}

.nav-item:hover::before,
.nav-item.active::before {
  width: 80%;
}

/* 响应式 */
@media (max-width: 768px) {
  .nav-menu {
    display: none;
  }

  .username {
    display: none;
  }

  .header-actions {
    gap: 8px;
  }

  .cta-btn {
    padding: 8px 16px;
    font-size: 14px;
  }
}
</style>
