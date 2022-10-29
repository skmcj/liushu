package top.skmcj.liushu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.skmcj.liushu.common.exception.BusinessException;
import top.skmcj.liushu.dao.mapper.BookMapper;
import top.skmcj.liushu.dto.BookDto;
import top.skmcj.liushu.entity.Book;
import top.skmcj.liushu.entity.BookCost;
import top.skmcj.liushu.entity.BookDetail;
import top.skmcj.liushu.entity.BookInfo;
import top.skmcj.liushu.service.BookCostService;
import top.skmcj.liushu.service.BookDetailService;
import top.skmcj.liushu.service.BookInfoService;
import top.skmcj.liushu.service.BookService;
import top.skmcj.liushu.vo.BookPageVo;

import java.util.List;

@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    @Autowired
    private BookInfoService bookInfoService;

    @Autowired
    private BookDetailService bookDetailService;

    @Autowired
    private BookCostService bookCostService;

    /**
     * 新增图书
     * @param bookDto
     * @param storeId 商家id
     * @param employeeId 创建者id
     * @return
     */
    @Override
    @Transactional
    public void saveBook(BookDto bookDto, Long storeId, Long employeeId) {
        // 获取值
        Book book = bookDto.getBook();
        book.setStoreId(storeId);
        BookInfo bookInfo = bookDto.getBookInfo();
        BookDetail bookDetail = bookDto.getBookDetail();
        BookCost bookCost = bookDto.getBookCost();
        // 包装数据
        book.setCreateUser(employeeId);
        book.setUpdateUser(employeeId);
        // 新增图书记录
        this.save(book);
        // 获取图书记录ID
        Long bookId = book.getId();
        bookInfo.setBookId(bookId);
        bookDetail.setBookId(bookId);
        bookCost.setBookId(bookId);
        // 增加图书其它信息
        bookInfoService.save(bookInfo);
        bookDetailService.save(bookDetail);
        bookCostService.save(bookCost);
    }

    /**
     * 修改图书
     * @param bookDto
     */
    @Override
    @Transactional
    public void editBook(BookDto bookDto) {
        Book book = bookDto.getBook();
        BookInfo bookInfo = bookDto.getBookInfo();
        BookDetail bookDetail = bookDto.getBookDetail();
        BookCost bookCost = bookDto.getBookCost();
        this.updateById(book);
        bookInfoService.updateById(bookInfo);
        bookDetailService.updateById(bookDetail);
        bookCostService.updateById(bookCost);
    }

    /**
     * 批量修改图书状态
     * @param ids
     * @param status
     */
    @Override
    @Transactional
    public void editBookStatus(List<Long> ids, int status) {
        Book book = new Book();
        book.setStatus(status);
        LambdaQueryWrapper<Book> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Book::getId, ids);
        this.update(book, queryWrapper);
    }

    /**
     * 批量删除书籍
     * @param ids
     */
    @Override
    @Transactional
    public void deleteBook(List<Long> ids) {
        LambdaQueryWrapper<Book> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Book::getId, ids);
        queryWrapper.eq(Book::getStatus, 1);
        long count = this.count(queryWrapper);
        if(count > 0) {
            // 还有图书处于在售状态，不给删除，抛出一个业务异常
            throw new BusinessException("所选图书存有在售图书，不能删除");
        }
        this.removeByIds(ids);

    }

    /**
     * 根据ID获取图书详情
     * @param id
     * @return
     */
    @Override
    @Transactional
    public BookDto getBookById(Long id) {
        BookDto bookDto = new BookDto();
        Book book = this.getById(id);
        // 更加bookId获取图书其它信息
        if(book != null) {
            LambdaQueryWrapper<BookInfo> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(BookInfo::getBookId, id);
            LambdaQueryWrapper<BookDetail> queryWrapper2 = new LambdaQueryWrapper<>();
            queryWrapper2.eq(BookDetail::getBookId, id);
            LambdaQueryWrapper<BookCost> queryWrapper3 = new LambdaQueryWrapper<>();
            queryWrapper3.eq(BookCost::getBookId, id);
            BookInfo bookInfo = bookInfoService.getOne(queryWrapper1);
            BookDetail bookDetail = bookDetailService.getOne(queryWrapper2);
            BookCost bookCost = bookCostService.getOne(queryWrapper3);
            bookDto.setBook(book);
            bookDto.setBookInfo(bookInfo);
            bookDto.setBookDetail(bookDetail);
            bookDto.setBookCost(bookCost);
        }
        return bookDto;
    }

    /**
     * 分页获取图书
     * @param bookPageVo
     * @return
     */
    @Override
    public Page getBookPage(BookPageVo bookPageVo, Long storeId) {
        // 构建分页构造器
        Page pageInfo = new Page(bookPageVo.getPage(), bookPageVo.getPageSize());
        // 构建条件构造器
        LambdaQueryWrapper<Book> queryWrapper = new LambdaQueryWrapper<>();
        // 添加条件
        queryWrapper.eq(Book::getStoreId, storeId);
        queryWrapper.eq(bookPageVo.getBookCateId() != null, Book::getBookCateId, bookPageVo.getBookCateId());
        queryWrapper.eq(bookPageVo.getGoodsCateId() != null, Book::getGoodsCateId, bookPageVo.getGoodsCateId());
        queryWrapper.like(StringUtils.isNotEmpty(bookPageVo.getName()), Book::getName, bookPageVo.getName());
        queryWrapper.like(StringUtils.isNotEmpty(bookPageVo.getAuthor()), Book::getAuthor, bookPageVo.getAuthor());
        queryWrapper.like(StringUtils.isNotEmpty(bookPageVo.getPress()), Book::getPress, bookPageVo.getPress());
        // 添加排序
        queryWrapper.orderByDesc(Book::getUpdateTime);
        // 执行请求
        this.page(pageInfo, queryWrapper);

        return pageInfo;
    }
}
