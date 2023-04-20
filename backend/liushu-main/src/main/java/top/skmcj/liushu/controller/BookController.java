package top.skmcj.liushu.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.skmcj.liushu.common.GlobalData;
import top.skmcj.liushu.common.Result;
import top.skmcj.liushu.common.enums.StatusCodeEnum;
import top.skmcj.liushu.dto.BookDto;
import top.skmcj.liushu.entity.Book;
import top.skmcj.liushu.entity.BookCate;
import top.skmcj.liushu.entity.Employee;
import top.skmcj.liushu.service.BookCateService;
import top.skmcj.liushu.service.BookService;
import top.skmcj.liushu.util.CommonUtil;
import top.skmcj.liushu.util.JwtUtil;
import top.skmcj.liushu.vo.BookPageVo;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 图书相关接口
 */
@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookCateService bookCateService;

    @Autowired
    private BookService bookService;

    /**
     * 获取图书类别列表
     * @return
     */
    @GetMapping("/cate")
    public Result<List<BookCate>> getBookCate() {
        List<BookCate> list = bookCateService.list();
        return Result.success(list);
    }

    /**
     * 保存图书
     * @param bookDto
     * @return
     */
    @PostMapping
    public Result<String> saveBook(@RequestBody BookDto bookDto, HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        // 当前登录用户token信息
        Employee lEmployee = JwtUtil.verifyTokenOfEmployee(token);
        if(lEmployee.getCompetence() > 1) {
            return Result.success(StatusCodeEnum.NO_ACCESS);
        }
        // System.out.println("bookDto => " + bookDto);
        bookService.saveBook(bookDto, lEmployee.getStoreId(), lEmployee.getId());
        return Result.success("图书新增成功");
    }

    /**
     * 修改图书
     * @param bookDto
     * @return
     */
    @PutMapping
    public Result<String> updateBook(@RequestBody BookDto bookDto, HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        // 当前登录用户token信息
        Employee lEmployee = JwtUtil.verifyTokenOfEmployee(token);
        if(lEmployee.getCompetence() > 1) {
            return Result.success(StatusCodeEnum.NO_ACCESS);
        }
        bookService.editBook(bookDto);
        return Result.success("图书修改成功");
    }

    /**
     * 根据id获取图书完整信息
     * @param id
     * @return
     */
    @GetMapping
    public Result<BookDto> getBookById(Long id, HttpServletRequest request) {
        BookDto bookDto = bookService.getBookById(id);
        // 设置图片链接
        Book book = bookDto.getBook();
        String url = CommonUtil.getImgDoMain(request) + book.getCover();
        book.setCoverUrl(url);
        bookDto.setBook(book);
        return Result.success(bookDto);
    }



    /**
     * 分页获取图书信息
     * @param bookPageVo
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/page")
    public Result<Page> getBookPage(BookPageVo bookPageVo, HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        // 当前登录用户token信息
        Employee lEmployee = JwtUtil.verifyTokenOfEmployee(token);

        Page bookPage = bookService.getBookPage(bookPageVo, lEmployee.getStoreId());
        List<Book> books = bookPage.getRecords();
        String imgDoMain = CommonUtil.getImgDoMain(request);
        for (int i = 0; i < books.size(); i++) {
            Book sBook = books.get(i);
            sBook.setCoverUrl(imgDoMain + sBook.getCover());
        }
        bookPage.setRecords(books);
        return Result.success(bookPage);
    }

    /**
     * 修改图书状态
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public Result<String> updateStatus(@PathVariable int status, @RequestBody List<Long> ids, HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        // 当前登录用户token信息
        Employee lEmployee = JwtUtil.verifyTokenOfEmployee(token);
        if(lEmployee.getCompetence() > 1) {
            return Result.success(StatusCodeEnum.NO_ACCESS);
        }
        bookService.editBookStatus(ids, status);
        return Result.success("图书状态修改成功");
    }

    /**
     * 批量删除图书
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result<String> deleteBook(@RequestBody List<Long> ids, HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        // 当前登录用户token信息
        Employee lEmployee = JwtUtil.verifyTokenOfEmployee(token);
        if(lEmployee.getCompetence() > 1) {
            return Result.success(StatusCodeEnum.NO_ACCESS);
        }
        bookService.deleteBook(ids);
        return Result.success("图书删除成功");
    }
}
