/**
 * 项目正则的枚举
 */
export const REGEX = {
  /** 强密码正则匹配表达式，6个字符以上，且至少包含大写字母、小写字母、数字、特殊字符 */
  PASS_PLUS: /^\S*(?=\S{6,})(?=\S*\d)(?=\S*[A-Z])(?=\S*[a-z])(?=\S*[!#%*?,.:;`+-])\S*$/,
  /** 弱密码验证，8个及以上字符，包含字母及数字即可 */
  PASS: /^\S*(?=\S{8,32})(?=\S*[a-zA-Z])(?=\S*\d)\S*$/,
  /** 用户名正则：由字母、数字、-、_组成，大于6位且小于等于25位 */
  USERNAME: /^[A-Za-z0-9_-]{6,25}$/,
  /** 邮箱正则 */
  EMAIL: /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/
}