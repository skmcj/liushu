package top.skmcj.liushu.service.impl;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericPreference;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.skmcj.liushu.entity.UserPreference;
import top.skmcj.liushu.service.RecommendService;
import top.skmcj.liushu.service.UserPreferenceService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 图书推荐操作实现
 */
@Service
public class RecommendServiceImpl implements RecommendService {

    @Value("${liushu.mahout.user.nearest-num}")
    private int userNearestNum;

    @Autowired
    private UserPreferenceService preferenceService;

    /**
     * 获取用户相似度模型
     * @return
     */
    @Override
    public DataModel getDataModel() {
        List<UserPreference> list = preferenceService.list();
        FastByIDMap<PreferenceArray> fastByIDMap = new FastByIDMap<>();
        list.stream()
                .collect(Collectors.groupingBy(UserPreference::getUserId))
                .values()
                .stream()
                .map(userPreferences -> userPreferences.stream().map(item -> new GenericPreference(item.getUserId(), item.getBookId(),
                            item.getScore())).toArray(GenericPreference[]::new)
                )
                .forEach(preferenceArr -> {
                    fastByIDMap.put(preferenceArr[0].getUserID(),
                            new GenericUserPreferenceArray(Arrays.asList(preferenceArr)));
                });
        return new GenericDataModel(fastByIDMap);
    }

    /**
     * 获取基于用户的协同过滤推荐器
     * @param dataModel
     * @return
     */
    @Override
    public GenericUserBasedRecommender getUserRecommender(DataModel dataModel) {
        GenericUserBasedRecommender userRecommender = null;
        try {
            UserSimilarity userSimilarity = new PearsonCorrelationSimilarity(dataModel);
            UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(userNearestNum, userSimilarity, dataModel);
            userRecommender = new GenericUserBasedRecommender(dataModel, userNeighborhood, userSimilarity);
        } catch (TasteException e) {
            e.printStackTrace();
        }
        return userRecommender;
    }

    /**
     * 获取基于物品的协同过滤推荐器
     * @param dataModel
     * @return
     */
    @Override
    public GenericItemBasedRecommender getItemRecommender(DataModel dataModel) {
        GenericItemBasedRecommender itemRecommender = null;
        try {
            ItemSimilarity itemSimilarity = new PearsonCorrelationSimilarity(dataModel);
            itemRecommender = new GenericItemBasedRecommender(dataModel, itemSimilarity);
        } catch (TasteException e) {
            e.printStackTrace();
        }
        return itemRecommender;
    }


}
