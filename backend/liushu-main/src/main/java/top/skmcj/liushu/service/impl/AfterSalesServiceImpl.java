package top.skmcj.liushu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.skmcj.liushu.dao.mapper.AfterSalesMapper;
import top.skmcj.liushu.entity.AfterSales;
import top.skmcj.liushu.service.AfterSalesService;

/**
 * 售后单据
 */
@Service
public class AfterSalesServiceImpl extends ServiceImpl<AfterSalesMapper, AfterSales> implements AfterSalesService {
}
