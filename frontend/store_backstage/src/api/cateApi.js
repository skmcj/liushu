import request from '@/utils/request.js';

export const getCatesApi = function() {
  return request.get('/mockdata/cate.json');
}
