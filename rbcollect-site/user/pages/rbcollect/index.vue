<template>
  <!--列表-->
  <div style="margin=30px">
    <el-container>
      <el-aside width="450px"></el-aside>
      <el-main style="background-color:white;margin-top:20px">
        <ul>
          <li style="margin-bottom:15px">
            <span>垃圾重量： </span>
            <el-input
              v-model="rbDealItem.rbWeight"
              placeholder="单位 /500g"
              style="width:400px"
            ></el-input>
          </li>
          <li style="margin-bottom:15px">
            <span>上门时间： </span>
            <el-date-picker
              v-model="rbDealItem.serviceTime"
              type="datetime"
              placeholder="选择日期时间"
              :picker-options="pickerOptions"
              style="width:400px"
            >
            </el-date-picker>
          </li>
          <li style="margin-bottom:15px">
            <span>垃圾类型： </span>
            <el-select
              v-model="rbDealItem.rbType"
              placeholder="请选择"
              @change="fetchRbNameList()"
              style="width:400px"
            >
              <el-option
                v-for="item in RbTypeList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              >
              </el-option>
            </el-select>
            <el-button type="text" @click="searchRbType()"
              >不知道？点我查询</el-button
            >
          </li>
          <li style="margin-bottom:15px">
            <span>垃圾名称： </span>
            <el-select
              v-model="rbDealItem.rbName"
              placeholder="请选择"
              style="width:400px"
            >
              <el-option
                v-for="item in RbNameList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              >
              </el-option>
            </el-select>
          </li>
          <li style="margin-bottom:15px">
            <span>我的地址： </span>
            <el-select
              v-model="rbDealItem.userLocalId"
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
        </ul>
        <ul>
          <li style="margin-left:180px">
            <el-button
              type="primary"
              icon="el-icon-message-solid"
              @click="saveRbDealItem()"
              :disabled="saveBtnDisabled"
            >
              预约
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
    return {
      saveBtnDisabled: false, //是否禁用保存按钮
      rbDealItem: {},
      pickerOptions: {
        //设置时间选择限制
        disabledDate(time) {
          return time.getTime() <= Date.now()
        },
      },
      RbTypeList: {},
      RbNameList: {},
      UserLocalList: {},
    }
  },
  created() {
    this.fetchRbTypeList()
    this.fetchLocalList()
  },
  methods: {
    fetchRbTypeList() {
      //从字典查询垃圾类型
      this.$axios
        .$get('/api/core/dict/findByDictCode/ROOT')
        .then((response) => {
          this.RbTypeList = response.data.dictList
        })
    },
    fetchRbNameList() {
      //通过垃圾类型查询垃圾名称
      console.log(this.rbDealItem.serviceTime + '=============')
      this.$axios
        .$get(
          '/api/core/dict/listByParentId?parentId=' + this.rbDealItem.rbType
        )
        .then((response) => {
          this.RbNameList = response.data.list
        })
    },
    searchRbType() {
      window.location.href = '/rb'
    },
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
    resetData() {
      //还原表单
      this.rbDealItem = {}
    },
    saveRbDealItem() {
      //记得后端要进行用户身份校验
      this.$axios
        .$post('/api/core/rbDealItem/auth/save', this.rbDealItem)
        .then((response) => {
          if (response.message != '垃圾回收申请成功') {
            this.$message.error(response.message)
          } else {
            this.$message.success(response.message)
          }
        })
      this.resetData()
    },
  },
}
</script>
