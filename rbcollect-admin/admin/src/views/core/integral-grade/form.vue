<template>
  <div class="app-container">
    <!-- 输入表单 -->
    <el-form label-width="120px">
      <el-form-item label="积分额度">
        <el-input-number v-model="integralGrade.integralAmount" :min="0" />
      </el-form-item>
      <el-form-item label="重量区间开始">
        <el-input-number v-model="integralGrade.weightEnd" :min="0" />
      </el-form-item>
      <el-form-item label="重量区间结束">
        <el-input-number v-model="integralGrade.weightStart" :min="0" />
      </el-form-item>
      <el-form-item>
        <el-button
          :disabled="saveBtnDisabled"
          type="primary"
          @click="saveOrUpdate()"
        >
          保存
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import intergralGradeApi from "@/api/core/integral-grade";
export default {
  data() {
    return {
      saveBtnDisabled: false, //是否禁用保存按钮，防止重复提交
      integralGrade: {}, //积分等级对象
    };
  },
  created() {
    //当路由中存在id属性，就是回显表单组件
    if (this.$route.params.id) {
      this.fetchById(this.$route.params.id);
    }
  },

  methods: {
    //根据id获取数据
    fetchById(id) {
      intergralGradeApi.getById(id).then((response) => {
        this.integralGrade = response.data.record;
      });
    },
    //保存或更新
    saveOrUpdate() {
      this.saveBtnDisabled = true;
      if (!this.integralGrade.id) {
        //调用新增
        this.saveData();
      } else {
        //调用更新
        this.updateData();
      }
    },

    saveData() {
      intergralGradeApi.save(this.integralGrade).then((response) => {
        this.$message({
          message: response.message,
          type: "success",
        });
      });
      this.$router.push("/core/integral-grade/list"); //跳转路由
    },

    updateData() {
      intergralGradeApi.updateById(this.integralGrade).then((response) => {
        this.$message({
          message: response.message,
          type: "success",
        });
      });
      this.$router.push("/core/integral-grade/list"); //跳转路由
    },
  },
};
</script>
<style scoped></style>
