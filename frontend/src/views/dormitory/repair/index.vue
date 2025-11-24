<template>
  <div class="page-container">
    <div class="page-header">
      <h1>报修管理</h1>
    </div>

    <el-card class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="报修人学号">
          <el-input v-model="searchForm.reporterNo" placeholder="请输入学号" clearable style="width: 160px" />
        </el-form-item>
        <el-form-item label="报修人姓名">
          <el-input v-model="searchForm.reporterName" placeholder="请输入姓名" clearable style="width: 160px" />
        </el-form-item>
        <el-form-item label="房间号">
          <el-input v-model="searchForm.roomNo" placeholder="请输入房间号" clearable style="width: 160px" />
        </el-form-item>
        <el-form-item label="报修类型">
          <el-input v-model="searchForm.repairType" placeholder="如：电路" clearable style="width: 160px" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择" clearable style="width: 160px">
            <el-option label="待处理" :value="1" />
            <el-option label="处理中" :value="2" />
            <el-option label="已完成" :value="3" />
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
          <span>报修记录</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            提交报修
          </el-button>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column prop="reporterNo" label="报修人学号" width="130" />
        <el-table-column prop="reporterName" label="报修人" width="120" />
        <el-table-column prop="roomNo" label="房间号" width="120" />
        <el-table-column prop="repairType" label="类型" width="120" />
        <el-table-column prop="description" label="问题描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="reportTime" label="报修时间" width="160" />
        <el-table-column prop="urgencyLevel" label="紧急程度" width="110">
          <template #default="{ row }">
            <el-tag :type="urgencyTag(row.urgencyLevel)">
              {{ urgencyText(row.urgencyLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="110">
          <template #default="{ row }">
            <el-tag :type="statusTag(row.status)">
              {{ statusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="assignedToName" label="维修人员" width="120" />
        <el-table-column prop="appointmentTime" label="预约时间" width="160" />
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="760px" @close="handleDialogClose">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="110px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="报修人" prop="reporterId">
              <el-select v-model="formData.reporterId" placeholder="请选择报修人" clearable filterable style="width: 100%">
                <el-option
                  v-for="item in studentOptions"
                  :key="item.id"
                  :label="`${item.studentNo} - ${item.nickname}`"
                  :value="item.id"
                />
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
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="报修类型" prop="repairType">
              <el-input v-model="formData.repairType" placeholder="如 电路/水管" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="紧急程度">
              <el-select v-model="formData.urgencyLevel" placeholder="请选择" style="width: 100%">
                <el-option label="普通" :value="1" />
                <el-option label="紧急" :value="2" />
                <el-option label="特急" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="问题描述" prop="description">
          <el-input v-model="formData.description" type="textarea" rows="3" placeholder="请描述报修问题" />
        </el-form-item>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="报修时间">
              <el-date-picker
                v-model="formData.reportTime"
                type="datetime"
                value-format="YYYY-MM-DD HH:mm:ss"
                placeholder="选择时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="formData.status" placeholder="请选择" style="width: 100%">
                <el-option label="待处理" :value="1" />
                <el-option label="处理中" :value="2" />
                <el-option label="已完成" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="指派维修人">
              <el-select v-model="formData.assignedTo" placeholder="请选择维修人" clearable filterable style="width: 100%">
                <el-option
                  v-for="item in repairmanOptions"
                  :key="item.id"
                  :label="item.nickname"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预约时间">
              <el-date-picker
                v-model="formData.appointmentTime"
                type="datetime"
                value-format="YYYY-MM-DD HH:mm:ss"
                placeholder="选择时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="维修费用">
              <el-input-number v-model="formData.repairCost" :min="0" :step="50" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="费用承担方">
              <el-select v-model="formData.costBearer" placeholder="请选择" style="width: 100%">
                <el-option label="学校" :value="1" />
                <el-option label="学生" :value="2" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="处理结果">
          <el-input v-model="formData.resultDesc" type="textarea" rows="2" placeholder="请输入处理结果" />
        </el-form-item>
        <el-form-item label="满意度">
          <el-rate v-model="formData.satisfaction" allow-half show-score />
        </el-form-item>
        <el-form-item label="评价内容">
          <el-input v-model="formData.comment" type="textarea" rows="2" placeholder="请输入评价" />
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
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Edit, Delete } from '@element-plus/icons-vue'
import { getRepairPage, addRepair, updateRepair, deleteRepair, getRoomList } from '@/api/dormitory'
import { getUserList } from '@/api/user'

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('提交报修')
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)
const studentOptions = ref([])
const roomOptions = ref([])
const repairmanOptions = ref([])

const searchForm = reactive({
  reporterNo: '',
  reporterName: '',
  roomNo: '',
  repairType: '',
  status: null
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const formData = reactive({
  id: null,
  reporterId: null,
  roomId: null,
  repairType: '',
  description: '',
  images: '',
  reportTime: '',
  urgencyLevel: 1,
  status: 1,
  assignedTo: null,
  appointmentTime: '',
  repairCost: 0,
  costBearer: 1,
  resultDesc: '',
  satisfaction: 0,
  comment: ''
})

const formRules = {
  reporterId: [{ required: true, message: '请输入报修人ID', trigger: 'blur' }],
  roomId: [{ required: true, message: '请输入宿舍ID', trigger: 'blur' }],
  repairType: [{ required: true, message: '请输入报修类型', trigger: 'blur' }],
  description: [{ required: true, message: '请输入问题描述', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

const urgencyText = (val) => {
  switch (val) {
    case 1: return '普通'
    case 2: return '紧急'
    case 3: return '特急'
    default: return '-'
  }
}

const urgencyTag = (val) => {
  switch (val) {
    case 1: return 'info'
    case 2: return 'warning'
    case 3: return 'danger'
    default: return 'info'
  }
}

const statusText = (val) => {
  switch (val) {
    case 1: return '待处理'
    case 2: return '处理中'
    case 3: return '已完成'
    default: return '-'
  }
}

const statusTag = (val) => {
  switch (val) {
    case 1: return 'warning'
    case 2: return 'info'
    case 3: return 'success'
    default: return 'info'
  }
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getRepairPage({
      ...searchForm,
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    })
    tableData.value = res.data.records || []
    pagination.total = res.data.total || 0
  } catch (error) {
    console.error('获取报修数据失败', error)
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
    reporterNo: '',
    reporterName: '',
    roomNo: '',
    repairType: '',
    status: null
  })
  handleSearch()
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '提交报修'
  Object.assign(formData, {
    id: null,
    reporterId: null,
    roomId: null,
    repairType: '',
    description: '',
    images: '',
    reportTime: new Date().toISOString().slice(0, 19).replace('T', ' '),
    urgencyLevel: 1,
    status: 1,
    assignedTo: null,
    appointmentTime: '',
    repairCost: 0,
    costBearer: 1,
    resultDesc: '',
    satisfaction: 0,
    comment: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑报修'
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
        await updateRepair(formData)
        ElMessage.success('更新成功')
      } else {
        await addRepair(formData)
        ElMessage.success('提交成功')
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
  ElMessageBox.confirm('确认删除该报修记录吗？', '提示', {
    type: 'warning'
  })
    .then(async () => {
      await deleteRepair(row.id)
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

// 加载维修人员选项（宿管员和维修人员）
const fetchRepairmanOptions = async () => {
  try {
    const res = await getUserList()
    // 过滤出宿管员(roleId=3)和管理员(roleId=1,2)
    repairmanOptions.value = (res.data || []).filter(user => [1, 2, 3].includes(user.roleId))
  } catch (error) {
    console.error('获取维修人员列表失败', error)
  }
}

onMounted(() => {
  fetchData()
  fetchStudentOptions()
  fetchRoomOptions()
  fetchRepairmanOptions()
})
</script>

