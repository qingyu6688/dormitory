<template>
  <div class="profile-container">
    <!-- 顶部背景卡片 -->
    <div class="profile-header">
      <div class="header-bg"></div>
      <div class="header-content">
        <div class="avatar-section">
          <el-upload
            class="avatar-uploader"
            :action="uploadUrl"
            :headers="uploadHeaders"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
          >
            <el-avatar :size="120" :src="avatarUrl" class="user-avatar">
              <el-icon :size="60"><User /></el-icon>
            </el-avatar>
            <div class="avatar-overlay">
              <el-icon :size="24"><Camera /></el-icon>
              <span>更换头像</span>
            </div>
          </el-upload>
        </div>
        <div class="user-info">
          <h2 class="user-name">{{ userInfo?.username || '用户名' }}</h2>
          <p class="user-role">
            <el-tag type="primary" effect="plain">{{ getRoleName(userInfo?.roleId) }}</el-tag>
          </p>
        </div>
      </div>
    </div>

    <!-- 主要内容区 -->
    <el-row :gutter="20" class="content-row">
      <!-- 左侧信息卡片 -->
      <el-col :span="6">
        <el-card class="info-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <el-icon><InfoFilled /></el-icon>
              <span>基本信息</span>
            </div>
          </template>
          <div class="info-list">
            <div class="info-item">
              <div class="info-label">
                <el-icon><User /></el-icon>
                <span>用户名</span>
              </div>
              <div class="info-value">{{ userInfo?.username || '-' }}</div>
            </div>
            <div class="info-item">
              <div class="info-label">
                <el-icon><Message /></el-icon>
                <span>邮箱</span>
              </div>
              <div class="info-value">{{ userInfo?.email || '未设置' }}</div>
            </div>
            <div class="info-item">
              <div class="info-label">
                <el-icon><Phone /></el-icon>
                <span>手机号</span>
              </div>
              <div class="info-value">{{ userInfo?.phone || '未设置' }}</div>
            </div>
            <div class="info-item" v-if="userInfo?.roleId === 4">
              <div class="info-label">
                <el-icon><User /></el-icon>
                <span>学号</span>
              </div>
              <div class="info-value">{{ userInfo?.studentNo || '未设置' }}</div>
            </div>
            <div class="info-item" v-if="userInfo?.roleId === 4">
              <div class="info-label">
                <el-icon><School /></el-icon>
                <span>班级</span>
              </div>
              <div class="info-value">{{ userInfo?.className || '未设置' }}</div>
            </div>
            <div class="info-item">
              <div class="info-label">
                <el-icon><Calendar /></el-icon>
                <span>注册时间</span>
              </div>
              <div class="info-value">{{ formatDate(userInfo?.createTime) }}</div>
            </div>
            <div class="info-item">
              <div class="info-label">
                <el-icon><Clock /></el-icon>
                <span>最后登录</span>
              </div>
              <div class="info-value">{{ formatDate(userInfo?.lastLoginTime) }}</div>
            </div>
          </div>
        </el-card>

      </el-col>

      <!-- 右侧表单区 -->
      <el-col :span="18">
        <el-card class="form-card" shadow="hover">
          <el-tabs v-model="activeTab" class="profile-tabs">
            <!-- 编辑资料 -->
            <el-tab-pane label="编辑资料" name="profile">
              <el-form :model="profileForm" :rules="profileRules" ref="profileFormRef" label-width="100px" class="profile-form">
                <el-form-item label="昵称" prop="nickname">
                  <el-input v-model="profileForm.nickname" placeholder="请输入昵称">
                    <template #prefix>
                      <el-icon><User /></el-icon>
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
                <el-form-item label="学号" prop="studentNo" v-if="userInfo?.roleId === 4">
                  <el-input v-model="profileForm.studentNo" placeholder="请输入学号">
                    <template #prefix>
                      <el-icon><User /></el-icon>
                    </template>
                  </el-input>
                </el-form-item>
                <el-form-item label="班级" prop="className" v-if="userInfo?.roleId === 4">
                  <el-input v-model="profileForm.className" placeholder="请输入班级">
                    <template #prefix>
                      <el-icon><School /></el-icon>
                    </template>
                  </el-input>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="handleUpdateProfile" :loading="loading">
                    <el-icon><Check /></el-icon>
                    保存修改
                  </el-button>
                  <el-button @click="resetProfileForm">
                    <el-icon><Refresh /></el-icon>
                    重置
                  </el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>

            <!-- 安全设置 -->
            <el-tab-pane label="安全设置" name="security">
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
                  <el-button type="primary" @click="handleUpdatePassword" :loading="loading">
                    <el-icon><Check /></el-icon>
                    修改密码
                  </el-button>
                  <el-button @click="resetPasswordForm">
                    <el-icon><Refresh /></el-icon>
                    重置
                  </el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>

          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  User, Camera, InfoFilled, Message, Phone, Calendar, Clock,
  Lock, Check, Refresh, School
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
  email: '',
  phone: '',
  studentNo: '',
  className: ''
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
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  phone: [
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
        const updateData = {
          id: userInfo.value.id,
          nickname: profileForm.nickname,
          email: profileForm.email,
          phone: profileForm.phone
        }
        
        // 如果是学生，添加学号和班级
        if (userInfo.value.roleId === 4) {
          updateData.studentNo = profileForm.studentNo
          updateData.className = profileForm.className
        }
        
        await updateUser(updateData)
        
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

// 角色名称映射
const getRoleName = (roleId) => {
  const roleMap = {
    1: '超级管理员',
    2: '管理员',
    3: '宿管员',
    4: '学生',
    5: '辅导员'
  }
  return roleMap[roleId] || '普通用户'
}

// 格式化日期
const formatDate = (dateStr) => {
  if (!dateStr) return '未知'
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

onMounted(() => {
  // 初始化表单数据
  if (userInfo.value) {
    profileForm.nickname = userInfo.value.nickname || ''
    profileForm.email = userInfo.value.email || ''
    profileForm.phone = userInfo.value.phone || ''
    profileForm.studentNo = userInfo.value.studentNo || ''
    profileForm.className = userInfo.value.className || ''
    avatarUrl.value = userInfo.value.avatar || ''
  }
})
</script>

<style scoped>
.profile-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 60px);
}

/* 顶部背景卡片 */
.profile-header {
  position: relative;
  margin-bottom: 30px;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.header-bg {
  height: 200px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.header-content {
  position: relative;
  background: white;
  padding: 0 40px 30px;
  display: flex;
  align-items: flex-end;
  gap: 30px;
}

.avatar-section {
  position: relative;
  margin-top: -60px;
}

.avatar-uploader {
  position: relative;
  cursor: pointer;
  display: inline-block;
}

.user-avatar {
  border: 5px solid white;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  transition: all 0.3s;
}

.avatar-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 120px;
  height: 120px;
  border-radius: 50%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 4px;
  color: white;
  opacity: 0;
  transition: all 0.3s;
  font-size: 12px;
}

.avatar-uploader:hover .avatar-overlay {
  opacity: 1;
}

.avatar-uploader:hover .user-avatar {
  transform: scale(1.05);
}

.user-info {
  flex: 1;
  padding-bottom: 10px;
}

.user-name {
  font-size: 28px;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 8px 0;
}

.user-role {
  margin: 0;
}

/* 内容区 */
.content-row {
  margin-bottom: 20px;
}

.info-card,
.form-card {
  border-radius: 12px;
  border: none;
}

.card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
  color: #1f2937;
}

.info-list {
  padding: 10px 0;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 0;
  border-bottom: 1px solid #f3f4f6;
}

.info-item:last-child {
  border-bottom: none;
}

.info-label {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #6b7280;
  font-size: 14px;
}

.info-value {
  color: #1f2937;
  font-weight: 500;
}

/* 表单样式 */
.profile-tabs :deep(.el-tabs__header) {
  margin-bottom: 30px;
}

.profile-tabs :deep(.el-tabs__item) {
  font-size: 15px;
  font-weight: 500;
}

.profile-form {
  max-width: 600px;
}

.profile-form :deep(.el-input),
.profile-form :deep(.el-textarea) {
  border-radius: 8px;
}

.profile-form :deep(.el-button) {
  border-radius: 8px;
  padding: 12px 24px;
}

/* 响应式 */
@media (max-width: 768px) {
  .profile-container {
    padding: 10px;
  }

  .header-content {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }

  .avatar-section {
    margin-top: -60px;
  }
}
</style>
