<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="下载名称" prop="downloadName">
        <el-input
          v-model="queryParams.downloadName"
          placeholder="请输入下载名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="频道名称" prop="channelName">
        <el-input
          v-model="queryParams.channelName"
          placeholder="请输入频道名称"
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
          v-hasPermi="['system:uploadInfo:add']"
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
          v-hasPermi="['system:uploadInfo:edit']"
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
          v-hasPermi="['system:uploadInfo:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="uploadInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="上传名称" align="center" prop="uploadName" min-width="140" />
      <el-table-column label="下载名称" align="center" prop="downloadName" min-width="140" />
      <el-table-column label="频道名称" align="center" prop="channelName" min-width="140" />
      <el-table-column label="开关" align="center" prop="uploadEnable" width="80">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.uploadEnable"/>
        </template>
      </el-table-column>
      <el-table-column label="日上限" align="center" prop="uploadDayMax" width="80" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="140">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)" v-hasPermi="['system:uploadInfo:view']">查看</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:uploadInfo:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['system:uploadInfo:remove']">删除</el-button>
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

    <!-- 添加或修改上传管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px" size="small">
        <el-row :gutter="15">
          <el-col :span="12">
            <el-form-item label="上传名称" prop="uploadName">
              <template v-if="isView">
                <span>{{ form.uploadName }}</span>
              </template>
              <template v-else>
                <el-input v-model="form.uploadName" placeholder="请输入上传名称" />
              </template>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="下载名称" prop="downloadName">
              <template v-if="isView">
                <span>{{ form.downloadName }}</span>
              </template>
              <template v-else>
                <el-input v-model="form.downloadName" placeholder="请输入下载名称" />
              </template>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="15">
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
            <el-form-item label="日上限" prop="uploadDayMax">
              <template v-if="isView">
                <span>{{ form.uploadDayMax }}</span>
              </template>
              <template v-else>
                <el-input v-model="form.uploadDayMax" placeholder="请输入日上限" />
              </template>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="15">
          <el-col :span="12">
            <el-form-item label="视频路径" prop="uploadPath">
              <template v-if="isView">
                <span>{{ form.uploadPath }}</span>
              </template>
              <template v-else>
                <el-input v-model="form.uploadPath" placeholder="请输入视频路径" />
              </template>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="脚本路径" prop="uploadShellPath">
              <template v-if="isView">
                <span>{{ form.uploadShellPath }}</span>
              </template>
              <template v-else>
                <el-input v-model="form.uploadShellPath" placeholder="请输入脚本路径" />
              </template>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="15">
          <el-col :span="12">
            <el-form-item label="播放列表" prop="uploadPlayList">
              <template v-if="isView">
                <span>{{ form.uploadPlayList }}</span>
              </template>
              <template v-else>
                <el-input v-model="form.uploadPlayList" placeholder="请输入播放列表" />
              </template>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="item" prop="uploadItem">
              <template v-if="isView">
                <span>{{ form.uploadItem }}</span>
              </template>
              <template v-else>
                <el-input v-model="form.uploadItem" placeholder="请输入item" />
              </template>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="15">
          <el-col :span="12">
            <el-form-item label="标签" prop="uploadLabel">
              <template v-if="isView">
                <span>{{ form.uploadLabel }}</span>
              </template>
              <template v-else>
                <el-input v-model="form.uploadLabel" placeholder="请输入标签" />
              </template>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="ok后缀" prop="uploadOk">
              <template v-if="isView">
                <span>{{ form.uploadOk }}</span>
              </template>
              <template v-else>
                <el-input v-model="form.uploadOk" placeholder="请输入ok后缀" />
              </template>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="15">
          <el-col :span="24">
            <el-form-item label="描述" prop="uploadDesc">
              <template v-if="isView">
                <div style="white-space: pre-line;">{{ form.uploadDesc }}</div>
              </template>
              <template v-else>
                <el-input v-model="form.uploadDesc" type="textarea" :rows="3" placeholder="请输入内容" />
              </template>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer" v-if="!isView">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listUploadInfo, getUploadInfo, delUploadInfo, addUploadInfo, updateUploadInfo } from "@/api/system/uploadInfo";
import DictTag from "@/components/DictTag";

export default {
  name: "UploadInfo",
  components: {
    DictTag
  },
  dicts: ['sys_normal_disable'],
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
      // 上传管理表格数据
      uploadInfoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否查看模式
      isView: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        uploadEnable: null,
        downloadName: null,
        channelName: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询上传管理列表 */
    getList() {
      this.loading = true;
      listUploadInfo(this.queryParams).then(response => {
        this.uploadInfoList = response.rows;
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
        uploadId: null,
        uploadName: null,
        uploadEnable: null,
        uploadDayMax: null,
        uploadPath: null,
        uploadShellPath: null,
        uploadPlayList: null,
        uploadItem: null,
        uploadLabel: null,
        uploadDesc: null,
        downloadName: null,
        channelName: null,
        modifyTime: null,
        createTime: null,
        uploadOk: null
      };
      this.resetForm("form");
      this.isView = false;
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
      this.ids = selection.map(item => item.uploadId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加上传管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const uploadId = row.uploadId || this.ids
      getUploadInfo(uploadId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改上传管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.uploadId != null) {
            updateUploadInfo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addUploadInfo(this.form).then(response => {
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
      const uploadIds = row.uploadId || this.ids;
      this.$modal.confirm('是否确认删除上传管理编号为"' + uploadIds + '"的数据项？').then(function() {
        return delUploadInfo(uploadIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 查看按钮操作 */
    handleView(row) {
      this.reset();
      const uploadId = row.uploadId;
      getUploadInfo(uploadId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "查看上传管理";
        this.isView = true;
      });
    }
  }
};
</script>
