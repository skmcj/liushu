import request from '@/utils/request';

/**
 * 根据ID获取商家详细信息
 * @param {*} storeId 商家ID
 * @returns
 */
export const getShopDetailApi = function(storeId) {
  return request.get('/data/businessDetail.json');
}

/**
 * 获取推荐商家
 * @param {*} size
 * @returns
 */
export const getShopByRecommendApi = function(size) {
  return request.get('/frontend/store', {
    params: {
      size
    }
  });
}

/**
 * 获取商家基本信息
 * @param {*} id
 * @returns
 */
export const getShopByIdApi = function(id) {
  return request.get(`/frontend/store/${id}`);
}

/**
 * 获取商家借阅量排行榜
 * @param {*} storeId
 * @param {*} size
 * @returns
 */
export const getBookRankOfShopApi = function(storeId, size) {
  return request.get('/frontend/store/rank', {
    params: {
      storeId,
      size
    }
  });
}

/**
 * 分页获取店内图书
 * @param {*} storeId
 * @param {*} currentPage
 * @param {*} pageSize
 * @returns
 */
export const getBookPageOfShopApi = function(storeId, currentPage, pageSize) {
  return request.get('/frontent/store/book/page', {
    params: {
      storeId,
      currentPage,
      pageSize
    }
  });
}

/**
 * 根据类别分页获取店内图书
 * @param {*} storeId
 * @param {*} cateId
 * @param {*} currentPage
 * @param {*} pageSize
 * @returns
 */
export const getBookPageByCateOfShopApi = function(storeId, cateId, currentPage, pageSize) {
  return request.get('/frontend/store/book/cate', {
    params: {
      storeId,
      cateId,
      currentPage,
      pageSize
    }
  });
}
