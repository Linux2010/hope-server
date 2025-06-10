/**
 * 上传管理常量
 */

// OK后缀
export const UPLOAD_OK_SUFFIX = '.ok'

// YouTube脚本路径
export const YOUTUBE_SCRIPT_PATH = 'video-upload/you-upload/you_upload.py'

// Bili脚本路径
export const BILI_SCRIPT_PATH = 'video-upload/bili-upload/bili_upload_v2.py'

// Xigua脚本路径
export const XIGUA_SCRIPT_PATH = 'video-upload/xigua-upload/xigua_upload.py'

// 获取脚本路径
export function getScriptPathByType(channelType) {
  if (!channelType) {
    return ''
  }
  
  const type = channelType.toLowerCase()
  if (type === 'youtube') {
    return YOUTUBE_SCRIPT_PATH
  } else if (type === 'bili') {
    return BILI_SCRIPT_PATH
  } else if (type === 'xigua') {
    return XIGUA_SCRIPT_PATH
  }
  return ''
} 