package top.skmcj.liushu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.skmcj.liushu.dao.mapper.BookCateMapper;
import top.skmcj.liushu.entity.BookCate;
import top.skmcj.liushu.service.BookCateService;

@Service
public class BookCateServiceImpl extends ServiceImpl<BookCateMapper, BookCate> implements BookCateService {
}
