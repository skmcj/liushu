package top.skmcj.liushu.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.skmcj.liushu.common.Result;
import top.skmcj.liushu.dto.CommentDto;
import top.skmcj.liushu.entity.Comment;
import top.skmcj.liushu.entity.Reply;
import top.skmcj.liushu.service.CommentService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 评论接口
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 添加评论
     * @param comment
     * @return
     */
    @PostMapping
    public Result<String> addCommentByUser(@RequestBody Comment comment) {
        return Result.success("");
    }

    /**
     * 修改评论
     * @param comment
     * @return
     */
    @PutMapping
    public Result<String> updateCommentByUser(@RequestBody Comment comment) {
        return Result.success("");
    }

    /**
     * 删除评论
     * @param id
     * @return
     */
    @DeleteMapping
    public Result<String> deleteCommentByUser(Long id) {
        return Result.success("");
    }

    /**
     * 根据评论ID获取评论
     * @param id
     * @return
     */
    @GetMapping("/id/{id}")
    public Result<CommentDto> getCommentById(@PathVariable Long id) {
        return Result.success("");
    }

    /**
     * 分页获取用户评论
     * @param currentPage
     * @param pageSize
     * @param request
     * @return
     */
    @GetMapping("/user")
    public Result<Page<CommentDto>> getCommentPageByUser(int currentPage, int pageSize, HttpServletRequest request) {
        return Result.success("");
    }

    /**
     * 分页获取商家评论
     * @param currentPage
     * @param pageSize
     * @param request
     * @return
     */
    @GetMapping("/store")
    public Result<Page<CommentDto>> getCommentPageByStore(int currentPage, int pageSize, HttpServletRequest request) {
        return Result.success("");
    }

    /**
     * 分页获取图书评论
     * @param bookId
     * @param currentPage
     * @param pageSize
     * @return
     */
    @GetMapping("/book")
    public Result<Page<CommentDto>> getCommentPageByBook(Long bookId, int currentPage, int pageSize) {
        return Result.success("");
    }

    /**
     * 根据订单ID获取用户订单相关评论
     * @param orderId
     * @param request
     * @return
     */
    @GetMapping("/user/order")
    public Result<List<CommentDto>> getCommentListByOrderOfUser(Long orderId, HttpServletRequest request) {
        return Result.success("");
    }

    /**
     * 根据订单ID获取商家订单相关评论
     * @param orderId
     * @param request
     * @return
     */
    @GetMapping("/store/order")
    public Result<List<CommentDto>> getCommentListByOrderOfStore(Long orderId, HttpServletRequest request) {
        return Result.success("");
    }

    /**
     * 用户回复【评论】
     * @param reply
     * @return
     */
    @PostMapping("/user/reply/comment")
    public Result<String> addReplyByUserOfComment(@RequestBody Reply reply) {
        return Result.success("");
    }

    /**
     * 用户回复【回复】
     * @param reply
     * @return
     */
    @PostMapping("/user/reply/reply")
    public Result<String> addReplyByUserOfReply(@RequestBody Reply reply) {
        return Result.success("");
    }

    /**
     * 商家回复【评论】
     * @param reply
     * @return
     */
    @PostMapping("/store/reply/comment")
    public Result<String> addReplyByStoreOfComment(@RequestBody Reply reply) {
        return Result.success("");
    }

    /**
     * 商家回复【回复】
     * @param reply
     * @return
     */
    @PostMapping("/store/reply/reply")
    public Result<String> addReplyByStoreOfReply(@RequestBody Reply reply) {
        return Result.success("");
    }
}
