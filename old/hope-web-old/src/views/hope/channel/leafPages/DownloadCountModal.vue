<template>
  <a-modal :title="title" :width="900" @cancel="handleCancel" :visible="modalStatus" :footer="null">
    <a-table ref="TableInfo" :rowKey="(record,index) =>record.engine_id +index" :columns="columns" :dataSource="dataSource" :pagination="false"  :loading="loading">
      <span slot="download_enable" slot-scope="text, record">
        <a-switch :disabled="true" checked-children="开" un-checked-children="关" :checked="record.download_enable == 'true' ? true : false" />
      </span>
    </a-table>
  </a-modal>
</template>
<script>
export default {
  data () {
    return {
      modalStatus: false,
      title: '已管理下载项',
      dataSource: [],
      loading: false
    }
  },
  computed: {
    columns () {
      return [{
        title: '引擎编号',
        dataIndex: 'engine_id'
      }, {
        title: '引擎名称',
        dataIndex: 'engine_name'
      }, {
        title: '下载编号',
        dataIndex: 'download_type'
      }, {
        title: '下载名称',
        dataIndex: 'download_name'
      }, {
        title: '开关状态',
        dataIndex: 'download_enable',
        key: 'download_enable',
        scopedSlots: { customRender: 'download_enable' }
      }]
    }
  },
  methods: {
    handleCancel () {
      this.modalStatus = false
    },
    showModal (row) {
      this.loading = true
      this.$get('downloadInfo/queryDownloadInfoByChannelName', {
        channelName: row.channelName
      }).then((r) => {
        this.loading = false
        this.dataSource = r.data.data
        this.modalStatus = true
      })
    }
  }
}
</script>
