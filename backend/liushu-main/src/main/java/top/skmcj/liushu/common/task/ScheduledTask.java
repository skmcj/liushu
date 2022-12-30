package top.skmcj.liushu.common.task;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.skmcj.liushu.common.GlobalData;
import top.skmcj.liushu.util.GlobalDataUtil;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@EnableScheduling
public class ScheduledTask {

    @Autowired
    private GlobalDataUtil globalDataUtil;

    /**
     * 每月 1 号 00:00:00 执行
     */
    @Async("scheduledTaskPool")
    @Scheduled(cron = "0 0 0 1 * ?")
    public void taskMonthly() {
        log.info("每月任务执行 =>" + System.currentTimeMillis());
        // 统计前一个月订单，计算各个图书的月借阅量及总借阅量
        // 统计前一个月订单，计算各个商家的月借阅量
    }

    /**
     * 每日 00:00:00 执行
     */
    /*@Async
    @Scheduled(cron = "0 38 17 * * ?")
    public void taskDaily() {
        log.info("每日任务执行 =>" + System.currentTimeMillis());
    }*/

    /**
     * 计算图书喜欢度
     * 每天7、15、23点执行
     */
    /*@Async
    @Scheduled(cron = "0 27 7,15,23 ? * ?")
    public void taskCalculateBookPreference() {
        // 取出之前的喜好度
        // 保存缓存中的喜好度
        // 根据之前的喜好度计算图书相似矩阵
    }*/

    /**
     * 每 15 分钟 执行
     */
    @Async("scheduledTaskPool")
    @Scheduled(fixedRate = 900000)
    public void taskMinute() {
        log.info("每15分钟执行 =>" + System.currentTimeMillis());
        globalDataUtil.updateRecommender();
    }
}
