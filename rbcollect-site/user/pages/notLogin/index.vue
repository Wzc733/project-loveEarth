<template>
  <el-container>
    <el-aside width="200px"></el-aside>
    <el-main style="margin-left:450px">
      <el-col :sm="12" :lg="6">
        <el-result icon="warning" title="信息提示" subTitle="请先登录">
          <template slot="extra">
            <el-button type="primary" size="medium" @click="backout()"
              >点击登陆</el-button
            >
          </template>
        </el-result>
      </el-col>
    </el-main>

    <el-aside width="200px"> </el-aside>
  </el-container>
</template>
<script>
import cookie from 'js-cookie'
export default {

  methods: {
    
    backout() {
      window.location.href = '/login'
    },
  
  },
}
</script>
<style>