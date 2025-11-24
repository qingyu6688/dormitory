<template>
  <div class="bill-container">
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
        <h1 class="page-title">费用查询</h1>
        <p class="page-subtitle">查看水电费账单，在线缴费</p>
      </div>

      <!-- 费用统计卡片 -->
    <el-row :gutter="24" class="stats-row">
      <el-col :span="8">
        <el-card class="stat-card modern-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon water">
              <el-icon><MostlyCloudy /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-label">本月水费</div>
              <div class="stat-value">¥{{ waterBill.toFixed(2) }}</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card class="stat-card modern-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon electric">
              <el-icon><Sunny /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-label">本月电费</div>
              <div class="stat-value">¥{{ electricBill.toFixed(2) }}</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card class="stat-card modern-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon total">
              <el-icon><Wallet /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-label">本月合计</div>
              <div class="stat-value total-value">¥{{ totalBill.toFixed(2) }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 账单列表 -->
    <el-card class="list-card modern-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <div class="header-icon">
            <el-icon><Document /></el-icon>
          </div>
          <div class="header-content">
            <span class="header-title">账单记录</span>
            <span class="header-subtitle">Utility Bills</span>
          </div>
          <div class="header-right">
            <el-date-picker
              v-model="dateRange"
              type="monthrange"
              range-separator="至"
              start-placeholder="开始月份"
              end-placeholder="结束月份"
              @change="fetchBillList"
              style="width: 300px"
            />
          </div>
        </div>
      </template>

      <el-table :data="tableData" style="width: 100%">
        <el-table-column prop="billMonth" label="账单月份" width="120" />
        <el-table-column prop="waterUsage" label="用水量(吨)" width="120">
          <template #default="{ row }">
            {{ row.waterUsage || 0 }}
          </template>
        </el-table-column>
        <el-table-column prop="waterAmount" label="水费(元)" width="120">
          <template #default="{ row }">
            ¥{{ (row.waterAmount || 0).toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="electricUsage" label="用电量(度)" width="120">
          <template #default="{ row }">
            {{ row.electricUsage || 0 }}
          </template>
        </el-table-column>
        <el-table-column prop="electricAmount" label="电费(元)" width="120">
          <template #default="{ row }">
            ¥{{ (row.electricAmount || 0).toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="actualAmount" label="应缴(元)" width="120">
          <template #default="{ row }">
            <span class="total-fee">¥{{ (row.actualAmount || 0).toFixed(2) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="paymentStatus" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.paymentStatus)">
              {{ getStatusText(row.paymentStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="meterReadingTime" label="抄表时间" width="180">
          <template #default="{ row }">
            {{ formatTime(row.meterReadingTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handlePay(row)" v-if="row.paymentStatus === 1">
              立即缴费
            </el-button>
            <el-button link type="success" disabled v-else>
              已缴费
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.size"
        :total="pagination.total"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="fetchBillList"
        @current-change="fetchBillList"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>

    <!-- 缴费对话框 -->
    <el-dialog v-model="payDialogVisible" title="在线缴费" width="500px">
      <div class="pay-content">
        <div class="pay-info">
          <div class="pay-item">
            <span class="pay-label">账单月份：</span>
            <span class="pay-value">{{ currentBill?.billMonth }}</span>
          </div>
          <div class="pay-item">
            <span class="pay-label">水费：</span>
            <span class="pay-value">¥{{ (currentBill?.waterAmount || 0).toFixed(2) }}</span>
          </div>
          <div class="pay-item">
            <span class="pay-label">电费：</span>
            <span class="pay-value">¥{{ (currentBill?.electricAmount || 0).toFixed(2) }}</span>
          </div>
          <div class="pay-item total">
            <span class="pay-label">应缴金额：</span>
            <span class="pay-value">¥{{ (currentBill?.actualAmount || 0).toFixed(2) }}</span>
          </div>
        </div>
        <el-alert title="提示：请确认账单信息无误后进行缴费" type="info" :closable="false" />
      </div>
      <template #footer>
        <el-button @click="payDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmPay" :loading="payLoading">
          确认缴费
        </el-button>
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
  Wallet, Document, Right, HomeFilled, MostlyCloudy, Sunny
} from '@element-plus/icons-vue'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

// 检查是否为学生角色（roleId = 4）
const isStudent = computed(() => userStore.userInfo?.roleId === 4)

const dateRange = ref([])
const waterBill = ref(0)
const electricBill = ref(0)
const totalBill = computed(() => waterBill.value + electricBill.value)

const tableData = ref([])
const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const payDialogVisible = ref(false)
const payLoading = ref(false)
const currentBill = ref(null)

// 获取账单列表
const fetchBillList = async () => {
  try {
    const res = await request.get('/utilitybill/my-bills')
    tableData.value = res.data || []

    // 计算本月费用
    const currentMonth = new Date().toISOString().slice(0, 7)
    const currentMonthBill = tableData.value.find(item => item.billMonth === currentMonth)
    if (currentMonthBill) {
      waterBill.value = currentMonthBill.waterAmount || 0
      electricBill.value = currentMonthBill.electricAmount || 0
    } else {
      waterBill.value = 0
      electricBill.value = 0
    }
  } catch (error) {
    console.error('获取账单列表失败:', error)
    ElMessage.error('获取账单列表失败')
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

// 缴费
const handlePay = (row) => {
  currentBill.value = row
  payDialogVisible.value = true
}

// 确认缴费
const confirmPay = async () => {
  payLoading.value = true
  try {
    await request.post(`/utilitybill/pay/${currentBill.value.id}`, null, {
      params: { paymentMethod: '在线支付' }
    })
    ElMessage.success('缴费成功')
    payDialogVisible.value = false
    await fetchBillList()
  } catch (error) {
    console.error('缴费失败:', error)
    ElMessage.error(error.response?.data?.message || '缴费失败')
  } finally {
    payLoading.value = false
  }
}

// 查看详情
const handleView = (row) => {
  ElMessage.info('查看详情功能开发中')
}

// 获取状态类型
const getStatusType = (status) => {
  const typeMap = {
    1: 'warning',
    2: 'success',
    3: 'danger'
  }
  return typeMap[status] || 'info'
}

// 获取状态文本
const getStatusText = (status) => {
  const textMap = {
    1: '未缴',
    2: '已缴',
    3: '逾期'
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
    fetchBillList()
  }
})
</script>

<style scoped>
.bill-container {
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

/* 统计卡片 */
.stats-row {
  margin-bottom: 24px;
}

.stat-card {
  border-radius: 16px;
  border: none;
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 8px 0;
}

.stat-icon {
  width: 64px;
  height: 64px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32px;
  color: white;
}

.stat-icon.water {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-icon.electric {
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
}

.stat-icon.total {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-info {
  flex: 1;
}

.stat-label {
  font-size: 14px;
  color: #6b7280;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #1f2937;
}

.stat-value.total-value {
  color: #667eea;
}

/* 列表卡片 */
.list-card {
  border-radius: 16px;
  border: none;
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

.card-header {
  display: flex;
  justify-content: space-between;
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

.header-left {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
  color: #1f2937;
}

.total-fee {
  font-weight: 600;
  color: #667eea;
}

/* 缴费对话框 */
.pay-content {
  padding: 20px 0;
}

.pay-info {
  background: #f9fafb;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 20px;
}

.pay-item {
  display: flex;
  justify-content: space-between;
  padding: 12px 0;
  border-bottom: 1px solid #e5e7eb;
}

.pay-item:last-child {
  border-bottom: none;
}

.pay-item.total {
  padding-top: 16px;
  margin-top: 8px;
  border-top: 2px solid #e5e7eb;
}

.pay-label {
  font-size: 15px;
  color: #6b7280;
}

.pay-value {
  font-size: 16px;
  font-weight: 600;
  color: #1f2937;
}

.pay-item.total .pay-value {
  font-size: 20px;
  color: #667eea;
}

/* 响应式 */
@media (max-width: 768px) {
  .bill-container {
    padding: 16px;
  }

  .stats-row {
    flex-direction: column;
  }
}
</style>
