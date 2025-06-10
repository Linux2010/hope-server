<template>
  <a-modal :title="title" :width="1250" @cancel="handleCancel" :height="1000" :visible="modalStatus">
      <!-- 表格区域 -->
      <a-table ref="TableInfo" :rowKey="(r,i)=>{ return i}" :columns="columns" :dataSource="dataSource" :pagination="pagination" :loading="loading" @change="handleTableChange" :scroll="{ x: 900 }">
        <span slot="downloadEnable" slot-scope="text, record">
          {{record.downloadEnable == 'true' ? '开' : '关' }}
        </span>
      </a-table>
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
      title: '上传记录',
      dataSource: [],
      paginationInfo: null,
      pagination: {
        pageSizeOptions: ['10', '20', '30', '40', '100'],
        defaultCurrent: 1,
        defaultPageSize: 10,
        showQuickJumper: true,
        showSizeChanger: true,
        showTotal: (total, range) => `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`
      },
      loading: false,
      row: {}
    }
  },
  computed: {
    columns () {
      return [{
        title: '频道类型',
        width: 100,
        ellipsis: true,
        dataIndex: 'channelType'
      }, {
        title: '频道名称',
        width: 120,
        ellipsis: true,
        dataIndex: 'channelName'
      }, {
        title: '下载名称',
        width: 120,
        ellipsis: true,
        dataIndex: 'downloadName'
      }, {
        title: '下载类型',
        width: 120,
        ellipsis: true,
        dataIndex: 'downloadType'
      }, {
        title: '上传命令',
        width: 120,
        ellipsis: true,
        dataIndex: 'uploadCmd'
      }, {
        title: '上传日期',
        width: 120,
        ellipsis: true,
        dataIndex: 'uploadDate'
      }, {
        title: '上传信息名称',
        width: 120,
        ellipsis: true,
        dataIndex: 'uploadName'
      }, {
        title: '视频名称',
        width: 120,
        ellipsis: true,
        dataIndex: 'videoName'
      }, {
        title: '视频地址',
        width: 120,
        ellipsis: true,
        dataIndex: 'videoUrl'
      }, {
        title: '创建时间',
        width: 120,
        ellipsis: true,
        dataIndex: 'createTime'
      }, {
        title: '修改时间',
        width: 120,
        ellipsis: true,
        dataIndex: 'modifyTime'
      }]
    }
  },
  methods: {
    getQueryList (params = {}) {
      this.loading = true
      if (this.paginationInfo) {
        this.$refs.TableInfo.pagination.current = this.paginationInfo.current
        this.$refs.TableInfo.pagination.pageSize = this.paginationInfo.pageSize
        params.pageSize = this.paginationInfo.pageSize
        params.pageNum = this.paginationInfo.current
      } else {
        params.pageSize = this.pagination.defaultPageSize
        params.pageNum = this.pagination.defaultCurrent
      }
      let data = {
        channelName: this.row.channelName,
        channelType: this.row.channelType
      }
      this.$get('uploadInstance/queryList', {
        ...params,
        ...data
      }).then((r) => {
        let data = r.data
        const pagination = { ...this.pagination }
        pagination.total = data.total
        this.loading = false
        this.dataSource = data.rows
        this.pagination = pagination
      })
    },
    handleCancel () {
      this.modalStatus = false
      this.dataSource = []
    },
    showModal (row) {
      this.modalStatus = true
      this.row = row
      this.getQueryList()
    },
    handleTableChange (pagination, filters, sorter) {
      this.paginationInfo = pagination
      this.getQueryList()
    }
  }
}
</script>
<style lang="less" scoped>
/deep/ .ant-table-body{
  min-height: 120px;
}
//或者
/deep/ .ant-table-body{
  min-height: 120px;
}
</style>
