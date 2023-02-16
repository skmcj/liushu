package top.skmcj.liushu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.skmcj.liushu.dao.mapper.BookstoreMapper;
import top.skmcj.liushu.dto.BookstoreDto;
import top.skmcj.liushu.entity.BookDetail;
import top.skmcj.liushu.entity.Bookstore;
import top.skmcj.liushu.entity.BookstoreDetail;
import top.skmcj.liushu.service.BookDetailService;
import top.skmcj.liushu.service.BookstoreDetailService;
import top.skmcj.liushu.service.BookstoreService;
import top.skmcj.liushu.util.BigDecimalUtil;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookstoreServiceImpl extends ServiceImpl<BookstoreMapper, Bookstore> implements BookstoreService {

    @Autowired
    private BookstoreDetailService detailService;

    /**
     * 流书网订单服务费百分比
     */
    @Value("${liushu.account.service-fee}")
    private BigDecimal serviceFee;

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
            queryWrapper.eq(Bookstore::getStatus, 1);
            queryWrapper.eq(Bookstore::getAuditStatus, 2);
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
        targetStore.setDeliveryFee(store.getDeliveryFee());
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

    /**
     * 商家获取收入
     * @param storeId
     * @param income
     * @return
     */
    @Override
    public boolean addIncome(Long storeId, BigDecimal income) {
        Bookstore store = this.getById(storeId);
        Bookstore bookstore = new Bookstore();
        bookstore.setId(store.getId());
        // 平台抽取服务费
        BigDecimal serviceCost = BigDecimalUtil.multiply(income, serviceFee);
        // 真实收入
        BigDecimal rIncome = BigDecimalUtil.subtract(income, serviceCost);
        bookstore.setIncome(BigDecimalUtil.add(store.getIncome(), rIncome));
        boolean flag = this.updateById(bookstore);
        return flag;
    }

    /**
     * 搜索商家
     * @param keyword
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public Page<Bookstore> searchStorePage(String keyword, int currentPage, int pageSize) {
        Page<Bookstore> page = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<Bookstore> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(keyword), Bookstore::getStoreName, keyword);
        this.page(page, wrapper);
        return page;
    }
}
