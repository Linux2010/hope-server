import request from '@/utils/request'

// 查询下载管理列表
export function listDownloadInfo(query) {
  return request({
    url: '/system/downloadInfo/list',
    method: 'get',
    params: query
  })
}

// 查询下载管理详细
export function getDownloadInfo(downloadId) {
  return request({
    url: '/system/downloadInfo/' + downloadId,
    method: 'get'
  })
}

// 新增下载管理
export function addDownloadInfo(data) {
  return request({
    url: '/system/downloadInfo',
    method: 'post',
    data: data
  })
}

// 修改下载管理
export function updateDownloadInfo(data) {
  return request({
    url: '/system/downloadInfo',
    method: 'put',
    data: data
  })
}

// 删除下载管理
export function delDownloadInfo(downloadId) {
  return request({
    url: '/system/downloadInfo/' + downloadId,
    method: 'delete'
  })
}

// 获取所有引擎信息
export function listAllEngines() {
  return request({
    url: '/system/engineInfo/listAll',
    method: 'get'
  })
}

// 获取所有下载名称列表
export function listAllDownloadNames() {
  return request({
    url: '/system/downloadInfo/listAllNames',
    method: 'get'
  })
}

// 模糊搜索下载名称
export function searchDownloadNames(downloadName) {
  return request({
    url: '/system/downloadInfo/searchNames',
    method: 'get',
    params: { downloadName }
  })
}
