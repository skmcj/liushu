package top.skmcj.liushu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.skmcj.liushu.entity.UserPreference;

public interface UserPreferenceService extends IService<UserPreference> {

    boolean setPreference(Long userId, Long bookId, Float score);
}
