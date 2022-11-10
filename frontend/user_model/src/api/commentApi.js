import request from '@/utils/request';

/**
 * 根据图书ID获取评论
 * @param {*} bookId
 * @returns
 */
export const getCommentByBookId = function(bookId) {
  return request.get('/data/comment.json');
}
