import request from '@/utils/request'

// ===== 宿舍楼管理 =====
export const getBuildingPage = (params) => {
  return request({
    url: '/building/page',
    method: 'get',
    params
  })
}

export const getBuildingList = () => {
  return request({
    url: '/building/list',
    method: 'get'
  })
}

export const addBuilding = (data) => {
  return request({
    url: '/building',
    method: 'post',
    data
  })
}

export const updateBuilding = (data) => {
  return request({
    url: '/building',
    method: 'put',
    data
  })
}

export const deleteBuilding = (id) => {
  return request({
    url: `/building/${id}`,
    method: 'delete'
  })
}

// ===== 宿舍管理 =====
export const getRoomPage = (params) => {
  return request({
    url: '/room/page',
    method: 'get',
    params
  })
}

export const getRoomList = (params) => {
  return request({
    url: '/room/list',
    method: 'get',
    params
  })
}

export const addRoom = (data) => {
  return request({
    url: '/room',
    method: 'post',
    data
  })
}

export const updateRoom = (data) => {
  return request({
    url: '/room',
    method: 'put',
    data
  })
}

export const deleteRoom = (id) => {
  return request({
    url: `/room/${id}`,
    method: 'delete'
  })
}

// 查询宿舍住宿人员
export const getRoomResidents = (id) => {
  return request({
    url: `/room/${id}/residents`,
    method: 'get'
  })
}

// ===== 入住管理 =====
export const getCheckInPage = (params) => {
  return request({
    url: '/checkin/page',
    method: 'get',
    params
  })
}

export const getCheckInList = () => {
  return request({
    url: '/checkin/list',
    method: 'get'
  })
}

export const addCheckIn = (data) => {
  return request({
    url: '/checkin',
    method: 'post',
    data
  })
}

export const updateCheckIn = (data) => {
  return request({
    url: '/checkin',
    method: 'put',
    data
  })
}

export const deleteCheckIn = (id) => {
  return request({
    url: `/checkin/${id}`,
    method: 'delete'
  })
}

// ===== 调宿申请 =====
export const getTransferPage = (params) => {
  return request({
    url: '/transfer/page',
    method: 'get',
    params
  })
}

export const getTransferList = () => {
  return request({
    url: '/transfer/list',
    method: 'get'
  })
}

export const addTransfer = (data) => {
  return request({
    url: '/transfer',
    method: 'post',
    data
  })
}

export const updateTransfer = (data) => {
  return request({
    url: '/transfer',
    method: 'put',
    data
  })
}

export const approveTransfer = (data) => {
  return request({
    url: '/transfer/approve',
    method: 'put',
    data
  })
}

export const deleteTransfer = (id) => {
  return request({
    url: `/transfer/${id}`,
    method: 'delete'
  })
}

// ===== 报修管理 =====
export const getRepairPage = (params) => {
  return request({
    url: '/repair/page',
    method: 'get',
    params
  })
}

export const getRepairList = () => {
  return request({
    url: '/repair/list',
    method: 'get'
  })
}

export const addRepair = (data) => {
  return request({
    url: '/repair',
    method: 'post',
    data
  })
}

export const updateRepair = (data) => {
  return request({
    url: '/repair',
    method: 'put',
    data
  })
}

export const deleteRepair = (id) => {
  return request({
    url: `/repair/${id}`,
    method: 'delete'
  })
}

// ===== 访客登记 =====
export const getVisitorPage = (params) => {
  return request({
    url: '/visitor/page',
    method: 'get',
    params
  })
}

export const getVisitorList = () => {
  return request({
    url: '/visitor/list',
    method: 'get'
  })
}

export const addVisitor = (data) => {
  return request({
    url: '/visitor',
    method: 'post',
    data
  })
}

export const updateVisitor = (data) => {
  return request({
    url: '/visitor',
    method: 'put',
    data
  })
}

export const deleteVisitor = (id) => {
  return request({
    url: `/visitor/${id}`,
    method: 'delete'
  })
}

// ===== 卫生检查 =====
export const getHygienePage = (params) => {
  return request({
    url: '/hygiene/page',
    method: 'get',
    params
  })
}

export const getHygieneList = () => {
  return request({
    url: '/hygiene/list',
    method: 'get'
  })
}

export const addHygiene = (data) => {
  return request({
    url: '/hygiene',
    method: 'post',
    data
  })
}

export const updateHygiene = (data) => {
  return request({
    url: '/hygiene',
    method: 'put',
    data
  })
}

export const deleteHygiene = (id) => {
  return request({
    url: `/hygiene/${id}`,
    method: 'delete'
  })
}

// ===== 水电费管理 =====
export const getUtilityBillPage = (params) => {
  return request({
    url: '/utilitybill/page',
    method: 'get',
    params
  })
}

export const getUtilityBillList = () => {
  return request({
    url: '/utilitybill/list',
    method: 'get'
  })
}

export const addUtilityBill = (data) => {
  return request({
    url: '/utilitybill',
    method: 'post',
    data
  })
}

export const updateUtilityBill = (data) => {
  return request({
    url: '/utilitybill',
    method: 'put',
    data
  })
}

export const deleteUtilityBill = (id) => {
  return request({
    url: `/utilitybill/${id}`,
    method: 'delete'
  })
}

// ===== 违纪记录 =====
export const getViolationPage = (params) => {
  return request({
    url: '/violation/page',
    method: 'get',
    params
  })
}

export const getViolationList = () => {
  return request({
    url: '/violation/list',
    method: 'get'
  })
}

export const addViolation = (data) => {
  return request({
    url: '/violation',
    method: 'post',
    data
  })
}

export const updateViolation = (data) => {
  return request({
    url: '/violation',
    method: 'put',
    data
  })
}

export const deleteViolation = (id) => {
  return request({
    url: `/violation/${id}`,
    method: 'delete'
  })
}

// ===== 通知公告 =====
export const getNoticePage = (params) => {
  return request({
    url: '/notice/page',
    method: 'get',
    params
  })
}

export const getNoticeList = () => {
  return request({
    url: '/notice/list',
    method: 'get'
  })
}

export const addNotice = (data) => {
  return request({
    url: '/notice',
    method: 'post',
    data
  })
}

export const updateNotice = (data) => {
  return request({
    url: '/notice',
    method: 'put',
    data
  })
}

export const deleteNotice = (id) => {
  return request({
    url: `/notice/${id}`,
    method: 'delete'
  })
}

// ===== 宿舍选择相关 =====
// 学生选择宿舍
export const selectRoom = (data) => {
  return request({
    url: '/room/select',
    method: 'post',
    data
  })
}

// 管理员分配宿舍
export const assignRoom = (data) => {
  return request({
    url: '/checkin/assign',
    method: 'post',
    data
  })
}
