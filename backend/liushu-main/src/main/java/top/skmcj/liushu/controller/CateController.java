package top.skmcj.liushu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.skmcj.liushu.common.Result;
import top.skmcj.liushu.entity.BookCate;
import top.skmcj.liushu.entity.GoodsCate;
import top.skmcj.liushu.service.BookCateService;
import top.skmcj.liushu.service.GoodsCateService;

import java.util.List;

/**
 * 类别接口
 */
@RestController
@RequestMapping("/cate")
public class CateController {

    @Autowired
    private BookCateService bookCateService;

    @Autowired
    private GoodsCateService goodsCateService;

    /**
     * 获取图书类别
     * @return
     */
    @GetMapping("/book")
    public Result<List<BookCate>> getBookCate() {
        List<BookCate> cates = bookCateService.list();
        return Result.success(cates);
    }

    /**
     * 获取商家店内分类
     * @param storeId
     * @return
     */
    @GetMapping("/store")
    public Result<List<GoodsCate>> getGoodsCate(Long storeId) {
        LambdaQueryWrapper<GoodsCate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GoodsCate::getStoreId, storeId);
        List<GoodsCate> cates = goodsCateService.list(queryWrapper);
        return Result.success(cates);
    }
}
