package top.skmcj.liushu.controller.frontend;

import lombok.extern.slf4j.Slf4j;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.skmcj.liushu.common.GlobalData;
import top.skmcj.liushu.common.Result;
import top.skmcj.liushu.dto.BookDto;
import top.skmcj.liushu.entity.Book;
import top.skmcj.liushu.entity.User;
import top.skmcj.liushu.service.BookService;
import top.skmcj.liushu.service.UserPreferenceService;
import top.skmcj.liushu.util.CommonUtil;
import top.skmcj.liushu.util.DataHandleUtil;
import top.skmcj.liushu.util.GlobalDataUtil;
import top.skmcj.liushu.util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 前端图书接口
 */
@Slf4j
@RestController
@RequestMapping("/frontend/book")
public class FrontendBookController {

    @Value("${liushu.preference.click}")
    private float clickScore;

    @Value("${liushu.mahout.user.lately-num}")
    private int latelyNum;

    @Value("${liushu.mahout.user.tourist-id}")
    private Long touristId;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserPreferenceService preferenceService;

    /**
     * 根据id获取图书完整信息
     * @param id
     * @return
     */
    @GetMapping
    public Result<BookDto> getBookById(Long id, HttpServletRequest request) throws Exception {
        Long userId;
        String token = request.getHeader("Authorization");
        if(token != null && token.length() > 0) {
            // 用户已登录
            User user = JwtUtil.verifyTokenOfUser(token);
            userId = user.getId();
        } else {
            // 游客点击
            userId = touristId;
        }
        HttpSession session = request.getSession();
        List<Long> recentBooks = (List<Long>) session.getAttribute("recentBooks");
        if(recentBooks == null) {
            recentBooks = new ArrayList<>();
        }
        DataHandleUtil.addToLimitedList(recentBooks, id, latelyNum);
        // 将点击ID存入session中
        session.setAttribute("recentBooks", recentBooks);
        // 添加/更新喜好度记录
        preferenceService.setPreference(userId, id, clickScore);
        // 获取图书详情
        BookDto bookDto = bookService.getBookById(id);
        // 设置图片链接
        Book book = bookDto.getBook();
        String url = CommonUtil.getImgDoMain(request) + book.getCover();
        book.setCoverUrl(url);
        bookDto.setBook(book);
        return Result.success(bookDto);
    }

    /**
     * 获取推荐图书
     * BUG 当用户无操作重复推荐时，可能推荐重复结果
     * @param size 获取条数
     * @param request
     * @return
     */
    @GetMapping("/recommend")
    public Result<List<Book>> getBookByRecommend(int size, HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        List<Book> books = new ArrayList<>();
        System.out.println("token ==>" + token);
        if(token != null && token.length() > 0) {
            // 已登录，推荐 + 随机
            User user = JwtUtil.verifyTokenOfUser(token);
            Long userId = user.getId();
            HttpSession session = request.getSession();
            // 存储推荐结果ID
            List<Long> recommendIds = new ArrayList<>();
            // 获取最近点击的几本图书
            List<Long> recentBooks = (List<Long>) session.getAttribute("recentBooks");
            if(recentBooks != null) {
                for (int i = 0; i < recentBooks.size(); i++) {
                    List<RecommendedItem> recommendedItems =
                            GlobalData.getItemRecommender().recommendedBecause(userId, recentBooks.get(i), 2);
                    for (RecommendedItem item : recommendedItems) {
                        log.info("推荐图书 ==> " + item.getItemID());
                        recommendIds.add(item.getItemID());
                    }
                }
            }
            // 获取推荐图书数据
            if(recommendIds != null && recommendIds.size() > 0) {
                List<Book> recommendBooks = bookService.getBookByIds(recommendIds);
                recommendBooks.stream().forEach(item -> {
                    books.add(item);
                });
            }
            int randomSize = size - books.size();
            if(randomSize > 0) {
                List<Book> randomBooks = bookService.getBookByRandom(randomSize);
                randomBooks.stream().forEach(item -> {
                    books.add(item);
                });
            }
        } else {
            // 未登录，随机
            List<Book> randomBooks = bookService.getBookByRandom(size);
            randomBooks.stream().forEach(item -> {
                books.add(item);
            });
        }
        String coverDomain = CommonUtil.getImgDoMain(request);
        books.stream().forEach(item -> {
            item.setCoverUrl(coverDomain + item.getCover());
        });
        return Result.success(books);
    }

    @GetMapping("/cate")
    public Result<List<Book>> getBookByCate(int size, Long cateId, HttpServletRequest request) {
        List<Book> books = bookService.getBookByRandomOfType(size, cateId);
        String coverDomain = CommonUtil.getImgDoMain(request);
        books.stream().forEach(item -> {
            item.setCoverUrl(coverDomain + item.getCover());
        });
        return Result.success(books);
    }
}
