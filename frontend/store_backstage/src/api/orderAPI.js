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
 * 根据订单ID获取订单信息
 * @param {*} orderId 订单ID
 * @returns
 */
export const getOrderByIdApi = function(orderId) {
  return request.get('/order', {
    params: {
      id: orderId
    }
  });
}

/**
 * 确认订单配送完成
 * @param {*} orderId 订单ID
 * @returns
 */
export const deliveryOfOrderApi = function(orderId) {
  return request.put('/order/status/delivery', {
    id: orderId
  });
}

/**
 * 确认收货(需7天缓冲)
 * @param {*} orderId 订单ID
 * @returns
 */
export const confirmOfOrderApi = function(orderId) {
  return request.put('/order/status/confirm', {
    id: orderId
  });
}

/**
 * 确认完成
 * @param {*} orderId 订单ID
 * @returns
 */
export const confirmCompleteApi = function(orderId) {
  return request.put('/order/status/complete', {
    id: orderId
  });
}

/**
 * 确认上门回收图书
 * @param {*} orderId 订单ID
 * @returns
 */
export const recycleOfOrderApi = function(orderId) {
  return request.put('/order/status/recycle', {
    id: orderId
  });
}

/**
 * 标记已逾期订单
 * @param {*} orderId 订单ID
 * @returns
 */
export const beOverdueOfOrderApi = function(orderId) {
  return request.put('/order/status/overdue', {
    id: orderId
  });
}

/**
 * 同意售后请求
 * @param {*} aSId 售后单据ID
 * @returns
 */
export const agreeAfterSalesServiceApi = function(aSId) {
  return request.put('/order/after/status/audit/1', {
    id: aSId
  });
}

/**
 * 拒绝/关闭售后请求
 * @param {*} aSId 售后单据ID
 * @param {*} failReason 拒绝原因
 * @returns
 */
export const refuseAfterSalesServiceApi = function(aSId, failReason) {
  return request.put('/order/after/status/audit/0', {
    id: aSId,
    failReason
  });
}

/**
 * 修改售后退货模式
 * @param {*} aSId 售后单据ID
 * @param {*} mode 退货模式
 * @returns
 */
export const updateRModeOfAfterSalesApi = function(aSId, mode) {
  return request.put('/order/after/returnMode', {
    id: aSId,
    returnMode: mode
  });
}

/**
 * 确认回收售后退货
 * @param {*} aSId 售后单据ID
 * @returns
 */
export const confirmReceiptOfASOrderApi = function(aSId) {
  return request.put('/order/after/status/receipt', {
    id: aSId
  });
}

/**
 * 确认退款
 * @param {*} orderId 订单ID
 * @returns
 */
export const refundOfOrderApi = function(orderId) {
  return request.put('/order/status/refund', {
    id: orderId
  });
}

/**
 * 修改订单可续借时长
 * @param {*} orderId 订单ID
 * @param {*} duration 时长
 * @returns
 */
export const updateRenewDurationOfOrderApi = function(orderId, duration) {
  return request.put('/order/renew', {
    id: orderId,
    renewDuration: duration
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
  return request.get('/order/od/week/2');
}


/**
 * 获取图书借阅榜信息
 */
export const getBookRankApi = function() {
  return request.get('/order/ba/top10');
}
