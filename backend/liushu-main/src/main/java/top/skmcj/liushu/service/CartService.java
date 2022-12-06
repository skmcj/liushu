package top.skmcj.liushu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import top.skmcj.liushu.dto.CartDto;
import top.skmcj.liushu.entity.Cart;

import java.util.List;

public interface CartService extends IService<Cart> {

    /**
     * 更新用户购物车信息
     * @param carts
     * @param userId
     */
    void updateCart(List<Cart> carts, Long userId);

    /**
     * 根据ID获取用户购物车项
     * @param id
     * @param coverPrefix
     * @return
     */
    @Transactional
    CartDto getCartById(Long id, String coverPrefix);

    /**
     * 获取用户所有购物车信息
     * @param userId
     * @param coverPrefix
     * @return
     */
    @Transactional
    List<CartDto> getCartAll(Long userId, String coverPrefix);

    /**
     * 批量删除购物车项
     * @param ids
     * @return
     */
    @Transactional
    boolean deleteCart(List<Long> ids);
}
