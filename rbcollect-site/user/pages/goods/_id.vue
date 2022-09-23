<template>
  <el-container>
    <el-aside width="200px"></el-aside>
    <el-main style="height:850px">
      <div>
        <el-image
          style="width: 400px; height: 450px"
          :src="goodsDetail.goodsImg"
          :preview-src-list="srcList"
        >
        </el-image>
      </div>
      <div style="margin-top:-450px;margin-left:450px;width:500px">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <strong style="font-size:20px">{{ goodsDetail.name }}</strong>
          </div>
          <div class="text item">
            消耗积分：<strong style="font-size:30px;color:red"
              >{{ goodsDetail.goodsIntegral }}.00</strong
            >
          </div>
          <div class="text item">
            剩余容量：<strong style="font-size:10px">{{
              goodsDetail.volume
            }}</strong>
          </div>
          <el-divider></el-divider>
          <el-input-number
            v-model="num"
            :min="1"
            :max="5"
            label="购买数量"
          ></el-input-number>
          <el-button type="danger" @click="open()">选择地址</el-button>
          <el-button type="danger" plain :disabled="disable" @click="buyGoods()"
            >购买</el-button
          >
        </el-card>
        <el-card style="margin-top:20px;height:500px">
          <el-tabs v-model="activeName" @tab-click="handleClick">
            <el-tab-pane label="商品描述" name="first">{{
              goodsDetail.content
            }}</el-tab-pane>
            <el-tab-pane label="评价" name="second">
              <div style="height:420px;overflow-y:auto">
                <div
                  style="float: left;width:100%;"
                  v-for="item in evaluateList"
                  :key="item.id"
                >
                  <ul>
                    <li>
                      <el-avatar :size="size" :src="item.headImg"></el-avatar>
                      <span
                        style="position:relative;top:-20px;font-weight:200;font-size:12px"
                        >{{ item.userName }}</span
                      >
                      <el-rate
                        v-model="item.commodityGrade"
                        disabled
                        show-text
                      ></el-rate>
                    </li>
                    <li>
                      <span>{{ item.evaluateContent }}</span>
                    </li>
                    <li
                      style="font-size:10px;font-weight:200;margin-top:5px;margin-left:350px"
                    >
                      {{ item.createTime }}
                    </li>
                    <li>
                      <el-divider></el-divider>
                    </li>
                  </ul>
                </div>
              </div>
            </el-tab-pane> </el-tabs
        ></el-card>
      </div>
      <!-- 用户登录日志 -->
      <el-dialog
        title="用户地址
      "
        :visible.sync="dialogTableVisible"
      >
        <ul>
          <li>
            <span>我的地址： </span>
            <el-select
              v-model="goodsDetail.userLocalId"
              placeholder="请选择"
              style="width:400px"
            >
              <el-option
                v-for="item in UserLocalList"
                :key="item.id"
                :label="item.local"
                :value="item.id"
              >
              </el-option>
            </el-select>
          </li>
          <li>
            <el-button
              type="danger"
              plain
              style="margin-left:410px;margin-top:10px"
              @click="close()"
              >确定</el-button
            >
          </li>
        </ul>
      </el-dialog>
      <!-- <el-divider style="margin-top:800px"></el-divider>
      <div style="text-align:center;">已经到底了</div> -->
    </el-main>

    <el-aside width="200px"> </el-aside>
  </el-container>
</template>

<script>
import cookie from 'js-cookie'
export default {
  async asyncData({ $axios, params }) {
    let goodsId = params.id
    let response = await $axios.$get('/admin/core/goods/get/' + goodsId)

    return {
      goodsDetail: response.data.goodsQuery, //商品详情
      num: 1, //购买数量
    }
  },
  data() {
    return {
      srcList: [
        'https://rbcollect-file-wzc.oss-cn-guangzhou.aliyuncs.com/goodDetail/img1.jpg',
        'https://rbcollect-file-wzc.oss-cn-guangzhou.aliyuncs.com/goodDetail/img2.jpg',
      ],
      activeName: 'first',
      errorPage: 1,
      UserLocalList: {},
      dialogTableVisible: false,
      disable: true,
      userInfo: {},
      evaluateList: {},
    }
  },
  created() {},
  methods: {
    fetchLocalList() {
      //判断用户有无登陆
      let userInfo = cookie.get('userInfo')
      if (userInfo) {
        this.$axios
          .$get('/api/core/userLocal/auth/getUserLocalWithString')
          .then((response) => {
            this.UserLocalList = response.data.userLocalDTOList
          })
      } else {
        this.saveBtnDisabled = true
        this.$message.error('请先登陆 即将跳转登录页...')
        const timejump = 1
        if (!this.timer) {
          this.count = timejump
          this.show = false
          this.timer = setInterval(() => {
            if (this.count > 0 && this.count <= timejump) {
              this.count--
            } else {
              this.show = true
              clearInterval(this.timer)
              this.timer = null
              //跳转的页面写在此处
              this.$router.push({ path: '/login' })
            }
          }, 1700)
        }
      }
    },
    open() {
      this.dialogTableVisible = true
      this.fetchLocalList()
    },
    close() {
      this.dialogTableVisible = false
      this.disable = false
    },
    buyGoods() {
      this.$axios
        .$post(
          '/api/core/goodsDealItem/auth/save?num=' + this.num,
          this.goodsDetail
        )
        .then((response) => {
          console.log(response.data.message)
          this.$message.success(response.message)
          setTimeout('location.reload () ', 1500)
        })
    },
    handleClick(tab) {
      if (tab.name === 'second') {
        this.$axios
          .$get(
            '/api/core/goodsEvaluate/listFiftyEvaluate?id=' +
              this.$route.params.id
          )
          .then((response) => {
            this.evaluateList = response.data.listFiftyEvaluate
          })
      }
    },
  },
}
</script>
<style>
/* .mainEl {
  background-color: rgb(147, 237, 154);
} */
</style>
