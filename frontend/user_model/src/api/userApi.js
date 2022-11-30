import request from '@/utils/request';

/**
 * 用户登录
 * @param {*} user
 * @returns
 */
export const userLoginApi = function(user) {
  return request.post('/user/login', user);
}

/**
 * 校验用户token
 * @param {*} token
 * @returns
 */
export const validateTokenApi = function(token) {
  return request.get('/user/validate/token', {
    params: {
      token
    }
  })
}

/**
 * 获取验证码
 * @param {*} email
 * @returns
 */
export const getValidateCodeApi = function(email, type = 'logon') {
  return request.get(`/user/code/${type}`, {
    params: {
      to: email
    }
  });
}

/**
 * 校验验证码
 * @param {*} code
 * @returns
 */
export const checkValidateCodeApi = function(code) {
  return request.get('/user/code/check', {
    params: {
      code
    }
  });
}

/**
 * 用户注册
 * @param {*} user
 * @returns
 */
export const userLogonApi = function(user) {
  return request.post('/user/logon', user);
}

/**
 * 修改更新用户信息
 * @param {*} userInfo
 * @returns
 */
export const updateUserInfoApi = function(userInfo) {
  return request.put('/user/info', userInfo);
}

/**
 * 修改更新用户邮箱
 * @param {*} user
 * @returns
 */
export const updateUserEmailApi = function(user) {
  return request.put('/user/email', user);
}

/**
 * 修改更新用户密码
 * @param {*} user
 * @returns
 */
export const updateUserPassApi = function(user) {
  return request.put('/user/pass', user);
}

/**
 * 获取用户信息
 * @param {*} userId
 * @returns
 */
export const getUserInfoApi = function(userId) {
  return request.get('/user/info', {
    params: {
      id: userId
    }
  });
}
