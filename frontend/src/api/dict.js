import request from '@/utils/request'

// 获取字典分页列表
export const getDictPage = (params) => {
  return request({
    url: '/dict/page',
    method: 'get',
    params
  })
}

// 获取所有字典列表
export const getDictList = () => {
  return request({
    url: '/dict/list',
    method: 'get'
  })
}

// 根据类型获取字典
export const getDictByType = (type) => {
  return request({
    url: `/dict/type/${type}`,
    method: 'get'
  })
}

// 新增字典
export const addDict = (data) => {
  return request({
    url: '/dict',
    method: 'post',
    data
  })
}

// 更新字典
export const updateDict = (data) => {
  return request({
    url: '/dict',
    method: 'put',
    data
  })
}

// 删除字典
export const deleteDict = (id) => {
  return request({
    url: `/dict/${id}`,
    method: 'delete'
  })
}

// 批量删除字典
export const deleteDictBatch = (ids) => {
  return request({
    url: '/dict/batch',
    method: 'delete',
    data: ids
  })
}
