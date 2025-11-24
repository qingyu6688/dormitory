import request from '@/utils/request'

// 获取文件分页列表
export const getFilePage = (params) => {
  return request({
    url: '/file/page',
    method: 'get',
    params
  })
}

// 获取所有文件列表
export const getFileList = () => {
  return request({
    url: '/file/list',
    method: 'get'
  })
}

// 删除文件
export const deleteFile = (id) => {
  return request({
    url: `/file/${id}`,
    method: 'delete'
  })
}

// 批量删除文件
export const deleteFileBatch = (ids) => {
  return request({
    url: '/file/batch',
    method: 'delete',
    data: ids
  })
}

// 更新文件启用状态
export const updateFileEnable = (id, enable) => {
  return request({
    url: `/file/enable/${id}`,
    method: 'put',
    params: { enable }
  })
}
