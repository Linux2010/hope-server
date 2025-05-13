import request from '@/utils/request'

// 查询【下载管理】列表
export function listInfo(query) {
  return request({
    url: '/system/channel/list',
    method: 'get',
    params: query
  })
}

// 查询【下载管理】详细
export function getInfo(channelId) {
  return request({
    url: '/system/channel/' + channelId,
    method: 'get'
  })
}

// 新增【下载管理】
export function addInfo(data) {
  return request({
    url: '/system/channel',
    method: 'post',
    data: data
  })
}

// 修改【下载管理】
export function updateInfo(data) {
  return request({
    url: '/system/channel',
    method: 'put',
    data: data
  })
}

// 删除【下载管理】
export function delInfo(channelId) {
  return request({
    url: '/system/channel/' + channelId,
    method: 'delete'
  })
}

// 启动后台实例
export function openBack(channelType, channelName) {
  return request({
    url: `/system/channel/openBack/${channelType}/${channelName}`,
    method: 'post',
    data: {}
  })
}

// 查询引擎列表
export function listEngine(query) {
  return request({
    url: '/system/engineInfo/list',
    method: 'get',
    params: query
  })
}

// 获取所有引擎信息
export function listAllEngines() {
  return request({
    url: '/system/engineInfo/listAll',
    method: 'get'
  })
}

// 获取所有频道名称列表
export function listAllChannelNames() {
  return request({
    url: '/system/channel/listAllNames',
    method: 'get'
  })
}

// 模糊搜索频道名称
export function searchChannelNames(channelName) {
  return request({
    url: '/system/channel/searchNames',
    method: 'get',
    params: { channelName }
  })
}
