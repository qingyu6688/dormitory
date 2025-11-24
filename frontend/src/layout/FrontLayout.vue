<template>
  <div class="front-layout">
    <!-- 前台导航栏 -->
    <FrontNav />
    
    <!-- 主要内容区 -->
    <main class="front-main">
      <router-view />
    </main>
    
    <!-- 前台页脚 -->
    <FrontFooter />
  </div>
</template>

<script setup>
import FrontNav from '@/components/FrontNav/index.vue'
import FrontFooter from '@/components/FrontFooter/index.vue'
</script>

<style scoped>
.front-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.front-main {
  flex: 1;
}
</style>
