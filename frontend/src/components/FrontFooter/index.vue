<template>
  <footer class="front-footer">
    <div class="footer-container">
      <div class="footer-content">
        <div class="footer-section brand-section">
          <div class="footer-logo">
            <div class="logo-icon">
              <el-icon><School /></el-icon>
            </div>
            <div class="logo-text">
              <h3>智慧宿舍管理系统</h3>
              <p>让宿舍生活更便捷、更智能、更美好</p>
            </div>
          </div>
          <div class="social-links">
            <a href="#" class="social-link">
              <el-icon><ChatDotRound /></el-icon>
            </a>
            <a href="#" class="social-link">
              <el-icon><Message /></el-icon>
            </a>
            <a href="#" class="social-link">
              <el-icon><Phone /></el-icon>
            </a>
          </div>
        </div>
        
        <div class="footer-section">
          <h4>快速链接</h4>
          <a href="#home" @click.prevent="scrollToSection('home')">
            <el-icon><HomeFilled /></el-icon>
            首页
          </a>
          <a href="#features" @click.prevent="scrollToSection('features')">
            <el-icon><Grid /></el-icon>
            功能特性
          </a>
          <a href="#about" @click.prevent="scrollToSection('about')">
            <el-icon><InfoFilled /></el-icon>
            关于系统
          </a>
          <a href="#contact" @click.prevent="scrollToSection('contact')">
            <el-icon><ChatLineRound /></el-icon>
            联系我们
          </a>
        </div>
        
        <div class="footer-section">
          <h4>核心功能</h4>
          <a href="#">
            <el-icon><House /></el-icon>
            宿舍信息
          </a>
          <a href="#">
            <el-icon><Tools /></el-icon>
            在线报修
          </a>
          <a href="#">
            <el-icon><User /></el-icon>
            访客登记
          </a>
          <a href="#">
            <el-icon><Wallet /></el-icon>
            费用查询
          </a>
        </div>
        
        <div class="footer-section">
          <h4>联系方式</h4>
          <div class="contact-item">
            <el-icon><Location /></el-icon>
            <span>学生公寓1号楼一楼大厅</span>
          </div>
          <div class="contact-item">
            <el-icon><Phone /></el-icon>
            <span>0571-88888888</span>
          </div>
          <div class="contact-item">
            <el-icon><Message /></el-icon>
            <span>dorm@university.edu.cn</span>
          </div>
          <div class="contact-item">
            <el-icon><Clock /></el-icon>
            <span>周一至周日 8:00-22:00</span>
          </div>
        </div>
      </div>
      
      <div class="footer-bottom">
        <p>© 2025 智慧宿舍管理系统. All rights reserved.</p>
        <div class="footer-links">
          <a href="#">隐私政策</a>
          <span>|</span>
          <a href="#">服务条款</a>
          <span>|</span>
          <a href="#">帮助中心</a>
        </div>
      </div>
    </div>
  </footer>
</template>

<script setup>
import { 
  School, ChatDotRound, Message, Phone, HomeFilled, Grid, InfoFilled, 
  ChatLineRound, House, Tools, User, Wallet, Location, Clock 
} from '@element-plus/icons-vue'

const scrollToSection = (section) => {
  const element = document.getElementById(section)
  if (element) {
    element.scrollIntoView({ behavior: 'smooth' })
  }
}
</script>

<style scoped>
/* 页脚 */
.front-footer {
  background: linear-gradient(135deg, #1f2937 0%, #111827 100%);
  color: white;
  padding: 80px 24px 32px;
  position: relative;
  overflow: hidden;
}

.front-footer::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 1px;
  background: linear-gradient(90deg, transparent, rgba(102, 126, 234, 0.5), transparent);
}

.footer-container {
  max-width: 1200px;
  margin: 0 auto;
}

.footer-content {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr 1.5fr;
  gap: 60px;
  margin-bottom: 60px;
}

/* 品牌区域 */
.brand-section {
  display: flex;
  flex-direction: column;
  gap: 32px;
}

.footer-logo {
  display: flex;
  align-items: flex-start;
  gap: 16px;
}

.logo-icon {
  width: 56px;
  height: 56px;
  min-width: 56px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 28px;
  box-shadow: 0 8px 24px rgba(102, 126, 234, 0.3);
}

.logo-text h3 {
  font-size: 20px;
  font-weight: 700;
  margin: 0 0 8px 0;
  color: white;
}

.logo-text p {
  font-size: 14px;
  color: #9ca3af;
  margin: 0;
  line-height: 1.6;
}

.social-links {
  display: flex;
  gap: 12px;
}

.social-link {
  width: 40px;
  height: 40px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #9ca3af;
  transition: all 0.3s;
  font-size: 18px;
}

.social-link:hover {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
}

/* 链接区域 */
.footer-section h4 {
  font-size: 16px;
  font-weight: 600;
  margin: 0 0 20px 0;
  color: white;
}

.footer-section a {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #9ca3af;
  text-decoration: none;
  margin: 12px 0;
  transition: all 0.3s;
  font-size: 14px;
}

.footer-section a:hover {
  color: #667eea;
  transform: translateX(5px);
}

.footer-section a .el-icon {
  font-size: 16px;
}

/* 联系方式 */
.contact-item {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  color: #9ca3af;
  margin: 12px 0;
  font-size: 14px;
}

.contact-item .el-icon {
  font-size: 16px;
  margin-top: 2px;
  color: #667eea;
}

.contact-item span {
  flex: 1;
  line-height: 1.6;
}

/* 页脚底部 */
.footer-bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 32px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  color: #6b7280;
  font-size: 14px;
}

.footer-bottom p {
  margin: 0;
}

.footer-links {
  display: flex;
  align-items: center;
  gap: 16px;
}

.footer-links a {
  color: #9ca3af;
  text-decoration: none;
  transition: color 0.3s;
}

.footer-links a:hover {
  color: #667eea;
}

.footer-links span {
  color: #4b5563;
}

/* 响应式 */
@media (max-width: 1024px) {
  .footer-content {
    grid-template-columns: repeat(2, 1fr);
    gap: 40px;
  }
}

@media (max-width: 768px) {
  .footer-content {
    grid-template-columns: 1fr;
    gap: 40px;
  }

  .footer-bottom {
    flex-direction: column;
    gap: 16px;
    text-align: center;
  }

  .footer-links {
    flex-wrap: wrap;
    justify-content: center;
  }
}
</style>
