import request from '@/utils/request';

/**
 * 根据图书ID获取评论
 * @param {String} bookId 图书ID
 * @param {Number} currentPage 页码
 * @param {Number} pageSize 每页数量
 * @returns
 */
export const getCommentByBookId = function(bookId, currentPage, pageSize) {
  return request.get('/comment/book', {
    params: {
      bookId,
      currentPage,
      pageSize
    }
  });
}
