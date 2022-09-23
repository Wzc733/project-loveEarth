<template>
  <div class="app-container">
    <!--查询表单-->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item label="申请垃圾回收的用户id">
        <el-input v-model="searchObj.rbUserId" placeholder="请输入:" />
      </el-form-item>

      <el-button type="primary" icon="el-icon-search" @click="fetchData()">
        查询
      </el-button>
      <el-button type="default" @click="resetData()">清空</el-button>
    </el-form>
    <el-alert
      title="请在交易详细查看买家有无取消垃圾回收订单操作"
      type="warning"
      show-icon
    >
    </el-alert>
    <!-- 列表 -->
    <el-table :data="list" border stripe>
      <el-table-column label="#" width="50">
        <template slot-scope="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column prop="rbUserId" label="申请回收用户的id" />
      <el-table-column prop="rbUserName" label="用户姓名" />
      <el-table-column prop="rbName" label="垃圾名称" />
      <el-table-column prop="rbType" label="垃圾类型" />
      <el-table-column prop="rbWeight" label="垃圾重量" />
      <el-table-column prop="deliveryUserLocal" label="回收地址" />
      <el-table-column prop="deliveryUserName" label="联系人" />
      <el-table-column prop="deliveryUserMobile" label="手机号码" />
      <el-table-column prop="expectIntegral" label="用户收益积分" />
      <el-table-column prop="createTime" label="下单时间" width="100" />
      <el-table-column label="交易状态" width="138">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status == noDone()" type="danger" size="mini">
            未完成
          </el-tag>
          <el-tag type="success" size="mini" v-else>{{
            scope.row.status
          }}</el-tag>

          <el-tag v-if="scope.row.cancleCycle === true" type="error" size="mini"
            >用户取消垃圾回收</el-tag
          >
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="300">
        <template slot-scope="scope">
          <el-button
            type="info"
            size="mini"
            @click="sendRb(scope.row.id)"
            v-if="scope.row.status == noDone()"
          >
            进行回收
          </el-button>
          <el-button
            type="danger"
            size="mini"
            @click="DoneRb(scope.row.id)"
            v-else-if="
              scope.row.status == Doing() || scope.row.status == Check()
            "
          >
            确认回收完成
          </el-button>
          <el-button
            type="success"
            size="mini"
            v-else-if="scope.row.status == Done()"
            :disabled="true"
          >
            回收完成
          </el-button>

          <el-button
            v-if="scope.row.status === Check()"
            type="primary"
            size="mini"
            @click="deleteDeal(scope.row.id)"
          >
            取消回收
          </el-button>

          <el-divider direction="vertical"></el-divider>
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
  </div>
</template>
<script>
// {{ (page - 1) * limit + scope.$index + 1 }} 自定义序列号公式 scope.$index是前端界面的索引,无关id
// slot-scope
import rbDealApi from "@/api/core/rb-deal.js";

export default {
  data() {
    return {
      list: null, // 数据列表
      total: 0, // 数据库中的总记录数
      page: 1, // 默认页码
      limit: 10, // 每页记录数
      searchObj: {}, // 查询条件
      TransFlowlList: null, //流水线详细
      dialogTableVisible: false, //对话框是否显示
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    noDone() {
      return "未完成";
    },
    Doing() {
      return "已联系人员上门回收";
    },
    Done() {
      return "已完成";
    },
    Check() {
      return "审核中/处理中";
    },
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
      rbDealApi
        .getPageList(this.page, this.limit, this.searchObj)
        .then((response) => {
          this.list = response.data.pageModel.records;
          console.log(this.list);
          this.total = response.data.pageModel.total;
        });
    },
    resetData() {
      //还原表单
      this.searchObj = {};
      //重新查询
      this.fetchData();
    },
    sendRb(id) {
      console.log(id);
      this.$confirm(
        "此操作将进行垃圾回收, 是否已经查看该订单的详情,继续?",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(() => {
          rbDealApi.sendRb(id).then((response) => {
            this.$message.success(response.message);
            this.fetchData();
          });
        })
        .catch((error) => {
          if (error === "cancle") {
            //点击取消时error的值是cancle 防止因其它出错拿这个处理
            this.$message({
              type: "info",
              message: "已取消删除",
            });
          }
        });
    },
    DoneRb(id) {
      this.$confirm("此操作将完成垃圾回收,继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          rbDealApi.DondRb(id).then((response) => {
            this.$message.success(response.message);
            this.fetchData();
          });
        })
        .catch((error) => {
          if (error === "cancle") {
            //点击取消时error的值是cancle 防止因其它出错拿这个处理
            this.$message({
              type: "info",
              message: "已取消删除",
            });
          }
        });
    },
    // showRbDealDetailList(dealItemNo) {
    //   transFlowApi.getTransFlowList(dealItemNo).then((response) => {
    //     this.TransFlowlList = response.data.list;
    //     this.dialogTableVisible = true;
    //   });
    // },
    deleteDeal(id) {
      this.$confirm("此操作将取消交易,继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          rbDealApi.removeById(id).then((response) => {
            this.$message.success(response.message);
            this.fetchData();
          });
        })
        .catch((error) => {
          if (error === "cancle") {
            //点击取消时error的值是cancle 防止因其它出错拿这个处理
            this.$message({
              type: "info",
              message: "已取消",
            });
          }
        });
    },
  },
};
</script>
