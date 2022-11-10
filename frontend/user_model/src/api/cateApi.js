import request from '@/utils/request';

/**
 * 获取图书类别信息
 * @returns
 */
export const getBookCateApi = function() {
  return request.get('/data/bookcate.json');
}
