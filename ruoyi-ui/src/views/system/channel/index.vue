<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item prop="channelType">
        <el-select v-model="queryParams.channelType" placeholder="请选择频道类型" clearable @change="handleQuery">
          <el-option label="油管" value="youtube"></el-option>
          <el-option label="B站" value="bili"></el-option>
          <el-option label="头条" value="xigua"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="channelOffOn">
        <el-select v-model="queryParams.channelOffOn" placeholder="请选择开关状态" clearable @change="handleQuery">
          <el-option label="关闭" value="off"></el-option>
          <el-option label="开启" value="on"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="cookieEnable">
        <el-select v-model="queryParams.cookieEnable" placeholder="请选择预警状态" clearable @change="handleQuery">
          <el-option label="正常" value="true">正常</el-option>
          <el-option label="报警" value="false">报警</el-option>
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

    <el-table v-loading="loading" :data="infoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="引擎" align="center" prop="engineId" />
      <el-table-column label="名称" align="center" prop="channelName" />
      <el-table-column label="类型" align="center" prop="channelType" />
      <el-table-column label="开关" align="center" prop="channelOffOn" />
      <el-table-column label="cookie" align="center" prop="cookieEnable" />
      <el-table-column label="修改时间" align="center" prop="modifyTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.modifyTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="手机" align="center" prop="phone" />
      <el-table-column label="盈利" align="center" prop="gainStatus" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">

        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
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

    <!-- 添加或修改【请填写功能名称】对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="160px" >
        <el-form-item label="频道名称" prop="channelName">
          <el-input v-model="form.channelName" placeholder="请输入频道名称" />
        </el-form-item>
        <el-form-item label="开关" prop="channelOffOn">
          <el-input v-model="form.channelOffOn" placeholder="请输入开关" />
        </el-form-item>

        <el-form-item label="cookie" prop="cookieEnable">
          <el-input v-model="form.cookieEnable" placeholder="请输入true为可用，false为不可用" />
        </el-form-item>

        <el-form-item label="modifyTime" prop="modifyTime">
          <el-date-picker clearable
            v-model="form.modifyTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择modifyTime">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="username" prop="username">
          <el-input v-model="form.username" placeholder="请输入username" />
        </el-form-item>
        <el-form-item label="password" prop="password">
          <el-input v-model="form.password" placeholder="请输入password" />
        </el-form-item>
        <el-form-item label="phone" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入phone" />
        </el-form-item>
        <el-form-item label="loginUrl" prop="loginUrl">
          <el-input v-model="form.loginUrl" placeholder="请输入loginUrl" />
        </el-form-item>
        <el-form-item label="homeUrl" prop="homeUrl">
          <el-input v-model="form.homeUrl" placeholder="请输入homeUrl" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="辅助邮箱" prop="assistEmail">
          <el-input v-model="form.assistEmail" placeholder="请输入辅助邮箱" />
        </el-form-item>
        <el-form-item label="描述" prop="channelDesc">
          <el-input v-model="form.channelDesc" type="textarea" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="channelUrl" prop="channelUrl">
          <el-input v-model="form.channelUrl" type="textarea" placeholder="请输入channelUrl" />
        </el-form-item>
        <el-form-item label="${comment}" prop="cookieDomain">
          <el-input v-model="form.cookieDomain" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="${comment}" prop="dayMax">
          <el-input v-model="form.dayMax" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="title上限" prop="titleLimit">
          <el-input v-model="form.titleLimit" placeholder="请输入title上限" />
        </el-form-item>
        <el-form-item label="accDue" prop="accDue">
          <el-date-picker clearable
            v-model="form.accDue"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择${comment}">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listInfo, getInfo, delInfo, addInfo, updateInfo } from "@/api/system/channel";

export default {
  name: "Info",
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
      // 【请填写功能名称】表格数据
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
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询【请填写功能名称】列表 */
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
      this.open = true;
      this.title = "添加【请填写功能名称】";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const channelId = row.channelId || this.ids
      getInfo(channelId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改【请填写功能名称】";
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
      this.$modal.confirm('是否确认删除【请填写功能名称】编号为"' + channelIds + '"的数据项？').then(function() {
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
    }
  }
};
</script>

