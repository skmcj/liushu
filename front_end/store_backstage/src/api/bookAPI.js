import request from '@/utils/request';

export const getBooksAPI = function() {
  return request.get('/mockdata/books.json');
}
