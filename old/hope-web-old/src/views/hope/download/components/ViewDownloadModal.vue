<template>
  <a-modal :title="title" :width="1200"  @cancel="handleCancel" :confirm-loading="confirmLoading" :visible="modalStatus" >
    <a-form-model ref="paramsFormRef" :form="paramsForm" :model="paramsForm" :label-col="formItemLayout.labelCol" :wrapper-col="formItemLayout.wrapperCol">
      <a-row>
        <a-col :span="12">
          <a-form-model-item label="下载名称">
            <span>{{paramsForm.downloadName}}</span>
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="下载类型">
            <span>{{paramsForm.downloadType}}</span>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item label="引擎名称">
            <span>{{paramsForm.engineName}}</span>
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="下载路径">
            <span>{{paramsForm.downloadPath}}</span>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="下载开关">
            <span>{{paramsForm.downloadEnable}}</span>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="循环类型">
            <span>{{paramsForm.cycleType}}</span>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="视频分类">
            <span>{{paramsForm.videoType}}</span>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="后置shell">
            <span>{{paramsForm.afterShellPath}}</span>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="增量结束位">
            <span>{{paramsForm.downloadEnd}}</span>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="全量结束位">
            <span>{{paramsForm.downloadAllEnd}}</span>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="下载信息描述">
            <span>{{paramsForm.downloadDesc}}</span>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="下载URL">
            <a class="href" :href="paramsForm.downloadUrl" target="_blank">{{paramsForm.downloadUrl}}</a>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="视频质量">
            <span>{{paramsForm.quality}}</span>
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
  components: {},
  data () {
    return {
      modalStatus: false,
      title: '详情',
      confirmLoading: false,
      formItemLayout: {
        labelCol: { span: 5 },
        wrapperCol: { span: 17 }
      },
      paramsForm: {
        downloadName: '',
        downloadType: '',
        cycleType: '',
        engineName: '',
        shellPath: '',
        agent: '',
        downloadEnable: '',
        downloadPath: '',
        downloadStart: '',
        downloadUrl: '',
        videoType: '',
        afterShellPath: '',
        downloadEnd: '',
        downloadAllEnd: '',
        downloadDesc: '',
        quality: ''
      }
    }
  },
  methods: {
    showModal (row) {
      if (row) {
        Object.keys(this.paramsForm).forEach(key => { this.paramsForm[key] = row[key] })
      }
      this.modalStatus = true
    },
    handleCancel () {
      this.$refs.paramsFormRef.resetFields()
      this.modalStatus = false
    }
  }
}
</script>
