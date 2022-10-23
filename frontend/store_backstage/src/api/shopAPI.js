import request from '@/utils/request';

/**
 * 获取店内分类信息
 * @returns {Promise}
 */
export const getGoodsCateApi = function() {
  return request.get('/mockdata/goods_cate.json');
}

/**
 * 注册商家账户
 * @param {*} email
 * @param {*} password
 * @param {*} code
 * @returns
 */
export const businessLogonApi = function(email, password, code) {
  return request.post('/business/logon', {
    email,
    password,
    code
  });
}

/**
 * 提交审核信息
 * @param {*} form
 * @returns
 */
export const businessAuthApi = function(form) {
  return request.post('/business/auth', form);
}

/**
 * 员工登录
 * @returns
 */
export const loginEmployeeApi = function(username, password) {
  return request.post('/business/login', {
    username: username,
    password: password
  });
}

/**
 * 获取员工列表
 * @returns
 */
export const getEmployeesApi = function() {
  return request.get('/mockdata/employee.json');
}
