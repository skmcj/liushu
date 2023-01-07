package top.skmcj.liushu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.skmcj.liushu.entity.UserInfo;

import java.math.BigDecimal;

public interface UserInfoService extends IService<UserInfo> {

    boolean addMoney(Long userId, BigDecimal inc);
}
