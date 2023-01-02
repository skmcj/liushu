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

export default {
  formatDate,
  formatUnits
}
