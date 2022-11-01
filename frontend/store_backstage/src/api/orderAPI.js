import request from '@/utils/request';

/**
 * 获取订单列表
 * @param {*} param
 * @returns
 */
export const getOrdersApi = function(param) {
  return request.get('/order/page', {
    params: param
  });
}

/**
 * 获取订单列表 - 测试
 * @param {*} param
 * @returns
 */
export const getOrdersTestApi = function(param) {
  return request.get('/mockdata/orders.json');
}

export const getOrderQuantityApi = function() {
  return request.get('/mockdata/orderData.json');
}
