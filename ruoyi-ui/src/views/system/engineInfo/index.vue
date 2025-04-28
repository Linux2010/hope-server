<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item prop="engineStatus">
        <el-select v-model="queryParams.engineStatus" placeholder="启用状态" clearable @change="handleQuery" style="width: 120px">
          <el-option
            v-for="dict in dict.type.engine_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item prop="engineNetStatus">
        <el-select v-model="queryParams.engineNetStatus" placeholder="网络状态" clearable @change="handleQuery" style="width: 120px">
          <el-option
            v-for="dict in dict.type.engine_net_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item prop="engineName">
        <el-input
          v-model="queryParams.engineName"
          placeholder="名称"
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
          v-hasPermi="['system:engineInfo:add']"
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
          v-hasPermi="['system:engineInfo:edit']"
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
          v-hasPermi="['system:engineInfo:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:engineInfo:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="engineInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column label="引擎id" align="center" prop="engineId" width="80"/>
      <el-table-column label="名称" align="center" prop="engineName" min-width="120"/>
      <el-table-column label="域名" align="center" prop="engineIp" min-width="140"/>
      <el-table-column label="启用状态" align="center" prop="engineStatus" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.engine_status" :value="scope.row.engineStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="网络状态" align="center" prop="engineNetStatus" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.engine_net_status" :value="scope.row.engineNetStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="账号" align="center" prop="username" width="100"/>
      <el-table-column label="密码" align="center" prop="password" width="100"/>
      <el-table-column label="机房" align="center" prop="machineRoomId" width="100"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="220">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
            v-hasPermi="['system:engineInfo:view']"
          >查看</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:engineInfo:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:engineInfo:remove']"
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

    <!-- 添加或修改引擎管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="名称" prop="engineName">
              <template v-if="isView">
                <span>{{ form.engineName }}</span>
              </template>
              <template v-else>
                <el-input v-model="form.engineName" placeholder="请输入名称" />
              </template>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="域名" prop="engineIp">
              <template v-if="isView">
                <span>{{ form.engineIp }}</span>
              </template>
              <template v-else>
                <el-input v-model="form.engineIp" placeholder="请输入域名" />
              </template>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="账号" prop="username">
              <template v-if="isView">
                <span>{{ form.username }}</span>
              </template>
              <template v-else>
                <el-input v-model="form.username" placeholder="请输入账号" />
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
            <el-form-item label="机房" prop="machineRoomId">
              <template v-if="isView">
                <span>{{ form.machineRoomId }}</span>
              </template>
              <template v-else>
                <el-input v-model="form.machineRoomId" placeholder="请输入机房" />
              </template>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="启用状态" prop="engineStatus">
              <template v-if="isView">
                <span>{{ form.engineStatus }}</span>
              </template>
              <template v-else>
                <el-input v-model="form.engineStatus" placeholder="请输入启用状态" />
              </template>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="网络状态" prop="engineNetStatus">
              <template v-if="isView">
                <span>{{ form.engineNetStatus }}</span>
              </template>
              <template v-else>
                <el-input v-model="form.engineNetStatus" placeholder="请输入网络状态" />
              </template>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="描述" prop="engineDesc">
              <template v-if="isView">
                <span>{{ form.engineDesc }}</span>
              </template>
              <template v-else>
                <el-input v-model="form.engineDesc" type="textarea" placeholder="请输入内容" />
              </template>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer" v-if="!isView">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
      <div slot="footer" class="dialog-footer" v-else>
        <el-button @click="cancel">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listEngineInfo, getEngineInfo, delEngineInfo, addEngineInfo, updateEngineInfo } from "@/api/system/engineInfo";

export default {
  name: "EngineInfo",
  dicts: ['engine_status', 'engine_net_status'],
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
      // 引擎管理表格数据
      engineInfoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        engineName: null,
        engineStatus: null,
        engineNetStatus: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      isView: false, // 新增：是否为查看弹框
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询引擎管理列表 */
    getList() {
      this.loading = true;
      listEngineInfo(this.queryParams).then(response => {
        this.engineInfoList = response.rows;
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
        engineId: null,
        engineName: null,
        engineIp: null,
        username: null,
        password: null,
        engineDesc: null,
        engineStatus: null,
        machineRoomId: null,
        engineNetStatus: null
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
      this.queryParams.engineStatus = undefined;
      this.queryParams.engineNetStatus = undefined;
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.engineId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.isView = false;
      this.open = true;
      this.title = "添加引擎管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.isView = false;
      const engineId = row.engineId || this.ids
      getEngineInfo(engineId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改引擎管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.engineId != null) {
            updateEngineInfo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addEngineInfo(this.form).then(response => {
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
      const engineIds = row.engineId || this.ids;
      this.$modal.confirm('是否确认删除引擎管理编号为"' + engineIds + '"的数据项？').then(function() {
        return delEngineInfo(engineIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/engineInfo/export', {
        ...this.queryParams
      }, `engineInfo_${new Date().getTime()}.xlsx`)
    },
    handleView(row) {
      this.reset();
      this.isView = true;
      this.title = "查看引擎管理";
      getEngineInfo(row.engineId).then(response => {
        this.form = response.data;
        this.open = true;
      });
    },
  }
};
</script>
