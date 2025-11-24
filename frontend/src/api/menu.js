import request from '@/utils/request'

// 分页查询菜单
export const getMenuPage = (params) => {
  return request({
    url: '/menu/page',
    method: 'get',
    params
  })
}

// 获取菜单树
export const getMenuTree = () => {
  return request({
    url: '/menu/tree',
    method: 'get'
  })
}

// 根据角色ID获取菜单
export const getMenusByRoleId = (roleId) => {
  return request({
    url: `/menu/role/${roleId}`,
    method: 'get'
  })
}

// 获取所有菜单列表
export const getMenuList = () => {
  return request({
    url: '/menu/list',
    method: 'get'
  })
}

// 新增菜单
export const addMenu = (data) => {
  return request({
    url: '/menu',
    method: 'post',
    data
  })
}

// 更新菜单
export const updateMenu = (data) => {
  return request({
    url: '/menu',
    method: 'put',
    data
  })
}

// 删除菜单
export const deleteMenu = (id) => {
  return request({
    url: `/menu/${id}`,
    method: 'delete'
  })
}
