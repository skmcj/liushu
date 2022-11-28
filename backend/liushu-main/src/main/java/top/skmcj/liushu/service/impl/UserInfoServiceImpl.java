package top.skmcj.liushu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.skmcj.liushu.dao.mapper.UserInfoMapper;
import top.skmcj.liushu.entity.UserInfo;
import top.skmcj.liushu.service.UserInfoService;

@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
}
