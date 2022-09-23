<template>
  <div class="app-container">
    <el-table :data="list" border stripe>
      <el-table-column type="index" width="50" />
      <el-table-column prop="integralAmount" label="积分额度" />
      <el-table-column prop="weightStart" label="重量区间开始(kg)" />
      <el-table-column prop="weightEnd" label="重量区间结束(kg)" />
      <el-table-column label="操作">
        <template slot-scope="scope">
          <router-link
            :to="'/core/integral-grade/edit/' + scope.row.id"
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
  </div>
</template>
<script>
import intergralGradeApi from "@/api/core/integral-grade";
export default {
  data() {
    return {
      list: [], //积分等级列表
    };
  },

  created() {
    this.fetchData();
  },

  methods: {
    fetchData() {
      intergralGradeApi.list().then((response) => {
        this.list = response.data.list;
      });
    },

    removeById(id) {
      this.$confirm("此操作将永久删除该记录, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          return intergralGradeApi.removeById(id);
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
<style scoped></style>
