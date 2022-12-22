package top.skmcj.liushu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.skmcj.liushu.dao.mapper.BookstoreMapper;
import top.skmcj.liushu.entity.Bookstore;
import top.skmcj.liushu.service.BookstoreService;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookstoreServiceImpl extends ServiceImpl<BookstoreMapper, Bookstore> implements BookstoreService {

    /**
     * 随机获取商家
     * @param size
     * @return
     */
    @Override
    public List<Bookstore> getStoreByRandom(int size) {
        long total = this.count();
        List<Bookstore> stores = new ArrayList<>();
        SecureRandom random = new SecureRandom();
        // 将 size 分为 3 份，分 3 次获取
        int[] sizeArr = new int[]{ size / 3, size / 3, size - 2 * (size / 3) };

        for(int i = 0; i < sizeArr.length; i++) {
            LambdaQueryWrapper<Bookstore> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.last("LIMIT " + String.valueOf(random.nextInt((int)(total - sizeArr[i] + 1))) + ", " + sizeArr[i]);
            List<Bookstore> list = this.list(queryWrapper);
            list.stream().forEach(item -> {
                stores.add(item);
            });
        }
        return stores;
    }
}
