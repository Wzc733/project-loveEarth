<template>
  <!--注册-->
  <div class="wrap">
    <div v-if="step === 1" class="tdbModule register">
      <div class="registerTitle">注册账户</div>
      <div class="registerLc1">
        <p class="p1">填写账户信息</p>
        <p class="p2">注册成功</p>
      </div>

      <div class="registerCont">
        <ul>
          <li class="telNumber">
            <span class="dis">手机号码</span>
            <input class="input" v-model="userInfo.mobile" />
            <el-button type="success" plain v-if="!sending" @click="send()"
              >获取验证码</el-button
            >
            <el-button type="info" plain v-else disabled>
              {{ leftSecond }}秒后重发</el-button
            >
          </li>

          <li>
            <span class="dis">短信验证码</span>
            <input class="input" v-model="userInfo.code" />
            <span class="info">
              请输入验证码
            </span>
          </li>

          <li>
            <span class="dis">密码</span>
            <input type="password" v-model="userInfo.password" class="input" />
            <span class="info">
              6-24个字符，英文、数字组成，区分大小写
            </span>
          </li>

          <li>
            <span class="dis">邮箱</span>
            <input type="email" v-model="userInfo.email" class="input" />
            <span class="info">
              邮箱账号 + @标识符 + 邮箱域名
            </span>
          </li>

          <li>
            <span class="dis">用户名</span>
            <input
              type="text"
              v-model="userInfo.name"
              class="input"
              maxlength="15"
            />
          </li>

          <li class="agree">
            <input type="checkbox" checked />
            我同意《<NuxtLink to="#" target="_black"
              >爱地球回收站注册协议</NuxtLink
            >》
            <span>请查看协议</span>
          </li>
          <li class="btn">
            <button @click="register()">
              下一步
            </button>
          </li>
        </ul>
      </div>
    </div>

    <div v-if="step === 2" class="tdbModule register">
      <div class="registerTitle">注册账户</div>
      <div class="registerLc2">
        <p class="p1">填写账户信息</p>
        <p class="p2">注册成功</p>
      </div>
      <div class="registerCont">
        <ul>
          <li class="scses">
            {{ this.userInfo.name }} 恭喜您注册成功！
            <NuxtLink class="blue" to="/login">
              请登录
            </NuxtLink>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
import '~/assets/css/register.css'
export default {
  data() {
    return {
      step: 1, //注册步骤
      userInfo: {},
      sending: false, // 是否发送验证码
      second: 10, // 倒计时间
      leftSecond: 0, //剩余时间
    }
  },

  methods: {
    //发短信
    send() {
      if (!this.userInfo.mobile) {
        this.$message.error('请输入手机号码')
        return
      }
      //防止重复提交。提交后隐藏，换成倒计时显示
      if (this.sending) return
      this.sending = true

      this.timeDown()

      this.$axios
        .$get('/api/sms/send/' + this.userInfo.mobile)
        .then((response) => {
          this.$message.success(response.message)
        })
    },

    //倒计时
    timeDown() {
      console.log('倒计时')
      this.leftSecond = this.second

      //实现倒计时,使用js原生函数
      const timmer = setInterval(() => {
        this.leftSecond--
        if (this.leftSecond <= 0) {
          //停止计时器
          clearInterval(timmer)
          //还原发送验证码
          this.sending = false
        }
      }, 1000)
    },

    //注册
    register() {
      this.$axios
        .$post('/api/core/userInfo/register', this.userInfo)
        .then((response) => {
          this.$message.success(response.message)
          this.step = 2
        })
    },
  },
}
</script>
