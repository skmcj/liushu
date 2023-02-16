package top.skmcj.liushu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.skmcj.liushu.common.Result;
import top.skmcj.liushu.entity.Bookstore;
import top.skmcj.liushu.entity.Collection;
import top.skmcj.liushu.entity.User;
import top.skmcj.liushu.service.BookstoreService;
import top.skmcj.liushu.service.CollectionService;
import top.skmcj.liushu.util.CommonUtil;
import top.skmcj.liushu.util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户收藏
 */
@RestController
@RequestMapping("/collection")
public class CollectionController {

    @Autowired
    private CollectionService collectionService;

    @Autowired
    private BookstoreService storeService;

    /**
     * 查询某商家是否被用户收藏
     * @param storeId
     * @param request
     * @return
     */
    @GetMapping("/is")
    public Result<String> isCollectionOfUser(@RequestParam Long storeId, HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        User user = JwtUtil.verifyTokenOfUser(token);
        LambdaQueryWrapper<Collection> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Collection::getUserId, user.getId());
        wrapper.eq(Collection::getStoreId, storeId);
        Collection collection = collectionService.getOne(wrapper);
        if(collection == null) return Result.error("用户尚未收藏");
        return Result.success("用户已收藏");
    }

    /**
     * 分页获取用户收藏信息
     * @param currentPage
     * @param pageSize
     * @param request
     * @return
     */
    @GetMapping
    public Result<Page> getCollectionPage(int currentPage, int pageSize, HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        User user = JwtUtil.verifyTokenOfUser(token);
        Page<Collection> page = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<Collection> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Collection::getUserId, user.getId());
        collectionService.page(page, wrapper);
        List<Collection> collections = page.getRecords();
        String imgDoMain = CommonUtil.getImgDoMain(request);
        collections.stream().forEach(item -> {
            Bookstore bookstore = storeService.getById(item.getStoreId());
            item.setStoreName(bookstore.getStoreName());
            item.setStoreCover(imgDoMain + bookstore.getCover());
            item.setScore(bookstore.getScore());
            item.setLabel(bookstore.getLabel());
            item.setMba(bookstore.getMba());
        });
        return Result.success(page);
    }

    /**
     * 确认收藏
     * @param storeId
     * @param request
     * @return
     */
    @GetMapping("/add")
    public Result<String> confirmCollection(@RequestParam Long storeId, HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        User user = JwtUtil.verifyTokenOfUser(token);
        Collection collection = new Collection();
        collection.setUserId(user.getId());
        collection.setStoreId(storeId);
        boolean flag = collectionService.save(collection);
        if(!flag) return Result.error("收藏失败");
        return Result.success("收藏成功");
    }

    /**
     * 取消收藏
     * @param storeId
     * @param request
     * @return
     */
    @DeleteMapping
    public Result<String> cancelCollection(@RequestParam Long storeId, HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        User user = JwtUtil.verifyTokenOfUser(token);
        LambdaQueryWrapper<Collection> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Collection::getUserId, user.getId());
        wrapper.eq(Collection::getStoreId, storeId);
        boolean flag = collectionService.remove(wrapper);
        if(!flag) return Result.error("取消收藏失败");
        return Result.success("取消收藏成功");
    }
}
