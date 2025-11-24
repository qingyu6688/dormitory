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
          <el-icon :size="32"><Grid /></el-icon>
          <span class="brand-name">TechFlow</span>
        </div>

        <!-- Slogan -->
        <div class="brand-content">
          <h2 class="brand-title">找回您的账号密码</h2>
          <p class="brand-desc">
            输入您注册时使用的邮箱地址，我们将发送重置密码的验证码到您的邮箱。
          </p>
        </div>

        <!-- 版权 -->
        <div class="brand-footer">
          &copy; 2025 TechFlow Team. All rights reserved.
        </div>
      </div>

      <!-- 右侧：找回密码表单区 -->
      <div class="form-section">
        <!-- 标题 -->
        <div class="form-header">
          <h1 class="form-title">找回密码</h1>
          <p class="form-subtitle">请按照以下步骤重置您的密码</p>
        </div>

        <!-- 步骤指示器 -->
        <el-steps :active="currentStep" align-center style="margin-bottom: 30px;">
          <el-step title="验证邮箱" />
          <el-step title="重置密码" />
          <el-step title="完成" />
        </el-steps>

        <!-- 找回密码表单 -->
        <div class="form-wrapper">
          <!-- 步骤1: 验证邮箱 -->
          <el-form v-if="currentStep === 0" :model="emailForm" :rules="emailRules" ref="emailFormRef" size="large">
            <el-form-item prop="username">
              <el-input v-model="emailForm.username" placeholder="请输入用户名">
                <template #prefix>
                  <el-icon class="input-icon"><User /></el-icon>
                </template>
              </el-input>
            </el-form-item>

            <el-form-item prop="email">
              <el-input v-model="emailForm.email" placeholder="请输入注册邮箱">
                <template #prefix>
                  <el-icon class="input-icon"><Message /></el-icon>
                </template>
              </el-input>
            </el-form-item>

            <el-form-item prop="code">
              <div style="display: flex; gap: 10px;">
                <el-input v-model="emailForm.code" placeholder="请输入验证码" style="flex: 1;">
                  <template #prefix>
                    <el-icon class="input-icon"><Key /></el-icon>
                  </template>
                </el-input>
                <el-button 
                  :disabled="countdown > 0" 
                  @click="sendVerificationCode"
                  :loading="sendingCode"
                >
                  {{ countdown > 0 ? `${countdown}秒后重试` : '发送验证码' }}
                </el-button>
              </div>
            </el-form-item>

            <el-button 
              type="primary" 
              class="submit-btn" 
              :loading="loading" 
              @click="verifyEmail"
            >
              下一步
            </el-button>
          </el-form>

          <!-- 步骤2: 重置密码 -->
          <el-form v-if="currentStep === 1" :model="passwordForm" :rules="passwordRules" ref="passwordFormRef" size="large">
            <el-form-item prop="newPassword">
              <el-input 
                v-model="passwordForm.newPassword" 
                type="password" 
                placeholder="请输入新密码 (至少6位)" 
                show-password
              >
                <template #prefix>
                  <el-icon class="input-icon"><Lock /></el-icon>
                </template>
              </el-input>
            </el-form-item>

            <el-form-item prop="confirmPassword">
              <el-input 
                v-model="passwordForm.confirmPassword" 
                type="password" 
                placeholder="请再次输入新密码" 
                show-password
                @keyup.enter="resetPassword"
              >
                <template #prefix>
                  <el-icon class="input-icon"><Lock /></el-icon>
                </template>
              </el-input>
            </el-form-item>

            <el-button 
              type="primary" 
              class="submit-btn" 
              :loading="loading" 
              @click="resetPassword"
            >
              重置密码
            </el-button>
          </el-form>

          <!-- 步骤3: 完成 -->
          <div v-if="currentStep === 2" class="success-content">
            <el-result icon="success" title="密码重置成功！" sub-title="您的密码已成功重置，请使用新密码登录">
              <template #extra>
                <el-button type="primary" @click="goToLogin">前往登录</el-button>
              </template>
            </el-result>
          </div>
        </div>

        <!-- 底部切换 -->
        <div class="form-footer">
          <div class="toggle-mode">
            想起密码了? 
            <router-link to="/login" class="toggle-link">
              直接登录
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Message, Lock, Key, Grid, User } from '@element-plus/icons-vue'
import request from '@/utils/request'

const router = useRouter()

const loading = ref(false)
const sendingCode = ref(false)
const countdown = ref(0)
const currentStep = ref(0)
const emailFormRef = ref(null)
const passwordFormRef = ref(null)

// 邮箱验证表单
const emailForm = reactive({
  username: '',
  email: '',
  code: ''
})

// 密码重置表单
const passwordForm = reactive({
  newPassword: '',
  confirmPassword: ''
})

// 邮箱验证规则
const emailRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入有效的邮箱地址', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    { len: 6, message: '验证码为6位数字', trigger: 'blur' }
  ]
}

// 密码重置规则
const passwordRules = {
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    { 
      validator: (rule, value, callback) => {
        if (value !== passwordForm.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      }, 
      trigger: 'blur' 
    }
  ]
}

// 发送验证码
const sendVerificationCode = async () => {
  if (!emailForm.username) {
    ElMessage.error('请先输入用户名')
    return
  }

  if (!emailForm.email) {
    ElMessage.error('请先输入邮箱地址')
    return
  }

  // 验证邮箱格式
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  if (!emailRegex.test(emailForm.email)) {
    ElMessage.error('请输入有效的邮箱地址')
    return
  }

  sendingCode.value = true
  try {
    await request.post('/auth/send-reset-code', { 
      username: emailForm.username,
      email: emailForm.email 
    })
    ElMessage.success('验证码已发送到您的邮箱，请查收')
    
    // 开始倒计时
    countdown.value = 60
    const timer = setInterval(() => {
      countdown.value--
      if (countdown.value <= 0) {
        clearInterval(timer)
      }
    }, 1000)
  } catch (error) {
    console.error('发送验证码失败:', error)
    ElMessage.error(error.response?.data?.message || '发送验证码失败')
  } finally {
    sendingCode.value = false
  }
}

// 验证邮箱和验证码
const verifyEmail = async () => {
  if (!emailFormRef.value) return
  
  await emailFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await request.post('/auth/verify-reset-code', {
          username: emailForm.username,
          email: emailForm.email,
          code: emailForm.code
        })
        ElMessage.success('验证成功')
        currentStep.value = 1
      } catch (error) {
        console.error('验证失败:', error)
        ElMessage.error(error.response?.data?.message || '验证码错误或已过期')
      } finally {
        loading.value = false
      }
    }
  })
}

// 重置密码
const resetPassword = async () => {
  if (!passwordFormRef.value) return
  
  await passwordFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await request.post('/auth/reset-password', {
          username: emailForm.username,
          email: emailForm.email,
          code: emailForm.code,
          newPassword: passwordForm.newPassword
        })
        ElMessage.success('密码重置成功')
        currentStep.value = 2
      } catch (error) {
        console.error('重置密码失败:', error)
        ElMessage.error(error.response?.data?.message || '重置密码失败')
      } finally {
        loading.value = false
      }
    }
  })
}

// 前往登录
const goToLogin = () => {
  router.push('/login')
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
  position: relative;
  overflow: hidden;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

// 动态背景
.animated-bg {
  position: absolute;
  width: 100%;
  height: 100%;
  overflow: hidden;
  z-index: 0;
}

.circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  animation: float 20s infinite ease-in-out;

  &.circle-1 {
    width: 300px;
    height: 300px;
    top: 10%;
    left: 10%;
    animation-delay: 0s;
  }

  &.circle-2 {
    width: 200px;
    height: 200px;
    top: 60%;
    left: 70%;
    animation-delay: 5s;
  }

  &.circle-3 {
    width: 150px;
    height: 150px;
    top: 80%;
    left: 20%;
    animation-delay: 10s;
  }

  &.circle-4 {
    width: 250px;
    height: 250px;
    top: 20%;
    left: 80%;
    animation-delay: 15s;
  }
}

@keyframes float {
  0%, 100% {
    transform: translate(0, 0) scale(1);
  }
  33% {
    transform: translate(30px, -50px) scale(1.1);
  }
  66% {
    transform: translate(-20px, 20px) scale(0.9);
  }
}

// 主容器
.auth-container {
  width: 90%;
  max-width: 1000px;
  min-height: 600px;
  background: white;
  border-radius: 20px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  display: flex;
  position: relative;
  z-index: 1;
  animation: slideUp 0.8s ease-out;

  @media (max-width: 768px) {
    flex-direction: column;
    height: auto;
  }
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

// 左侧品牌区
.brand-section {
  flex: 1;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 3rem;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  color: white;
  border-radius: 20px 0 0 20px;
  position: relative;
  overflow: hidden;
  animation: fadeInLeft 0.8s ease-out;

  @media (max-width: 768px) {
    width: 100%;
    padding: 2rem;
  }
}

// 发光圆圈装饰
.glow-circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  filter: blur(40px);

  &.glow-circle-1 {
    width: 200px;
    height: 200px;
    top: -50px;
    right: -50px;
  }

  &.glow-circle-2 {
    width: 150px;
    height: 150px;
    bottom: -30px;
    left: -30px;
  }
}

.brand-logo {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 24px;
  font-weight: 700;
  margin-bottom: 2rem;
  position: relative;
  z-index: 1;
}

.brand-content {
  position: relative;
  z-index: 1;
}

.brand-title {
  font-size: 32px;
  font-weight: 700;
  line-height: 1.3;
  margin-bottom: 1rem;
}

.brand-desc {
  font-size: 16px;
  line-height: 1.6;
  opacity: 0.9;
}

.brand-footer {
  font-size: 14px;
  opacity: 0.7;
  position: relative;
  z-index: 1;
}

// 右侧表单区
.form-section {
  flex: 1;
  padding: 3rem;
  display: flex;
  flex-direction: column;
  justify-content: center;
  position: relative;
  animation: fadeInRight 0.8s ease-out;

  @media (max-width: 768px) {
    width: 100%;
    padding: 2rem;
  }
}

@keyframes fadeInLeft {
  from {
    opacity: 0;
    transform: translateX(-30px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes fadeInRight {
  from {
    opacity: 0;
    transform: translateX(30px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

.form-header {
  margin-bottom: 2rem;
  text-align: center;
}

.form-title {
  font-size: 28px;
  font-weight: 700;
  color: #1f2937;
  margin-bottom: 0.5rem;
}

.form-subtitle {
  font-size: 14px;
  color: #6b7280;
}

.form-wrapper {
  margin-bottom: 1.5rem;
}

.submit-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(135deg, $primary-color 0%, color.scale($primary-color, $lightness: -10%) 100%);
  border: none;
  border-radius: 8px;
  transition: all 0.3s;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 16px rgba($primary-color, 0.3);
  }
}

.form-footer {
  text-align: center;
  margin-top: 1.5rem;
}

.toggle-mode {
  font-size: 14px;
  color: #6b7280;
}

.toggle-link {
  color: $primary-color;
  text-decoration: none;
  font-weight: 600;
  transition: all 0.3s;

  &:hover {
    color: color.scale($primary-color, $lightness: -10%);
  }
}

.success-content {
  text-align: center;
  padding: 20px 0;
}

:deep(.el-input__wrapper) {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.3s;

  &:hover {
    box-shadow: 0 4px 12px rgba($primary-color, 0.15);
  }
}

:deep(.el-steps) {
  .el-step__title {
    font-size: 14px;
  }
}
</style>
