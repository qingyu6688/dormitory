<template>
  <div class="home">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>欢迎使用管理系统</span>
        </div>
      </template>
      <div class="content">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-statistic title="用户总数" :value="268500">
              <template #prefix>
                <el-icon><User /></el-icon>
              </template>
            </el-statistic>
          </el-col>
          <el-col :span="6">
            <el-statistic title="今日访问" :value="1234">
              <template #prefix>
                <el-icon><View /></el-icon>
              </template>
            </el-statistic>
          </el-col>
          <el-col :span="6">
            <el-statistic title="订单数量" :value="5678">
              <template #prefix>
                <el-icon><Document /></el-icon>
              </template>
            </el-statistic>
          </el-col>
          <el-col :span="6">
            <el-statistic title="销售额" :value="98765">
              <template #prefix>
                <el-icon><Money /></el-icon>
              </template>
            </el-statistic>
          </el-col>
        </el-row>
        
        <el-divider />
        
        <div class="test-section">
          <h3>接口测试</h3>
          <el-button type="primary" @click="testApi">测试后端接口</el-button>
          <div v-if="apiResult" class="api-result">
            <p>{{ apiResult }}</p>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'

const apiResult = ref('')

const testApi = async () => {
  try {
    const res = await request.get('/test/hello')
    apiResult.value = res.data
    ElMessage.success('接口调用成功')
  } catch (error) {
    ElMessage.error('接口调用失败')
  }
}
</script>

<style scoped>
.home {
  height: 100%;
}

.card-header {
  font-size: 18px;
  font-weight: bold;
}

.content {
  padding: 20px 0;
}

.test-section {
  margin-top: 20px;
}

.test-section h3 {
  margin-bottom: 15px;
}

.api-result {
  margin-top: 15px;
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 4px;
}
</style>
