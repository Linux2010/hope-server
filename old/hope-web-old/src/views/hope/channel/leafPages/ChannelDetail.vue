<template>
  <a-modal :title="title" :width="1200" @cancel="handleCancel" :confirm-loading="confirmLoading" :visible="modalStatus" >
    <a-form-model ref="paramsFormRef" :form="paramsForm" :model="paramsForm" :label-col="formItemLayout.labelCol" :wrapper-col="formItemLayout.wrapperCol">
      <a-row>
        <a-col :span="12">
          <a-form-model-item label="频道类型" prop="channelType">
            <span class="spn">{{ paramsForm.channelType }}</span>
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="频道名称" prop="channelName">
            <span class="spn">{{ paramsForm.channelName }}</span>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item label="盈利状态" prop="gainStatus">
            <span class="spn">{{ paramsForm.gainStatus }}</span>
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item label="频道开关" prop="channelOffOn">
            <span class="spn">{{ paramsForm.channelOffOn == 'off' ? '关':'开' }}</span>
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="登录账号" prop="username">
            <span class="spn">{{ paramsForm.username }}</span>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="登录密码" prop="password">
            <span class="spn">{{ paramsForm.password }}</span>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="手机号" prop="phone">
            <span class="spn">{{ paramsForm.phone }}</span>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="电子邮箱" prop="email">
            <span class="spn">{{ paramsForm.email }}</span>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="辅助信息" prop="assistEmail">
            <span class="spn">
              <a class="href" :href="paramsForm.assistEmail" target="_blank">{{ paramsForm.assistEmail }}</a>
              </span>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="频道url" prop="channelUrl">
            <span class="spn">{{ paramsForm.channelUrl }}</span>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="每日上传上限" prop="dayMax">
            <span class="spn">{{ paramsForm.dayMax }}</span>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="账号过期时间" prop="accDue">
            <span class="spn">{{ paramsForm.accDue }}</span>
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
      title: '查看',
      confirmLoading: false,
      formItemLayout: {
        labelCol: { span: 5 },
        wrapperCol: { span: 17 }
      },
      paramsForm: {},
      channelId: ''
    }
  },
  mounted () {},
  methods: {
    getChannelDetail (channelId) {
      this.$get('channelInfo/selectDetail', {channelId}).then((r) => {
        this.paramsForm = r.data.data
      })
    },
    channelOffOnHeader (e) {
      this.channelOffOn = e
      this.paramsForm.channelOffOn = e ? 'on' : 'off'
    },
    showModal (row) {
      let channelId = row.channelId
      this.getChannelDetail(channelId)
      this.modalStatus = true
    },
    handleCancel () {
      this.modalStatus = false
    }
  }
}
</script>
<style lang="less" scoped>
.spn{
  color:#999;
}
</style>
