package top.skmcj.liushu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.skmcj.liushu.bo.CommentIdEntityBo;
import top.skmcj.liushu.dao.mapper.CommentMapper;
import top.skmcj.liushu.dto.CommentDto;
import top.skmcj.liushu.dto.ReplyDto;
import top.skmcj.liushu.entity.*;
import top.skmcj.liushu.service.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private ReplyService replyService;

    @Autowired
    private UserInfoService infoService;

    @Autowired
    private BookstoreService storeService;

    @Autowired
    private BookService bookService;

    /**
     * 根据ID获取评论
     * @param id
     * @param imgDoMain
     * @return
     */
    @Override
    public CommentDto getCommentById(Long id, String imgDoMain) {
        CommentDto commentDto = new CommentDto();
        Comment comment = this.getById(id);
        if (comment == null) return null;
        commentDto.setId(comment.getId());
        commentDto.setOrderId(comment.getOrderId());
        commentDto.setOrderItemId(comment.getOrderItemId());
        commentDto.setUserId(comment.getUserId());
        commentDto.setBookId(comment.getBookId());
        commentDto.setStoreId(comment.getStoreId());
        commentDto.setContent(comment.getContent());
        commentDto.setScore(comment.getScore());
        commentDto.setIsAnonymous(comment.getIsAnonymous());
        commentDto.setCreateTime(comment.getCreateTime());
        commentDto.setUpdateTime(comment.getUpdateTime());
        commentDto.setIsDeleted(comment.getIsDeleted());
        Bookstore store = storeService.getById(comment.getStoreId());
        Book book = bookService.getById(comment.getBookId());
        commentDto.setStoreName(store.getStoreName());
        commentDto.setBookName(book.getName());
        commentDto.setBookCover(imgDoMain + book.getCover());
        if(comment.getIsAnonymous().equals(0)) {
            // 不匿名
            LambdaQueryWrapper<UserInfo> infoWrapper = new LambdaQueryWrapper<>();
            infoWrapper.eq(UserInfo::getUserId, comment.getUserId());
            UserInfo info = infoService.getOne(infoWrapper);
            commentDto.setNickname(info.getNickname());
            commentDto.setUserAvatar(imgDoMain + info.getAvatar());
        }
        commentDto.setReply(this.getReplyByComment(id));
        return commentDto;
    }

    /**
     * 分页获取用户评论
     * @param userId
     * @param currentPage
     * @param pageSize
     * @param imgDoMain
     * @return
     */
    @Override
    public Page<CommentDto> getCommentPageByUser(Long userId, int currentPage, int pageSize, String imgDoMain) {
        // 完整评论信息页
        Page<CommentDto> commentDtoPage = new Page<>();
        // 评论页
        Page<Comment> commentPage = new Page<>(currentPage, pageSize);
        // 用户信息
        LambdaQueryWrapper<UserInfo> infoWrapper = new LambdaQueryWrapper<>();
        infoWrapper.eq(UserInfo::getUserId, userId);
        UserInfo info = infoService.getOne(infoWrapper);
        // 获取用户评论页
        LambdaQueryWrapper<Comment> commentWrapper = new LambdaQueryWrapper<>();
        commentWrapper.eq(Comment::getUserId, userId);
        this.page(commentPage, commentWrapper);
        List<Comment> comments = commentPage.getRecords();
        // 包装评论信息
        List<CommentDto> commentDtos = comments.stream().map(item -> {
            CommentDto commentDto = new CommentDto();
            // 封装Dto
            commentDto.setId(item.getId());
            commentDto.setOrderId(item.getOrderId());
            commentDto.setOrderItemId(item.getOrderItemId());
            commentDto.setUserId(item.getUserId());
            commentDto.setBookId(item.getBookId());
            commentDto.setStoreId(item.getStoreId());
            commentDto.setContent(item.getContent());
            commentDto.setScore(item.getScore());
            commentDto.setIsAnonymous(item.getIsAnonymous());
            commentDto.setCreateTime(item.getCreateTime());
            commentDto.setUpdateTime(item.getUpdateTime());
            commentDto.setIsDeleted(item.getIsDeleted());
            Bookstore store = storeService.getById(item.getStoreId());
            Book book = bookService.getById(item.getBookId());
            commentDto.setStoreName(store.getStoreName());
            commentDto.setBookName(book.getName());
            commentDto.setBookCover(imgDoMain + book.getCover());
            if(item.getIsAnonymous().equals(0)) {
                // 不匿名
                commentDto.setNickname(info.getNickname());
                commentDto.setUserAvatar(imgDoMain + info.getAvatar());
            }

            commentDto.setReply(this.getReplyByComment(item.getId()));

            return commentDto;
        }).collect(Collectors.toList());
        commentDtoPage.setTotal(commentPage.getTotal());
        commentDtoPage.setSize(commentPage.getSize());
        commentDtoPage.setCurrent(commentPage.getCurrent());
        commentDtoPage.setRecords(commentDtos);
        return commentDtoPage;
    }

    /**
     * 分页获取商家评论
     * @param storeId
     * @param currentPage
     * @param pageSize
     * @param imgDoMain
     * @return
     */
    @Override
    public Page<CommentDto> getCommentPageByStore(Long storeId, int currentPage, int pageSize, String imgDoMain) {
        // 完整评论信息页
        Page<CommentDto> commentDtoPage = new Page<>();
        // 评论页
        Page<Comment> commentPage = new Page<>(currentPage, pageSize);
        // 商家信息
        Bookstore store = storeService.getById(storeId);
        // 获取用户评论页
        LambdaQueryWrapper<Comment> commentWrapper = new LambdaQueryWrapper<>();
        commentWrapper.eq(Comment::getStoreId, storeId);
        this.page(commentPage, commentWrapper);
        List<Comment> comments = commentPage.getRecords();
        // 包装评论信息
        List<CommentDto> commentDtos = comments.stream().map(item -> {
            CommentDto commentDto = new CommentDto();

            commentDto.setId(item.getId());
            commentDto.setOrderId(item.getOrderId());
            commentDto.setOrderItemId(item.getOrderItemId());
            commentDto.setUserId(item.getUserId());
            commentDto.setBookId(item.getBookId());
            commentDto.setStoreId(item.getStoreId());
            commentDto.setContent(item.getContent());
            commentDto.setScore(item.getScore());
            commentDto.setIsAnonymous(item.getIsAnonymous());
            commentDto.setCreateTime(item.getCreateTime());
            commentDto.setUpdateTime(item.getUpdateTime());
            commentDto.setIsDeleted(item.getIsDeleted());
            Book book = bookService.getById(item.getBookId());
            commentDto.setStoreName(store.getStoreName());
            commentDto.setBookName(book.getName());
            commentDto.setBookCover(imgDoMain + book.getCover());
            if(item.getIsAnonymous().equals(0)) {
                // 不匿名
                LambdaQueryWrapper<UserInfo> infoWrapper = new LambdaQueryWrapper<>();
                infoWrapper.eq(UserInfo::getUserId, item.getUserId());
                UserInfo info = infoService.getOne(infoWrapper);
                commentDto.setNickname(info.getNickname());
                commentDto.setUserAvatar(imgDoMain + info.getAvatar());
            }

            commentDto.setReply(this.getReplyByComment(item.getId()));

            return commentDto;
        }).collect(Collectors.toList());
        commentDtoPage.setTotal(commentPage.getTotal());
        commentDtoPage.setSize(commentPage.getSize());
        commentDtoPage.setCurrent(commentPage.getCurrent());
        commentDtoPage.setRecords(commentDtos);
        return commentDtoPage;
    }

    /**
     * 分页获取图书评论
     * @param bookId
     * @param currentPage
     * @param pageSize
     * @param imgDoMain
     * @return
     */
    @Override
    public Page<CommentDto> getCommentPageByBook(Long bookId, int currentPage, int pageSize, String imgDoMain) {
        // 完整评论信息页
        Page<CommentDto> commentDtoPage = new Page<>();
        // 评论页
        Page<Comment> commentPage = new Page<>(currentPage, pageSize);
        // 图书信息
        Book book = bookService.getById(bookId);
        // 获取用户评论页
        LambdaQueryWrapper<Comment> commentWrapper = new LambdaQueryWrapper<>();
        commentWrapper.eq(Comment::getBookId, bookId);
        this.page(commentPage, commentWrapper);
        List<Comment> comments = commentPage.getRecords();
        // 包装评论信息
        List<CommentDto> commentDtos = comments.stream().map(item -> {
            CommentDto commentDto = new CommentDto();
            commentDto.setId(item.getId());
            commentDto.setOrderId(item.getOrderId());
            commentDto.setOrderItemId(item.getOrderItemId());
            commentDto.setUserId(item.getUserId());
            commentDto.setBookId(item.getBookId());
            commentDto.setStoreId(item.getStoreId());
            commentDto.setContent(item.getContent());
            commentDto.setScore(item.getScore());
            commentDto.setIsAnonymous(item.getIsAnonymous());
            commentDto.setCreateTime(item.getCreateTime());
            commentDto.setUpdateTime(item.getUpdateTime());
            commentDto.setIsDeleted(item.getIsDeleted());
            Bookstore store = storeService.getById(item.getStoreId());
            commentDto.setStoreName(store.getStoreName());
            commentDto.setBookName(book.getName());
            commentDto.setBookCover(imgDoMain + book.getCover());
            if(item.getIsAnonymous().equals(0)) {
                // 不匿名
                LambdaQueryWrapper<UserInfo> infoWrapper = new LambdaQueryWrapper<>();
                infoWrapper.eq(UserInfo::getUserId, item.getUserId());
                UserInfo info = infoService.getOne(infoWrapper);
                commentDto.setNickname(info.getNickname());
                commentDto.setUserAvatar(imgDoMain + info.getAvatar());
            }

            commentDto.setReply(this.getReplyByComment(item.getId()));

            return commentDto;
        }).collect(Collectors.toList());
        commentDtoPage.setTotal(commentPage.getTotal());
        commentDtoPage.setSize(commentPage.getSize());
        commentDtoPage.setCurrent(commentPage.getCurrent());
        commentDtoPage.setRecords(commentDtos);
        return commentDtoPage;
    }

    /**
     * 获取订单评论列表
     * @param orderId
     * @param imgDoMain
     * @return
     */
    @Override
    public List<CommentDto> getCommentListByOrder(Long orderId, Long userId, Long storeId, String imgDoMain) {
        // 获取对应评论
        LambdaQueryWrapper<Comment> commentWrapper = new LambdaQueryWrapper<>();
        commentWrapper.eq(Comment::getOrderId, orderId);
        List<Comment> comments = this.list(commentWrapper);
        System.out.println("comments ==> " + comments);
        // 获取对应商家
        Bookstore store = storeService.getById(storeId);
        // 获取对应用户信息
        LambdaQueryWrapper<UserInfo> infoWrapper = new LambdaQueryWrapper<>();
        infoWrapper.eq(UserInfo::getUserId, userId);
        UserInfo info = infoService.getOne(infoWrapper);
        List<CommentDto> commentDtos = comments.stream().map(item -> {
            CommentDto commentDto = new CommentDto();
            commentDto.setId(item.getId());
            commentDto.setOrderId(item.getOrderId());
            commentDto.setOrderItemId(item.getOrderItemId());
            commentDto.setUserId(item.getUserId());
            commentDto.setBookId(item.getBookId());
            commentDto.setStoreId(item.getStoreId());
            commentDto.setContent(item.getContent());
            commentDto.setScore(item.getScore());
            commentDto.setIsAnonymous(item.getIsAnonymous());
            commentDto.setCreateTime(item.getCreateTime());
            commentDto.setUpdateTime(item.getUpdateTime());
            commentDto.setIsDeleted(item.getIsDeleted());
            commentDto.setStoreName(store.getStoreName());
            Book book = bookService.getById(item.getBookId());
            commentDto.setBookName(book.getName());
            commentDto.setBookCover(imgDoMain + book.getCover());
            if(item.getIsAnonymous().equals(0)) {
                // 不匿名
                commentDto.setNickname(info.getNickname());
                commentDto.setUserAvatar(imgDoMain + info.getAvatar());
            }
            commentDto.setReply(this.getReplyByComment(item.getId()));

            return commentDto;
        }).collect(Collectors.toList());
        return commentDtos;
    }

    /**
     * 根据订单项ID获取评论
     * @param orderItemId
     * @param imgDoMain
     * @return
     */
    @Override
    public CommentDto getCommentByOrderItem(Long orderItemId, String imgDoMain) {
        CommentDto commentDto = new CommentDto();

        LambdaQueryWrapper<Comment> commentWrapper = new LambdaQueryWrapper<>();
        commentWrapper.eq(Comment::getOrderItemId, orderItemId);
        Comment comment = this.getOne(commentWrapper);
        if(comment == null) return null;
        commentDto.setId(comment.getId());
        commentDto.setOrderId(comment.getOrderId());
        commentDto.setOrderItemId(comment.getOrderItemId());
        commentDto.setUserId(comment.getUserId());
        commentDto.setBookId(comment.getBookId());
        commentDto.setStoreId(comment.getStoreId());
        commentDto.setContent(comment.getContent());
        commentDto.setScore(comment.getScore());
        commentDto.setIsAnonymous(comment.getIsAnonymous());
        commentDto.setCreateTime(comment.getCreateTime());
        commentDto.setUpdateTime(comment.getUpdateTime());
        commentDto.setIsDeleted(comment.getIsDeleted());
        // 获取商家名称
        Bookstore store = storeService.getById(comment.getStoreId());
        commentDto.setStoreName(store.getStoreName());
        // 获取图书名
        Book book = bookService.getById(comment.getBookId());
        commentDto.setBookName(book.getName());
        commentDto.setBookCover(imgDoMain + book.getCover());
        if(comment.getIsAnonymous().equals(0)) {
            // 不匿名
            LambdaQueryWrapper<UserInfo> infoWrapper = new LambdaQueryWrapper<>();
            infoWrapper.eq(UserInfo::getUserId, comment.getUserId());
            UserInfo info = infoService.getOne(infoWrapper);
            commentDto.setNickname(info.getNickname());
            commentDto.setUserAvatar(imgDoMain + info.getAvatar());
        }

        commentDto.setReply(this.getReplyByComment(comment.getId()));
        return commentDto;
    }

    /**
     * 根据回复ID获取回复列表
     * @param commentId 父评论ID
     * @return
     */
    private ReplyDto getReplyByComment(Long commentId) {
        ReplyDto replyDto = new ReplyDto();
        LambdaQueryWrapper<Reply> replyWrapper = new LambdaQueryWrapper<>();
        replyWrapper.eq(Reply::getCommentId, commentId);
        Reply reply = replyService.getOne(replyWrapper);
        if(reply == null) return null;
        replyDto.setId(reply.getId());
        replyDto.setCommentId(reply.getCommentId());
        replyDto.setStoreId(reply.getStoreId());
        replyDto.setUserId(reply.getUserId());
        replyDto.setContent(reply.getContent());
        replyDto.setCreateTime(reply.getCreateTime());
        replyDto.setUpdateTime(reply.getUpdateTime());
        replyDto.setIsDeleted(reply.getIsDeleted());
        replyDto.setReply(this.getReplyByComment(reply.getId()));
        return replyDto;
    }

    /**
     * 根据评论列表获取包装commentDto列表
     * @param comments
     * @param commentBo
     * @param imgDoMain
     * @return
     */
    private List<CommentDto> getCommentDtoListByComments(List<Comment> comments, CommentIdEntityBo commentBo,
                                                         String imgDoMain) {

        List<CommentDto> commentDtos = comments.stream().map(item -> {
            CommentDto commentDto = new CommentDto();
            // 封装Dto
            commentDto.setId(item.getId());
            commentDto.setOrderId(item.getOrderId());
            commentDto.setOrderItemId(item.getOrderItemId());
            commentDto.setUserId(item.getUserId());
            commentDto.setBookId(item.getBookId());
            commentDto.setStoreId(item.getStoreId());
            commentDto.setContent(item.getContent());
            commentDto.setScore(item.getScore());
            commentDto.setIsAnonymous(item.getIsAnonymous());
            commentDto.setCreateTime(item.getCreateTime());
            commentDto.setUpdateTime(item.getUpdateTime());
            commentDto.setIsDeleted(item.getIsDeleted());
            // 存入商家名称
            if(commentBo.getStore() != null) {
                commentDto.setStoreName(commentBo.getStore().getStoreName());
            } else {
                Bookstore store = storeService.getById(item.getStoreId());
                commentDto.setStoreName(store.getStoreName());
            }
            // 存入图书信息
            if(commentBo.getBook() != null) {
                commentDto.setBookName(commentBo.getBook().getName());
                commentDto.setBookCover(imgDoMain + commentBo.getBook().getCover());
            } else {
                Book book = bookService.getById(item.getBookId());
                commentDto.setBookName(book.getName());
                commentDto.setBookCover(imgDoMain + book.getCover());
            }

            if(item.getIsAnonymous().equals(0)) {
                // 不匿名
                if(commentBo.getInfo() != null) {
                    commentDto.setNickname(commentBo.getInfo().getNickname());
                    commentDto.setUserAvatar(imgDoMain + commentBo.getInfo().getAvatar());
                } else {
                    // 用户信息
                    LambdaQueryWrapper<UserInfo> infoWrapper = new LambdaQueryWrapper<>();
                    infoWrapper.eq(UserInfo::getUserId, item.getUserId());
                    UserInfo info = infoService.getOne(infoWrapper);
                    commentDto.setNickname(info.getNickname());
                    commentDto.setUserAvatar(imgDoMain + info.getAvatar());
                }

            }
            commentDto.setReply(this.getReplyByComment(item.getId()));

            return commentDto;
        }).collect(Collectors.toList());
        return commentDtos;
    }
}
