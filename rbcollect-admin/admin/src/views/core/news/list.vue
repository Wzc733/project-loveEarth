<template>
  <div class="app-container">
    <!-- 列表 -->
    <el-table :data="list" border stripe>
      <el-table-column label="编号" width="50">
        <template slot-scope="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column prop="title" label="新闻标题" />
      <el-table-column prop="content" label="新闻内容" show-overflow-tooltip />
      <el-table-column label="新闻封面">
        <template slot-scope="scope">
          <el-image
            style="width: 100%; height: 100%"
            :src="scope.row.newsImg"
          ></el-image>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" />
      <el-table-column label="操作">
        <template slot-scope="scope">
          <router-link
            :to="'/core/news/edit/' + scope.row.id"
            style="margin-right: 5px"
          >
            <el-button type="primary" size="mini" icon="el-icon-edit">
              修改
            </el-button>
          </router-link>
          <el-button
            type="danger"
            size="mini"
            icon="el-icon-delete"
            @click="removeById(scope.row.id)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <el-pagination
      :current-page="page"
      :total="total"
      :page-size="limit"
      :page-sizes="[6, 10, 14]"
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
import newsApi from "@/api/core/news";
export default {
  data() {
    return {
      list: null, // 数据列表
      total: 0, // 数据库中的总记录数
      page: 1, // 默认页码
      limit: 6, // 每页记录数
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
      newsApi.getPageList(this.page, this.limit).then((response) => {
        this.list = response.data.pageModel.records;
        this.total = response.data.pageModel.total;
        console.log(this.list);
      });
    },

    removeById(id) {
      this.$confirm("此操作将永久删除该记录, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          return newsApi.removeById(id);
        }) //防止回调地狱 return后的对象再then 从嵌套then到串联then
        .then((response) => {
          this.$message({
            message: response.message,
            type: "success",
          });
          this.fetchData();
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
