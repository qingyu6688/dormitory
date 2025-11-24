<template>
  <div class="page-container">
    <div class="page-header">
      <h1>操作日志</h1>
    </div>

    <el-card class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="用户名">
          <el-input v-model="searchForm.username" placeholder="请输入用户名" clearable />
        </el-form-item>
        <el-form-item label="操作类型">
          <el-input v-model="searchForm.operation" placeholder="请输入操作类型" clearable />
        </el-form-item>
        <el-form-item label="操作状态">
          <el-select v-model="searchForm.status" placeholder="全部状态" clearable style="width: 120px">
            <el-option label="✓ 成功" :value="1">
              <span style="color: #67c23a">✓ 成功</span>
            </el-option>
            <el-option label="✗ 失败" :value="0">
              <span style="color: #f56c6c">✗ 失败</span>
            </el-option>
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
          <span>日志列表</span>
          <div>
            <el-button type="danger" plain @click="handleBatchDelete" :disabled="selectedIds.length === 0">
              <el-icon><Delete /></el-icon>
              批量删除
            </el-button>
            <el-button type="danger" @click="handleClear">
              <el-icon><Delete /></el-icon>
              清空日志
            </el-button>
          </div>
        </div>
      </template>

      <el-table 
        :data="tableData" 
        v-loading="loading"
        stripe
        @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="username" label="用户名" min-width="100" />
        <el-table-column prop="operation" label="操作类型" min-width="100" />
        <el-table-column prop="method" label="请求方法" min-width="200" show-overflow-tooltip />
        <el-table-column prop="time" label="执行时长" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.time > 1000 ? 'danger' : row.time > 500 ? 'warning' : 'success'" size="small">
              {{ row.time }}ms
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="ipAddress" label="IP地址" min-width="130" />
        <el-table-column prop="location" label="操作地点" min-width="100" />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
              {{ row.status === 1 ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="操作时间" min-width="160" />
        <el-table-column label="操作" width="150" fixed="right" align="center">
          <template #default="{ row }">
            <div class="action-buttons">
              <el-button type="primary" link size="small" @click="handleViewDetail(row)">
                <el-icon><View /></el-icon>
                <span>详情</span>
              </el-button>
              <el-button type="danger" link size="small" @click="handleDelete(row.id)">
                <el-icon><Delete /></el-icon>
                <span>删除</span>
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
        @size-change="getOperationLogList"
        @current-change="getOperationLogList"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>

    <!-- 详情对话框 -->
    <el-dialog v-model="detailVisible" title="操作日志详情" width="800px" :close-on-click-modal="false">
      <el-descriptions :column="2" border size="default">
        <el-descriptions-item label="ID" label-align="right">{{ detailData.id }}</el-descriptions-item>
        <el-descriptions-item label="用户名" label-align="right">{{ detailData.username }}</el-descriptions-item>
        <el-descriptions-item label="操作类型" label-align="right">{{ detailData.operation }}</el-descriptions-item>
        <el-descriptions-item label="执行时长" label-align="right">
          <el-tag :type="detailData.time > 1000 ? 'danger' : detailData.time > 500 ? 'warning' : 'success'" size="small">
            {{ detailData.time }}ms
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="IP地址" label-align="right">{{ detailData.ipAddress }}</el-descriptions-item>
        <el-descriptions-item label="操作地点" label-align="right">{{ detailData.location }}</el-descriptions-item>
        <el-descriptions-item label="操作状态" label-align="right">
          <el-tag :type="detailData.status === 1 ? 'success' : 'danger'" size="small">
            {{ detailData.status === 1 ? '成功' : '失败' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="操作时间" label-align="right">{{ detailData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="请求方法" :span="2" label-align="right">
          <code class="method-code">{{ detailData.method }}</code>
        </el-descriptions-item>
        <el-descriptions-item label="请求参数" :span="2" label-align="right">
          <pre class="json-pre">{{ formatJson(detailData.params) }}</pre>
        </el-descriptions-item>
        <el-descriptions-item label="错误信息" :span="2" label-align="right" v-if="detailData.errorMsg">
          <div class="error-text">{{ detailData.errorMsg }}</div>
        </el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Delete, View } from '@element-plus/icons-vue'
import request from '@/utils/request'

// 搜索表单
const searchForm = reactive({
  username: '',
  operation: '',
  status: null
})

// 表格数据
const tableData = ref([])
const loading = ref(false)
const selectedIds = ref([])

// 分页
const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 详情对话框
const detailVisible = ref(false)
const detailData = ref({})

// 获取操作日志列表
const getOperationLogList = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      ...searchForm
    }
    const res = await request.get('/operationLog/page', { params })
    tableData.value = res.data.records
    pagination.total = res.data.total
  } catch (error) {
    ElMessage.error('获取操作日志失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.pageNum = 1
  getOperationLogList()
}

// 重置
const handleReset = () => {
  searchForm.username = ''
  searchForm.operation = ''
  searchForm.status = null
  pagination.pageNum = 1
  getOperationLogList()
}

// 选择变化
const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.id)
}

// 查看详情
const handleViewDetail = (row) => {
  detailData.value = { ...row }
  detailVisible.value = true
}

// 格式化JSON
const formatJson = (jsonStr) => {
  if (!jsonStr) return '-'
  try {
    const obj = JSON.parse(jsonStr)
    return JSON.stringify(obj, null, 2)
  } catch {
    return jsonStr
  }
}

// 删除
const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这条操作日志吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.delete(`/operationLog/${id}`)
    ElMessage.success('删除成功')
    getOperationLogList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 批量删除
const handleBatchDelete = async () => {
  try {
    await ElMessageBox.confirm(`确定要删除选中的 ${selectedIds.value.length} 条操作日志吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.delete('/operationLog/batch', { data: selectedIds.value })
    ElMessage.success('批量删除成功')
    getOperationLogList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('批量删除失败')
    }
  }
}

// 清空日志
const handleClear = async () => {
  try {
    await ElMessageBox.confirm('确定要清空所有操作日志吗？此操作不可恢复！', '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'error'
    })
    await request.delete('/operationLog/clear')
    ElMessage.success('清空成功')
    getOperationLogList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('清空失败')
    }
  }
}


// 初始化
onMounted(() => {
  getOperationLogList()
})
</script>

<style scoped>
.page-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h1 {
  font-size: 24px;
  font-weight: 500;
  color: #303133;
  margin: 0;
}

.search-card {
  margin-bottom: 20px;
}

.table-card {
  min-height: 600px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header span {
  font-size: 16px;
  font-weight: 500;
}

.action-buttons {
  display: flex;
  gap: 8px;
  justify-content: center;
}

.error-text {
  color: #f56c6c;
  word-break: break-all;
}

.json-pre {
  background-color: #f5f7fa;
  padding: 12px;
  border-radius: 4px;
  overflow-x: auto;
  font-size: 13px;
  line-height: 1.6;
  margin: 0;
  max-height: 300px;
  overflow-y: auto;
}

.method-code {
  background-color: #f5f7fa;
  padding: 4px 8px;
  border-radius: 3px;
  font-size: 13px;
  color: #606266;
  font-family: 'Courier New', monospace;
}
</style>
