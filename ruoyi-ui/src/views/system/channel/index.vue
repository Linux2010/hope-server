<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item prop="channelType">
        <el-select v-model="queryParams.channelType" placeholder="请选择频道类型" clearable @change="handleQuery">
          <el-option
            v-for="dict in dict.type.channel_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item prop="channelOffOn">
        <el-select v-model="queryParams.channelOffOn" placeholder="请选择开关状态" clearable @change="handleQuery">
          <el-option
            v-for="dict in dict.type.hope_channel_off_on"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item prop="cookieEnable">
        <el-select v-model="queryParams.cookieEnable" placeholder="请选择预警状态" clearable @change="handleQuery">
          <el-option
            v-for="dict in dict.type.hope_cookie_enable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item  prop="channelName">
        <el-input
          v-model="queryParams.channelName"
          placeholder="请输入频道名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item  prop="phone">
        <el-input
          v-model="queryParams.phone"
          placeholder="请输入手机号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:channel:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:channel:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:channel:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:channel:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="infoList" @selection-change="handleSelectionChange" @sort-change="handleSortChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="引擎" align="center" prop="engineId"  width="90" />
      <el-table-column label="名称" align="center" prop="channelName"  width="160"/>
      <el-table-column label="盈利" align="center" prop="gainStatus"  width="90" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.hope_gain_status" :value="scope.row.gainStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="类型" align="center" prop="channelType" width="90">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.channel_type" :value="scope.row.channelType"/>
        </template>
      </el-table-column>
      <el-table-column label="开关" align="center" prop="channelOffOn" width="90" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.hope_channel_off_on" :value="scope.row.channelOffOn"/>
        </template>
      </el-table-column>
      <el-table-column label="cookie" align="center" prop="cookieEnable" width="90">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.hope_cookie_enable" :value="scope.row.cookieEnable"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" sortable="custom">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="手机" align="center" prop="phone" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">

        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
            v-hasPermi="['system:channel:view']"
          >查看</el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:channel:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:channel:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改【频道管理】对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="160px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="频道名称" prop="channelName">
              <template v-if="isView">
                <span>{{ form.channelName }}</span>
              </template>
              <template v-else>
                <el-input v-model="form.channelName" placeholder="请输入频道名称" />
              </template>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="类型" prop="channelType">
              <template v-if="isView">
                <dict-tag :options="dict.type.channel_type" :value="form.channelType"/>
              </template>
              <template v-else>
                <el-select v-model="form.channelType" placeholder="请选择频道类型" clearable>
                  <el-option
                    v-for="dict in dict.type.channel_type"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                  />
                </el-select>
              </template>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开关" prop="channelOffOn">
              <template v-if="isView">
                <dict-tag :options="dict.type.hope_channel_off_on" :value="form.channelOffOn"/>
              </template>
              <template v-else>
                <el-select v-model="form.channelOffOn" placeholder="请选择开关状态">
                  <el-option
                    v-for="dict in dict.type.hope_channel_off_on"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                  />
                </el-select>
              </template>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="盈利" prop="gainStatus">
              <template v-if="isView">
                <dict-tag :options="dict.type.hope_gain_status" :value="form.gainStatus"/>
              </template>
              <template v-else>
                <el-select v-model="form.gainStatus" placeholder="请选择盈利状态">
                  <el-option
                    v-for="dict in dict.type.hope_gain_status"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                  />
                </el-select>
              </template>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户名" prop="username">
              <template v-if="isView">
                <span>{{ form.username }}</span>
              </template>
              <template v-else>
                <el-input v-model="form.username" placeholder="请输入用户名" />
              </template>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="密码" prop="password">
              <template v-if="isView">
                <span>{{ form.password }}</span>
              </template>
              <template v-else>
                <el-input v-model="form.password" placeholder="请输入密码" />
              </template>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="手机号" prop="phone">
              <template v-if="isView">
                <span>{{ form.phone }}</span>
              </template>
              <template v-else>
                <el-input v-model="form.phone" placeholder="请输入手机号" />
              </template>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="登录地址" prop="loginUrl">
              <template v-if="isView">
                <span>{{ form.loginUrl }}</span>
              </template>
              <template v-else>
                <el-input v-model="form.loginUrl" placeholder="请输入登录地址" />
              </template>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="主页地址" prop="homeUrl">
              <template v-if="isView">
                <span>{{ form.homeUrl }}</span>
              </template>
              <template v-else>
                <el-input v-model="form.homeUrl" placeholder="请输入主页地址" />
              </template>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <template v-if="isView">
                <span>{{ form.email }}</span>
              </template>
              <template v-else>
                <el-input v-model="form.email" placeholder="请输入邮箱" />
              </template>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="辅助邮箱" prop="assistEmail">
              <template v-if="isView">
                <span>{{ form.assistEmail }}</span>
              </template>
              <template v-else>
                <el-input v-model="form.assistEmail" placeholder="请输入辅助邮箱" />
              </template>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="单日上限" prop="dayMax">
              <template v-if="isView">
                <span>{{ form.dayMax }}</span>
              </template>
              <template v-else>
                <el-input v-model="form.dayMax" placeholder="请输入单日上限" />
              </template>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="标题上限" prop="titleLimit">
              <template v-if="isView">
                <span>{{ form.titleLimit }}</span>
              </template>
              <template v-else>
                <el-input v-model="form.titleLimit" placeholder="请输入标题上限" />
              </template>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="账号到期" prop="accDue">
              <template v-if="isView">
                <span>{{ parseTime(form.accDue, '{y}-{m}-{d}') }}</span>
              </template>
              <template v-else>
                <el-date-picker clearable
                  v-model="form.accDue"
                  type="date"
                  value-format="yyyy-MM-dd"
                  placeholder="请选择账号到期时间">
                </el-date-picker>
              </template>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="描述" prop="channelDesc">
              <template v-if="isView">
                <span>{{ form.channelDesc }}</span>
              </template>
              <template v-else>
                <el-input v-model="form.channelDesc" type="textarea" placeholder="请输入描述" />
              </template>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="频道地址" prop="channelUrl">
              <template v-if="isView">
                <span>{{ form.channelUrl }}</span>
              </template>
              <template v-else>
                <el-input v-model="form.channelUrl" type="textarea" placeholder="请输入频道地址" />
              </template>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button v-if="!isView" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listInfo, getInfo, delInfo, addInfo, updateInfo } from "@/api/system/channel";

export default {
  name: "Info",
  dicts: ['hope_channel_off_on', 'hope_cookie_enable', 'hope_gain_status', 'channel_type'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 【频道管理】表格数据
      infoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        channelName: null,
        channelType: null,
        channelOffOn: null,
        cookieEnable: null,
        username: null,
        password: null,
        phone: null,
        loginUrl: null,
        homeUrl: null,
        email: null,
        assistEmail: null,
        channelDesc: null,
        channelUrl: null,
        cookieDomain: null,
        dayMax: null,
        titleLimit: null,
        engineId: null,
        gainStatus: null,
        accDue: null,
        channelDetailJson: null,
        processStatus: null,
        orderByColumn: 'createTime',
        isAsc: 'desc'
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      formStyle: {
        display: 'flex',
        flexWrap: 'wrap',
        justifyContent:'space-between'
      },
      // 是否查看模式
      isView: false,
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询【频道管理】列表 */
    getList() {
      this.loading = true;
      listInfo(this.queryParams).then(response => {
        this.infoList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.isView = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        channelId: null,
        channelName: null,
        channelType: null,
        channelOffOn: null,
        cookieEnable: null,
        createTime: null,
        modifyTime: null,
        username: null,
        password: null,
        phone: null,
        loginUrl: null,
        homeUrl: null,
        email: null,
        assistEmail: null,
        channelDesc: null,
        channelUrl: null,
        cookieDomain: null,
        dayMax: null,
        titleLimit: null,
        engineId: null,
        gainStatus: null,
        accDue: null,
        channelDetailJson: null,
        processStatus: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.channelId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.isView = false;
      this.open = true;
      this.title = "添加频道信息";
    },
    /** 查看按钮操作 */
    handleView(row) {
      this.reset();
      const channelId = row.channelId;
      getInfo(channelId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "查看频道信息";
        this.isView = true;
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const channelId = row.channelId || this.ids;
      getInfo(channelId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改频道信息";
        this.isView = false;
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.channelId != null) {
            updateInfo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addInfo(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const channelIds = row.channelId || this.ids;
      this.$modal.confirm('是否确认删除【频道管理】编号为"' + channelIds + '"的数据项？').then(function() {
        return delInfo(channelIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/channel/export', {
        ...this.queryParams
      }, `info_${new Date().getTime()}.xlsx`)
    },
    /** 排序触发事件 */
    handleSortChange(column) {
      this.queryParams.orderByColumn = column.prop;
      this.queryParams.isAsc = column.order;
      this.getList();
    },
  }
};
</script>

