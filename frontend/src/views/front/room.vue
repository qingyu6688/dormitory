<template>
  <div class="room-container">
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
      <!-- 未分配宿舍提示 -->
      <el-result
        v-if="!roomInfo"
        icon="info"
        title="暂未分配宿舍"
        sub-title="您还没有分配宿舍，请先完善个人信息并选择宿舍"
      >
        <template #extra>
          <el-button type="primary" @click="goToProfile">
            <el-icon><User /></el-icon>
            完善个人信息
          </el-button>
          <el-button @click="handleSelectRoom">
            <el-icon><House /></el-icon>
            选择宿舍
          </el-button>
        </template>
      </el-result>

      <!-- 已分配宿舍 -->
      <div v-else>
        <div class="page-header">
          <h1 class="page-title">我的宿舍</h1>
          <p class="page-subtitle">查看宿舍信息、室友和卫生评分</p>
        </div>

        <el-row :gutter="24">
      <!-- 宿舍信息卡片 -->
      <el-col :span="16">
        <el-card class="room-card modern-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <div class="header-icon">
                <el-icon><House /></el-icon>
              </div>
              <div class="header-content">
                <span class="header-title">宿舍信息</span>
                <span class="header-subtitle">Dormitory Information</span>
              </div>
            </div>
          </template>
          
          <div v-if="roomInfo" class="room-info">
            <div class="info-grid">
              <div class="info-item modern-info-item">
                <div class="info-icon">
                  <el-icon><HomeFilled /></el-icon>
                </div>
                <div class="info-content">
                  <span class="label">楼栋</span>
                  <span class="value">{{ roomInfo.buildingName }}</span>
                </div>
              </div>
              <div class="info-item modern-info-item">
                <div class="info-icon">
                  <el-icon><Location /></el-icon>
                </div>
                <div class="info-content">
                  <span class="label">楼栋编号</span>
                  <span class="value">{{ roomInfo.buildingNo }}</span>
                </div>
              </div>
              <div class="info-item modern-info-item">
                <div class="info-icon">
                  <el-icon><House /></el-icon>
                </div>
                <div class="info-content">
                  <span class="label">房间号</span>
                  <span class="value">{{ roomInfo.roomNo }}</span>
                </div>
              </div>
              <div class="info-item">
                <span class="label">楼层</span>
                <span class="value">{{ roomInfo.floor }}层</span>
              </div>
              <div class="info-item">
                <span class="label">我的床位</span>
                <span class="value">{{ roomInfo.bedNo }}号床</span>
              </div>
              <div class="info-item">
                <span class="label">房间类型</span>
                <span class="value">{{ roomInfo.roomType || '标准间' }}</span>
              </div>
              <div class="info-item">
                <span class="label">朝向</span>
                <span class="value">{{ roomInfo.direction || '-' }}</span>
              </div>
              <div class="info-item">
                <span class="label">入住日期</span>
                <span class="value">{{ roomInfo.checkInDate }}</span>
              </div>
              <div class="info-item">
                <span class="label">床位数</span>
                <span class="value">{{ roomInfo.capacity }}人间</span>
              </div>
              <div class="info-item">
                <span class="label">已入住</span>
                <span class="value">{{ roomInfo.currentCount }}人</span>
              </div>
              <div class="info-item">
                <span class="label">房间状态</span>
                <span class="value">
                  <el-tag :type="getRoomStatusType(roomInfo.roomStatus)">
                    {{ getRoomStatusText(roomInfo.roomStatus) }}
                  </el-tag>
                </span>
              </div>
            </div>

            <!-- 室友列表 -->
            <div class="roommates-section">
              <h3 class="section-title">
                <el-icon><UserFilled /></el-icon>
                室友信息
              </h3>
              <div class="roommates-grid">
                <div v-for="mate in roommates" :key="mate.id" class="roommate-card">
                  <el-avatar :size="60" :src="mate.avatar">
                    <el-icon><User /></el-icon>
                  </el-avatar>
                  <div class="roommate-info">
                    <div class="roommate-name">
                      {{ mate.nickname }}
                      <el-tag v-if="mate.id === userStore.userInfo?.id" type="success" size="small" effect="dark">
                        我
                      </el-tag>
                      <el-tag v-else type="info" size="small">
                        室友
                      </el-tag>
                      <el-tag size="small" :type="mate.gender === 1 ? 'primary' : 'danger'">
                        {{ mate.gender === 1 ? '男' : '女' }}
                      </el-tag>
                    </div>
                    <div class="roommate-detail">
                      <el-icon><Postcard /></el-icon>
                      学号：{{ mate.studentNo }}
                    </div>
                    <div class="roommate-detail">
                      <el-icon><School /></el-icon>
                      {{ mate.major }} {{ mate.className }}
                    </div>
                    <div class="roommate-detail">
                      <el-icon><House /></el-icon>
                      床位：{{ mate.bedNo }}号床
                    </div>
                    <div class="roommate-detail" v-if="mate.phone">
                      <el-icon><Phone /></el-icon>
                      {{ mate.phone }}
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <el-empty v-else description="暂无宿舍信息" />
        </el-card>
      </el-col>

      <!-- 侧边栏 -->
      <el-col :span="8">
        <!-- 卫生评分 -->
        <el-card class="score-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <el-icon><Medal /></el-icon>
              <span>本月卫生评分</span>
            </div>
          </template>
          <div class="score-content">
            <div class="score-circle">
              <el-progress
                type="circle"
                :percentage="hygieneScore"
                :width="120"
                :stroke-width="10"
                :color="getScoreColor(hygieneScore)"
              >
                <template #default="{ percentage }">
                  <span class="score-text">{{ percentage }}</span>
                  <span class="score-label">分</span>
                </template>
              </el-progress>
            </div>
            <div class="score-desc">
              <el-tag :type="getScoreType(hygieneScore)" size="large">
                {{ getScoreLevel(hygieneScore) }}
              </el-tag>
            </div>
          </div>
        </el-card>

        <!-- 快捷操作 -->
        <el-card class="action-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <el-icon><Grid /></el-icon>
              <span>快捷操作</span>
            </div>
          </template>
          <div class="action-list">
            <div class="action-btn-wrapper" @click="goToRepair">
              <div class="action-btn-icon">
                <el-icon><Tools /></el-icon>
              </div>
              <div class="action-btn-content">
                <div class="action-btn-title">在线报修</div>
                <div class="action-btn-desc">Online Repair</div>
              </div>
              <el-icon class="action-btn-arrow"><Right /></el-icon>
            </div>
            <div class="action-btn-wrapper" @click="goToVisitor">
              <div class="action-btn-icon">
                <el-icon><User /></el-icon>
              </div>
              <div class="action-btn-content">
                <div class="action-btn-title">访客登记</div>
                <div class="action-btn-desc">Visitor Registration</div>
              </div>
              <el-icon class="action-btn-arrow"><Right /></el-icon>
            </div>
            <div class="action-btn-wrapper" @click="goToBill">
              <div class="action-btn-icon">
                <el-icon><Wallet /></el-icon>
              </div>
              <div class="action-btn-content">
                <div class="action-btn-title">费用查询</div>
                <div class="action-btn-desc">Bill Inquiry</div>
              </div>
              <el-icon class="action-btn-arrow"><Right /></el-icon>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
      </div>
    </div>

    <!-- 选择宿舍对话框 -->
    <el-dialog v-model="showRoomSelectDialog" title="选择宿舍" width="600px" @close="handleDialogClose">
      <el-alert
        :title="`温馨提示 - 您是${userStore.userInfo?.gender === 1 ? '男生' : '女生'}`"
        type="info"
        :closable="false"
        style="margin-bottom: 20px;"
      >
        系统已根据您的性别自动筛选可选楼栋，{{ userStore.userInfo?.gender === 1 ? '男生' : '女生' }}只能选择{{ userStore.userInfo?.gender === 1 ? '男生' : '女生' }}宿舍楼栋
      </el-alert>
      <el-form :model="roomSelectForm" label-width="100px">
        <el-form-item label="选择楼栋">
          <el-select v-model="roomSelectForm.buildingId" placeholder="请选择楼栋" style="width: 100%" @change="handleBuildingChange">
            <el-option
              v-for="building in buildings"
              :key="building.id"
              :label="building.buildingName"
              :value="building.id"
            >
              <span style="float: left">{{ building.buildingName }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">
                {{ building.genderType === 1 ? '男生' : '女生' }}宿舍
              </span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="选择房间">
          <el-select v-model="roomSelectForm.roomId" placeholder="请先选择楼栋" style="width: 100%" :disabled="!roomSelectForm.buildingId" @change="handleRoomChange">
            <el-option
              v-for="room in availableRooms"
              :key="room.id"
              :label="`${room.roomNo} (${room.currentCount}/${room.capacity}人)`"
              :value="room.id"
              :disabled="room.currentCount >= room.capacity"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="选择床位">
          <el-select v-model="roomSelectForm.bedNo" placeholder="请先选择房间" style="width: 100%" :disabled="!roomSelectForm.roomId">
            <el-option
              v-for="bed in availableBeds"
              :key="bed"
              :label="`${bed}号床`"
              :value="bed"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showRoomSelectDialog = false">取消</el-button>
        <el-button type="primary" @click="handleRoomSelect" :loading="selectLoading">
          确认选择
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import {
  House, Location, User, UserFilled, Medal, Grid, Tools, Wallet, Right, HomeFilled, Postcard, School, Phone
} from '@element-plus/icons-vue'
import request from '@/utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

// 检查是否为学生角色（roleId = 4）
const isStudent = computed(() => userStore.userInfo?.roleId === 4)

const roomInfo = ref(null)
const roommates = ref([])
const hygieneScore = ref(85)

// 选择宿舍相关
const showRoomSelectDialog = ref(false)
const selectLoading = ref(false)
const roomSelectForm = ref({
  buildingId: null,
  roomId: null,
  bedNo: null
})
const buildings = ref([])
const availableRooms = ref([])
const availableBeds = ref([])

// 获取宿舍信息
const fetchRoomInfo = async () => {
  try {
    const res = await request.get('/room/my-room')
    console.log('获取宿舍信息响应:', res)
    
    if (res.data) {
      roomInfo.value = res.data
      console.log('宿舍信息:', roomInfo.value)
      
      // 获取室友信息
      if (roomInfo.value?.id) {
        await fetchRoommates(roomInfo.value.id)
      }
    } else {
      roomInfo.value = null // 未分配宿舍
      console.log('未分配宿舍')
    }
  } catch (error) {
    console.error('获取宿舍信息失败:', error)
    // 如果返回404或其他错误，说明未分配宿舍
    roomInfo.value = null
  }
}

// 获取室友信息
const fetchRoommates = async (roomId) => {
  try {
    const res = await request.get(`/room/${roomId}/roommates`)
    roommates.value = res.data || []
  } catch (error) {
    console.error('获取室友信息失败:', error)
    roommates.value = []
  }
}

// 获取卫生评分
const fetchHygieneScore = async () => {
  try {
    const res = await request.get('/hygiene/my-score')
    hygieneScore.value = res.data?.score || 85
  } catch (error) {
    console.error('获取卫生评分失败:', error)
    hygieneScore.value = 85
  }
}

// 获取评分颜色
const getScoreColor = (score) => {
  if (score >= 90) return '#67C23A'
  if (score >= 80) return '#409EFF'
  if (score >= 70) return '#E6A23C'
  return '#F56C6C'
}

// 获取房间状态文本
const getRoomStatusText = (status) => {
  const statusMap = {
    1: '空闲',
    2: '部分入住',
    3: '已满',
    4: '维修中'
  }
  return statusMap[status] || '未知'
}

// 获取房间状态类型
const getRoomStatusType = (status) => {
  const typeMap = {
    1: 'info',
    2: 'warning',
    3: 'success',
    4: 'danger'
  }
  return typeMap[status] || 'info'
}

// 获取评分类型
const getScoreType = (score) => {
  if (score >= 90) return 'success'
  if (score >= 80) return 'primary'
  if (score >= 70) return 'warning'
  return 'danger'
}

// 获取评分等级
const getScoreLevel = (score) => {
  if (score >= 90) return '优秀'
  if (score >= 80) return '良好'
  if (score >= 70) return '合格'
  return '需改进'
}

// 快捷操作
const goToRepair = () => {
  router.push('/front/repair')
}

const goToVisitor = () => {
  router.push('/front/visitor')
}

const goToBill = () => {
  router.push('/front/bill')
}

// 返回后台管理
const goToAdmin = () => {
  router.push('/dashboard')
}

// 返回首页
const goToHome = () => {
  router.push('/front')
}

// 前往个人中心
const goToProfile = () => {
  router.push('/front/profile')
}

// 处理选择宿舍按钮点击
const handleSelectRoom = () => {
  // 检查用户是否完善了个人信息
  const user = userStore.userInfo
  
  // 优先检查性别
  if (!user.gender) {
    ElMessageBox.alert(
      '选择宿舍前必须先完善性别信息，以便系统为您分配合适的宿舍楼栋（男生/女生宿舍）。',
      '请先完善性别信息',
      {
        confirmButtonText: '去完善',
        type: 'warning',
        callback: () => {
          goToProfile()
        }
      }
    )
    return
  }
  
  // 检查其他必填信息
  if (!user.studentNo || !user.college || !user.major || !user.className) {
    ElMessageBox.alert(
      '请先完善个人信息（学号、学院、专业、班级）后再选择宿舍。',
      '信息不完整',
      {
        confirmButtonText: '去完善',
        type: 'warning',
        callback: () => {
          goToProfile()
        }
      }
    )
    return
  }
  
  // 信息完善后，打开选择宿舍对话框
  showRoomSelectDialog.value = true
  fetchBuildings()
}

// 对话框关闭时重置表单
const handleDialogClose = () => {
  roomSelectForm.value = {
    buildingId: null,
    roomId: null,
    bedNo: null
  }
  availableRooms.value = []
  availableBeds.value = []
}

// 获取楼栋列表
const fetchBuildings = async () => {
  try {
    const res = await request.get('/building/list')
    const allBuildings = res.data || []
    
    // 根据用户性别过滤楼栋
    const user = userStore.userInfo
    if (user.gender) {
      // 只显示与用户性别匹配的楼栋
      buildings.value = allBuildings.filter(building => {
        // 如果楼栋没有设置性别类型，则显示
        if (!building.genderType) return true
        // 性别匹配才显示
        return building.genderType === user.gender
      })
      
      if (buildings.value.length === 0) {
        const genderText = user.gender === 1 ? '男生' : '女生'
        ElMessage.warning(`暂无可选的${genderText}宿舍楼栋`)
      }
    } else {
      buildings.value = allBuildings
    }
  } catch (error) {
    console.error('获取楼栋列表失败:', error)
    ElMessage.error('获取楼栋列表失败')
  }
}

// 楼栋变化时获取房间列表
const handleBuildingChange = async () => {
  roomSelectForm.value.roomId = null
  roomSelectForm.value.bedNo = null
  availableRooms.value = []
  availableBeds.value = []
  
  if (!roomSelectForm.value.buildingId) return
  
  try {
    const res = await request.get(`/room/available/${roomSelectForm.value.buildingId}`)
    availableRooms.value = res.data || []
  } catch (error) {
    console.error('获取房间列表失败:', error)
    ElMessage.error('获取房间列表失败')
  }
}

// 房间变化时获取可用床位
const handleRoomChange = async () => {
  roomSelectForm.value.bedNo = null
  availableBeds.value = []
  
  if (!roomSelectForm.value.roomId) return
  
  try {
    // 获取房间已占用的床位
    const res = await request.get(`/room/${roomSelectForm.value.roomId}/occupied-beds`)
    const occupiedBeds = res.data || []
    
    // 获取房间容量
    const selectedRoom = availableRooms.value.find(r => r.id === roomSelectForm.value.roomId)
    if (!selectedRoom) return
    
    // 生成可用床位列表（排除已占用的床位）
    availableBeds.value = Array.from(
      { length: selectedRoom.capacity },
      (_, i) => i + 1
    ).filter(bed => !occupiedBeds.includes(bed))
  } catch (error) {
    console.error('获取床位信息失败:', error)
    // 如果API失败，使用简单逻辑
    const selectedRoom = availableRooms.value.find(r => r.id === roomSelectForm.value.roomId)
    if (selectedRoom) {
      availableBeds.value = Array.from(
        { length: selectedRoom.capacity },
        (_, i) => i + 1
      ).filter(bed => bed > selectedRoom.currentCount)
    }
  }
}

// 确认选择宿舍
const handleRoomSelect = async () => {
  if (!roomSelectForm.value.buildingId || !roomSelectForm.value.roomId || !roomSelectForm.value.bedNo) {
    ElMessage.warning('请完整选择楼栋、房间和床位')
    return
  }
  
  selectLoading.value = true
  try {
    await request.post('/room/select', {
      roomId: roomSelectForm.value.roomId,
      bedNo: roomSelectForm.value.bedNo
    })
    
    ElMessage.success('宿舍选择成功')
    showRoomSelectDialog.value = false
    
    // 重置表单
    roomSelectForm.value = {
      buildingId: null,
      roomId: null,
      bedNo: null
    }
    
    // 刷新宿舍信息和用户信息
    await fetchRoomInfo()
    await userStore.fetchUserInfo()
  } catch (error) {
    console.error('选择宿舍失败:', error)
    ElMessage.error(error.response?.data?.message || '选择宿舍失败')
  } finally {
    selectLoading.value = false
  }
}

onMounted(() => {
  // 只有学生才获取数据
  if (isStudent.value) {
    fetchRoomInfo()
    fetchHygieneScore()
    // 不在这里加载楼栋列表，而是在点击选择宿舍时加载
  }
})
</script>

<style scoped>
.room-container {
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
  font-size: 16px;
  font-weight: 600;
  color: #1f2937;
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

.room-card,
.score-card,
.action-card {
  margin-bottom: 24px;
  border-radius: 16px;
  border: none;
}

/* 宿舍信息 */
.room-info {
  padding: 8px 0;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 24px;
  margin-bottom: 32px;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 16px;
  background: #f9fafb;
  border-radius: 12px;
  transition: all 0.3s;
}

.info-item:hover {
  background: #f3f4f6;
  transform: translateY(-2px);
}

.modern-info-item {
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 12px;
  padding: 16px;
  background: linear-gradient(135deg, #f9fafb 0%, #ffffff 100%);
  border-radius: 12px;
  border: 1px solid #e5e7eb;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
}

.modern-info-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 4px;
  height: 100%;
  background: linear-gradient(180deg, #667eea 0%, #764ba2 100%);
  opacity: 0;
  transition: opacity 0.3s;
}

.modern-info-item:hover::before {
  opacity: 1;
}

.modern-info-item:hover {
  background: white;
  transform: translateX(4px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.1);
  border-color: #667eea;
}

.info-icon {
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 20px;
  flex-shrink: 0;
  transition: all 0.3s;
}

.modern-info-item:hover .info-icon {
  transform: scale(1.1) rotate(5deg);
}

.info-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.info-label {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  color: #6b7280;
}

.info-value {
  font-size: 20px;
  font-weight: 600;
  color: #1f2937;
}

/* 室友列表 */
.roommates-section {
  padding-top: 24px;
  border-top: 1px solid #e5e7eb;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 20px 0;
}

.roommates-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.roommate-card {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  background: linear-gradient(135deg, #ffffff 0%, #f9fafb 100%);
  border-radius: 16px;
  border: 1px solid #e5e7eb;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
}

.roommate-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(102, 126, 234, 0.1), transparent);
  transition: left 0.5s;
}

.roommate-card:hover::before {
  left: 100%;
}

.roommate-card:hover {
  background: white;
  transform: translateY(-4px) scale(1.02);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.15);
  border-color: #667eea;
}

.roommate-card :deep(.el-avatar) {
  border: 3px solid #e5e7eb;
  transition: all 0.3s;
}

.roommate-card:hover :deep(.el-avatar) {
  border-color: #667eea;
  transform: scale(1.05);
}

.roommate-info {
  flex: 1;
}

.roommate-name {
  font-size: 16px;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 4px;
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

.roommate-detail {
  font-size: 14px;
  color: #6b7280;
  margin: 2px 0;
}

/* 卫生评分 */
.score-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 24px 0;
}

.score-circle {
  margin-bottom: 24px;
}

.score-text {
  font-size: 32px;
  font-weight: 700;
  color: #1f2937;
}

.score-label {
  font-size: 14px;
  color: #6b7280;
  margin-left: 4px;
}

/* 快捷操作 */
.action-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.action-btn-wrapper {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  background: linear-gradient(135deg, #ffffff 0%, #f9fafb 100%);
  border-radius: 16px;
  border: 1px solid #e5e7eb;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
}

.action-btn-wrapper::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 4px;
  height: 100%;
  background: linear-gradient(180deg, #667eea 0%, #764ba2 100%);
  opacity: 0;
  transition: opacity 0.3s;
}

.action-btn-wrapper:hover::before {
  opacity: 1;
}

.action-btn-wrapper:hover {
  background: white;
  transform: translateX(4px);
  box-shadow: 0 4px 16px rgba(102, 126, 234, 0.15);
  border-color: #667eea;
}

.action-btn-icon {
  width: 48px;
  height: 48px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 24px;
  flex-shrink: 0;
  transition: all 0.3s;
}

.action-btn-wrapper:hover .action-btn-icon {
  transform: scale(1.1) rotate(5deg);
}

.action-btn-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.action-btn-title {
  font-size: 16px;
  font-weight: 600;
  color: #1f2937;
}

.action-btn-desc {
  font-size: 12px;
  color: #9ca3af;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.action-btn-arrow {
  font-size: 20px;
  color: #9ca3af;
  transition: all 0.3s;
}

.action-btn-wrapper:hover .action-btn-arrow {
  color: #667eea;
  transform: translateX(4px);
}

/* 响应式 */
@media (max-width: 768px) {
  .info-grid,
  .roommates-grid {
    grid-template-columns: 1fr;
  }
}
</style>
