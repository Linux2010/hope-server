<template>
  <a-modal :title="title" :width="1250" @cancel="handleCancel" @ok="handleOk" :height="400" :visible="modalStatus">
      <a-form-model ref="paramsFormRef" :form="paramsForm" :model="paramsForm" :label-col="formItemLayout.labelCol" :wrapper-col="formItemLayout.wrapperCol">
        <a-row>
          <a-col :span="12">
            <a-form-item prop="downloadName">
              <a-input v-model="paramsForm.downloadName" placeholder="请输入下载名称" />
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <a-button type="primary" @click="handleSearch">查 询</a-button>
          </a-col>
        </a-row>
      </a-form-model>
      <!-- 表格区域 -->
      <a-table ref="TableInfo" :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange, type: 'radio' }" :rowKey="(r,i)=>{ return i}" :columns="columns" :dataSource="dataSource" :pagination="pagination" :loading="loading" @change="handleTableChange" :scroll="{ x: 900, y: 300 }" :customRow="loadCustomRow">
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
      title: '新增/选择下载信息',
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
        downloadName: ''
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
        title: '引擎序号',
        width: 120,
        ellipsis: true,
        dataIndex: 'engineId'
      }, {
        title: '引擎名称',
        width: 120,
        ellipsis: true,
        dataIndex: 'engineName'
      }, {
        title: '开关状态',
        width: 120,
        ellipsis: true,
        scopedSlots: { customRender: 'downloadEnable' },
        dataIndex: 'downloadEnable'
      }, {
        title: '已关联频道',
        dataIndex: 'channelCount',
        key: 'channelCount',
        ellipsis: true,
        width: 120,
        scopedSlots: { customRender: 'channelCount' }
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
    handleCancel () {
      this.selectedRowKeys = []
      this.selectedRows = []
      this.selectedTaskOrder = ''
      this.$refs.paramsFormRef.resetFields()
      this.modalStatus = false
      this.dataSource = []
    },
    handleOk () {
      this.$refs.paramsFormRef.resetFields()
      this.modalStatus = false
      this.dataSource = []
      this.$emit('getDownloadName', this.record)
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
      this.$get('downloadInfo/queryList', {
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
