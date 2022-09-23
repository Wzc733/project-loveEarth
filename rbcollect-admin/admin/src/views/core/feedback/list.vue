<template>
  <div class="app-container">
    <!--查询表单-->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item label="状态：">
        <el-select v-model="searchObj.status" placeholder="请选择">
          <el-option
            v-for="item in optionsStatus"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="反馈类型：">
        <el-select v-model="searchObj.feedbackType" placeholder="请选择">
          <el-option
            v-for="item in optionsType"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-button type="primary" icon="el-icon-search" @click="fetchData()">
        查询
      </el-button>
      <el-button type="default" @click="resetData()">清空</el-button>
    </el-form>
    <el-alert title="请注意查询用户反馈" type="warning" show-icon> </el-alert>
    <!-- 列表 -->
    <el-table :data="list" border stripe>
      <el-table-column label="#" width="50">
        <template slot-scope="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column prop="feedbackType" label="反馈类型" />
      <el-table-column prop="createTime" label="反馈时间" />
      <el-table-column label="状态" width="138">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status == noDone()" type="danger" size="mini">
            未查阅
          </el-tag>
          <el-tag type="success" size="mini" v-else>{{
            scope.row.status
          }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="300">
        <template slot-scope="scope">
          <el-button
            type="info"
            size="mini"
            @click="deatail(scope.row.id)"
            v-if="scope.row.status == noDone()"
          >
            查看详细信息
          </el-button>
          <el-button
            type="success"
            size="mini"
            @click="deatail(scope.row.id)"
            v-else
          >
            查看详细信息
          </el-button>
          <el-drawer
            title="反馈内容详细"
            :visible.sync="drawer"
            :direction="direction"
            v-if="isLoad()"
            :before-close="handleClose"
            size="300px"
          >
            <li style="font-size: 20px">
              反馈内容：{{ feedBackDeatail.feedbackContent }}
            </li>
            <li style="font-size: 20px">
              联系反馈人姓名：{{ feedBackDeatail.linkmanName }}
            </li>
            <li style="font-size: 20px">
              联系反馈人号码：{{ feedBackDeatail.linkmanMobile }}
            </li>
          </el-drawer>
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
import feedBackApi from "@/api/core/feedback.js";

export default {
  data() {
    return {
      feedback: {},
      optionsStatus: [
        {
          value: "6",
          label: "未查阅",
        },
        {
          value: "7",
          label: "已查阅",
        },
      ],
      optionsType: [
        {
          value: "201",
          label: "积分充值",
        },
        {
          value: "202",
          label: "商品交易",
        },
        {
          value: "203",
          label: "垃圾回收",
        },
        {
          value: "204",
          label: "其它",
        },
      ],
      list: null, // 数据列表
      total: 0, // 数据库中的总记录数
      page: 1, // 默认页码
      limit: 10, // 每页记录数
      searchObj: {}, // 查询条件
      drawer: false,
      feedBackDeatail: null,
      direction: "btt",
      flag: false,
      feedbackId: null,
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    noDone() {
      return "未查阅";
    },
    Done() {
      return "已查阅";
    },
    isLoad() {
      return this.flag;
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
      feedBackApi
        .getList(this.page, this.limit, this.searchObj)
        .then((response) => {
          this.list = response.data.pageModel.records;
          this.total = response.data.pageModel.total;
        });
    },
    deatail(id) {
      feedBackApi.getDetail(id).then((response) => {
        this.feedBackDeatail = response.data.userFeedback;
        this.feedbackId = id;
        this.drawer = true;
        this.flag = true;
      });
    },
    read(id) {
      feedBackApi.read(id).then((response) => {
        this.$message.success(response.message);
        setTimeout("location.reload () ", 1500);
      });
    },
    handleClose(Done) {
      this.read(this.feedbackId);
      Done();
    },
    resetData() {
      //还原表单
      this.searchObj = {};
      //重新查询
      this.fetchData();
    },
  },
};
</script>
