import request from '@/utils/request';

/**
 * 获取书店名称及门脸图片
 * @param {*} storeId
 * @returns
 */
export const getStoreNameApi = function(storeId) {
  return request.get('/business/name', {
    params: {
      storeId: storeId
    }
  });
}

/**
 * 获取书店信息
 * @param {*} storeId
 */
export const getStoreInfoApi = function(storeId) {
  return request.get('/business/info', {
    params: {
      storeId: storeId
    }
  });
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
 * 修改员工状态
 * @returns
 */
export const editStoreStatusApi = function() {
  return request.get('/business/status');
}

/**
 * 修改店铺基本信息
 */
export const editStoreInfoApi = function(storeData) {
  return request.put('/business/info', storeData);
}

/**
 * 修改店铺认证信息
 */
export const editStoreAuthApi = function(detailData) {
  return request.put('/business/auth', detailData);
}

/**
 * 修改邮箱
 * @param {*} data
 * @returns
 */
export const editStoreEmailApi = function(data) {
  return request.put('/business/email', data);
}

/**
 * 修改手机号
 * @param {*} data
 * @returns
 */
export const editStorePhoneApi = function(data) {
  return request.put('/business/phone', data);
}
