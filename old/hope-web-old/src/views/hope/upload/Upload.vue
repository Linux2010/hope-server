<template>
  <div style="width: 100%">
    <a-card :bordered="false" class="card-area">
      <a-form layout="horizontal">
        <a-row :gutter="16">
          <a-col :span="3">
            <a-form-item>
              <a-select  show-search  placeholder="频道类型" v-model="queryParams.channelType" allowClear>
                <a-select-option :value="item.keyy" v-for="item in channelTypeList" :key="item.DICT_ID" >
                 {{item.valuee}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="3">
            <a-form-item>
              <a-select  show-search  placeholder="下载类型" v-model="queryParams.downloadType" allowClear>
                <a-select-option :value="item.keyy" v-for="item in downloadTypeList" :key="item.DICT_ID">
                 {{item.valuee}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item>
              <a-input placeholder="请输入上传信息名称" v-model="queryParams.uploadName"/>
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <span style="float: left; margin-top: 3px;">
              <a-button type="primary" @click="handleSearch">查询</a-button>
            </span>&ensp;
            <span style="float: left; margin-top: 3px;margin-left: 6px;" @click="handleAdd" >
              <a-button type="primary">新增</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
      <!-- 表格区域 -->
      <a-table ref="TableInfo"
        :rowKey="(r,i)=>{ return i}"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="pagination"
        :loading="loading"
        @change="handleTableChange"
        :scroll="{ x: 1400 }">
        <span slot="uploadEnable" slot-scope="text, record">
          <a-switch checked-children="开" un-checked-children="关" :checked="record.uploadEnable == 'true' ? true : false" @change="uploadEnableHeader(record)"/>
        </span>
        <span slot="cookieEnable" slot-scope="text, record">
          <a-tag :color="record.cookieEnable ? '#87d068' : '#f50'">
            {{record.cookieEnable ? '正常' : '报警' }}
          </a-tag>
          <a-divider type="vertical" />
          <a>刷新</a>
        </span>
        <template slot="uploadDesc" slot-scope="text, record">
          <a-popover placement="topLeft">
            <template slot="content">
              <div>{{text}}</div>
            </template>
            <p style="width: 150px;margin-bottom: 0">{{text}}</p>
          </a-popover>
        </template>
        <template slot="operation" slot-scope="text, record">
          <a href="javascript:void(0)" @click="handleEdit(record)">编辑&ensp;</a>
          <a href="javascript:void(0)" @click="handleView(record)">详情&ensp;</a>
          <a-popconfirm
            title="确定删除吗？"
            trigger="click"
            ok-text="确定"
            cancel-text="取消"
            @confirm="handleDelete(record.uploadId)">
              <a href="javascript:void(0)">删除</a>
          </a-popconfirm>
        </template>
      </a-table>
    </a-card>
    <EditUploadModal ref="editUploadModalRef" @refresh="handleRefresh"/>
    <ViewUploadModal ref="viewUploadModalRef" />
  </div>
</template>
<script>
import EditUploadModal from './components/EditUploadModal.vue'
import ViewUploadModal from './components/ViewUploadModal.vue'
export default {
  components: {
    EditUploadModal,
    ViewUploadModal
  },
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
      filteredInfo: null,
      sortedInfo: null,
      pagination: {
        pageSizeOptions: ['10', '20', '30', '40', '100'],
        defaultCurrent: 1,
        defaultPageSize: 10,
        showQuickJumper: true,
        showSizeChanger: true,
        showTotal: (total, range) => `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`
      },
      queryParams: {
        channelType: undefined,
        downloadType: undefined
      },
      loading: false,
      channelTypeList: this.$store.state.account.dictList['channelType'] || [],
      downloadTypeList: this.$store.state.account.dictList['downloadType'] || []
    }
  },
  computed: {
    columns () {
      let { sortedInfo } = this
      sortedInfo = sortedInfo || {}
      return [{
        title: '上传名称',
        dataIndex: 'uploadName',
        ellipsis: true,
        width: 180
      }, {
        title: '下载名称',
        dataIndex: 'downloadName',
        ellipsis: true,
        width: 120
      }, {
        title: '频道名称',
        dataIndex: 'channelName',
        ellipsis: true,
        width: 120
      }, {
        title: '创建时间',
        dataIndex: 'createTime',
        sorter: true,
        ellipsis: true,
        sortOrder: sortedInfo.columnKey === 'createTime' && sortedInfo.order,
        width: 120
      }, {
        title: '开关',
        dataIndex: 'uploadEnable',
        scopedSlots: { customRender: 'uploadEnable' },
        width: 120
      }, {
        title: '每日上限',
        dataIndex: 'uploadDayMax',
        ellipsis: true,
        width: 100
      }, {
        title: '上传路径',
        dataIndex: 'uploadPath',
        ellipsis: true,
        width: 120
      }, {
        title: '上传脚本',
        dataIndex: 'uploadShellPath',
        ellipsis: true,
        width: 120
      }, {
        title: '播放列表',
        dataIndex: 'uploadPlayList',
        ellipsis: true,
        width: 120
      }, {
        title: '上传item',
        dataIndex: 'uploadItem',
        ellipsis: true,
        width: 120
      }, {
        title: '上传标签',
        dataIndex: 'uploadLabel',
        ellipsis: true,
        width: 120
      }, {
        title: '上传描述',
        dataIndex: 'uploadDesc',
        scopedSlots: { customRender: 'uploadDesc' },
        ellipsis: true,
        width: 150
      }, {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: { customRender: 'operation' },
        fixed: 'right',
        width: 150
      }]
    }
  },
  // mounted () {
  //   this.fetch()
  // },
  methods: {
    uploadEnableHeader (row) {
      this.$post('uploadInfo/update', {uploadId: row.uploadId, uploadEnable: !(row.uploadEnable === 'true')}).then((r) => {
        this.fetch()
      })
    },
    handleRefresh () {
      this.fetch()
    },
    handleView (row) {
      this.$refs.viewUploadModalRef.showModal(row)
    },
    handleDelete (id) {
      this.$post('uploadInfo/' + id, {}).then((r) => {
        this.$message.success('删除成功！')
        this.handleSearch()
        this.modalStatus = false
      })
    },
    handleSearch () {
      this.paginationInfo = null
      this.fetch()
    },
    handleAdd () {
      this.$refs.editUploadModalRef.showModal()
    },
    handleEdit (row) {
      this.$refs.editUploadModalRef.showModal(row)
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
      if (this.queryParams.channelType === undefined) {
        this.queryParams.channelType = ''
      }
      if (this.queryParams.downloadType === undefined) {
        this.queryParams.downloadType = ''
      }
      this.$get('uploadInfo/queryList', {
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
