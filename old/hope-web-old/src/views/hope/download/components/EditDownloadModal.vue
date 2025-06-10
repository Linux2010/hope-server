<template>
  <a-modal :title="title" :width="1200"  @ok="handleOk" @cancel="handleCancel" :confirm-loading="confirmLoading" :visible="modalStatus" >
    <a-form-model ref="paramsFormRef" :form="paramsForm" :model="paramsForm" :rules="rules" :label-col="formItemLayout.labelCol" :wrapper-col="formItemLayout.wrapperCol">
      <a-row>
        <a-col :span="12">
          <a-form-model-item label="下载名称" prop="downloadName">
            <a-input v-if="editBoolean" disabled v-model="paramsForm.downloadName" />
            <a-input v-else v-model="paramsForm.downloadName" placeholder="请输入下载名称"/>
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="下载类型" prop="downloadType">
            <a-input v-if="editBoolean" disabled v-model="paramsForm.downloadType"/>
            <a-select v-else v-model="paramsForm.downloadType" placeholder="请选择下载类型" allowClear>
              <a-select-option :value="item.keyy" v-for="item in downloadTypeList" :key="item.id">
                {{item.valuee}}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="循环类型" prop="cycleType">
            <a-select  v-model="paramsForm.cycleType" placeholder="请选择循环类型" allowClear>
              <a-select-option :value="item.keyy" v-for="item in cycleTypeList" :key="item.id">
                {{item.valuee}}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item  label="引擎名称" prop="engineName">
            <a-input v-if="editBoolean" disabled v-model="paramsForm.engineName"/>
            <a-select v-else v-model="paramsForm.engineId"  placeholder="请选择引擎名称" @select="getStoragePathList" allowClear>
              <a-select-option :value="item.engineId" v-for="item in engineNameList" :key="item.engineId" >
                {{item.engineName}}
              </a-select-option>
            </a-select>
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item label="存储路径" prop="storagePath">
            <a-select v-model="paramsForm.storageId"  placeholder="请选择存储路径"  @select="setDownloadPathView" allowClear>
              <a-select-option :value="item.storageId" v-for="item in storagePathList" :key="item.storageId">
                {{item.storagePath}}
              </a-select-option>
            </a-select>
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="下载路径" prop="downloadPath">
            <a-input v-model="paramsForm.downloadPath" placeholder="请输入下载路径" disabled />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="下载url" prop="downloadUrl">
            <a-input v-model="paramsForm.downloadUrl" placeholder="请输入下载url" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="下载类型" prop="videoType">
            <a-select  v-model="paramsForm.videoType" placeholder="请选择下载类型" allowClear>
              <a-select-option :value="item.keyy" v-for="item in videoTypeList" :key="item.id">
                {{item.valuee}}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="增量结束位" prop="downloadEnd">
            <a-input v-model="paramsForm.downloadEnd" placeholder="请输入增量结束位" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="全量结束位" prop="downloadAllEnd">
            <a-input v-model="paramsForm.downloadAllEnd" placeholder="请输入全量结束位" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="下载信息描述" prop="downloadDesc">
            <a-input v-model="paramsForm.downloadDesc" placeholder="请输入下载信息描述" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="视频质量" prop="视频质量">
            <a-select v-model="paramsForm.quality" placeholder="视频质量">
              <a-select-option value="720">720p</a-select-option>
              <a-select-option value="1080">1080p</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form-model>
  </a-modal>
</template>
<script>
export default {
  components: {},
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
      downloadEnable: false,
      paramsForm: {
        downloadName: undefined,
        downloadType: undefined,
        engineName: '',
        cycleType: '',
        engineId: undefined,
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
        storageId: '',
        quality: ''
      },
      hardDiskStorageForm: {
        storageId: '',
        engineId: '',
        storagePath : '',
        storageDesc : ''
      },
      rules: {
        channel_name: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        region: [{ required: true, message: '请选择频道类型', trigger: 'change' }]
      },
      channelTypeList: this.$store.state.account.dictList['channelType'] || [],
      editBoolean: false,
      downloadTypeList: this.$store.state.account.dictList['downloadType'] || [],
      videoTypeList: this.$store.state.account.dictList['video_type'] || [],
      cycleTypeList: this.$store.state.account.dictList['cycleType'] || [],
      engineNameList: [],
      storagePathList: [],
      engineId: undefined,
      downloadId: ''
    }
  },
  created() {
    this.paramsForm.quality = '720';
  },
  methods: {
    getEngineName () {
      this.$get('engineInfo/queryAll').then((r) => {
        this.engineNameList = r.data
      })
    },
    getStoragePathList(){
      if(this.paramsForm.engineId != null){
        this.$get('hardDiskStorage/query',{
          engineId: this.paramsForm.engineId
        }).then((r) => {
          this.storagePathList = r.data
        })
      }
    },
    setDownloadPathView(e){
      //遍历获取当前选中
      this.storagePathList.forEach(l=>{
        if(l.storageId === e){
          this.hardDiskStorageForm = l
        }
      })
      this.setDownloadPath()
    },
    setDownloadPath(){
      this.paramsForm.downloadPath = this.hardDiskStorageForm.storagePath+this.paramsForm.downloadName
      this.paramsForm.storageId = this.hardDiskStorageForm.storageId
    },
    handleDownloadEnable (e) {
      this.downloadEnable = e
      this.paramsForm.downloadEnable = e ? 'true' : 'false'
    },
    showModal (row) {
      if (row) {
        this.editBoolean = true
        this.title = '修改'
        this.downloadId = row.downloadId
        // delete this.paramsForm.downloadName
        this.downloadEnable = row.downloadEnable === 'true'
        Object.keys(this.paramsForm).forEach(key => { this.paramsForm[key] = row[key] })
        this.getStoragePathList()
      } else {
        this.paramsForm = {
          downloadName: '',
          downloadType: '',
          engineName: '',
          engineId: '',
          shellPath: '',
          agent: '',
          downloadEnable: '',
          downloadPath: '',
          downloadStart: '',
          downloadUrl: '',
          videoType: '',
          afterShellPath: '',
          downloadEnd: '',
          downloadDesc: ''
        }
        this.title = '新增'
        this.editBoolean = false
      }
      this.modalStatus = true
    },
    handleOk () {
      this.$refs.paramsFormRef.validate(valid => {
        if (valid) {
          this.confirmLoading = true
          this.engineNameList.forEach(item => {
            if (item.engineId === this.engineId) {
              this.paramsForm.engineName = item.engineName
              this.paramsForm.engineId = item.engineId
            }
          })
          if (this.editBoolean) {
            let postData = this.paramsForm
            postData.downloadId = this.downloadId
            delete postData.downloadName
            this.$post('downloadInfo/update', postData).then((r) => {
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
            this.$post('downloadInfo/add', this.paramsForm).then((r) => {
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
  },
  mounted () {
    this.getEngineName()
  }
}
</script>
