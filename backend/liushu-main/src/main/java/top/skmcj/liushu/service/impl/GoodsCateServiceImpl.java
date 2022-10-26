package top.skmcj.liushu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.skmcj.liushu.dao.mapper.GoodsCateMapper;
import top.skmcj.liushu.entity.GoodsCate;
import top.skmcj.liushu.service.GoodsCateService;

@Service
public class GoodsCateServiceImpl extends ServiceImpl<GoodsCateMapper, GoodsCate> implements GoodsCateService {
}
