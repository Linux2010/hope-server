<template>
  <a-modal :title="title" :width="1200"  @ok="handleOk" @cancel="handleCancel" :confirm-loading="confirmLoading" :visible="modalStatus" >
    <a-form-model ref="paramsFormRef" :form="paramsForm" :model="paramsForm" :rules="rules" :label-col="formItemLayout.labelCol" :wrapper-col="formItemLayout.wrapperCol">
      <a-row>
        <a-col :span="12">
          <a-form-model-item label="频道类型" prop="channelType">
            <a-input v-if="editBoolean" disabled v-model="paramsForm.channelType"/>
            <a-select v-else v-model="paramsForm.channelType" placeholder="请选择频道类型" allowClear>
              <a-select-option :value="item.keyy" v-for="item in channelTypeList" :key="item.DICT_ID">
                {{item.valuee}}
              </a-select-option>
            </a-select>
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="频道名称" prop="channelName">
            <a-input v-if="editBoolean" disabled v-model="paramsForm.channelName"/>
            <a-input v-else v-model="paramsForm.channelName" placeholder="请输入频道名称" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="盈利状态" prop="gainStatus">
            <a-select  v-model="paramsForm.gainStatus" placeholder="请选择盈利状态"  allowClear>
              <a-select-option :value="item.keyy" v-for="item in gainStatusList" :key="item.DICT_ID">
                {{item.valuee}}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
<!--todo 修复 id与name匹配问题 bug -->
        <a-col :span="12">
          <a-form-model-item label="引擎名称" prop="engineName">
            <a-select v-model="paramsForm.engineId" placeholder="请选择引擎名称" allowClear>
              <a-select-option :value="item.engineId" v-for="item in engineNameList" :key="item.engineId">
                {{item.engineName}}
              </a-select-option>
            </a-select>
          </a-form-model-item>
        </a-col>

        <a-col :span="12">
          <a-form-model-item label="频道开关" prop="channelOffOn">
            <a-switch checked-children="开" un-checked-children="关" :checked="channelOffOn" @change="channelOffOnHeader"/>
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="登录账号" prop="username">
            <a-input v-model="paramsForm.username" placeholder="请输入登录账号" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="登录密码" prop="password">
            <a-input v-model="paramsForm.password" placeholder="请输入登录密码" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="手机号" prop="phone">
            <a-input v-model="paramsForm.phone" placeholder="请输入手机号" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="电子邮箱" prop="email">
            <a-input v-model="paramsForm.email" placeholder="请输入电子邮箱" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="辅助邮箱" prop="assistEmail">
            <a-input v-model="paramsForm.assistEmail" placeholder="请输入辅助邮箱" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="频道url" prop="channelUrl">
            <a-input v-model="paramsForm.channelUrl" placeholder="请输入频道url" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="每日上传上限" prop="dayMax">
            <a-input v-model="paramsForm.dayMax" placeholder="请输入每日上传上限"  />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="频道描述" prop="channelDesc">
            <a-input v-model="paramsForm.channelDesc" placeholder="请输入频道描述" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="账号过期时间" prop="accDue">
            <a-date-picker format="YYYY-MM-DD" v-model="paramsForm.accDue" placeholder="账号过期时间"/>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form-model>
  </a-modal>
</template>
<script>
export default {
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
      channelOffOn: false,
      paramsForm: {
        channelType: undefined,
        engineId: undefined,
        engineName:'',
        channelName: '',
        gainStatus: '',
        channelOffOn: 'off',
        username: '',
        password: '',
        phone: '',
        email: '',
        assistEmail: '',
        channelUrl: '',
        dayMax: '',
        titleLimit: '',
        loginUrl: '',
        homeUrl: '',
        cookieDomain: '',
        channelDesc: '',
        accDue: ''
      },
      rules: {
        channel_name: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        region: [{ required: true, message: '请选择频道类型', trigger: 'change' }]
      },
      channelTypeList: this.$store.state.account.dictList['channelType'] || [],
      gainStatusList: this.$store.state.account.dictList['gain_status'] || [],
      editBoolean: false,
      channelId: '',
      engineNameList: []
    }
  },
  methods: {
    getEngineName () {
      this.$get('engineInfo/queryAll').then((r) => {
        this.engineNameList = r.data
      })
    },
    channelOffOnHeader (e) {
      this.channelOffOn = e
      this.paramsForm.channelOffOn = e ? 'on' : 'off'
    },
    showModal (row) {
      if (row) {
        this.editBoolean = true
        this.title = '修改'
        this.channelOffOn = row.channelOffOn === 'on'
        this.channelId = row.channelId
        Object.keys(this.paramsForm).forEach(key => { this.paramsForm[key] = row[key] })
      } else {
        this.editBoolean = false
        this.paramsForm = {
          channelType: '',
          channelName: '',
          gainStatus: '',
          channelOffOn: 'off',
          username: '',
          password: '',
          phone: '',
          email: '',
          assistEmail: '',
          channelUrl: '',
          dayMax: '',
          titleLimit: '',
          loginUrl: '',
          homeUrl: '',
          cookieDomain: '',
          channelDesc: '',
          accDue: ''
        }
        this.title = '新增'
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
            let postData = Object.assign({}, this.paramsForm)
            delete postData.channelType
            delete postData.channelName
            postData.channelId = this.channelId
            debugger
            if (typeof postData.accDue === 'string')  delete postData.accDue
            this.$post('channelInfo/update', postData).then((r) => {
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
            this.confirmLoading = false
          } else {
            this.$post('channelInfo/add', this.paramsForm).then((r) => {
              if (r.data.message === 'badRequest' || r.data.message === 'error') {
                this.$message.error(r.data.messageDetail)
                this.confirmLoading = false
              } else {
                this.$message.success(r.data.messageDetail)
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
