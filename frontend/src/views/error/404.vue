<template>
  <div class="error-page">
    <div class="error-content">
      <div class="error-code">404</div>
      <div class="error-title">页面不存在</div>
      <div class="error-desc">抱歉，您访问的页面不存在或已被删除</div>
      <div class="error-actions">
        <el-button type="primary" @click="goHome">
          <el-icon><HomeFilled /></el-icon>
          返回首页
        </el-button>
        <el-button @click="goBack">
          <el-icon><Back /></el-icon>
          返回上一页
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { HomeFilled, Back } from '@element-plus/icons-vue'

const router = useRouter()

const goHome = () => {
  router.push('/')
}

const goBack = () => {
  router.back()
}
</script>

<style lang="scss" scoped>
@use '@/styles/variables.scss' as *;

.error-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, $bg-color 0%, $bg-white 100%);
}

.error-content {
  text-align: center;
  padding: $spacing-xl;

  .error-code {
    font-size: 120px;
    font-weight: 700;
    color: $primary-color;
    line-height: 1;
    margin-bottom: $spacing-lg;
    background: linear-gradient(135deg, $primary-color 0%, $primary-hover 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
  }

  .error-title {
    font-size: $font-size-h1;
    font-weight: 600;
    color: $text-primary;
    margin-bottom: $spacing-md;
  }

  .error-desc {
    font-size: $font-size-base;
    color: $text-secondary;
    margin-bottom: $spacing-xl;
  }

  .error-actions {
    display: flex;
    gap: $spacing-md;
    justify-content: center;
  }
}
</style>
