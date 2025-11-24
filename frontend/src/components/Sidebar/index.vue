<template>
  <div class="sidebar-container" :class="{ 'is-collapsed': collapsed }">
    <div class="sidebar-logo">
      <img v-if="!collapsed" src="../../assets/logo.png" alt="Logo" class="logo-img">
      <span v-if="!collapsed" class="logo-title">管理系统</span>
      <img v-else src="../../assets/logo.png" alt="Logo" class="logo-img-small">
    </div>

    <el-scrollbar class="sidebar-scrollbar">
      <el-menu
        :default-active="activeMenu"
        :collapse="collapsed"
        :unique-opened="true"
        :router="true"
        background-color="#001529"
        text-color="#A6ADB4"
        active-text-color="#FFFFFF"
        @select="handleMenuSelect"
      >
        <sidebar-item
          v-for="menu in menuList"
          :key="menu.id"
          :menu="menu"
        />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useAppStore } from '@/stores/app'
import { useUserStore } from '@/stores/user'
import SidebarItem from './SidebarItem.vue'

const route = useRoute()
const appStore = useAppStore()
const userStore = useUserStore()

const collapsed = computed(() => appStore.sidebarCollapsed)
const activeMenu = computed(() => route.path)

// 从Store获取菜单数据
const menuList = computed(() => userStore.menus || [])

// 菜单选择处理
const handleMenuSelect = (path) => {
  // 由路由导航处理，这里不需要额外操作
}
</script>

<style lang="scss" scoped>
@use '@/styles/variables.scss' as *;

.sidebar-container {
  width: $sidebar-width;
  height: 100%;
  background-color: $sidebar-bg;
  transition: width $transition-base ease-in-out;
  overflow: hidden;
  will-change: width;

  &.is-collapsed {
    width: $sidebar-collapsed-width;
  }
}

.sidebar-logo {
  height: $header-height;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 $spacing-md;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);

  .logo-img {
    width: 32px;
    height: 32px;
    margin-right: $spacing-sm;
  }

  .logo-img-small {
    width: 32px;
    height: 32px;
  }

  .logo-title {
    font-size: 18px;
    font-weight: 600;
    color: $sidebar-active-text;
  }
}

.sidebar-scrollbar {
  height: calc(100% - #{$header-height});

  :deep(.el-scrollbar__wrap) {
    overflow-x: hidden;
  }
}

.el-menu {
  border-right: none;

  :deep(.el-menu-item) {
    height: 48px;
    line-height: 48px;
    margin: 4px 8px;
    border-radius: $radius-sm;
    transition: all $transition-fast;

    &.is-active {
      background-color: $sidebar-active-bg !important;
      color: $sidebar-active-text !important;
      font-weight: 500;
      position: relative;

      &::before {
        content: '';
        position: absolute;
        left: 0;
        top: 50%;
        transform: translateY(-50%);
        width: 3px;
        height: 24px;
        background-color: $sidebar-active-text;
        border-radius: 0 2px 2px 0;
      }
    }

    &:hover {
      background-color: rgba(255, 255, 255, 0.08) !important;
      color: $sidebar-active-text !important;
    }
  }

  :deep(.el-sub-menu__title) {
    height: 48px;
    line-height: 48px;
    margin: 4px 8px;
    border-radius: $radius-sm;
    transition: all $transition-fast;

    &:hover {
      background-color: rgba(255, 255, 255, 0.08) !important;
      color: $sidebar-active-text !important;
    }
  }

  :deep(.el-sub-menu.is-active) {
    > .el-sub-menu__title {
      color: $sidebar-active-text !important;
      font-weight: 500;
      background-color: rgba(255, 255, 255, 0.05) !important;
    }
  }

  :deep(.el-sub-menu.is-opened) {
    > .el-sub-menu__title {
      background-color: rgba(255, 255, 255, 0.05) !important;
    }
  }

  :deep(.el-sub-menu) {
    .el-menu {
      background-color: transparent;
    }

    .el-menu-item {
      padding-left: 48px !important;
      background-color: transparent;

      &.is-active {
        background-color: $sidebar-active-bg !important;
        color: $sidebar-active-text !important;
        font-weight: 500;
        position: relative;

        &::before {
          content: '';
          position: absolute;
          left: 8px;
          top: 50%;
          transform: translateY(-50%);
          width: 3px;
          height: 20px;
          background-color: $sidebar-active-text;
          border-radius: 0 2px 2px 0;
        }
      }

      &:hover {
        background-color: rgba(255, 255, 255, 0.08) !important;
        color: $sidebar-active-text !important;
      }
    }
  }

  // 折叠状态下的样式
  &.el-menu--collapse {
    :deep(.el-menu-item),
    :deep(.el-sub-menu__title) {
      margin: 4px 4px;
      padding: 0 !important;
      text-align: center;
    }
  }
}
</style>
