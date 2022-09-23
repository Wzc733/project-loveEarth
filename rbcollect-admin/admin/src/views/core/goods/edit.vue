<template>
  <div class="app-container">
    <el-row>
      <el-card padding="30px">
        <el-image
          :src="GoodsQuery.goodsImg"
          class="image"
          style="width: 30%; height: 30%"
        />
        <el-upload
          :on-success="onUploadSuccessImg"
          :on-remove="onUploadRemove"
          :multiple="false"
          :action="uploadUrl"
          :data="{ module: 'GoodsImg' }"
          :limit="1"
          list-type="picture-card"
          style="margin-left: 20px; margin-top: 20px"
        >
          <i class="el-icon-plus"></i>
        </el-upload>
        <div>
          <div style="margin: 20px; width: 40%">
            <b>商品名:</b><el-input v-model="GoodsQuery.name"></el-input>
          </div>
          <div class="bottom clearfix">
            <div style="margin: 20px; width: 40%">
              <b>描述:</b><el-input v-model="GoodsQuery.content"></el-input>
            </div>
            <div style="margin: 20px; width: 40%">
              <b>积分:</b
              ><el-input v-model="GoodsQuery.goodsIntegral"></el-input>
            </div>
            <div>
              <b style="margin: 20px">剩余数量上添加:</b>
              <el-input-number v-model="GoodsQuery.volume" :min="0" />
              <el-button
                type="primary"
                plain
                style="margin-left: 80px"
                @click="updateData"
                >修改</el-button
              >
            </div>
          </div>
        </div>
      </el-card>
    </el-row>
  </div>
</template>
<script>
import goodsApi from "@/api/core/goods";
export default {
  data() {
    return {
      uploadUrl: "http://localhost/api/oss/file/upload", //文件上传地址
      saveBtnDisabled: false, //是否禁用保存按钮，防止重复提交
      deleteUrl: "",
      GoodsQuery: [],
    };
  },
  created() {
    //当路由中存在id属性，就是回显表单组件
    if (this.$route.params.id) {
      this.fetchById(this.$route.params.id);
    }
  },

  methods: {
    //根据id获取数据
    fetchById(id) {
      goodsApi.getById(id).then((response) => {
        this.GoodsQuery = response.data.goodsQuery;
      });
    },
    onUploadSuccessImg(response, file, type) {
      if (response.code === 0) {
        this.$message({
          message: response.message,
          type: "success",
        });
        this.GoodsQuery.goodsImg = file.response.data.url;
      } else {
        this.$message({
          message: response.message,
          type: "error",
        });
        window.location.reload();
      }
    },
    onUploadRemove(file) {
      //从阿里云删除图片
      this.deleteUrl = file.response.data.url;
      goodsApi.onUploadRemove(this.deleteUrl).then((response) => {
        this.$message({
          message: response.message,
          type: "success",
        });
      });
      location.reload();
    },
    updateData() {
      goodsApi.updateById(this.GoodsQuery).then((response) => {
        this.$message({
          message: response.message,
          type: "success",
        });
      });
      this.$router.push("/core/goods/list"); //跳转路由
    },
  },
};
</script>
<style scoped></style>
