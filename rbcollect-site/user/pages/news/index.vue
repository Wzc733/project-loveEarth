<template>
  <!--列表-->

  <el-container>
    <el-aside width="100px"></el-aside>
    <el-main class="elMain">
      <div>
        <el-carousel :interval="3000" type="card" height="300px" loop>
          <el-carousel-item v-for="item in urlList" :key="item.id">
            <el-image style="width: 100%; height: 100%" :src="item"></el-image>
          </el-carousel-item>
        </el-carousel>
      </div>
      <el-row>
        <el-col
          :span="7"
          v-for="item in news"
          :key="item.id"
          :offset="1"
          style="margin-left:35px;margin-top:20px"
        >
          <el-card :body-style="{ padding: '0px' }" style="height:300px">
            <NuxtLink :to="'/news/' + item.id">
              <el-image
                :src="item.newsImg"
                class="image"
                style="height:225px;width:100%"
              ></el-image>
            </NuxtLink>
            <div style="padding: 14px;">
              <span>{{ item.title }}</span>
              <div class="bottom clearfix">
                <time class="time">爱地球报社:{{ item.createTime }}</time>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
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
    </el-main>
    <el-aside width="100px"></el-aside>
  </el-container>
</template>

<script>
import '~/assets/css/index.css'
import '~/assets/css/detail.css'
export default {
  data() {
    return {
      urlList: '',
      total: 0, // 数据库中的总记录数
      page: 1, // 默认页码
      limit: 6, // 每页记录数
      news: {},
    }
  },
  created() {
    this.getImgUrl()
    this.fetchData()
  },
  methods: {
    changePageSize(size) {
      //有一个回调参数:每页条数,element-ui帮我们组装好了,只需要接收就行
      this.limit = size
      this.fetchData()
    },
    changeCurrentPage(page) {
      this.page = page
      this.fetchData()
    },
    getImgUrl() {
      this.$axios
        .$get('/api/oss/file/get?prefix=' + 'icon/rb')
        .then((response) => {
          this.urlList = response.data.urlList
        })
    },
    fetchData() {
      this.$axios
        .$get('/admin/core/news/list/' + this.page + '/' + this.limit)
        .then((response) => {
          this.news = response.data.pageModel.records
          this.total = response.data.pageModel.total
          console.log(this.news)
        })
    },
  },
}
</script>
<style>
.time {
  font-size: 13px;
  color: #999;
}

.bottom {
  margin-top: 13px;
  line-height: 12px;
}

.button {
  padding: 0;
  float: right;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: '';
}

.clearfix:after {
  clear: both;
}
.elMain {
  background-color: azure;
}
</style>
