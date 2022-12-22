package top.skmcj.liushu.common.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.skmcj.liushu.util.GlobalDataUtil;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
@Component
public class AppLifeTask {

    @Autowired
    private GlobalDataUtil globalDataUtil;

    @PostConstruct
    public void start() {
        log.info(" => 项目启动了");
        // globalDataUtil.updateRecommender();
    }

    @PreDestroy
    public void destroy() {
        log.info(" => 项目销毁了");
    }
}
