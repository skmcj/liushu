package top.skmcj.liushu.common.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.skmcj.liushu.util.GlobalDataUtil;
import top.skmcj.liushu.util.TimingWheelUtil;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
@Component
public class AppLifeTask {

    @Autowired
    private GlobalDataUtil globalDataUtil;

    @Autowired
    private TimingWheelUtil timingWheelUtil;

    @PostConstruct
    public void start() {
        log.info(" => 项目启动了");
        // globalDataUtil.updateRecommender();
        // 初始化订单监听任务
        timingWheelUtil.initListenerTask();
    }

    @PreDestroy
    public void destroy() {
        log.info(" => 项目销毁了");
    }
}
