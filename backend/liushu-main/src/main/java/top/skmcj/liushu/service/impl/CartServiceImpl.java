package top.skmcj.liushu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.skmcj.liushu.dao.mapper.CartMapper;
import top.skmcj.liushu.entity.Cart;
import top.skmcj.liushu.service.CartService;

@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {
}
