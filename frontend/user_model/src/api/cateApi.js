import request from '@/utils/request';

/**
 * 获取图书类别信息
 * @returns
 */
export const getBookCateApi = function() {
  return request.get('/cate/book');
}

/**
 * 获取店内分类
 * @param {*} storeId
 * @returns
 */
export const getGoodsCateApi = function(storeId) {
  return request.get('/cate/store', {
    params: {
      storeId
    }
  });
}
