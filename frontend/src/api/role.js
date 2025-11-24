import request from '@/utils/request'

// 获取角色分页列表
export const getRolePage = (params) => {
  return request({
    url: '/role/page',
    method: 'get',
    params
  })
}

// 获取所有角色列表
export const getRoleList = () => {
  return request({
    url: '/role/list',
    method: 'get'
  })
}

// 新增角色
export const addRole = (data) => {
  return request({
    url: '/role',
    method: 'post',
    data
  })
}

// 更新角色
export const updateRole = (data) => {
  return request({
    url: '/role',
    method: 'put',
    data
  })
}

// 删除角色
export const deleteRole = (id) => {
  return request({
    url: `/role/${id}`,
    method: 'delete'
  })
}

// 获取角色的菜单权限
export const getRoleMenus = (roleId) => {
  return request({
    url: `/role/${roleId}/menus`,
    method: 'get'
  })
}

// 更新角色的菜单权限
export const updateRoleMenus = (roleId, menuIds) => {
  return request({
    url: `/role/${roleId}/menus`,
    method: 'put',
    data: menuIds
  })
}
