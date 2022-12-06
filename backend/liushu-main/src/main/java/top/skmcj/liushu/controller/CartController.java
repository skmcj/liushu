package top.skmcj.liushu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import top.skmcj.liushu.common.Result;
import top.skmcj.liushu.common.enums.StatusCodeEnum;
import top.skmcj.liushu.dto.CartDto;
import top.skmcj.liushu.entity.Cart;
import top.skmcj.liushu.entity.User;
import top.skmcj.liushu.service.BookService;
import top.skmcj.liushu.service.CartService;
import top.skmcj.liushu.util.CommonUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 获取用户的所有购物车信息
     * @param request
     * @return
     */
    @GetMapping("/all")
    public Result<List> getAllCart(HttpServletRequest request) throws Exception {
        String coverPrefix = CommonUtil.getImgDoMain(request);
        User user = CommonUtil.getTokenMessByUser(request);
        List<CartDto> carts = cartService.getCartAll(user.getId(), coverPrefix);
        return Result.success(carts, StatusCodeEnum.GET_OK);
    }

    /**
     * 根据ID获取购物车项
     * @param id
     * @return
     */
    @GetMapping
    public Result<CartDto> getCartById(Long id, HttpServletRequest request) {
        if(id == null) return Result.error("ID为空");
        String coverPrefix = CommonUtil.getImgDoMain(request);
        CartDto cartDto = cartService.getCartById(id, coverPrefix);
        if(cartDto == null) return Result.error("找不到对应的购物车信息");
        return Result.success(cartDto, StatusCodeEnum.GET_OK);
    }

    /**
     * 添加购物车项
     * @param cart
     * @param request
     * @return
     */
    @PostMapping
    public Result<String> addCart(@RequestBody Cart cart, HttpServletRequest request) throws Exception {
        User user = CommonUtil.getTokenMessByUser(request);
        if(cart.getBookId() == null || cart.getStoreId() == null) {
            return Result.error("购物车项信息不完整");
        }
        Cart sCart = new Cart();
        sCart.setUserId(user.getId());
        sCart.setBookId(cart.getBookId());
        sCart.setStoreId(cart.getStoreId());
        sCart.setBorrowTime(cart.getBorrowTime());
        sCart.setBorrowCost(cart.getBorrowCost());
        sCart.setPackingCost(cart.getPackingCost());
        sCart.setDeposit(cart.getDeposit());
        sCart.setAmount(cart.getAmount());
        sCart.setQuantity(cart.getQuantity());
        sCart.setChecked(cart.getChecked());
        boolean flag = cartService.save(sCart);
        if(!flag) return Result.error(StatusCodeEnum.ADD_ERR);
        return Result.success(StatusCodeEnum.ADD_OK);
    }

    /**
     * 更新购物车信息
     * @param carts
     * @param request
     * @return
     */
    @PutMapping("/update")
    public Result<String> updateCartMess(@RequestBody List<Cart> carts, HttpServletRequest request) throws Exception {
        /**
         * 1. 删除用户现有购物车项
         * 2. 添加传入的购物车信息列表
         */
        User user = CommonUtil.getTokenMessByUser(request);
        cartService.updateCart(carts, user.getId());
        return Result.success(StatusCodeEnum.UPDATE_OK);
    }

}
