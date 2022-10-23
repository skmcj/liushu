package top.skmcj.liushu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.skmcj.liushu.dao.mapper.BookstoreDetailMapper;
import top.skmcj.liushu.entity.BookstoreDetail;
import top.skmcj.liushu.service.BookstoreDetailService;

@Service
public class BookstoreDetailServiceImpl extends ServiceImpl<BookstoreDetailMapper, BookstoreDetail> implements BookstoreDetailService {
}
