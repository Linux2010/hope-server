<template>
  <div style="width: 100%">
    <a-card :bordered="false" class="card-area">
      <!-- 搜索区域 -->
      <a-form layout="horizontal">
        <a-row :gutter="16">
          <a-col :span="4">
            <a-form-item>
              <a-date-picker format="YYYY-MM-DD" :showToday='showToday' v-model="uploadDate" @change="handleDateChange"/>
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <a-form-item prop="channelType">
              <a-select v-model="paramsForm.channelType" placeholder="请选择频道类型" allowClear>
                <a-select-option :value="item.keyy" v-for="item in channelTypeList" :key="item.DICT_ID">
                  {{item.valuee}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <a-form-item prop="channelName">
              <a-input  placeholder="请输入频道名称" v-model="paramsForm.channelName"/>
              <!-- <a-input placeholder="请输入频道名称" v-model="paramsForm.channelName"/>       -->
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
        <span slot="videoUrl" slot-scope="text">
          <a class="href" :href="text" target="_blank">{{text}}</a>
        </span>
        <template slot="operation" slot-scope="text, record">
          <!-- 仅当 record.status === 3 时显示 "清理" -->
          <a
            href="javascript:void(0)"
            v-if="record.status === '3'"
            @click="handleClean(record)"
          >
            清理&ensp;
          </a>
          <a href="javascript:void(0)" @click="handleGoDetail(record)">日志详情</a>
        </template>
      </a-table>
    </a-card>
    <ViewInstanceModal ref="viewInstanceModalRef" />
  </div>
</template>
<script>
import moment from 'moment'
import ViewInstanceModal from './components/ViewInstanceModal.vue'
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
      paramsForm: {
        channelType: undefined,
        channelName: '',
        uploadDate: ''
      },
      uploadDate: null,
      fromChannelPage: false,
      instanceStatus: null,
      filteredInfo: null
    }
  },
  computed: {
    columns () {
      let { sortedInfo,filteredInfo } = this
      sortedInfo = sortedInfo || {}
      filteredInfo = filteredInfo || {}
      return [{
        title: '频道名称',
        dataIndex: 'channelName',
        ellipsis: true,
        width: 180
      }, {
        title: '上传名称',
        dataIndex: 'uploadName',
        ellipsis: true,
        width: 120
      }, {
        title: '下载名称',
        dataIndex: 'downloadName',
        ellipsis: true,
        width: 120
      }, {
        title: '频道类型',
        dataIndex: 'channelType',
        width: 120
      }, {
        title: '实例状态',
        dataIndex: 'status',
        // scopedSlots: { customRender: 'status' }
        customRender: (text, row, index) => {
          switch (text) {
            case '0':
              return <a-tag color="cyan">成功</a-tag>
            case '1':
              return <a-tag color="red">失败</a-tag>
            case '2':
              return <a-tag color="orange">上传中</a-tag>
            case '3':
              return <a-tag color="red">初始化</a-tag>
            default:
              return text
          }
        },
        filters: [
          { text: '成功', value: '0' },
          { text: '失败', value: '1' },
          { text: '上传中', value: '2' },
          { text: '初始化', value: '3' }
        ],
        filterMultiple: false,
        filteredValue: filteredInfo.status || null,
        onFilter: (value, record) => record.status.includes(value),
        width: 120
      }, {
        title: '业务日期',
        dataIndex: 'uploadDate',
        sorter: true,
        ellipsis: true,
        sortOrder: sortedInfo.columnKey === 'uploadDate' && sortedInfo.order,
        width: 160
      },{
        title: '上传次数',
        dataIndex: 'uploadOverflow',
        sorter: true,
        ellipsis: true,
        sortOrder: sortedInfo.columnKey === 'uploadOverflow' && sortedInfo.order,
        width: 100
      }, {
        title: '视频名称',
        dataIndex: 'videoName',
        ellipsis: true,
        width: 180
      }, {
        title: '创建时间',
        dataIndex: 'createTime',
        sorter: true,
        ellipsis: true,
        sortOrder: sortedInfo.columnKey === 'createTime' && sortedInfo.order,
        width: 180
      }, {
        title: '视频地址',
        dataIndex: 'videoUrl',
        scopedSlots: { customRender: 'videoUrl' },
        ellipsis: true,
        width: 180
      }, {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: { customRender: 'operation' },
        fixed: 'right',
        width: 200
      }]
    }
  },
  mounted () {
    if (!this.fromChannelPage) {
      this.fetch()
    }
  },
  methods: {
    // capitalize (value) {
    //   var instanceStatus = this.$store.state.account.dictList.instanceStatus
    //   console.log(instanceStatus,'186')
    //   if (!instanceStatus.length) return ''
    //   const result = instanceStatus.filter(i => i.keyy === value)[0].valuee || ''
    //   return result
    // },
    handleDateChange (e) {
      if (!e) {
        this.paramsForm.uploadDate = ''
        this.uploadDate = null
      }
    },
    resetParamsForm () {
      this.paramsForm = {
        channelType: undefined,
        channelName: '',
        uploadDate: ''
      }
    },
    formDate (date) {
      const d = new Date(date)
      var year = d.getFullYear()
      var month = d.getMonth() + 1
      var strDate = d.getDate()
      if (month >= 1 && month <= 9) {
        month = '0' + month
      } else {
        month = '' + month
      }
      if (strDate >= 0 && strDate <= 9) {
        strDate = '0' + strDate
      } else {
        strDate = '' + strDate
      }
      const newDate = year + month + strDate
      return newDate
    },
    //清理失败视频
    handleClean(row){
      this.$postByJson('uploadInstance/delVideoAndUpInstance',JSON.stringify(row)
      ).then((r) => {
        if (r.data.message === 'error') {
          this.$message.error('清理视频失败！')
        } else {
          this.$message.success('清理视频成功！')
          this.fetch({
            ...this.filteredInfo,
            ...this.paramsForm
          })
        }
      })
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
      if (date) {
        this.paramsForm.uploadDate = this.formDate(date)
      }
      this.$get('uploadInstance/queryList', {
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
      if (from.path === '/hope/channel') {
        vm.uploadDate = moment(new Date())
        vm.paramsForm.channelType = to.query.channelType || ''
        vm.paramsForm.channelName = to.query.channelName || ''
        vm.fromChannelPage = true
        vm.fetch()
      } else {
        vm.uploadDate = ''
        vm.paramsForm.channelType = ''
        vm.paramsForm.channelName = ''
        vm.fromChannelPage = false
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
