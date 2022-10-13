import request from '@/utils/request.js';

export const getCatesAPI = function() {
  return request.get('/mockdata/cate.json');
}
