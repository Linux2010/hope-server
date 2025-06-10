<template>
  <a-modal :title="title" :width="1250" :height="400" @cancel="handleCancel" @ok="handleOk" :visible="modalStatus">
      <a-form-model ref="paramsFormRef" :form="paramsForm" :model="paramsForm" :label-col="formItemLayout.labelCol" :wrapper-col="formItemLayout.wrapperCol">
        <a-row>
          <a-col :span="12">
            <a-form-item prop="channelName">
              <a-input v-model="paramsForm.channelName" placeholder="请输入下载名称" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-button type="primary" @click="handleSearch">查询</a-button>
          </a-col>
        </a-row>
      </a-form-model>
      <!-- 表格区域 -->
      <a-table :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange, type: 'radio' }" ref="TableInfo" :rowKey="(r,i)=>{ return i}" :columns="columns" :dataSource="dataSource" :pagination="pagination" :loading="loading" @change="handleTableChange" :scroll="{ x: 900, y: 300 }" :customRow="loadCustomRow">
        <span slot="downloadEnable" slot-scope="text, record">
          {{record.downloadEnable == 'true' ? '开' : '关' }}
        </span>
      </a-table>
      <!-- <template slot="footer">
        <a-button @click="handleCancel">关闭</a-button>
      </template> -->
  </a-modal>
</template>
<script>
export default {
  data () {
    return {
      modalStatus: false,
      channelName: '',
      title: '新增/选择频道信息',
      dataSource: [],
      formItemLayout: {
        labelCol: { span: 5 },
        wrapperCol: { span: 17 }
      },
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
      paramsForm: {
        channelName: ''
      },
      selectedRowKeys: [],
      selectedRows: [],
      downloadName: '',
      selectedTaskOrder: '',
      record: {}
    }
  },
  computed: {
    columns () {
      return [{
        title: '频道名称',
        width: 120,
        ellipsis: true,
        dataIndex: 'channelName'
      }, {
        title: '频道类型',
        width: 120,
        ellipsis: true,
        dataIndex: 'channelType'
      }, {
        title: '频道状态',
        width: 120,
        ellipsis: true,
        dataIndex: 'channelOffOn'
      }, {
        title: 'cookie状态',
        width: 120,
        ellipsis: true,
        dataIndex: 'cookieEnable'
      }, {
        title: '已关联下载',
        dataIndex: 'downloadCount',
        key: 'downloadCount',
        ellipsis: true,
        width: 120,
        scopedSlots: { customRender: 'downloadCount' }
      }]
    }
  },
  methods: {
    loadCustomRow (record, index) {
      return {
        on: {
          click: () => { // 监听的是单选框的点击事件
            this.selectedRowKeys = []
            this.selectedRows = []
            this.selectedTaskOrder = ''
            this.selectedRowKeys.push(index)
            this.selectedRows.push(record)
            this.selectedTaskOrder = record
            this.record = record
          },
          change: () => { // 监听的是行的点击事件
            this.record = record
          }
        }
      }
    },
    onSelectChange (selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
      this.selectedTaskOrder = selectedRows[0]
    },
    handleOk () {
      this.$refs.paramsFormRef.resetFields()
      this.modalStatus = false
      this.dataSource = []
      this.$emit('getChannelNameName', this.record)
    },
    handleCancel () {
      this.selectedRowKeys = []
      this.selectedRows = []
      this.selectedTaskOrder = ''
      this.modalStatus = false
    },
    showModal (row) {
      this.modalStatus = true
      this.handleSearch()
    },
    handleSearch (params = {}) {
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
      this.$get('channelInfo/queryList', {
        ...params,
        ...this.paramsForm
      }).then((r) => {
        this.loading = false
        this.dataSource = r.data.rows
        const pagination = { ...this.pagination }
        pagination.total = r.data.total
        this.pagination = pagination
      })
    },
    handleTableChange (pagination, filters, sorter) {
      this.paginationInfo = pagination
      this.handleSearch()
    }
  }
}
</script>
<style lang="less" scoped>
/deep/ .ant-table-body{
  min-height: 120px;
}
/deep/ .ant-radio-inner{
  width:25px;
  height:25px;
}
/deep/ .ant-radio-inner::after {
  top: 5px;
  left: 5px;
  width: 13px;
  height: 13px;
}
</style>
