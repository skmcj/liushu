package top.skmcj.liushu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import top.skmcj.liushu.dto.BookDto;
import top.skmcj.liushu.entity.Book;
import top.skmcj.liushu.vo.BookPageVo;

import java.util.List;

public interface BookService extends IService<Book> {

    void saveBook(BookDto bookDto, Long storeId, Long employeeId);

    void editBook(BookDto bookDto);

    void editBookStatus(List<Long> ids, int status);

    void deleteBook(List<Long> ids);

    BookDto getBookById(Long id);

    Page getBookPage(BookPageVo bookPageVo, Long storeId);

}
