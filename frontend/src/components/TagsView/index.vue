<template>
  <div class="tags-view-container">
    <el-scrollbar class="tags-view-wrapper">
      <div class="tags-view-content">
        <router-link
          v-for="tag in tabs"
          :key="tag.path"
          :to="tag.path"
          class="tags-view-item"
          :class="{ 'active': isActive(tag.path) }"
        >
          <span class="tag-title">{{ tag.title }}</span>
          <el-icon
            v-if="tabs.length > 1"
            class="tag-close"
            @click.prevent.stop="closeTab(tag.path)"
          >
            <Close />
          </el-icon>
        </router-link>
      </div>
    </el-scrollbar>

    <div class="tags-view-actions">
      <el-dropdown @command="handleCommand">
        <el-button size="small" text>
          <el-icon><ArrowDown /></el-icon>
        </el-button>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="closeOthers">
              <el-icon><Close /></el-icon>
              关闭其他
            </el-dropdown-item>
            <el-dropdown-item command="closeAll">
              <el-icon><CircleClose /></el-icon>
              关闭所有
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup>
import { computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAppStore } from '@/stores/app'
import { Close, ArrowDown, CircleClose } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const appStore = useAppStore()

const tabs = computed(() => appStore.tabs)
const activeTab = computed(() => appStore.activeTab)

// 判断是否为激活标签
const isActive = (path) => {
  return route.path === path
}

// 关闭标签
const closeTab = (path) => {
  const nextPath = appStore.removeTab(path)
  if (nextPath && route.path === path) {
    router.push(nextPath)
  }
}

// 下拉菜单命令处理
const handleCommand = (command) => {
  if (command === 'closeOthers') {
    appStore.closeOtherTabs(route.path)
  } else if (command === 'closeAll') {
    appStore.closeAllTabs()
    router.push('/')
  }
}

// 监听路由变化，添加标签
watch(
  () => route.path,
  () => {
    if (route.meta.title) {
      appStore.addTab({
        path: route.path,
        title: route.meta.title,
        name: route.name
      })
    }
  },
  { immediate: true }
)
</script>

<style lang="scss" scoped>
@use '@/styles/variables.scss' as *;

.tags-view-container {
  height: 44px;
  background: $bg-white;
  border-bottom: 1px solid $border-color;
  display: flex;
  align-items: center;
  padding: 0 $spacing-md;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.03);
  transition: width $transition-base, margin-left $transition-base;
  will-change: width;
}

.tags-view-wrapper {
  flex: 1;
  height: 100%;

  :deep(.el-scrollbar__wrap) {
    overflow-y: hidden;
  }
}

.tags-view-content {
  display: flex;
  align-items: center;
  height: 100%;
  gap: $spacing-sm;
  padding: $spacing-xs 0;
}

.tags-view-item {
  display: inline-flex;
  align-items: center;
  padding: 0 $spacing-md;
  height: 32px;
  line-height: 32px;
  border: none;
  border-radius: $radius-sm;
  background: $bg-white;
  color: $text-regular;
  font-size: $font-size-small;
  text-decoration: none;
  cursor: pointer;
  transition: all $transition-fast;
  position: relative;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);

  &::before {
    content: '';
    position: absolute;
    left: 0;
    top: 0;
    bottom: 0;
    width: 3px;
    background: transparent;
    border-radius: $radius-sm 0 0 $radius-sm;
    transition: all $transition-fast;
  }

  &:hover {
    background: $bg-color;
    color: $primary-color;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.08);
    transform: translateY(-1px);

    &::before {
      background: $primary-color;
    }
  }

  &.active {
    background: linear-gradient(135deg, $primary-color 0%, $primary-hover 100%);
    color: $bg-white;
    box-shadow: 0 2px 8px rgba($primary-color, 0.3);
    font-weight: 500;

    &::before {
      background: $bg-white;
    }

    .tag-close {
      color: $bg-white;

      &:hover {
        background: rgba(255, 255, 255, 0.2);
        transform: rotate(90deg);
      }
    }
  }

  .tag-title {
    margin-right: $spacing-xs;
    white-space: nowrap;
  }

  .tag-close {
    width: 18px;
    height: 18px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 12px;
    transition: all $transition-fast;
    margin-left: $spacing-xs;

    &:hover {
      background: rgba($text-secondary, 0.1);
      transform: rotate(90deg);
    }
  }
}

.tags-view-actions {
  margin-left: $spacing-sm;
}
</style>
