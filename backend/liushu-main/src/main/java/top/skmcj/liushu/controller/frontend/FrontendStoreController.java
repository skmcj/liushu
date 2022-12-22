package top.skmcj.liushu.controller.frontend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.skmcj.liushu.common.Result;
import top.skmcj.liushu.entity.Bookstore;
import top.skmcj.liushu.service.BookstoreService;
import top.skmcj.liushu.util.CommonUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/frontend/store")
public class FrontendStoreController {

    @Autowired
    private BookstoreService storeService;

    /**
     * TODO: 商家推荐接口
     * 由于当前数据库商家数量较少，没有必要开发推荐接口
     * 当商家数量达到一定数量级时，实现推荐接口
     */

    /**
     * 获取推荐商家
     * 随机获取
     * @param size
     * @return
     */
    @GetMapping
    public Result<List<Bookstore>> getStoreRecommend(int size, HttpServletRequest request) {
        List<Bookstore> randomStores = storeService.getStoreByRandom(size);
        String coverDomain = CommonUtil.getImgDoMain(request);
        randomStores.stream().forEach(item -> {
            item.setCoverUrl(coverDomain + item.getCover());
        });
        return Result.success(randomStores);
    }
}
