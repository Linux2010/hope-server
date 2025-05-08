import request from '@/utils/request'

// 查询上传实例列表
export function listInstance(query) {
  return request({
    url: '/system/instance/list',
    method: 'get',
    params: query
  })
}

// 查询上传实例详细
export function getInstance(instanceId) {
  return request({
    url: '/system/instance/' + instanceId,
    method: 'get'
  })
}

// 新增上传实例
export function addInstance(data) {
  return request({
    url: '/system/instance',
    method: 'post',
    data: data
  })
}

// 修改上传实例
export function updateInstance(data) {
  return request({
    url: '/system/instance',
    method: 'put',
    data: data
  })
}

// 删除上传实例
export function delInstance(instanceId) {
  return request({
    url: '/system/instance/' + instanceId,
    method: 'delete'
  })
}
