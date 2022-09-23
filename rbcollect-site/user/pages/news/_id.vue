<template>
  <el-container>
    <el-aside width="400px"></el-aside>
    <el-main>
      <div>
        <span style="font-size:30px">
          {{ newDetail.title }}
        </span>
      </div>
      <div style="margin-top:20px;margin-bottom:20px">
        <span>{{ newDetail.createTime }}</span>
      </div>
      <div>
        <el-image
          style="width: 100%; height: 400px"
          :src="newDetail.newsImg"
        ></el-image>
      </div>
      <div>
        <p style="line-height:45px;font-size:20px;text-indent:2em;">
          {{ newDetail.content }}
        </p>
      </div>
      <el-divider></el-divider>
      <div style="text-align:center;">已经到底了</div>
    </el-main>

    <el-aside width="400px">
      <div
        style="width:50px;height:50px;position:fixed;no-repeat;padding-left:160px;padding-top:250px"
      >
        <NuxtLink :to="'/news'">
          <el-button type="warning" plain>退上一页</el-button>
        </NuxtLink>
        <NuxtLink :to="'/'">
          <el-button type="warning" plain>返回首页</el-button>
        </NuxtLink>
      </div>
    </el-aside>
  </el-container>
</template>

<script>
export default {
  async asyncData({ $axios, params }) {
    let newsId = params.id
    let response = await $axios.$get('/admin/core/news/get/' + newsId)

    return {
      newDetail: response.data.record, //新闻详情
    }
  },

  data() {
    return {}
  },
  created() {},
  methods: {},
}
</script>
<style>
/* .mainEl {
  background-color: rgb(147, 237, 154);
} */
</style>
