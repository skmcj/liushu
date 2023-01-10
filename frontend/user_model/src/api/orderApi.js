import request from '@/utils/request';

/**
 * 分页获取订单
 * @param {*} currentPage 页码
 * @param {*} pageSize 每页数量
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
 * @param {*} status 订单状态
 * @param {*} currentPage 页码
 * @param {*} pageSize 每页数量
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
 * 根据ID获取订单信息
 * @param {*} orderId 订单ID
 * @returns
 */
export const getOrderByIdApi = function(orderId) {
  return request.get(`/frontend/order/id/${orderId}`);
}

/**
 * 创建订单
 * @param {*} order 订单数据
 * @returns
 */
export const generateOrderApi = function(order) {
  return request.post('/frontend/order', order);
}

/**
 * 支付订单 - 零钱支付
 * @param {*} orderId 订单ID
 * @param {*} payPass 支付密码
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
 * @param {*} orderId 订单ID
 * @returns
 */
export const cancelOrderApi = function(orderId) {
  return request.delete('/frontend/order/cancel', {
    params: {
      orderId
    }
  });
}

/**
 * 订单确认收货
 * @param {*} orderId 订单ID
 * @returns
 */
export const confirmReceiptApi = function(orderId) {
  return request.put('/frontend/order/status/confirm', {
    id: orderId
  });
}

/**
 * 订单确认完成
 * @param {*} orderId 订单ID
 * @returns
 */
export const confirmCompleteApi = function(orderId) {
  return request.put('/frontend/order/status/complete', {
    id: orderId
  });
}

/**
 * 订单续借
 * @param {*} orderId 订单ID
 * @param {*} borrowTime 续借时长
 * @returns
 */
export const renewOrderApi = function(orderId, borrowTime) {
  return request.put('/frontend/order/status/renew', {
    id: orderId,
    borrowTime
  });
}

/**
 * 续借订单支付 - 零钱支付
 * @param {*} orderId 订单ID
 * @param {*} payPass 支付密码
 * @returns
 */
export const renewOrderToPayOfLSApi = function(orderId, payPass) {
  return request.put('/frontend/order/renew/pay', {
    orderId,
    payPass
  });
}

/**
 * 订单预约上门回收
 * @param {*} orderId 订单ID
 * @param {*} returnTime 预约上门回收时间
 * @returns
 */
export const repayOrderApi = function(orderId, returnTime) {
  return request.put('/frontend/order/status/repay', {
    id: orderId,
    returnTime
  });
}

/**
 * 逾期中(逾期但未超过系统阈值)订单预约上门回收
 * @param {*} orderId 订单ID
 * @param {*} returnTime 预约上门回收时间
 * @returns
 */
export const repayOverdueOrderApi = function(orderId, returnTime) {
  return request.put('/frontend/order/status/overdue/repay', {
    id: orderId,
    returnTime
  });
}

/**
 * 逾期中订单支付
 * @param {*} orderId 订单ID
 * @param {*} payPass 支付密码
 * @returns
 */
export const repayOverdueOrderToPayOfLSApi = function(orderId, payPass) {
  return request.put('/frontend/order/repay/pay', {
    orderId,
    payPass
  });
}

/**
 * 申请售后
 * @param {{orderId: Number, type: Number, reason: String, proof: String}} afterSales 售后数据
 * @returns
 */
export const applyOrderToASApi = function(afterSales) {
  return request.put('/frontend/order/status/aAS', afterSales);
}

/**
 * 订单售后请求失败，还原请求前订单状态
 * @param {*} orderId 订单ID
 * @returns
 */
export const revertOrderStatusOfASApi = function(orderId) {
  return request.put('/frontend/order/after/status/revert', {
    id: orderId
  });
}

/**
 * 售后订单退货预约上门时间
 * @param {*} asId 售后单据ID
 * @param {*} returnTime 预约上门时间
 * @returns
 */
export const repayOrderOfASApi = function(asId, returnTime) {
  return request.put('/frontend/order/after/status/repay', {
    id: asId,
    returnTime
  });
}
