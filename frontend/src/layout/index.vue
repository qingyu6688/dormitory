<template>
  <div class="app-wrapper">
    <!-- 侧边栏 -->
    <sidebar class="sidebar-wrapper" />

    <!-- 主内容区 -->
    <div class="main-container" :class="{ 'sidebar-collapsed': collapsed }">
      <!-- 顶部导航 -->
      <app-header />

      <!-- 标签页 -->
      <tags-view />

      <!-- 页面内容 -->
      <div class="app-main">
        <router-view v-slot="{ Component }">
          <transition name="fade-transform" mode="out-in">
            <keep-alive>
              <component :is="Component" />
            </keep-alive>
          </transition>
        </router-view>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useAppStore } from '@/stores/app'
import Sidebar from '@/components/Sidebar/index.vue'
import AppHeader from '@/components/Header/index.vue'
import TagsView from '@/components/TagsView/index.vue'

const appStore = useAppStore()
const collapsed = computed(() => appStore.sidebarCollapsed)
</script>

<style lang="scss" scoped>
@use '@/styles/variables.scss' as *;

.app-wrapper {
  position: relative;
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.sidebar-wrapper {
  position: fixed;
  left: 0;
  top: 0;
  bottom: 0;
  z-index: 1001;
  transition: width $transition-base;
}

.main-container {
  min-height: 100%;
  margin-left: $sidebar-width;
  transition: margin-left $transition-base;
  display: flex;
  flex-direction: column;
  will-change: margin-left;
  width: calc(100% - #{$sidebar-width});

  &.sidebar-collapsed {
    margin-left: $sidebar-collapsed-width;
    width: calc(100% - #{$sidebar-collapsed-width});
  }
}

.app-main {
  flex: 1;
  padding: $content-padding;
  overflow-y: auto;
  background: $bg-color;
}

// 页面切换动画
.fade-transform-leave-active,
.fade-transform-enter-active {
  transition: all $transition-base;
}

.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(-30px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(30px);
}
</style>
