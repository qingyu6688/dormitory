<template>
  <div class="page-container">
    <div class="page-header">
      <h1>菜单管理</h1>
    </div>

    <!-- 搜索区 -->
    <el-card class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="菜单名称">
          <el-input v-model="searchForm.title" placeholder="请输入菜单名称" clearable />
        </el-form-item>
        <el-form-item label="菜单类型">
          <el-select v-model="searchForm.type" placeholder="请选择" clearable>
            <el-option label="菜单" :value="1" />
            <el-option label="按钮" :value="2" />
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

    <!-- 表格区 -->
    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span>菜单列表</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增菜单
          </el-button>
        </div>
      </template>

      <div class="table-wrapper">
        <el-table
          :data="tableData"
          v-loading="loading"
          border
        >
          <el-table-column prop="title" label="菜单名称" width="200" />
          <el-table-column prop="icon" label="图标" width="80">
            <template #default="{ row }">
              <el-icon v-if="row.icon" :size="18">
                <component :is="row.icon" />
              </el-icon>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="路由名称" />
          <el-table-column prop="path" label="路由路径" />
          <el-table-column prop="component" label="组件路径" show-overflow-tooltip />
          <el-table-column prop="type" label="类型" width="80">
            <template #default="{ row }">
              <el-tag v-if="row.type === 1" type="primary" size="small">菜单</el-tag>
              <el-tag v-else type="info" size="small">按钮</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="sortNum" label="排序" width="80" />
          <el-table-column prop="permKey" label="权限标识" show-overflow-tooltip />
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="{ row }">
              <div class="action-buttons">
                <el-button type="primary" link size="small" @click="handleEdit(row)">
                  编辑
                </el-button>
                <el-button type="success" link size="small" @click="handleAdd(row)">
                  新增
                </el-button>
                <el-button type="danger" link size="small" @click="handleDelete(row)">
                  删除
                </el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <el-pagination
        v-model:current-page="pagination.pageNum"
        v-model:page-size="pagination.pageSize"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handlePageSizeChange"
        @current-change="handlePageChange"
        class="table-pagination"
      />
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="700px"
      @close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="菜单类型" prop="type">
              <el-radio-group v-model="formData.type">
                <el-radio :label="1">菜单</el-radio>
                <el-radio :label="2">按钮</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="上级菜单" prop="parentId">
              <el-tree-select
                v-model="formData.parentId"
                :data="menuTreeData"
                :props="treeProps"
                check-strictly
                placeholder="请选择上级菜单"
                clearable
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="菜单名称" prop="title">
              <el-input v-model="formData.title" placeholder="请输入菜单名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="路由名称" prop="name">
              <el-input v-model="formData.name" placeholder="如：User" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20" v-if="formData.type === 1">
          <el-col :span="12">
            <el-form-item label="路由路径" prop="path">
              <el-input v-model="formData.path" placeholder="如：/system/user" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="组件路径" prop="component">
              <el-input v-model="formData.component" placeholder="如：system/user/index" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12" v-if="formData.type === 1">
            <el-form-item label="菜单图标" prop="icon">
              <el-select v-model="formData.icon" placeholder="请选择图标" filterable>
                <el-option
                  v-for="icon in iconList"
                  :key="icon"
                  :label="icon"
                  :value="icon"
                >
                  <div style="display: flex; align-items: center; gap: 8px;">
                    <el-icon><component :is="icon" /></el-icon>
                    <span>{{ icon }}</span>
                  </div>
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="排序" prop="sortNum">
              <el-input-number v-model="formData.sortNum" :min="0" :max="9999" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="权限标识" prop="permKey">
          <el-input v-model="formData.permKey" placeholder="如：system:user:list" />
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
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Search, Refresh, Plus, Edit, Delete,
  HomeFilled, User, Setting, DataLine, Folder, Collection,
  Menu, Avatar, Document, View, MessageBox, Lock, Key
} from '@element-plus/icons-vue'
import { getMenuPage, getMenuList, addMenu, updateMenu, deleteMenu } from '@/api/menu'

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)
const rawMenuList = ref([])

const searchForm = reactive({
  title: '',
  type: null
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const formData = reactive({
  id: null,
  parentId: null,
  name: '',
  title: '',
  path: '',
  component: '',
  icon: '',
  sortNum: 0,
  type: 1,
  permKey: ''
})

const formRules = {
  title: [
    { required: true, message: '请输入菜单名称', trigger: 'blur' }
  ],
  type: [
    { required: true, message: '请选择菜单类型', trigger: 'change' }
  ]
}

const treeProps = {
  children: 'children',
  label: 'title',
  value: 'id'
}

// 图标列表
const iconList = [
  'HomeFilled', 'User', 'Setting', 'DataLine', 'Folder', 'Collection',
  'Menu', 'Avatar', 'Document', 'View', 'MessageBox', 'Lock', 'Key',
  'Search', 'Edit', 'Delete', 'Plus', 'Refresh'
]

// 菜单树数据（用于选择上级菜单）
const menuTreeData = computed(() => {
  return [
    { id: null, title: '顶级菜单', children: buildTree(rawMenuList.value) }
  ]
})

// 查询
const handleSearch = () => {
  pagination.pageNum = 1
  fetchData()
}

// 重置
const handleReset = () => {
  Object.assign(searchForm, {
    title: '',
    type: null
  })
  handleSearch()
}

// 获取数据
const fetchData = async () => {
  loading.value = true
  try {
    const res = await getMenuPage({
      ...searchForm,
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    })
    tableData.value = res.data?.records || []
    pagination.total = res.data?.total || 0
  } catch (error) {
    console.error('获取数据失败:', error)
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

const loadMenuTree = async () => {
  try {
    const res = await getMenuList()
    rawMenuList.value = res.data || []
  } catch (error) {
    console.error('获取菜单树失败:', error)
  }
}

// 构建树形结构
const buildTree = (data) => {
  const map = {}
  const tree = []
  
  // 创建映射
  data.forEach(item => {
    map[item.id] = { ...item, children: [] }
  })
  
  // 构建树
  data.forEach(item => {
    if (item.parentId) {
      if (map[item.parentId]) {
        map[item.parentId].children.push(map[item.id])
      }
    } else {
      tree.push(map[item.id])
    }
  })
  
  return tree
}

// 新增
const handleAdd = (row) => {
  isEdit.value = false
  dialogTitle.value = row ? `新增子菜单 - ${row.title}` : '新增菜单'
  Object.assign(formData, {
    id: null,
    parentId: row ? row.id : null,
    name: '',
    title: '',
    path: '',
    component: '',
    icon: '',
    sortNum: 0,
    type: 1,
    permKey: ''
  })
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑菜单'
  Object.assign(formData, {
    id: row.id,
    parentId: row.parentId,
    name: row.name,
    title: row.title,
    path: row.path,
    component: row.component,
    icon: row.icon,
    sortNum: row.sortNum,
    type: row.type,
    permKey: row.permKey
  })
  dialogVisible.value = true
}

// 提交
const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        if (isEdit.value) {
          await updateMenu(formData)
          ElMessage.success('更新成功')
        } else {
          await addMenu(formData)
          ElMessage.success('新增成功')
        }
        dialogVisible.value = false
        fetchData()
        loadMenuTree()
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

// 删除
const handleDelete = (row) => {
  if (hasChildren(row.id)) {
    ElMessage.warning('该菜单下存在子菜单，无法删除')
    return
  }
  
  ElMessageBox.confirm('确定要删除该菜单吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteMenu(row.id)
      ElMessage.success('删除成功')
      fetchData()
      loadMenuTree()
    } catch (error) {
      console.error('删除失败:', error)
    }
  }).catch(() => {})
}

const hasChildren = (id) => {
  return rawMenuList.value.some(item => item.parentId === id)
}

const handlePageSizeChange = (size) => {
  pagination.pageSize = size
  pagination.pageNum = 1
  fetchData()
}

const handlePageChange = (page) => {
  pagination.pageNum = page
  fetchData()
}

onMounted(() => {
  fetchData()
  loadMenuTree()
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

.search-card :deep(.el-form-item) {
  margin-bottom: 0;
}

.search-card :deep(.el-input) {
  width: 200px;
}

.search-card :deep(.el-select) {
  width: 150px;
}

.search-card :deep(.el-button) {
  margin-left: 8px;
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

.table-card :deep(.el-table .el-table__header th) {
  background-color: #F3F4F6;
  color: #1F2937;
  font-weight: 600;
}

.table-card :deep(.el-table .el-table__row) {
  transition: all 150ms;
}

.table-card :deep(.el-table .el-table__row:hover) {
  background-color: rgba(37, 99, 235, 0.03);
}

.table-wrapper {
  max-height: calc(100vh - 320px);
  overflow-y: auto;
  border-radius: 8px;
  border: 1px solid #E5E7EB;
}

.table-wrapper :deep(.el-table) {
  min-width: 960px;
}

.table-pagination {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
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

/* 对话框样式 */
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

:deep(.el-dialog .el-form .el-form-item) {
  margin-bottom: 24px;
}

:deep(.el-dialog .el-form .el-form-item__label) {
  font-weight: 500;
  color: #1F2937;
}

:deep(.el-dialog .el-form .el-input),
:deep(.el-dialog .el-form .el-select),
:deep(.el-dialog .el-form .el-tree-select) {
  width: 100%;
}

/* 按钮样式 */
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

:deep(.el-button.is-link.el-button--primary:hover) {
  color: #1D4ED8;
  transform: none;
  box-shadow: none;
}

:deep(.el-button.is-link.el-button--success:hover) {
  color: #059669;
}

:deep(.el-button.is-link.el-button--danger:hover) {
  color: #DC2626;
}

/* 响应式 */
@media (max-width: 768px) {
  .page-container {
    padding: 16px;
  }

  .search-card :deep(.el-form .el-form-item) {
    display: block;
    margin-bottom: 16px;
  }

  .search-card :deep(.el-input),
  .search-card :deep(.el-select) {
    width: 100%;
  }

  .table-card :deep(.el-table) {
    font-size: 12px;
  }

  .table-wrapper {
    max-height: calc(100vh - 280px);
  }
}
</style>
