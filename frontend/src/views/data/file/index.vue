<template>
  <div class="page-container">
    <div class="page-header">
      <h1>文件管理</h1>
    </div>

    <!-- 搜索区 -->
    <el-card class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="文件名称">
          <el-input v-model="searchForm.fileName" placeholder="请输入文件名称" clearable />
        </el-form-item>
        <el-form-item label="文件类型">
          <el-select v-model="searchForm.fileType" placeholder="请选择" clearable>
            <el-option label="图片" value="image" />
            <el-option label="文档" value="document" />
            <el-option label="视频" value="video" />
            <el-option label="其他" value="other" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon>
              <Search />
            </el-icon>
            查询
          </el-button>
          <el-button @click="handleReset">
            <el-icon>
              <Refresh />
            </el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格区 -->
    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span>文件列表</span>
          <el-upload :action="uploadUrl" :headers="uploadHeaders" :on-success="handleUploadSuccess"
            :on-error="handleUploadError" :show-file-list="false" :before-upload="beforeUpload">
            <el-button type="primary">
              <el-icon>
                <Upload />
              </el-icon>
              上传文件
            </el-button>
          </el-upload>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="预览" width="100">
          <template #default="{ row }">
            <el-image v-if="isImage(row.type)" :src="row.url" :preview-src-list="[row.url]" fit="cover"
              style="width: 60px; height: 60px; border-radius: 4px;" />
            <el-icon v-else :size="40" color="#909399">
              <Document />
            </el-icon>
          </template>
        </el-table-column>
        <el-table-column prop="fileName" label="文件名称" show-overflow-tooltip />
        <el-table-column prop="type" label="文件类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getFileTypeTag(row.type)" size="small">
              {{ row.type || '未知' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="size" label="文件大小" width="120">
          <template #default="{ row }">
            {{ formatFileSize(row.size) }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="上传时间" width="180" />
        <el-table-column prop="enable" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.enable === 1 ? 'success' : 'info'" size="small">
              {{ row.enable === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <div class="action-buttons">
              <el-button type="primary" link size="small" @click="handlePreview(row)">
                预览
              </el-button>
              <el-button type="success" link size="small" @click="handleDownload(row)">
                下载
              </el-button>
              <el-button type="danger" link size="small" @click="handleDelete(row)">
                删除
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination v-model:current-page="pagination.pageNum" v-model:page-size="pagination.pageSize"
        :total="pagination.total" :page-sizes="[10, 20, 50, 100]" layout="total, sizes, prev, pager, next, jumper"
        @size-change="fetchData" @current-change="fetchData" style="margin-top: 20px; justify-content: flex-end" />
    </el-card>

    <!-- 预览对话框 -->
    <el-dialog v-model="previewVisible" title="文件预览" width="800px">
      <div class="preview-container">
        <el-image v-if="isImage(currentFile.type)" :src="currentFile.url" fit="contain"
          style="width: 100%; max-height: 600px;" />
        <div v-else class="file-info">
          <el-icon :size="80" color="#909399">
            <Document />
          </el-icon>
          <p>此文件类型不支持预览</p>
          <el-button type="primary" @click="handleDownload(currentFile)">
            下载文件
          </el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Upload, Document } from '@element-plus/icons-vue'
import { getFilePage, deleteFile } from '@/api/file'

const loading = ref(false)
const tableData = ref([])
const previewVisible = ref(false)
const currentFile = ref({})

const searchForm = reactive({
  fileName: '',
  type: ''
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 上传配置
const uploadUrl = computed(() => {
  return '/api/file/upload'
})

const uploadHeaders = computed(() => {
  const token = localStorage.getItem('token')
  return {
    Authorization: token ? `Bearer ${token}` : ''
  }
})

// 查询
const handleSearch = () => {
  pagination.pageNum = 1
  fetchData()
}

// 重置
const handleReset = () => {
  Object.assign(searchForm, {
    fileName: '',
    type: ''
  })
  handleSearch()
}

// 获取数据
const fetchData = async () => {
  loading.value = true
  try {
    const res = await getFilePage({
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

// 文件上传前验证
const beforeUpload = (file) => {
  const maxSize = 50 * 1024 * 1024 // 50MB
  if (file.size > maxSize) {
    ElMessage.error('文件大小不能超过50MB')
    return false
  }
  return true
}

// 上传成功
const handleUploadSuccess = (response) => {
  ElMessage.success('上传成功')
  fetchData()
}

// 上传失败
const handleUploadError = (error) => {
  ElMessage.error('上传失败')
  console.error(error)
}

// 预览
const handlePreview = (row) => {
  currentFile.value = row
  previewVisible.value = true
}

// 下载
const handleDownload = (row) => {
  // 使用后端下载接口
  const token = localStorage.getItem('token')
  const url = `/api/file/download/${row.id}`
  
  // 创建一个隐藏的a标签进行下载
  const link = document.createElement('a')
  link.href = url
  link.download = row.fileName
  
  // 如果需要携带token，使用fetch下载
  fetch(url, {
    method: 'GET',
    headers: {
      'Authorization': token ? `Bearer ${token}` : ''
    }
  })
  .then(response => {
    if (!response.ok) {
      throw new Error('下载失败')
    }
    return response.blob()
  })
  .then(blob => {
    // 创建下载链接
    const downloadUrl = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = downloadUrl
    link.download = row.fileName
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(downloadUrl)
    ElMessage.success('下载成功')
  })
  .catch(error => {
    console.error('下载失败:', error)
    ElMessage.error('下载失败')
  })
}

// 删除
const handleDelete = async (row) => {
  ElMessageBox.confirm('确定要删除该文件吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteFile(row.id)
      ElMessage.success('删除成功')
      fetchData()
    } catch (error) {
      console.error('删除失败:', error)
    }
  }).catch(() => { })
}

// 判断是否为图片
const isImage = (fileType) => {
  if (!fileType) return false
  const imageTypes = ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp', 'svg']
  return imageTypes.includes(fileType.toLowerCase())
}

// 获取文件类型标签
const getFileTypeTag = (fileType) => {
  if (!fileType) return 'info'
  const type = fileType.toLowerCase()
  if (['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp', 'svg'].includes(type)) return 'success'
  if (type === 'pdf') return 'danger'
  if (['doc', 'docx'].includes(type)) return 'primary'
  if (['xls', 'xlsx'].includes(type)) return 'warning'
  return 'info'
}

// 格式化文件大小（数据库存储的是KB）
const formatFileSize = (kb) => {
  if (!kb || kb === 0) return '0 KB'
  if (kb < 1024) return kb.toFixed(2) + ' KB'
  const mb = kb / 1024
  if (mb < 1024) return mb.toFixed(2) + ' MB'
  const gb = mb / 1024
  return gb.toFixed(2) + ' GB'
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

.search-card :deep(.el-select) {
  width: 150px;
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

.action-buttons {
  display: flex;
  gap: 8px;
  align-items: center;
}

.action-buttons .el-button {
  padding: 4px 8px;
  margin: 0;
}

.preview-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 300px;
}

.file-info {
  text-align: center;
}

.file-info p {
  margin: 20px 0;
  color: #909399;
  font-size: 14px;
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
</style>
