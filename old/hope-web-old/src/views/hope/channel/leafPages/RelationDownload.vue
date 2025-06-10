<template>
  <div>
    <a-modal :title="title" :width="1200"  @ok="handleOk" @cancel="handleCancel" :confirm-loading="confirmLoading" :visible="modalStatus" >
      <a-form-model ref="paramsFormRef" :form="paramsForm" :model="paramsForm" :rules="rules" :label-col="formItemLayout.labelCol" :wrapper-col="formItemLayout.wrapperCol">
        <a-row>
          <a-col :span="12">
            <a-form-item label="频道名称" prop="channelName">
              <a-input disabled v-model="paramsForm.channelName" placeholder="请输入频道名称" />
            </a-form-item>
          </a-col>
          <a-col :span="12" @click="handleSelectDownloadName">
            <a-form-model-item label="下载名称" prop="downloadName">
              <a-auto-complete
                class="global-search"
                v-model="paramsForm.downloadName"
                placeholder="请选择下载名称"
                disabled
                >
                <a-input>
                  <a-button slot="suffix" class="search-btn" type="primary">
                    <a-icon type="search"/>
                  </a-button>
                </a-input>
              </a-auto-complete>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="上传开关" prop="uploadEnable">
              <a-switch checked-children="开" un-checked-children="关" :checked="uploadEnable" @change="uploadEnableHeader"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="每日上传上限" prop="uploadDayMax">
              <a-input v-model="paramsForm.uploadDayMax"  placeholder="请输入每日上传上限" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="上传路径" prop="uploadPath">
              <a-input v-model="paramsForm.uploadPath" placeholder="请输入上传路径" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="播放列表" prop="uploadPlayList">
              <a-input v-model="paramsForm.uploadPlayList" placeholder="请输入播放列表" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="上传item类型" prop="uploadItem">
              <a-input v-model="paramsForm.uploadItem" placeholder="请输入上传item类型" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="上传标签" prop="uploadLabel">
              <a-input v-model="paramsForm.uploadLabel" placeholder="请输入上传标签" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="上传信息描述" prop="uploadDesc">
              <a-input v-model="paramsForm.uploadDesc" placeholder="请输入上传信息描述" />
            </a-form-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-modal>
    <SelectDownloadModal ref="SelectDownloadModalRef" @getDownloadName="getDownloadName"/>
  </div>
</template>
<script>
import SelectDownloadModal from './SelectDownloadModal.vue'
export default {
  components: {
    SelectDownloadModal
  },
  data () {
    return {
      modalStatus: false,
      title: '关联下载',
      dataSource: [],
      confirmLoading: false,
      formItemLayout: {
        labelCol: { span: 5 },
        wrapperCol: { span: 17 }
      },
      uploadEnable: false,
      paramsForm: {
        channelName: '',
        channelType: '',
        downloadName: '',
        uploadEnable: 'on',
        uploadDayMax: '',
        uploadPath: '',
        uploadShellPath: '',
        uploadPlayList: '',
        uploadItem: '',
        uploadLabel: '',
        uploadOk: '',
        uploadDesc: ''
      },
      rules: {
        channel_name: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        region: [{ required: true, message: '请选择频道类型', trigger: 'change' }]
      },
      channelTypeList: this.$store.state.account.dictList['channelType'] || []
    }
  },
  methods: {
    getDownloadName (e) {
      this.paramsForm.downloadName = e.downloadName
      this.paramsForm.uploadPath = e.downloadPath
      this.paramsForm.uploadDesc = e.downloadDesc
      this.paramsForm.uploadLabel = e.downloadDesc
      this.paramsForm.uploadPlayList=e.downloadName.split('-')[0]
    },
    uploadEnableHeader (e) {
      this.uploadEnable = e
      this.paramsForm.uploadEnable = e ? 'on' : 'off'
    },
    showModal (row) {
      this.modalStatus = true
      this.paramsForm = {
        channelName: row.channelName,
        channelType: row.channelType,
        downloadName: '',
        uploadEnable: 'on',
        uploadDayMax: '5',
        uploadPath: '',
        uploadShellPath: '',
        uploadPlayList: '',//row.channelName.split('-')[0],
        uploadItem: '',
        uploadLabel: '',
        uploadOk: '',
        uploadDesc: ''
      }
    },
    handleOk () {
      this.$refs.paramsFormRef.validate(valid => {
        if (valid) {
          this.confirmLoading = true
          this.$post('uploadInfo/add', this.paramsForm).then((r) => {
            this.confirmLoading = false
            if (r.data.message === 'error') {
            this.$message.error('服务器错误,添加失败！')
          }else if(r.data.message==='badRequest'){
            this.$message.error('请求参数错误,添加失败！')
          } else {
            this.modalStatus = false
            this.$message.success('添加成功！')
            this.fetch()
          }
          })
        } else {
          return false
        }
      })
    },
    handleSelectDownloadName () {
      this.paramsForm.downloadName = ''
      this.$refs.SelectDownloadModalRef.showModal()
    },
    handleCancel () {
      this.$refs.paramsFormRef.resetFields()
      this.modalStatus = false
    }
  }
}
</script>
<style lang="less">
.global-search{
  width:280px;
}
.ant-input-affix-wrapper .ant-input-suffix{
  right:0;
}
</style>
