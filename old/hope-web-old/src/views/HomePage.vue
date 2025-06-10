<template>
  <div class="home-page">
    <a-row :gutter="8">
      <a-card>
        <a-col :span="24">
          <div class="statistics">
            <div class="static-title">频道统计</div>
            <div v-for="(item, index) in channelStatisticsData" :key="index" class="static-item">
              <div class="static-inner">
                <span>{{ item.channel_type }}:</span>
                <span>{{ item.channel_count }}</span>
              </div>
              <div class="line"></div>
            </div>
          </div>
          <div class="statistics">
            <div class="static-title">下载统计</div>
            <div v-for="(item, index) in downloadStatisticsData" :key="index" class="static-item">
              <div class="static-inner">
                <span>{{ item.download_type }}:</span>
                <span>{{ item.download_count }}</span>
              </div>
              <div class="line"></div>
            </div>
          </div>
        </a-col>
      </a-card>
      <a-card class="mt10">
        <a-col :span="24"><div class="b-title">上传趋势图</div></a-col>
        <a-col class="visit-count">
          <!-- 查询区域 -->
          <a-form layout="horizontal">
            <a-row :gutter="16">
              <a-col :span="4">
                <a-form-item>
                  <a-select  show-search  placeholder="请选择频道类型" v-model="uploadParams.channelType" allowClear>
                    <a-select-option :value="item.keyy" v-for="item in channelTypeList" :key="item.DICT_ID">
                    {{item.valuee}}
                    </a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :span="4">
                <a-form-item>
                  <a-input  placeholder="请输入频道名称" v-model="uploadParams.channelName"/>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <span style="float: left; margin-top: 3px;">
                  <a-button type="primary" @click="handleSearch">查询</a-button>
                </span>
              </a-col>
            </a-row>
          </a-form>
        </a-col>
        <a-col :span="24" class="visit-count-wrapper">
          <apexchart ref="count" type=line height=300 :options="chartOptions" :series="series" />
        </a-col>
      </a-card>
      <a-card class="mt10">
        <a-col :span="24"><div class="b-title">失败排行</div></a-col>
        <a-col :span="24">
          <!-- 查询区域 -->
          <a-form layout="horizontal">
            <a-row :gutter="16">
              <a-col :span="4">
                <a-form-item>
                  <a-select  show-search  placeholder="请选择频道类型" v-model="failParams.channelType" allowClear>
                    <a-select-option :value="item.keyy" v-for="item in channelTypeList" :key="item.DICT_ID">
                    {{item.valuee}}
                    </a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
            <a-col :span="4">
                <a-form-item>
                  <a-input  placeholder="请输入频道名称" v-model="failParams.channelName"/>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <span style="float: left; margin-top: 3px;">
                  <a-button type="primary" @click="handleFailSearch">查询</a-button>
                </span>
              </a-col>
            </a-row>
          </a-form>
        </a-col>
        <a-col :span="24">
          <!-- 表格区域 -->
          <a-table ref="TableInfo" :rowKey="(r,i)=>{ return i}" :columns="columns" :dataSource="dataSource" :pagination="pagination" :loading="loading" @change="handleTableChange" :scroll="{ x: 900 }">
            <a slot="downloadCount" slot-scope="text,record" @click="handleDownloadCount(record)">{{ text || 0 }}</a>
            <span slot="cookieEnable" slot-scope="text, record">
              <a-tag :color="record.cookieEnable ? '#87d068' : '#f50'">
                {{record.cookieEnable ? '正常' : '报警' }}
              </a-tag>
              <a-divider type="vertical" />
              <a>刷新</a>
            </span>
            <span slot="channelOffOn" slot-scope="text, record">
              <a-switch checked-children="开" un-checked-children="关" :checked="record.channelOffOn == 'on' ? true : false" />
            </span>
            <span slot="channelUrl" slot-scope="text, record">
              <a :href="record.channelUrl" target="_blank">前台</a>
              <a-divider type="vertical" />
              <a :href="record.homeUrl" target="_blank">后台</a>
            </span>
            <template slot="operation" slot-scope="text, record">
              <a-popconfirm
                title="确定删除吗？"
                trigger="click"
                ok-text="确定"
                cancel-text="取消"
                @confirm="deleteVideo(record)">
                  <a style="cursor:pointer;">删除视频&ensp;&ensp;</a>
              </a-popconfirm>
              <a style="cursor:pointer;" @click="handleGoLogDetail(record)">日志</a>
            </template>
          </a-table>
        </a-col>
      </a-card>
    </a-row>
    <ViewFailLogModal ref="ViewFailLogModalRef" />
  </div>
</template>
<script>
import ViewFailLogModal from './components/ViewFailLogModal.vue'
import moment from 'moment'
import {mapState} from 'vuex'
moment.locale('zh-cn')
export default {
  name: 'HomePage',
  components: {ViewFailLogModal},
  data () {
    return {
      channelStatisticsData: [],
      downloadStatisticsData: [],
      chartOptions: {
        chart: {
          toolbar: {
            show: false
          }
        },
        plotOptions: {
          bar: {
            horizontal: false,
            columnWidth: '35%'
          }
        },
        dataLabels: {
          enabled: false
        },
        stroke: {
          show: true,
          width: 2,
          colors: ['transparent']
        },
        xaxis: {
          categories: []
        },
        fill: {
          opacity: 1

        }
      },
      series: [],
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
      channelTypeList: this.$store.state.account.dictList['channelType'] || [],
      uploadParams: {
        channelType: undefined,
        channelName: ''
      },
      failParams: {
        channelType: undefined,
        channelName: ''
      }
    }
  },
  computed: {
    ...mapState({
      multipage: state => state.setting.multipage,
      user: state => state.account.user
    }),
    columns () {
      return [{
        title: '频道类型',
        width: 120,
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
        title: '视频名称',
        width: 160,
        ellipsis: true,
        dataIndex: 'videoName'
      }, {
        title: '最新失败日期',
        width: 120,
        ellipsis: true,
        dataIndex: 'lastFailDate'
      }, {
        title: '累计失败次数',
        width: 90,
        ellipsis: true,
        dataIndex: 'failCount'
      }, {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: { customRender: 'operation' },
        fixed: 'right',
        width: 140
      }]
    }
  },
  // created () {
  //   this.uploadParams.channelType = this.channelTypeList[0].keyy
  //   this.queryTrend()
  // },
  methods: {
    handleGoLogDetail (row) {
      this.$refs.ViewFailLogModalRef.showModal(row)
    },
    handleSearch () {
      this.queryTrend()
    },
    deleteVideo (row) {
      const data = {
        'engineName': row.engineName || 1,
        'videoPath': row.videoPath || '/test/test2'
      }
      this.$post('uploadInstance/deleteInstance', JSON.stringify(data)).then((r) => {
        if (r.data.message === 'error') {
          this.$message.error('删除失败！')
        } else {
          this.$message.success('删除成功！')
          this.getQueryFailList()
        }
      })
    },
    channelStatistics () {
      this.$get('channelInfo/statistics').then((r) => {
        this.channelStatisticsData = r.data.data
      })
    },
    downloadStatistics () {
      this.$get('downloadInfo/statistics').then((r) => {
        this.downloadStatisticsData = r.data.data
      })
    },
    handleTableChange (pagination, filters, sorter) {
      this.paginationInfo = pagination
      this.getQueryFailList()
    },
    queryTrend () {
      this.$get('uploadInstance/queryTrend', {...this.uploadParams}).then((r) => {
        let data = r.data.data
        let successCount = []
        let dateArr = []
        let uploadCount = []
        let errorCount = []
        let initCount = []
        let uploadingCount = []
        data.forEach(item => {
          dateArr.push(item.upload_date)
          successCount.push(item.success_count)
          uploadCount.push(item.upload_count)
          initCount.push(item.init_count)
          errorCount.push(item.error_count)
          uploadingCount.push(item.uploading_count)
        })
        this.$refs.count.updateSeries([
          {
            name: '总数',
            data: uploadCount
          },
          {
            name: '成功',
            data: successCount
          },
          {
            name: '初始化',
            data: initCount
          },
          {
            name: '出错',
            data: errorCount
          },
          {
            name: '上传中',
            data: uploadingCount
          }
        ], true)
        this.$refs.count.updateOptions({
          xaxis: {
            categories: dateArr
          },
          title: {
            text: '上传趋势图',
            align: 'left'
          }
        }, true, true)
      }).catch((r) => {
        console.error(r)
        this.$message.error('获取首页信息失败')
      })
    },
    handleFailSearch () {
      this.paginationInfo = null
      this.getQueryFailList()
    },
    getQueryFailList (params = {}) {
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
      let failParams = {}
      if (this.failParams.channelType === undefined) {
        failParams.channelType = ''
      } else {
        failParams.channelType = this.failParams.channelType
      }
      failParams.channelName = this.failParams.channelName
      this.$get('uploadInstance/queryFailList', {...params, ...failParams}).then((r) => {
        let data = r.data
        const pagination = { ...this.pagination }
        pagination.total = data.total
        this.loading = false
        this.dataSource = data.rows
        this.pagination = pagination
      })
    }
  },
  // mounted () {
  //   this.downloadStatistics()
  //   this.channelStatistics()
  //   this.getQueryFailList()
  // },
  beforeRouteEnter: (to, from, next) => {
    next(vm => {
      vm.uploadParams.channelType = vm.channelTypeList[0].keyy
      vm.downloadStatistics()
      vm.channelStatistics()
      vm.getQueryFailList()
      vm.queryTrend()
    })
  }
}
</script>
<style lang="less" scoped>
.statistics{
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: center;
  height:60px;
  .static-title{
    font-size:14px;
    color:#333;
    margin-right:10px;
    background:#f7f7f7;
    padding:4px 10px;
    border-radius: 2px;
  }
  .static-item{
    font-size:14px;
    color:#666;
    padding:2px 4px;
    margin-right:4px;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    .static-inner{
      display: flex;
      flex-direction: row;
      justify-content: flex-start;
      align-items: center;
    }
  }
  .line{
    background:#bbb;
    height:12px;
    width:1px;
    margin:2px 10px 0 20px;
  }
}
.b-title{
  font-size:14px;
  font-weight: bold;
  padding:0 0 14px 0;
}
.mt10{
  margin-top:10px;
}
</style>
