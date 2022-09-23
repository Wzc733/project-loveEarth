<template>
  <header>
    <div class="header-top min-width">
      <div class="container fn-clear">
        <strong class="fn-left">
          咨询热线：400-000-0000
          <span class="s-time">服务时间：9:00 - 18:00</span>
        </strong>
        <ul class="header_contact">
          <li class="c_1">
            <a href="#" target="_blank" title="官方微信" alt="官方微信">
              <b class="ico_head_weixin"></b>
            </a>
          </li>
          <li class="c_2">
            <a href="#" target="_blank" title="官方QQ" alt="官方QQ">
              <b class="ico_head_QQ"></b>
            </a>
          </li>
          <li class="c_4">
            <a href="#" target="_blank" title="新浪微博" alt="新浪微博">
              <b class="ico_head_sina"></b>
            </a>
          </li>
        </ul>

        <!-- 用户未登录 -->
        <ul v-if="!userInfo" class="fn-right header-top-ul">
          <!-- <li><a href="" :class="'c-orange'">测试</a></li> -->
          <li>
            <NuxtLink to="/" :class="{ 'c-orange': $route.fullPath === '/' }"
              >返回首页</NuxtLink
            >
          </li>
          <li>
            <div class="">
              <NuxtLink
                to="/register"
                :class="{ 'c-orange': $route.fullPath === '/register' }"
              >
                免费注册
              </NuxtLink>
            </div>
          </li>
          <li>
            <div class="">
              <NuxtLink
                to="/login"
                :class="{ 'c-orange': $route.fullPath === '/login' }"
              >
                登录
              </NuxtLink>
            </div>
          </li>
        </ul>

        <!-- 用户已登录 -->
        <ul v-if="userInfo" class="fn-right header-top-ul">
          <li><NuxtLink to="/" class="app">返回首页</NuxtLink></li>
          <li>
            <div class="">
              <NuxtLink to="/user" class="user" title="我的账户">
                <i class="el-icon-user-solid">{{ userInfo.name }}</i>
              </NuxtLink>
            </div>
          </li>
          <li>
            <div class="">
              <a
                href="javascript:void(0)"
                class="js-login"
                @click="logout()"
                title="退出"
              >
                退出
              </a>
            </div>
          </li>
        </ul>
      </div>
    </div>
    <div class="header min-width">
      <div class="container">
        <ul class="top-nav fn-clear">
          <li>
            <NuxtLink to="/rb"> 垃圾识别 </NuxtLink>
          </li>
          <li>
            <NuxtLink to="/news">新闻知识</NuxtLink>
          </li>
          <li>
            <NuxtLink to="/rbcollect">上门回收</NuxtLink>
          </li>
          <li>
            <NuxtLink to="/rbdeal">回收订单</NuxtLink>
          </li>
          <li>
            <NuxtLink to="/goods">环保商城</NuxtLink>
          </li>
          <li>
            <NuxtLink to="/goodsdeal">我的订单</NuxtLink>
          </li>
          <li>
            <NuxtLink to="/user">个人中心</NuxtLink>
          </li>
          <li>
            <NuxtLink to="/feedback">反馈中心</NuxtLink>
          </li>
        </ul>
      </div>
    </div>
  </header>
</template>
<script>
import '~/assets/font/iconfont.css'
import cookie from 'js-cookie'

export default {
  data() {
    return {
      userInfo: null,
    }
  },

  mounted() {
    //浏览器初始化完成后才能有cookie,所以不能用create()
    this.showInfo()
  },

  methods: {
    //显示用户信息
    showInfo() {
      //判断cookie中是否有用户信息
      let userInfo = cookie.get('userInfo')
      console.log(userInfo)
      if (!userInfo) {
        //如果该用户过期或者其它原因不满足登录条件，刷新页面时记得把Vue对象中的userInfo给清空
        this.userInfo = null
        return
      }
      //放进cookie的数据是字符串形式的，要使用的话还得转成JSON形式(拦截器做了,但由于axios是异步请求,先执行userInfo 赋值时要转JSON数据类型)
      userInfo = JSON.parse(userInfo)
      //校验token是否合法  因为以后用户每次发起任何业务请求 都要校验token,所以直接用拦截器校验方便
      this.$axios({
        url: '/api/core/userInfo/checkToken',
        method: 'get',
        // headers: { (拦截器做了)
        //   token: userInfo.token,
        // },
      }).then((response) => {
        console.log('校验token成功')
        this.userInfo = userInfo
      })
    },

    //退出
    logout() {
      //要删除，不然拦截器获取userInfo出问题的
      cookie.remove('userInfo')
      window.location.href = '/login'
    },
  },
}
</script>
