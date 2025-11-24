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

      <!-- 右侧：注册表单区 -->
      <div class="form-section">
        <!-- 标题 -->
        <div class="form-header">
          <h1 class="form-title">创建新账号</h1>
          <p class="form-subtitle">填写以下信息注册您的学生账号</p>
        </div>

        <!-- 注册表单 -->
        <div class="form-wrapper">
          <el-form :model="regForm" :rules="regRules" ref="regRef" size="large">
            <el-form-item prop="username">
              <el-input v-model="regForm.username" placeholder="设置用户名">
                <template #prefix>
                  <el-icon class="input-icon"><User /></el-icon>
                </template>
              </el-input>
            </el-form-item>

            <el-form-item prop="email">
              <el-input v-model="regForm.email" placeholder="常用邮箱地址">
                <template #prefix>
                  <el-icon class="input-icon"><Message /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            
            <el-form-item prop="password">
              <el-input 
                v-model="regForm.password" 
                type="password" 
                placeholder="设置密码 (至少6位)" 
                show-password
              >
                <template #prefix>
                  <el-icon class="input-icon"><Lock /></el-icon>
                </template>
              </el-input>
            </el-form-item>

            <el-form-item prop="confirmPassword">
              <el-input 
                v-model="regForm.confirmPassword" 
                type="password" 
                placeholder="再次输入密码" 
                show-password
                @keyup.enter="handleRegister"
              >
                <template #prefix>
                  <el-icon class="input-icon"><Lock /></el-icon>
                </template>
              </el-input>
            </el-form-item>

            <el-form-item prop="agree">
              <el-checkbox v-model="regForm.agree">
                我已阅读并同意 
                <el-link type="primary" :underline="false" class="link-inline" @click="showTermsDialog = true">服务条款</el-link>
              </el-checkbox>
            </el-form-item>

            <el-button 
              type="primary" 
              class="submit-btn" 
              :loading="loading" 
              @click="handleRegister"
            >
              注 册
            </el-button>
          </el-form>
        </div>

        <!-- 底部切换 -->
        <div class="form-footer">
          <div class="toggle-mode">
            已有账号? 
            <router-link to="/login" class="toggle-link">
              直接登录
            </router-link>
          </div>
        </div>
      </div>
    </div>

    <!-- 服务条款对话框 -->
    <el-dialog v-model="showTermsDialog" title="服务条款" width="700px" class="terms-dialog">
      <div class="terms-content">
        <h3>一、服务条款的接受与修改</h3>
        <p>1.1 欢迎使用TechFlow平台（以下简称"本平台"）。在使用本平台服务前，请您仔细阅读并充分理解本服务条款。</p>
        <p>1.2 您点击"同意"按钮或实际使用本平台服务，即表示您已充分阅读、理解并接受本服务条款的全部内容。</p>
        <p>1.3 本平台有权根据需要不时修改本服务条款，修改后的条款将在本平台公布，不再另行通知。</p>

        <h3>二、账号注册与使用</h3>
        <p>2.1 您在注册账号时应提供真实、准确、完整的个人信息，并及时更新以保持信息的有效性。</p>
        <p>2.2 您应妥善保管账号和密码，对使用该账号进行的所有活动承担全部责任。</p>
        <p>2.3 您不得将账号转让、出售或以其他方式提供给第三方使用。</p>
        <p>2.4 如发现账号被盗用或存在安全漏洞，应立即通知本平台。</p>

        <h3>三、用户行为规范</h3>
        <p>3.1 您在使用本平台服务时，应遵守中华人民共和国相关法律法规。</p>
        <p>3.2 您不得利用本平台从事以下行为：</p>
        <ul>
          <li>发布、传播违法违规信息</li>
          <li>侵犯他人知识产权、商业秘密等合法权益</li>
          <li>进行网络攻击、传播病毒或恶意代码</li>
          <li>干扰或破坏本平台正常运营</li>
          <li>其他违反法律法规或本服务条款的行为</li>
        </ul>

        <h3>四、隐私保护</h3>
        <p>4.1 本平台重视用户隐私保护，将按照《隐私政策》的约定收集、使用、存储和保护您的个人信息。</p>
        <p>4.2 未经您的同意，本平台不会向第三方披露您的个人信息，法律法规另有规定的除外。</p>

        <h3>五、知识产权</h3>
        <p>5.1 本平台所包含的所有内容（包括但不限于文字、图片、软件、程序、数据等）的知识产权均归本平台所有。</p>
        <p>5.2 未经本平台书面许可，您不得擅自使用、复制、修改、传播本平台的任何内容。</p>

        <h3>六、免责声明</h3>
        <p>6.1 本平台将尽力确保服务的稳定性和安全性，但不对服务的及时性、准确性、完整性作出任何明示或暗示的保证。</p>
        <p>6.2 因不可抗力、网络故障、系统维护等原因导致的服务中断或数据丢失，本平台不承担责任。</p>
        <p>6.3 您使用本平台服务所产生的风险由您自行承担。</p>

        <h3>七、服务的变更、中断或终止</h3>
        <p>7.1 本平台有权根据业务需要随时变更、中断或终止部分或全部服务。</p>
        <p>7.2 如您违反本服务条款，本平台有权暂停或终止向您提供服务。</p>

        <h3>八、争议解决</h3>
        <p>8.1 本服务条款的解释、效力及纠纷解决均适用中华人民共和国法律。</p>
        <p>8.2 如双方就本服务条款内容或其执行发生争议，应友好协商解决；协商不成的，任何一方均可向本平台所在地人民法院提起诉讼。</p>

        <h3>九、其他</h3>
        <p>9.1 本服务条款自发布之日起生效。</p>
        <p>9.2 如本服务条款的任何条款被认定为无效或不可执行，不影响其他条款的效力。</p>
        <p>9.3 本平台对本服务条款享有最终解释权。</p>

        <div class="terms-footer">
          <p><strong>最后更新日期：2025年11月20日</strong></p>
          <p>如有任何疑问，请联系我们：admin@techflow.com</p>
        </div>
      </div>
      <template #footer>
        <el-button @click="showTermsDialog = false">关闭</el-button>
        <el-button type="primary" @click="acceptTerms">同意并继续</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import { User, Lock, Message, School } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

const loading = ref(false)
const regRef = ref(null)
const showTermsDialog = ref(false)

// 注册表单数据
const regForm = reactive({
  username: '',
  email: '',
  password: '',
  confirmPassword: '',
  agree: false
})

// 校验规则
const regRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入有效的邮箱地址', trigger: ['blur', 'change'] }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    { 
      validator: (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'))
        } else if (value !== regForm.password) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      }, 
      trigger: 'blur' 
    }
  ],
  agree: [
    { 
      validator: (rule, value, callback) => {
        if (!value) callback(new Error('请勾选同意服务条款'))
        else callback()
      }, 
      trigger: 'change' 
    }
  ]
}

// 注册处理
const handleRegister = async () => {
  if (!regRef.value) return
  
  await regRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        // 调用注册API
        await userStore.register({
          username: regForm.username,
          email: regForm.email,
          password: regForm.password
        })
        
        // 注册成功，跳转到登录页并自动填充用户名
        router.push({
          path: '/login',
          query: { username: regForm.username }
        })
      } catch (error) {
        console.error('注册失败:', error)
      } finally {
        loading.value = false
      }
    }
  })
}

// 同意服务条款
const acceptTerms = () => {
  regForm.agree = true
  showTermsDialog.value = false
  ElMessage.success('感谢您同意服务条款')
}
</script>

<style lang="scss" scoped>
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
    transform: translateY(50px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
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
  animation: fadeInLeft 0.8s ease-out;

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
  width: 60%;
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

    &:hover {
      text-decoration: underline;
    }
  }
}

// 输入框图标
.input-icon {
  color: $text-secondary;
}

.link-inline {
  display: inline;
  vertical-align: baseline;
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

// ==================== 动画关键帧 ====================
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

// 表单项依次出现动画
.register-form {
  :deep(.el-form-item) {
    animation: fadeInUp 0.6s ease-out backwards;

    &:nth-child(1) { animation-delay: 0.1s; }
    &:nth-child(2) { animation-delay: 0.2s; }
    &:nth-child(3) { animation-delay: 0.3s; }
    &:nth-child(4) { animation-delay: 0.4s; }
    &:nth-child(5) { animation-delay: 0.5s; }
    &:nth-child(6) { animation-delay: 0.6s; }
  }
}

// 输入框聚焦动画
:deep(.el-input__wrapper) {
  transition: all 0.3s;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba($primary-color, 0.15) !important;
  }

  &.is-focus {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba($primary-color, 0.25) !important;
  }
}

// 服务条款对话框样式
.terms-dialog {
  :deep(.el-dialog__body) {
    max-height: 60vh;
    overflow-y: auto;
    padding: 20px 30px;
  }
}

.terms-content {
  line-height: 1.8;
  color: #333;

  h3 {
    font-size: 16px;
    font-weight: 600;
    color: #2563EB;
    margin: 24px 0 12px 0;
    
    &:first-child {
      margin-top: 0;
    }
  }

  p {
    margin: 8px 0;
    font-size: 14px;
    text-align: justify;
  }

  ul {
    margin: 12px 0;
    padding-left: 24px;
    
    li {
      margin: 6px 0;
      font-size: 14px;
    }
  }

  .terms-footer {
    margin-top: 32px;
    padding-top: 20px;
    border-top: 1px solid #e5e7eb;
    text-align: center;
    
    p {
      margin: 8px 0;
      color: #6b7280;
      font-size: 13px;
    }
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

// 链接悬停效果
.toggle-link {
  transition: all 0.3s;

  &:hover {
    transform: translateX(5px);
  }
}
</style>
