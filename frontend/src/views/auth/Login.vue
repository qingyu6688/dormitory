<template>
  <div class="auth-page">
    <!-- 动态背景 -->
    <div class="animated-bg">
      <div class="circle circle-1"></div>
      <div class="circle circle-2"></div>
      <div class="circle circle-3"></div>
      <div class="circle circle-4"></div>
    </div>

    <div class="auth-container">
      <!-- 左侧：品牌展示区 -->
      <div class="brand-section">
        <!-- 动态装饰 -->
        <div class="glow-circle glow-circle-1"></div>
        <div class="glow-circle glow-circle-2"></div>

        <!-- Logo -->
        <div class="brand-logo">
          <el-icon :size="32"><School /></el-icon>
          <span class="brand-name">智慧宿舍</span>
        </div>

        <!-- Slogan -->
        <div class="brand-content">
          <h2 class="brand-title">智慧宿舍<br>管理系统</h2>
          <p class="brand-desc">
            让宿舍生活更便捷、更智能、更美好。在线报修、访客登记、费用查询，一站式解决宿舍生活所有需求。
          </p>
        </div>

        <!-- 版权 -->
        <div class="brand-footer">
          &copy; 2025 智慧宿舍管理系统. All rights reserved.
        </div>
      </div>

      <!-- 右侧：登录表单区 -->
      <div class="form-section">
        <!-- 标题 -->
        <div class="form-header">
          <h1 class="form-title">欢迎回来</h1>
          <p class="form-subtitle">登录智慧宿舍管理系统</p>
        </div>

        <!-- 登录表单 -->
        <div class="form-wrapper">
          <el-form :model="loginForm" :rules="loginRules" ref="loginRef" size="large">
            <el-form-item prop="username">
              <el-input v-model="loginForm.username" placeholder="用户名 / 邮箱">
                <template #prefix>
                  <el-icon class="input-icon"><User /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            
            <el-form-item prop="password">
              <el-input 
                v-model="loginForm.password" 
                type="password" 
                placeholder="密码" 
                show-password
                @keyup.enter="handleLogin"
              >
                <template #prefix>
                  <el-icon class="input-icon"><Lock /></el-icon>
                </template>
              </el-input>
            </el-form-item>

            <div class="form-options">
              <el-checkbox v-model="loginForm.remember" label="记住我" size="small" />
              <router-link to="/forgot-password" class="forgot-link">忘记密码?</router-link>
            </div>

            <el-button 
              type="primary" 
              class="submit-btn" 
              :loading="loading" 
              @click="handleLogin"
            >
              登 录
            </el-button>
          </el-form>
        </div>

        <!-- 底部切换 -->
        <div class="form-footer">
          <div class="toggle-mode">
            还没有账号? 
            <router-link to="/register" class="toggle-link">
              立即注册
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import { User, Lock, School } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const loading = ref(false)
const loginRef = ref(null)

// 登录表单数据
const loginForm = reactive({
  username: 'admin',
  password: '111111',
  remember: false
})

// 从URL参数获取用户名（注册成功后跳转）
onMounted(() => {
  if (route.query.username) {
    loginForm.username = route.query.username
  }
})

// 校验规则
const loginRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

// 登录处理
const handleLogin = async () => {
  if (!loginRef.value) return
  
  await loginRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await userStore.login(loginForm)
        ElMessage.success('登录成功，正在跳转...')
        
        // 根据用户角色跳转不同页面
        // roleId: 1 = 超级管理员，2 = 管理员，3 = 宿管员，4 = 学生，5 = 辅导员
        const userInfo = userStore.userInfo
        let redirect = router.currentRoute.value.query.redirect
        
        if (!redirect) {
          // 如果没有重定向路径，根据角色决定跳转
          if (userInfo.roleId === 1 || userInfo.roleId === 2 || userInfo.roleId === 3 || userInfo.roleId === 5) {
            // 管理员、宿管员、辅导员跳转到后台首页
            redirect = '/dashboard'
          } else {
            // 学生跳转到前台首页
            redirect = '/front'
          }
        }
        
        setTimeout(() => {
          router.push(redirect)
        }, 500)
      } catch (error) {
        console.error('登录失败:', error)
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style lang="scss" scoped>
@use 'sass:color';
@use '@/styles/variables.scss' as *;

.auth-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 1rem;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow: hidden;
}

// 动态背景圆圈
.animated-bg {
  position: absolute;
  width: 100%;
  height: 100%;
  overflow: hidden;
  z-index: 0;

  .circle {
    position: absolute;
    border-radius: 50%;
    animation: float 20s infinite ease-in-out;
    filter: blur(40px);
    opacity: 0.6;

    &.circle-1 {
      width: 300px;
      height: 300px;
      top: 10%;
      left: 10%;
      background: linear-gradient(135deg, rgba(59, 130, 246, 0.5), rgba(139, 92, 246, 0.5));
      animation-delay: 0s;
    }

    &.circle-2 {
      width: 200px;
      height: 200px;
      top: 60%;
      left: 70%;
      background: linear-gradient(135deg, rgba(139, 92, 246, 0.5), rgba(168, 85, 247, 0.5));
      animation-delay: 2s;
    }

    &.circle-3 {
      width: 150px;
      height: 150px;
      top: 80%;
      left: 20%;
      background: linear-gradient(135deg, rgba(37, 99, 235, 0.5), rgba(59, 130, 246, 0.5));
      animation-delay: 4s;
    }

    &.circle-4 {
      width: 250px;
      height: 250px;
      top: 30%;
      left: 80%;
      background: linear-gradient(135deg, rgba(168, 85, 247, 0.5), rgba(192, 132, 252, 0.5));
      animation-delay: 6s;
    }
  }
}

@keyframes float {
  0%, 100% {
    transform: translateY(0) translateX(0) scale(1);
  }
  25% {
    transform: translateY(-30px) translateX(30px) scale(1.1);
  }
  50% {
    transform: translateY(-60px) translateX(-30px) scale(0.9);
  }
  75% {
    transform: translateY(-30px) translateX(-60px) scale(1.05);
  }
}

.auth-container {
  width: 100%;
  max-width: 1000px;
  height: 600px;
  background: $bg-white;
  border-radius: 16px;
  box-shadow: $shadow-level-2;
  overflow: hidden;
  display: flex;

  @media (max-width: 768px) {
    flex-direction: column;
    height: auto;
  }
}

// ==================== 左侧品牌区 ====================
.brand-section {
  width: 40%;
  background: linear-gradient(135deg, #001529 0%, #003a70 50%, $primary-color 100%);
  padding: 2.5rem;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  color: $bg-white;
  position: relative;
  overflow: hidden;

  @media (max-width: 768px) {
    width: 100%;
    padding: 2rem;
  }
}

// 装饰性光圈
.glow-circle {
  position: absolute;
  border-radius: 50%;
  filter: blur(60px);
  opacity: 0.4;
  pointer-events: none;

  &.glow-circle-1 {
    width: 16rem;
    height: 16rem;
    background: #3B82F6;
    top: -3rem;
    left: -3rem;
  }

  &.glow-circle-2 {
    width: 20rem;
    height: 20rem;
    background: #8B5CF6;
    bottom: -6rem;
    right: -6rem;
  }
}

.brand-logo {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  position: relative;
  z-index: 10;

  .brand-name {
    font-size: 1.5rem;
    font-weight: 700;
    letter-spacing: 0.05em;
  }
}

.brand-content {
  position: relative;
  z-index: 10;
  margin-bottom: 3rem;

  .brand-title {
    font-size: 1.875rem;
    font-weight: 700;
    margin-bottom: 1rem;
    line-height: 1.3;
  }

  .brand-desc {
    color: rgba(255, 255, 255, 0.8);
    font-size: 0.875rem;
    line-height: 1.6;
  }
}

.brand-footer {
  font-size: 0.75rem;
  color: rgba(255, 255, 255, 0.6);
  position: relative;
  z-index: 10;
}

// ==================== 右侧表单区 ====================
.form-section {
  flex: 1;
  padding: 3rem;
  display: flex;
  flex-direction: column;
  justify-content: center;
  animation: fadeInRight 0.8s ease-out;
  position: relative;

  @media (max-width: 768px) {
    width: 100%;
    padding: 2rem;
  }
}

.form-header {
  margin-bottom: 2rem;

  .form-title {
    font-size: 1.5rem;
    font-weight: 700;
    color: $text-primary;
    margin-bottom: 0.5rem;
  }

  .form-subtitle {
    color: $text-secondary;
    font-size: 0.875rem;
  }
}

.form-wrapper {
  width: 100%;
}

// 表单选项
.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;

  .forgot-link {
    font-size: 0.75rem;
    color: $primary-color;
    text-decoration: none;
    transition: all 0.3s;

    &:hover {
      color: color.scale($primary-color, $lightness: -10%);
    }
  }
}

// 提交按钮
.submit-btn {
  width: 100%;
  height: 2.5rem;
  border-radius: $radius-lg;
  font-size: 1rem;
  font-weight: 500;
}

// 底部区域
.form-footer {
  margin-top: 2rem;
  padding-top: 1.5rem;
  border-top: 1px solid $divider-color;
  text-align: center;
}

.toggle-mode {
  font-size: 0.875rem;
  color: $text-secondary;

  .toggle-link {
    color: $primary-color;
    text-decoration: none;
    font-weight: 500;
    transition: all 0.3s;

    &:hover {
      text-decoration: underline;
      transform: translateX(5px);
    }
  }
}

// 动画关键帧
@keyframes fadeInLeft {
  from {
    opacity: 0;
    transform: translateX(-50px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes fadeInRight {
  from {
    opacity: 0;
    transform: translateX(50px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

// 表单项依次出现动画
.login-form {
  :deep(.el-form-item) {
    animation: fadeInUp 0.6s ease-out backwards;

    &:nth-child(1) { animation-delay: 0.1s; }
    &:nth-child(2) { animation-delay: 0.2s; }
    &:nth-child(3) { animation-delay: 0.3s; }
    &:nth-child(4) { animation-delay: 0.4s; }
  }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

// 输入框聚焦动画
:deep(.el-input__wrapper) {
  transition: all 0.3s;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba($primary-color, 0.15);
  }

  &.is-focus {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba($primary-color, 0.25);
  }
}

// 按钮动画
.submit-btn {
  transition: all 0.3s;
  position: relative;
  overflow: hidden;

  &::before {
    content: '';
    position: absolute;
    top: 50%;
    left: 50%;
    width: 0;
    height: 0;
    border-radius: 50%;
    background: rgba(255, 255, 255, 0.3);
    transform: translate(-50%, -50%);
    transition: width 0.6s, height 0.6s;
  }

  &:hover::before {
    width: 300px;
    height: 300px;
  }

  &:hover {
    transform: translateY(-3px);
    box-shadow: 0 8px 20px rgba($primary-color, 0.4);
  }

  &:active {
    transform: translateY(-1px);
  }
}

// 覆盖 Element Plus 样式
:deep(.el-input__wrapper) {
  padding: 8px 15px;
  box-shadow: 0 0 0 1px $border-color inset;

  &:hover {
    box-shadow: 0 0 0 1px #c0c4cc inset;
  }

  &.is-focus {
    box-shadow: 0 0 0 1px $primary-color inset !important;
  }
}

:deep(.el-form-item) {
  margin-bottom: 1.25rem;
}

:deep(.el-checkbox__label) {
  font-size: 0.875rem;
}
</style>
