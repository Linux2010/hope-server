<template>
  <div style="width: 100%">
    <a-card :bordered="false" class="card-area">
      <!-- 搜索区域 -->
      <a-form layout="horizontal">
        <a-row :gutter="12">
          <a-col :span="4">
            <a-form-item prop="downloadName">
              <a-input  placeholder="请输入下载信息名称" v-model="paramsForm.downloadName"/>
            </a-form-item>
          </a-col>

          <a-col :span="4">
            <a-form-item prop="videoPath">
              <a-input  placeholder="请输入视频路径或名称" v-model="paramsForm.videoPath"/>
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <span style="float: left; margin-top: 3px;">
              <a-button type="primary" @click="handleSearch">查询</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
      <!-- 表格区域 -->
      <a-table ref="TableInfo"
               :columns="columns"
               :rowKey="(r,i)=>{ return i}"
               :dataSource="dataSource"
               :pagination="pagination"
               :loading="loading"
               @change="handleTableChange"
               :scroll="{ x: 900 }">
        <template slot="operation" slot-scope="text, record">
          <a-popconfirm
            title="确定清理视频吗？"
            trigger="click"
            ok-text="确定"
            cancel-text="取消"
            @confirm="handleCleanVideo(record)">
            <a href="javascript:void(0)">手动清理</a>
          </a-popconfirm>
        </template>
        <span slot="videoUrl" slot-scope="text">
          <a class="href" :href="text" target="_blank">{{text}}</a>
        </span>
      </a-table>
    </a-card>
    <ViewInstanceModal ref="viewInstanceModalRef" />
  </div>
</template>
<script>
import moment from 'moment'
import ViewInstanceModal from './components/ViewDownloadInstanceModal.vue'
export default {
  components: {ViewInstanceModal},
  data () {
    return {
      showToday: false,
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
      loading: false,
      paramsForm: {
        videoName: '',
        downloadName: '',
        translationTitle: '',
        cleanFlag: ''
      },
      fromChannelPage: false,
      instanceStatus: null,
      filteredInfo: null,
      sortedInfo: null

    }
  },
  computed: {
    columns () {
      let { sortedInfo,filteredInfo } = this
      sortedInfo = sortedInfo || {}
      filteredInfo = filteredInfo || {}
      return [{
        title: '下载名称',
        dataIndex: 'downloadName',
        ellipsis: true,
        width: 100
      }, {
        title: '视频路径',
        dataIndex: 'videoPath',
        ellipsis: true,
        width: 240
      }, {
        title: '翻译视频名称',
        dataIndex: 'translationTitle',
        ellipsis: true,
        width: 240
      }, {
        title: '视频size/mb',
        dataIndex: 'videoSize',
        sorter: true,
        sortOrder: sortedInfo.columnKey === 'videoSize' && sortedInfo.order,
        ellipsis: true,
        width: 80
      }, {
        title: '上传状态',
        dataIndex: 'cleanFlag',
        width: 80,
        scopedSlots: { customRender: 'cleanFlag' },
        filters: [
          { text: '已上传', value: 0 },
          { text: '手动清理', value: 1 },
          { text: '未上传', value: 2 }
        ],
        customRender: (text, row, index) => {
          switch (text) {
            case 1:
              return <a-tag color="orange">手动清理</a-tag>
            case 0:
              return <a-tag color="cyan">已上传</a-tag>
            case 2:
              return <a-tag color="red">未上传</a-tag>
            default:
              return text
          }
        },
        filterMultiple: false,
        filteredValue: filteredInfo.cleanFlag || null,
        onFilter: (value, record) => record.cleanFlag===value
      },{
        title: '视频日期',
        dataIndex: 'createTime',
        ellipsis: true,
        sorter: true,
        sortOrder: sortedInfo.columnKey === 'createTime' && sortedInfo.order,
        width: 120
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
    if (!this.fromChannelPage) {
      this.fetch()
    }
  },
  methods: {
    handleDateChange (e) {
      if (!e) {
        this.paramsForm.uploadDate = ''
        this.uploadDate = null
      }
    },
    handleCleanVideo (row) {
      delete row.createTime
      delete row.modifyTime
      delete row.fileCreateTime
      this.$post('downloadInstance/cleanVideo', row).then((r) => {
        if (r.data.message === 'error') {
          this.$message.error(r.data.messageDetail)
        } else {
          this.$message.success('清理成功！')
          this.fetch({
            sortField: this.sortedInfo.field,
            sortOrder: this.sortedInfo.order
          })
        }
      })
    },
    resetParamsForm () {
      this.paramsForm = {
        channelType: undefined,
        channelName: '',
        uploadDate: ''
      }
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
      // 下拉框提示语处理
      if (this.paramsForm.channelType === undefined) {
        this.paramsForm.channelType = ''
      }
      const date = this.uploadDate

      this.$get('downloadInstance/queryList', {
        ...params,
        ...this.paramsForm
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
      if (from.path === '/hope/download') {
        vm.paramsForm.downloadName = to.query.downloadName || ''
        vm.fetch()
      } else {
        vm.paramsForm.downloadName = ''
        vm.fetch()
      }
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
  .href{
    text-decoration: underline;
    color:#999;
  }
</style>
