/**
 * 返回格式化字符串
 * @param {Date} date 日期
 * @param {String} fmt 格式字符串
 *   - Y -> 年
 *   - m -> 月
 *   - d -> 日
 *   - H -> 时
 *   - M -> 分
 *   - S -> 秒
 * @returns
 */
const formatDate = function(date, fmt = 'YYYY-mm-dd') {
  let ret;
  const opt = {
    'Y+': date.getFullYear().toString(),        // 年
    'm+': (date.getMonth() + 1).toString(),     // 月
    'd+': date.getDate().toString(),            // 日
    'H+': date.getHours().toString(),           // 时
    'M+': date.getMinutes().toString(),         // 分
    'S+': date.getSeconds().toString()          // 秒
    // 有其他格式化字符需求可以继续添加，必须转化成字符串
  };
  for (let k in opt) {
    ret = new RegExp('(' + k + ')').exec(fmt);
    if (ret) {
      fmt = fmt.replace(ret[1], (ret[1].length === 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, '0')))
    }
  }
  return fmt;
}

/**
 * 计算两个日期之间相差的天数
 * @param {*} start
 * @param {*} end
 * @returns  end - start 天
 */
const getDaysBetween = function(start, end) {
  let days = (end - start) / (1 * 24 * 60 * 60 * 1000);
  return parseInt(days);
}

/**
 * 将数字转化为指定位数的数字，高位默认补0
 * @param {Number} num 数字
 * @param {Number} unit 位数
 * @param {String} char 补位字符，默认为0
 * @returns {String}
 */
const formatUnits = function(num, unit = 2, char = '0') {
  let numStr = num.toString();
  if(numStr.length > unit) return numStr;
  return numStr.padStart(unit, char);
}

/**
 * 旋转色调
 * @param {*} rgb 源色
 * @param {*} degree 色调旋转值
 * @returns
 */
const changeHue = function(rgb, degree) {
  let hsl = rgbToHSL(rgb);
  hsl.h += degree;
  if (hsl.h > 360) {
    hsl.h -= 360;
  } else if (hsl.h < 0) {
    hsl.h += 360;
  }
  return hslToRGB(hsl);
}

// exepcts a string and returns an object
function rgbToHSL(rgb) {
  // strip the leading # if it's there
  rgb = rgb.replace(/^\s*#|\s*$/g, '');

  // convert 3 char codes --> 6, e.g. `E0F` --> `EE00FF`
  if (rgb.length === 3) {
    rgb = rgb.replace(/(.)/g, '$1$1');
  }

  let r = parseInt(rgb.substr(0, 2), 16) / 255;
  let g = parseInt(rgb.substr(2, 2), 16) / 255;
  let b = parseInt(rgb.substr(4, 2), 16) / 255;
  let cMax = Math.max(r, g, b);
  let cMin = Math.min(r, g, b);
  let delta = cMax - cMin;
  let l = (cMax + cMin) / 2;
  let h = 0;
  let s = 0;

  if (delta === 0) {
    h = 0;
  } else if (cMax === r) {
    h = 60 * (((g - b) / delta) % 6);
  } else if (cMax === g) {
    h = 60 * ((b - r) / delta + 2);
  } else {
    h = 60 * ((r - g) / delta + 4);
  }

  if (delta === 0) {
    s = 0;
  } else {
    s = delta / (1 - Math.abs(2 * l - 1));
  }

  return {
    h: h,
    s: s,
    l: l
  };
}

// expects an object and returns a string
function hslToRGB(hsl) {
  let h = hsl.h;
  let s = hsl.s;
  let l = hsl.l;
  let c = (1 - Math.abs(2 * l - 1)) * s;
  let x = c * (1 - Math.abs(((h / 60) % 2) - 1));
  let m = l - c / 2;
  let r;
  let g;
  let b;

  if (h < 60) {
    r = c;
    g = x;
    b = 0;
  } else if (h < 120) {
    r = x;
    g = c;
    b = 0;
  } else if (h < 180) {
    r = 0;
    g = c;
    b = x;
  } else if (h < 240) {
    r = 0;
    g = x;
    b = c;
  } else if (h < 300) {
    r = x;
    g = 0;
    b = c;
  } else {
    r = c;
    g = 0;
    b = x;
  }

  r = normalizeRgbValue(r, m);
  g = normalizeRgbValue(g, m);
  b = normalizeRgbValue(b, m);

  return rgbToHex(r, g, b);
}

function normalizeRgbValue(color, m) {
  color = Math.floor((color + m) * 255);
  if (color < 0) {
    color = 0;
  }
  return color;
}

function rgbToHex(r, g, b) {
  return '#' + ((1 << 24) + (r << 16) + (g << 8) + b).toString(16).slice(1);
}

export default {
  formatDate,
  formatUnits,
  getDaysBetween,
  changeHue
}
