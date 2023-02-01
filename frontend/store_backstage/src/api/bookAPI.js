import request from '@/utils/request';

/**
 * 获取图书类别
 */
export const getBookCateApi = function() {
  return request.get('/book/cate');
}

/**
 * 获取图书信息
 * @returns {Promise} 返回响应
 */
export const getBookPageApi = function(param) {
  return request.get('/book/page', {
    params: param
  });
}

/**
 * 根据id获取图书信息
 * @param {*} id
 * @returns
 */
export const getBookByIdApi = function(id) {
  return request.get('/book', {
    params: {
      id: id
    }
  });
}

/**
 * 新增图书
 * @param {Object} bookData
 * @returns
 */
export const saveBookApi = function(bookData) {
  return request.post('/book', bookData);
}

/**
 * 修改图书
 * @param {*} bookData
 */
export const editBookApi = function(bookData) {
  return request.put('/book', bookData);
}

/**
 * 修改图书状态
 * @param {*} status 将修改的状态
 * @param {*} ids id列表
 * @returns
 */
export const editBookStatusApi = function(status, ids) {
  return request.post(`/book/status/${status}`, ids);
}

/**
 * 删除指定id列表里的图书
 * @param {*} ids
 * @returns
 */
export const deleteBookApi = function(ids) {
  return request.delete('/book', {
    data: ids
  });
}
