import request from '@/utils/request';

/**
 * 判断商家是否已收藏
 * @param {*} storeId 商家ID
 * @returns
 */
export const isCollectionApi = function(storeId) {
  return request.get('/collection/is', {
    params: {
      storeId
    }
  });
}

/**
 * 收藏商家
 * @param {*} storeId 商家ID
 * @returns
 */
export const addCollectionApi = function(storeId) {
  return request.get('/collection/add', {
    params: {
      storeId
    }
  });
}

/**
 * 取消收藏商家
 * @param {} storeId 商家ID
 * @returns
 */
export const cancelCollectionApi = function(storeId) {
  return request.delete('/collection', {
    params: {
      storeId
    }
  });
}

/**
 * 分页获取收藏列表
 * @param {*} currentPage 页码
 * @param {*} pageSize 页量
 * @returns
 */
export const getCollectionPageApi = function(currentPage, pageSize) {
  return request.get('/collection', {
    params: {
      currentPage,
      pageSize
    }
  });
}
