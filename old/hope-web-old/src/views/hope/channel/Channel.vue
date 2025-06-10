<template>
  <div style="width: 100%">
    <a-card :bordered="false" class="card-area">
      <!-- 查询区域 -->
      <a-form layout="horizontal">
        <a-row :gutter="20">
          <a-col :span="4">
            <a-form-item prop="engineName">
              <a-select v-model="queryParams.engineName" placeholder="引擎名称" allowClear>
                <a-select-option :value="item.engineName" v-for="item in engineNameList" :key="item.engineId">
                  {{item.engineName}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <a-form-item>
              <a-select  show-search  placeholder="请选择频道类型" v-model="queryParams.channelType" allowClear>
                <a-select-option :value="item.keyy" v-for="item in channelTypeList" :key="item.DICT_ID">
                 {{item.valuee}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
         <a-col :span="4">
            <a-form-item>
              <a-input  placeholder="请输入频道名称" v-model="queryParams.channelName"/>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <span style="float: left; margin-top: 3px;">
              <a-button type="primary" @click="handleSearch">查询</a-button>
              <a-button style="margin-left: 8px" @click="handleReset">重置</a-button>
              <a-button style="margin-left: 8px"  @click="handleAdd">新增</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
      <!-- 表格区域 -->
      <a-table ref="TableInfo" :rowKey="record =>record.channelId" :columns="columns" :dataSource="dataSource" :pagination="pagination" :loading="loading" @change="handleTableChange" :scroll="{ x: 900 }">
        <span slot="channelName" slot-scope="text,record">
          <a class="href" :href="record.channelUrl" target="_blank">{{text}}</a>
        </span>
        <a slot="downloadCount" slot-scope="text,record" @click="handleDownloadCount(record)">{{ text || 0 }}</a>
        <span slot="cookieEnable" slot-scope="text, record">
          <a-tag :color="record.cookieEnable === 'true' ? '#87d068' : '#f50'">
            {{record.cookieEnable === 'true' ? '正常' : '报警' }}
          </a-tag>
          <a-divider type="vertical" />
          <a href="javascript:void(0)"  @click="refreshCookies(record)">刷新</a>
        </span>
        <span slot="channelOffOn" slot-scope="text, record">
          <a-switch checked-children="开" un-checked-children="关" :checked="record.channelOffOn == 'on' ? true : false" @change="uploadEnableHeader(record)"/>
        </span>
        <template slot="operation" slot-scope="text, record">
<!--          <a-divider type="vertical" />-->
<!--          <a :href="record.channelUrl" target="_blank">前台 </a>-->
          <a href="javascript:void(0)"  @click="handleOpenBack(record)">后台&ensp;</a>

          <a href="javascript:void(0)" @click="handleEdit(record)">修改&ensp;</a>
          <a href="javascript:void(0)" @click="handleView(record)">查看&ensp;</a>
          <a href="javascript:void(0)" @click="handleDownLoad(record)">关联&ensp;</a>
          <a href="javascript:void(0)" @click="handleUploadList(record)">记录</a>
          <a-popconfirm
            title="确定删除吗？"
            trigger="click"
            ok-text="确定"
            cancel-text="取消"
            @confirm="handleDelete(record)">
              <a href="javascript:void(0)">删除&ensp;</a>
          </a-popconfirm>
        </template>
      </a-table>
    </a-card>
    <DownloadCountModal ref="downloadCountModalRef" />
    <EditChannelModal ref="editChannelModalRef" @refresh="handleRefresh"/>
    <RelationDownload ref="relationDownloadRef"/>
    <ChannelDetail ref="channelDetailRef"/>
    <uploadRecords ref="uploadRecordsRef"/>
  </div>
</template>
<script>
import DownloadCountModal from './leafPages/DownloadCountModal.vue'
import EditChannelModal from './leafPages/EditChannelModal.vue'
import RelationDownload from './leafPages/RelationDownload.vue'
import ChannelDetail from './leafPages/ChannelDetail.vue'
import uploadRecords from './leafPages/uploadRecords.vue'
export default {
  components: {
    DownloadCountModal,
    EditChannelModal,
    RelationDownload,
    ChannelDetail,
    uploadRecords
  },
  data () {
    return {
      queryParams: {
        channelType: null,
        engineName: null,
        channelName: null
      },
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
      loading: false,
      channelTypeList: this.$store.state.account.dictList['channelType'] || [],
      engineNameList: []
    }
  },
  computed: {
    columns () {
      let { sortedInfo,filteredInfo } = this
      sortedInfo = sortedInfo || {}
      filteredInfo = filteredInfo || {}
      return [{
        title: '所属引擎',
        dataIndex: 'engineName',
        ellipsis: true,
        width: 100
      },{
        title: '频道类型',
        width: 100,
        ellipsis: true,
        dataIndex: 'channelType'
      },{
        title: '频道名称',
        width: 160,
        ellipsis: true,
        dataIndex: 'channelName',
        scopedSlots: { customRender: 'channelName' },
      }, {
        title: '状态',
        dataIndex: 'gainStatus',
        width: 100,
        scopedSlots: { customRender: 'gainStatus' },
        filters: [
          { text: '非盈利', value: '0' },
          { text: '盈利', value: '1' },
          { text: '永封', value: '2' },
          { text: '暂封', value: '3' }
        ],
        customRender: (text, row, index) => {
          switch (text) {
            case '0':
              return <a-tag color="orange">非盈利</a-tag>
            case '1':
              return <a-tag color="cyan">盈利</a-tag>
            case '2':
              return <a-tag color="red">永封</a-tag>
            case '3':
              return <a-tag color="red">暂封</a-tag>
            default:
              return text
          }
        },
        filterMultiple: false,
        filteredValue: filteredInfo.gainStatus || null,
        onFilter: (value, record) => record.gainStatus.includes(value)

      },{
        title: '开关',
        dataIndex: 'channelOffOn',
        width: 120,
        scopedSlots: { customRender: 'channelOffOn' },
        filters: [
          { text: '开启', value: 'on' },
          { text: '关闭', value: 'off' }
        ],

        filterMultiple: false,
        filteredValue: filteredInfo.channelOffOn || null,
        onFilter: (value, record) => record.channelOffOn.includes(value)

      }, {
        title: '关联',
        dataIndex: 'downloadCount',
        key: 'downloadCount',
        ellipsis: true,
        width: 80,
        scopedSlots: { customRender: 'downloadCount' }
      }, {
        title: 'cookie预警',
        dataIndex: 'cookieEnable',
        scopedSlots: { customRender: 'cookieEnable' },
        width: 140,
        filters: [
          { text: '正常', value: 'true' },
          { text: '报警', value: 'false' }
        ],
        filterMultiple: false,
        filteredValue: filteredInfo.cookieEnable || null,
        onFilter: (value, record) => record.cookieEnable.includes(value)

      }, {
        title: '上限',
        width: 80,
        ellipsis: true,
        dataIndex: 'dayMax'
      }, {
        title: '登录账号',
        width: 180,
        ellipsis: true,
        dataIndex: 'username'
      }, {
        title: '创建时间',
        width: 130,
        ellipsis: true,
        sorter: true,
        sortOrder: sortedInfo.columnKey === 'createTime' && sortedInfo.order,
        dataIndex: 'createTime'
      }, {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: { customRender: 'operation' },
        fixed: 'right',
        width: 350
      }]
    }
  },
  // mounted () {
  //   this.fetch()
  // },
  methods: {
    uploadEnableHeader (row) {
      const data = {
        channelId: row.channelId,
        channelOffOn: row.channelOffOn === 'on' ? 'off' : 'on'
      }
      this.$post('channelInfo/update', data).then((r) => {
        this.$message.success(r.data.message)
        this.fetch()
      })
    },
    handleRefresh () {
      this.fetch()
    },
    handleUploadList (row) {
      // this.$router.push('/hope/instance')
      this.$router.push({path: '/hope/instance', query: {channelName: row.channelName, channelType: row.channelType}})
      // this.$refs.uploadRecordsRef.showModal(row)
    },
    handleDelete (row) {
      if (row.downloadCount > 0) {
        this.$message.warn('无法删除，需首先解除下载关联关系')
      } else {
        this.$post('channelInfo/' + row.channelId, {}).then((r) => {
          if (r.data.message === 'error') {
            this.$message.error('删除失败！')
          } else {
            this.$message.success('删除成功！')
            this.fetch()
          }
        })
      }
    },
    handleSearch () {
      this.paginationInfo = null
      this.fetch()
    },
    handleReset () {
      this.queryParams = {}
      this.fetch()
    },
    refreshCookies (row) {
      this.$post('channelInfo/refresh/'+row.channelType+'/'+ row.channelName, {}).then((r) => {
        if (r.data.message === 'error') {
          this.$message.error('刷新实例启动失败！')
        } else {
          this.$message.success('刷新实例启动成功！')
          this.fetch()
        }
      })

    },

    handleOpenBack (row) {
      this.$post('channelInfo/openBack/'+row.channelType+'/'+ row.channelName, {}).then((r) => {
        if (r.data.message === 'error') {
          this.$message.error('后台实例启动失败！')
        } else {
          this.$message.success('后台实例启动成功！')
        }
      })

    },
    handleAdd () {
      this.$refs.editChannelModalRef.showModal()
    },
    handleEdit (row) {
      this.$refs.editChannelModalRef.showModal(row)
    },
    handleView (row) {
      this.$refs.channelDetailRef.showModal(row)
    },
    handleDownloadCount (record) {
      if (record.downloadCount > 0) {
        this.$refs.downloadCountModalRef.showModal(record)
      }
    },
    handleDownLoad (record) {
      this.$refs.relationDownloadRef.showModal(record)
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
      // 下拉框提示语处理
      if (this.queryParams.channelType === undefined) {
        this.queryParams.channelType = ''
      }
      if (this.queryParams.engineName === undefined) {
        this.queryParams.engineName = ''
      }
      this.$get('channelInfo/queryList', {
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
    },
    getEngineName () {
      this.$get('engineInfo/queryAll').then((r) => {
        this.engineNameList = r.data
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
</style>
