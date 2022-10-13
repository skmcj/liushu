// ---------- 工具函数 ----------
function noop() {}

// ---------- 功能函数 ----------
/**
 * 处理title属性的传值
 * @param {String | Object} val 需处理的传值
 */
const handleTitle = function(val) {
  let type = Object.prototype.toString.call(val);
  if(type === '[object String]') {
    return {
      text: val,
      textStyle: {
        color: '#333333',
        fontSize: 16
      }
    };
  }else if(type === '[object Object]') {
    return val;
  }else {
    return {};
  }
}

/**
 * 处理legend属性的传值
 * @param {Array | Object} val 需处理的传值
 */
const handleLegend = function(val) {
  let type = Object.prototype.toString.call(val);
  if(type === '[object Array]') {
    return {
      data: val
    };
  }else if(type === '[object Object]') {
    return val;
  }else {
    return {
      data: ['系列']
    };
  }
}

/**
 * 包装订单量数据
 * @param {Object} data 后台获取的json数据
 */
const packingOrderData = function(data) {
  let option = {};
  option.title = {
    text: '近14天订单数据',
    textStyle: {
      fontSize: 16
    }
  };
  option.tooltip = {
    trigger: 'axis',
    axisPointer: {
      type: 'none'
    },
    formatter: function(param) {
      return `${param[0].data[0]}<br />订单量：${param[0].data[1]}`;
    }
  };
  option.grid = {
    bottom: '9%'
  };
  option.legend = {
    data: ['柱形图', '折线图'],
    top: '5%'
  };
  option.dataset = {
    dimensions: ['日期', '订单量'],
    source: data
  };
  option.xAxis = {
    type: 'category',
    name: '日期',
    axisLabel: {
      interval: 0,
      rotate: 45
    },
    axisTick: {
      alignWithLabel: true
    }
  };
  option.yAxis = {
    name: '订单量'
  };
  option.series = [
    {
      type: 'bar',
      name: '柱形图',
      encode: {
        x: '日期',
        y: '订单量'
      },
      itemStyle: {
        color: {
          type: 'linear',
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          colorStops: [{
            offset: 0, color: '#79f1a4' // 0% 处的颜色
          }, {
            offset: 1, color: '#0e5cad' // 100% 处的颜色
          }],
          global: false // 缺省为 false
        }
      }
    },
    {
      type: 'line',
      name: '折线图',
      encode: {
        x: '日期',
        y: '订单量'
      },
      itemStyle: {
        color: '#f6ad49'
      }
    }
  ];
  return option;
}

/**
 * 包装图书借阅量数据
 * @param {Object} data 待包装数据
 */
const packingBookRank = function(data) {
  let option = {};
  option.title = {
    text: '图书借阅榜',
    textStyle: {
      fontSize: 16
    }
  };
  option.grid = { containLabel: true };
  option.visualMap = {
    orient: 'horizontal',
    left: 'center',
    min: 0,
    max: data.max + 18,
    text: ['高借阅量', '低借阅量'],
    // Map the score column to color
    dimension: 1,
    inRange: {
      color: ['#87cbd0', '#6abf93', '#fbec70']
    }
  };
  option.tooltip = {
    trigger: 'axis',
    axisPointer: {
      type: 'none'
    }
    // formatter: function(param) {
    //   return `${param[0].data[0]}<br />订单量：${param[0].data[1]}`;
    // }
  };
  option.dataset = {
    dimensions: ['书名', '借阅量'],
    source: data.data
  };
  option.xAxis = {
    name: '借阅量'
  };
  option.yAxis = {
    type: 'category',
    axisLabel: {
      interval: 0,
      rotate: -25
    },
    axisTick: {
      alignWithLabel: true
    },
    inverse: true
  };
  option.series = [
    {
      type: 'bar',
      realtimeSort: true,
      encode: {
        x: '借阅量',
        y: '书名'
      }
    }
  ];
  return option;
}

export default {
  handleTitle,
  handleLegend,
  packingOrderData,
  packingBookRank
}
