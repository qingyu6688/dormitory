<template>
  <div class="page-container">
    <div class="page-header">
      <h1>角色管理</h1>
    </div>

    <!-- 搜索区 -->
    <el-card class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="角色名称">
          <el-input v-model="searchForm.name" placeholder="请输入角色名称" clearable />
        </el-form-item>
        <el-form-item label="角色编码">
          <el-input v-model="searchForm.code" placeholder="请输入角色编码" clearable />
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
          <span>角色列表</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增角色
          </el-button>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="角色名称" />
        <el-table-column prop="code" label="角色编码" />
        <el-table-column prop="description" label="描述" show-overflow-tooltip />
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <div class="action-buttons">
              <el-button type="primary" link size="small" @click="handleEdit(row)">
                编辑
              </el-button>
              <el-button type="success" link size="small" @click="handlePermission(row)">
                权限
              </el-button>
              <el-button type="danger" link size="small" @click="handleDelete(row)">
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

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="80px"
      >
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色编码" prop="code">
          <el-input
            v-model="formData.code"
            placeholder="请输入角色编码（如：ROLE_ADMIN）"
            :disabled="isEdit"
          />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="formData.description"
            type="textarea"
            :rows="3"
            placeholder="请输入角色描述"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
          确定
        </el-button>
      </template>
    </el-dialog>

    <!-- 权限分配对话框 -->
    <el-dialog
      v-model="permissionDialogVisible"
      title="分配权限"
      width="600px"
      @close="handlePermissionDialogClose"
    >
      <el-alert
        title="提示"
        type="info"
        :closable="false"
        style="margin-bottom: 20px"
      >
        为角色 <strong>{{ currentRole.name }}</strong> 分配菜单权限
      </el-alert>

      <el-tree
        ref="treeRef"
        :data="menuTree"
        :props="treeProps"
        show-checkbox
        node-key="id"
        :default-checked-keys="checkedMenuIds"
        :default-expand-all="true"
      >
        <template #default="{ node, data }">
          <span class="custom-tree-node">
            <el-icon v-if="data.icon">
              <component :is="data.icon" />
            </el-icon>
            <span>{{ node.label }}</span>
          </span>
        </template>
      </el-tree>

      <template #footer>
        <el-button @click="permissionDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handlePermissionSubmit" :loading="permissionLoading">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Edit, Delete, Lock } from '@element-plus/icons-vue'
import { getRolePage, addRole, updateRole, deleteRole, getRoleMenus, updateRoleMenus } from '@/api/role'
import { getMenuList } from '@/api/menu'


const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)
const permissionDialogVisible = ref(false)
const permissionLoading = ref(false)
const treeRef = ref(null)
const currentRole = ref({})
const menuTree = ref([])
const checkedMenuIds = ref([])

const searchForm = reactive({
  name: '',
  code: ''
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const formData = reactive({
  id: null,
  name: '',
  code: '',
  description: ''
})

const formRules = {
  name: [
    { required: true, message: '请输入角色名称', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入角色编码', trigger: 'blur' },
    { pattern: /^ROLE_[A-Z_]+$/, message: '编码格式：ROLE_XXX（大写字母和下划线）', trigger: 'blur' }
  ]
}

const treeProps = {
  children: 'children',
  label: 'title'
}

// 查询
const handleSearch = () => {
  pagination.pageNum = 1
  fetchData()
}

// 重置
const handleReset = () => {
  Object.assign(searchForm, {
    name: '',
    code: ''
  })
  handleSearch()
}

// 获取数据
const fetchData = async () => {
  loading.value = true
  try {
    const res = await getRolePage({
      ...searchForm,
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    })
    tableData.value = res.data.records
    pagination.total = res.data.total
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
  dialogTitle.value = '新增角色'
  Object.assign(formData, {
    id: null,
    name: '',
    code: '',
    description: ''
  })
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑角色'
  Object.assign(formData, {
    id: row.id,
    name: row.name,
    code: row.code,
    description: row.description
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
          await updateRole(formData)
          ElMessage.success('更新成功')
        } else {
          await addRole(formData)
          ElMessage.success('新增成功')
        }
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

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该角色吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteRole(row.id)
      ElMessage.success('删除成功')
      fetchData()
    } catch (error) {
      console.error('删除失败:', error)
    }
  }).catch(() => {})
}

// 权限分配
const handlePermission = async (row) => {
  currentRole.value = row
  permissionDialogVisible.value = true
  
  try {
    // 获取所有菜单
    const menuRes = await getMenuList()
    menuTree.value = buildMenuTree(menuRes.data)
    
    // 获取角色已有权限
    const roleMenuRes = await getRoleMenus(row.id)
    checkedMenuIds.value = roleMenuRes.data.map(item => item.id)
  } catch (error) {
    console.error('获取权限数据失败:', error)
    ElMessage.error('获取权限数据失败')
  }
}

// 构建菜单树
const buildMenuTree = (menus) => {
  const map = {}
  const tree = []
  
  // 创建映射
  menus.forEach(menu => {
    map[menu.id] = { ...menu, children: [] }
  })
  
  // 构建树
  menus.forEach(menu => {
    if (menu.parentId) {
      if (map[menu.parentId]) {
        map[menu.parentId].children.push(map[menu.id])
      }
    } else {
      tree.push(map[menu.id])
    }
  })
  
  return tree
}

// 提交权限
const handlePermissionSubmit = async () => {
  permissionLoading.value = true
  try {
    const checkedKeys = treeRef.value.getCheckedKeys()
    const halfCheckedKeys = treeRef.value.getHalfCheckedKeys()
    const menuIds = [...checkedKeys, ...halfCheckedKeys]
    
    await updateRoleMenus(currentRole.value.id, menuIds)
    ElMessage.success('权限分配成功')
    permissionDialogVisible.value = false
  } catch (error) {
    console.error('权限分配失败:', error)
    ElMessage.error('权限分配失败')
  } finally {
    permissionLoading.value = false
  }
}

// 关闭权限对话框
const handlePermissionDialogClose = () => {
  checkedMenuIds.value = []
  menuTree.value = []
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

.search-card :deep(.el-form-item) {
  margin-bottom: 0;
}

.search-card :deep(.el-input) {
  width: 200px;
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

.action-buttons {
  display: flex;
  gap: 8px;
  align-items: center;
}

.action-buttons .el-button {
  padding: 4px 8px;
  margin: 0;
}

.table-card :deep(.el-pagination) {
  padding: 24px 0;
  display: flex;
  justify-content: flex-end;
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
:deep(.el-dialog .el-form .el-select) {
  width: 100%;
}

/* 树形控件样式 */
.custom-tree-node {
  display: flex;
  align-items: center;
  gap: 8px;
}

:deep(.el-tree) {
  max-height: 400px;
  overflow-y: auto;
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

  .search-card :deep(.el-form .el-input) {
    width: 100%;
  }

  .table-card :deep(.el-table) {
    font-size: 12px;
  }
}
</style>
