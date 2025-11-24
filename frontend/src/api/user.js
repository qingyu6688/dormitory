import request from '@/utils/request'

// 获取用户分页列表
export const getUserPage = (params) => {
  return request({
    url: '/user/page',
    method: 'get',
    params
  })
}

// 获取所有用户列表
export const getUserList = () => {
  return request({
    url: '/user/list',
    method: 'get'
  })
}

// 新增用户
export const addUser = (data) => {
  return request({
    url: '/user',
    method: 'post',
    data
  })
}

// 更新用户
export const updateUser = (data) => {
  return request({
    url: '/user',
    method: 'put',
    data
  })
}

// 删除用户
export const deleteUser = (id) => {
  return request({
    url: `/user/${id}`,
    method: 'delete'
  })
}

// 重置密码
export const resetPassword = (id) => {
  return request({
    url: `/user/resetPassword/${id}`,
    method: 'put'
  })
}

// 根据ID获取用户
export const getUserById = (id) => {
  return request({
    url: `/user/${id}`,
    method: 'get'
  })
}

// 更新用户头像
export const updateAvatar = (id, avatar) => {
  return request({
    url: `/user/avatar/${id}`,
    method: 'put',
    params: { avatar }
  })
}

// 修改密码
export const updatePassword = (id, oldPassword, newPassword) => {
  return request({
    url: `/user/password/${id}`,
    method: 'put',
    params: { oldPassword, newPassword }
  })
}
