package top.skmcj.liushu.common;

import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局数据
 */
@Component
public class GlobalData {

    /**
     * 喜好度字典
     */
    private static Map<Long, HashMap<Long, Integer>> userPreferenceMap = new HashMap<>();

    /**
     * 基于用户的协同过滤推荐器
     */
    private static GenericUserBasedRecommender userRecommender;

    /**
     * 基于物品的协同过滤推荐器
     */
    private static GenericItemBasedRecommender itemRecommender;

    /**
     * 获取基于用户的协同过滤推荐器
     * @return
     */
    public static GenericUserBasedRecommender getUserRecommender() {
        return userRecommender;
    }

    /**
     * 设置基于用户的协同过滤推荐器
     * @param userRecommender
     */
    public static void setUserRecommender(GenericUserBasedRecommender userRecommender) {
        GlobalData.userRecommender = userRecommender;
    }

    /**
     * 获取基于物品的协同过滤推荐器
     * @return
     */
    public static GenericItemBasedRecommender getItemRecommender() {
        return itemRecommender;
    }

    /**
     * 设置基于物品的协同过滤推荐器
     * @param itemRecommender
     */
    public static void setItemRecommender(GenericItemBasedRecommender itemRecommender) {
        GlobalData.itemRecommender = itemRecommender;
    }

    /**
     * 获取喜好度字典
     * @return
     */
    public static Map<Long, HashMap<Long, Integer>> getUserPreferenceMap() {
        return userPreferenceMap;
    }

    /**
     * 添加喜好度记录
     * @param userId
     * @param bookId
     * @param score
     */
    public static void addBookPreferenceOfUser(Long userId, Long bookId, Integer score) {
        if(userPreferenceMap.containsKey(userId)) {
            HashMap<Long, Integer> bookPreference = userPreferenceMap.get(userId);
            if(bookPreference.containsKey(bookId)) {
                Integer oldScore = bookPreference.get(bookId);
                bookPreference.put(bookId, oldScore + score);
            } else {
                bookPreference.put(bookId, score);
            }
        } else {
            HashMap<Long, Integer> bookPreference = new HashMap<>();
            bookPreference.put(bookId, score);
            userPreferenceMap.put(userId, bookPreference);
        }
    }
}
