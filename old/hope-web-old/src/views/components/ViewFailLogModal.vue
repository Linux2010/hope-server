<template>
  <a-modal :title="title" :width="1200" @cancel="handleCancel" :confirm-loading="confirmLoading" :visible="modalStatus" >
    <div v-for="(item,index) in logDetailArr" :key="index" class="instance">
      <div class="instance-item">
        <span>执行脚本:&ensp;{{item.create_time}}</span>
        <span v-html="item.upload_cmd"></span>
      </div>
      <div class="instance-item">
        <span>执行日志:&ensp;{{item.create_time}}</span>
        <span v-html="item.upload_log"></span>
      </div>
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
      confirmLoading: false,
      logDetailArr: [],
      engineName: '',
      videoPath: ''
    }
  },
  methods: {
    showModal (row) {
      this.engineName = row.engineName
      this.videoPath = row.videoPath
      this.modalStatus = true
      this.getLogDetail()
    },
    getLogDetail () {
      // let data = new FormData()
      // data.append('engineName', this.engineName || 1)
      // data.append('videoPath', this.videoPath || '/test/test2')
      const data = {
        'engineName': this.engineName || 1,
        'videoPath': this.videoPath || '/test/test2'
      }
      this.$post('uploadInstance/queryFailLog', JSON.stringify(data)).then((r) => {
        // this.logDetailArr = r.data.data
        let logDetailArr = r.data.data
        logDetailArr.forEach(ele => {
          if (ele.upload_log) {
            ele.upload_log = ele.upload_log.replace(/\n/g, '<br/>')
          }
        })
        this.logDetailArr = logDetailArr
      })
    },
    handleCancel () {
      this.modalStatus = false
    }
  }
}
</script>
<style lang="less">
  .instance{
    background:#f7f7f7;
    margin-bottom:20px;
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-items: flex-start;
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
  }
  .ant-modal-body{
    height: 600px;
    overflow-y: scroll;
  }
</style>
