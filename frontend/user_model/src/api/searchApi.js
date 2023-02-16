import request from '@/utils/request';

/**
 * 全局搜索图书
 * @param {*} keyword 关键词
 * @param {*} currentPage 页码
 * @param {*} pageSize 页量
 * @returns
 */
export const searchBookApi = function(keyword, currentPage, pageSize) {
  return request.get('/search/book', {
    params: {
      keyword,
      currentPage,
      pageSize
    }
  });
}

/**
 * 全局搜索书店
 * @param {*} keyword 关键词
 * @param {*} currentPage 页码
 * @param {*} pageSize 页量
 * @returns
 */
export const searchShopApi = function(keyword, currentPage, pageSize) {
  return request.get('/search/shop', {
    params: {
      keyword,
      currentPage,
      pageSize
    }
  });
}
