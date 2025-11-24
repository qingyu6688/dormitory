<template>
  <div class="page-container">
    <div class="page-header">
      <h1>水电费管理</h1>
    </div>

    <el-card class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="房间号">
          <el-input v-model="searchForm.roomNo" placeholder="请输入房间号" clearable style="width: 160px" />
        </el-form-item>
        <el-form-item label="账单月份">
          <el-input v-model="searchForm.billMonth" placeholder="YYYY-MM" clearable />
        </el-form-item>
        <el-form-item label="缴费状态">
          <el-select v-model="searchForm.paymentStatus" placeholder="请选择" clearable style="width: 160px">
            <el-option label="未缴" :value="1" />
            <el-option label="已缴" :value="2" />
            <el-option label="逾期" :value="3" />
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
          <span>水电费账单</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增账单
          </el-button>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column prop="roomNo" label="房间号" min-width="120" />
        <el-table-column prop="billMonth" label="账单月份" min-width="110" />
        <el-table-column prop="electricUsage" label="用电量(kWh)" min-width="120" />
        <el-table-column prop="waterUsage" label="用水量(吨)" min-width="120" />
        <el-table-column prop="electricAmount" label="电费(元)" min-width="100" />
        <el-table-column prop="waterAmount" label="水费(元)" min-width="100" />
        <el-table-column prop="totalAmount" label="总费用(元)" min-width="110" />
        <el-table-column prop="actualAmount" label="应缴金额(元)" min-width="120" />
        <el-table-column prop="paymentStatus" label="缴费状态" min-width="100">
          <template #default="{ row }">
            <el-tag :type="paymentTag(row.paymentStatus)">
              {{ paymentText(row.paymentStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="paymentTime" label="缴费时间" min-width="160" />
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="800px" @close="handleDialogClose">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="120px">
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
            <el-form-item label="账单月份" prop="billMonth">
              <el-date-picker
                v-model="formData.billMonth"
                type="month"
                value-format="YYYY-MM"
                placeholder="选择月份"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="电表起始">
              <el-input-number v-model="formData.electricStart" :min="0" :precision="1" :step="1" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="电表结束">
              <el-input-number v-model="formData.electricEnd" :min="0" :precision="1" :step="1" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="水表起始">
              <el-input-number v-model="formData.waterStart" :min="0" :precision="1" :step="1" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="水表结束">
              <el-input-number v-model="formData.waterEnd" :min="0" :precision="1" :step="1" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="电价(元)">
              <el-input-number v-model="formData.electricPrice" :min="0" :precision="2" :step="0.1" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="水价(元)">
              <el-input-number v-model="formData.waterPrice" :min="0" :precision="2" :step="0.1" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="用电量">
              <el-input-number v-model="formData.electricUsage" :min="0" :precision="1" :step="1" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用水量">
              <el-input-number v-model="formData.waterUsage" :min="0" :precision="1" :step="1" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="电费金额">
              <el-input-number v-model="formData.electricAmount" :min="0" :precision="2" :step="10" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="水费金额">
              <el-input-number v-model="formData.waterAmount" :min="0" :precision="2" :step="10" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="总费用" prop="totalAmount">
              <el-input-number v-model="formData.totalAmount" :min="0" :precision="2" :step="10" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="应缴金额" prop="actualAmount">
              <el-input-number v-model="formData.actualAmount" :min="0" :precision="2" :step="10" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="缴费状态" prop="paymentStatus">
              <el-select v-model="formData.paymentStatus" placeholder="请选择">
                <el-option label="未缴" :value="1" />
                <el-option label="已缴" :value="2" />
                <el-option label="逾期" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="缴费方式">
              <el-input v-model="formData.paymentMethod" placeholder="如 线上支付" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="缴费时间">
          <el-date-picker
            v-model="formData.paymentTime"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="选择时间"
            style="width: 100%"
          />
        </el-form-item>
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
import { getUtilityBillPage, addUtilityBill, updateUtilityBill, deleteUtilityBill, getRoomList } from '@/api/dormitory'

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增账单')
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)
const roomOptions = ref([])

const searchForm = reactive({
  roomNo: '',
  billMonth: '',
  paymentStatus: null
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const formData = reactive({
  id: null,
  roomId: null,
  billMonth: '',
  electricStart: 0,
  electricEnd: 0,
  electricUsage: 0,
  electricPrice: 0.8,
  electricAmount: 0,
  waterStart: 0,
  waterEnd: 0,
  waterUsage: 0,
  waterPrice: 3,
  waterAmount: 0,
  totalAmount: 0,
  actualAmount: 0,
  paymentStatus: 1,
  paymentMethod: '',
  paymentTime: '',
  remark: ''
})

const formRules = {
  roomId: [{ required: true, message: '请输入宿舍ID', trigger: 'blur' }],
  billMonth: [{ required: true, message: '请选择账单月份', trigger: 'change' }],
  totalAmount: [{ required: true, message: '请输入总费用', trigger: 'blur' }],
  actualAmount: [{ required: true, message: '请输入应缴金额', trigger: 'blur' }],
  paymentStatus: [{ required: true, message: '请选择缴费状态', trigger: 'change' }]
}

const paymentText = (val) => {
  switch (val) {
    case 1: return '未缴'
    case 2: return '已缴'
    case 3: return '逾期'
    default: return '-'
  }
}

const paymentTag = (val) => {
  switch (val) {
    case 1: return 'warning'
    case 2: return 'success'
    case 3: return 'danger'
    default: return 'info'
  }
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getUtilityBillPage({
      ...searchForm,
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    })
    tableData.value = res.data.records || []
    pagination.total = res.data.total || 0
  } catch (error) {
    console.error('获取水电费数据失败', error)
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
    billMonth: '',
    paymentStatus: null
  })
  handleSearch()
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增账单'
  Object.assign(formData, {
    id: null,
    roomId: null,
    billMonth: '',
    electricStart: 0,
    electricEnd: 0,
    electricUsage: 0,
    electricPrice: 0.8,
    electricAmount: 0,
    waterStart: 0,
    waterEnd: 0,
    waterUsage: 0,
    waterPrice: 3,
    waterAmount: 0,
    totalAmount: 0,
    actualAmount: 0,
    paymentStatus: 1,
    paymentMethod: '',
    paymentTime: '',
    remark: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑账单'
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
        await updateUtilityBill(formData)
        ElMessage.success('更新成功')
      } else {
        await addUtilityBill(formData)
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
  ElMessageBox.confirm('确认删除该账单吗？', '提示', { type: 'warning' })
    .then(async () => {
      await deleteUtilityBill(row.id)
      ElMessage.success('删除成功')
      fetchData()
    })
    .catch(() => {})
}

const handleDialogClose = () => {
  formRef.value?.resetFields()
}

const fetchRoomOptions = async () => {
  try {
    const res = await getRoomList()
    roomOptions.value = res.data || []
  } catch (error) {
    console.error('获取宿舍列表失败', error)
  }
}

onMounted(() => {
  fetchData()
  fetchRoomOptions()
})
</script>

