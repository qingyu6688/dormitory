<template>
  <div class="page-container">
    <div class="page-header">
      <h1>宿舍管理</h1>
    </div>

    <el-card class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="宿舍楼">
          <el-select v-model="searchForm.buildingId" placeholder="请选择" clearable style="width: 200px">
            <el-option
              v-for="item in buildingOptions"
              :key="item.id"
              :label="item.buildingName"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="房间号">
          <el-input v-model="searchForm.roomNo" placeholder="请输入房间号" clearable />
        </el-form-item>
        <el-form-item label="房间状态">
          <el-select v-model="searchForm.roomStatus" placeholder="请选择" clearable style="width: 180px">
            <el-option label="空闲" :value="1" />
            <el-option label="部分入住" :value="2" />
            <el-option label="已满" :value="3" />
            <el-option label="维修中" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            查询
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span>宿舍列表</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增宿舍
          </el-button>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column label="宿舍楼" min-width="140">
          <template #default="{ row }">
            {{ buildingMap[row.buildingId] || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="roomNo" label="房间号" width="120" />
        <el-table-column prop="floor" label="楼层" width="90" />
        <el-table-column prop="bedCount" label="床位数" width="90" />
        <el-table-column prop="occupiedCount" label="已入住" width="90" />
        <el-table-column prop="roomType" label="房间类型" min-width="120" show-overflow-tooltip />
        <el-table-column label="房间状态" width="120">
          <template #default="{ row }">
            <el-tag :type="statusTag(row.roomStatus)">
              {{ statusText(row.roomStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="direction" label="朝向" width="100" />
        <el-table-column prop="area" label="面积(㎡)" width="110" />
        <el-table-column prop="remark" label="备注" min-width="150" show-overflow-tooltip />
        <el-table-column label="操作" width="260" fixed="right">
          <template #default="{ row }">
            <div class="action-buttons">
              <el-button type="success" link size="small" @click="handleViewResidents(row)">
                <el-icon><User /></el-icon>
                住宿人员
              </el-button>
              <el-button type="primary" link size="small" @click="handleEdit(row)">
                <el-icon><Edit /></el-icon>
                编辑
              </el-button>
              <el-button type="danger" link size="small" @click="handleDelete(row)">
                <el-icon><Delete /></el-icon>
                删除
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="pagination.pageNum"
        v-model:page-size="pagination.pageSize"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="fetchData"
        @current-change="fetchData"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="640px" @close="handleDialogClose">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px">
        <el-form-item label="宿舍楼" prop="buildingId">
          <el-select v-model="formData.buildingId" placeholder="请选择宿舍楼" clearable filterable style="width: 100%">
            <el-option
              v-for="item in buildingOptions"
              :key="item.id"
              :label="item.buildingName"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="房间号" prop="roomNo">
              <el-input v-model="formData.roomNo" placeholder="如 A1-101" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="楼层" prop="floor">
              <el-select v-model="formData.floor" placeholder="请先选择宿舍楼" style="width: 100%" :disabled="!formData.buildingId">
                <el-option
                  v-for="floor in floorOptions"
                  :key="floor"
                  :label="`${floor}层`"
                  :value="floor"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="床位数" prop="bedCount">
              <el-select v-model="formData.bedCount" placeholder="请选择床位数" style="width: 100%">
                <el-option
                  v-for="bed in bedCountOptions"
                  :key="bed"
                  :label="`${bed}人间`"
                  :value="bed"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="已入住">
              <el-input v-model="formData.occupiedCount" disabled style="width: 100%">
                <template #suffix>
                  <span style="color: #909399">人（系统自动统计）</span>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="房间类型">
              <el-input v-model="formData.roomType" placeholder="如 4人间" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="房间状态" prop="roomStatus">
              <el-select v-model="formData.roomStatus" placeholder="请选择" style="width: 100%">
                <el-option label="空闲" :value="1" />
                <el-option label="部分入住" :value="2" />
                <el-option label="已满" :value="3" />
                <el-option label="维修中" :value="4" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="朝向">
              <el-input v-model="formData.direction" placeholder="如 南" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="面积(㎡)">
              <el-input-number v-model="formData.area" :min="0" :precision="1" :step="0.5" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注">
          <el-input v-model="formData.remark" type="textarea" rows="3" placeholder="请输入备注信息" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 住宿人员对话框 -->
    <el-dialog v-model="residentsDialogVisible" title="住宿人员" width="900px">
      <div v-if="currentRoom" class="room-info">
        <el-descriptions :column="3" border>
          <el-descriptions-item label="宿舍楼">{{ buildingMap[currentRoom.buildingId] || '-' }}</el-descriptions-item>
          <el-descriptions-item label="房间号">{{ currentRoom.roomNo }}</el-descriptions-item>
          <el-descriptions-item label="楼层">{{ currentRoom.floor }}层</el-descriptions-item>
          <el-descriptions-item label="床位数">{{ currentRoom.bedCount }}</el-descriptions-item>
          <el-descriptions-item label="已入住">{{ currentRoom.occupiedCount }}</el-descriptions-item>
          <el-descriptions-item label="空余床位">{{ currentRoom.bedCount - currentRoom.occupiedCount }}</el-descriptions-item>
        </el-descriptions>
      </div>

      <el-divider />

      <el-table :data="residentsData" v-loading="residentsLoading" border style="margin-top: 20px">
        <el-table-column prop="bedNo" label="床位号" width="90" align="center" />
        <el-table-column prop="studentNo" label="学号" width="130" />
        <el-table-column prop="studentName" label="姓名" width="120" />
        <el-table-column prop="gender" label="性别" width="80" align="center">
          <template #default="{ row }">
            {{ row.gender === 1 ? '男' : row.gender === 2 ? '女' : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="college" label="学院" min-width="140" show-overflow-tooltip />
        <el-table-column prop="major" label="专业" min-width="120" show-overflow-tooltip />
        <el-table-column prop="className" label="班级" width="120" />
        <el-table-column prop="phone" label="电话" width="130" />
        <el-table-column prop="checkInDate" label="入住日期" width="120" />
      </el-table>

      <el-empty v-if="!residentsLoading && residentsData.length === 0" description="暂无住宿人员" />

      <template #footer>
        <el-button @click="residentsDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Edit, Delete, User } from '@element-plus/icons-vue'
import { getRoomPage, addRoom, updateRoom, deleteRoom, getBuildingList, getRoomResidents } from '@/api/dormitory'

const loading = ref(false)
const tableData = ref([])
const buildingOptions = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增宿舍')
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)

// 住宿人员相关
const residentsDialogVisible = ref(false)
const residentsData = ref([])
const residentsLoading = ref(false)
const currentRoom = ref(null)

const searchForm = reactive({
  buildingId: null,
  roomNo: '',
  roomStatus: null
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const formData = reactive({
  id: null,
  buildingId: null,
  roomNo: '',
  floor: 1,
  bedCount: 4,
  occupiedCount: 0,
  roomType: '',
  roomStatus: 1,
  direction: '',
  area: 0,
  remark: ''
})

// 床位数选项（常见的宿舍配置）
const bedCountOptions = [2, 4, 6, 8]

// 根据选择的宿舍楼动态生成楼层选项
const floorOptions = computed(() => {
  if (!formData.buildingId) return []
  const building = buildingOptions.value.find(b => b.id === formData.buildingId)
  if (!building || !building.floorCount) return []
  return Array.from({ length: building.floorCount }, (_, i) => i + 1)
})

// 监听宿舍楼变化，只在新增模式下重置楼层
watch(() => formData.buildingId, (newVal, oldVal) => {
  if (newVal !== oldVal && !isEdit.value) {
    formData.floor = null
  }
})

const formRules = {
  buildingId: [{ required: true, message: '请选择宿舍楼', trigger: 'change' }],
  roomNo: [{ required: true, message: '请输入房间号', trigger: 'blur' }],
  floor: [{ required: true, message: '请输入楼层', trigger: 'blur' }],
  bedCount: [{ required: true, message: '请输入床位数', trigger: 'blur' }],
  roomStatus: [{ required: true, message: '请选择房间状态', trigger: 'change' }]
}

const buildingMap = computed(() => {
  const map = {}
  buildingOptions.value.forEach(item => {
    map[item.id] = item.buildingName
  })
  return map
})

const statusText = (status) => {
  switch (status) {
    case 1: return '空闲'
    case 2: return '部分入住'
    case 3: return '已满'
    case 4: return '维修中'
    default: return '-'
  }
}

const statusTag = (status) => {
  switch (status) {
    case 1: return 'success'
    case 2: return 'warning'
    case 3: return 'danger'
    case 4: return 'info'
    default: return ''
  }
}

const fetchBuildingOptions = async () => {
  try {
    const res = await getBuildingList()
    buildingOptions.value = res.data || []
  } catch (error) {
    console.error('获取宿舍楼失败', error)
  }
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getRoomPage({
      ...searchForm,
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    })
    tableData.value = res.data.records || []
    pagination.total = res.data.total || 0
  } catch (error) {
    console.error('获取宿舍数据失败', error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.pageNum = 1
  fetchData()
}

const handleReset = () => {
  Object.assign(searchForm, {
    buildingId: null,
    roomNo: '',
    roomStatus: null
  })
  handleSearch()
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增宿舍'
  Object.assign(formData, {
    id: null,
    buildingId: null,
    roomNo: '',
    floor: 1,
    bedCount: 4,
    occupiedCount: 0,
    roomType: '',
    roomStatus: 1,
    direction: '',
    area: 0,
    remark: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑宿舍'
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (!valid) return
    submitLoading.value = true
    try {
      if (isEdit.value) {
        await updateRoom(formData)
        ElMessage.success('更新成功')
      } else {
        await addRoom(formData)
        ElMessage.success('新增成功')
      }
      dialogVisible.value = false
      fetchData()
    } catch (error) {
      console.error('提交失败', error)
    } finally {
      submitLoading.value = false
    }
  })
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除宿舍 ${row.roomNo} 吗？`, '提示', {
    type: 'warning'
  })
    .then(async () => {
      await deleteRoom(row.id)
      ElMessage.success('删除成功')
      fetchData()
    })
    .catch(() => {})
}

const handleDialogClose = () => {
  formRef.value?.resetFields()
}

// 查看住宿人员
const handleViewResidents = async (row) => {
  currentRoom.value = row
  residentsDialogVisible.value = true
  residentsLoading.value = true
  residentsData.value = []
  
  try {
    const res = await getRoomResidents(row.id)
    residentsData.value = res.data || []
  } catch (error) {
    console.error('获取住宿人员失败', error)
    ElMessage.error('获取住宿人员失败')
  } finally {
    residentsLoading.value = false
  }
}

onMounted(() => {
  fetchBuildingOptions()
  fetchData()
})
</script>

<style scoped>
.room-info {
  margin-bottom: 20px;
}

.action-buttons {
  display: flex;
  gap: 4px;
  align-items: center;
  flex-wrap: wrap;
}

.action-buttons .el-button.is-link {
  padding: 6px 10px;
  height: auto;
  border-radius: 4px;
  transition: all 150ms;
}

.action-buttons .el-button.is-link .el-icon {
  margin-right: 4px;
}

.action-buttons .el-button.is-link.el-button--success:hover {
  background-color: rgba(103, 194, 58, 0.1) !important;
}

.action-buttons .el-button.is-link.el-button--primary:hover {
  background-color: rgba(37, 99, 235, 0.1) !important;
}

.action-buttons .el-button.is-link.el-button--danger:hover {
  background-color: rgba(239, 68, 68, 0.1) !important;
}
</style>
