package top.skmcj.liushu.dto;

import lombok.Data;
import top.skmcj.liushu.entity.Reply;

import java.util.List;

/**
 * 回复
 */
@Data
public class ReplyDto extends Reply {

    /**
     * 回复的回复列表
     */
    private ReplyDto reply;
}
