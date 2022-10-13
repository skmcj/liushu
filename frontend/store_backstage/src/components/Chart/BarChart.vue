<template>
  <div class="bar-chart" :ref="refName"></div>
</template>

<script>
/* eslint-disable */
import chartHandler from '@/utils/chartDataHandler';

export default {
  props: {
    /**
     * 用于帮助echarts获取DOM，创建实例
     */
    refName: {
      type: String,
      required: true
    },
    /**
     * 用于指定图表设置，会覆盖所有默认设置
     * - 当指定了该属性值，其它属性(除必传值外)可不传，以该项为主
     */
    option: Object,
    /**
     * 图表的主标题
     * - 如果输入为字符串，则为主标题
     */
    title: [String, Object],
    /**
     * 图表的图例
     */
    legend: [Array, Object],
    // 图表位置
    grid: Object,
    /**
     * 图表的数据集
     */
    dataset: Object,
    /**
     * 图表x轴信息
     */
    xAxis: {
      type: Object,
      default: function () {
        return {
          type: 'category',
          name: 'x轴',
          data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
        };
      }
    },
    /**
     * 图表y轴信息
     */
    yAxis: {
      type: Object,
      default: function () {
        return {
          type: 'value',
          name: 'y轴'
        };
      }
    },
    series: {
      type: Array,
      default: function () {
        return [
          {
            type: 'bar',
            name: '系列',
            data: [12, 45, 56, 24, 18, 42, 8]
          }
        ];
      }
    },
    /**
     * 提示框组件
     */
    tooltip: {
      type: Object,
      default: function () {
        return {
          trigger: 'axis'
        };
      }
    }
  },
  data() {
    return {
      chart: '',
      options: {
        title: chartHandler.handleTitle(this.title),
        grid: this.grid,
        legend: chartHandler.handleLegend(this.legend),
        dataset: this.dataset,
        xAxis: this.xAxis,
        yAxis: this.yAxis,
        series: this.series,
        tooltip: this.tooltip
      },
      chartOption: {}
    };
  },
  mounted() {
    this.drawChart();
  },
  methods: {
    /**
     * 初始化图表
     */
    drawChart() {
      this.chart = echarts.init(this.$refs[this.refName]);
      this.chartOption = this.option ? this.option : this.options;
      this.chart.setOption(this.chartOption);
    },
    /**
     * 更新图表
     */
    updateChart() {
      this.chart = echarts.init(this.$refs[this.refName]);
      // this.chart.clear();
      this.chart.setOption(this.chartOption);
    }
  },
  watch: {
    /**
     * 监听图表数据
     */
    chartOption: {
      handler(val) {
        this.updateChart();
      },
      deep: true
    }
  }
};
</script>

<style lang="less" scoped>
.bar-chart {
  width: 100%;
  height: 100%;
}
</style>
