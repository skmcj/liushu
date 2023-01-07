package top.skmcj.liushu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.skmcj.liushu.dao.mapper.UserInfoMapper;
import top.skmcj.liushu.entity.UserInfo;
import top.skmcj.liushu.service.UserInfoService;
import top.skmcj.liushu.util.BigDecimalUtil;

import java.math.BigDecimal;

@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    /**
     * 增加零钱
     * @param userId
     * @param inc
     * @return
     */
    @Override
    public boolean addMoney(Long userId, BigDecimal inc) {
        LambdaQueryWrapper<UserInfo> infoWrapper = new LambdaQueryWrapper<>();
        infoWrapper.eq(UserInfo::getUserId, userId);
        UserInfo info = this.getOne(infoWrapper);
        UserInfo sInfo = new UserInfo();
        sInfo.setId(info.getId());
        sInfo.setMoney(BigDecimalUtil.add(info.getMoney(), inc));
        boolean flag = this.updateById(sInfo);
        return flag;
    }
}
