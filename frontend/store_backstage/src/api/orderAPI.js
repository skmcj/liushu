import request from '@/utils/request';

export const getOrdersApi = function() {
  return request.get('/mockdata/orders.json');
}

export const getOrderQuantityApi = function() {
  return request.get('/mockdata/orderData.json');
}
