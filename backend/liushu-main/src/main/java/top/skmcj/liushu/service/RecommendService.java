package top.skmcj.liushu.service;

import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

/**
 * 推荐相关接口
 */
public interface RecommendService {

    DataModel getDataModel();

    GenericUserBasedRecommender getUserRecommender(DataModel dataModel);

    GenericItemBasedRecommender getItemRecommender(DataModel dataModel);
}
