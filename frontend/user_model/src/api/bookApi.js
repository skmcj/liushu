import request from '@/utils/request';

/**
 * 根据图书ID获取图书详情
 * @param {String} id 图书id
 * @returns
 */
export const getBookDetailApi = function(id) {
  return request.get('/data/bookDetail.json');
}
