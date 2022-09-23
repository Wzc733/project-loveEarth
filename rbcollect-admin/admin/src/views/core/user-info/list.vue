<template>
  <div class="app-container">
    <!--查询表单-->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item label="手机号">
        <el-input v-model="searchObj.mobile" placeholder="手机号" />
      </el-form-item>

      <el-form-item label="用户状态">
        <el-select v-model="searchObj.status" placeholder="请选择" clearable>
          <el-option label="正常" value="112" />
          <el-option label="锁定" value="111" />
        </el-select>
      </el-form-item>

      <el-button type="primary" icon="el-icon-search" @click="fetchData()">
        查询
      </el-button>
      <el-button type="default" @click="resetData()">清空</el-button>
    </el-form>

    <!-- 列表 -->
    <el-table :data="list" border stripe>
      <el-table-column label="#" width="50">
        <template slot-scope="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column prop="mobile" label="手机号" />
      <el-table-column prop="name" label="用户姓名" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column prop="integral" label="用户积分" />
      <el-table-column prop="createTime" label="注册时间" width="100" />
      <el-table-column label="用户状态" width="90">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 111" type="danger" size="mini">
            锁定
          </el-tag>
          <el-tag v-else type="success" size="mini">正常</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="200">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.status == 112"
            type="primary"
            size="mini"
            @click="lock(scope.row.id, 111)"
          >
            锁定
          </el-button>
          <el-button
            v-else
            type="danger"
            size="mini"
            @click="lock(scope.row.id, 112)"
          >
            解锁
          </el-button>
          <el-button
            type="primary"
            size="mini"
            @click="showLoginRecord(scope.row.id)"
          >
            登录日志
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <el-pagination
      :current-page="page"
      :total="total"
      :page-size="limit"
      :page-sizes="[5, 10, 15]"
      style="padding: 30px 0"
      layout="total, sizes, prev, pager, next,->, jumper"
      @size-change="changePageSize"
      @current-change="changeCurrentPage"
    />
    <!-- 用户登录日志 -->
    <el-dialog title="用户登录日志" :visible.sync="dialogTableVisible">
      <el-table :data="loginRecordList" border stripe>
        <el-table-column type="index" />
        <el-table-column prop="ip" label="IP" />
        <el-table-column prop="createTime" label="登录时间" />
      </el-table>
    </el-dialog>
  </div>
</template>
<script>
// {{ (page - 1) * limit + scope.$index + 1 }} 自定义序列号公式 scope.$index是前端界面的索引,无关id
// slot-scope
import userInfoApi from "@/api/core/user-info";
export default {
  data() {
    return {
      list: null, // 数据列表
      total: 0, // 数据库中的总记录数
      page: 1, // 默认页码
      limit: 10, // 每页记录数
      searchObj: {}, // 查询条件
      loginRecordList: [], //会员登录日志
      dialogTableVisible: false, //对话框是否显示
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    changePageSize(size) {
      //有一个回调参数:每页条数,element-ui帮我们组装好了,只需要接收就行
      this.limit = size;
      this.fetchData();
    },
    changeCurrentPage(page) {
      this.page = page;
      this.fetchData();
    },
    fetchData() {
      userInfoApi
        .getPageList(this.page, this.limit, this.searchObj)
        .then((response) => {
          this.list = response.data.pageModel.records;
          this.total = response.data.pageModel.total;
        });
    },
    resetData() {
      //还原表单
      this.searchObj = {};
      //重新查询
      this.fetchData();
    },
    lock(id, status) {
      userInfoApi.lock(id, status).then((response) => {
        this.$message.success(response.message);
        this.fetchData();
      });
    },
    showLoginRecord(id) {
      userInfoApi.getuserLoginRecordTop50(id).then((response) => {
        this.loginRecordList = response.data.list;
        this.dialogTableVisible = true;
      });
    },
  },
};
</script>
