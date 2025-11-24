<template>
  <div class="page-container">
    <div class="page-header">
      <h1>字典管理</h1>
    </div>

    <!-- 搜索区 -->
    <el-card class="search-card">
      <el-input
        v-model="searchText"
        placeholder="请输入字典类型名称"
        clearable
        style="width: 300px;"
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
    </el-card>

    <!-- 表格区 -->
    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span>字典列表</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增字典
          </el-button>
        </div>
      </template>

      <el-table :data="filteredData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="字典名称" />
        <el-table-column prop="type" label="字典类型" />
        <el-table-column prop="code" label="字典码" />
        <el-table-column prop="value" label="字典值" />
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column prop="remark" label="备注" show-overflow-tooltip />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <div class="action-buttons">
              <el-button type="primary" link size="small" @click="handleEdit(row)">
                编辑
              </el-button>
              <el-button type="danger" link size="small" @click="handleDelete(row)">
                删除
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
      @close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
      >
        <el-form-item label="字典名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入字典名称（如：用户性别）" />
        </el-form-item>
        <el-form-item label="字典类型" prop="type">
          <el-input v-model="formData.type" placeholder="请输入字典类型（如：sys_user_sex）" />
        </el-form-item>
        <el-form-item label="字典码" prop="code">
          <el-input v-model="formData.code" placeholder="请输入字典码（如：1）" />
        </el-form-item>
        <el-form-item label="字典值" prop="value">
          <el-input v-model="formData.value" placeholder="请输入字典值（如：男）" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="formData.sort" :min="0" :max="9999" style="width: 100%" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="formData.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Plus } from '@element-plus/icons-vue'
import { getDictList, addDict, updateDict, deleteDict } from '@/api/dict'

const loading = ref(false)
const tableData = ref([])
const searchText = ref('')
const dialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)

const formData = reactive({
  id: null,
  name: '',
  type: '',
  code: '',
  value: '',
  sort: 0,
  remark: ''
})

const formRules = {
  name: [
    { required: true, message: '请输入字典名称', trigger: 'blur' }
  ],
  type: [
    { required: true, message: '请输入字典类型', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入字典码', trigger: 'blur' }
  ],
  value: [
    { required: true, message: '请输入字典值', trigger: 'blur' }
  ]
}

// 过滤数据
const filteredData = computed(() => {
  if (!searchText.value) return tableData.value
  return tableData.value.filter(item =>
    item.name.includes(searchText.value) ||
    item.type.includes(searchText.value)
  )
})

// 获取数据
const fetchData = async () => {
  loading.value = true
  try {
    const res = await getDictList()
    tableData.value = res.data
  } catch (error) {
    console.error('获取数据失败:', error)
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

// 新增
const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增字典'
  Object.assign(formData, {
    id: null,
    name: '',
    type: '',
    code: '',
    value: '',
    sort: 0,
    remark: ''
  })
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑字典'
  Object.assign(formData, row)
  dialogVisible.value = true
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该字典吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteDict(row.id)
      ElMessage.success('删除成功')
      fetchData()
    } catch (error) {
      console.error('删除失败:', error)
    }
  }).catch(() => {})
}

// 提交
const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        if (isEdit.value) {
          await updateDict(formData)
        } else {
          await addDict(formData)
        }
        ElMessage.success(isEdit.value ? '更新成功' : '新增成功')
        dialogVisible.value = false
        fetchData()
      } catch (error) {
        console.error('提交失败:', error)
      } finally {
        submitLoading.value = false
      }
    }
  })
}

// 关闭对话框
const handleDialogClose = () => {
  formRef.value?.resetFields()
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.page-container {
  padding: 24px;
}

.page-header {
  margin-bottom: 24px;
}

.page-header h1 {
  font-size: 24px;
  font-weight: 600;
  color: #1F2937;
  margin: 0;
  display: flex;
  align-items: center;
  gap: 8px;
}

.page-header h1::before {
  content: '';
  width: 4px;
  height: 24px;
  background: linear-gradient(135deg, #2563EB 0%, #1D4ED8 100%);
  border-radius: 2px;
}

.search-card {
  margin-bottom: 24px;
}

.search-card :deep(.el-card__body) {
  padding: 24px;
}

.table-card :deep(.el-card__header) {
  padding: 24px;
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header span {
  font-size: 16px;
  font-weight: 600;
  color: #1F2937;
}

.action-buttons {
  display: flex;
  gap: 8px;
  align-items: center;
}

.action-buttons .el-button {
  padding: 4px 8px;
  margin: 0;
}

:deep(.el-table .el-table__header th) {
  background-color: #F3F4F6;
  color: #1F2937;
  font-weight: 600;
}

:deep(.el-table .el-table__row) {
  transition: all 150ms;
}

:deep(.el-table .el-table__row:hover) {
  background-color: rgba(37, 99, 235, 0.03);
}

:deep(.el-button) {
  border-radius: 4px;
  font-weight: 500;
  transition: all 150ms;
}

:deep(.el-button.el-button--primary) {
  background: linear-gradient(135deg, #2563EB 0%, #1D4ED8 100%);
  border: none;
}

:deep(.el-button.el-button--primary:hover) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(37, 99, 235, 0.3);
}

:deep(.el-dialog) {
  border-radius: 8px;
}

:deep(.el-dialog .el-dialog__header) {
  padding: 24px;
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
  border-bottom: 1px solid #E5E7EB;
  margin-right: 0;
}

:deep(.el-dialog .el-dialog__title) {
  font-size: 18px;
  font-weight: 600;
  color: #1F2937;
}

:deep(.el-dialog .el-dialog__body) {
  padding: 32px;
}

:deep(.el-dialog .el-dialog__footer) {
  padding: 24px 32px;
  border-top: 1px solid #E5E7EB;
}
</style>
