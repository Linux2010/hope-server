<template>
  <a-modal :title="title" :width="1200" @cancel="handleCancel" :confirm-loading="confirmLoading" :visible="modalStatus" >
    <div class="instance-item">
      <span>上传命令</span>
      <span v-html="uploadCmd"></span>
    </div>
    <div class="instance-item">
      <span>上传日志记录</span>
      <span v-html="uploadLog"></span>
    </div>
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
      // logDetail: {}
      uploadCmd: '',
      uploadLog: ''
    }
  },
  methods: {
    showModal (row) {
      this.modalStatus = true
      this.getLogDetail(row)
    },
    getLogDetail (row) {
      this.$get('uploadInstance/selectDetail', {
        instanceId: row.instanceId
      }).then((r) => {
        let data = r.data.data
        this.uploadCmd = data.uploadCmd ? data.uploadCmd.replace(/\n/g, '<br/>') : ''
        this.uploadLog = data.uploadLog ? data.uploadLog.replace(/\n/g, '<br/>') : ''
      })
    },
    handleCancel () {
      this.modalStatus = false
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
  // .ant-modal-body{
  //   height: 600px;
  //   overflow-y: scroll;
  // }
</style>
