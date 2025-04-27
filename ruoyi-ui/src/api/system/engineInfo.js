import request from '@/utils/request'

// 查询引擎管理列表
export function listEngineInfo(query) {
  return request({
    url: '/system/engineInfo/list',
    method: 'get',
    params: query
  })
}

// 查询引擎管理详细
export function getEngineInfo(engineId) {
  return request({
    url: '/system/engineInfo/' + engineId,
    method: 'get'
  })
}

// 新增引擎管理
export function addEngineInfo(data) {
  return request({
    url: '/system/engineInfo',
    method: 'post',
    data: data
  })
}

// 修改引擎管理
export function updateEngineInfo(data) {
  return request({
    url: '/system/engineInfo',
    method: 'put',
    data: data
  })
}

// 删除引擎管理
export function delEngineInfo(engineId) {
  return request({
    url: '/system/engineInfo/' + engineId,
    method: 'delete'
  })
}
