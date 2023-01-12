package top.skmcj.liushu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import top.skmcj.liushu.dto.CommentDto;
import top.skmcj.liushu.entity.Comment;
import top.skmcj.liushu.entity.Reply;

import java.util.List;

public interface CommentService extends IService<Comment> {

    CommentDto getCommentById(Long id, String imgDoMain);

    Page<CommentDto> getCommentPageByUser(Long userId, int currentPage, int pageSize, String imgDoMain);

    Page<CommentDto> getCommentPageByStore(Long storeId, int currentPage, int pageSize, String imgDoMain);

    Page<CommentDto> getCommentPageByBook(Long bookId, int currentPage, int pageSize, String imgDoMain);

    List<CommentDto> getCommentListByOrder(Long orderId, Long userId, Long storeId, String imgDoMain);

    CommentDto getCommentByOrderItem(Long orderItemId, String imgDoMain);
}
