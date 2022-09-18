import request from '@/utils/request';

export const getOrdersAPI = function() {
  return request.get('/mockdata/orders.json');
}
