import request from '@/utils/request.js';

export const getEmployeesAPI = function() {
  return request.get('/mockdata/employee.json');
}
