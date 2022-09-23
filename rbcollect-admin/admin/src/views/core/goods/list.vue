<template>
  <div class="app-container">
    <el-row>
      <el-col :span="4" v-for="item in list" :key="item.id" :offset="1">
        <el-card style="margin-bottom: 20px; height: 470px">
          <el-image :src="item.goodsImg" class="image" style="height: 200px" />
          <div>
            <ul style="margin-left: -40px">
              <li>
                <span style=""><b>商品名:</b>{{ item.name }}</span>
              </li>

              <div class="bottom clearfix">
                <li style="margin-top: 10px">
                  <b>描述:</b
                  ><span style="line-height: 1em">{{
                    item.content | ellipsis
                  }}</span>
                </li>
                <li>
                  <div style="margin-top: 10px">
                    <b>积分:</b>{{ item.goodsIntegral }}
                  </div>
                </li>
                <li>
                  <div style="margin-top: 10px">
                    <b>剩余:</b>{{ item.volume }}
                  </div>
                </li>
              </div>
            </ul>
          </div>

          <el-button
            type="danger"
            size="mini"
            icon="el-icon-delete"
            style=""
            @click="removeById(item.id)"
            >删除</el-button
          >

          <router-link
            :to="'/core/goods/edit/' + item.id"
            style="margin-right: 5px"
          >
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-edit"
              style="margin-left: 5px"
            >
              修改
            </el-button>
          </router-link>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import goodsApi from "@/api/core/goods";
export default {
  filters: {
    //限制文本显示字数,超出部分用...代替
    ellipsis(value) {
      if (!value) return "";
      if (value.length > 25) {
        return value.slice(0, 25) + "..."; //0:下标,从第一个字开始显示,50:显示字数,多余用...代替
      }
      return value;
    },
  },

  data() {
    return {
      list: [], //列表
    };
  },

  created() {
    this.fetchData();
  },

  methods: {
    fetchData() {
      goodsApi.list().then((response) => {
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
          goodsApi.removeById(id).then((response) => {
            this.$message({
              message: response.message,
              type: "success",
            });
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
<style>
.bottom {
  margin-top: 13px;
  line-height: 12px;
}

.button {
  padding: 0;
  float: right;
}

.image {
  width: 200px;
  display: block;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both;
}
</style>
