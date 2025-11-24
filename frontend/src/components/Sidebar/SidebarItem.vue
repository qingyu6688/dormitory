<template>
  <!-- 有子菜单 -->
  <el-sub-menu v-if="hasChildren" :index="menu.path">
    <template #title>
      <el-icon v-if="menu.icon">
        <component :is="menu.icon" />
      </el-icon>
      <span>{{ menu.title }}</span>
    </template>
    <sidebar-item
      v-for="child in menu.children"
      :key="child.id"
      :menu="child"
      :base-path="menu.path"
    />
  </el-sub-menu>

  <!-- 无子菜单 -->
  <el-menu-item v-else :index="resolvePath">
    <el-icon v-if="menu.icon">
      <component :is="menu.icon" />
    </el-icon>
    <template #title>
      <span>{{ menu.title }}</span>
    </template>
  </el-menu-item>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const props = defineProps({
  menu: {
    type: Object,
    required: true
  },
  basePath: {
    type: String,
    default: ''
  }
})

const route = useRoute()

// 是否有子菜单
const hasChildren = computed(() => {
  return props.menu.children && props.menu.children.length > 0
})

// 解析完整路径
const resolvePath = computed(() => {
  // 如果是绝对路径，直接返回
  if (props.menu.path && props.menu.path.startsWith('/')) {
    return props.menu.path
  }
  
  // 如果有父路径，拼接父路径
  if (props.basePath) {
    return `${props.basePath}/${props.menu.path}`.replace(/\/+/g, '/')
  }
  
  return props.menu.path || '/'
})
</script>

<style lang="scss" scoped>
.el-icon {
  margin-right: 8px;
}
</style>
