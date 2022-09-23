<template>
  <el-container>
    <el-aside width="100px"></el-aside>
    <el-main class="mainContain" v-if="this.active == 1">
      <el-steps :active="this.active" align-center style="margin-top:30px">
        <el-step title="步骤1" description="选择问题反馈类型"></el-step>
        <el-step title="步骤2" description="问题详细说明"></el-step>
        <el-step title="步骤3" description="提交说明"></el-step>
      </el-steps>
      <ul>
        <li>
          <span
            style="font-weight:100;margin-left:150px;position:absolute;top:300px"
            >提示:为了您的问题朋等及时被相关人员解决,请认真选择问题类型反馈</span
          >
        </li>
        <li>
          <el-tag
            type="success"
            disable-transitions
            style="height:40px;width:300px ;font-size:20px;margin-top:100px;margin-left:500px;text-align: center; "
            >反馈类型</el-tag
          >
        </li>
        <li style="margin-top:20px;margin-left:450px">
          <template>
            <el-radio v-model="feedBackItem.feedbackType" label="201"
              >积分充值</el-radio
            >
            <el-radio v-model="feedBackItem.feedbackType" label="202"
              >商品交易</el-radio
            >
            <el-radio v-model="feedBackItem.feedbackType" label="203"
              >垃圾回收</el-radio
            >
            <el-radio v-model="feedBackItem.feedbackType" label="204"
              >其它</el-radio
            >
          </template>
        </li>
        <li style="margin-left:600px;margin-top:60px">
          <el-button type="success" plain @click="nextStep1()"
            >下一步</el-button
          >
        </li>
      </ul>
    </el-main>
    <el-main class="mainContain" v-if="this.active == 2">
      <el-steps :active="this.active" align-center style="margin-top:30px">
        <el-step title="步骤1" description="选择问题反馈类型"></el-step>
        <el-step title="步骤2" description="问题详细说明"></el-step>
        <el-step title="步骤3" description="提交说明"></el-step>
      </el-steps>
      <ul>
        <li>
          <span
            style="font-weight:100;margin-left:150px;position:absolute;top:300px"
            >提示:为了您的问题朋等及时被相关人员解决,请认真填写</span
          >
        </li>
        <li>
          <el-input
            type="textarea"
            :rows="3"
            placeholder="请输入内容"
            v-model="feedBackItem.feedbackContent"
            style="margin-top:100px;width:800px;margin-left:150px"
          >
          </el-input>
        </li>
        <li style="margin-left:600px;margin-top:60px">
          <el-button type="success" plain @click="nextStep2()"
            >下一步</el-button
          >
        </li>
      </ul>
    </el-main>
    <el-main class="mainContain" v-if="this.active == 3">
      <el-steps :active="this.active" align-center style="margin-top:30px">
        <el-step title="步骤1" description="选择问题反馈类型"></el-step>
        <el-step title="步骤2" description="问题详细说明"></el-step>
        <el-step title="步骤3" description="提交说明"></el-step>
      </el-steps>
      <ul>
        <li>
          <span
            style="font-weight:100;margin-left:150px;position:absolute;top:300px"
            >提示:为了后续问题的解决，请准确填写您的手机号码</span
          >
        </li>
        <li style="margin-left:160px">
          <el-input
            placeholder="请输入联系人姓名"
            prefix-icon="el-icon-search"
            v-model="feedBackItem.linkmanName"
            style="margin-top:80px;width:400px"
          >
          </el-input>
        </li>
        <li style="margin-left:160px">
          <el-input
            placeholder="请输入联系号码"
            prefix-icon="el-icon-search"
            v-model="feedBackItem.linkmanMobile"
            style="margin-top:20px;width:400px"
          >
          </el-input>
        </li>
        <li style="margin-left:600px;margin-top:60px">
          <el-button type="success" plain @click="submit()">提交</el-button>
        </li>
      </ul>
    </el-main>
    <el-aside width="100px"></el-aside>
  </el-container>
</template>

<script>
import cookie from 'js-cookie'
export default {
  data() {
    return {
      feedBackItem: {},
      active: 1,
    }
  },
  created() {
    this.decideLogin()
  },
  methods: {
    decideLogin() {
      //判断用户有无登陆
      let userInfo = cookie.get('userInfo')
      if (userInfo == 0) {
        //好东西
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
          }, 1500)
        }
      }
    },

    nextStep1() {
      this.active = 2
    },
    nextStep2() {
      this.active = 3
    },
    submit() {
      this.$axios
        .$post('/api/core/userFeedback/auth/save', this.feedBackItem)
        .then((response) => {
          this.$message.success(response.message)
          setTimeout('location.reload () ', 2000) //好东西！
        })
    },
  },
}
</script>

<style>
.mainContain {
  background: white;
  height: 500px;
  margin-bottom: 50px;
}
</style>
