<template>
  <div class="page-container">
    <div class="page-header">
      <h1>宿舍楼管理</h1>
    </div>

    <el-card class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="楼栋名称">
          <el-select v-model="searchForm.buildingName" placeholder="请选择楼栋" clearable filterable style="width: 200px">
            <el-option
              v-for="item in buildingNameOptions"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="性别限制">
          <el-select v-model="searchForm.genderType" placeholder="请选择" clearable style="width: 160px">
            <el-option label="男生宿舍" :value="1" />
            <el-option label="女生宿舍" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择" clearable style="width: 160px">
            <el-option label="启用" :value="1" />
            <el-option label="停用" :value="2" />
            <el-option label="维修中" :value="3" />
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
          <span>宿舍楼列表</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增楼栋
          </el-button>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column prop="buildingNo" label="楼栋编号" width="120" />
        <el-table-column prop="buildingName" label="楼栋名称" min-width="140" />
        <el-table-column prop="genderType" label="性别限制" width="110">
          <template #default="{ row }">
            <el-tag :type="row.genderType === 1 ? 'primary' : 'success'">
              {{ row.genderType === 1 ? '男生' : '女生' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="floorCount" label="楼层数" width="90" />
        <el-table-column prop="roomPerFloor" label="每层房间" width="110" />
        <el-table-column prop="status" label="状态" width="110">
          <template #default="{ row }">
            <el-tag :type="statusTagMap[row.status]">
              {{ statusTextMap[row.status] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="address" label="地址" min-width="160" show-overflow-tooltip />
        <el-table-column prop="buildYear" label="建造年份" width="110" />
        <el-table-column prop="managerName" label="宿管员" width="120" />
        <el-table-column label="操作" width="180" fixed="right">
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="640px" @close="handleDialogClose">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="楼栋编号" prop="buildingNo">
              <el-input v-model="formData.buildingNo" placeholder="请输入楼栋编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="楼栋名称" prop="buildingName">
              <el-input v-model="formData.buildingName" placeholder="请输入楼栋名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="楼层数" prop="floorCount">
              <el-input-number v-model="formData.floorCount" :min="1" :max="30" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="每层房间" prop="roomPerFloor">
              <el-input-number v-model="formData.roomPerFloor" :min="1" :max="50" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="性别限制" prop="genderType">
              <el-radio-group v-model="formData.genderType">
                <el-radio :label="1">男生宿舍</el-radio>
                <el-radio :label="2">女生宿舍</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="formData.status" placeholder="请选择">
                <el-option label="启用" :value="1" />
                <el-option label="停用" :value="2" />
                <el-option label="维修中" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="宿管员" prop="managerId">
              <el-select v-model="formData.managerId" placeholder="请选择宿管员" clearable filterable style="width: 100%">
                <el-option
                  v-for="item in managerOptions"
                  :key="item.id"
                  :label="item.nickname"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="建造年份" prop="buildYear">
              <el-input-number v-model="formData.buildYear" :min="1990" :max="2099" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="地址" prop="address">
          <el-input v-model="formData.address" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="formData.remark" type="textarea" rows="3" placeholder="请输入备注信息" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Edit, Delete } from '@element-plus/icons-vue'
import { getBuildingPage, addBuilding, updateBuilding, deleteBuilding } from '@/api/dormitory'
import { getUserList } from '@/api/user'

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增楼栋')
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)
const managerOptions = ref([])
const buildingNameOptions = ref([])

const statusTextMap = {
  1: '启用',
  2: '停用',
  3: '维修中'
}

const statusTagMap = {
  1: 'success',
  2: 'warning',
  3: 'danger'
}

const searchForm = reactive({
  buildingName: '',
  genderType: null,
  status: null
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const formData = reactive({
  id: null,
  buildingNo: '',
  buildingName: '',
  floorCount: 6,
  roomPerFloor: 20,
  genderType: 1,
  status: 1,
  managerId: null,
  address: '',
  buildYear: new Date().getFullYear(),
  remark: ''
})

const formRules = {
  buildingNo: [{ required: true, message: '请输入楼栋编号', trigger: 'blur' }],
  buildingName: [{ required: true, message: '请输入楼栋名称', trigger: 'blur' }],
  genderType: [{ required: true, message: '请选择性别限制', trigger: 'change' }],
  floorCount: [{ required: true, message: '请输入楼层数', trigger: 'blur' }],
  roomPerFloor: [{ required: true, message: '请输入每层房间数', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getBuildingPage({
      ...searchForm,
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    })
    tableData.value = res.data.records || []
    pagination.total = res.data.total || 0
  } catch (error) {
    console.error('获取宿舍楼数据失败', error)
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
    buildingName: '',
    genderType: null,
    status: null
  })
  handleSearch()
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增楼栋'
  Object.assign(formData, {
    id: null,
    buildingNo: '',
    buildingName: '',
    floorCount: 6,
    roomPerFloor: 20,
    genderType: 1,
    status: 1,
    managerId: null,
    address: '',
    buildYear: new Date().getFullYear(),
    remark: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑楼栋'
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
        await updateBuilding(formData)
        ElMessage.success('更新成功')
      } else {
        await addBuilding(formData)
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
  ElMessageBox.confirm(`确认删除楼栋「${row.buildingName}」吗？`, '提示', {
    type: 'warning'
  })
    .then(async () => {
      await deleteBuilding(row.id)
      ElMessage.success('删除成功')
      fetchData()
    })
    .catch(() => {})
}

const handleDialogClose = () => {
  formRef.value?.resetFields()
}

// 加载宿管员列表（角色为宿管员的用户）
const fetchManagerOptions = async () => {
  try {
    const res = await getUserList()
    // 过滤出宿管员角色的用户（roleId为3）
    managerOptions.value = (res.data || []).filter(user => user.roleId === 3)
  } catch (error) {
    console.error('获取宿管员列表失败', error)
  }
}

// 加载楼栋名称选项
const fetchBuildingNameOptions = async () => {
  try {
    const res = await getBuildingPage({ pageNum: 1, pageSize: 1000 })
    const buildings = res.data.records || []
    // 提取所有楼栋名称并去重
    buildingNameOptions.value = [...new Set(buildings.map(b => b.buildingName).filter(name => name))]
  } catch (error) {
    console.error('获取楼栋名称列表失败', error)
  }
}

onMounted(() => {
  fetchData()
  fetchManagerOptions()
  fetchBuildingNameOptions()
})
</script>

