<template>
  <a-modal :title="title" :width="1200" @ok="handleOk" @cancel="handleCancel" :confirm-loading="confirmLoading" :visible="modalStatus" >
    <a-form-model ref="paramsFormRef" :form="paramsForm" :model="paramsForm" :rules="rules" :label-col="formItemLayout.labelCol" :wrapper-col="formItemLayout.wrapperCol">
      <a-row>
        <a-col :span="12">
          <a-form-item label="引擎名称" prop="engineName">
            <a-input v-if="editBoolean" disabled v-model="paramsForm.engineName" />
            <a-input v-else v-model="paramsForm.engineName" placeholder="请输入引擎名称" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="引擎服务器IP" prop="engineIp">
            <a-input v-model="paramsForm.engineIp" placeholder="请输入引擎服务器IP" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="服务器用户" prop="username">
            <a-input v-model="paramsForm.username" placeholder="请输入服务器用户" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="服务器密码" prop="password">
            <a-input v-model="paramsForm.password" type="password" placeholder="请输入服务器密码" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="引擎状态" prop="engineOffOn">
            <a-switch checked-children="开" un-checked-children="关" :checked="engineOffOn" @change="channelOffOnHeader"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="引擎描述" prop="engineDesc">
            <a-input v-model="paramsForm.engineDesc" placeholder="请输入引擎描述" />
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
      uploadEnable: false,
      logDetail: {},
      paramsForm: {
        engineName: '',
        engineIp: '',
        username: '',
        password: '',
        engineStatus: '',
        engineDesc: ''
      },
      rules: {
        engineName: [{ required: true, message: '请输入账号名称', trigger: 'blur' }],
        engineIp: [{ required: true, message: '请输入账号手机', trigger: 'change' }]
      },
      editBoolean: false,
      accId: '',
      engineOffOn: ''
    }
  },
  methods: {
    channelOffOnHeader (e) {
      this.engineOffOn = e
      this.paramsForm.engineStatus = e
    },
    showModal (row) {
      if (row) {
        this.editBoolean = true
        this.title = '修改'
        this.engineId = row.engineId
        Object.keys(this.paramsForm).forEach(key => { this.paramsForm[key] = row[key] })
      } else {
        this.editBoolean = false
        this.title = '新增'
        Object.keys(this.paramsForm).forEach(key => { this.paramsForm[key] = '' })
      }
      this.modalStatus = true
    },
    handleCancel () {
      this.modalStatus = false
    },
    handleOk () {
      // this.paramsForm.engineStatus = this.engineOffOn
      if (this.editBoolean) {
        this.paramsForm.engineId = this.engineId
        this.$post('engineInfo/update', {
          ...this.paramsForm
        }).then((r) => {
          if (r.data.message === 'badRequest') {
            this.$message.error(r.data.messageDetail)
          } else {
            this.$emit('refresh')
            this.modalStatus = false
          }
        })
      } else {
        this.$post('engineInfo/add', {
          ...this.paramsForm
        }).then((r) => {
          if (r.data.message === 'badRequest') {
            this.$message.error(r.data.messageDetail)
          } else {
            this.$emit('refresh')
            this.modalStatus = false
          }
        })
      }
    }
  }
}
</script>
<style lang="less">
  .instance-item{
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-items: flex-start;
    padding:20px 20px 10px 20px;
    width:98%;
    border-radius: 6px;
    span{
      font-size:14px;
      color:#333;
      padding-bottom:16px;
      &:last-child{
        font-size:14px;
        color:#666;
        width:99%;
      }
    }
  }
</style>
