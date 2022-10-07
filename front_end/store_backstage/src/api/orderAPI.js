import request from '@/utils/request';

export const getOrdersAPI = function() {
  return request.get('/mockdata/orders.json');
}

export const getOrderQuantityAPI = function() {
  return request.get('/mockdata/orderData.json');
}
