<template>
  <div class="visitor-container">
    <!-- 非学生角色提示 -->
    <el-result
      v-if="!isStudent"
      icon="warning"
      title="无权访问"
      sub-title="该功能仅对学生开放，您当前的角色无法访问此页面"
    >
      <template #extra>
        <el-button type="primary" @click="goToAdmin">
          <el-icon><Right /></el-icon>
          返回后台管理
        </el-button>
        <el-button @click="goToHome">
          <el-icon><HomeFilled /></el-icon>
          返回首页
        </el-button>
      </template>
    </el-result>

    <!-- 学生内容 -->
    <div v-else>
      <div class="page-header">
        <h1 class="page-title">访客登记</h1>
        <p class="page-subtitle">在线预约访客，安全便捷的访客管理</p>
      </div>

      <el-row :gutter="24">
      <!-- 登记表单 -->
      <el-col :span="16">
        <el-card class="form-card modern-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <div class="header-icon">
                <el-icon><Edit /></el-icon>
              </div>
              <div class="header-content">
                <span class="header-title">访客预约</span>
                <span class="header-subtitle">Visitor Registration</span>
              </div>
            </div>
          </template>

          <el-form :model="formData" :rules="rules" ref="formRef" label-width="100px">
            <el-form-item label="访客姓名" prop="visitorName">
              <el-input v-model="formData.visitorName" placeholder="请输入访客姓名" />
            </el-form-item>

            <el-form-item label="访客电话" prop="visitorPhone">
              <el-input v-model="formData.visitorPhone" placeholder="请输入访客电话" />
            </el-form-item>

            <el-form-item label="访客性别" prop="visitorGender">
              <el-radio-group v-model="formData.visitorGender">
                <el-radio :label="1">男</el-radio>
                <el-radio :label="2">女</el-radio>
              </el-radio-group>
            </el-form-item>

            <el-form-item label="身份证号" prop="visitorIdCard">
              <el-input v-model="formData.visitorIdCard" placeholder="请输入身份证号" />
            </el-form-item>

            <el-form-item label="预计离开时间" prop="planLeaveTime">
              <el-date-picker
                v-model="formData.planLeaveTime"
                type="datetime"
                placeholder="选择预计离开时间"
                style="width: 100%"
              />
            </el-form-item>

            <el-form-item label="来访事由" prop="visitReason">
              <el-input
                v-model="formData.visitReason"
                type="textarea"
                :rows="3"
                placeholder="请输入来访事由"
                maxlength="200"
                show-word-limit
              />
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="handleSubmit" :loading="loading">
                <el-icon><Check /></el-icon>
                提交预约
              </el-button>
              <el-button @click="handleReset">
                <el-icon><Refresh /></el-icon>
                重置
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>

      <!-- 访客记录 -->
      <el-col :span="8">
        <el-card class="record-card modern-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <div class="header-icon">
                <el-icon><List /></el-icon>
              </div>
              <div class="header-content">
                <span class="header-title">最近访客</span>
                <span class="header-subtitle">Recent Visitors</span>
              </div>
            </div>
          </template>

          <div class="record-list">
            <div v-for="item in recentVisitors" :key="item.id" class="record-item">
              <div class="visitor-avatar">
                <el-icon><User /></el-icon>
              </div>
              <div class="visitor-info">
                <div class="visitor-name">{{ item.visitorName }}</div>
                <div class="visitor-phone">{{ item.visitorPhone }}</div>
                <div class="visitor-time">{{ formatTime(item.enterTime) }}</div>
              </div>
              <el-tag :type="getStatusType(item.status)" size="small">
                {{ getStatusText(item.status) }}
              </el-tag>
            </div>

            <el-empty v-if="recentVisitors.length === 0" description="暂无访客记录" />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 访客列表 -->
    <el-card class="list-card modern-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <div class="header-icon">
            <el-icon><Document /></el-icon>
          </div>
          <div class="header-content">
            <span class="header-title">全部访客记录</span>
            <span class="header-subtitle">All Visitor Records</span>
          </div>
        </div>
      </template>

      <el-table :data="tableData" style="width: 100%">
        <el-table-column prop="visitorName" label="访客姓名" width="120" />
        <el-table-column prop="visitorGender" label="性别" width="80">
          <template #default="{ row }">
            {{ row.visitorGender === 1 ? '男' : '女' }}
          </template>
        </el-table-column>
        <el-table-column prop="visitorPhone" label="联系电话" width="140" />
        <el-table-column prop="visitReason" label="来访事由" min-width="200" show-overflow-tooltip />
        <el-table-column prop="buildingName" label="宿舍楼" width="150" />
        <el-table-column prop="enterTime" label="进入时间" width="180">
          <template #default="{ row }">
            {{ formatTime(row.enterTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="planLeaveTime" label="预计离开" width="180">
          <template #default="{ row }">
            {{ formatTime(row.planLeaveTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.size"
        :total="pagination.total"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="fetchVisitorList"
        @current-change="fetchVisitorList"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import {
  Edit, Check, Refresh, List, Document, User, Right, HomeFilled
} from '@element-plus/icons-vue'
import request from '@/utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref(null)
const loading = ref(false)

// 检查是否为学生角色（roleId = 4）
const isStudent = computed(() => userStore.userInfo?.roleId === 4)

const formData = reactive({
  visitorName: '',
  visitorGender: 1,
  visitorIdCard: '',
  visitorPhone: '',
  visitReason: '',
  planLeaveTime: ''
})

const rules = {
  visitorName: [{ required: true, message: '请输入访客姓名', trigger: 'blur' }],
  visitorGender: [{ required: true, message: '请选择访客性别', trigger: 'change' }],
  visitorPhone: [
    { required: true, message: '请输入访客电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  visitorIdCard: [
    { required: true, message: '请输入身份证号', trigger: 'blur' },
    { pattern: /^[1-9]\d{5}(18|19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])\d{3}[\dXx]$/, message: '请输入正确的身份证号', trigger: 'blur' }
  ],
  planLeaveTime: [{ required: true, message: '请选择预计离开时间', trigger: 'change' }],
  visitReason: [{ required: true, message: '请输入来访事由', trigger: 'blur' }]
}

const recentVisitors = ref([])
const tableData = ref([])
const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

// 提交预约
const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await request.post('/visitor/register', formData)
        ElMessage.success('访客登记成功')
        handleReset()
        await fetchVisitorList()
      } catch (error) {
        console.error('提交失败:', error)
        ElMessage.error(error.response?.data?.message || '提交失败')
      } finally {
        loading.value = false
      }
    }
  })
}

// 重置表单
const handleReset = () => {
  formRef.value?.resetFields()
}

// 获取访客列表
const fetchVisitorList = async () => {
  try {
    const res = await request.get('/visitor/my-list')
    tableData.value = res.data || []
    
    // 最近5条记录
    recentVisitors.value = tableData.value.slice(0, 5)
  } catch (error) {
    console.error('获取访客列表失败:', error)
    ElMessage.error('获取访客列表失败')
  }
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return '-'
  const date = new Date(time)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 取消预约
const handleCancel = async (row) => {
  ElMessageBox.confirm('确定要取消该访客预约吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await request.post(`/visitor/cancel/${row.id}`)
      ElMessage.success('取消成功')
      fetchVisitorList()
    } catch (error) {
      console.error('取消失败:', error)
      ElMessage.error('取消失败')
    }
  })
}

// 获取状态类型
const getStatusType = (status) => {
  const typeMap = {
    1: 'success',
    2: 'info'
  }
  return typeMap[status] || 'info'
}

// 获取状态文本
const getStatusText = (status) => {
  const textMap = {
    1: '在楼内',
    2: '已离开'
  }
  return textMap[status] || '未知'
}

// 返回后台管理
const goToAdmin = () => {
  router.push('/dashboard')
}

// 返回首页
const goToHome = () => {
  router.push('/front')
}

onMounted(() => {
  // 只有学生才获取数据
  if (isStudent.value) {
    fetchVisitorList()
  }
})
</script>

<style scoped>
.visitor-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;
}

.page-header {
  margin-bottom: 32px;
}

.page-title {
  font-size: 32px;
  font-weight: 700;
  color: #1f2937;
  margin: 0 0 8px 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  animation: fadeInDown 0.6s ease-out;
}

.page-subtitle {
  font-size: 16px;
  color: #6b7280;
  margin: 0;
  animation: fadeInUp 0.6s ease-out 0.2s both;
}

@keyframes fadeInDown {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
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

.card-header {
  display: flex;
  align-items: center;
  gap: 16px;
}

.header-icon {
  width: 48px;
  height: 48px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: white;
  backdrop-filter: blur(10px);
  transition: all 0.3s;
}

.header-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.header-title {
  font-size: 18px;
  font-weight: 700;
  color: white;
}

.header-subtitle {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.8);
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.modern-card {
  margin-bottom: 24px;
  border-radius: 16px;
  overflow: hidden;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid #e5e7eb;
  position: relative;
}

.modern-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
  opacity: 0;
  transition: opacity 0.3s;
}

.modern-card:hover::before {
  opacity: 1;
}

.modern-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 28px rgba(102, 126, 234, 0.15);
}

.modern-card :deep(.el-card__header) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px 24px;
  border-bottom: none;
}

.form-card,
.record-card,
.list-card {
  margin-bottom: 24px;
  border-radius: 16px;
  border: none;
}

/* 访客记录 */
.record-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
  max-height: 400px;
  overflow-y: auto;
}

.record-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  background: #f9fafb;
  border-radius: 12px;
  transition: all 0.3s;
}

.record-item:hover {
  background: #f3f4f6;
}

.visitor-avatar {
  width: 48px;
  height: 48px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 24px;
}

.visitor-info {
  flex: 1;
}

.visitor-name {
  font-size: 15px;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 4px;
}

.visitor-phone,
.visitor-time {
  font-size: 13px;
  color: #6b7280;
  margin: 2px 0;
}

/* 响应式 */
@media (max-width: 768px) {
  .visitor-container {
    padding: 16px;
  }
}
</style>
