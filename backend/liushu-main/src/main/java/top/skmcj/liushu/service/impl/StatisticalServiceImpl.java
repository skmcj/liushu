package top.skmcj.liushu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.skmcj.liushu.dao.mapper.StatisticalMapper;
import top.skmcj.liushu.entity.Statistical;
import top.skmcj.liushu.service.StatisticalService;

@Service
public class StatisticalServiceImpl extends ServiceImpl<StatisticalMapper, Statistical> implements StatisticalService {
}
