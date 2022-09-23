<template>
  <el-container>
    <el-aside style="width:300px"></el-aside>
    <el-main>
      <div class="personal-pay">
        <h3><i>设置用户地址</i></h3>
        <div class="pay-notice">
          <p>
            请设置收获/上门回收地址以便于您账号正常使用
          </p>
        </div>
        <div class="pay-form">
          <ul v-if="type == 1">
            <li>
              <label>姓名</label>
              <input
                v-model="userLocal.userName"
                type="text"
                class="pay-txt"
                maxlength="16"
                placeholder="您的真实姓名"
              />
            </li>
            <li>
              <label>地址</label>
              <input
                v-model="userLocal.userLocal"
                type="text"
                class="pay-txt"
                maxlength="18"
                placeholder="您的住址"
              />
            </li>
            <li>
              <label>联系号码</label>
              <input
                v-model="userLocal.userMobile"
                type="text"
                class="pay-txt"
                placeholder="输入您的手机号码"
              />
            </li>

            <li>
              <label>&nbsp;</label>
              <input v-model="agree" type="checkbox" />
              我已阅读并同意
              <a href="#" class="c-orange" target="_blank">
                《爱地球回收站用户协议》
              </a>
            </li>
            <li>
              <label>&nbsp;</label>
              <el-button
                :disabled="!agree"
                @click="commitBind()"
                type="primary"
                style="position:relative;top:-20px;left:200px"
              >
                完成
              </el-button>
            </li>
            <li>
              <el-table :data="userLocalList" border style="width: 870px">
                <el-table-column prop="userName" label="联系人姓名">
                </el-table-column>
                <el-table-column prop="userLocal" label="联系人地址">
                </el-table-column>
                <el-table-column prop="userMobile" label="联系人号码">
                </el-table-column>
                <el-table-column label="操作">
                  <template slot-scope="scope">
                    <el-button
                      @click="deleteLocal(scope.row.id)"
                      type="text"
                      size="small"
                      >删除</el-button
                    >
                    <el-button
                      type="text"
                      size="small"
                      @click="updateLocal(scope.row.id)"
                      >编辑</el-button
                    >
                  </template>
                </el-table-column>
              </el-table>
            </li>
          </ul>
          <ul v-else>
            <li>
              <label>姓名</label>
              <input
                v-model="userLocalCompile.userName"
                type="text"
                class="pay-txt"
                maxlength="16"
                placeholder="您的真实姓名"
              />
            </li>
            <li>
              <label>地址</label>
              <input
                v-model="userLocalCompile.userLocal"
                type="text"
                class="pay-txt"
                maxlength="18"
                placeholder="您的住址"
              />
            </li>
            <li>
              <label>联系号码</label>
              <input
                v-model="userLocalCompile.userMobile"
                type="text"
                class="pay-txt"
                placeholder="输入您的手机号码"
              />
            </li>

            <li>
              <label>&nbsp;</label>
              <input v-model="agree" type="checkbox" />
              我已阅读并同意
              <a href="#" class="c-orange" target="_blank">
                《爱地球回收站用户协议》
              </a>
            </li>
            <li>
              <label>&nbsp;</label>
              <el-button :disabled="!agree" @click="update()" type="primary">
                完成
              </el-button>
            </li>
          </ul>
        </div>
      </div>
    </el-main>
    <el-aside style="width:200px"></el-aside>
  </el-container>
</template>
<script>
import cookie from 'js-cookie'
export default {
  data() {
    return {
      agree: false,
      userLocal: {},
      userLocalList: [],
      type: 1,
      userLocalCompile: {},
    }
  },

  created() {
    this.fetchData()
    this.submit()
    this.type = 1
  },

  methods: {
    fetchData() {
      //判断用户有无登陆
      let userInfo = cookie.get('userInfo')
      if (userInfo != 0) {
        this.agree = false
        this.$axios
          .$get('/api/core/userLocal/auth/getUserInfoConnectLocal')
          .then((response) => {
            this.userLocal = response.data.userLocal
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

    commitBind() {
      this.$alert(
        '<div style="size: 18px;color: red;">确认信息填写无误</div>',
        '提示',
        {
          dangerouslyUseHTMLString: true, //解析alert内容成html格式
          confirmButtonText: '确认',
          callback: (action) => {
            if (action === 'confirm') {
              this.$axios
                .$post('/api/core/userLocal/save', this.userLocal)
                .then((response) => {
                  this.$message.success(response.message)
                })
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
            }
          },
        }
      )
    },
    submit() {
      this.$axios
        .$get('/api/core/userLocal/auth/getUserLocal')
        .then((response) => {
          this.userLocalList = response.data.userLocalList
        })
    },
    deleteLocal(id) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          this.$axios
            .$get('/api/core/userLocal/deleteUserLocal?id=' + id)
            .then((response) => {
              this.$message.success(response.message)
            })
          setTimeout('location.reload () ', 2000)
        })
        .catch((error) => {
          if (error === 'cancle') {
            //点击取消时error的值是cancle 防止因其它出错拿这个处理
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          }
        })
    },
    updateLocal(id) {
      this.type = 2
      this.$axios
        .$get('/api/core/userLocal/auth/getUserLocal?id=' + id)
        .then((response) => {
          this.userLocalCompile = response.data.userLocal
        })
    },
    update() {
      this.$axios
        .$post(
          '/api/core/userLocal/auth/updateUserLocal',
          this.userLocalCompile
        )
        .then((response) => {
          this.$message.success(response.message)
        })
      setTimeout('location.reload () ', 1600)
    },
  },
}
</script>
