package top.skmcj.liushu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.skmcj.liushu.common.Result;
import top.skmcj.liushu.common.exception.BusinessException;
import top.skmcj.liushu.dao.mapper.BookMapper;
import top.skmcj.liushu.dto.BookCardDto;
import top.skmcj.liushu.dto.BookDto;
import top.skmcj.liushu.entity.*;
import top.skmcj.liushu.service.*;
import top.skmcj.liushu.vo.BookPageVo;

import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    @Autowired
    private BookCateService bookCateService;

    @Autowired
    private BookstoreService bookstoreService;

    @Autowired
    private BookInfoService bookInfoService;

    @Autowired
    private BookDetailService bookDetailService;

    @Autowired
    private BookCostService bookCostService;

    @Autowired
    private BookMapper bookMapper;

    /**
     * 新增图书
     *
     * @param bookDto
     * @param storeId    商家id
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
     *
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
     *
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
     *
     * @param ids
     */
    @Override
    @Transactional
    public void deleteBook(List<Long> ids) {
        LambdaQueryWrapper<Book> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Book::getId, ids);
        queryWrapper.eq(Book::getStatus, 1);
        long count = this.count(queryWrapper);
        if (count > 0) {
            // 还有图书处于在售状态，不给删除，抛出一个业务异常
            throw new BusinessException("所选图书存有在售图书，不能删除");
        }
        this.removeByIds(ids);

    }

    /**
     * 根据ID获取图书详情
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public BookDto getBookById(Long id) {
        BookDto bookDto = new BookDto();
        Book book = this.getById(id);
        // 更加bookId获取图书其它信息
        if (book != null) {
            LambdaQueryWrapper<BookInfo> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(BookInfo::getBookId, id);
            LambdaQueryWrapper<BookDetail> queryWrapper2 = new LambdaQueryWrapper<>();
            queryWrapper2.eq(BookDetail::getBookId, id);
            LambdaQueryWrapper<BookCost> queryWrapper3 = new LambdaQueryWrapper<>();
            queryWrapper3.eq(BookCost::getBookId, id);
            BookCate bookCate = bookCateService.getById(book.getBookCateId());
            BookInfo bookInfo = bookInfoService.getOne(queryWrapper1);
            BookDetail bookDetail = bookDetailService.getOne(queryWrapper2);
            BookCost bookCost = bookCostService.getOne(queryWrapper3);
            bookDto.setBook(book);
            bookDto.setBookInfo(bookInfo);
            bookDto.setBookDetail(bookDetail);
            bookDto.setBookCost(bookCost);
            bookDto.setBookCateName(bookCate.getName());
        }
        return bookDto;
    }

    /**
     * 分页获取图书
     *
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

    /**
     * 根据借阅量获取指定条数
     *
     * @param start
     * @param size
     * @return
     */
    @Override
    public List<Book> getBookByMba(int start, int size) {
        List<Book> books = bookMapper.getBookLimit(start, size);
        return books;
    }

    /**
     * 随机获取指定条数的图书
     * BUG - 有可能随机重复的记录
     *
     * @param size 条数
     * @return
     */
    @Override
    public List<BookCardDto> getBookByRandom(int size) {
        LambdaQueryWrapper<Book> bookWrapper = new LambdaQueryWrapper<>();
        bookWrapper.eq(Book::getStatus, 1);
        long total = this.count(bookWrapper);
        List<BookCardDto> bookCards = new ArrayList<>();
        List<Book> books = new ArrayList<>();
        SecureRandom random = new SecureRandom();
        // 将 size 分为 3 份，分 3 次获取
        int[] sizeArr = new int[]{size / 3, size / 3, size - 2 * (size / 3)};

        for (int i = 0; i < sizeArr.length; i++) {
            LambdaQueryWrapper<Book> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Book::getStatus, 1);
            queryWrapper.last("LIMIT " + String.valueOf(random.nextInt((int) (total - sizeArr[i] + 1))) + ", " + sizeArr[i]);
            List<Book> list = this.list(queryWrapper);
            list.stream().forEach(item -> {
                books.add(item);
            });
        }
        books.stream().forEach(item -> {
            BookCardDto bookCard = new BookCardDto();
            // 拷贝图书信息
            bookCard.setId(item.getId());
            bookCard.setStoreId(item.getStoreId());
            bookCard.setCover(item.getCover());
            bookCard.setCoverUrl(item.getCoverUrl());
            bookCard.setName(item.getName());
            bookCard.setAuthor(item.getAuthor());
            bookCard.setBookCateId(item.getBookCateId());
            bookCard.setGoodsCateId(item.getGoodsCateId());
            bookCard.setPress(item.getPress());
            bookCard.setPubDate(item.getPubDate());
            bookCard.setSize(item.getSize());
            bookCard.setPages(item.getPages());
            bookCard.setInventory(item.getInventory());
            bookCard.setStatus(item.getStatus());
            bookCard.setMba(item.getMba());
            bookCard.setTba(item.getTba());
            // 获取图书简介
            LambdaQueryWrapper<BookDetail> detailWrapper = new LambdaQueryWrapper<>();
            detailWrapper.eq(BookDetail::getBookId, item.getId());
            BookDetail itemDetail = bookDetailService.getOne(detailWrapper);
            bookCard.setOutline(itemDetail.getOutline());
            // 获取商家名
            Bookstore store = bookstoreService.getById(item.getStoreId());
            bookCard.setStoreName(store.getStoreName());
            bookCards.add(bookCard);
        });
        return bookCards;
    }

    /**
     * 根据类型ID获取随机图书
     *
     * @param size
     * @param cateId
     * @return
     */
    @Override
    public List<BookCardDto> getBookByRandomOfType(int size, Long cateId) {
        List<BookCardDto> bookCards = new ArrayList<>();
        LambdaQueryWrapper<Book> countWrapper = new LambdaQueryWrapper<>();
        countWrapper.eq(Book::getBookCateId, cateId);
        countWrapper.eq(Book::getStatus, 1);
        long total = this.count(countWrapper);
        List<Book> books = new ArrayList<>();
        SecureRandom random = new SecureRandom();
        // 将 size 分为 3 份，分 3 次获取
        int[] sizeArr = new int[]{size / 3, size / 3, size - 2 * (size / 3)};

        for (int i = 0; i < sizeArr.length; i++) {
            LambdaQueryWrapper<Book> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Book::getBookCateId, cateId);
            queryWrapper.eq(Book::getStatus, 1);
            queryWrapper.last("LIMIT " + String.valueOf(random.nextInt((int) (total - sizeArr[i] + 1))) + ", " + sizeArr[i]);
            List<Book> list = this.list(queryWrapper);
            list.stream().forEach(item -> {
                books.add(item);
            });
        }
        books.stream().forEach(item -> {
            BookCardDto bookCard = new BookCardDto();
            // 拷贝图书信息
            bookCard.setId(item.getId());
            bookCard.setStoreId(item.getStoreId());
            bookCard.setCover(item.getCover());
            bookCard.setCoverUrl(item.getCoverUrl());
            bookCard.setName(item.getName());
            bookCard.setAuthor(item.getAuthor());
            bookCard.setBookCateId(item.getBookCateId());
            bookCard.setGoodsCateId(item.getGoodsCateId());
            bookCard.setPress(item.getPress());
            bookCard.setPubDate(item.getPubDate());
            bookCard.setSize(item.getSize());
            bookCard.setPages(item.getPages());
            bookCard.setInventory(item.getInventory());
            bookCard.setStatus(item.getStatus());
            bookCard.setMba(item.getMba());
            bookCard.setTba(item.getTba());
            // 获取图书简介
            LambdaQueryWrapper<BookDetail> detailWrapper = new LambdaQueryWrapper<>();
            detailWrapper.eq(BookDetail::getBookId, item.getId());
            BookDetail itemDetail = bookDetailService.getOne(detailWrapper);
            bookCard.setOutline(itemDetail.getOutline());
            // 获取商家名
            Bookstore store = bookstoreService.getById(item.getStoreId());
            bookCard.setStoreName(store.getStoreName());
            bookCards.add(bookCard);
        });
        return bookCards;
    }

    /**
     * 根据ID列表查询图书
     *
     * @param ids
     * @return
     */
    @Override
    public List<Book> getBookByIds(List<Long> ids) {
        LambdaQueryWrapper<Book> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Book::getId, ids);
        List<Book> books = this.list(queryWrapper);
        return books;
    }

    /**
     * 根据类别获取图书销量榜
     * @param cateId
     * @return
     */
    @Override
    public List<Book> getBookRankByCate(Integer cateId, int size) {
        LambdaQueryWrapper<Book> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Book::getBookCateId, cateId);
        queryWrapper.eq(Book::getStatus, 1);
        queryWrapper.orderByDesc(Book::getTba);
        queryWrapper.orderByDesc(Book::getMba);
        queryWrapper.orderByDesc(Book::getUpdateTime);
        queryWrapper.last("LIMIT " + String.valueOf(size));
        List<Book> books = this.list(queryWrapper);
        return books;
    }

    /**
     * 根据ID列表查询图书
     *
     * @param ids
     * @return
     */
    @Override
    public List<BookCardDto> getBookCardByIds(List<Long> ids) {
        List<BookCardDto> bookCards = new ArrayList<>();
        LambdaQueryWrapper<Book> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Book::getId, ids);
        List<Book> books = this.list(queryWrapper);
        books.stream().forEach(item -> {
            BookCardDto bookCard = new BookCardDto();
            // 拷贝图书信息
            bookCard.setId(item.getId());
            bookCard.setStoreId(item.getStoreId());
            bookCard.setCover(item.getCover());
            bookCard.setCoverUrl(item.getCoverUrl());
            bookCard.setName(item.getName());
            bookCard.setAuthor(item.getAuthor());
            bookCard.setBookCateId(item.getBookCateId());
            bookCard.setGoodsCateId(item.getGoodsCateId());
            bookCard.setPress(item.getPress());
            bookCard.setPubDate(item.getPubDate());
            bookCard.setSize(item.getSize());
            bookCard.setPages(item.getPages());
            bookCard.setInventory(item.getInventory());
            bookCard.setStatus(item.getStatus());
            bookCard.setMba(item.getMba());
            bookCard.setTba(item.getTba());
            // 获取图书简介
            LambdaQueryWrapper<BookDetail> detailWrapper = new LambdaQueryWrapper<>();
            detailWrapper.eq(BookDetail::getBookId, item.getId());
            BookDetail itemDetail = bookDetailService.getOne(detailWrapper);
            bookCard.setOutline(itemDetail.getOutline());
            // 获取商家名
            Bookstore store = bookstoreService.getById(item.getStoreId());
            bookCard.setStoreName(store.getStoreName());
            bookCards.add(bookCard);
        });
        return bookCards;
    }

    /**
     * 根据类别分页获取图书
     * @param cateId
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public Page<BookCardDto> getBookCardPageByCate(Integer cateId, int currentPage, int pageSize) {
        Page<BookCardDto> bookCardPage = new Page<>();
        Page<Book> bookPage = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<Book> bookWrapper = new LambdaQueryWrapper<>();
        bookWrapper.eq(Book::getBookCateId, cateId);
        bookWrapper.eq(Book::getStatus, 1);
        bookWrapper.orderByDesc(Book::getUpdateTime);
        this.page(bookPage, bookWrapper);
        List<Book> books = bookPage.getRecords();
        List<BookCardDto> bookCards = books.stream().map(item -> {
            // 设置图书基本信息
            BookCardDto bookCard = new BookCardDto();
            bookCard.setId(item.getId());
            bookCard.setStoreId(item.getStoreId());
            bookCard.setCover(item.getCover());
            bookCard.setCoverUrl(item.getCoverUrl());
            bookCard.setName(item.getName());
            bookCard.setAuthor(item.getAuthor());
            bookCard.setBookCateId(item.getBookCateId());
            bookCard.setGoodsCateId(item.getGoodsCateId());
            bookCard.setPress(item.getPress());
            bookCard.setPubDate(item.getPubDate());
            bookCard.setSize(item.getSize());
            bookCard.setPages(item.getPages());
            bookCard.setInventory(item.getInventory());
            bookCard.setStatus(item.getStatus());
            bookCard.setMba(item.getMba());
            bookCard.setTba(item.getTba());
            // 获取图书简介
            LambdaQueryWrapper<BookDetail> detailWrapper = new LambdaQueryWrapper<>();
            detailWrapper.eq(BookDetail::getBookId, item.getId());
            BookDetail detail = bookDetailService.getOne(detailWrapper);
            Bookstore store = bookstoreService.getById(item.getStoreId());
            bookCard.setOutline(detail.getOutline());
            bookCard.setStoreName(store.getStoreName());
            return bookCard;
        }).collect(Collectors.toList());
        bookCardPage.setRecords(bookCards);
        bookCardPage.setTotal(bookPage.getTotal());
        bookCardPage.setSize(bookPage.getSize());
        bookCardPage.setCurrent(bookPage.getCurrent());
        return bookCardPage;
    }

    /**
     * 分页获取店内图书
     *
     * @param storeId
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public Page<BookCardDto> getBookCardOfStore(Long storeId, int currentPage, int pageSize) {
        Page<BookCardDto> bookCardPage = new Page<>();
        // 获取图书列表
        LambdaQueryWrapper<Book> bookWrapper = new LambdaQueryWrapper<>();
        bookWrapper.eq(Book::getStoreId, storeId);
        bookWrapper.eq(Book::getStatus, 1);
        bookWrapper.orderByDesc(Book::getTba);
        bookWrapper.orderByDesc(Book::getMba);
        bookWrapper.orderByDesc(Book::getUpdateTime);
        Page<Book> bookPage = new Page<>(currentPage, pageSize);
        this.page(bookPage, bookWrapper);
        // 获取商家信息
        Bookstore store = bookstoreService.getById(storeId);
        List<Book> books = bookPage.getRecords();
        List<BookCardDto> bookCards = books.stream().map(item -> {
            // 设置图书基本信息
            BookCardDto bookCard = new BookCardDto();
            bookCard.setId(item.getId());
            bookCard.setStoreId(item.getStoreId());
            bookCard.setCover(item.getCover());
            bookCard.setCoverUrl(item.getCoverUrl());
            bookCard.setName(item.getName());
            bookCard.setAuthor(item.getAuthor());
            bookCard.setBookCateId(item.getBookCateId());
            bookCard.setGoodsCateId(item.getGoodsCateId());
            bookCard.setPress(item.getPress());
            bookCard.setPubDate(item.getPubDate());
            bookCard.setSize(item.getSize());
            bookCard.setPages(item.getPages());
            bookCard.setInventory(item.getInventory());
            bookCard.setStatus(item.getStatus());
            bookCard.setMba(item.getMba());
            bookCard.setTba(item.getTba());
            // 获取图书简介
            LambdaQueryWrapper<BookDetail> detailWrapper = new LambdaQueryWrapper<>();
            detailWrapper.eq(BookDetail::getBookId, item.getId());
            BookDetail detail = bookDetailService.getOne(detailWrapper);
            bookCard.setOutline(detail.getOutline());
            bookCard.setStoreName(store.getStoreName());
            return bookCard;
        }).collect(Collectors.toList());
        bookCardPage.setRecords(bookCards);
        bookCardPage.setTotal(bookPage.getTotal());
        bookCardPage.setSize(bookPage.getSize());
        bookCardPage.setCurrent(bookPage.getCurrent());
        return bookCardPage;
    }

    /**
     * 根据类型分页获取店内图书
     *
     * @param storeId
     * @param cateId
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public Page<BookCardDto> getBookCardByCateOfStore(Long storeId, Long cateId, int currentPage, int pageSize) {
        Page<BookCardDto> bookCardPage = new Page<>();
        // 获取图书页
        Page<Book> bookPage = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<Book> bookWrapper = new LambdaQueryWrapper<>();
        bookWrapper.eq(Book::getStoreId, storeId);
        bookWrapper.eq(Book::getGoodsCateId, cateId);
        bookWrapper.eq(Book::getStatus, 1);
        bookWrapper.orderByDesc(Book::getUpdateTime);
        this.page(bookPage, bookWrapper);
        // 获取商家信息
        Bookstore store = bookstoreService.getById(storeId);
        List<Book> books = bookPage.getRecords();
        // 封装图书卡片信息
        List<BookCardDto> bookCards = books.stream().map(item -> {
            // 设置图书基本信息
            BookCardDto bookCard = new BookCardDto();
            bookCard.setId(item.getId());
            bookCard.setStoreId(item.getStoreId());
            bookCard.setCover(item.getCover());
            bookCard.setCoverUrl(item.getCoverUrl());
            bookCard.setName(item.getName());
            bookCard.setAuthor(item.getAuthor());
            bookCard.setBookCateId(item.getBookCateId());
            bookCard.setGoodsCateId(item.getGoodsCateId());
            bookCard.setPress(item.getPress());
            bookCard.setPubDate(item.getPubDate());
            bookCard.setSize(item.getSize());
            bookCard.setPages(item.getPages());
            bookCard.setInventory(item.getInventory());
            bookCard.setStatus(item.getStatus());
            bookCard.setMba(item.getMba());
            bookCard.setTba(item.getTba());
            // 获取图书简介
            LambdaQueryWrapper<BookDetail> detailWrapper = new LambdaQueryWrapper<>();
            detailWrapper.eq(BookDetail::getBookId, item.getId());
            BookDetail detail = bookDetailService.getOne(detailWrapper);
            bookCard.setOutline(detail.getOutline());
            bookCard.setStoreName(store.getStoreName());
            return bookCard;
        }).collect(Collectors.toList());
        bookCardPage.setRecords(bookCards);
        bookCardPage.setTotal(bookPage.getTotal());
        bookCardPage.setSize(bookPage.getSize());
        bookCardPage.setCurrent(bookPage.getCurrent());
        return bookCardPage;
    }

    /**
     * 分页搜索店内图书
     *
     * @param storeId
     * @param text
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public Page<BookCardDto> getBookPageBySearchOfStore(Long storeId, String text, int currentPage, int pageSize) {
        Page<BookCardDto> bookCardPage = new Page<>();
        // 获取图书页
        Page<Book> bookPage = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<Book> searchWrapper = new LambdaQueryWrapper<>();
        boolean hasText = StringUtils.isNotEmpty(text);
        searchWrapper.eq(Book::getStoreId, storeId);
        searchWrapper.eq(Book::getStatus, 1);
        if(hasText) {
            searchWrapper.like(Book::getName, text);
            searchWrapper.or().like(Book::getAuthor, text);
            searchWrapper.or().like(Book::getPress, text);
        }
        searchWrapper.orderByDesc(Book::getTba);
        searchWrapper.orderByDesc(Book::getUpdateTime);
        this.page(bookPage, searchWrapper);
        // 获取商家信息
        Bookstore store = bookstoreService.getById(storeId);
        List<Book> books = bookPage.getRecords();
        List<BookCardDto> bookCards = books.stream().map(item -> {
            // 设置图书基本信息
            BookCardDto bookCard = new BookCardDto();
            bookCard.setId(item.getId());
            bookCard.setStoreId(item.getStoreId());
            bookCard.setCover(item.getCover());
            bookCard.setCoverUrl(item.getCoverUrl());
            bookCard.setName(item.getName());
            bookCard.setAuthor(item.getAuthor());
            bookCard.setBookCateId(item.getBookCateId());
            bookCard.setGoodsCateId(item.getGoodsCateId());
            bookCard.setPress(item.getPress());
            bookCard.setPubDate(item.getPubDate());
            bookCard.setSize(item.getSize());
            bookCard.setPages(item.getPages());
            bookCard.setInventory(item.getInventory());
            bookCard.setStatus(item.getStatus());
            bookCard.setMba(item.getMba());
            bookCard.setTba(item.getTba());
            // 获取图书简介
            LambdaQueryWrapper<BookDetail> detailWrapper = new LambdaQueryWrapper<>();
            detailWrapper.eq(BookDetail::getBookId, item.getId());
            BookDetail detail = bookDetailService.getOne(detailWrapper);
            bookCard.setOutline(detail.getOutline());
            bookCard.setStoreName(store.getStoreName());
            return bookCard;
        }).collect(Collectors.toList());
        bookCardPage.setRecords(bookCards);
        bookCardPage.setTotal(bookPage.getTotal());
        bookCardPage.setSize(bookPage.getSize());
        bookCardPage.setCurrent(bookPage.getCurrent());
        return bookCardPage;
    }

    /**
     * 全局搜索
     * @param keyword
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public Page<BookCardDto> searchBookOfGlobal(String keyword, int currentPage, int pageSize) {
        Page<BookCardDto> bookCardPage = new Page<>();
        // 获取图书页
        Page<Book> bookPage = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<Book> searchWrapper = new LambdaQueryWrapper<>();
        boolean hasText = StringUtils.isNotEmpty(keyword);
        searchWrapper.eq(Book::getStatus, 1);
        if(hasText) {
            searchWrapper.like(Book::getName, keyword);
            searchWrapper.or().like(Book::getAuthor, keyword);
            searchWrapper.or().like(Book::getPress, keyword);
        }
        searchWrapper.orderByDesc(Book::getTba);
        searchWrapper.orderByDesc(Book::getUpdateTime);
        this.page(bookPage, searchWrapper);
        List<Book> books = bookPage.getRecords();
        List<BookCardDto> bookCards = books.stream().map(item -> {
            // 设置图书基本信息
            BookCardDto bookCard = new BookCardDto();
            bookCard.setId(item.getId());
            bookCard.setStoreId(item.getStoreId());
            bookCard.setCover(item.getCover());
            bookCard.setCoverUrl(item.getCoverUrl());
            bookCard.setName(item.getName());
            bookCard.setAuthor(item.getAuthor());
            bookCard.setBookCateId(item.getBookCateId());
            bookCard.setGoodsCateId(item.getGoodsCateId());
            bookCard.setPress(item.getPress());
            bookCard.setPubDate(item.getPubDate());
            bookCard.setSize(item.getSize());
            bookCard.setPages(item.getPages());
            bookCard.setInventory(item.getInventory());
            bookCard.setStatus(item.getStatus());
            bookCard.setMba(item.getMba());
            bookCard.setTba(item.getTba());
            // 获取图书简介
            LambdaQueryWrapper<BookDetail> detailWrapper = new LambdaQueryWrapper<>();
            detailWrapper.eq(BookDetail::getBookId, item.getId());
            BookDetail detail = bookDetailService.getOne(detailWrapper);
            bookCard.setOutline(detail.getOutline());
            // 获取商家信息
            Bookstore store = bookstoreService.getById(item.getStoreId());
            bookCard.setStoreName(store.getStoreName());
            return bookCard;
        }).collect(Collectors.toList());
        bookCardPage.setRecords(bookCards);
        bookCardPage.setTotal(bookPage.getTotal());
        bookCardPage.setSize(bookPage.getSize());
        bookCardPage.setCurrent(bookPage.getCurrent());
        return bookCardPage;
    }
}
