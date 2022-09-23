<template>
  <div>
    <div class="dashboard-container">
      <div class="dashboard-text">
        <el-tag type="success" style="font-size: 20px; margin: 15px"
          >姓名: {{ name }}</el-tag
        >
      </div>
      <div class="dashboard-text">
        <span v-for="role in roles" :key="role">
          <el-tag type="success" style="font-size: 20px; margin-left: 15px"
            >权限: {{ role }}</el-tag
          ></span
        >
      </div>
    </div>
    <el-container>
      <div id="myChart" style="width: 800px; height: 500px"></div>
    </el-container>
  </div>
</template>

<script>
import userInfoApi from "@/api/core/user-info";
import { mapGetters } from "vuex";
import * as echarts from "echarts";
export default {
  name: "Echarts",
  computed: {
    ...mapGetters(["name", "roles"]),
  },
  data() {
    return {
      chartColumn: null,
      time: null,
      count: null,
    };
  },
  mounted() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      userInfoApi.getForCharts().then((response) => {
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById("myChart"));
        // 绘制图表
        myChart.setOption({
          xAxis: {
            type: "category",
            data: response.data.result.time,
            name: "日期", // x轴名称
            // x轴名称样式
            nameTextStyle: {
              fontWeight: 600,
              fontSize: 15,
            },
          },
          yAxis: {
            type: "value",
            name: "注册用户人数", // y轴名称
            // y轴名称样式
            nameTextStyle: {
              fontWeight: 600,
              fontSize: 15,
            },
          },
          series: [
            {
              //根据名字对应到相应的系列

              data: response.data.result.count,
              type: "line",
              smooth: true,
              label: {
                show: true,
                position: "bottom",
                textStyle: {
                  fontSize: 10,
                },
              },
            },
          ],
          tooltip: {
            trigger: "axis", // axis   item   none三个值
          },
        });
      });
    },
  },
};
</script>
