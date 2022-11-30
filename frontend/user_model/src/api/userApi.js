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
 * 获取注册验证码
 * @param {*} email
 * @returns
 */
export const getValidateCodeApi = function(email) {
  return request.get('/user/logon/code', {
    params: {
      to: email
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
  return request.post('/user/info', userInfo);
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
