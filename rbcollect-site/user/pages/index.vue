<template>
  <main>
    <div>
      <el-carousel
        :interval="3000"
        type="card"
        height="400px"
        loop
        style="margin:20px"
      >
        <el-carousel-item v-for="item in urlList" :key="item">
          <el-image style="width: 100%; height: 100%" :src="item"></el-image>
        </el-carousel-item>
      </el-carousel>
    </div>
    <el-divider></el-divider>
    <div class="new-announcement">
      <div class="new-announcement-title">最新公告</div>
      <div class="new-announcement-content">
        <div id="scrollDiv">
          <ul style="margin-top: 0px;">
            <li>
              <a
                class="black-link"
                href="https://www.baidu.com/s?wd=%E7%88%B1%E5%9C%B0%E7%90%83"
                target="_blank"
              >
                欢迎大家来到爱地球回收站，爱地球回收站，可以帮您解决垃圾回收分类难题，垃圾回收上门回收，安全可靠，居家必备！！！
              </a>
            </li>
          </ul>
        </div>
      </div>
    </div>
    <div class="ipubs">
      <span class="o1"
        >累计注册人数:<strong>{{ countUser }}</strong
        >人</span
      >
      <span class="o1"
        >累计垃圾回收次数:<strong>{{ countRb }}</strong
        >次</span
      >
      <span class="o1"
        >已回收利用垃圾约:<strong>{{ countRb * 4 }}</strong
        >斤</span
      >
    </div>
    <div class="feature">
      <a class="fea1" href="#">
        <i></i>
        <h3>减少资源消耗</h3>
        <span>
          垃圾分类，去掉能回收的、不易降 解的物质，减少垃圾数量达50%以上
        </span>
      </a>
      <a class="fea2" href="#">
        <i></i>
        <h3>变废为宝</h3>
        <span>
          生产垃圾中有30%-40%可以回收利用，应珍惜这个小本大利的资源。
        </span>
      </a>
      <a class="fea3" href="#">
        <i></i>
        <h3>经济利益最大化</h3>
        <span>
          实现可回收物的最大限度的回收，可回收物作为再生资源减缓了对能源的消耗。
        </span>
      </a>
      <a class="fea4" href="#">
        <i></i>
        <h3>提高垃圾处理效率</h3>
        <span>
          可以减少用于填埋的垃圾中湿垃圾和有毒垃圾的含量，减少环境污染。
        </span>
      </a>
    </div>
  </main>
</template>

<script>
import '~/assets/css/index.css'
export default {
  async asyncData({ $axios }) {
    let response1 = await $axios.$get('/api/core/rbDealItem/getCountRb')
    let response2 = await $axios.$get('/api/core/userInfo/getCountUser')
    return {
      countRb: response1.data.countRb,
      countUser: response2.data.countUser,
    }
  },
  data() {
    return {
      urlList: '',
    }
  },
  created() {
    this.getImgUrl()
  },
  methods: {
    getImgUrl() {
      this.$axios
        .$get('/api/oss/file/get?prefix=' + 'icon/rb')
        .then((response) => {
          this.urlList = response.data.urlList
        })
    },
  },
}
</script>
<style>
.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}
</style>
