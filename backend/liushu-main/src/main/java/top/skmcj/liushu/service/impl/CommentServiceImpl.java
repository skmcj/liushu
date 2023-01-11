package top.skmcj.liushu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.skmcj.liushu.dao.mapper.CommentMapper;
import top.skmcj.liushu.entity.Comment;
import top.skmcj.liushu.service.*;

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


}
