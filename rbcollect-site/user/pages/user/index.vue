<template>
  <div class="personal-main">
    <div class="pmain-profile" style="height:400px">
      <div class="pmain-welcome">
        <span class="fr">上次登录时间： {{ userIndexVO.lastLoginTime }} </span>
      </div>
      <div class="pmain-user">
        <div>
          <span class="head-img" v-if="userIndexVO.headImg">
            <span>
              <img
                :src="userIndexVO.headImg"
                style="width:88px;height:88px;z-index:0;"
              />
              <i class="headframe" style="z-index:0;"></i>
            </span>
          </span>
          <el-upload
            :on-success="onUploadSuccessImg"
            :multiple="false"
            :action="uploadUrl"
            :data="{ module: 'head_img' }"
            :limit="1"
            list-type="picture-card"
            v-else
          >
            <i class="el-icon-plus"></i>
          </el-upload>
        </div>
        <div class="userInfo">
          <ul>
            <li>
              <el-tag
                type="success"
                style="font-size:20px;position:absolute;left:600px;top:245px"
                >用户名:{{ userIndexVO.name }}</el-tag
              >
            </li>
            <div style="width:230px">
              <el-divider></el-divider>
            </div>
            <ul v-if="userIndexVO.bindLocal">
              <li style="margin:13px">
                <el-link type="primary" href="/local" style=""
                  >我的收获地址</el-link
                >
              </li>
              <li style="margin-left:13px">
                剩余积分:

                <span style="font-size:20px;">{{ userIndexVO.integral }}</span
                >分
              </li>
              <li style="margin:13px">
                <el-link type="primary" href="/user-update" style=""
                  >修改个人信息</el-link
                >
              </li>
            </ul>
            <li v-else>
              您还未设置个人地址请
              <el-link type="primary" href="/local" style="">立即设置</el-link>
              以确保您的正常使用。
            </li>
            <li>
              <div
                id="myChart"
                style="width: 400px; height: 300px;position:absolute;left:800px;top: 200px"
              ></div>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import cookie from 'js-cookie'
export default {
  data() {
    let BASE_API = process.env.BASE_API
    return {
      userIndexVO: {},
      uploadUrl: BASE_API + '/api/oss/file/upload', //文件上传地址
      imageUrl: '',
    }
  },

  mounted() {
    this.fetchUserData()
    this.fetchData()
  },

  methods: {
    fetchUserData() {
      //判断用户有无登陆
      let userInfo = cookie.get('userInfo')
      if (userInfo) {
        this.$axios
          .$get('/api/core/userInfo/auth/getIndexUserInfo')
          .then((response) => {
            this.userIndexVO = response.data.userIndexVO
          })
      } else {
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
    onUploadSuccessImg(response, file, type) {
      if (response.code === 0) {
        console.log(response)
        console.log(file.response.data.url)
        //上传成功,将头像信息填充到user_info的head_img
        this.$axios.$get(
          '/api/core/userInfo/auth/save/img?url=' + file.response.data.url
        )
        this.$message.success(response.message)
        this.imageUrl = file.response.data.url
        setTimeout('location.reload () ', 2000)
      } else {
        this.$message.error(response.message)
        setTimeout('location.reload () ', 2000)
      }
    },

    fetchData() {
      this.$axios
        .$get('/api/core/userInfo/auth/getUserInfoForCharts')
        .then((response) => {
          console.log(response)

          // 基于准备好的dom，初始化echarts实例
          let myChart = this.$echarts.init(document.getElementById('myChart'))
          // 绘制图表
          myChart.setOption({
            xAxis: {
              type: 'category',
              data: response.data.result.time,
              name: '日期', // x轴名称
              // x轴名称样式
              nameTextStyle: {
                fontWeight: 600,
                fontSize: 10,
              },
            },
            yAxis: {
              type: 'value',
              name: '完成垃圾回收次数', // y轴名称
              // y轴名称样式
              nameTextStyle: {
                fontWeight: 600,
                fontSize: 10,
              },
            },
            series: [
              {
                //根据名字对应到相应的系列

                data: response.data.result.count,
                type: 'line',
                smooth: true,
                label: {
                  show: true,
                  position: 'bottom',
                  textStyle: {
                    fontSize: 10,
                  },
                },
              },
            ],
            tooltip: {
              trigger: 'axis', // axis   item   none三个值
            },
          })
        })
    },
  },
}
</script>
