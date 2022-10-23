package top.skmcj.liushu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.skmcj.liushu.dao.mapper.BookstoreMapper;
import top.skmcj.liushu.entity.Bookstore;
import top.skmcj.liushu.service.BookstoreService;

@Service
public class BookstoreServiceImpl extends ServiceImpl<BookstoreMapper, Bookstore> implements BookstoreService {
}
