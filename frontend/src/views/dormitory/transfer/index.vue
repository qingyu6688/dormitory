<template>
  <div class="page-container">
    <div class="page-header">
      <h1>调宿申请</h1>
    </div>

    <el-card class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="学生ID">
          <el-input-number v-model="searchForm.studentId" :min="1" placeholder="学生ID" />
        </el-form-item>
        <el-form-item label="审批状态">
          <el-select v-model="searchForm.approveStatus" placeholder="请选择" clearable style="width: 160px">
            <el-option label="待审批" :value="1" />
            <el-option label="已通过" :value="2" />
            <el-option label="已拒绝" :value="3" />
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
          <span>调宿申请列表</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            提交申请
          </el-button>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column prop="studentId" label="学生ID" width="100" />
        <el-table-column prop="currentRoomId" label="当前宿舍" width="120" />
        <el-table-column prop="targetRoomId" label="目标宿舍" width="120" />
        <el-table-column prop="reason" label="申请原因" min-width="180" show-overflow-tooltip />
        <el-table-column prop="applyTime" label="申请时间" width="160" />
        <el-table-column prop="approveStatus" label="审批状态" width="110">
          <template #default="{ row }">
            <el-tag :type="approveTag(row.approveStatus)">
              {{ approveText(row.approveStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="executeStatus" label="执行状态" width="110">
          <template #default="{ row }">
            <el-tag :type="row.executeStatus === 2 ? 'success' : 'info'">
              {{ row.executeStatus === 2 ? '已执行' : '未执行' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="approverId" label="审批人" width="100" />
        <el-table-column prop="approveOpinion" label="审批意见" min-width="180" show-overflow-tooltip />
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="720px" @close="handleDialogClose">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="110px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="学生ID" prop="studentId">
              <el-input-number v-model="formData.studentId" :min="1" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="当前宿舍" prop="currentRoomId">
              <el-input-number v-model="formData.currentRoomId" :min="1" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="目标宿舍" prop="targetRoomId">
              <el-input-number v-model="formData.targetRoomId" :min="1" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执行状态">
              <el-select v-model="formData.executeStatus" placeholder="请选择">
                <el-option label="未执行" :value="1" />
                <el-option label="已执行" :value="2" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="申请原因" prop="reason">
          <el-input v-model="formData.reason" type="textarea" rows="3" placeholder="请输入申请原因" />
        </el-form-item>
        <el-form-item label="审批状态">
          <el-select v-model="formData.approveStatus" placeholder="请选择">
            <el-option label="待审批" :value="1" />
            <el-option label="已通过" :value="2" />
            <el-option label="已拒绝" :value="3" />
          </el-select>
        </el-form-item>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="审批人ID">
              <el-input-number v-model="formData.approverId" :min="1" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="申请时间">
              <el-date-picker
                v-model="formData.applyTime"
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
            <el-form-item label="审批时间">
              <el-date-picker
                v-model="formData.approveTime"
                type="datetime"
                value-format="YYYY-MM-DD HH:mm:ss"
                placeholder="选择时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执行时间">
              <el-date-picker
                v-model="formData.executeTime"
                type="datetime"
                value-format="YYYY-MM-DD HH:mm:ss"
                placeholder="选择时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="审批意见">
          <el-input v-model="formData.approveOpinion" type="textarea" rows="2" placeholder="请输入审批意见" />
        </el-form-item>
        <el-form-item label="附件地址">
          <el-input v-model="formData.attachment" placeholder="请输入附件链接" />
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
import { getTransferPage, addTransfer, updateTransfer, deleteTransfer } from '@/api/dormitory'

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('提交申请')
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)

const searchForm = reactive({
  studentId: null,
  approveStatus: null
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const formData = reactive({
  id: null,
  studentId: null,
  currentRoomId: null,
  targetRoomId: null,
  reason: '',
  applyTime: '',
  approveStatus: 1,
  approverId: null,
  approveOpinion: '',
  approveTime: '',
  executeStatus: 1,
  executeTime: '',
  attachment: ''
})

const formRules = {
  studentId: [{ required: true, message: '请输入学生ID', trigger: 'blur' }],
  currentRoomId: [{ required: true, message: '请输入当前宿舍ID', trigger: 'blur' }],
  targetRoomId: [{ required: true, message: '请输入目标宿舍ID', trigger: 'blur' }],
  reason: [{ required: true, message: '请输入申请原因', trigger: 'blur' }]
}

const approveText = (status) => {
  switch (status) {
    case 1: return '待审批'
    case 2: return '已通过'
    case 3: return '已拒绝'
    default: return '-'
  }
}

const approveTag = (status) => {
  switch (status) {
    case 1: return 'warning'
    case 2: return 'success'
    case 3: return 'danger'
    default: return 'info'
  }
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getTransferPage({
      ...searchForm,
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    })
    tableData.value = res.data.records || []
    pagination.total = res.data.total || 0
  } catch (error) {
    console.error('获取调宿申请失败', error)
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
    studentId: null,
    approveStatus: null
  })
  handleSearch()
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '提交申请'
  Object.assign(formData, {
    id: null,
    studentId: null,
    currentRoomId: null,
    targetRoomId: null,
    reason: '',
    applyTime: new Date().toISOString().slice(0, 19).replace('T', ' '),
    approveStatus: 1,
    approverId: null,
    approveOpinion: '',
    approveTime: '',
    executeStatus: 1,
    executeTime: '',
    attachment: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑申请'
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
        await updateTransfer(formData)
        ElMessage.success('更新成功')
      } else {
        await addTransfer(formData)
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
  ElMessageBox.confirm('确认删除该调宿申请吗？', '提示', {
    type: 'warning'
  })
    .then(async () => {
      await deleteTransfer(row.id)
      ElMessage.success('删除成功')
      fetchData()
    })
    .catch(() => {})
}

const handleDialogClose = () => {
  formRef.value?.resetFields()
}

onMounted(() => {
  fetchData()
})
</script>

