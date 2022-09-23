<template>
  <!--列表-->
  <div style="margin=30px">
    <el-container>
      <el-aside width="300px"></el-aside>
      <el-main>
        <el-tabs type="border-card">
          <el-tab-pane label="垃圾分类查询">
            <el-input
              placeholder="请输入内容"
              v-model="word"
              clearable
              style="width:200px"
            >
            </el-input>
            <el-button
              type="primary"
              icon="el-icon-search"
              @click="SearchRbSort()"
              >查询</el-button
            >
            <el-descriptions
              title="查询结果:"
              direction="vertical"
              :column="1"
              border
              style="margin-top:20px"
            >
              <el-descriptions-item label="垃圾名称">{{
                this.listFromFontSort.name
              }}</el-descriptions-item>
              <el-descriptions-item
                label="垃圾类型"
                v-if="this.listFromFontSort.type === 0"
                >可回收垃圾</el-descriptions-item
              >
              <el-descriptions-item
                label="垃圾类型"
                v-if="this.listFromFontSort.type === 1"
                >有害垃圾</el-descriptions-item
              >
              <el-descriptions-item
                label="垃圾类型"
                v-if="this.listFromFontSort.type === 2"
                >厨余垃圾(湿垃圾)</el-descriptions-item
              >
              <el-descriptions-item
                label="垃圾类型"
                v-if="this.listFromFontSort.type === 3"
                >其它垃圾(干垃圾)</el-descriptions-item
              >
              <el-descriptions-item label="小备注" :span="2">{{
                this.listFromFontSort.tip
              }}</el-descriptions-item>
              <el-descriptions-item label="分类解释" :span="2">{{
                this.listFromFontSort.explain
              }}</el-descriptions-item>
              <el-descriptions-item label="其它常见" :span="2">{{
                this.listFromFontSort.contain
              }}</el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>
        </el-tabs>
        <el-tabs type="border-card" style="margin-top:100px">
          <el-tab-pane label="图片识别">
            <el-form label-width="120px">
              <el-form-item label="请上传图片：">
                <el-upload
                  :on-success="onUploadSuccess"
                  :on-remove="onUploadRemove"
                  :multiple="false"
                  :action="uploadUrl"
                  :data="{ module: 'imgRecognition' }"
                  :limit="1"
                  list-type="picture-card"
                >
                  <i class="el-icon-plus"></i>
                </el-upload>
              </el-form-item>
            </el-form>
            <el-button
              type="primary"
              icon="el-icon-search"
              @click="SearchRbSortWithImg()"
              >查询</el-button
            >
            <el-descriptions
              title="查询结果:"
              direction="vertical"
              :column="1"
              border
              style="margin-top:20px"
            >
              <el-descriptions-item label="垃圾名称">{{
                this.listFromImgSort.name
              }}</el-descriptions-item>
              <el-descriptions-item
                label="垃圾类型"
                v-if="this.listFromImgSort.lajitype === 0"
                >可回收垃圾</el-descriptions-item
              >
              <el-descriptions-item
                label="垃圾类型"
                v-if="this.listFromImgSort.lajitype === 1"
                >有害垃圾</el-descriptions-item
              >
              <el-descriptions-item
                label="垃圾类型"
                v-if="this.listFromImgSort.lajitype === 2"
                >厨余垃圾(湿垃圾)</el-descriptions-item
              >
              <el-descriptions-item
                label="垃圾类型"
                v-if="this.listFromImgSort.lajitype === 3"
                >其它垃圾(干垃圾)</el-descriptions-item
              >
              <el-descriptions-item
                label="垃圾类型"
                v-if="this.listFromImgSort.lajitype === 4"
                >未知...请重新拍照查询</el-descriptions-item
              >
              <el-descriptions-item label="小备注" :span="2">{{
                this.listFromImgSort.lajitip
              }}</el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>
        </el-tabs>
      </el-main>
      <el-aside width="300px"></el-aside>
    </el-container>
  </div>
</template>

<script>
import '~/assets/css/index.css'
import '~/assets/css/detail.css'
export default {
  data() {
    let BASE_API = process.env.BASE_API
    return {
      word: '',
      listFromFontSort: '',
      imgUrl: '',
      uploadUrl: BASE_API + '/api/oss/file/uploadImg', //文件上传地址
      listFromImgSort: '',
    }
  },
  created() {
    // this.fetchUserData()
  },
  methods: {
    SearchRbSort() {
      this.$axios
        .$get('/api/core/rbDealItem/getRbSort?word=' + this.word)
        .then((response) => {
          this.listFromFontSort = response.data.RbSortResult.newslist[0]
        })
    },
    onUploadSuccess(response) {
      if (response.code === 0) {
        this.imgUrl = response.data.imgUrl
        this.$message.success(response.message)
      } else {
        this.$message.error(response.message)
      }
    },

    SearchRbSortWithImg() {
      this.$axios
        .$get('/api/core/rbDealItem/getRbSortByImg?imgUrl=' + this.imgUrl)
        .then((response) => {
          this.listFromImgSort = response.data.RbSortResultByImg.newslist[0]
        })
    },
  },
}
</script>
