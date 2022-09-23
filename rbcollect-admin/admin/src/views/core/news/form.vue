<template>
  <div class="app-container">
    <el-image
      v-if="this.news.id"
      :src="news.newsImg"
      class="image"
      style="width: 30%; height: 30%"
    />
    <!-- 输入表单 -->
    <el-form label-width="50px">
      <el-tag type="success">新闻标题</el-tag>
      <el-input
        placeholder="请输入内容"
        v-model="news.title"
        clearable
        size="medium"
      >
      </el-input>
      <div style="margin: 20px 0"></div>
      <el-tag type="success">新闻内容</el-tag>
      <el-input
        type="textarea"
        autosize
        placeholder="请输入内容"
        v-model="news.content"
      >
      </el-input>

      <div style="margin: 20px 0"></div>
      <el-upload
        :on-success="onUploadSuccessImg"
        :on-remove="onUploadRemove"
        :multiple="false"
        :action="uploadUrl"
        :data="{ module: 'newsImg' }"
        :limit="1"
        list-type="picture-card"
        style=""
      >
        <i class="el-icon-plus"></i>
      </el-upload>
      <el-form-item>
        <el-button
          :disabled="saveBtnDisabled"
          type="primary"
          @click="saveOrUpdate()"
          style="margin-left: -45px; margin-top: 20px"
        >
          保存
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import newsApi from "@/api/core/news";
export default {
  data() {
    return {
      saveBtnDisabled: false, //是否禁用保存按钮，防止重复提交
      news: {}, //新闻对象
      uploadUrl: "http://localhost/api/oss/file/upload", //文件上传地址
      deleteUrl: "",
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
      newsApi.getById(id).then((response) => {
        this.news = response.data.record;
      });
    },
    //保存或更新
    saveOrUpdate() {
      this.saveBtnDisabled = true;
      if (!this.news.id) {
        //调用新增
        this.saveData();
      } else {
        //调用更新
        this.updateData();
      }
    },

    saveData() {
      newsApi.save(this.news).then((response) => {
        this.$message({
          message: response.message,
          type: "success",
        });
      });
      //好东西
      this.$router.push("/core/news/list"); //跳转路由
    },

    updateData() {
      newsApi.updateById(this.news).then((response) => {
        this.$message({
          message: response.message,
          type: "success",
        });
      });
      this.$router.push("/core/news/list"); //跳转路由
    },
    onUploadSuccessImg(response, file, type) {
      if (response.code === 0) {
        this.$message({
          message: response.message,
          type: "success",
        });
        this.news.newsImg = file.response.data.url;
        console.log(this.news.newsImg);
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
      newsApi.onUploadRemove(this.deleteUrl).then((response) => {
        this.$message({
          message: response.message,
          type: "success",
        });
      });
      location.reload();
    },
  },
};
</script>
<style scoped></style>
