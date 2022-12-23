package top.skmcj.liushu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.skmcj.liushu.dao.mapper.BookstoreMapper;
import top.skmcj.liushu.dto.BookstoreDto;
import top.skmcj.liushu.entity.BookDetail;
import top.skmcj.liushu.entity.Bookstore;
import top.skmcj.liushu.entity.BookstoreDetail;
import top.skmcj.liushu.service.BookDetailService;
import top.skmcj.liushu.service.BookstoreDetailService;
import top.skmcj.liushu.service.BookstoreService;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookstoreServiceImpl extends ServiceImpl<BookstoreMapper, Bookstore> implements BookstoreService {

    @Autowired
    private BookstoreDetailService detailService;

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

    /**
     * 获取图书详细信息
     * @param storeId
     * @return
     */
    @Override
    public BookstoreDto getStoreMessById(Long storeId) {
        BookstoreDto bookstoreDto = new BookstoreDto();
        Bookstore targetStore = new Bookstore();
        BookstoreDetail targetDetail = new BookstoreDetail();
        // 获取商家基本信息
        Bookstore store = this.getById(storeId);
        targetStore.setId(store.getId());
        targetStore.setEmail(store.getEmail());
        targetStore.setStoreName(store.getStoreName());
        targetStore.setCover(store.getCover());
        targetStore.setCoverUrl(store.getCoverUrl());
        targetStore.setScore(store.getScore());
        targetStore.setAddress(store.getAddress());
        targetStore.setBusinessHours(store.getBusinessHours());
        targetStore.setDistribution(store.getDistribution());
        targetStore.setDeliverFee(store.getDeliverFee());
        targetStore.setBorrowDay(store.getBorrowDay());
        targetStore.setRenewDay(store.getRenewDay());
        targetStore.setStoreService(store.getStoreService());
        targetStore.setLabel(store.getLabel());
        targetStore.setNotice(store.getNotice());
        targetStore.setMba(store.getMba());
        bookstoreDto.setBookstore(targetStore);
        // 获取商家详细信息
        LambdaQueryWrapper<BookstoreDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BookstoreDetail::getStoreId, storeId);
        BookstoreDetail detailStore = detailService.getOne(queryWrapper);
        targetDetail.setId(detailStore.getId());
        targetDetail.setStoreId(detailStore.getStoreId());
        targetDetail.setOwnerName(detailStore.getOwnerName());
        targetDetail.setPhone(detailStore.getPhone());
        targetDetail.setBusinessLicense(detailStore.getBusinessLicense());
        targetDetail.setLicenseImg(detailStore.getLicenseImg());
        targetDetail.setEnvImgs(detailStore.getEnvImgs());
        bookstoreDto.setBookstoreDetail(targetDetail);
        return bookstoreDto;
    }
}
