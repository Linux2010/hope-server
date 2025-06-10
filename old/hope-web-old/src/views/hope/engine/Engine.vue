<template>
  <div style="width: 100%">
    <a-card :bordered="false" class="card-area">
      <!-- 查询区域 -->
      <a-form layout="horizontal">
        <a-row :gutter="12">
         <a-col :span="4">
            <a-form-item>
              <a-input  placeholder="请输入引擎名称" v-model="queryParams.engineName"/>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <span style="float: left; margin-top: 3px;">
              <a-button type="primary" @click="handleSearch">查询</a-button>
              <a-button style="margin-left: 8px" @click="handleAdd">新增</a-button>
              <a-button style="margin-left: 8px" @click="handleReset">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
      <!-- 表格区域 -->
      <a-table ref="TableInfo" :rowKey="record =>record.engineId" :columns="columns" :dataSource="dataSource" :pagination="pagination" :loading="loading" @change="handleTableChange" :scroll="{ x: 900 }">
        <span slot="engineStatus" slot-scope="text, record">
          <a-switch checked-children="开" un-checked-children="关" :checked="record.engineStatus == 'true' ? true : false"/>
        </span>
        <template slot="operation" slot-scope="text, record">
          <a href="javascript:void(0)" @click="handleGoDetail(record)">编辑</a>&ensp;&ensp;
          <!-- <a-popconfirm
            title="确定删除吗？"
            trigger="click"
            ok-text="确定"
            cancel-text="取消"
            @confirm="handleDelete(record)">
              <a href="javascript:void(0)">删除&ensp;&ensp;</a>
          </a-popconfirm>           -->
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
      queryParams: {
        engineName: null
      },
      advanced: false,
      fileList: [],
      importData: [],
      times: '',
      errors: [],
      uploading: false,
      importResultVisible: false,
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
      loading: false
    }
  },
  computed: {
    columns () {
      return [{
        title: '引擎序号',
        ellipsis: true,
        dataIndex: 'engineId',
        width: 100
      }, {
        title: '引擎名称',
        ellipsis: true,
        dataIndex: 'engineName',
        sorter: true,
        width: 120
      }, {
        title: '网络状态',
        ellipsis: true,
        dataIndex: 'engineNetStatus',
        width: 120
      }, {
        title: '引擎状态',
        ellipsis: true,
        dataIndex: 'engineStatus',
        width: 120
      }, {
        title: '引擎描述',
        ellipsis: true,
        dataIndex: 'engineDesc'
      }, {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: { customRender: 'operation' },
        fixed: 'right',
        width: 180
      }]
    }
  },
  mounted () {
    this.fetch()
  },
  methods: {
    // handleDelete (row) {
    //   this.$post('accountInfo/' + row.accId, {}).then((r) => {
    //     if (r.data.message === 'error') {
    //       this.$message.error('删除失败！')
    //     } else {
    //       this.$message.success('删除成功！')
    //       this.fetch()
    //     }
    //   })
    // },
    handleGoDetail (row) {
      this.$refs.viewInstanceModalRef.showModal(row)
    },
    handleRefresh () {
      this.fetch()
    },
    handleAdd () {
      this.$refs.viewInstanceModalRef.showModal()
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
    handleReset () {
      this.queryParams = {}
      this.fetch()
    },
    handleSearch () {
      this.paginationInfo = null
      this.fetch()
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
      this.$get('engineInfo/list', {
        ...params,
        ...this.queryParams
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
