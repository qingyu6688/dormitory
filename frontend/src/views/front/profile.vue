<template>
  <div class="front-profile-container">
    <!-- 返回按钮 -->
    <div class="back-button">
      <el-button @click="goToHome" text>
        <el-icon><ArrowLeft /></el-icon>
        返回首页
      </el-button>
    </div>
    
    <!-- 顶部横幅 -->
    <div class="profile-banner">
      <div class="banner-bg">
        <div class="banner-pattern"></div>
        <div class="banner-glow"></div>
      </div>
      <div class="banner-content">
        <div class="avatar-wrapper">
          <el-upload
            class="avatar-uploader"
            :action="uploadUrl"
            :headers="uploadHeaders"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
          >
            <el-avatar :size="140" :src="avatarUrl" class="user-avatar">
              <el-icon :size="70"><User /></el-icon>
            </el-avatar>
            <div class="avatar-overlay">
              <el-icon :size="28"><Camera /></el-icon>
              <span>更换头像</span>
            </div>
          </el-upload>
        </div>
        <div class="user-info">
          <h1 class="user-name">{{ userInfo?.nickname || userInfo?.username || '用户名' }}</h1>
          <p class="user-meta">
            <el-tag type="primary" size="large" effect="dark" class="role-tag">
              <el-icon><School /></el-icon>
              {{ userInfo?.roleName || '学生' }}
            </el-tag>
            <span class="user-id">ID: {{ userInfo?.studentNo || '-' }}</span>
          </p>
          <div class="user-stats">
            <div class="stat-item">
              <div class="stat-value">{{ userInfo?.grade || '-' }}</div>
              <div class="stat-label">年级</div>
            </div>
            <div class="stat-divider"></div>
            <div class="stat-item">
              <div class="stat-value">{{ userInfo?.className || '-' }}</div>
              <div class="stat-label">班级</div>
            </div>
            <div class="stat-divider"></div>
            <div class="stat-item">
              <div class="stat-value">{{ roomInfo !== '未分配' ? '已分配' : '未分配' }}</div>
              <div class="stat-label">宿舍</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 信息卡片区域 -->
    <div class="info-cards">
      <div class="info-card">
        <div class="card-icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
          <el-icon :size="32"><Postcard /></el-icon>
        </div>
        <div class="card-content">
          <div class="card-label">学号</div>
          <div class="card-value">{{ userInfo?.studentNo || '未设置' }}</div>
        </div>
      </div>
      <div class="info-card">
        <div class="card-icon" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
          <el-icon :size="32"><School /></el-icon>
        </div>
        <div class="card-content">
          <div class="card-label">专业班级</div>
          <div class="card-value">{{ getMajorClass() }}</div>
        </div>
      </div>
      <div class="info-card">
        <div class="card-icon" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
          <el-icon :size="32"><House /></el-icon>
        </div>
        <div class="card-content">
          <div class="card-label">宿舍</div>
          <div class="card-value">{{ roomInfo || '未分配' }}</div>
        </div>
      </div>
      <div class="info-card">
        <div class="card-icon" style="background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);">
          <el-icon :size="32"><Phone /></el-icon>
        </div>
        <div class="card-content">
          <div class="card-label">手机号</div>
          <div class="card-value">{{ userInfo?.phone || '未设置' }}</div>
        </div>
      </div>
    </div>

    <!-- 编辑区域 -->
    <div class="edit-section">
      <div class="section-header">
        <h2 class="section-title">
          <el-icon><Edit /></el-icon>
          个人资料管理
        </h2>
        <p class="section-desc">完善您的个人信息，让我们更好地为您服务</p>
      </div>
      <el-tabs v-model="activeTab" class="profile-tabs">
        <el-tab-pane label="编辑资料" name="profile">
          <el-form :model="profileForm" :rules="profileRules" ref="profileFormRef" label-width="100px" class="profile-form">
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="profileForm.nickname" placeholder="请输入昵称">
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="学号" prop="studentNo">
          <el-input v-model="profileForm.studentNo" placeholder="请输入学号">
            <template #prefix>
              <el-icon><Postcard /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="profileForm.gender">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="2">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="学院" prop="college">
          <el-input v-model="profileForm.college" placeholder="请输入学院">
            <template #prefix>
              <el-icon><School /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="专业" prop="major">
          <el-input v-model="profileForm.major" placeholder="请输入专业">
            <template #prefix>
              <el-icon><School /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="班级" prop="className">
          <el-input v-model="profileForm.className" placeholder="请输入班级">
            <template #prefix>
              <el-icon><School /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="年级" prop="grade">
          <el-input-number v-model="profileForm.grade" :min="2020" :max="2030" placeholder="请输入年级" style="width: 100%" />
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="profileForm.idCard" placeholder="请输入身份证号">
            <template #prefix>
              <el-icon><Postcard /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="profileForm.email" placeholder="请输入邮箱">
            <template #prefix>
              <el-icon><Message /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="profileForm.phone" placeholder="请输入手机号">
            <template #prefix>
              <el-icon><Phone /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="家长电话" prop="parentPhone">
          <el-input v-model="profileForm.parentPhone" placeholder="请输入家长电话">
            <template #prefix>
              <el-icon><Phone /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="紧急联系人" prop="emergencyContact">
          <el-input v-model="profileForm.emergencyContact" placeholder="请输入紧急联系人">
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="紧急电话" prop="emergencyPhone">
          <el-input v-model="profileForm.emergencyPhone" placeholder="请输入紧急联系电话">
            <template #prefix>
              <el-icon><Phone /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <div class="form-actions">
            <el-button type="primary" @click="handleUpdateProfile" :loading="loading" size="large" class="save-btn">
              <el-icon><Check /></el-icon>
              保存修改
            </el-button>
            <el-button @click="resetProfileForm" size="large" class="reset-btn">
              <el-icon><Refresh /></el-icon>
              重置
            </el-button>
          </div>
        </el-form-item>
      </el-form>
        </el-tab-pane>

        <el-tab-pane label="修改密码" name="password">
          <el-form :model="passwordForm" :rules="passwordRules" ref="passwordFormRef" label-width="100px" class="profile-form">
        <el-alert
          title="为了您的账号安全，建议定期更换密码"
          type="info"
          :closable="false"
          style="margin-bottom: 20px;"
        />
        <el-form-item label="原密码" prop="oldPassword">
          <el-input v-model="passwordForm.oldPassword" type="password" placeholder="请输入原密码" show-password>
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="passwordForm.newPassword" type="password" placeholder="请输入新密码" show-password>
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="passwordForm.confirmPassword" type="password" placeholder="请再次输入新密码" show-password>
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <div class="form-actions">
            <el-button type="primary" @click="handleUpdatePassword" :loading="loading" size="large" class="save-btn">
              <el-icon><Check /></el-icon>
              修改密码
            </el-button>
            <el-button @click="resetPasswordForm" size="large" class="reset-btn">
              <el-icon><Refresh /></el-icon>
              重置
            </el-button>
          </div>
        </el-form-item>
      </el-form>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  User, Camera, Message, Phone, Calendar, Edit, Lock, ArrowLeft, Postcard, School, House, Check, Refresh
} from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()
const userInfo = computed(() => userStore.userInfo)

const activeTab = ref('profile')
const loading = ref(false)
const profileFormRef = ref(null)
const passwordFormRef = ref(null)
const avatarUrl = ref('')

// 上传配置
const uploadUrl = computed(() => '/api/file/upload')
const uploadHeaders = computed(() => {
  const token = localStorage.getItem('token')
  return {
    Authorization: token ? `Bearer ${token}` : ''
  }
})

const profileForm = reactive({
  nickname: '',
  studentNo: '',
  gender: null,
  college: '',
  major: '',
  className: '',
  grade: null,
  idCard: '',
  email: '',
  phone: '',
  parentPhone: '',
  emergencyContact: '',
  emergencyPhone: ''
})

const roomInfo = computed(() => {
  if (userInfo.value?.buildingName && userInfo.value?.roomNo) {
    return `${userInfo.value.buildingName} ${userInfo.value.roomNo}`
  }
  return '未分配'
})

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const profileRules = {
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' }
  ],
  studentNo: [
    { required: true, message: '请输入学号', trigger: 'blur' }
  ],
  gender: [
    { required: true, message: '请选择性别', trigger: 'change' }
  ],
  college: [
    { required: true, message: '请输入学院', trigger: 'blur' }
  ],
  major: [
    { required: true, message: '请输入专业', trigger: 'blur' }
  ],
  className: [
    { required: true, message: '请输入班级', trigger: 'blur' }
  ],
  grade: [
    { required: true, message: '请输入年级', trigger: 'change' }
  ],
  idCard: [
    { required: true, message: '请输入身份证号', trigger: 'blur' },
    { pattern: /^[1-9]\d{5}(18|19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])\d{3}[\dXx]$/, message: '请输入正确的身份证号', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  parentPhone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  emergencyPhone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ]
}

const passwordRules = {
  oldPassword: [
    { required: true, message: '请输入原密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
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

// 更新资料
const handleUpdateProfile = async () => {
  if (!profileFormRef.value) return
  
  await profileFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const { updateUser } = await import('@/api/user')
        await updateUser({
          id: userInfo.value.id,
          nickname: profileForm.nickname,
          studentNo: profileForm.studentNo,
          gender: profileForm.gender,
          college: profileForm.college,
          major: profileForm.major,
          className: profileForm.className,
          grade: profileForm.grade,
          idCard: profileForm.idCard,
          email: profileForm.email,
          phone: profileForm.phone,
          parentPhone: profileForm.parentPhone,
          emergencyContact: profileForm.emergencyContact,
          emergencyPhone: profileForm.emergencyPhone
        })
        
        // 更新Store中的用户信息
        await userStore.fetchUserInfo()
        
        ElMessage.success('资料更新成功')
      } catch (error) {
        console.error('资料更新失败:', error)
        ElMessage.error('资料更新失败')
      } finally {
        loading.value = false
      }
    }
  })
}

// 修改密码
const handleUpdatePassword = async () => {
  if (!passwordFormRef.value) return
  
  await passwordFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const { updatePassword } = await import('@/api/user')
        await updatePassword(
          userInfo.value.id,
          passwordForm.oldPassword,
          passwordForm.newPassword
        )
        
        ElMessage.success('密码修改成功，请重新登录')
        
        // 延迟1秒后退出登录并跳转到登录页
        setTimeout(async () => {
          await userStore.logout()
          router.push('/login')
        }, 1000)
      } catch (error) {
        console.error('密码修改失败:', error)
        ElMessage.error(error.response?.data?.message || '密码修改失败')
      } finally {
        loading.value = false
      }
    }
  })
}

// 重置表单
const resetProfileForm = () => {
  profileFormRef.value?.resetFields()
}

const resetPasswordForm = () => {
  passwordFormRef.value?.resetFields()
}

// 头像上传前验证
const beforeAvatarUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB!')
    return false
  }
  return true
}

// 头像上传成功
const handleAvatarSuccess = async (response) => {
  if (response.code === 200) {
    const newAvatarUrl = response.data.url
    avatarUrl.value = newAvatarUrl
    
    // 更新数据库中的头像
    try {
      const { updateAvatar } = await import('@/api/user')
      await updateAvatar(userInfo.value.id, newAvatarUrl)
      
      // 更新Store中的用户信息
      userStore.updateAvatar(newAvatarUrl)
      
      ElMessage.success('头像更新成功')
    } catch (error) {
      console.error('头像更新失败:', error)
      ElMessage.error('头像更新失败')
    }
  } else {
    ElMessage.error('头像上传失败')
  }
}

// 返回首页
const goToHome = () => {
  router.push('/front')
}

// 获取专业班级显示
const getMajorClass = () => {
  const parts = []
  if (userInfo.value?.major) parts.push(userInfo.value.major)
  if (userInfo.value?.className) parts.push(userInfo.value.className)
  return parts.length > 0 ? parts.join(' ') : '未设置'
}

onMounted(() => {
  // 初始化表单数据
  if (userInfo.value) {
    profileForm.nickname = userInfo.value.nickname || ''
    profileForm.studentNo = userInfo.value.studentNo || ''
    profileForm.gender = userInfo.value.gender || null
    profileForm.college = userInfo.value.college || ''
    profileForm.major = userInfo.value.major || ''
    profileForm.className = userInfo.value.className || ''
    profileForm.grade = userInfo.value.grade || null
    profileForm.idCard = userInfo.value.idCard || ''
    profileForm.email = userInfo.value.email || ''
    profileForm.phone = userInfo.value.phone || ''
    profileForm.parentPhone = userInfo.value.parentPhone || ''
    profileForm.emergencyContact = userInfo.value.emergencyContact || ''
    profileForm.emergencyPhone = userInfo.value.emergencyPhone || ''
    avatarUrl.value = userInfo.value.avatar || ''
  }
})
</script>

<style scoped>
.front-profile-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 90px 24px 60px;
}

.back-button {
  margin-bottom: 20px;
}

.back-button .el-button {
  font-size: 16px;
  color: #2563EB;
  transition: all 0.3s;
}

.back-button .el-button:hover {
  color: #1D4ED8;
  transform: translateX(-4px);
}

/* 顶部横幅 */
.profile-banner {
  position: relative;
  margin-bottom: 40px;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.banner-bg {
  height: 200px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow: hidden;
}

.banner-pattern {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image: 
    repeating-linear-gradient(45deg, transparent, transparent 35px, rgba(255,255,255,.05) 35px, rgba(255,255,255,.05) 70px);
  animation: patternMove 20s linear infinite;
}

@keyframes patternMove {
  0% {
    transform: translateX(0);
  }
  100% {
    transform: translateX(70px);
  }
}

.banner-glow {
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(255,255,255,0.1) 0%, transparent 70%);
  animation: glowMove 15s ease-in-out infinite;
}

@keyframes glowMove {
  0%, 100% {
    transform: translate(0, 0);
  }
  50% {
    transform: translate(30%, 30%);
  }
}

.banner-content {
  position: relative;
  background: white;
  padding: 0 40px 32px;
  display: flex;
  align-items: flex-end;
  gap: 32px;
}

.avatar-wrapper {
  position: relative;
  margin-top: -70px;
}

.avatar-uploader {
  position: relative;
  cursor: pointer;
  display: inline-block;
}

.user-avatar {
  border: 6px solid white;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  transition: all 0.3s;
}

.avatar-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 140px;
  height: 140px;
  border-radius: 50%;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 6px;
  color: white;
  opacity: 0;
  transition: all 0.3s;
  font-size: 14px;
  font-weight: 500;
}

.avatar-uploader:hover .avatar-overlay {
  opacity: 1;
}

.avatar-uploader:hover .user-avatar {
  transform: scale(1.05);
}

.user-info {
  flex: 1;
  padding-bottom: 8px;
}

.user-name {
  font-size: 32px;
  font-weight: 700;
  color: #1f2937;
  margin: 0 0 12px 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.user-meta {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 20px;
}

.role-tag {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  border-radius: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  font-weight: 600;
}

.user-id {
  font-size: 14px;
  color: #6b7280;
  font-weight: 500;
  padding: 6px 12px;
  background: #f3f4f6;
  border-radius: 12px;
}

.user-stats {
  display: flex;
  align-items: center;
  gap: 24px;
  padding: 16px 0;
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: 20px;
  font-weight: 700;
  color: #1f2937;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 13px;
  color: #6b7280;
}

.stat-divider {
  width: 1px;
  height: 32px;
  background: #e5e7eb;
}

/* 信息卡片 */
.info-cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
  margin-bottom: 40px;
}

.info-card {
  background: white;
  border-radius: 16px;
  padding: 28px;
  display: flex;
  align-items: center;
  gap: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.info-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255,255,255,0.3), transparent);
  transition: left 0.5s;
}

.info-card:hover::before {
  left: 100%;
}

.info-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 32px rgba(102, 126, 234, 0.2);
}

.card-icon {
  width: 72px;
  height: 72px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  flex-shrink: 0;
  transition: all 0.3s;
  position: relative;
}

.card-icon::after {
  content: '';
  position: absolute;
  inset: -4px;
  border-radius: 18px;
  background: inherit;
  opacity: 0;
  filter: blur(12px);
  transition: opacity 0.3s;
}

.info-card:hover .card-icon {
  transform: scale(1.1) rotate(5deg);
}

.info-card:hover .card-icon::after {
  opacity: 0.6;
}

.card-content {
  flex: 1;
}

.card-label {
  font-size: 14px;
  color: #6b7280;
  margin-bottom: 6px;
}

.card-value {
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
  word-break: break-all;
}

/* 编辑区域 */
.edit-section {
  background: white;
  border-radius: 20px;
  padding: 40px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  position: relative;
}

.section-header {
  margin-bottom: 32px;
  padding-bottom: 24px;
  border-bottom: 2px solid #f3f4f6;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 24px;
  font-weight: 700;
  color: #1f2937;
  margin: 0 0 8px 0;
}

.section-title .el-icon {
  font-size: 28px;
  color: #667eea;
}

.section-desc {
  font-size: 14px;
  color: #6b7280;
  margin: 0;
}

.profile-tabs :deep(.el-tabs__header) {
  margin-bottom: 32px;
}

.profile-tabs :deep(.el-tabs__item) {
  font-size: 16px;
  font-weight: 600;
  padding: 0 24px;
  height: 48px;
  line-height: 48px;
}

.profile-tabs :deep(.el-tabs__active-bar) {
  height: 3px;
}

.profile-form {
  max-width: 600px;
  margin: 0 auto;
}

.profile-form :deep(.el-input),
.profile-form :deep(.el-textarea) {
  border-radius: 8px;
}

.profile-form :deep(.el-button) {
  border-radius: 12px;
  padding: 12px 32px;
  font-weight: 600;
  transition: all 0.3s;
}

.form-actions {
  display: flex;
  gap: 16px;
}

.save-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  position: relative;
  overflow: hidden;
}

.save-btn::before {
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

.save-btn:hover::before {
  width: 300px;
  height: 300px;
}

.save-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
}

.reset-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.profile-form :deep(.el-form-item__label) {
  font-weight: 600;
  color: #1f2937;
  font-size: 15px;
}

.profile-form :deep(.el-input__wrapper) {
  transition: all 0.3s;
}

.profile-form :deep(.el-input__wrapper):hover {
  box-shadow: 0 0 0 1px #667eea inset;
}

.profile-form :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #667eea inset, 0 4px 12px rgba(102, 126, 234, 0.15);
}

.profile-form :deep(.el-radio) {
  margin-right: 24px;
}

.profile-form :deep(.el-radio__input.is-checked .el-radio__inner) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-color: #667eea;
}

.profile-form :deep(.el-radio__input.is-checked + .el-radio__label) {
  color: #667eea;
  font-weight: 600;
}

/* 响应式 */
@media (max-width: 768px) {
  .front-profile-container {
    padding: 80px 16px 40px;
  }

  .banner-content {
    flex-direction: column;
    align-items: center;
    text-align: center;
    padding: 0 24px 24px;
  }

  .avatar-wrapper {
    margin-top: -70px;
  }

  .user-name {
    font-size: 24px;
  }

  .info-cards {
    grid-template-columns: repeat(2, 1fr);
  }

  @media (max-width: 480px) {
    .info-cards {
      grid-template-columns: 1fr;
    }
  }

  .profile-form {
    max-width: 100%;
  }
}
</style>
