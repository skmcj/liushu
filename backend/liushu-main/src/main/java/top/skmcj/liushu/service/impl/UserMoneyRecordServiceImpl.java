package top.skmcj.liushu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.skmcj.liushu.dao.mapper.UserMoneyRecordMapper;
import top.skmcj.liushu.entity.UserMoneyRecord;
import top.skmcj.liushu.service.UserMoneyRecordService;

@Service
public class UserMoneyRecordServiceImpl extends ServiceImpl<UserMoneyRecordMapper, UserMoneyRecord> implements UserMoneyRecordService {
}
