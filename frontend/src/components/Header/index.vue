<template>
  <div class="header-container">
    <div class="header-left">
      <el-icon class="collapse-icon" @click="toggleSidebar">
        <Expand v-if="collapsed" />
        <Fold v-else />
      </el-icon>
      
      <div class="breadcrumb-container">
        <el-icon class="breadcrumb-icon"><HomeFilled /></el-icon>
        <el-breadcrumb separator-icon="ArrowRight" class="custom-breadcrumb">
          <el-breadcrumb-item
            v-for="(item, index) in breadcrumbs"
            :key="index"
            :to="index < breadcrumbs.length - 1 ? item.path : undefined"
          >
            <span :class="{ 'is-current': index === breadcrumbs.length - 1 }">
              {{ item.title }}
            </span>
          </el-breadcrumb-item>
        </el-breadcrumb>
      </div>
    </div>

    <div class="header-right">
      <!-- 管理员、宿管员、辅导员显示前往前台按钮 -->
      <el-button v-if="userInfo?.roleId === 1 || userInfo?.roleId === 2 || userInfo?.roleId === 3 || userInfo?.roleId === 5" class="front-btn" @click="goToFront">
        <el-icon><Monitor /></el-icon>
        <span>前往前台</span>
      </el-button>
      
      <el-dropdown @command="handleCommand">
        <div class="user-info">
          <el-avatar :size="32" :src="userInfo?.avatar || ''">
            <el-icon v-if="!userInfo?.avatar"><User /></el-icon>
            <span v-else>{{ userInfo?.nickname?.charAt(0) || 'U' }}</span>
          </el-avatar>
          <span class="username">{{ userInfo?.nickname || '用户' }}</span>
          <el-icon><ArrowDown /></el-icon>
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
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAppStore } from '@/stores/app'
import { useUserStore } from '@/stores/user'
import { ElMessageBox } from 'element-plus'
import {
  Expand,
  Fold,
  ArrowDown,
  User,
  SwitchButton,
  HomeFilled,
  ArrowRight,
  Monitor
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const appStore = useAppStore()
const userStore = useUserStore()

const collapsed = computed(() => appStore.sidebarCollapsed)
const userInfo = computed(() => userStore.userInfo)

// 面包屑导航
const breadcrumbs = computed(() => {
  const matched = route.matched.filter(item => item.meta && item.meta.title)
  return matched.map(item => ({
    path: item.path,
    title: item.meta.title
  }))
})

// 切换侧边栏
const toggleSidebar = () => {
  appStore.toggleSidebar()
}

// 前往前台
const goToFront = () => {
  router.push('/front')
}

// 下拉菜单命令处理
const handleCommand = async (command) => {
  switch (command) {
    case 'profile':
      router.push('/profile')
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

<style lang="scss" scoped>
@use '@/styles/variables.scss' as *;

.header-container {
  height: $header-height;
  background: $bg-white;
  border-bottom: 1px solid $border-color;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 $spacing-lg;
  transition: width $transition-base, margin-left $transition-base;
  will-change: width;
}

.header-left {
  display: flex;
  align-items: center;
  gap: $spacing-md;

  .collapse-icon {
    font-size: 20px;
    cursor: pointer;
    transition: all $transition-fast;
    padding: $spacing-xs;
    border-radius: $radius-sm;

    &:hover {
      color: $primary-color;
      background-color: rgba($primary-color, 0.1);
    }
  }

  .breadcrumb-container {
    display: flex;
    align-items: center;
    gap: $spacing-sm;
    padding: $spacing-xs $spacing-md;
    background-color: $bg-color;
    border-radius: $radius-lg;

    .breadcrumb-icon {
      font-size: 16px;
      color: $primary-color;
    }

    .custom-breadcrumb {
      :deep(.el-breadcrumb__item) {
        .el-breadcrumb__inner {
          color: $text-regular;
          font-weight: 400;
          transition: all $transition-fast;

          &:hover {
            color: $primary-color;
          }

          &.is-link {
            font-weight: 400;
          }
        }

        &:last-child {
          .el-breadcrumb__inner {
            color: $text-primary;
            font-weight: 500;

            &.is-current {
              color: $primary-color;
            }
          }
        }

        .el-breadcrumb__separator {
          color: $text-secondary;
          margin: 0 $spacing-xs;
        }
      }
    }
  }
}

.header-right {
  display: flex;
  align-items: center;
  gap: $spacing-md;

  .front-btn {
    display: flex;
    align-items: center;
    gap: 6px;
    padding: 8px 16px;
    border-radius: $radius-sm;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    border: none;
    transition: all $transition-fast;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
    }
  }

  .user-info {
    display: flex;
    align-items: center;
    gap: $spacing-sm;
    cursor: pointer;
    padding: $spacing-xs $spacing-sm;
    border-radius: $radius-sm;
    transition: background $transition-fast;

    &:hover {
      background: $bg-color;
    }

    .username {
      font-size: $font-size-base;
      color: $text-primary;
    }
  }
}
</style>
