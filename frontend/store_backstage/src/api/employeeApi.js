import request from '@/utils/request';

/**
 * 添加员工
 */
export const addEmployeeApi = function(employee) {
  return request.post('/employee', employee);
}

/**
 * 分页获取员工
 * @param {*} param
 * @returns
 */
export const getEmployeePageApi = function(param) {
  return request.get('/employee/page', {
    params: param
  });
}

/**
 * 根据id获取员工信息
 * @param {*} id
 * @returns
 */
export const getEmployeeByIdApi = function(id) {
  return request.get('/employee', {
    params: {
      id: id
    }
  });
}

/**
 * 修改用户信息
 * @param {*} employee
 * @returns
 */
export const editEmployeeApi = function(employee) {
  return request.put('/employee', employee);
}

/**
 * 修改用户状态
 * @param {*} employee
 * @returns
 */
export const editEmployeeStatusApi = function(employee) {
  return request.put('/employee', employee);
}

/**
 * 删除用户状态
 * @param {*} employee
 * @returns
 */
export const delEmployeeApi = function(id) {
  return request.delete('/employee', {
    params: {
      id: id
    }
  });
}
