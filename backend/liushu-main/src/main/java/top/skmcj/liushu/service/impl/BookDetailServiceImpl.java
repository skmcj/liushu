package top.skmcj.liushu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.skmcj.liushu.dao.mapper.BookDetailMapper;
import top.skmcj.liushu.entity.BookDetail;
import top.skmcj.liushu.service.BookDetailService;

@Service
public class BookDetailServiceImpl extends ServiceImpl<BookDetailMapper, BookDetail> implements BookDetailService {
}
