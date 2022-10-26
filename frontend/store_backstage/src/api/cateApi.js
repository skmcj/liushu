import request from '@/utils/request.js';

/**
 * 分页获取店内分类
 * @param {*} param
 * @returns
 */
export const getCatePageApi = function(param) {
  return request.get('/business/cate/page', {
    params: param
  });
}

/**
 * 根据ID获取店内分类信息
 * @param {*} id
 * @returns
 */
export const getCateByIdApi = function(id) {
  return request.get('/business/cate', {
    params: {
      id: id
    }
  });
}

/**
 * 获取店内所有分类
 * @param {*} storeId
 * @returns
 */
export const getAllCateApi = function(storeId) {
  return request.get('/business/cate/all', {
    params: {
      storeId: storeId
    }
  });
}

/**
 * 修改指定分类
 * @param {*} cate
 * @returns
 */
export const editCateApi = function(cate) {
  return request.put('/business/cate', cate);
}

/**
 * 删除指定店内分类
 * @param {*} id
 * @returns
 */
export const delCateApi = function(id) {
  return request.delete('/business/cate', {
    params: {
      id: id
    }
  });
}

/**
 * 新增店内分类
 * @param {*} cate
 * @returns
 */
export const saveCateApi = function(cate) {
  return request.post('/business/cate', cate);
}
