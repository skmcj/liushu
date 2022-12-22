package top.skmcj.liushu.util;

import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.skmcj.liushu.common.GlobalData;
import top.skmcj.liushu.service.RecommendService;

/**
 * 操作全局数据的工具
 */
@Component
public class GlobalDataUtil {

    @Autowired
    private RecommendService recommendService;

    /**
     * 更新用户推荐器
     */
    public void updateRecommenderByUser() {
        DataModel dataModel = recommendService.getDataModel();
        GenericUserBasedRecommender userRecommender = recommendService.getUserRecommender(dataModel);
        GlobalData.setUserRecommender(userRecommender);
    }

    /**
     * 更新物品推荐器
     */
    public void updateRecommenderByItem() {
        DataModel dataModel = recommendService.getDataModel();
        GenericItemBasedRecommender itemRecommender = recommendService.getItemRecommender(dataModel);
        GlobalData.setItemRecommender(itemRecommender);
    }

    /**
     * 更新推荐器
     */
    public void updateRecommender() {
        DataModel dataModel = recommendService.getDataModel();
        GenericUserBasedRecommender userRecommender = recommendService.getUserRecommender(dataModel);
        GlobalData.setUserRecommender(userRecommender);
        GenericItemBasedRecommender itemRecommender = recommendService.getItemRecommender(dataModel);
        GlobalData.setItemRecommender(itemRecommender);
    }
}
