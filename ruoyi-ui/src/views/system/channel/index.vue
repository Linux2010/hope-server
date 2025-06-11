<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item prop="engineId">
        <el-select v-model="queryParams.engineId" placeholder="引擎" clearable @change="handleQuery" style="width: 120px">
          <el-option
            v-for="engine in engineOptions"
            :key="engine.engineId"
            :label="engine.engineName"
            :value="engine.engineId"
          />
        </el-select>
      </el-form-item>
      <el-form-item prop="channelType">
        <el-select v-model="queryParams.channelType" placeholder="频道类型" clearable @change="handleQuery" style="width: 120px">
          <el-option
            v-for="dict in dict.type.channel_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item prop="channelOffOn">
        <el-select v-model="queryParams.channelOffOn" placeholder="开关状态" clearable @change="handleQuery" style="width: 120px">
          <el-option
            v-for="dict in dict.type.hope_channel_off_on"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item prop="cookieEnable">
        <el-select v-model="queryParams.cookieEnable" placeholder="预警状态" clearable @change="handleQuery" style="width: 120px">
          <el-option
            v-for="dict in dict.type.hope_cookie_enable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item prop="channelName">
        <el-input
          v-model="queryParams.channelName"
          placeholder="频道名称"
          clearable
          @keyup.enter.native="handleQuery"
          style="width: 120px"
        />
      </el-form-item>
      <el-form-item prop="phone">
        <el-input
          v-model="queryParams.phone"
          placeholder="手机号"
          clearable
          @keyup.enter.native="handleQuery"
          style="width: 120px"
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
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="infoList" @selection-change="handleSelectionChange" @sort-change="handleSortChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="引擎" align="center" prop="engineName"  width="80" />
      <el-table-column label="名称" align="center" prop="channelName" width="160">
        <template slot-scope="scope">
          <a v-if="scope.row.channelUrl" :href="scope.row.channelUrl" target="_blank" style="color:#409EFF; text-decoration:underline;">{{ scope.row.channelName }}</a>
          <span v-else>{{ scope.row.channelName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="盈利" align="center" prop="gainStatus"  width="80" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.hope_gain_status" :value="scope.row.gainStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="类型" align="center" prop="channelType" width="80">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.channel_type" :value="scope.row.channelType"/>
        </template>
      </el-table-column>
      <el-table-column label="开关" align="center" prop="channelOffOn" width="80" >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.channelOffOn"
            active-value="on"
            inactive-value="off"
            @change="handleSwitchChange(scope.row)"
            v-hasPermi="['system:channel:edit']">
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column label="cookie" align="center" prop="cookieEnable" width="80">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.hope_cookie_enable" :value="scope.row.cookieEnable"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="120" sortable="custom">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="手机" align="center" prop="phone"  width="120" />
      <el-table-column label="操作" align="center" class-name="small-padding">
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

          <el-button
            size="mini"
            type="text"
            icon="el-icon-video-play"
            @click="handleBackendAction(scope.row)"
            v-hasPermi="['system:channel:execute']"
          >后台</el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-document"
            @click="handleAssistEmailClick(scope.row)"
            v-hasPermi="['system:channel:assistEmail']"
          >接码</el-button>
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
      <el-form ref="form" :model="form" :rules="rules" label-width="100px" size="small">
        <!-- 下拉框类型的表单项放在前面 -->
        <el-row :gutter="15">
          <el-col :span="12">
            <el-form-item label="类型" prop="channelType">
              <template v-if="isView">
                <dict-tag :options="dict.type.channel_type" :value="form.channelType"/>
              </template>
              <template v-else>
                <el-select v-model="form.channelType" placeholder="请选择频道类型" clearable style="width: 100%">
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
          <el-col :span="12">
            <el-form-item label="开关" prop="channelOffOn">
              <template v-if="isView">
                <dict-tag :options="dict.type.hope_channel_off_on" :value="form.channelOffOn"/>
              </template>
              <template v-else>
                <el-select v-model="form.channelOffOn" placeholder="请选择开关状态" style="width: 100%">
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
        </el-row>

        <el-row :gutter="15">
          <el-col :span="12">
            <el-form-item label="盈利" prop="gainStatus">
              <template v-if="isView">
                <dict-tag :options="dict.type.hope_gain_status" :value="form.gainStatus"/>
              </template>
              <template v-else>
                <el-select v-model="form.gainStatus" placeholder="请选择盈利状态" style="width: 100%">
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
          <el-col :span="12">
            <el-form-item label="引擎" prop="engineId">
              <template v-if="isView">
                <span>{{ form.engineName }}</span>
              </template>
              <template v-else>
                <el-select v-model="form.engineId" placeholder="请选择引擎" clearable @change="handleEngineChange" style="width: 100%">
                  <el-option
                    v-for="engine in engineOptions"
                    :key="engine.engineId"
                    :label="engine.engineName"
                    :value="engine.engineId"
                  />
                </el-select>
              </template>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="15">
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
                  placeholder="请选择账号到期时间"
                  style="width: 100%">
                </el-date-picker>
              </template>
            </el-form-item>
          </el-col>
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
        </el-row>

        <!-- 单行输入框，每行两个 -->
        <el-row :gutter="15">
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

        <el-row :gutter="15">
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

        <el-row :gutter="15">
          <el-col :span="12">
        <el-form-item label="接码地址" prop="assistEmail">
          <template v-if="isView">
            <el-link :href="form.assistEmail" target="_blank" :underline="false" style="color:#409EFF;">{{ form.assistEmail }}</el-link>
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

        <el-row :gutter="15">
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
        <el-form-item label="登录地址" prop="loginUrl">
          <template v-if="isView">
            <el-link :href="form.loginUrl" target="_blank" :underline="false" style="color:#409EFF;">{{ form.loginUrl }}</el-link>
          </template>
              <template v-else>
                <el-input v-model="form.loginUrl" placeholder="请输入登录地址" />
              </template>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="15">
          <el-col :span="12">
        <el-form-item label="主页地址" prop="homeUrl">
          <template v-if="isView">
            <el-link :href="form.homeUrl" target="_blank" :underline="false" style="color:#409EFF;">{{ form.homeUrl }}</el-link>
          </template>
          <template v-else>
            <el-input v-model="form.homeUrl" placeholder="请输入主页地址" />
          </template>
        </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Cookie域名" prop="cookieDomain">
              <template v-if="isView">
                <span>{{ form.cookieDomain }}</span>
              </template>
              <template v-else>
                <el-input v-model="form.cookieDomain" placeholder="请输入Cookie域名" />
              </template>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 多行输入框，独占一行 -->
        <el-row :gutter="15">
          <el-col :span="24">
            <el-form-item label="描述" prop="channelDesc">
              <template v-if="isView">
                <span>{{ form.channelDesc }}</span>
              </template>
              <template v-else>
                <el-input v-model="form.channelDesc" type="textarea" :rows="2" placeholder="请输入描述" />
              </template>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="15">
          <el-col :span="24">
            <el-form-item label="频道地址" prop="channelUrl">
          <template v-if="isView">
            <el-link :href="form.channelUrl" target="_blank" :underline="false" style="color:#409EFF;">{{ form.channelUrl }}</el-link>
          </template>
              <template v-else>
                <el-input v-model="form.channelUrl" type="textarea" :rows="2" placeholder="请输入频道地址" />
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
import { listInfo, getInfo, delInfo, addInfo, updateInfo, listEngine, listAllEngines, openBack } from "@/api/system/channel";

export default {
  name: "Channel",
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
      rules: {},
      formStyle: {
        display: 'flex',
        flexWrap: 'wrap',
        justifyContent:'space-between'
      },
      // 是否查看模式
      isView: false,
      engineOptions: [],
    };
  },
  created() {
    this.getList();
    this.getEngineList();
  },
  methods: {
    /** 查询【频道管理】列表 */
    getList() {
      this.loading = true;
      listInfo(this.queryParams).then(response => {
          this.infoList = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
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
      // 确保engineId是数字类型
      if (this.queryParams.engineId) {
        this.queryParams.engineId = Number(this.queryParams.engineId);
      }
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.queryParams.engineId = undefined;
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
    /** 排序触发事件 */
    handleSortChange(column) {
      this.queryParams.orderByColumn = column.prop;
      this.queryParams.isAsc = column.order;
      this.getList();
    },
    handleEngineSearch(queryString, callback) {
      if (queryString.length === 0) {
        callback([]);
        return;
      }
      // 从已有的引擎选项中过滤
      const results = this.engineOptions.filter(engine => 
        engine.engineName.toLowerCase().includes(queryString.toLowerCase())
      );
      callback(results);
    },
    handleEngineSelect(item) {
      this.form.engineId = item.engineId;
      this.form.engineName = item.engineName;
    },
    handleEngineChange(engineId) {
      if (engineId) {
        const selectedEngine = this.engineOptions.find(engine => engine.engineId === engineId);
        if (selectedEngine) {
          this.form.engineName = selectedEngine.engineName;
        }
      } else {
        this.form.engineName = null;
      }
    },
    /** 查询引擎列表 */
    getEngineList() {
      listAllEngines().then(response => {
        if (response.code === 200) {
          this.engineOptions = response.data;
        } else {
          this.$message.error("获取引擎列表失败");
        }
      });
    },
    /** 切换开关状态 */
    handleSwitchChange(row) {
      updateInfo(row).then(() => {
        this.$modal.msgSuccess(row.channelOffOn === "on" ? "启用成功" : "停用成功");
      }).catch(() => {
        row.channelOffOn = row.channelOffOn === "on" ? "off" : "on";
      });
    },

    handleBackendAction(row) {
      // 调用后端API触发shell脚本执行（需后端配合）
      // 示例：假设存在/api/channel/execute接口
      if (!row.channelType || !row.channelName) {
        this.$modal.msgError("频道类型和频道名称不能为空");
        return;
      }
      openBack(row.channelType, row.channelName)
        .then(() => this.$modal.msgSuccess("启动后台实例成功"))
        .catch(() => this.$modal.msgError("启动后台实例失败"));
    },

    /** 接码点击事件 */
    handleAssistEmailClick(row) {
      if (row.assistEmail) {
        window.open(row.assistEmail, '_blank');
      } else {
        this.$modal.msgWarning("接码URL为空");
      }
    }
  } 
};
</script>
