<template>
  <div style="width: 100%">
    <!-- 搜索区域 -->
    <a-form layout="horizontal">
      <a-row :gutter="16">
        <a-col :span="3">
          <a-form-item prop="engineName">
            <a-select v-model="paramsForm.engineName" placeholder="引擎名称" allowClear>
              <a-select-option :value="item.engineName" v-for="item in engineNameList" :key="item.engineId">
                {{item.engineName}}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="3">
          <a-form-item prop="downloadType">
            <a-select v-model="paramsForm.downloadType" placeholder="下载类型" allowClear>
              <a-select-option :value="item.keyy" v-for="item in downloadTypeList" :key="item.DICT_ID">
                {{item.valuee}}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="4">
          <a-form-item prop="downloadName">
            <a-input  placeholder="下载名称" v-model="paramsForm.downloadName"/>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <span style="float: left; margin-top: 3px;">
            <a-button type="primary" @click="handleSearch">查询</a-button>
            <a-button style="margin-left: 8px" @click="handleReset">重置</a-button>
          </span>
          <span style="float: left; margin-top: 3px;margin-left:10px;" @click="handleAdd">
            <a-button type="primary">新增</a-button>
          </span>
        </a-col>
      </a-row>
    </a-form>
    <a-card :bordered="false" class="card-area">
      <!-- 表格区域 -->
      <a-table ref="TableInfo"
               :rowKey="(r,i)=>{ return i}"
               :columns="columns"
               :dataSource="dataSource"
               :pagination="pagination"
               :loading="loading"
               @change="handleTableChange"
               :scroll="{ x: 900 }">
        <a slot="channelCount" slot-scope="text,record" @click="handleChannelCount(record)">{{ text || 0 }}</a>
        <span slot="downloadEnable" slot-scope="text, record">
          <a-switch checked-children="开" un-checked-children="关" :checked="record.downloadEnable == 'true' ? true : false" @change="uploadEnableHeader(record)"/>
        </span>
        <span slot="downloadName" slot-scope="text,record">
          <a class="href" :href="record.downloadUrl" target="_blank">{{text}}</a>
        </span>
        <template slot="operation" slot-scope="text, record">
          <a href="javascript:void(0)" @click="handleDownloadInstanceList(record)">下载记录</a>
          <a href="javascript:void(0)" @click="handleEdit(record)">编辑&ensp;</a>
          <a href="javascript:void(0)" @click="handleView(record)">详情&ensp;</a>
          <a-popconfirm
            title="确定删除吗？"
            trigger="click"
            ok-text="确定"
            cancel-text="取消"
            @confirm="handleDelete(record.downloadId)">
              <a href="javascript:void(0)">删除</a>
          </a-popconfirm>
        </template>
      </a-table>
    </a-card>
    <ChannelCountModal ref="channelCountModalRef" />
    <EditDownloadModal ref="editDownloadModalRef" @refresh="handleRefresh"/>
    <ViewDownloadModal ref="viewDownloadModalRef" />
  </div>
</template>
<script>
import ChannelCountModal from './components/ChannelCountModal.vue'
import EditDownloadModal from './components/EditDownloadModal.vue'
import ViewDownloadModal from './components/ViewDownloadModal.vue'
export default {
  components: {EditDownloadModal, ViewDownloadModal, ChannelCountModal},
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
        engineName: undefined,
        downloadType: undefined,
        downloadName: ''
      },
      downloadTypeList: this.$store.state.account.dictList['downloadType'] || [],
      engineNameList: [],
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
        title: '引擎',
        dataIndex: 'engineName',
        sorter: true,
        ellipsis: true,
        width: 80
      }, {
        title: '类型',
        dataIndex: 'downloadType',
        ellipsis: true,
        width: 100
      }, {
        title: '下载名称',
        dataIndex: 'downloadName',
        ellipsis: true,
        width: 180,
        scopedSlots: { customRender: 'downloadName' }
      },{
        title: '开关',
        dataIndex: 'downloadEnable',
        width: 80,
        scopedSlots: { customRender: 'downloadEnable' }
      },{
        title: '循环',
        dataIndex: 'cycleType',
        width: 100,
        scopedSlots: { customRender: 'cycleType' },
        filters: [
          { text: '增量', value: 'inc' },
          { text: '全量', value: 'all' },
        ],
        customRender: (text) => {
          switch (text) {
            case 'all':
              return <a-tag color="orange">全量</a-tag>
            case 'inc':
              return <a-tag color="cyan">增量</a-tag>
            default:
              return text
          }
        },
        filterMultiple: false,
        filteredValue: filteredInfo.cycleType || null,
        onFilter: (value, record) => record.cycleType.includes(value)

      }, {
        title: '关联',
        dataIndex: 'channelCount',
        ellipsis: true,
        key: 'channelCount',
        scopedSlots: { customRender: 'channelCount' },
        width: 60
      }, {
        title: '分类',
        dataIndex: 'videoType',
        ellipsis: true,
        width: 120
      },{
        title: '占用量',
        dataIndex: 'diskStorageInfo',
        ellipsis: true,
        width: 80
      }, {
        title: '创建时间',
        dataIndex: 'createTime',
        ellipsis: true,
        sorter: true,
        sortOrder: sortedInfo.columnKey === 'createTime' && sortedInfo.order,
        width: 160
      }, {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: { customRender: 'operation' },
        fixed: 'right',
        width: 280
      }]
    }
  },
  // mounted () {
  //   this.getEngineName()
  //   this.fetch()
  // },
  methods: {
    handleDownloadInstanceList (row) {
      // this.$router.push('/hope/instance')
      debugger
      this.$router.push({path: '/hope/downloadInstance', query: {downloadName: row.downloadName}})
      // this.$refs.uploadRecordsRef.showModal(row)
    },
    uploadEnableHeader (row) {
      const data = {
        downloadId: row.downloadId,
        downloadEnable: row.downloadEnable === 'true' ? 'false' : 'true'
      }
      this.$post('downloadInfo/update', data).then((r) => {
        this.$message.success(r.data.message)
        this.fetch()
      })
    },
    handleChannelCount (record) {
      if (record.channelCount > 0) {
        this.$refs.channelCountModalRef.showModal(record)
      }
    },
    getEngineName () {
      this.$get('engineInfo/queryAll').then((r) => {
        this.engineNameList = r.data
      })
    },
    handleRefresh () {
      this.fetch()
    },
    handleDelete (id) {
      this.$post('downloadInfo/' + id, {}).then((r) => {
        if (r.data.message === 'error') {
          this.$message.error('删除失败！')
        } else {
          this.$message.success('删除成功！')
          this.fetch()
        }
      })
    },
    handleEdit (row) {
      this.$refs.editDownloadModalRef.showModal(row)
    },
    handleAdd () {
      this.$refs.editDownloadModalRef.showModal()
    },
    handleView (row) {
      this.$refs.viewDownloadModalRef.showModal(row)
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
      this.paginationInfo = null
      this.fetch()
    },
    handleReset () {
      this.queryParams = {}
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
      // 下拉框提示语处理
      if (this.paramsForm.engineName === undefined) {
        this.paramsForm.engineName = ''
      }
      if (this.paramsForm.downloadType === undefined) {
        this.paramsForm.downloadType = ''
      }
      this.$get('downloadInfo/queryList', {
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
      vm.getEngineName()
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
  .href{
    text-decoration: underline;
    color:#999;
  }
</style>
