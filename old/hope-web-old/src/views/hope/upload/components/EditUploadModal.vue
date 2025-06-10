<template>
  <a-modal :title="title" :width="1200"  @ok="handleOk" @cancel="handleCancel" :confirm-loading="confirmLoading" :visible="modalStatus" >
    <a-form-model ref="paramsFormRef" :form="paramsForm" :model="paramsForm" :rules="rules" :label-col="formItemLayout.labelCol" :wrapper-col="formItemLayout.wrapperCol">
      <a-row>
        <a-col :span="12" v-if="editBoolean">
          <a-form-model-item label="频道名称" prop="channelName">
            <a-input v-model="paramsForm.channelName" placeholder="点击选择频道名称"/>
          </a-form-model-item>
        </a-col>
        <a-col :span="12" v-else @click="handleSelectChannelName">
          <a-form-model-item label="频道名称" prop="channelName">
            <a-auto-complete
              class="global-search"
              v-model="paramsForm.channelName"
              placeholder="点击选择频道名称"
              disabled
            >
              <a-input>
                <a-button slot="suffix" class="search-btn" type="primary">
                  <a-icon type="search"/>
                </a-button>
              </a-input>
            </a-auto-complete>
            <!-- <a-input disabled v-model="paramsForm.channelName" placeholder="点击选择频道名称"/> -->
          </a-form-model-item>
        </a-col>
        <a-col :span="12"  v-if="editBoolean">
          <a-form-item label="下载名称" prop="downloadName">
            <a-input v-model="paramsForm.downloadName" placeholder="点击选择下载名称"/>
          </a-form-item>
        </a-col>
        <a-col :span="12" v-else @click="handleSelectDownload">
          <a-form-item label="下载名称" prop="downloadName">
            <a-auto-complete
              class="global-search"
              v-model="paramsForm.downloadName"
              placeholder="点击选择下载名称"
              disabled
              >
              <a-input>
                <a-button slot="suffix" class="search-btn" type="primary">
                  <a-icon type="search"/>
                </a-button>
              </a-input>
            </a-auto-complete>
            <!-- <a-input disabled v-model="paramsForm.downloadName" placeholder="点击选择下载名称"/> -->
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item label="上传开关" prop="uploadEnable">
            <a-switch checked-children="开" un-checked-children="关" :checked="uploadEnable" @change="uploadEnableHeader"/>
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="每日上传上限" prop="uploadDayMax">
            <a-input v-model="paramsForm.uploadDayMax" placeholder="请输入每日上传上限" />
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
    <SelectDownloadModal ref="selectDownloadModalRef" @getDownloadName="getDownloadName"/>
    <SelectChannelNameModal ref="selectChannelNameModalRef" @getChannelNameName="getChannelNameName"/>
  </a-modal>
</template>
<script>
import SelectDownloadModal from './SelectDownloadModal.vue'
import SelectChannelNameModal from './SelectChannelNameModal.vue'
export default {
  components: {
    SelectDownloadModal,
    SelectChannelNameModal
  },
  data () {
    return {
      modalStatus: false,
      title: '新增',
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
        uploadEnable: '',
        uploadDayMax: '',
        uploadPath: '',
        uploadShellPath: '',
        uploadPlayList: '',
        uploadItem: '',
        uploadLabel: '',
        uploadOk: '',
        uploadDesc: '',
        uploadName: ''
      },
      rules: {
        channel_name: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        region: [{ required: true, message: '请选择频道类型', trigger: 'change' }]
      },
      channelTypeList: this.$store.state.account.dictList['channelType'] || [],
      editBoolean: false,
      uploadId: ''
    }
  },
  methods: {
    uploadEnableHeader (e) {
      this.uploadEnable = e
      this.paramsForm.uploadEnable = e ? 'true' : 'false'
    },
    getDownloadName (e) {
      this.paramsForm.downloadName = e.downloadName
      this.paramsForm.uploadPath = e.downloadPath
      this.paramsForm.uploadDesc = e.downloadDesc
      this.paramsForm.uploadLabel = e.downloadDesc
      this.paramsForm.uploadPlayList = e.downloadName.split('-')[0]
    },
    getChannelNameName (e) {
      this.paramsForm.channelName = e.channelName
      this.paramsForm.channelType = e.channelType
    },
    handleSelectDownload () {
      //this.paramsForm.downloadName = ''
      this.$refs.selectDownloadModalRef.showModal()
    },
    handleSelectChannelName () {
      //this.paramsForm.channelName = ''
      this.$refs.selectChannelNameModalRef.showModal()
    },
    showModal (row) {
      if (row) {
        this.editBoolean = true
        this.title = '修改'
        this.uploadEnable = (row.uploadEnable === 'true')
        this.uploadId = row.uploadId
        Object.keys(this.paramsForm).forEach(key => { this.paramsForm[key] = row[key] })
      } else {
        this.paramsForm = {
          channelName: '',
          downloadName: '',
          uploadEnable: this.uploadEnable,
          uploadDayMax: '5',
          uploadPath: '',
          uploadShellPath: '',
          uploadPlayList: '',
          uploadItem: '',
          uploadLabel: '',
          uploadOk: '',
          uploadDesc: '',
          uploadName: ''
        }
        this.editBoolean = false
        this.title = '新增'
      }
      this.modalStatus = true
    },
    handleOk () {
      this.$refs.paramsFormRef.validate(valid => {
        if (valid) {
          this.confirmLoading = true
          if (this.editBoolean) {
            let postData = this.paramsForm
            // delete postData.channelName
            postData.uploadId = this.uploadId
            this.$post('uploadInfo/update', postData).then((r) => {
              if (r.data.message === 'badRequest' || r.data.message === 'error') {
                this.$message.error(r.data.messageDetail)
                this.confirmLoading = false
              } else {
                this.$message.success(r.data.message)
                this.confirmLoading = false
                this.visible = false
                this.$refs.paramsFormRef.resetFields()
                this.modalStatus = false
                this.$emit('refresh')
              }
            })
          } else {
            this.$post('uploadInfo/add', this.paramsForm).then((r) => {
              if (r.data.message === 'badRequest' || r.data.message === 'error') {
                this.$message.error(r.data.messageDetail)
                this.confirmLoading = false
              } else {
                this.confirmLoading = false
                this.visible = false
                this.$refs.paramsFormRef.resetFields()
                this.modalStatus = false
                this.$emit('refresh')
              }
            })
          }
        } else {
          return false
        }
      })
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
