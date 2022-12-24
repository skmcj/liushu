import request from '@/utils/request';

/**
 * 获取推荐图书
 * @param {*} size 数量
 * @returns
 */
export const getBookByRecommendApi = function(size) {
  return request.get('/frontend/book/recommend', {
    params: {
      size
    }
  });
}

/**
 * 根据分类获取推荐图书
 * @param {*} size 数量
 * @param {*} cateId 分类ID
 * @returns
 */
export const getBookByCateApi = function(size, cateId) {
  return request.get('/frontend/book/cate', {
    params: {
      size,
      cateId
    }
  });
}

/**
 * 根据ID获取图书详情
 * @param {*} id
 * @returns
 */
export const getBookDetailByIdApi = function(id) {
  return request.get('/frontend/book', {
    params: {
      id
    }
  });
}

/**
 * 根据类别分页获取图书
 * @param {*} cateId
 * @param {*} currentPage
 * @param {*} pageSize
 * @returns
 */
export const getBookPageByCateApi = function(cateId, currentPage, pageSize) {
  return request.get('/frontend/book/cate/page', {
    params: {
      cateId,
      currentPage,
      pageSize
    }
  });
}

/**
 * 根据分类获取图书榜单
 * @param {*} cateId
 * @param {*} size
 * @returns
 */
export const getBookRankByCateApi = function(cateId, size) {
  return request.get('/frontend/book/cate/rank', {
    params: {
      cateId,
      size
    }
  });
}
