<template>
  <div class="page-container">
    <div class="page-header flex-between">
      <h1>宿舍运营统计</h1>
      <el-button type="primary" @click="loadStats" :loading="loading">
        <el-icon><Refresh /></el-icon>
        刷新数据
      </el-button>
    </div>

    <el-row :gutter="16" class="summary-row">
      <el-col v-for="card in summaryCards" :key="card.label" :xs="12" :sm="12" :md="6" :lg="6">
        <el-card shadow="hover" class="summary-card">
          <div class="summary-card__label">{{ card.label }}</div>
          <div class="summary-card__value">{{ card.value }}</div>
          <div class="summary-card__desc">{{ card.desc }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" style="margin-top: 16px">
      <el-col :xs="24" :md="12">
        <el-card shadow="hover" class="stat-card">
          <template #header>
            <div class="card-header">
              <span>安全与卫生</span>
            </div>
          </template>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="平均卫生分">{{ stats.hygieneAvgScore }}</el-descriptions-item>
            <el-descriptions-item label="待复查宿舍">{{ stats.pendingRechecks }}</el-descriptions-item>
            <el-descriptions-item label="未处理违纪">{{ stats.pendingViolations }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
      <el-col :xs="24" :md="12">
        <el-card shadow="hover" class="stat-card">
          <template #header>
            <div class="card-header">
              <span>公告概况</span>
            </div>
          </template>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="已发布公告">{{ stats.noticeCount }}</el-descriptions-item>
            <el-descriptions-item label="置顶公告">{{ stats.topNoticeCount }}</el-descriptions-item>
            <el-descriptions-item label="需确认公告">{{ stats.needConfirmCount }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" style="margin-top: 16px">
      <el-col :xs="24" :md="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>最新卫生检查</span>
            </div>
          </template>
          <el-table :data="latestHygiene" max-height="320">
            <el-table-column prop="checkDate" label="日期" width="120" />
            <el-table-column prop="roomId" label="宿舍ID" width="100" />
            <el-table-column prop="score" label="得分" width="80" />
            <el-table-column prop="grade" label="等级" width="100" />
            <el-table-column prop="recheckStatus" label="复查" width="120">
              <template #default="{ row }">
                <el-tag :type="recheckTag(row.recheckStatus)">
                  {{ recheckText(row.recheckStatus) }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :xs="24" :md="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>待处理报修</span>
            </div>
          </template>
          <el-table :data="pendingRepairsList" max-height="320">
            <el-table-column prop="reportTime" label="报修时间" width="150" />
            <el-table-column prop="roomId" label="宿舍ID" width="100" />
            <el-table-column prop="repairType" label="类型" width="120" />
            <el-table-column prop="description" label="描述" show-overflow-tooltip />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Refresh } from '@element-plus/icons-vue'
import {
  getBuildingList,
  getRoomList,
  getCheckInList,
  getRepairList,
  getHygieneList,
  getViolationList,
  getNoticeList
} from '@/api/dormitory'

const loading = ref(false)
const stats = ref({
  buildingCount: 0,
  roomCount: 0,
  totalBeds: 0,
  occupiedBeds: 0,
  availableBeds: 0,
  activeResidents: 0,
  pendingRepairs: 0,
  hygieneAvgScore: 0,
  pendingRechecks: 0,
  pendingViolations: 0,
  noticeCount: 0,
  topNoticeCount: 0,
  needConfirmCount: 0
})

const latestHygiene = ref([])
const pendingRepairsList = ref([])

const summaryCards = computed(() => [
  {
    label: '楼栋数量',
    value: stats.value.buildingCount,
    desc: '当前启用的宿舍楼'
  },
  {
    label: '宿舍数量',
    value: stats.value.roomCount,
    desc: `床位 ${stats.value.totalBeds} 个`
  },
  {
    label: '在住学生',
    value: stats.value.activeResidents,
    desc: `空余床位 ${stats.value.availableBeds}`
  },
  {
    label: '待处理报修',
    value: stats.value.pendingRepairs,
    desc: '含紧急报修'
  }
])

const recheckText = (val) => {
  switch (val) {
    case 1: return '无需复查'
    case 2: return '待复查'
    case 3: return '已复查'
    default: return '-'
  }
}

const recheckTag = (val) => {
  switch (val) {
    case 1: return 'info'
    case 2: return 'warning'
    case 3: return 'success'
    default: return 'info'
  }
}

const loadStats = async () => {
  loading.value = true
  try {
    const [
      buildingRes,
      roomRes,
      checkInRes,
      repairRes,
      hygieneRes,
      violationRes,
      noticeRes
    ] = await Promise.all([
      getBuildingList(),
      getRoomList(),
      getCheckInList(),
      getRepairList(),
      getHygieneList(),
      getViolationList(),
      getNoticeList()
    ])

    const buildings = buildingRes.data || []
    const rooms = roomRes.data || []
    const checkIns = checkInRes.data || []
    const repairs = repairRes.data || []
    const hygienics = hygieneRes.data || []
    const violations = violationRes.data || []
    const notices = noticeRes.data || []

    const totalBeds = rooms.reduce((sum, room) => sum + (room.bedCount || 0), 0)
    const occupiedBeds = rooms.reduce((sum, room) => sum + (room.occupiedCount || 0), 0)
    const activeResidents = checkIns.filter(item => item.status === 1).length
    const pendingRepairs = repairs.filter(item => item.status !== 3).length
    const hygieneAvgScore =
      hygienics.length > 0
        ? (hygienics.reduce((sum, item) => sum + (item.score || 0), 0) / hygienics.length).toFixed(1)
        : 0
    const pendingRechecks = hygienics.filter(item => item.recheckStatus === 2).length
    const pendingViolations = violations.filter(item => item.handleStatus !== 2).length
    const noticeCount = notices.length
    const topNoticeCount = notices.filter(item => item.isTop === 1).length
    const needConfirmCount = notices.filter(item => item.needConfirm === 1).length

    stats.value = {
      buildingCount: buildings.length,
      roomCount: rooms.length,
      totalBeds,
      occupiedBeds,
      availableBeds: Math.max(totalBeds - occupiedBeds, 0),
      activeResidents,
      pendingRepairs,
      hygieneAvgScore,
      pendingRechecks,
      pendingViolations,
      noticeCount,
      topNoticeCount,
      needConfirmCount
    }

    latestHygiene.value = hygienics.sort((a, b) => (b.checkDate || '').localeCompare(a.checkDate || '')).slice(0, 5)
    pendingRepairsList.value = repairs
      .filter(item => item.status !== 3)
      .sort((a, b) => (b.reportTime || '').localeCompare(a.reportTime || ''))
      .slice(0, 5)
  } catch (error) {
    console.error('加载统计失败', error)
    ElMessage.error('加载统计数据失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadStats()
})
</script>

<style scoped>
.summary-card {
  border-radius: 12px;
}

.summary-card__label {
  color: #6b7280;
  font-size: 14px;
}

.summary-card__value {
  font-size: 28px;
  font-weight: 600;
  color: #111827;
  margin: 4px 0;
}

.summary-card__desc {
  color: #9ca3af;
  font-size: 13px;
}

.stat-card {
  min-height: 180px;
}
</style>

