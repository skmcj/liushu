package top.skmcj.liushu.controller.frontend;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.skmcj.liushu.common.Result;
import top.skmcj.liushu.dto.BookCardDto;
import top.skmcj.liushu.entity.Bookstore;
import top.skmcj.liushu.service.BookService;
import top.skmcj.liushu.service.BookstoreService;
import top.skmcj.liushu.util.CommonUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 全局搜索
 */
@RestController
@RequestMapping("/search")
public class FrontendSearchController {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookstoreService storeService;


    /**
     * 搜索图书
     * @param keyword
     * @return
     */
    @GetMapping("/book")
    public Result<Page<BookCardDto>> searchBookPage(@RequestParam String keyword, int currentPage, int pageSize,
                                                    HttpServletRequest request) {
        Page<BookCardDto> bookCardDtoPage = bookService.searchBookOfGlobal(keyword, currentPage, pageSize);
        String imgDoMain = CommonUtil.getImgDoMain(request);
        List<BookCardDto> bookCardDtos = bookCardDtoPage.getRecords();
        bookCardDtos.stream().forEach(item -> {
            item.setCoverUrl(imgDoMain + item.getCover());
        });
        return Result.success(bookCardDtoPage);
    }

    /**
     * 搜索商家
     * @param keyword
     * @param currentPage
     * @param pageSize
     * @param request
     * @return
     */
    @GetMapping("/shop")
    public Result<Page<Bookstore>> searchShopPage(@RequestParam String keyword, int currentPage, int pageSize,
                                                  HttpServletRequest request) {
        Page<Bookstore> storePage = storeService.searchStorePage(keyword, currentPage, pageSize);
        List<Bookstore> bookstores = storePage.getRecords();
        String coverDomain = CommonUtil.getImgDoMain(request);
        bookstores.stream().forEach(item -> {
            item.setCoverUrl(coverDomain + item.getCover());
        });
        return Result.success(storePage);
    }
}
