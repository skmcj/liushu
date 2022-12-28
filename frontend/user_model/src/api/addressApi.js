import request from '@/utils/request';

/**
 * 获取用户所有地址
 * @returns
 */
export const getAllAddressApi = function() {
  return request.get('/address/all');
}

/**
 * 编辑地址
 * @param {*} address
 * @returns
 */
export const editAddressApi = function(address) {
  return request.put('/address', address);
}

/**
 * 新增地址
 * @param {*} address
 * @returns
 */
export const addAddressApi = function(address) {
  return request.post('/address', address);
}

/**
 * 根据ID删除地址
 * @param {*} id
 * @returns
 */
export const deleteAddressByIdApi = function(id) {
  return request.delete('/address', {
    params: {
      id
    }
  });
}

/**
 * 设置默认地址
 * @param {*} id
 * @returns
 */
export const setDefaultAddressApi = function(id) {
  return request.put('/address/default', { id });
}

/**
 * 获取登录用户默认地址
 * @returns
 */
export const getDefaultAddressApi = function() {
  return request.get('/address/default');
}
