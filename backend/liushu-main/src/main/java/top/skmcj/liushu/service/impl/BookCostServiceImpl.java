package top.skmcj.liushu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.skmcj.liushu.dao.mapper.BookCostMapper;
import top.skmcj.liushu.entity.BookCost;
import top.skmcj.liushu.service.BookCostService;

@Service
public class BookCostServiceImpl extends ServiceImpl<BookCostMapper, BookCost> implements BookCostService {
}
