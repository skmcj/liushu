package top.skmcj.liushu.util;

import top.skmcj.liushu.common.alg.Snowflake;

/**
 * 生成订单号工具
 */
public class NumberUtil {

    // 号码生成器
    private static Snowflake numberGenerator = new Snowflake();

    /**
     * 生成一个订单号
     * @return
     */
    public static long genOrderNumber() {
        return numberGenerator.nextId();
    }

    /**
     * 生成一个订单号
     * @return
     */
    public static String genOrderNumberStr() {
        return String.valueOf(numberGenerator.nextId());
    }
}
