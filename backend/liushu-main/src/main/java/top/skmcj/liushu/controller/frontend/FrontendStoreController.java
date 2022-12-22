package top.skmcj.liushu.controller.frontend;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.skmcj.liushu.common.Result;
import top.skmcj.liushu.entity.Book;
import top.skmcj.liushu.entity.Bookstore;
import top.skmcj.liushu.service.BookService;
import top.skmcj.liushu.service.BookstoreService;
import top.skmcj.liushu.util.CommonUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
        return Result.success(bookstore);
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
        queryWrapper.orderByDesc(Book::getTba);
        queryWrapper.orderByDesc(Book::getMba);
        queryWrapper.orderByDesc(Book::getUpdateTime);
        queryWrapper.last("LIMIT " + String.valueOf(size));
        List<Book> books = bookService.list(queryWrapper);
        return Result.success(books);
    }
}
