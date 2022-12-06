import request from '@/utils/request';

/**
 * 获取所有购物车信息
 * @returns
 */
export const getAllCartApi = function() {
  return request.get('/cart/all');
}

/**
 * 根据ID获取购物车项
 * @param {*} id
 * @returns
 */
export const getCartByIdApi = function(id) {
  return request.get('/cart', {
    params: {
      id
    }
  });
}

/**
 * 添加购物车项
 * @param {*} cartItem
 * @returns
 */
export const addCartItemApi = function(cartItem) {
  return request.post('/cart');
}

/**
 * 更新购物车信息
 * @param {*} carts
 * @returns
 */
export const updateCartMessApi = function(carts) {
  return request.put('/cart/update', carts);
}

/**
 * 批量删除购物车项
 * @param {*} ids
 * @returns
 */
export const deleteCartApi = function(ids) {
  return request.delete('/cart', {
    params: {
      ids
    }
  });
}
