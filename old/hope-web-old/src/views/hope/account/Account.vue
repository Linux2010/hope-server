<template>
  <div style="width: 100%">
    <a-card :bordered="false" class="card-area">
      <!-- 搜索区域 -->
      <a-form layout="horizontal">
        <a-row :gutter="16" style="margin-bottom:10px;">
          <a-col :span="4">
            <a-form-item>
              <a-input placeholder="请输入账号名称" v-model="queryParams.accName"/>
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <span style="float: left; margin-top: 3px;margin-right:10px;">
              <a-button type="primary" @click="searchHandle">查询</a-button>
            </span>
            <span style="float: left; margin-top: 3px;">
              <a-button type="primary" @click="addHandle">新增</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
      <!-- 表格区域 -->
      <a-table
        ref="TableInfo"
        :rowKey="record =>record.id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="pagination"
        :loading="loading"
        @change="handleTableChange"
        :scroll="{ x: 900 }">
        <template slot="operation" slot-scope="text, record">
          <a href="javascript:void(0)" @click="handleGoDetail(record)">编辑</a>&ensp;&ensp;
          <a-popconfirm
            title="确定删除吗？"
            trigger="click"
            ok-text="确定"
            cancel-text="取消"
            @confirm="handleDelete(record)">
              <a href="javascript:void(0)">删除&ensp;&ensp;</a>
          </a-popconfirm>
        </template>
      </a-table>
    </a-card>
    <ViewInstanceModal ref="viewInstanceModalRef" @refresh="handleRefresh"/>
  </div>
</template>
<script>
import ViewInstanceModal from './components/ViewInstanceModal.vue'
export default {
  components: {ViewInstanceModal},
  data () {
    return {
      advanced: false,
      fileList: [],
      importData: [],
      times: '',
      errors: [],
      uploading: false,
      importResultVisible: false,
      dataSource: [],
      paginationInfo: null,
      sortedInfo: null,
      queryParams: {
        accName: ''
      },
      pagination: {
        pageSizeOptions: ['10', '20', '30', '40', '100'],
        defaultCurrent: 1,
        defaultPageSize: 10,
        showQuickJumper: true,
        showSizeChanger: true,
        showTotal: (total, range) => `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`
      },
      loading: false,
      filteredInfo: null
    }
  },
  computed: {
    columns () {
      let { sortedInfo,filteredInfo } = this
      sortedInfo = sortedInfo || {}
      filteredInfo = filteredInfo || {}
      return [ {
        title: '账号名称',
        dataIndex: 'accName',
        ellipsis: true,
        width: 140
      }, {
        title: '账号描述',
        dataIndex: 'accDesc',
        ellipsis: true,
        width: 140
      },{
        title: '账号类型',
        dataIndex: 'accType',
        ellipsis: true,
        width: 140,
        customRender: (text, row, index) => {
          switch (text) {
            case 'phone':
              return <a-tag color="cyan">phone</a-tag>
            case 'email':
              return <a-tag color="red">email</a-tag>
            default:
              return text
          }
        },
        filters: [
          { text: 'phone', value: 'phone' },
          { text: 'email', value: 'email' }
        ],
        filterMultiple: false,
        filteredValue: filteredInfo.accType || null,
        onFilter: (value, record) => record.accType.includes(value)
      }, {
        title: '余额',
        dataIndex: 'balance',
        ellipsis: true,
        sorter: true,
        sortOrder: sortedInfo.columnKey === 'balance' && sortedInfo.order,
        width: 100
      }, {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: { customRender: 'operation' },
        fixed: 'right',
        width: 140
      }]
    }
  },
  mounted () {
    this.fetch()
  },
  methods: {
    searchHandle () {
      this.fetch(this.queryParams)
    },
    handleDelete (row) {
      this.$post('accountInfo/' + row.accId, {}).then((r) => {
        if (r.data.message === 'error') {
          this.$message.error('删除失败！')
        } else {
          this.$message.success('删除成功！')
          this.fetch()
        }
      })
    },
    handleRefresh () {
      this.fetch()
    },
    addHandle () {
      this.$refs.viewInstanceModalRef.showModal()
    },
    handleGoDetail (row) {
      this.$refs.viewInstanceModalRef.showModal(row)
    },
    handleTableChange (pagination, filters, sorter) {
      this.paginationInfo = pagination
      this.filteredInfo = filters
      this.sortedInfo = sorter
      this.fetch({
        sortField: sorter.field,
        sortOrder: sorter.order
      })
    },
    handleSearch () {
      // this.paginationInfo = null
      this.fetch({
        ...this.filteredInfo,
        ...this.paramsForm
      })
    },
    fetch (params = {}) {
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
      this.$get('accountInfo/list', {
        ...params
      }).then((r) => {
        let data = r.data
        const pagination = { ...this.pagination }
        pagination.total = data.total
        this.loading = false
        this.dataSource = data.rows
        this.pagination = pagination
      })
    }
  },
  beforeRouteEnter: (to, from, next) => {
    next(vm => {
      vm.fetch()
    })
  }
}
</script>

<style lang="less" scoped>
  @import "../../../../static/less/Common";
  .option-area {
    display: inline-block;
    width: 100%;
    padding: 0 .9rem;
    margin: .5rem 0;
    .upload-area {
      display: inline;
      float: left;
      width: 50%
    }
    .button-area {
      margin-left: 1rem;
      display: inline;
      float: right;
      width: 30%;
    }
  }
</style>
