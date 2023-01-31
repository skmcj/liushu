import request from '@/utils/request';

/**
 * 根据图书ID获取评论
 * @param {String} bookId 图书ID
 * @param {Number} currentPage 页码
 * @param {Number} pageSize 每页数量
 * @returns
 */
export const getCommentByBookIdApi = function(bookId, currentPage, pageSize) {
  return request.get('/comment/book', {
    params: {
      bookId,
      currentPage,
      pageSize
    }
  });
}

/**
 * 分页获取用户评价
 * @param {*} currentPage 页码
 * @param {*} pageSize 每页数量
 * @returns
 */
export const getCommentPageByUserApi = function(currentPage, pageSize) {
  return request.get('/comment/user', {
    params: {
      currentPage,
      pageSize
    }
  });
}

/**
 * 添加评论
 * @param {*} comment 评论内容
 * @returns
 */
export const addCommentOfOrderItemApi = function(comment) {
  return request.post('/comment', comment);
}

/**
 * 根据ID删除评论
 * @param {*} commentId 评论ID
 * @returns
 */
export const deleteCommentApi = function(commentId) {
  return request.delete('/comment', {
    params: {
      id: commentId
    }
  });
}

/**
 * 商家回复
 * @param {*} reply 回复
 * @returns
 */
export const replyComentByStoreApi = function(reply) {
  return request.post('/comment/store/reply', reply);
}
