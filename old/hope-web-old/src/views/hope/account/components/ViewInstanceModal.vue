<template>
  <a-modal :title="title" :width="1200" @ok="handleOk" @cancel="handleCancel" :confirm-loading="confirmLoading" :visible="modalStatus" >
    <a-form-model ref="paramsFormRef" :form="paramsForm" :model="paramsForm" :rules="rules" :label-col="formItemLayout.labelCol" :wrapper-col="formItemLayout.wrapperCol">
      <a-row>
        <a-col :span="12">
          <a-form-item label="账号名称" prop="accName">
            <a-input v-if="editBoolean" disabled v-model="paramsForm.accName" />
            <a-input v-else v-model="paramsForm.accName" placeholder="请输入账号名称" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="账号类型" prop="accType">
            <a-select  v-model="paramsForm.accType" placeholder="请选择账号类型"  allowClear>
              <a-select-option :value="item.keyy" v-for="item in accTypeList" :key="item.DICT_ID">
                {{item.valuee}}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="账号描述" prop="accDesc">
            <a-input v-model="paramsForm.accDesc" placeholder="请输入账号描述" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="账号手机" prop="phone">
            <a-input v-model="paramsForm.phone" placeholder="请输入账号手机" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="手机余额" prop="balance">
            <a-input v-model="paramsForm.balance" placeholder="请输入手机余额" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="手机月租" prop="monthRent">
            <a-input v-model="paramsForm.monthRent" placeholder="请输入手机月租" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="机主" prop="personName">
            <a-input v-model="paramsForm.personName" placeholder="请输入机主名称" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="身份证" prop="identityCard">
            <a-input v-model="paramsForm.identityCard" placeholder="请输入机主身份证" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="账号邮箱" prop="email">
            <a-input v-model="paramsForm.email" placeholder="请输入账号邮箱" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="邮箱密码" prop="emailPwd">
            <a-input v-model="paramsForm.emailPwd" placeholder="请输入账号邮箱" />
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
        accName: '',
        accType: '',
        phone: '',
        email: '',
        emailPwd: '',
        balance: '',
        accDesc: '',
        personName: '',
        monthRent: '',
        identityCard: ''
      },
      rules: {
        accName: [{ required: true, message: '请输入账号名称', trigger: 'blur' }],
        phone: [{ required: true, message: '请输入账号手机', trigger: 'change' }]
      },
      editBoolean: false,
      accId: '',
      accTypeList: this.$store.state.account.dictList['acc_type'] || []
    }
  },
  methods: {
    showModal (row) {
      if (row) {
        this.editBoolean = true
        this.title = '修改'
        this.accId = row.accId
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
      if (this.editBoolean) {
        this.paramsForm.accId = this.accId
        this.$post('accountInfo/update', {
          ...this.paramsForm
        }).then((r) => {
          if (r.data.message === 'success') {
            this.$emit('refresh')
            this.modalStatus = false
            this.$message.success(r.data.message)
          } else {
            this.$message.error(r.data.messageDetail)
          }
        })
      } else {
        this.$post('accountInfo/add', {
          ...this.paramsForm
        }).then((r) => {
          if (r.data.message === 'success') {
            this.$emit('refresh')
            this.modalStatus = false
            this.$message.success(r.data.message)
          } else {
            this.$message.error(r.data.messageDetail)
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
