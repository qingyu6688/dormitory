<template>
  <div class="repair-container">
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
        <h1 class="page-title">在线报修</h1>
        <p class="page-subtitle">快速提交报修申请，实时跟踪维修进度</p>
      </div>

      <el-row :gutter="24">
      <!-- 报修表单 -->
      <el-col :span="16">
        <el-card class="form-card modern-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <div class="header-icon">
                <el-icon><Edit /></el-icon>
              </div>
              <div class="header-content">
                <span class="header-title">提交报修</span>
                <span class="header-subtitle">Submit Repair Request</span>
              </div>
            </div>
          </template>

          <el-form :model="formData" :rules="rules" ref="formRef" label-width="100px">
            <el-form-item label="报修类型" prop="repairType">
              <el-select v-model="formData.repairType" placeholder="请选择报修类型" style="width: 100%">
                <el-option
                  v-for="type in repairTypes"
                  :key="type"
                  :label="type"
                  :value="type"
                />
              </el-select>
            </el-form-item>

            <el-form-item label="紧急程度" prop="urgencyLevel">
              <el-radio-group v-model="formData.urgencyLevel">
                <el-radio :label="1">普通</el-radio>
                <el-radio :label="2">紧急</el-radio>
                <el-radio :label="3">特急</el-radio>
              </el-radio-group>
            </el-form-item>

            <el-form-item label="问题描述" prop="description">
              <el-input
                v-model="formData.description"
                type="textarea"
                :rows="6"
                placeholder="请详细描述问题，包括具体位置、故障现象等"
                maxlength="500"
                show-word-limit
              />
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="handleSubmit" :loading="loading">
                <el-icon><Check /></el-icon>
                提交报修
              </el-button>
              <el-button @click="handleReset">
                <el-icon><Refresh /></el-icon>
                重置
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>

      <!-- 报修记录 -->
      <el-col :span="8">
        <el-card class="record-card modern-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <div class="header-icon">
                <el-icon><List /></el-icon>
              </div>
              <div class="header-content">
                <span class="header-title">我的报修记录</span>
                <span class="header-subtitle">Recent Repairs</span>
              </div>
            </div>
          </template>

          <div class="record-list">
            <div v-for="item in repairList" :key="item.id" class="record-item">
              <div class="record-header">
                <span class="record-type-tag">{{ item.repairType }}</span>
                <el-tag :type="getStatusType(item.status)" size="small">
                  {{ getStatusText(item.status) }}
                </el-tag>
              </div>
              <div class="record-desc">{{ item.description }}</div>
              <div class="record-footer">
                <span class="record-room">房间：{{ item.roomNo }}</span>
                <span class="record-time">{{ formatTime(item.reportTime) }}</span>
              </div>
            </div>

            <el-empty v-if="repairList.length === 0" description="暂无报修记录" />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 报修列表 -->
    <el-card class="list-card modern-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <div class="header-icon">
            <el-icon><Document /></el-icon>
          </div>
          <div class="header-content">
            <span class="header-title">全部报修记录</span>
            <span class="header-subtitle">All Repair Records</span>
          </div>
        </div>
      </template>

      <el-table :data="tableData" style="width: 100%">
        <el-table-column prop="repairType" label="报修类型" width="120" />
        <el-table-column prop="description" label="问题描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="urgencyLevel" label="紧急程度" width="100">
          <template #default="{ row }">
            <el-tag :type="getUrgencyType(row.urgencyLevel)" size="small">
              {{ getUrgencyText(row.urgencyLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="roomNo" label="房间号" width="100" />
        <el-table-column prop="reportTime" label="提交时间" width="180">
          <template #default="{ row }">
            {{ formatTime(row.reportTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleView(row)">
              查看详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 详情对话框 -->
    <el-dialog v-model="showDetailDialog" title="报修详情" width="700px">
      <div v-if="currentRepair" class="detail-container">
        <!-- 基本信息 -->
        <div class="detail-section">
          <h3 class="section-title">基本信息</h3>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="报修类型">
              <el-tag>{{ currentRepair.repairType }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="紧急程度">
              <el-tag :type="getUrgencyType(currentRepair.urgencyLevel)">
                {{ getUrgencyText(currentRepair.urgencyLevel) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="房间号">
              {{ currentRepair.roomNo }}
            </el-descriptions-item>
            <el-descriptions-item label="状态">
              <el-tag :type="getStatusType(currentRepair.status)">
                {{ getStatusText(currentRepair.status) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="报修时间" :span="2">
              {{ formatTime(currentRepair.reportTime) }}
            </el-descriptions-item>
            <el-descriptions-item label="问题描述" :span="2">
              <div class="description-text">{{ currentRepair.description }}</div>
            </el-descriptions-item>
          </el-descriptions>
        </div>

        <!-- 处理信息 -->
        <div v-if="currentRepair.status >= 2" class="detail-section">
          <h3 class="section-title">处理信息</h3>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="维修人员" v-if="currentRepair.assignedToName">
              {{ currentRepair.assignedToName }}
            </el-descriptions-item>
            <el-descriptions-item label="预约时间" v-if="currentRepair.appointmentTime">
              {{ formatTime(currentRepair.appointmentTime) }}
            </el-descriptions-item>
            <el-descriptions-item label="处理时间" v-if="currentRepair.processTime">
              {{ formatTime(currentRepair.processTime) }}
            </el-descriptions-item>
            <el-descriptions-item label="完成时间" v-if="currentRepair.completeTime">
              {{ formatTime(currentRepair.completeTime) }}
            </el-descriptions-item>
            <el-descriptions-item label="处理结果" :span="2" v-if="currentRepair.resultDesc">
              <div class="description-text">{{ currentRepair.resultDesc }}</div>
            </el-descriptions-item>
          </el-descriptions>
        </div>

        <!-- 费用信息 -->
        <div v-if="currentRepair.repairCost" class="detail-section">
          <h3 class="section-title">费用信息</h3>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="维修费用">
              ¥{{ currentRepair.repairCost }}
            </el-descriptions-item>
            <el-descriptions-item label="费用承担">
              <el-tag :type="currentRepair.costBearer === 1 ? 'success' : 'warning'">
                {{ currentRepair.costBearer === 1 ? '学校' : '学生' }}
              </el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </div>

        <!-- 评价信息 -->
        <div v-if="currentRepair.status === 3 && !currentRepair.satisfaction" class="detail-section">
          <h3 class="section-title">服务评价</h3>
          <el-form :model="evaluationForm" label-width="100px">
            <el-form-item label="满意度">
              <el-rate v-model="evaluationForm.satisfaction" />
            </el-form-item>
            <el-form-item label="评价内容">
              <el-input
                v-model="evaluationForm.comment"
                type="textarea"
                :rows="3"
                placeholder="请输入您的评价"
                maxlength="200"
                show-word-limit
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleEvaluate">提交评价</el-button>
            </el-form-item>
          </el-form>
        </div>

        <!-- 已评价 -->
        <div v-if="currentRepair.satisfaction" class="detail-section">
          <h3 class="section-title">我的评价</h3>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="满意度">
              <el-rate v-model="currentRepair.satisfaction" disabled />
            </el-descriptions-item>
            <el-descriptions-item label="评价内容" v-if="currentRepair.comment">
              <div class="description-text">{{ currentRepair.comment }}</div>
            </el-descriptions-item>
          </el-descriptions>
        </div>
      </div>
      <template #footer>
        <el-button @click="showDetailDialog = false">关闭</el-button>
      </template>
    </el-dialog>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import {
  Edit, Check, Refresh, List, Document, Right, HomeFilled
} from '@element-plus/icons-vue'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref(null)
const loading = ref(false)

// 检查是否为学生角色（roleId = 4）
const isStudent = computed(() => userStore.userInfo?.roleId === 4)

const formData = reactive({
  repairType: '',
  description: '',
  urgencyLevel: 1
})

const repairTypes = ref([])

const rules = {
  repairType: [{ required: true, message: '请选择报修类型', trigger: 'change' }],
  description: [
    { required: true, message: '请输入问题描述', trigger: 'blur' },
    { min: 10, message: '问题描述至少10个字符', trigger: 'blur' }
  ],
  urgencyLevel: [{ required: true, message: '请选择紧急程度', trigger: 'change' }]
}

const repairList = ref([])
const tableData = ref([])
const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

// 详情对话框
const showDetailDialog = ref(false)
const currentRepair = ref(null)

// 评价表单
const evaluationForm = reactive({
  satisfaction: 5,
  comment: ''
})

// 提交报修
const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await request.post('/repair/submit', formData)
        ElMessage.success('报修提交成功，我们会尽快处理')
        handleReset()
        await fetchRepairList()
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

// 获取报修列表
const fetchRepairList = async () => {
  try {
    const res = await request.get('/repair/my-list')
    tableData.value = res.data || []
    
    // 最近3条记录
    repairList.value = tableData.value.slice(0, 3)
  } catch (error) {
    console.error('获取报修列表失败:', error)
    ElMessage.error('获取报修列表失败')
  }
}

// 获取报修类型
const fetchRepairTypes = async () => {
  try {
    const res = await request.get('/repair/types')
    repairTypes.value = res.data || []
  } catch (error) {
    console.error('获取报修类型失败:', error)
  }
}

// 查看详情
const handleView = async (row) => {
  try {
    const res = await request.get(`/repair/detail/${row.id}`)
    currentRepair.value = res.data
    showDetailDialog.value = true
    
    // 重置评价表单
    evaluationForm.satisfaction = 5
    evaluationForm.comment = ''
  } catch (error) {
    console.error('获取详情失败:', error)
    ElMessage.error('获取详情失败')
  }
}

// 提交评价
const handleEvaluate = async () => {
  if (!evaluationForm.satisfaction) {
    ElMessage.warning('请选择满意度')
    return
  }
  
  try {
    await request.put('/repair', {
      id: currentRepair.value.id,
      satisfaction: evaluationForm.satisfaction,
      comment: evaluationForm.comment
    })
    ElMessage.success('评价提交成功')
    showDetailDialog.value = false
    await fetchRepairList()
  } catch (error) {
    console.error('提交评价失败:', error)
    ElMessage.error('提交评价失败')
  }
}

// 获取状态类型
const getStatusType = (status) => {
  const typeMap = {
    1: 'warning',
    2: 'primary',
    3: 'success'
  }
  return typeMap[status] || 'info'
}

// 获取状态文本
const getStatusText = (status) => {
  const textMap = {
    1: '待处理',
    2: '处理中',
    3: '已完成'
  }
  return textMap[status] || '未知'
}

// 获取紧急程度类型
const getUrgencyType = (level) => {
  const typeMap = {
    1: 'info',
    2: 'warning',
    3: 'danger'
  }
  return typeMap[level] || 'info'
}

// 获取紧急程度文本
const getUrgencyText = (level) => {
  const textMap = {
    1: '普通',
    2: '紧急',
    3: '特急'
  }
  return textMap[level] || '未知'
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
    fetchRepairTypes()
    fetchRepairList()
  }
})
</script>

<style scoped>
.repair-container {
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

/* 报修记录 */
.record-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
  max-height: 400px;
  overflow-y: auto;
}

.record-item {
  padding: 16px;
  background: #f9fafb;
  border-radius: 12px;
  transition: all 0.3s;
  cursor: pointer;
}

.record-item:hover {
  background: #f3f4f6;
  transform: translateX(4px);
}

.record-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.record-type-tag {
  font-size: 14px;
  font-weight: 600;
  color: #667eea;
  background: rgba(102, 126, 234, 0.1);
  padding: 4px 12px;
  border-radius: 6px;
}

.record-desc {
  font-size: 14px;
  color: #4b5563;
  margin: 8px 0;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
}

.record-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 8px;
  padding-top: 8px;
  border-top: 1px solid #e5e7eb;
}

.record-room {
  font-size: 13px;
  color: #6b7280;
}

.record-time {
  font-size: 12px;
  color: #9ca3af;
}

/* 详情对话框 */
.detail-container {
  max-height: 70vh;
  overflow-y: auto;
}

.detail-section {
  margin-bottom: 24px;
}

.detail-section:last-child {
  margin-bottom: 0;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 16px 0;
  padding-bottom: 8px;
  border-bottom: 2px solid #667eea;
  display: flex;
  align-items: center;
  gap: 8px;
}

.section-title::before {
  content: '';
  width: 4px;
  height: 16px;
  background: linear-gradient(180deg, #667eea 0%, #764ba2 100%);
  border-radius: 2px;
}

.description-text {
  line-height: 1.6;
  color: #4b5563;
  white-space: pre-wrap;
  word-break: break-word;
}

.detail-container :deep(.el-descriptions__label) {
  font-weight: 600;
  background: #f9fafb;
}

.detail-container :deep(.el-rate) {
  display: inline-flex;
}

/* 响应式 */
@media (max-width: 768px) {
  .repair-container {
    padding: 16px;
  }
  
  .detail-container :deep(.el-descriptions) {
    font-size: 14px;
  }
}
</style>
