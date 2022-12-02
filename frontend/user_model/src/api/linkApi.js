import request from '@/utils/request';

/**
 * 根据类型获取友情链接
 * @param {*} type
 * @returns
 */
export const getLinkByTypeApi = function(type) {
  return request.get('/link/type', {
    params: {
      type
    }
  });
}

/**
 * 添加友情链接
 * @param {*} link
 * @returns
 */
export const addLinkApi = function(link) {
  return request.post('/link', link);
}
