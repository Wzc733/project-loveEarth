<template>
  <!--个人中心-->
  <div class="wrapper wbgcolor">
    <div class="w1200 personal">
      <div class="personal-left" style="height:50px">
        <el-tag type="success" style="font-size:20px;margin-left:45px"
          >状态:{{ this.userStatus }}</el-tag
        >
      </div>
      <NuxtChild />
      <div class="clear"></div>
    </div>
  </div>
</template>

<script>
//user页面的左layout需要判断有userInfo才能显示
//<NuxtChild />包含user目录下的vue文件
//直接访问user除了访问user.vue还会访问到user目录下的index.vue
import '~/assets/css/user.css'
import InvestorNav from '~/components/InvestorNav'
import BorrowerNav from '~/components/BorrowerNav'
import cookie from 'js-cookie'

export default {
  components: {
    InvestorNav,
    BorrowerNav,
  },

  data() {
    return {
      userStatus: null,
    }
  },

  mounted() {
    let userInfo = cookie.get('userInfo')
    if (userInfo) {
      userInfo = JSON.parse(userInfo)
    }
    this.submit()
  },
  methods: {
    submit() {
      this.$axios.$get('/api/core/userInfo/getStatusUser').then((response) => {
        this.userStatus = response.data.statusUser
      })
    },
  },
}
</script>
