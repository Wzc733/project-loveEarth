<template>
  <!--登录-->
  <div class="wrap">
    <div class="tdbModule loginPage">
      <div class="registerTitle">用户登录</div>
      <div class="registerCont">
        <ul>
          <li>
            <span class="dis">手机号：</span>
            <input class="input" v-model="userInfo.mobile" />
          </li>

          <li>
            <span class="dis">密码：</span>
            <input class="input" v-model="userInfo.password" type="password" />
          </li>
          <li class="btn">
            <button @click="login()" :class="{ disabled: !isValid }">
              立即登录
            </button>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
import '~/assets/css/register.css'
import cookie from 'js-cookie'

export default {
  data() {
    return {
      userInfo: {},
      isValid: true, //表单校验是否成功
    }
  },

  methods: {
    //登录
    login() {
      if (this.userInfo.mobile == null) {
        this.$message.error('账户不能为空')
        return
      }
      if (this.userInfo.password == null) {
        this.$message.error('密码不能为空')
        return
      }
      this.$axios
        .$post('/api/core/userInfo/login', this.userInfo)
        .then((response) => {
          {
            if (response.data.userInfo != null) {
              //引入js-cookie储存token等其它用户信息
              cookie.set('userInfo', response.data.userInfo)
              console.log(response.data.userInfo + '11111111111')
              window.location.href = '/user'
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
                }, 1500)
                this.$message.success(response.message)
              }
            } else {
              this.$message.error(response.message)
              setTimeout('location.reload () ', 2000)
            }
          }
        })
    },
  },
}
</script>
