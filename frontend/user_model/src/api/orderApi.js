import request from '@/utils/request';

/**
 * 分页获取订单
 * @param {*} currentPage
 * @param {*} pageSize
 * @returns
 */
export const getAllOrderOgPageApi = function(currentPage, pageSize) {
  return request.get('/frontend/order/all', {
    params: {
      currentPage,
      pageSize
    }
  });
}

/**
 * 根据状态分页获取订单
 * @param {*} status
 * @param {*} currentPage
 * @param {*} pageSize
 * @returns
 */
export const getOrderByStatusOfPageApi = function(status, currentPage, pageSize) {
  return request.get(`/frontend/order/${status}`, {
    params: {
      currentPage,
      pageSize
    }
  });
}

/**
 * 创建订单
 * @param {*} order
 * @returns
 */
export const generateOrderApi = function(order) {
  return request.post('/frontend/order', order);
}

/**
 * 支付订单 - 零钱支付
 * @param {*} orderId
 * @param {*} payPass
 * @returns
 */
export const payOrderByLSApi = function(orderId, payPass) {
  return request.post('/frontend/order/liushu/pay', {
    orderId,
    payPass
  });
}

/**
 * 取消订单
 * @param {*} orderId
 * @returns
 */
export const cancelOrderApi = function(orderId) {
  return request.delete('/frontend/order/cancel', {
    params: {
      orderId
    }
  });
}
