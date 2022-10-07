// 正则匹配18位身份证号
const REGEX_ID_18 = new RegExp('^'
                            + '\\d{6}'                     // 6位地区码
                            + '(18|19|([23]\\d))\\d{2}'    // 年YYYY
                            + '((0[1-9])|(10|11|12))'      // 月MM
                            + '(([0-2][1-9])|10|20|30|31)' // 日DD
                            + '\\d{3}'                     // 3位顺序码
                            + '[0-9Xx]'                    // 校验码
                            + '$');

// 正则匹配邮箱
const REGEX_EMAIL = /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;

// 身份证号验证过程的系数
const wArr = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3 ];

/**
 * 判断手机号格式是否正确
 * @param {*} val
 * @returns
 */
const isCellPhone = function(val) {
  if (!/^1(3|4|5|6|7|8)\d{9}$/.test(val)) {
    return false
  } else {
    return true
  }
}

/**
 * 验证邮箱格式是否正确
 * @param {String} val
 */
const isEmail = function(val) {
  if(REGEX_EMAIL.test(val)) {
    return true;
  }else {
    return false;
  }
}

/**
 * 检查18位身份证号的校验码是否正确
 * @param {String} val 身份证号
 */
function validateCheckNumber(val) {
  let sum = 0;
  let idArr = val.split('');
  for(let i = 0; i < idArr.length; i++) {
    if(idArr[i] !== 'x' || idArr !== 'X') {
      sum += parseInt(idArr[i]) * wArr[i % 10];
    } else {
      sum += 10 * wArr[i];
    }
  }
  if(sum % 11 === 1) {
    return true;
  } else {
    return false;
  }
}

/**
 * 检查ISBN码是否有效
 * @param {*} val ISBN码
 */
function validateCheckISBN(val) {
  let sum = 0;
  let numArr = val.split('');
  let flag = false;
  if(val.length === 10) {
    // 10位
    for(let i = 0; i < numArr.length; i++) {
      if(numArr[i] === 'x' || numArr[i] === 'X') {
        sum += 10 * (10 - i);
      }else {
        sum += parseInt(numArr[i]) * (10 - i);
      }
    }
    flag = (sum % 11 === 0);
  }else if(val.length === 13) {
    // 13位
    for(let i = 0; i < numArr.length - 1; i += 2) {
      sum += parseInt(numArr[i]) + parseInt(numArr[i + 1]) * 3;
    }
    sum += parseInt(numArr[numArr.length - 1]);
    flag = (sum % 10 === 0);
  }
  return flag;
}

/**
 * 自定义规则-检验手机号的格式
 * @param {Object} rule - 源描述符中与要验证的字段名称对应的验证规则。它始终分配有一个属性，其中包含要验证的字段的名称 -> field
 * @param {String} value 需验证的值，这里表示用户输入的手机号字符串
 * @param {Function} callback 验证完成返回的回调函数
 */
const checkPhone = function(rule, value, callback) {
  // let phoneReg = /(^1[3|4|5|6|7|8|9]\d{9}$)|(^09\d{8}$)/;
  if (value === '') {
    callback(new Error('请输入员工手机号'));
  } else if (!isCellPhone(value)) { // 引入methods中封装的检查手机格式的方法
    callback(new Error('请输入正确的手机号!'));
  } else {
    callback();
  }
}

/**
 * 自定义规则-检验身份证号的格式
 * @param {Object} rule 源对象规则
 * @param {String} value 用户输入身份证号
 * @param {Function} callback 验证完成的回调函数
 */
const checkIDNumber = function(rule, value, callback) {
  if(value === '') {
    callback(new Error('请输入员工身份证号'));
  } else if(value.length !== 18) {
    callback(new Error('请输入18位身份证号'));
  } else if(!REGEX_ID_18.test(value)) {
    callback(new Error('请输入正确的身份证号'));
  } else if(!validateCheckNumber(value)) {
    callback(new Error('请输入正确的身份证号'));
  } else {
    callback();
  }
}

/**
 * 自定义规则-检验ISBN的格式
 * @param {Object} rule 源对象规则
 * @param {String} value 用户输入ISBN号
 * @param {Function} callback 验证完成的回调函数
 */
const checkISBN = function(rule, value, callback) {
  value = value.replace(/-/g, '');
  if(value === '') {
    callback(new Error('请输入图书ISBN码'));
  }else if(value.length !== 10 && value.length !== 13) {
    console.log('length', value, value.length);
    callback(new Error('请输入10位或13位的ISBN码'));
  }else if(!validateCheckISBN(value)) {
    callback(new Error('请输入正确的ISBN码'));
  }else {
    callback();
  }
}

export default {
  isEmail,
  isCellPhone,
  checkPhone,
  checkIDNumber,
  checkISBN
}
