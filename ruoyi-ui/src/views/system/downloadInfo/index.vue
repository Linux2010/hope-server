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
      <el-form-item prop="downloadType">
        <el-select v-model="queryParams.downloadType" placeholder="下载类型" clearable @change="handleQuery" style="width: 120px">
          <el-option
            v-for="dict in dict.type.download_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item prop="downloadEnable">
        <el-select v-model="queryParams.downloadEnable" placeholder="下载开关" clearable @change="handleQuery" style="width: 120px">
          <el-option
            v-for="dict in dict.type.download_enable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item prop="cycleType">
        <el-select v-model="queryParams.cycleType" placeholder="下载循环" clearable @change="handleQuery" style="width: 120px">
          <el-option
            v-for="dict in dict.type.cycle_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item prop="downloadName">
        <el-input
          v-model="queryParams.downloadName"
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
          v-hasPermi="['system:downloadInfo:add']"
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
          v-hasPermi="['system:downloadInfo:edit']"
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
          v-hasPermi="['system:downloadInfo:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="downloadInfoList"
      @selection-change="handleSelectionChange"
      :cell-style="{padding: '6px 8px'}"
      :header-cell-style="{padding: '8px 8px', background: '#fafbfc'}"
    >
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column label="引擎" align="center" prop="engineName" min-width="90" />
      <el-table-column label="名称" align="center" prop="downloadName" min-width="120">
        <template slot-scope="scope">
          <el-tooltip class="item" effect="dark" :content="scope.row.downloadUrl" placement="top">
            <a :href="scope.row.downloadUrl" target="_blank" style="color:#409EFF; text-decoration:underline;">{{ scope.row.downloadName }}</a>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column label="类型" align="center" prop="downloadType" min-width="80">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.download_type" :value="scope.row.downloadType"/>
        </template>
      </el-table-column>
      <el-table-column label="内容" align="center" prop="videoType" min-width="80"/>
      <el-table-column label="开关" align="center" prop="downloadEnable" min-width="60">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.downloadEnable"
            active-value="true"
            inactive-value="false"
            @change="handleSwitchChange(scope.row)"
            v-hasPermi="['system:downloadInfo:edit']">
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="140">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="质量" align="center" prop="quality" min-width="60">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.quality" :value="scope.row.quality"/>
        </template>
      </el-table-column>
      <el-table-column label="存储" align="center" prop="diskStorageInfo" min-width="80"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)" v-hasPermi="['system:downloadInfo:view']">查看</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:downloadInfo:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['system:downloadInfo:remove']">删除</el-button>
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

    <!-- 添加或修改下载管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px" size="small">
        <!-- 下拉框类型的表单项放在前面 -->
        <el-row :gutter="15">
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
          <el-col :span="12">
            <el-form-item label="类型" prop="downloadType">
              <template v-if="isView">
                <dict-tag :options="dict.type.download_type" :value="form.downloadType"/>
              </template>
              <template v-else>
                <el-select v-model="form.downloadType" placeholder="请选择下载类型" clearable style="width: 100%">
                  <el-option
                    v-for="dict in dict.type.download_type"
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
            <el-form-item label="开关" prop="downloadEnable">
              <template v-if="isView">
                <dict-tag :options="dict.type.download_enable" :value="form.downloadEnable"/>
              </template>
              <template v-else>
                <el-select v-model="form.downloadEnable" placeholder="请选择下载开关" style="width: 100%">
                  <el-option
                    v-for="dict in dict.type.download_enable"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                  />
                </el-select>
              </template>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="质量" prop="quality">
              <template v-if="isView">
                <dict-tag :options="dict.type.quality" :value="form.quality"/>
              </template>
              <template v-else>
                <el-select v-model="form.quality" placeholder="请选择质量" style="width: 100%">
                  <el-option
                    v-for="dict in dict.type.quality"
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
            <el-form-item label="名称" prop="downloadName">
              <template v-if="isView">
                <span>{{ form.downloadName }}</span>
              </template>
              <template v-else>
                <el-input v-model="form.downloadName" placeholder="请输入名称" />
              </template>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="内容" prop="videoType">
              <template v-if="isView">
                <span>{{ form.videoType }}</span>
              </template>
              <template v-else>
                <el-input v-model="form.videoType" placeholder="请输入内容" />
              </template>
            </el-form-item>
          </el-col>
        </el-row>


        <el-row :gutter="15">
          <el-col :span="12">
            <el-form-item label="开始" prop="downloadStart">
              <template v-if="isView">
                <span>{{ form.downloadStart }}</span>
              </template>
              <template v-else>
                <el-input v-model="form.downloadStart" placeholder="请输入开始" />
              </template>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束" prop="downloadEnd">
              <template v-if="isView">
                <span>{{ form.downloadEnd }}</span>
              </template>
              <template v-else>
                <el-input v-model="form.downloadEnd" placeholder="请输入结束" />
              </template>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="15">
          <el-col :span="12">
            <el-form-item label="全量" prop="downloadAllEnd">
              <template v-if="isView">
                <span>{{ form.downloadAllEnd }}</span>
              </template>
              <template v-else>
                <el-input v-model="form.downloadAllEnd" placeholder="请输入全量" />
              </template>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="存储路径" prop="downloadPath">
              <template v-if="isView">
                <span>{{ form.downloadPath }}</span>
              </template>
              <template v-else>
                <el-input v-model="form.downloadPath" placeholder="请输入存储路径" />
              </template>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 多行输入框，独占一行 -->
        <el-row :gutter="15">
          <el-col :span="24">
            <el-form-item label="URL" prop="downloadUrl">
              <template v-if="isView">
                <div style="white-space: pre-line;">{{ form.downloadUrl }}</div>
              </template>
              <template v-else>
                <el-input v-model="form.downloadUrl" type="textarea" :rows="2" placeholder="请输入URL" />
              </template>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="15">
          <el-col :span="24">
            <el-form-item label="描述" prop="downloadDesc">
              <template v-if="isView">
                <div style="white-space: pre-line;">{{ form.downloadDesc }}</div>
              </template>
              <template v-else>
                <el-input v-model="form.downloadDesc" type="textarea" :rows="2" placeholder="请输入描述" />
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
import { listDownloadInfo, getDownloadInfo, delDownloadInfo, addDownloadInfo, updateDownloadInfo, listAllEngines } from "@/api/system/downloadInfo";
import { getDicts } from "@/api/system/dict/data";
import DictTag from "@/components/DictTag";

export default {
  name: "DownloadInfo",
  components: {
    DictTag
  },
  dicts: ['download_type', 'download_enable', 'cycle_type', 'quality'],
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
      // 下载管理表格数据
      downloadInfoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        downloadName: null,
        downloadType: null,
        videoType: null,
        downloadEnable: null,
        engineId: null,
        cycleType: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        downloadName: [
          { required: true, message: "名称不能为空", trigger: "blur" }
        ],
        downloadType: [
          { required: true, message: "类型不能为空", trigger: "change" }
        ],
        engineId: [
          { required: true, message: "引擎不能为空", trigger: "change" }
        ],
      },
      // 是否查看模式
      isView: false,
      // 引擎选项
      engineOptions: [],
    };
  },
  created() {
    this.getList();
    this.getEngineList();
  },
  methods: {
    /** 查询下载管理列表 */
    getList() {
      this.loading = true;
      listDownloadInfo(this.queryParams).then(response => {
        this.downloadInfoList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 获取字典数据 */
    getDicts() {
      Promise.all([
        getDicts("download_type"),
        getDicts("download_enable"),
        getDicts("cycle_type"),
        getDicts("quality")
      ]).then(([downloadTypeRes, downloadEnableRes, cycleTypeRes, qualityRes]) => {
        this.downloadTypeOptions = downloadTypeRes.data;
        this.downloadEnableOptions = downloadEnableRes.data;
        this.cycleTypeOptions = cycleTypeRes.data;
        this.qualityOptions = qualityRes.data;
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
        downloadId: null,
        downloadName: null,
        downloadType: null,
        videoType: null,
        shellPath: null,
        agent: null,
        downloadUrl: null,
        downloadEnable: null,
        downloadPath: null,
        downloadStart: null,
        downloadEnd: null,
        downloadAllEnd: null,
        afterShellPath: null,
        modifyTime: null,
        createTime: null,
        downloadDesc: null,
        engineId: null,
        engineName: null,
        cycleType: null,
        usedSize: null,
        quality: null,
        diskStorageInfo: null
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
      this.queryParams.downloadType = undefined;
      this.queryParams.downloadEnable = undefined;
      this.queryParams.cycleType = undefined;
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.downloadId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.isView = false;
      this.open = true;
      this.title = "添加下载管理";
    },
    /** 查看按钮操作 */
    handleView(row) {
      this.reset();
      const downloadId = row.downloadId;
      getDownloadInfo(downloadId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "查看下载管理";
        this.isView = true;
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const downloadId = row.downloadId || this.ids
      getDownloadInfo(downloadId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改下载管理";
        this.isView = false;
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.isView = false;
          if (this.form.downloadId != null) {
            updateDownloadInfo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDownloadInfo(this.form).then(response => {
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
      const downloadIds = row.downloadId || this.ids;
      this.$modal.confirm('是否确认删除下载管理编号为"' + downloadIds + '"的数据项？').then(function() {
        return delDownloadInfo(downloadIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
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
    /** 引擎选择变更 */
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
    /** 切换开关状态 */
    handleSwitchChange(row) {
      updateDownloadInfo(row).then(() => {
        this.$modal.msgSuccess(row.downloadEnable === "true" ? "启用成功" : "停用成功");
      }).catch(() => {
        row.downloadEnable = row.downloadEnable === "true" ? "false" : "true";
      });
    }
  }
};
</script>
