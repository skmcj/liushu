import request from '@/utils/request';

/**
 * 获取图书类别
 */
export const getBookCateAPI = function() {
  return request.get('/mockdata/book_cate.json');
}

/**
 * 获取图书信息
 * @returns {Promise} 返回响应
 */
export const getBooksAPI = function() {
  return request.get('/mockdata/books.json');
}
