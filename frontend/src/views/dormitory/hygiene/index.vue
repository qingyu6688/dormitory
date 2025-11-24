<template>
  <div class="page-container">
    <div class="page-header">
      <h1>卫生检查</h1>
    </div>

    <el-card class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="房间号">
          <el-input v-model="searchForm.roomNo" placeholder="请输入房间号" clearable style="width: 160px" />
        </el-form-item>
        <el-form-item label="检查类型">
          <el-input v-model="searchForm.checkType" placeholder="如 周检查" clearable style="width: 160px" />
        </el-form-item>
        <el-form-item label="复查状态">
          <el-select v-model="searchForm.recheckStatus" placeholder="请选择" clearable style="width: 180px">
            <el-option label="无需复查" :value="1" />
            <el-option label="待复查" :value="2" />
            <el-option label="已复查" :value="3" />
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
          <span>检查记录</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增检查
          </el-button>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column prop="checkDate" label="检查日期" width="140" />
        <el-table-column prop="checkType" label="检查类型" width="120" />
        <el-table-column prop="roomNo" label="房间号" width="120" />
        <el-table-column prop="checkerName" label="检查人" width="120" />
        <el-table-column prop="score" label="得分" width="80" />
        <el-table-column prop="grade" label="等级" width="100" />
        <el-table-column prop="problemDesc" label="问题描述" min-width="180" show-overflow-tooltip />
        <el-table-column prop="rectifyDeadline" label="整改期限" width="140" />
        <el-table-column prop="recheckStatus" label="复查状态" width="110">
          <template #default="{ row }">
            <el-tag :type="recheckTag(row.recheckStatus)">
              {{ recheckText(row.recheckStatus) }}
            </el-tag>
          </template>
        </el-table-column>
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
            <el-form-item label="检查日期" prop="checkDate">
              <el-date-picker
                v-model="formData.checkDate"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="选择日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="检查类型" prop="checkType">
              <el-input v-model="formData.checkType" placeholder="如 周检查/抽查" />
            </el-form-item>
          </el-col>
        </el-row>
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
            <el-form-item label="检查人" prop="checkerId">
              <el-select v-model="formData.checkerId" placeholder="请选择检查人" clearable filterable style="width: 100%">
                <el-option
                  v-for="item in checkerOptions"
                  :key="item.id"
                  :label="item.nickname"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="得分" prop="score">
              <el-input-number v-model="formData.score" :min="0" :max="100" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="等级">
              <el-input v-model="formData.grade" placeholder="如 优秀/良好" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="问题描述">
          <el-input v-model="formData.problemDesc" type="textarea" rows="2" placeholder="请输入问题描述" />
        </el-form-item>
        <el-form-item label="整改要求">
          <el-input v-model="formData.rectifyRequirement" type="textarea" rows="2" placeholder="请输入整改要求" />
        </el-form-item>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="整改期限">
              <el-date-picker
                v-model="formData.rectifyDeadline"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="选择日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="复查状态" prop="recheckStatus">
              <el-select v-model="formData.recheckStatus" placeholder="请选择">
                <el-option label="无需复查" :value="1" />
                <el-option label="待复查" :value="2" />
                <el-option label="已复查" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="复查结果">
          <el-input v-model="formData.recheckResult" type="textarea" rows="2" placeholder="请输入复查结果" />
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
import { getHygienePage, addHygiene, updateHygiene, deleteHygiene, getRoomList } from '@/api/dormitory'
import { getUserList } from '@/api/user'

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增检查')
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)
const roomOptions = ref([])
const checkerOptions = ref([])

const searchForm = reactive({
  roomNo: '',
  checkType: '',
  recheckStatus: null
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const formData = reactive({
  id: null,
  checkDate: '',
  checkType: '',
  roomId: null,
  checkerId: null,
  score: 0,
  grade: '',
  problemDesc: '',
  rectifyRequirement: '',
  rectifyDeadline: '',
  recheckStatus: 1,
  recheckResult: ''
})

const formRules = {
  checkDate: [{ required: true, message: '请选择检查日期', trigger: 'change' }],
  checkType: [{ required: true, message: '请输入检查类型', trigger: 'blur' }],
  roomId: [{ required: true, message: '请输入宿舍ID', trigger: 'blur' }],
  checkerId: [{ required: true, message: '请输入检查人ID', trigger: 'blur' }],
  score: [{ required: true, message: '请输入得分', trigger: 'blur' }],
  recheckStatus: [{ required: true, message: '请选择复查状态', trigger: 'change' }]
}

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

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getHygienePage({
      ...searchForm,
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    })
    tableData.value = res.data.records || []
    pagination.total = res.data.total || 0
  } catch (error) {
    console.error('获取检查数据失败', error)
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
    roomNo: '',
    checkType: '',
    recheckStatus: null
  })
  handleSearch()
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增检查'
  Object.assign(formData, {
    id: null,
    checkDate: '',
    checkType: '',
    roomId: null,
    checkerId: null,
    score: 0,
    grade: '',
    problemDesc: '',
    rectifyRequirement: '',
    rectifyDeadline: '',
    recheckStatus: 1,
    recheckResult: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑检查'
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
        await updateHygiene(formData)
        ElMessage.success('更新成功')
      } else {
        await addHygiene(formData)
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
  ElMessageBox.confirm('确认删除该检查记录吗？', '提示', { type: 'warning' })
    .then(async () => {
      await deleteHygiene(row.id)
      ElMessage.success('删除成功')
      fetchData()
    })
    .catch(() => {})
}

const handleDialogClose = () => {
  formRef.value?.resetFields()
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

// 加载检查人选项（宿管员和管理员）
const fetchCheckerOptions = async () => {
  try {
    const res = await getUserList()
    checkerOptions.value = (res.data || []).filter(user => [1, 2, 3].includes(user.roleId))
  } catch (error) {
    console.error('获取检查人列表失败', error)
  }
}

onMounted(() => {
  fetchData()
  fetchRoomOptions()
  fetchCheckerOptions()
})
</script>

