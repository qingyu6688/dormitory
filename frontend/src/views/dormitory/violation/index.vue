<template>
  <div class="page-container">
    <div class="page-header">
      <h1>违纪记录</h1>
    </div>

    <el-card class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="学生学号">
          <el-input v-model="searchForm.studentNo" placeholder="请输入学号" clearable style="width: 160px" />
        </el-form-item>
        <el-form-item label="房间号">
          <el-input v-model="searchForm.roomNo" placeholder="请输入房间号" clearable style="width: 160px" />
        </el-form-item>
        <el-form-item label="违纪类型">
          <el-input v-model="searchForm.violationType" placeholder="请输入类型" clearable style="width: 160px" />
        </el-form-item>
        <el-form-item label="处理状态">
          <el-select v-model="searchForm.handleStatus" placeholder="请选择" clearable style="width: 160px">
            <el-option label="待处理" :value="1" />
            <el-option label="已处理" :value="2" />
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
          <span>违纪列表</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增违纪
          </el-button>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column prop="studentNames" label="违纪学生" min-width="180" show-overflow-tooltip />
        <el-table-column prop="roomNo" label="房间号" min-width="120" />
        <el-table-column prop="violationType" label="违纪类型" width="140" />
        <el-table-column prop="violationTime" label="违纪时间" width="160" />
        <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="handleMethod" label="处理方式" min-width="160" show-overflow-tooltip />
        <el-table-column prop="deductScore" label="扣分" width="90" />
        <el-table-column prop="handleStatus" label="处理状态" width="110">
          <template #default="{ row }">
            <el-tag :type="row.handleStatus === 2 ? 'success' : 'warning'">
              {{ row.handleStatus === 2 ? '已处理' : '待处理' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="notifyParent" label="通知家长" width="110">
          <template #default="{ row }">
            <el-tag :type="row.notifyParent === 1 ? 'success' : 'info'">
              {{ row.notifyParent === 1 ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="780px" @close="handleDialogClose">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="120px">
        <el-form-item label="违纪学生" prop="studentIds">
          <el-select v-model="formData.studentIds" placeholder="请选择学生" multiple filterable :disabled="isEdit" style="width: 100%">
            <el-option
              v-for="item in studentOptions"
              :key="item.id"
              :label="`${item.studentNo} - ${item.nickname}`"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-row :gutter="16">
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
          <el-col :span="12">
            <el-form-item label="违纪类型" prop="violationType">
              <el-input v-model="formData.violationType" placeholder="请输入违纪类型" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="违纪时间" prop="violationTime">
          <el-date-picker
            v-model="formData.violationTime"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="选择时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="违纪描述">
          <el-input v-model="formData.description" type="textarea" rows="3" placeholder="请输入违纪描述" />
        </el-form-item>
        <el-form-item label="处理方式">
          <el-input v-model="formData.handleMethod" placeholder="请输入处理方式" />
        </el-form-item>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="扣分">
              <el-input-number v-model="formData.deductScore" :min="0" :max="100" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="处理状态">
              <el-select v-model="formData.handleStatus" placeholder="请选择" style="width: 100%">
                <el-option label="待处理" :value="1" />
                <el-option label="已处理" :value="2" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="通知家长">
              <el-switch v-model="formData.notifyParent" :active-value="1" :inactive-value="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="通知辅导员">
              <el-switch v-model="formData.notifyTeacher" :active-value="1" :inactive-value="0" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="处理状态" prop="handleStatus">
              <el-select v-model="formData.handleStatus" placeholder="请选择">
                <el-option label="待处理" :value="1" />
                <el-option label="已处理" :value="2" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="处理结果">
              <el-input v-model="formData.handleResult" placeholder="请输入处理结果" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注">
          <el-input v-model="formData.remark" type="textarea" rows="2" placeholder="请输入备注" />
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
import { getViolationPage, addViolation, updateViolation, deleteViolation, getRoomList } from '@/api/dormitory'
import { getUserList } from '@/api/user'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增违纪')
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)
const studentOptions = ref([])
const roomOptions = ref([])

const searchForm = reactive({
  studentNo: '',
  roomNo: '',
  violationType: '',
  handleStatus: null
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const formData = reactive({
  id: null,
  studentIds: '',
  roomId: null,
  violationType: '',
  violationTime: '',
  description: '',
  handleMethod: '',
  deductScore: 0,
  recorderId: null,
  notifyParent: 0,
  notifyTeacher: 0,
  handleStatus: 1,
  handleResult: '',
  remark: ''
})

const formRules = {
  studentIds: [{ required: true, message: '请输入学生ID列表', trigger: 'blur' }],
  roomId: [{ required: true, message: '请输入宿舍ID', trigger: 'blur' }],
  violationType: [{ required: true, message: '请输入违纪类型', trigger: 'blur' }],
  violationTime: [{ required: true, message: '请选择违纪时间', trigger: 'change' }],
  handleStatus: [{ required: true, message: '请选择处理状态', trigger: 'change' }]
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getViolationPage({
      ...searchForm,
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    })
    tableData.value = res.data.records || []
    pagination.total = res.data.total || 0
  } catch (error) {
    console.error('获取违纪数据失败', error)
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
    roomNo: '',
    violationType: '',
    handleStatus: null
  })
  handleSearch()
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增违纪'
  Object.assign(formData, {
    id: null,
    studentIds: '',
    roomId: null,
    violationType: '',
    violationTime: '',
    description: '',
    handleMethod: '',
    deductScore: 0,
    recorderId: null,
    notifyParent: 0,
    notifyTeacher: 0,
    handleStatus: 1,
    handleResult: '',
    remark: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑违纪'
  Object.assign(formData, row)
  
  // 将studentIds字符串转换为数组
  if (formData.studentIds && typeof formData.studentIds === 'string') {
    formData.studentIds = formData.studentIds.split(',').map(id => parseInt(id.trim())).filter(id => !isNaN(id))
  }
  
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    submitLoading.value = true
    try {
      // 将studentIds数组转换为逗号分隔的字符串
      const submitData = { ...formData }
      if (Array.isArray(submitData.studentIds)) {
        submitData.studentIds = submitData.studentIds.join(',')
      }
      
      // 自动添加当前用户ID作为记录人
      if (!isEdit.value && userStore.userInfo) {
        submitData.recorderId = userStore.userInfo.id
      }
      
      if (isEdit.value) {
        await updateViolation(submitData)
        ElMessage.success('更新成功')
      } else {
        await addViolation(submitData)
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
  ElMessageBox.confirm('确认删除该违纪记录吗？', '提示', { type: 'warning' })
    .then(async () => {
      await deleteViolation(row.id)
      ElMessage.success('删除成功')
      fetchData()
    })
    .catch(() => {})
}

const handleDialogClose = () => {
  formRef.value?.resetFields()
}

const fetchStudentOptions = async () => {
  try {
    const res = await getUserList()
    studentOptions.value = (res.data || []).filter(user => user.roleId === 4)
  } catch (error) {
    console.error('获取学生列表失败', error)
  }
}

const fetchRoomOptions = async () => {
  try {
    const res = await getRoomList()
    roomOptions.value = res.data || []
  } catch (error) {
    console.error('获取宿舍列表失败', error)
  }
}

onMounted(() => {
  fetchData()
  fetchStudentOptions()
  fetchRoomOptions()
})
</script>

