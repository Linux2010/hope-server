<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item prop="uploadDate">
        <el-date-picker clearable
          v-model="queryParams.uploadDate"
          type="date"
          value-format="yyyyMMdd"
          placeholder="请选择上传日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item prop="status">
        <el-select v-model="queryParams.status" placeholder="上传状态" clearable style="width: 120px">
          <el-option
            v-for="dict in dict.type.status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>      
      <el-form-item prop="videoName">
        <el-input
          v-model="queryParams.videoName"
          placeholder="请输入视频名称"
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
  <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
</el-row>

    <el-table v-loading="loading" :data="instanceList" @selection-change="handleSelectionChange" row-style="{ height: '40px' }">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="频道名称" align="center" prop="channelName" width="120" show-overflow-tooltip />
      <el-table-column label="上传名称" align="center" prop="uploadName" width="120" show-overflow-tooltip />
      <el-table-column label="下载名称" align="center" prop="downloadName" width="120" show-overflow-tooltip />
      <el-table-column label="频道类型" align="center" prop="channelType" width="100" show-overflow-tooltip />
      <el-table-column label="上传状态" align="center" prop="status" width="100" show-overflow-tooltip >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="上传日期" align="center" prop="uploadDate" width="180" show-overflow-tooltip />
      <el-table-column label="视频名称" align="center" prop="videoName" width="120" show-overflow-tooltip />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="{row}">
          <span>{{ parseTime(row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="视频URL" align="center" prop="videoUrl" width="200" show-overflow-tooltip />
      <el-table-column label="操作" align="center" width="200">
        <template slot-scope="{row}">
          <el-button size="mini" @click="showLog(row)">日志</el-button>
          <el-button size="mini" @click="handleDeleteNoConfirm(row)">删除</el-button>
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

    <el-dialog title="日志详情" :visible.sync="logDialogVisible" width="60%">
      <div style="height: 400px; overflow-y: auto; white-space: pre-wrap; background-color: #f4f4f4; padding: 10px; border-radius: 5px;">
        <p style="margin-bottom: 10px;"><strong>上传命令:</strong> {{ selectedRow.uploadCmd }}</p>
        <p style="white-space: pre-line;"><strong>日志信息:</strong>
          <br>{{ selectedRow.uploadLog }}</p>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listInstance, getInstance, delInstance, addInstance, updateInstance } from "@/api/system/instance";

export default {
  name: "Instance",
  dicts: ['status'],
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
      // 上传实例表格数据
      instanceList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        channelName: null,
        downloadName: null,
        status: null,
        uploadDate: null,
        videoName: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      logDialogVisible: false,
      selectedRow: {}
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询上传实例列表 */
    getList() {
      this.loading = true;
      listInstance(this.queryParams).then(response => {
        this.instanceList = response.rows;
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
        instanceId: null,
        channelName: null,
        uploadName: null,
        downloadName: null,
        channelType: null,
        status: null,
        uploadDate: null,
        videoName: null,
        uploadCmd: null,
        uploadLog: null,
        createTime: null,
        modifyTime: null,
        videoUrl: null,
        uploadOverflow: null,
        videoPath: null,
        engineName: null,
        videoFile: null,
        videoMd5: null,
        channelId: null,
        downloadId: null,
        isPublic: null
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
      this.ids = selection.map(item => item.instanceId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加上传实例";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const instanceId = row.instanceId || this.ids
      getInstance(instanceId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改上传实例";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.instanceId != null) {
            updateInstance(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addInstance(this.form).then(response => {
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
      const instanceIds = row.instanceId || this.ids;
      this.$modal.confirm('是否确认删除上传实例编号为"' + instanceIds + '"的数据项？').then(function() {
        return delInstance(instanceIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/instance/export', {
        ...this.queryParams
      }, `instance_${new Date().getTime()}.xlsx`)
    },
    showLog(row) {
      this.selectedRow = row;
      this.logDialogVisible = true;
    },
    handleDeleteNoConfirm(row) {
      const instanceIds = row.instanceId || this.ids;
      delInstance(instanceIds).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      });
    }
  }
};
</script>