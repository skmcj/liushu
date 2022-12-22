package top.skmcj.liushu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.skmcj.liushu.dao.mapper.UserPreferenceMapper;
import top.skmcj.liushu.entity.UserPreference;
import top.skmcj.liushu.service.UserPreferenceService;

import java.time.LocalDateTime;

@Service
public class UserPreferenceServiceImpl extends ServiceImpl<UserPreferenceMapper, UserPreference> implements UserPreferenceService {

    /**
     * 添加喜好度记录
     * 有则修改，无则添加
     * @param userId
     * @param bookId
     * @param score
     * @return
     */
    @Override
    public boolean setPreference(Long userId, Long bookId, Float score) {
        UserPreference preference = new UserPreference();
        boolean flag = false;
        LambdaQueryWrapper<UserPreference> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserPreference::getUserId, userId);
        queryWrapper.eq(UserPreference::getBookId, bookId);
        UserPreference oldPreference = this.getOne(queryWrapper);
        if(oldPreference != null) {
            preference.setId(oldPreference.getId());
            preference.setScore(oldPreference.getScore() + score);
            preference.setTime(LocalDateTime.now());
            flag = this.updateById(preference);
        } else {
            preference.setUserId(userId);
            preference.setBookId(bookId);
            preference.setScore(score);
            preference.setTime(LocalDateTime.now());
            flag = this.save(preference);
        }
        return flag;
    }
}
