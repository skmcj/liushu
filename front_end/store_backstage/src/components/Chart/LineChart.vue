<template>
  <div class="line-chart" ref="lineChart"></div>
</template>

<script>
/* eslint-disable */

export default {
  props: {
    option: {
      type: Object,
      default: function () {
        return {};
      }
    },
    title: {
      type: String,
      default: '折线图样例'
    },
    legend: {
      type: [Array, Object],
      default: function () {
        return ['样例一', '样例二', '样例三', '样例四', '样例五'];
      }
    },
    grid: {
      type: [Object],
      default: function () {
        return {
          left: '3%',
          right: '4%',
          bottom: '5%',
          containLabel: true
        };
      }
    },
    /**
     * - 'value' 数值轴，适用于连续数据。
     * - 'category' 类目轴，适用于离散的类目数据。为该类型时类目数据可自动从 series.data 或 dataset.source 中取，或者可通过 xAxis.data 设置类目数据。
     * - 'time' 时间轴，适用于连续的时序数据，与数值轴相比时间轴带有时间的格式化，在刻度计算上也有所不同，例如会根据跨度的范围来决定使用月，星期，日还是小时范围的刻度。
     * - 'log' 对数轴。适用于对数数据。
     */
    xAxisType: {
      type: String,
      default: 'category'
    },
    xAxisData: {
      type: Array,
      default: function () {
        return ['一', '二', '三', '四', '五'];
      }
    },
    yAxisType: {
      type: String,
      default: 'value'
    },
    series: {
      type: Array,
      default: function () {
        return [
          {
            name: '样例一',
            type: 'line',
            data: [135, 124, 98, 65, 214]
          },
          {
            name: '样例二',
            type: 'line',
            data: [138, 21, 125, 75, 158]
          },
          {
            name: '样例三',
            type: 'line',
            data: [75, 58, 254, 65, 124]
          },
          {
            name: '样例四',
            type: 'line',
            data: [125, 65, 57, 84, 185]
          },
          {
            name: '样例五',
            type: 'line',
            data: [158, 154, 23, 54, 45]
          }
        ];
      }
    }
  },
  data() {
    return {
      options: {
        title: {
          text: this.title
        },
        legend: this.handleLegend,
        grid: this.grid,
        xAxis: {
          type: this.xAxisType,
          boundaryGap: false,
          data: this.xAxisData
        },
        yAxis: {
          type: this.yAxisType
        },
        series: this.series
      }
    };
  },
  computed: {
    handleLegend() {
      let type = Object.prototype.toString.call(this.legend);
      if (type === '[object Array]') {
        return {
          top: '5%',
          data: this.legend
        };
      } else if (type === '[object Object]') {
        return this.legend;
      } else {
        return '';
      }
    }
  },
  mounted() {
    console.log(this.options);
    console.log(this.handleLegend);
    this.drawChart();
  },
  methods: {
    drawChart() {
      let chart = echarts.init(this.$refs.lineChart);
      this.options.legend = this.handleLegend;
      let chartOption = JSON.stringify(this.option) !== '{}' ? this.option : this.options;
      chart.setOption(chartOption);
    }
  }
};
</script>

<style lang="less" scoped>
.line-chart {
  width: 100%;
  height: 100%;
}
</style>
