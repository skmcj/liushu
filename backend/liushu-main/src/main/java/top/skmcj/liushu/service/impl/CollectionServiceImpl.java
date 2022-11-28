package top.skmcj.liushu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.skmcj.liushu.dao.mapper.CollectionMapper;
import top.skmcj.liushu.entity.Collection;
import top.skmcj.liushu.service.CollectionService;

@Service
public class CollectionServiceImpl extends ServiceImpl<CollectionMapper, Collection> implements CollectionService {
}
