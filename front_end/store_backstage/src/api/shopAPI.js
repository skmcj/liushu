import request from '@/utils/request';

/**
 * 获取店内分类信息
 * @returns {Promise}
 */
export const getGoodsCateAPI = function() {
  return request.get('/mockdata/goods_cate.json');
}
