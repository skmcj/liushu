package top.skmcj.liushu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.skmcj.liushu.dto.BookstoreDto;
import top.skmcj.liushu.entity.Bookstore;

import java.math.BigDecimal;
import java.util.List;

public interface BookstoreService extends IService<Bookstore> {

    List<Bookstore> getStoreByRandom(int size);

    BookstoreDto getStoreMessById(Long storeId);

    boolean addIncome(Long storeId, BigDecimal income);
}
