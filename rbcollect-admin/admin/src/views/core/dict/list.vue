<template>
  <div class="app-container">
    <div style="margin-bottom: 10px">
      <el-button
        @click="dialogVisible = true"
        type="primary"
        size="mini"
        icon="el-icon-download"
      >
        导入Excel
      </el-button>
      <el-button
        @click="exportData"
        type="primary"
        size="mini"
        icon="el-icon-upload2"
      >
        导出Excel
      </el-button>
      <el-table :data="list" border row-key="id" lazy :load="load">
        <el-table-column label="名称" align="left" prop="name" />
        <el-table-column label="编码" prop="dictCode" />
        <el-table-column label="值" align="left" prop="value" />
      </el-table>
    </div>
    <el-dialog title="数据字典导入" :visible.sync="dialogVisible" width="30%">
      <el-form>
        <el-form-item label="请选择Excel文件">
          <el-upload
            :auto-upload="true"
            :multiple="false"
            :limit="1"
            :on-exceed="fileUploadExceed"
            :on-success="fileUploadSuccess"
            :on-error="fileUploadError"
            :action="BASE_API + '/admin/core/dict/import'"
            name="file"
            accept="application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
          >
            <el-button size="small" type="primary">点击上传</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
// accept:接收的文件类型
// name:前后端接收的文件对象和组件名字一样才能传过去
// action:会以ajax形式向后端发送请求
// on-success/error:上传成功/失败回调函数
// multiple，limit：不能多选，一次只能选一个
//  on-exceed:当选文件超过1个，触发该函数
//  auto-upload：选取完自动上传
import dictApi from "@/api/core/dict";
export default {
  data() {
    return {
      dialogVisible: false, //对话框是否显示
      BASE_API: process.env.VUE_APP_BASE_API, //获取后端接口地址
      list: [], //数据字典列表
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      dictApi.listByParentId(1).then((response) => {
        this.list = response.data.list;
      });
    },

    fileUploadSuccess(response) {
      //这是处理通信成功200的情况，业务可能成功或失败
      if (response.code === 0) {
        this.$message.success("数据导入成功");
        this.dialogVisible = false;
        setTimeout("location.reload () ", 1400);
      } else {
        this.$message.error(response.message);
      }
    },
    fileUploadError(error) {
      //处理通信不成功
      this.$message.error("数据导入失败");
    },
    fileUploadExceed() {
      this.$message.warning("只能选取一个文件");
    },
    exportData() {
      //导出excel并下载   一定要浏览器调用，不用异步请求因为无刷新
      window.location.href = this.BASE_API + "/admin/core/dict/export";
    },

    load(tree, treeNode, resolve) {
      //获取数据
      dictApi.listByParentId(tree.id).then((response) => {
        resolve(response.data.list);
      });
    },
  },
};
</script>
