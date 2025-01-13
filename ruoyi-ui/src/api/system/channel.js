import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listInfo(query) {
  return request({
    url: '/system/channel/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getInfo(channelId) {
  return request({
    url: '/system/channel/' + channelId,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addInfo(data) {
  return request({
    url: '/system/channel',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateInfo(data) {
  return request({
    url: '/system/channel',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delInfo(channelId) {
  return request({
    url: '/system/channel/' + channelId,
    method: 'delete'
  })
}
