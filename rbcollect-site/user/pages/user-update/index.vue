<template>
  <!--列表-->
  <div style="margin=30px">
    <el-container>
      <el-aside width="450px"></el-aside>
      <el-main style="background-color:white;margin-top:20px">
        <ul>
          <li style="margin-bottom:15px">
            <span>头像： </span>
            <img
              :src="userIndexVO.headImg"
              style="width:88px;height:88px;z-index:0;margin-left:30px"
            />
            <!-- <el-upload
              :on-success="onUploadSuccessImg"
              :multiple="false"
              :action="uploadUrl"
              :data="{ module: 'head_img' }"
              :limit="1"
              list-type="picture-card"
            >
              <i class="el-icon-plus"></i>
            </el-upload> -->
          </li>
          <li style="margin-bottom:15px">
            <span>用户昵称： </span>
            <el-input v-model="userIndexVO.name" style="width:400px"></el-input>
          </li>
          <li style="margin-bottom:15px">
            <span>手机号码： </span>
            <el-input
              v-model="userIndexVO.phone"
              style="width:400px"
            ></el-input>
          </li>
          <li style="margin-bottom:15px">
            <span>电子邮箱： </span>
            <el-input
              v-model="userIndexVO.email"
              style="width:400px"
            ></el-input>
          </li>
          <li style="margin-bottom:15px">
            <span>密码： </span>
            <el-input
              placeholder="请输入密码"
              v-model="pwd"
              show-password
              style="width:400px;margin-left:29px"
            ></el-input>
          </li>
          <li>
            <span>再次输入： </span>
            <el-input
              v-if="!flag"
              placeholder="请输入密码"
              v-model="pwdNext"
              show-password
              style="width:400px"
              @blur="checkPwd()"
              suffix-icon="el-icon-circle-close"
            ></el-input>
            <el-input
              v-else
              placeholder="请输入密码"
              v-model="pwdNext"
              show-password
              style="width:400px"
              @blur="checkPwd()"
            ></el-input>
          </li>
          <li>
            <span style="margin-left:80px;color:red" v-show="!flag"
              >两次输入密码不一致</span
            >
          </li>
          <li style="margin-bottom:15px"></li>
        </ul>
        <ul>
          <li style="margin-left:180px">
            <el-button
              type="primary"
              icon="el-icon-message-solid"
              @click="update()"
              :disabled="saveBtnDisabled"
            >
              修改
            </el-button>

            <el-button type="primary" @click="resetData()">重置</el-button>
          </li>
        </ul>
      </el-main>
      <el-aside width="400px"></el-aside>
    </el-container>
  </div>
</template>

<script>
import '~/assets/css/index.css'
import '~/assets/css/detail.css'
import cookie from 'js-cookie'

export default {
  data() {
    let BASE_API = process.env.BASE_API
    return {
      userIndexVO: {},
      saveBtnDisabled: false,
      pwd: '',
      pwdNext: '',
      flag: true,
      uploadUrl: BASE_API + '/api/oss/file/upload', //文件上传地址
    }
  },
  created() {
    this.fetchUserData()
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
    update() {
      this.$axios
        .$post('/api/core/userInfo/update?pwd=' + this.pwd, this.userIndexVO)
        .then((response) => {
          if (response.data.result == true) {
            this.$message.success(response.message)
            this.saveBtnDisabled = false
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
                  this.$router.push({ path: '/user' })
                }
              }, 1700)
            }
          } else {
            this.$message.error(response.message + '，请重新输入')
          }
        })
    },
    resetData() {
      setTimeout('location.reload () ', 0)
    },
    checkPwd() {
      if (this.pwd == this.pwdNext) {
        this.flag = true
        this.saveBtnDisabled = false
      } else {
        this.flag = false
        this.saveBtnDisabled = true
      }
    },
  },
}
</script>
