<template>
  <el-container>
    <el-header style="height:150px">
      <div>
        <img
          src="~/assets/images/shop.png"
          style="width:100px;margin-left:350px;margin-top:20px"
        />
        <el-input
          placeholder="购物袋"
          v-model="goodsName"
          class="input-with-select"
          size="large"
          style="position:'absolute';top:-40px;right:-10px;width:600px;"
        >
          <el-button
            slot="append"
            icon="el-icon-search"
            @click="select()"
          ></el-button>
        </el-input>
      </div>
    </el-header>
    <el-container>
      <el-aside width="150px"></el-aside>
      <el-main style="background-color:white;margin-bottom:60px">
        <el-carousel :interval="3000" type="card" height="300px" loop>
          <el-carousel-item v-for="item in urlList" :key="item">
            <el-image style="width: 100%; height: 100%" :src="item"></el-image>
          </el-carousel-item>
        </el-carousel>
        <el-row>
          <el-col
            :span="5"
            v-for="item in goods"
            :key="item.id"
            :offset="1"
            style="margin-left:35px;margin-top:20px"
          >
            <el-card :body-style="{ padding: '0px' }" style="height:330px">
              <NuxtLink :to="'/goods/' + item.id">
                <el-image
                  :src="item.goodsImg"
                  class="image"
                  :fit="contain"
                  style="height:225px;width:100%"
                ></el-image>
              </NuxtLink>
              <div style="padding: 14px;">
                <span>{{ item.name }}</span>
              </div>
            </el-card>
          </el-col>
        </el-row>
        <el-divider content-position="center">没有啦</el-divider>
      </el-main>
      <el-aside width="200px"></el-aside>
    </el-container>
  </el-container>
</template>

<script>
import '~/assets/css/index.css'
import '~/assets/css/detail.css'
export default {
  data() {
    return {
      urlList: '',
      goods: {},
      goodsName: '',
    }
  },
  created() {
    this.fetchData()
    this.getImgUrl()
  },
  methods: {
    fetchData() {
      this.$axios.$get('/admin/core/goods/list/').then((response) => {
        this.goods = response.data.list
      })
    },
    getImgUrl() {
      this.$axios
        .$get('/api/oss/file/get?prefix=' + 'goodsShow/goodsShow')
        .then((response) => {
          this.urlList = response.data.urlList
        })
    },
    select() {
      this.$axios
        .$get('/api/core/goods/selectGoods?goodsName=' + this.goodsName)
        .then((response) => {
          this.goods = response.data.goodsList
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
</style>
