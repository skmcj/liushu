package top.skmcj.liushu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.skmcj.liushu.entity.Bookstore;

import java.util.List;

public interface BookstoreService extends IService<Bookstore> {

    List<Bookstore> getStoreByRandom(int size);
}
