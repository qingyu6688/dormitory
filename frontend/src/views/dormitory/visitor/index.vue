<template>
  <div class="page-container">
    <div class="page-header">
      <h1>访客登记</h1>
    </div>

    <el-card class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="访客姓名">
          <el-input v-model="searchForm.visitorName" placeholder="请输入访客姓名" clearable />
        </el-form-item>
        <el-form-item label="身份证号">
          <el-input v-model="searchForm.visitorIdCard" placeholder="请输入身份证号" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择" clearable style="width: 160px">
            <el-option label="在楼内" :value="1" />
            <el-option label="已离开" :value="2" />
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
          <span>访客列表</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增登记
          </el-button>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column prop="visitorName" label="访客姓名" width="120" />
        <el-table-column prop="visitorGender" label="性别" width="80">
          <template #default="{ row }">
            {{ row.visitorGender === 1 ? '男' : row.visitorGender === 2 ? '女' : '其他' }}
          </template>
        </el-table-column>
        <el-table-column prop="visitorIdCard" label="身份证号" width="180" />
        <el-table-column prop="visitorPhone" label="联系方式" width="130" />
        <el-table-column prop="visitedStudentNo" label="被访学生学号" width="130" />
        <el-table-column prop="visitedStudentName" label="被访学生姓名" width="130" />
        <el-table-column prop="buildingName" label="宿舍楼" width="120" />
        <el-table-column prop="visitReason" label="来访事由" min-width="180" show-overflow-tooltip />
        <el-table-column prop="enterTime" label="进入时间" width="160" />
        <el-table-column prop="planLeaveTime" label="预计离开" width="160" />
        <el-table-column prop="actualLeaveTime" label="实际离开" width="160" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'warning' : 'success'">
              {{ row.status === 1 ? '在楼内' : '已离开' }}
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="720px" @close="handleDialogClose">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="110px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="访客姓名" prop="visitorName">
              <el-input v-model="formData.visitorName" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别" prop="visitorGender">
              <el-radio-group v-model="formData.visitorGender">
                <el-radio :label="1">男</el-radio>
                <el-radio :label="2">女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="身份证号" prop="visitorIdCard">
              <el-input v-model="formData.visitorIdCard" placeholder="请输入身份证号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="visitorPhone">
              <el-input v-model="formData.visitorPhone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="被访学生ID" prop="visitedStudentId">
              <el-input-number v-model="formData.visitedStudentId" :min="1" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="宿舍楼ID" prop="buildingId">
              <el-input-number v-model="formData.buildingId" :min="1" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="来访事由">
          <el-input v-model="formData.visitReason" placeholder="请输入来访事由" />
        </el-form-item>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="进入时间" prop="enterTime">
              <el-date-picker
                v-model="formData.enterTime"
                type="datetime"
                value-format="YYYY-MM-DD HH:mm:ss"
                placeholder="选择时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预计离开" prop="planLeaveTime">
              <el-date-picker
                v-model="formData.planLeaveTime"
                type="datetime"
                value-format="YYYY-MM-DD HH:mm:ss"
                placeholder="选择时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="实际离开">
          <el-date-picker
            v-model="formData.actualLeaveTime"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="选择时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="登记人">
              <el-select v-model="formData.registrarId" placeholder="请选择登记人" clearable filterable style="width: 100%">
                <el-option
                  v-for="item in registrarOptions"
                  :key="item.id"
                  :label="item.nickname"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="formData.status" placeholder="请选择">
                <el-option label="在楼内" :value="1" />
                <el-option label="已离开" :value="2" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注">
          <el-input v-model="formData.remark" type="textarea" rows="2" placeholder="请输入备注信息" />
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
import { getVisitorPage, addVisitor, updateVisitor, deleteVisitor } from '@/api/dormitory'

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增登记')
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)

const searchForm = reactive({
  visitorName: '',
  visitorIdCard: '',
  status: null
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const formData = reactive({
  id: null,
  visitorName: '',
  visitorGender: 1,
  visitorIdCard: '',
  visitorPhone: '',
  visitedStudentId: null,
  buildingId: null,
  visitReason: '',
  enterTime: '',
  planLeaveTime: '',
  actualLeaveTime: '',
  registrarId: null,
  status: 1,
  remark: ''
})

const formRules = {
  visitorName: [{ required: true, message: '请输入访客姓名', trigger: 'blur' }],
  visitorGender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  visitorIdCard: [{ required: true, message: '请输入身份证号', trigger: 'blur' }],
  visitorPhone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }],
  visitedStudentId: [{ required: true, message: '请输入被访学生ID', trigger: 'blur' }],
  buildingId: [{ required: true, message: '请输入宿舍楼ID', trigger: 'blur' }],
  enterTime: [{ required: true, message: '请选择进入时间', trigger: 'change' }],
  planLeaveTime: [{ required: true, message: '请选择预计离开时间', trigger: 'change' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getVisitorPage({
      ...searchForm,
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    })
    tableData.value = res.data.records || []
    pagination.total = res.data.total || 0
  } catch (error) {
    console.error('获取访客数据失败', error)
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
    visitorName: '',
    visitorIdCard: '',
    status: null
  })
  handleSearch()
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增登记'
  Object.assign(formData, {
    id: null,
    visitorName: '',
    visitorGender: 1,
    visitorIdCard: '',
    visitorPhone: '',
    visitedStudentId: null,
    buildingId: null,
    visitReason: '',
    enterTime: new Date().toISOString().slice(0, 19).replace('T', ' '),
    planLeaveTime: '',
    actualLeaveTime: '',
    registrarId: null,
    status: 1,
    remark: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑登记'
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
        await updateVisitor(formData)
        ElMessage.success('更新成功')
      } else {
        await addVisitor(formData)
        ElMessage.success('登记成功')
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
  ElMessageBox.confirm('确认删除该访客记录吗？', '提示', { type: 'warning' })
    .then(async () => {
      await deleteVisitor(row.id)
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

