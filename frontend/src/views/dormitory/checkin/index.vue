<template>
  <div class="page-container">
    <div class="page-header">
      <h1>入住管理</h1>
    </div>

    <el-card class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="学生学号">
          <el-input v-model="searchForm.studentNo" placeholder="请输入学号" clearable style="width: 160px" />
        </el-form-item>
        <el-form-item label="学生姓名">
          <el-input v-model="searchForm.studentName" placeholder="请输入姓名" clearable style="width: 160px" />
        </el-form-item>
        <el-form-item label="房间号">
          <el-input v-model="searchForm.roomNo" placeholder="请输入房间号" clearable style="width: 160px" />
        </el-form-item>
        <el-form-item label="入住状态">
          <el-select v-model="searchForm.status" placeholder="请选择" clearable style="width: 160px">
            <el-option label="已入住" :value="1" />
            <el-option label="已退宿" :value="2" />
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
          <span>入住记录</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            办理入住
          </el-button>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column prop="studentNo" label="学号" width="120" />
        <el-table-column prop="studentName" label="学生姓名" width="120" />
        <el-table-column prop="roomNo" label="房间号" width="120" />
        <el-table-column prop="bedNo" label="床位号" width="90" />
        <el-table-column prop="checkInDate" label="入住日期" width="140" />
        <el-table-column prop="planCheckOutDate" label="计划退宿" width="140" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '已入住' : '已退宿' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="depositAmount" label="押金" width="100" />
        <el-table-column prop="depositStatus" label="押金状态" width="110">
          <template #default="{ row }">
            {{ depositStatusText(row.depositStatus) }}
          </template>
        </el-table-column>
        <el-table-column prop="keyNo" label="钥匙编号" width="120" />
        <el-table-column prop="operatorName" label="办理人" width="120" />
        <el-table-column prop="remark" label="备注" min-width="160" show-overflow-tooltip />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <div class="action-buttons">
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="700px" @close="handleDialogClose">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="110px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="学生" prop="studentId">
              <el-select v-model="formData.studentId" placeholder="请选择学生" clearable filterable style="width: 100%">
                <el-option
                  v-for="item in studentOptions"
                  :key="item.id"
                  :label="`${item.studentNo} - ${item.nickname}`"
                  :value="item.id"
                >
                  <span>{{ item.studentNo }} - {{ item.nickname }}</span>
                  <span style="float: right; color: #8492a6; font-size: 13px">{{ item.className }}</span>
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="宿舍" prop="roomId">
              <el-select v-model="formData.roomId" placeholder="请选择宿舍" clearable filterable style="width: 100%">
                <el-option
                  v-for="item in roomOptions"
                  :key="item.id"
                  :label="item.roomNo"
                  :value="item.id"
                >
                  <span>{{ item.roomNo }}</span>
                  <span style="float: right; color: #8492a6; font-size: 13px">{{ item.occupiedCount }}/{{ item.bedCount }}</span>
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="床位号" prop="bedNo">
              <el-select v-model="formData.bedNo" placeholder="请选择床位" clearable style="width: 100%" :disabled="!formData.roomId">
                <el-option
                  v-for="bed in bedOptions"
                  :key="bed"
                  :label="`${bed}号床`"
                  :value="bed"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="钥匙编号">
              <el-select v-model="formData.keyNo" placeholder="请选择钥匙编号" clearable filterable allow-create style="width: 100%" :disabled="!formData.roomId">
                <el-option
                  v-for="key in keyOptions"
                  :key="key"
                  :label="key"
                  :value="key"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="入住日期" prop="checkInDate">
              <el-date-picker
                v-model="formData.checkInDate"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="选择日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划退宿" prop="planCheckOutDate">
              <el-date-picker
                v-model="formData.planCheckOutDate"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="选择日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="押金金额">
              <el-input-number v-model="formData.depositAmount" :min="0" :step="50" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="押金状态">
              <el-select v-model="formData.depositStatus" placeholder="请选择">
                <el-option label="未交" :value="1" />
                <el-option label="已交" :value="2" />
                <el-option label="已退" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="formData.status" placeholder="请选择">
                <el-option label="已入住" :value="1" />
                <el-option label="已退宿" :value="2" />
              </el-select>
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Edit, Delete } from '@element-plus/icons-vue'
import { getCheckInPage, addCheckIn, updateCheckIn, deleteCheckIn, getRoomList } from '@/api/dormitory'
import { getUserList } from '@/api/user'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('办理入住')
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)
const studentOptions = ref([])
const roomOptions = ref([])
const operatorOptions = ref([])

const searchForm = reactive({
  studentNo: '',
  studentName: '',
  roomNo: '',
  status: null
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const formData = reactive({
  id: null,
  studentId: null,
  roomId: null,
  bedNo: 1,
  checkInDate: '',
  planCheckOutDate: '',
  status: 1,
  depositAmount: 0,
  depositStatus: 2,
  keyNo: '',
  operatorId: null,
  remark: ''
})

const formRules = {
  studentId: [{ required: true, message: '请输入学生ID', trigger: 'blur' }],
  roomId: [{ required: true, message: '请输入宿舍ID', trigger: 'blur' }],
  bedNo: [{ required: true, message: '请输入床位号', trigger: 'blur' }],
  checkInDate: [{ required: true, message: '请选择入住日期', trigger: 'change' }],
  planCheckOutDate: [{ required: true, message: '请选择计划退宿日期', trigger: 'change' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

const depositStatusText = (status) => {
  switch (status) {
    case 1: return '未交'
    case 2: return '已交'
    case 3: return '已退'
    default: return '-'
  }
}

// 根据选择的宿舍动态生成床位选项
const bedOptions = computed(() => {
  if (!formData.roomId) return []
  const room = roomOptions.value.find(r => r.id === formData.roomId)
  if (!room || !room.bedCount) return []
  
  const beds = Array.from({ length: room.bedCount }, (_, i) => i + 1)
  
  // 如果是编辑模式且当前床位号不在选项中，添加当前床位号
  if (isEdit.value && formData.bedNo && !beds.includes(formData.bedNo)) {
    beds.push(formData.bedNo)
    beds.sort((a, b) => a - b)
  }
  
  return beds
})

// 根据选择的宿舍动态生成钥匙编号选项
const keyOptions = computed(() => {
  if (!formData.roomId) return []
  const room = roomOptions.value.find(r => r.id === formData.roomId)
  if (!room || !room.bedCount || !room.roomNo) return []
  
  // 生成所有可能的钥匙编号：房间号-床位号
  const allKeys = Array.from({ length: room.bedCount }, (_, i) => `${room.roomNo}-${i + 1}`)
  
  // 获取当前宿舍已分配的钥匙（从表格数据中筛选）
  const usedKeys = tableData.value
    .filter(item => item.roomId === formData.roomId && item.status === 1 && item.keyNo)
    .map(item => item.keyNo)
  
  // 如果是编辑模式，排除当前记录的钥匙
  const excludeKeys = isEdit.value && formData.id 
    ? usedKeys.filter(key => {
        const currentRecord = tableData.value.find(item => item.id === formData.id)
        return key !== currentRecord?.keyNo
      })
    : usedKeys
  
  // 过滤出未使用的钥匙
  const availableKeys = allKeys.filter(key => !excludeKeys.includes(key))
  
  // 如果是编辑模式且当前钥匙不在可用列表中，添加当前钥匙
  if (isEdit.value && formData.keyNo && !availableKeys.includes(formData.keyNo)) {
    availableKeys.push(formData.keyNo)
  }
  
  return availableKeys
})

// 监听宿舍变化，只在新增模式下重置床位号和钥匙编号
watch(() => formData.roomId, (newVal, oldVal) => {
  if (newVal !== oldVal && !isEdit.value) {
    formData.bedNo = null
    formData.keyNo = ''
    
    // 自动选择第一个可用的钥匙
    if (keyOptions.value.length > 0) {
      formData.keyNo = keyOptions.value[0]
    }
  }
})

// 监听床位号变化，自动生成对应的钥匙编号
watch(() => formData.bedNo, (newVal) => {
  if (newVal && formData.roomId && !isEdit.value) {
    const room = roomOptions.value.find(r => r.id === formData.roomId)
    if (room && room.roomNo) {
      const suggestedKey = `${room.roomNo}-${newVal}`
      // 如果建议的钥匙编号可用，自动填充
      if (keyOptions.value.includes(suggestedKey)) {
        formData.keyNo = suggestedKey
      }
    }
  }
})

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getCheckInPage({
      ...searchForm,
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    })
    tableData.value = res.data.records || []
    pagination.total = res.data.total || 0
  } catch (error) {
    console.error('获取入住记录失败', error)
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
    studentNo: '',
    studentName: '',
    roomNo: '',
    status: null
  })
  handleSearch()
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '办理入住'
  Object.assign(formData, {
    id: null,
    studentId: null,
    roomId: null,
    bedNo: 1,
    checkInDate: '',
    planCheckOutDate: '',
    status: 1,
    depositAmount: 0,
    depositStatus: 2,
    keyNo: '',
    operatorId: null,
    remark: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑入住信息'
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    submitLoading.value = true
    try {
      const submitData = { ...formData }
      
      // 自动添加当前用户ID作为办理人
      if (!isEdit.value && userStore.userInfo) {
        submitData.operatorId = userStore.userInfo.id
      }
      
      if (isEdit.value) {
        await updateCheckIn(submitData)
        ElMessage.success('更新成功')
      } else {
        await addCheckIn(submitData)
        ElMessage.success('办理成功')
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
  ElMessageBox.confirm('确认删除该入住记录吗？', '提示', {
    type: 'warning'
  })
    .then(async () => {
      await deleteCheckIn(row.id)
      ElMessage.success('删除成功')
      fetchData()
    })
    .catch(() => {})
}

const handleDialogClose = () => {
  formRef.value?.resetFields()
}

// 加载学生选项（角色为学生的用户）
const fetchStudentOptions = async () => {
  try {
    const res = await getUserList()
    // 过滤出学生角色的用户（roleId为4）
    studentOptions.value = (res.data || []).filter(user => user.roleId === 4)
  } catch (error) {
    console.error('获取学生列表失败', error)
  }
}

// 加载宿舍选项
const fetchRoomOptions = async () => {
  try {
    const res = await getRoomList()
    roomOptions.value = res.data || []
  } catch (error) {
    console.error('获取宿舍列表失败', error)
  }
}

// 加载办理人选项（宿管员和管理员）
const fetchOperatorOptions = async () => {
  try {
    const res = await getUserList()
    // 过滤出宿管员(roleId=3)和管理员(roleId=1,2)
    operatorOptions.value = (res.data || []).filter(user => [1, 2, 3].includes(user.roleId))
  } catch (error) {
    console.error('获取办理人列表失败', error)
  }
}

onMounted(() => {
  fetchData()
  fetchStudentOptions()
  fetchRoomOptions()
  fetchOperatorOptions()
})
</script>

