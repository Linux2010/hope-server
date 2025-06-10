<template>
  <a-modal :title="title" :width="900" @cancel="handleCancel" :visible="modalStatus" :footer="null">
    <a-table ref="TableInfo" :rowKey="(record,index) =>record.engine_id +index" :columns="columns" :dataSource="dataSource" :pagination="false"  :loading="loading">
      <span slot="cookie_enable" slot-scope="text, record">
        <a-tag :color="record.cookie_enable === 'true' ? '#87d068' : '#f50'">
          {{record.cookie_enable === 'true' ? '正常' : '报警' }}
        </a-tag>
      </span>
      <span slot="channel_off_on" slot-scope="text, record">
        <!-- <a-tag :color="record.channel_off_on === 'on' ? '#87d068' : '#f50'"> -->
          {{record.channel_off_on === 'on' ? '开' : '关' }}
        <!-- </a-tag> -->
      </span>
      <!-- <span slot="channel_off_on" slot-scope="text, record">
        <a-switch checked-children="开" un-checked-children="关" :checked="record.channel_off_on == 'on' ? true : false" />
      </span> -->
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
        title: '频道名称',
        dataIndex: 'channel_name'
      }, {
        title: '频道类型',
        dataIndex: 'channel_type'
      }, {
        title: 'cookie预警',
        dataIndex: 'cookie_enable',
        ellipsis: true,
        scopedSlots: { customRender: 'cookie_enable' }
      }, {
        title: '开关状态',
        dataIndex: 'channel_off_on',
        key: 'channel_off_on',
        scopedSlots: { customRender: 'channel_off_on' }
      }]
    }
  },
  methods: {
    handleCancel () {
      this.modalStatus = false
    },
    showModal (row) {
      this.loading = true
      this.$get('channelInfo/queryChannelInfoByDownloadName', {
        downloadName: row.downloadName
      }).then((r) => {
        this.loading = false
        this.dataSource = r.data.data
        this.modalStatus = true
      })
    }
  }
}
</script>
