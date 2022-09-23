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
            :data="goodsDealListFiveItem"
            style="width: 100%"
            :row-class-name="tableRowClassName"
            border
            v-if="this.changeDealSign == 1"
          >
            <el-table-column prop="dealItemNo" label="交易编号" />
            <el-table-column prop="payUserId" label="购买商品用户id" />
            <el-table-column prop="payUserName" label="购买商品用户名称" />
            <el-table-column prop="goodsId" label="商品id" />
            <el-table-column prop="goodsName" label="商品名称" width="100" />
            <el-table-column prop="deliveryUserLocal" label="上门地址" />
            <el-table-column prop="deliveryUserName" label="联系人名字" />
            <el-table-column prop="deliveryUserMobile" label="联系人号码" />
            <el-table-column prop="status" label="状态" />
            <el-table-column prop="nums" label="购买数量" />
            <el-table-column prop="createTime" label="订单创建时间" />

            <el-table-column label="操作" align="center" width="200">
              <template slot-scope="scope">
                <el-button
                  type="danger"
                  @click="deleteGoodsDeal(scope.row.id)"
                  v-if="scope.row.status == noDone()"
                >
                  取消购买1</el-button
                >
                <el-button
                  type="danger"
                  @click="tryDeleteGoodsDeal(scope.row.id)"
                  v-if="scope.row.status == Doing()"
                >
                  取消购买2</el-button
                >
                <el-button
                  type="success"
                  @click="
                    evaluateGoodsDeal(scope.row.dealItemNo, scope.row.goodsId)
                  "
                  v-if="scope.row.status == Done()"
                  :disabled="scope.row.evaluateFull"
                >
                  商品评价</el-button
                >
              </template>
            </el-table-column>
          </el-table>
          <!--  -->
          <!-- 列表 -->
          <el-table
            :data="goodsDealList"
            style="width: 100%"
            :row-class-name="tableRowClassName"
            border
            v-else
          >
            <el-table-column prop="dealItemNo" label="交易编号" />
            <el-table-column prop="payUserId" label="购买商品用户id" />
            <el-table-column prop="payUserName" label="购买商品用户名称" />
            <el-table-column prop="goodsId" label="商品id" />
            <el-table-column prop="goodsName" label="商品名称" width="100" />
            <el-table-column prop="deliveryUserLocal" label="上门地址" />
            <el-table-column prop="deliveryUserName" label="联系人名字" />
            <el-table-column prop="deliveryUserMobile" label="联系人号码" />
            <el-table-column prop="status" label="状态" />
            <el-table-column prop="nums" label="购买数量" />
            <el-table-column prop="createTime" label="订单创建时间" />

            <el-table-column label="操作" align="center" width="200">
              <template slot-scope="scope">
                <el-button
                  type="danger"
                  @click="deleteGoodsDeal(scope.row.id)"
                  v-if="scope.row.status == noDone()"
                >
                  取消购买1</el-button
                >
                <el-button
                  type="danger"
                  @click="tryDeleteGoodsDeal(scope.row.id)"
                  v-if="scope.row.status == Doing()"
                >
                  取消购买2</el-button
                >
                <el-button
                  type="success"
                  @click="
                    evaluateGoodsDeal(scope.row.dealItemNo, scope.row.goodsId)
                  "
                  v-if="scope.row.status == Done()"
                  :disabled="scope.row.evaluateFull"
                >
                  商品评价</el-button
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
      <!-- 用户评价 -->
      <el-dialog title="用户评价" :visible.sync="dialogTableVisible">
        <ul>
          <li>
            <span>综合评分： </span>
            <div class="block" style="margin:10px">
              <el-rate v-model="evaluateGoods.commodityGrade"></el-rate>
            </div>
            <span>评价： </span>
            <el-input
              type="textarea"
              :rows="2"
              placeholder="请输入内容"
              v-model="evaluateGoods.evaluateContent"
              style="margin:10px;height:50px"
              size="medium"
            >
            </el-input>
          </li>
          <li>
            <el-button
              type="danger"
              round
              style="margin-left:20px;margin-top:10px"
              @click="submitEvaluation()"
              >提交评价</el-button
            >
          </li>
        </ul>
      </el-dialog>
    </el-container>
  </div>
</template>

<script>
import cookie from 'js-cookie'
export default {
  data() {
    return {
      goodsDealListFiveItem: [],
      goodsDealList: [],
      dialogTableVisible: false, //对话框是否显示
      activeIndex: '1',
      changeDealSign: '',
      total: 0, // 数据库中的总记录数
      page: 1, // 默认页码
      limit: 6, // 每页记录数
      dialogTableVisible: false,
      evaluateGoods: {},
      goodsDealItemNo: null,
      goodsDealItemId: null,
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
    Doing() {
      return '发货中'
    },
    Done() {
      return '已完成'
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
          .$get('/api/core/goodsDealItem/auth/listFiveItem')
          .then((response) => {
            this.goodsDealListFiveItem = response.data.goodsDealItemFiveItem
          })
      }
    },
    deleteGoodsDeal(id) {
      this.$confirm('此操作将取消商品购买, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          this.$axios
            .$get('/api/core/goodsDealItem/auth/deleteGoodsDeal?id=' + id)
            .then((response) => {
              this.$message.success(response.message)
              setTimeout('location.reload () ', 1700)
            })
        })
        .catch((error) => {
          this.$message({
            type: 'info',
            message: '已取消删除',
          })
        })
    },
    tryDeleteGoodsDeal(id) {
      this.$confirm('此操作将取消商品购买, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          this.$axios
            .$get('/api/core/goodsDealItem/auth/tryDeleteGoodsDeal?id=' + id)
            .then((response) => {
              this.$message.success(response.message)
              setTimeout('location.reload () ', 1700)
            })
        })
        .catch((error) => {
          this.$message({
            type: 'info',
            message: '已取消删除',
          })
        })
    },
    handleSelect(key) {
      //切换导航栏
      this.changeDealSign = key
    },
    fetchAllData() {
      this.$axios
        .$get(
          '/api/core/goodsDealItem/auth/list/' + this.page + '/' + this.limit
        )
        .then((response) => {
          this.goodsDealList = response.data.pageModel.records
          this.total = response.data.pageModel.total
        })
    },
    evaluateGoodsDeal(dealItemNo, id) {
      this.goodsDealItemNo = dealItemNo
      this.goodsDealItemId = id
      this.dialogTableVisible = true
    },
    submitEvaluation() {
      this.evaluateGoods.dealItemNo = this.goodsDealItemNo
      this.evaluateGoods.goodsId = this.goodsDealItemId
      this.$axios
        .$post('/api/core/goodsEvaluate/auth/save', this.evaluateGoods)
        .then((response) => {
          this.$message.success(response.message)
          this.dialogTableVisible = false
        })

      const h = this.$createElement
      this.$notify({
        title: '提示',
        message: h('i', { style: 'color: teal' }, '每个用户最多评价两次'),
      })
      setTimeout('location.reload () ', 1500) //好东西！
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
