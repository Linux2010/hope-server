<template>
  <a-modal :title="title" :width="1200" @cancel="handleCancel" :confirm-loading="confirmLoading" :visible="modalStatus" >
    <a-form-model ref="paramsFormRef" :form="paramsForm" :model="paramsForm" :label-col="formItemLayout.labelCol" :wrapper-col="formItemLayout.wrapperCol">
      <a-row>
        <a-col :span="12">
          <a-form-model-item label="频道名称" prop="channelName">
            <span>{{paramsForm.channelName}}</span>
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="下载名称" prop="downloadName">
            <span>{{paramsForm.downloadName}}</span>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="上传名称" prop="uploadName">
            <span>{{paramsForm.uploadName}}</span>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item label="上传开关" prop="uploadEnable">
            <span>{{paramsForm.uploadEnable == 'true' ? '开' : '关'}}</span>
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="每日上传上限" prop="uploadDayMax">
            <span>{{paramsForm.uploadDayMax}}</span>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="上传路径" prop="uploadPath">
            <span>{{paramsForm.uploadPath}}</span>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="上传执行脚本" prop="uploadShellPath">
            <span>{{paramsForm.uploadShellPath}}</span>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="播放列表" prop="uploadPlayList">
            <span>{{paramsForm.uploadPlayList}}</span>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="上传item类型" prop="uploadItem">
            <span>{{paramsForm.uploadItem}}</span>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="上传标签" prop="uploadLabel">
            <span>{{paramsForm.uploadLabel}}</span>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="上传ok标志" prop="uploadOk">
            <span>{{paramsForm.uploadOk}}</span>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="上传信息描述" prop="uploadDesc">
            <span>{{paramsForm.uploadDesc}}</span>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form-model>
    <template slot="footer">
      <a-button @click="handleCancel">关闭</a-button>
    </template>
  </a-modal>
</template>
<script>
export default {
  data () {
    return {
      modalStatus: false,
      title: '详情',
      dataSource: [],
      confirmLoading: false,
      formItemLayout: {
        labelCol: { span: 5 },
        wrapperCol: { span: 17 }
      },
      uploadEnable: false,
      paramsForm: {
        channelName: '',
        downloadName: '',
        uploadName: '',
        uploadEnable: '',
        uploadDayMax: '',
        uploadPath: '',
        uploadShellPath: '',
        uploadPlayList: '',
        uploadItem: '',
        uploadLabel: '',
        uploadOk: '',
        uploadDesc: ''
      }
    }
  },
  methods: {
    showModal (row) {
      Object.keys(this.paramsForm).forEach(key => { this.paramsForm[key] = row[key] })
      this.modalStatus = true
    },
    handleOk () {
      this.modalStatus = false
    },
    handleCancel () {
      this.$refs.paramsFormRef.resetFields()
      this.modalStatus = false
    }
  }
}
</script>
