package top.skmcj.liushu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.skmcj.liushu.dao.mapper.CartMapper;
import top.skmcj.liushu.dto.CartDto;
import top.skmcj.liushu.entity.Book;
import top.skmcj.liushu.entity.BookDetail;
import top.skmcj.liushu.entity.Bookstore;
import top.skmcj.liushu.entity.Cart;
import top.skmcj.liushu.service.BookDetailService;
import top.skmcj.liushu.service.BookService;
import top.skmcj.liushu.service.BookstoreService;
import top.skmcj.liushu.service.CartService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookDetailService detailService;

    @Autowired
    private BookstoreService storeService;

    /**
     * 更新购物车信息
     * @param carts
     * @param userId
     */
    @Override
    @Transactional
    public void updateCart(List<Cart> carts, Long userId) {
        LambdaQueryWrapper<Cart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Cart::getUserId, userId);
        this.remove(queryWrapper);
        List<Cart> newCarts = carts.stream().map(item -> {
            item.setId(null);
            item.setCreateTime(null);
            item.setUpdateTime(null);
            item.setUserId(userId);
            return item;
        }).collect(Collectors.toList());
        this.saveBatch(newCarts);
    }

    /**
     * 根据ID获取用户购物车项
     * @param id
     * @return
     */
    @Override
    @Transactional
    public CartDto getCartById(Long id, String coverPrefix) {
        Cart cartItem = this.getById(id);
        if(cartItem == null) return null;
        Book book = bookService.getById(cartItem.getBookId());
        LambdaQueryWrapper<BookDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BookDetail::getBookId, cartItem.getBookId());
        BookDetail detail = detailService.getOne(queryWrapper);
        Bookstore store = storeService.getById(cartItem.getStoreId());
        CartDto cartDto = new CartDto();
        cartDto.setStoreName(store.getStoreName());
        cartDto.setBookName(book.getName());
        String bookCover = coverPrefix + book.getCover();
        cartDto.setBookCover(bookCover);
        cartDto.setBookOutline(detail.getOutline());
        cartDto.setInventory(book.getInventory());
        cartDto.setId(cartItem.getId());
        cartDto.setUserId(cartDto.getUserId());
        cartDto.setBookId(cartItem.getBookId());
        cartDto.setStoreId(cartItem.getStoreId());
        cartDto.setBorrowTime(cartItem.getBorrowTime());
        cartDto.setBorrowCost(cartItem.getBorrowCost());
        cartDto.setPackingCost(cartItem.getPackingCost());
        cartDto.setDeposit(cartItem.getDeposit());
        cartDto.setAmount(cartItem.getAmount());
        cartDto.setQuantity(cartItem.getQuantity());
        cartDto.setChecked(cartItem.getChecked());
        cartDto.setCreateTime(cartItem.getCreateTime());
        cartDto.setUpdateTime(cartItem.getUpdateTime());
        return cartDto;
    }

    /**
     * 获取用户所有购物车信息
     * @param userId
     * @param coverPrefix
     * @return
     */
    @Transactional
    @Override
    public List<CartDto> getCartAll(Long userId, String coverPrefix) {
        LambdaQueryWrapper<Cart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Cart::getUserId, userId);
        List<Cart> carts = this.list(queryWrapper);
        Map<Long, String> storeMap = new HashMap<>();
        List<CartDto> cartDtos = carts.stream().map(item -> {
            CartDto cartDto = new CartDto();
            Book book = bookService.getById(item.getBookId());
            LambdaQueryWrapper<BookDetail> detailQueryWrapper = new LambdaQueryWrapper<>();
            detailQueryWrapper.eq(BookDetail::getBookId, item.getBookId());
            BookDetail detail = detailService.getOne(detailQueryWrapper);
            // 设置商家名称
            if(storeMap.containsKey(item.getStoreId())) {
                // 集合存在商家名，直接设置
                String storeName = storeMap.get(item.getStoreId());
                cartDto.setStoreName(storeName);
            } else {
                // 不存在商家名称，请求后设置
                Bookstore store = storeService.getById(item.getStoreId());
                cartDto.setStoreName(store.getStoreName());
                storeMap.put(item.getStoreId(), store.getStoreName());
            }
            cartDto.setBookName(book.getName());
            String bookCover = coverPrefix + book.getCover();
            cartDto.setBookCover(bookCover);
            cartDto.setBookOutline(detail.getOutline());
            cartDto.setInventory(book.getInventory());
            cartDto.setId(item.getId());
            cartDto.setUserId(userId);
            cartDto.setBookId(item.getBookId());
            cartDto.setStoreId(item.getStoreId());
            cartDto.setBorrowTime(item.getBorrowTime());
            cartDto.setBorrowCost(item.getBorrowCost());
            cartDto.setPackingCost(item.getPackingCost());
            cartDto.setDeposit(item.getDeposit());
            cartDto.setAmount(item.getAmount());
            cartDto.setQuantity(item.getQuantity());
            cartDto.setChecked(item.getChecked());
            cartDto.setCreateTime(item.getCreateTime());
            cartDto.setUpdateTime(item.getUpdateTime());
            return cartDto;
        }).collect(Collectors.toList());
        return cartDtos;
    }

    /**
     * 批量删除购物车项
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public boolean deleteCart(List<Long> ids) {
        return this.removeByIds(ids);
    }
}
