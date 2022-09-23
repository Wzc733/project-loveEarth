<template>
  <div>
    <el-container>
      <el-main>
        <div class="app-container">
          <el-menu
            :default-active="activeIndex"
            class="el-menu-demo"
            mode="horizontal"
            @select="handleSelect"
          >
            <el-menu-item index="1">部分订单</el-menu-item>
            <el-menu-item index="2" @click="fetchAllData()"
              >全部订单</el-menu-item
            >
          </el-menu>

          <!-- 列表 -->
          <el-table
            :data="rbDealListFiveItem"
            style="width: 100%"
            :row-class-name="tableRowClassName"
            border
            v-if="this.changeDealSign == 1"
          >
            <el-table-column prop="dealItemNo" label="交易编号" />
            <el-table-column prop="rbUserName" label="申请垃圾回收用户名" />
            <el-table-column prop="rbType" label="垃圾类型" />
            <el-table-column prop="rbName" label="垃圾名称" />
            <el-table-column
              prop="rbWeight"
              label="垃圾重量(单位:斤)"
              width="100"
            />
            <el-table-column prop="deliveryUserLocal" label="上门地址" />
            <el-table-column prop="deliveryUserName" label="联系人名字" />
            <el-table-column prop="deliveryUserMobile" label="联系人号码" />
            <el-table-column prop="expectIntegral" label="期待收益积分" />
            <el-table-column prop="serviceTime" label="上门时间" />
            <el-table-column prop="status" label="状态" />
            <el-table-column prop="createTime" label="订单创建时间" />

            <el-table-column label="操作" align="center" width="200">
              <template slot-scope="scope">
                <el-button
                  type="danger"
                  @click="deleteRbDeal(scope.row.id)"
                  v-if="scope.row.status == noDone()"
                >
                  取消申请1</el-button
                >
                <el-button
                  type="danger"
                  @click="tryDeleteRbDeal(scope.row.id)"
                  v-if="scope.row.status == Doing()"
                >
                  取消申请2</el-button
                >
              </template>
            </el-table-column>
          </el-table>
          <!--  -->
          <!-- 列表 -->
          <el-table
            :data="rbDealList"
            style="width: 100%"
            :row-class-name="tableRowClassName"
            border
            v-else
          >
            <el-table-column prop="dealItemNo" label="交易编号" />
            <el-table-column prop="rbUserName" label="申请垃圾回收用户名" />
            <el-table-column prop="rbType" label="垃圾类型" />
            <el-table-column prop="rbName" label="垃圾名称" />
            <el-table-column
              prop="rbWeight"
              label="垃圾重量(单位:斤)"
              width="100"
            />
            <el-table-column prop="deliveryUserLocal" label="上门地址" />
            <el-table-column prop="deliveryUserName" label="联系人名字" />
            <el-table-column prop="deliveryUserMobile" label="联系人号码" />
            <el-table-column prop="expectIntegral" label="期待收益积分" />
            <el-table-column prop="serviceTime" label="上门时间" />
            <el-table-column prop="status" label="状态" />
            <el-table-column prop="createTime" label="订单创建时间" />

            <el-table-column label="操作" align="center" width="200">
              <template slot-scope="scope">
                <el-button
                  type="danger"
                  @click="deleteRbDeal(scope.row.id)"
                  v-if="scope.row.status == noDone()"
                >
                  取消申请1</el-button
                >
                <el-button
                  type="danger"
                  @click="tryDeleteRbDeal(scope.row.id)"
                  v-if="scope.row.status == Doing()"
                >
                  取消申请2</el-button
                >
              </template>
            </el-table-column>
          </el-table>
        </div>
        <!-- 分页组件 -->
        <el-pagination
          v-if="this.changeDealSign != 1"
          :current-page="page"
          :total="total"
          :page-size="limit"
          :page-sizes="[6, 10, 14]"
          style="padding: 30px 0"
          layout="total, sizes, prev, pager, next,->, jumper"
          @size-change="changePageSize"
          @current-change="changeCurrentPage"
        />
      </el-main>
    </el-container>
  </div>
</template>

<script>
import cookie from 'js-cookie'
export default {
  data() {
    return {
      rbDealListFiveItem: [],
      rbDealList: [],
      dialogTableVisible: false, //对话框是否显示
      activeIndex: '1',
      changeDealSign: '',
      total: 0, // 数据库中的总记录数
      page: 1, // 默认页码
      limit: 6, // 每页记录数
    }
  },
  created() {
    this.fetchData()
    this.changeDealSign = 1
  },
  methods: {
    noDone() {
      return '未完成'
    },
    // Done() {
    //   return '已完成'
    // },
    Doing() {
      return '已联系人员上门回收'
    },
    changePageSize(size) {
      //有一个回调参数:每页条数,element-ui帮我们组装好了,只需要接收就行
      this.limit = size
      this.fetchAllData()
    },
    changeCurrentPage(page) {
      this.page = page
      this.fetchAllData()
    },
    tableRowClassName({ row, rowIndex }) {
      if (row.status === '未完成') {
        return 'warning-row'
      } else {
        return 'success-row'
      }
    },

    fetchData() {
      //判断用户有无登陆
      let userInfo = cookie.get('userInfo')
      if (userInfo) {
        this.$axios
          .$get('/api/core/rbDealItem/auth/listFiveItem')
          .then((response) => {
            this.rbDealListFiveItem = response.data.rbDealItemFiveItem
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
    deleteRbDeal(id) {
      this.$confirm('此操作将取消垃圾回收, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          this.$axios
            .$get('/api/core/rbDealItem/auth/deleteRbDeal?id=' + id)
            .then((response) => {
              this.$message.success(response.message)
              this.fetchData()
            })
        })
        .catch((error) => {
          this.$message({
            type: 'info',
            message: '已取消删除',
          })
        })
    },
    tryDeleteRbDeal(id) {
      this.$axios
        .$get('/api/core/rbDealItem/auth/tryDeleteRbDeal?id=' + id)
        .then((response) => {
          this.$message.success(response.message)
        })
      setTimeout('location.reload () ', 1700)
    },
    handleSelect(key) {
      //切换导航栏
      this.changeDealSign = key
    },
    fetchAllData() {
      this.$axios
        .$get('/api/core/rbDealItem/auth/list/' + this.page + '/' + this.limit)
        .then((response) => {
          this.rbDealList = response.data.pageModel.records
          this.total = response.data.pageModel.total
        })
    },
  },
}
</script>

<style>
.el-table .warning-row {
  background: oldlace;
}

.el-table .success-row {
  background: #f0f9eb;
}
</style>
