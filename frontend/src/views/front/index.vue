<template>
  <div class="front-container">

    <!-- Hero 区域 -->
    <section class="hero-section" id="home">
      <div class="hero-bg">
        <div class="hero-shape shape-1"></div>
        <div class="hero-shape shape-2"></div>
        <div class="hero-shape shape-3"></div>
      </div>
      <div class="hero-container">
        <div class="hero-content">
          <div class="hero-badge">
            <el-icon><School /></el-icon>
            <span>智慧校园生活</span>
          </div>
          <h1 class="hero-title">
            智慧宿舍管理系统
          </h1>
          <p class="hero-subtitle">
            让宿舍生活更便捷、更智能、更美好
          </p>
          <p class="hero-description">
            在线报修、访客登记、费用查询、卫生评分<br>
            一站式解决宿舍生活所有需求
          </p>
          <div class="hero-actions">
            <el-button class="primary-btn" size="large" @click="goToLogin">
              <el-icon><Right /></el-icon>
              立即登录
            </el-button>
            <el-button class="secondary-btn" size="large" @click="goToRegister">
              注册账号
            </el-button>
          </div>
          <div class="hero-stats">
            <div class="stat-card">
              <div class="stat-icon">
                <el-icon><House /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-number">{{ stats.roomCount }}</div>
                <div class="stat-label">宿舍房间</div>
              </div>
            </div>
            <div class="stat-card">
              <div class="stat-icon">
                <el-icon><User /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-number">{{ stats.studentCount }}</div>
                <div class="stat-label">在住学生</div>
              </div>
            </div>
            <div class="stat-card">
              <div class="stat-icon">
                <el-icon><Star /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-number">{{ stats.satisfaction }}%</div>
                <div class="stat-label">满意度</div>
              </div>
            </div>
          </div>
        </div>
        <div class="hero-image">
          <div class="image-wrapper">
            <div class="hero-placeholder">
              <el-icon :size="150"><School /></el-icon>
              <p>智慧宿舍管理系统</p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 功能特性 -->
    <section class="features-section" id="features">
      <div class="section-header">
        <div class="section-badge">核心功能</div>
        <h2 class="section-title">便捷的宿舍服务</h2>
        <p class="section-subtitle">一站式解决宿舍生活需求，让校园生活更美好</p>
      </div>
      <div class="features-grid">
        <div class="feature-card">
          <div class="feature-icon">
            <el-icon :size="40"><House /></el-icon>
          </div>
          <h3 class="feature-title">宿舍信息</h3>
          <p class="feature-desc">查看宿舍详情、床位信息、室友信息，随时了解宿舍状态</p>
        </div>
        <div class="feature-card">
          <div class="feature-icon">
            <el-icon :size="40"><Tools /></el-icon>
          </div>
          <h3 class="feature-title">在线报修</h3>
          <p class="feature-desc">快速提交报修申请，实时跟踪维修进度，问题及时解决</p>
        </div>
        <div class="feature-card">
          <div class="feature-icon">
            <el-icon :size="40"><UserFilled /></el-icon>
          </div>
          <h3 class="feature-title">访客登记</h3>
          <p class="feature-desc">在线预约访客，扫码登记，安全便捷的访客管理</p>
        </div>
        <div class="feature-card">
          <div class="feature-icon">
            <el-icon :size="40"><Wallet /></el-icon>
          </div>
          <h3 class="feature-title">费用查询</h3>
          <p class="feature-desc">水电费账单查询、在线缴费，费用明细一目了然</p>
        </div>
        <div class="feature-card">
          <div class="feature-icon">
            <el-icon :size="40"><DocumentChecked /></el-icon>
          </div>
          <h3 class="feature-title">违规查询</h3>
          <p class="feature-desc">查看违规记录、扣分情况，督促自己遵守宿舍规定</p>
        </div>
        <div class="feature-card">
          <div class="feature-icon">
            <el-icon :size="40"><Medal /></el-icon>
          </div>
          <h3 class="feature-title">卫生评分</h3>
          <p class="feature-desc">查看宿舍卫生检查记录和评分，争创文明宿舍</p>
        </div>
      </div>
    </section>

    <!-- 使用指南 -->
    <section class="guide-section">
      <div class="section-header">
        <div class="section-badge">使用指南</div>
        <h2 class="section-title">如何开始使用</h2>
        <p class="section-subtitle">简单三步，轻松享受便捷的宿舍服务</p>
      </div>
      <div class="guide-steps">
        <div class="step-card">
          <div class="step-number">01</div>
          <div class="step-icon">
            <el-icon :size="48"><User /></el-icon>
          </div>
          <h3 class="step-title">注册登录</h3>
          <p class="step-desc">使用学号注册账号，登录系统开始使用</p>
        </div>
        <div class="step-arrow">
          <el-icon :size="32"><Right /></el-icon>
        </div>
        <div class="step-card">
          <div class="step-number">02</div>
          <div class="step-icon">
            <el-icon :size="48"><Edit /></el-icon>
          </div>
          <h3 class="step-title">完善信息</h3>
          <p class="step-desc">填写个人信息，绑定宿舍，完善资料</p>
        </div>
        <div class="step-arrow">
          <el-icon :size="32"><Right /></el-icon>
        </div>
        <div class="step-card">
          <div class="step-number">03</div>
          <div class="step-icon">
            <el-icon :size="48"><Checked /></el-icon>
          </div>
          <h3 class="step-title">开始使用</h3>
          <p class="step-desc">享受便捷的宿舍服务，让生活更美好</p>
        </div>
      </div>
    </section>

    <!-- 关于系统 -->
    <section class="about-section" id="about">
      <div class="about-container">
        <div class="about-image">
          <div class="image-card">
            <div class="image-placeholder">
              <el-icon :size="120"><House /></el-icon>
              <p>智慧宿舍</p>
            </div>
          </div>
        </div>
        <div class="about-content">
          <div class="section-badge">关于系统</div>
          <h2 class="section-title">智慧宿舍管理平台</h2>
          <p class="about-text">
            智慧宿舍管理系统是专为高校学生打造的一站式宿舍服务平台。我们致力于通过信息化手段，为学生提供便捷、高效的宿舍生活服务，提升宿舍管理水平和学生满意度。
          </p>
          <div class="about-highlights">
            <div class="highlight-item">
              <el-icon class="highlight-icon"><Checked /></el-icon>
              <div class="highlight-text">
                <h4>便捷高效</h4>
                <p>在线服务，随时随地，快速响应学生需求</p>
              </div>
            </div>
            <div class="highlight-item">
              <el-icon class="highlight-icon"><Checked /></el-icon>
              <div class="highlight-text">
                <h4>安全可靠</h4>
                <p>数据加密保护，保障学生信息安全</p>
              </div>
            </div>
            <div class="highlight-item">
              <el-icon class="highlight-icon"><Checked /></el-icon>
              <div class="highlight-text">
                <h4>贴心服务</h4>
                <p>专业团队支持，及时解决宿舍问题</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 联系我们 -->
    <section class="contact-section" id="contact">
      <div class="contact-container">
        <div class="section-header">
          <div class="section-badge">联系我们</div>
          <h2 class="section-title">宿管中心随时为您服务</h2>
          <p class="section-subtitle">有任何问题或建议，欢迎随时联系宿管中心</p>
        </div>
        <div class="contact-grid">
          <div class="contact-card">
            <div class="contact-icon">
              <el-icon :size="32"><Location /></el-icon>
            </div>
            <h4>宿管中心</h4>
            <p>学生公寓1号楼一楼大厅</p>
          </div>
          <div class="contact-card">
            <div class="contact-icon">
              <el-icon :size="32"><Phone /></el-icon>
            </div>
            <h4>联系电话</h4>
            <p>0571-88888888</p>
          </div>
          <div class="contact-card">
            <div class="contact-icon">
              <el-icon :size="32"><Message /></el-icon>
            </div>
            <h4>邮箱地址</h4>
            <p>dorm@university.edu.cn</p>
          </div>
          <div class="contact-card">
            <div class="contact-icon">
              <el-icon :size="32"><Clock /></el-icon>
            </div>
            <h4>服务时间</h4>
            <p>周一至周日 8:00-22:00</p>
          </div>
        </div>
      </div>
    </section>

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import {
  User, House, Tools, UserFilled, Wallet, DocumentChecked, Medal,
  Right, Edit, Checked, Star, Location, Phone, Message, Clock, School
} from '@element-plus/icons-vue'
import request from '@/utils/request'

const router = useRouter()

// 统计数据
const stats = ref({
  roomCount: 0,
  studentCount: 0,
  satisfaction: 0
})

// 获取统计数据
const fetchStats = async () => {
  try {
    const res = await request.get('/dashboard/stats')
    stats.value = {
      roomCount: res.data.roomCount || 520,
      studentCount: res.data.studentCount || 2080,
      satisfaction: res.data.satisfaction || 98
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
    // 使用默认值
    stats.value = { roomCount: 520, studentCount: 2080, satisfaction: 98 }
  }
}

// 前往登录
const goToLogin = () => {
  router.push('/login')
}

// 前往注册
const goToRegister = () => {
  router.push('/register')
}

// 滚动到功能区域
const scrollToFeatures = () => {
  document.getElementById('features')?.scrollIntoView({ behavior: 'smooth' })
}

onMounted(() => {
  fetchStats()
})
</script>

<style scoped>
.front-container {
  background: #FFFFFF;
}

/* Hero 区域 */
.hero-section {
  position: relative;
  min-height: calc(100vh - 70px);
  display: flex;
  align-items: center;
  overflow: hidden;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.hero-bg {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  overflow: hidden;
}

.hero-shape {
  position: absolute;
  border-radius: 50%;
  opacity: 0.1;
}

.shape-1 {
  width: 500px;
  height: 500px;
  background: white;
  top: -100px;
  right: -100px;
  animation: float 20s ease-in-out infinite;
}

.shape-2 {
  width: 300px;
  height: 300px;
  background: white;
  bottom: -50px;
  left: 10%;
  animation: float 15s ease-in-out infinite reverse;
}

.shape-3 {
  width: 200px;
  height: 200px;
  background: white;
  top: 50%;
  left: -50px;
  animation: float 18s ease-in-out infinite;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0) rotate(0deg);
  }
  50% {
    transform: translateY(-30px) rotate(180deg);
  }
}

.hero-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 80px;
  align-items: center;
}

.hero-container {
  position: relative;
  z-index: 1;
}

.hero-content {
  animation: fadeInUp 1s ease-out;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.hero-badge {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  background: rgba(255, 255, 255, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 50px;
  padding: 8px 20px;
  color: white;
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 24px;
  backdrop-filter: blur(10px);
  animation: fadeIn 1s ease-out 0.2s both;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

.hero-title {
  font-size: 52px;
  font-weight: 700;
  color: white;
  margin: 0 0 20px 0;
  line-height: 1.2;
  animation: fadeInUp 1s ease-out 0.3s both;
}

.hero-subtitle {
  font-size: 24px;
  color: rgba(255, 255, 255, 0.95);
  margin: 0 0 16px 0;
  line-height: 1.4;
  font-weight: 500;
  animation: fadeInUp 1s ease-out 0.4s both;
}

.hero-description {
  font-size: 16px;
  color: rgba(255, 255, 255, 0.85);
  margin: 0 0 32px 0;
  line-height: 1.8;
  animation: fadeInUp 1s ease-out 0.5s both;
}

.hero-actions {
  display: flex;
  gap: 16px;
  margin-bottom: 48px;
  animation: fadeInUp 1s ease-out 0.6s both;
}

.primary-btn {
  background: white;
  color: #667eea;
  border: none;
  padding: 14px 32px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 12px;
  transition: all 0.3s;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.primary-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.25);
  background: #f8f9fa;
}

.secondary-btn {
  background: rgba(255, 255, 255, 0.15);
  color: white;
  border: 2px solid white;
  padding: 14px 32px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 12px;
  transition: all 0.3s;
  backdrop-filter: blur(10px);
}

.secondary-btn:hover {
  background: white;
  color: #667eea;
  transform: translateY(-3px);
  box-shadow: 0 8px 30px rgba(255, 255, 255, 0.3);
}

.hero-stats {
  display: flex;
  gap: 24px;
  animation: fadeInUp 1s ease-out 0.7s both;
}

.stat-card {
  flex: 1;
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 16px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 16px;
  transition: all 0.3s;
}

.stat-card:hover {
  background: rgba(255, 255, 255, 0.25);
  transform: translateY(-5px);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
}

.stat-icon {
  width: 56px;
  height: 56px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 28px;
}

.stat-info {
  flex: 1;
}

.stat-number {
  font-size: 32px;
  font-weight: 700;
  color: white;
  margin-bottom: 4px;
  font-family: 'Arial', sans-serif;
}

.stat-label {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.85);
  font-weight: 500;
}

.hero-image {
  position: relative;
  animation: fadeInRight 1s ease-out 0.5s both;
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

.image-wrapper {
  position: relative;
  border-radius: 24px;
  overflow: hidden;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  transition: all 0.5s;
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(20px);
  border: 2px solid rgba(255, 255, 255, 0.2);
}

.image-wrapper:hover {
  transform: scale(1.05) rotate(2deg);
  box-shadow: 0 30px 80px rgba(0, 0, 0, 0.4);
}

.hero-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100px 60px;
  color: white;
  text-align: center;
}

.hero-placeholder p {
  margin-top: 24px;
  font-size: 20px;
  font-weight: 600;
  color: white;
  opacity: 0.95;
}

.image-wrapper img {
  width: 100%;
  height: auto;
  display: block;
  transition: all 0.5s;
}

.image-wrapper:hover img {
  transform: scale(1.1);
}

/* 功能特性 */
.features-section {
  padding: 100px 24px;
  background: #f8f9fa;
}

.section-header {
  text-align: center;
  margin-bottom: 60px;
}

.section-badge {
  display: inline-block;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 8px 20px;
  border-radius: 50px;
  font-size: 14px;
  font-weight: 600;
  margin-bottom: 16px;
  animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
}

.section-title {
  font-size: 40px;
  font-weight: 700;
  color: #1f2937;
  margin: 0 0 16px 0;
}

.section-subtitle {
  font-size: 18px;
  color: #6b7280;
  margin: 0;
  line-height: 1.6;
}

.features-grid {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 32px;
}

.feature-card {
  background: white;
  border-radius: 20px;
  padding: 40px 32px;
  text-align: center;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  cursor: pointer;
  border: 2px solid transparent;
  position: relative;
  overflow: hidden;
}

.feature-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  opacity: 0;
  transition: opacity 0.4s;
  z-index: 0;
}

.feature-card:hover::before {
  opacity: 0.05;
}

.feature-card:hover {
  transform: translateY(-10px) scale(1.02);
  box-shadow: 0 20px 60px rgba(102, 126, 234, 0.25);
  border-color: #667eea;
}

.feature-card > * {
  position: relative;
  z-index: 1;
}

.feature-icon {
  width: 80px;
  height: 80px;
  border-radius: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 24px;
  color: white;
  transition: all 0.4s;
  box-shadow: 0 10px 30px rgba(102, 126, 234, 0.3);
}

.feature-card:hover .feature-icon {
  transform: scale(1.15) rotate(5deg);
  box-shadow: 0 15px 40px rgba(102, 126, 234, 0.5);
}

.feature-title {
  font-size: 20px;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 12px 0;
  transition: color 0.3s;
}

.feature-card:hover .feature-title {
  color: #667eea;
}

.feature-desc {
  font-size: 15px;
  color: #6b7280;
  line-height: 1.8;
  margin: 0;
}

/* 使用指南 */
.guide-section {
  padding: 100px 24px;
  background: white;
}

.guide-steps {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 40px;
}

.step-card {
  flex: 1;
  max-width: 300px;
  background: linear-gradient(135deg, #f8f9fa 0%, #ffffff 100%);
  border-radius: 20px;
  padding: 40px 32px;
  text-align: center;
  border: 2px solid #e9ecef;
  transition: all 0.4s;
  position: relative;
}

.step-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.1);
  border-color: #667eea;
}

.step-number {
  position: absolute;
  top: -20px;
  left: 50%;
  transform: translateX(-50%);
  width: 50px;
  height: 50px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 20px;
  font-weight: 700;
  box-shadow: 0 10px 30px rgba(102, 126, 234, 0.4);
}

.step-icon {
  width: 80px;
  height: 80px;
  margin: 20px auto 24px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  transition: all 0.4s;
}

.step-card:hover .step-icon {
  transform: scale(1.1) rotate(5deg);
}

.step-title {
  font-size: 20px;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 12px 0;
}

.step-desc {
  font-size: 15px;
  color: #6b7280;
  line-height: 1.6;
  margin: 0;
}

.step-arrow {
  color: #667eea;
  animation: bounce 2s ease-in-out infinite;
}

@keyframes bounce {
  0%, 100% {
    transform: translateX(0);
  }
  50% {
    transform: translateX(10px);
  }
}

/* 关于系统 */
.about-section {
  padding: 100px 24px;
  background: #f8f9fa;
}

.about-container {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 80px;
  align-items: center;
}

.about-image {
  position: relative;
  animation: fadeInLeft 1s ease-out;
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

.about-image .image-card {
  border-radius: 24px;
  overflow: hidden;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
  transition: all 0.5s;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.about-image .image-card:hover {
  transform: scale(1.05);
  box-shadow: 0 30px 80px rgba(0, 0, 0, 0.25);
}

.image-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 40px;
  color: white;
}

.image-placeholder p {
  margin-top: 20px;
  font-size: 24px;
  font-weight: 600;
  color: white;
}

.about-image .image-card img {
  width: 100%;
  height: auto;
  display: block;
  transition: all 0.5s;
}

.about-image .image-card:hover img {
  transform: scale(1.1);
}

.about-content {
  animation: fadeInRight 1s ease-out;
}

.about-text {
  font-size: 16px;
  color: #6b7280;
  line-height: 1.8;
  margin: 24px 0 32px;
}

.about-highlights {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.highlight-item {
  display: flex;
  align-items: flex-start;
  gap: 16px;
  padding: 20px;
  background: white;
  border-radius: 16px;
  transition: all 0.3s;
  border: 2px solid transparent;
}

.highlight-item:hover {
  transform: translateX(10px);
  border-color: #667eea;
  box-shadow: 0 10px 30px rgba(102, 126, 234, 0.15);
}

.highlight-icon {
  width: 48px;
  height: 48px;
  min-width: 48px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 24px;
}

.highlight-text h4 {
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 8px 0;
}

.highlight-text p {
  font-size: 14px;
  color: #6b7280;
  margin: 0;
  line-height: 1.6;
}

/* 联系我们 */
.contact-section {
  padding: 100px 24px;
  background: white;
}

.contact-container {
  max-width: 1200px;
  margin: 0 auto;
}

.contact-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
  margin-top: 60px;
}

.contact-card {
  background: linear-gradient(135deg, #f8f9fa 0%, #ffffff 100%);
  border-radius: 20px;
  padding: 32px 24px;
  text-align: center;
  border: 2px solid #e9ecef;
  transition: all 0.4s;
}

.contact-card:hover {
  transform: translateY(-10px);
  border-color: #667eea;
  box-shadow: 0 20px 60px rgba(102, 126, 234, 0.15);
}

.contact-icon {
  width: 64px;
  height: 64px;
  margin: 0 auto 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  transition: all 0.4s;
}

.contact-card:hover .contact-icon {
  transform: scale(1.1) rotate(5deg);
}

.contact-card h4 {
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 12px 0;
}

.contact-card p {
  font-size: 15px;
  color: #6b7280;
  margin: 0;
  line-height: 1.6;
}

/* 响应式 */
@media (max-width: 1024px) {
  .hero-container {
    gap: 60px;
  }

  .features-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .guide-steps {
    flex-wrap: wrap;
  }

  .step-arrow {
    display: none;
  }

  .contact-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .hero-section {
    min-height: auto;
    padding: 80px 0 60px;
  }

  .hero-container {
    grid-template-columns: 1fr;
    gap: 40px;
  }

  .hero-title {
    font-size: 36px;
  }

  .hero-subtitle {
    font-size: 20px;
  }

  .hero-stats {
    flex-direction: column;
    gap: 16px;
  }

  .hero-image {
    order: -1;
  }

  .features-section,
  .guide-section,
  .about-section,
  .contact-section {
    padding: 60px 24px;
  }

  .section-title {
    font-size: 32px;
  }

  .features-grid {
    grid-template-columns: 1fr;
    gap: 24px;
  }

  .guide-steps {
    flex-direction: column;
  }

  .step-card {
    max-width: 100%;
  }

  .about-container {
    grid-template-columns: 1fr;
    gap: 40px;
  }

  .about-image {
    order: -1;
  }

  .contact-grid {
    grid-template-columns: 1fr;
    gap: 20px;
  }
}

@media (max-width: 480px) {
  .hero-title {
    font-size: 28px;
  }

  .hero-subtitle {
    font-size: 18px;
  }

  .hero-actions {
    flex-direction: column;
    width: 100%;
  }

  .primary-btn,
  .secondary-btn {
    width: 100%;
    justify-content: center;
  }

  .section-title {
    font-size: 28px;
  }

  .section-subtitle {
    font-size: 16px;
  }
}
</style>
