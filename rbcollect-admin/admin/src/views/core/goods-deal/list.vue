<template>
  <div class="app-container">
    <!--查询表单-->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item label="购买商品的用户id">
        <el-input v-model="searchObj.payUserId" placeholder="请输入:" />
      </el-form-item>

      <el-form-item label="商品id">
        <el-input v-model="searchObj.goodsId" placeholder="请输入:" />
      </el-form-item>

      <el-button type="primary" icon="el-icon-search" @click="fetchData()">
        查询
      </el-button>
      <el-button type="default" @click="resetData()">清空</el-button>
    </el-form>
    <el-alert
      title="请在交易详细查看买家有无取消订单操作"
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

      <el-table-column prop="payUserId" label="消费者的id" />
      <el-table-column prop="payUserName" label="消费者姓名" />
      <el-table-column prop="goodsId" label="商品id" />
      <el-table-column prop="goodsName" label="商品名称" />
      <el-table-column prop="createTime" label="下单时间" width="100" />
      <el-table-column label="交易状态" width="90">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status == noDone()" type="danger" size="mini">
            未完成
          </el-tag>
          <el-tag type="success" size="mini" v-else>{{
            scope.row.status
          }}</el-tag>

          <el-tag v-if="scope.row.cancleCycle === true" type="error" size="mini"
            >用户取消商品购买</el-tag
          >
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="300">
        <template slot-scope="scope">
          <el-button
            type="info"
            size="mini"
            @click="sendGoods(scope.row.id)"
            v-if="scope.row.status == noDone()"
          >
            进行发货
          </el-button>
          <el-button
            type="danger"
            size="mini"
            @click="DoneGoods(scope.row.id)"
            v-else-if="
              scope.row.status == Doing() || scope.row.status == Check()
            "
          >
            确认交易完成
          </el-button>
          <el-button
            type="success"
            size="mini"
            v-else-if="scope.row.status == Done()"
            :disabled="true"
          >
            交易完成
          </el-button>

          <el-button
            v-if="scope.row.status === Check()"
            type="primary"
            size="mini"
            @click="deleteDeal(scope.row.id)"
          >
            取消交易
          </el-button>
          <!-- ------------------------------------------------ -->
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
import goodsDealApi from "@/api/core/goods-deal.js";
import transFlowApi from "@/api/core/transflow.js";

export default {
  data() {
    return {
      list: null, // 数据列表
      total: 0, // 数据库中的总记录数
      page: 1, // 默认页码
      limit: 10, // 每页记录数
      searchObj: {}, // 查询条件
      TransFlowlList: null, //流水线详细
      GoodsDealDetailList: null, //商品详细
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
      return "发货中";
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
      goodsDealApi
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
    sendGoods(id) {
      this.$confirm("此操作将发货, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          goodsDealApi.sendGoods(id).then((response) => {
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
    deleteDeal(id) {
      this.$confirm("此操作将取消交易,继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          goodsDealApi.removeById(id).then((response) => {
            this.$message.success(response.message);
            setTimeout("location.reload () ", 1700);
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
    DoneGoods(id) {
      this.$confirm("此操作将完成商品交易,继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          goodsDealApi.DondGoods(id).then((response) => {
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
  },
};
</script>
