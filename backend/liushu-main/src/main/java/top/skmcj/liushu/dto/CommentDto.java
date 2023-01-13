package top.skmcj.liushu.dto;

import lombok.Data;
import top.skmcj.liushu.entity.Comment;

import java.util.List;

/**
 * 评论
 */
@Data
public class CommentDto extends Comment {

    /**
     * 评论的回复列表
     */
    private ReplyDto reply;

    /**
     * 订单号
     */
    private String orderNumber;
}
