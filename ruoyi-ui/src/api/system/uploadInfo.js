import request from '@/utils/request'

// 查询上传管理列表
export function listUploadInfo(query) {
  return request({
    url: '/system/uploadInfo/list',
    method: 'get',
    params: query
  })
}

// 查询上传管理详细
export function getUploadInfo(uploadId) {
  return request({
    url: '/system/uploadInfo/' + uploadId,
    method: 'get'
  })
}

// 新增上传管理
export function addUploadInfo(data) {
  return request({
    url: '/system/uploadInfo',
    method: 'post',
    data: data
  })
}

// 修改上传管理
export function updateUploadInfo(data) {
  return request({
    url: '/system/uploadInfo',
    method: 'put',
    data: data
  })
}

// 删除上传管理
export function delUploadInfo(uploadId) {
  return request({
    url: '/system/uploadInfo/' + uploadId,
    method: 'delete'
  })
}
