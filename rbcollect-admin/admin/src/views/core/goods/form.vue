<template>
  <div class="app-container">
    <el-form
      :label-position="labelPosition"
      label-width="80px"
      :model="formLabelAlign"
    >
      <el-form-item label="商品名">
        <el-input v-model="GoodsQuery.name"></el-input>
      </el-form-item>
      <el-form-item label="所需积分">
        <el-input v-model="GoodsQuery.goodsIntegral"></el-input>
      </el-form-item>
      <el-form-item label="商品说明">
        <el-input v-model="GoodsQuery.content"></el-input>
      </el-form-item>
      <el-form-item label="库存">
        <el-input v-model="GoodsQuery.volume"></el-input>
      </el-form-item>
    </el-form>

    <el-upload
      :on-success="onUploadSuccessImg"
      :on-remove="onUploadRemove"
      :multiple="false"
      :action="uploadUrl"
      :data="{ module: 'GoodsImg' }"
      :limit="1"
      list-type="picture-card"
      style="margin-left: 90px"
    >
      <i class="el-icon-plus"></i>
    </el-upload>
    <el-divider><i class="el-icon-mobile-phone"></i></el-divider>
    <el-button
      type="success"
      size="big"
      @click="saveGoods"
      style="margin-left: 685px"
    >
      上传
    </el-button>
  </div>
</template>
<script>
import GoodsApi from "@/api/core/goods";
export default {
  data() {
    return {
      uploadUrl: "http://localhost/api/oss/file/upload", //文件上传地址
      deleteUrl: "",
      GoodsQuery: {},
    };
  },

  methods: {
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
      GoodsApi.onUploadRemove(this.deleteUrl).then((response) => {
        this.$message({
          message: response.message,
          type: "success",
        });
      });
      location.reload();
    },
    saveGoods() {
      GoodsApi.save(this.GoodsQuery).then((response) => {
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
