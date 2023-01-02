package top.skmcj.liushu.controller.frontend;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.skmcj.liushu.common.Result;
import top.skmcj.liushu.dto.BookCardDto;
import top.skmcj.liushu.dto.BookstoreDto;
import top.skmcj.liushu.entity.Book;
import top.skmcj.liushu.entity.Bookstore;
import top.skmcj.liushu.entity.BookstoreDetail;
import top.skmcj.liushu.service.BookService;
import top.skmcj.liushu.service.BookstoreService;
import top.skmcj.liushu.util.CommonUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/frontend/store")
public class FrontendStoreController {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookstoreService storeService;

    /**
     * TODO: 商家推荐接口
     * 由于当前数据库商家数量较少，没有必要开发推荐接口
     * 当商家数量达到一定数量级时，实现推荐接口
     */

    /**
     * 获取推荐商家
     * 随机获取
     * @param size
     * @return
     */
    @GetMapping
    public Result<List<Bookstore>> getStoreRecommend(int size, HttpServletRequest request) {
        List<Bookstore> randomStores = storeService.getStoreByRandom(size);
        String coverDomain = CommonUtil.getImgDoMain(request);
        randomStores.stream().forEach(item -> {
            item.setCoverUrl(coverDomain + item.getCover());
        });
        return Result.success(randomStores);
    }

    /**
     * 获取商家基本信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Bookstore> getStoreById(@PathVariable Long id, HttpServletRequest request) {
        Bookstore bookstore = new Bookstore();
        Bookstore store = storeService.getById(id);
        bookstore.setId(store.getId());
        bookstore.setStoreName(store.getStoreName());
        bookstore.setCover(store.getCover());
        bookstore.setCoverUrl(CommonUtil.getImgDoMain(request) + store.getCover());
        bookstore.setScore(store.getScore());
        bookstore.setMba(store.getMba());
        bookstore.setLabel(store.getLabel());
        bookstore.setDeliveryFee(store.getDeliveryFee());
        bookstore.setBorrowDay(store.getBorrowDay());
        return Result.success(bookstore);
    }

    /**
     * 获取商家完整信息
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/mess")
    public Result<BookstoreDto> getStoreMessById(Long id, HttpServletRequest request) {
        BookstoreDto storeDto = storeService.getStoreMessById(id);
        String imgDoMain = CommonUtil.getImgDoMain(request);
        storeDto.getBookstore().setCoverUrl(imgDoMain + storeDto.getBookstore().getCover());
        BookstoreDetail detail = storeDto.getBookstoreDetail();
        // 给相关图片添加协议前缀
        List<String> businessLicense = detail.getBusinessLicenseList();
        List<String> licenseImg = detail.getLicenseImgList();
        List<String> envImgs = detail.getEnvImgsList();
        List<String> businessLicenseUrl = businessLicense.stream().map(item -> {
            item = imgDoMain + item;
            return item;
        }).collect(Collectors.toList());
        List<String> licenseImgUrl = licenseImg.stream().map(item -> {
            item = imgDoMain + item;
            return item;
        }).collect(Collectors.toList());
        List<String> envImgsUrl = envImgs.stream().map(item -> {
            item = imgDoMain + item;
            return item;
        }).collect(Collectors.toList());
        detail.setBusinessLicenseUrl(JSON.toJSONString(businessLicenseUrl));
        detail.setLicenseImgUrl(JSON.toJSONString(licenseImgUrl));
        detail.setEnvImgsUrl(JSON.toJSONString(envImgsUrl));
        return Result.success(storeDto);
    }

    /**
     * 获取店内图书排行榜
     * @param size
     * @param request
     * @return
     */
    @GetMapping("/rank")
    public Result<List<Book>> getBookRank(Long storeId, int size, HttpServletRequest request) {
        LambdaQueryWrapper<Book> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Book::getStoreId, storeId);
        queryWrapper.eq(Book::getStatus, 1);
        queryWrapper.orderByDesc(Book::getTba);
        queryWrapper.orderByDesc(Book::getMba);
        queryWrapper.orderByDesc(Book::getUpdateTime);
        queryWrapper.last("LIMIT " + String.valueOf(size));
        List<Book> books = bookService.list(queryWrapper);
        return Result.success(books);
    }

    /**
     * 获取商家图书
     * @param storeId
     * @param currentPage
     * @param pageSize
     * @param request
     * @return
     */
    @GetMapping("/book/page")
    public Result<Page<BookCardDto>> getBookPageOfStore(Long storeId, int currentPage, int pageSize,
                                                        HttpServletRequest request) {
        Page<BookCardDto> bookCardPage = bookService.getBookCardOfStore(storeId, currentPage, pageSize);
        String imgDoMain = CommonUtil.getImgDoMain(request);
        List<BookCardDto> bookCards = bookCardPage.getRecords();
        bookCards.stream().forEach(item -> {
            item.setCoverUrl(imgDoMain + item.getCover());
        });
        return Result.success(bookCardPage);
    }

    /**
     * 根据类别Id分页获取店内图书
     * @param storeId
     * @param cateId
     * @param currentPage
     * @param pageSize
     * @return
     */
    @GetMapping("/book/cate")
    public Result<Page<BookCardDto>> getBookPageByCateOfStore(Long storeId, Long cateId, int currentPage,
                                                              int pageSize, HttpServletRequest request) {
        Page<BookCardDto> bookCardPage = bookService.getBookCardByCateOfStore(storeId, cateId, currentPage,
                pageSize);
        String imgDoMain = CommonUtil.getImgDoMain(request);
        List<BookCardDto> bookCards = bookCardPage.getRecords();
        bookCards.stream().forEach(item -> {
            item.setCoverUrl(imgDoMain + item.getCover());
        });
        return Result.success(bookCardPage);
    }

    /**
     * 搜索店内图书
     * @param storeId
     * @param text
     * @param currentPage
     * @param pageSize
     * @param request
     * @return
     */
    @GetMapping("/book/search")
    public Result<Page<BookCardDto>> getBookPageBySearchOfStore(Long storeId, String text, int currentPage,
                                                                int pageSize, HttpServletRequest request) {
        Page<BookCardDto> bookCardPage = bookService.getBookPageBySearchOfStore(storeId, text, currentPage
                , pageSize);
        List<BookCardDto> bookCards = bookCardPage.getRecords();
        String imgDoMain = CommonUtil.getImgDoMain(request);
        bookCards.stream().forEach(item -> {
            item.setCoverUrl(imgDoMain + item.getCover());
        });
        return Result.success(bookCardPage);
    }
}
