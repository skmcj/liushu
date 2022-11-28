package top.skmcj.liushu.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.skmcj.liushu.dao.mapper.ReplyMapper;
import top.skmcj.liushu.entity.Reply;
import top.skmcj.liushu.service.ReplyService;

@Service
public class ReplyServiceImpl extends ServiceImpl<ReplyMapper, Reply> implements ReplyService {
}
