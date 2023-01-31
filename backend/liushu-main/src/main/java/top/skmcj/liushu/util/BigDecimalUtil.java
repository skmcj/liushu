package top.skmcj.liushu.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 操作计算BigDecimal的工具类
 * 自动保留2位小数
 */
public class BigDecimalUtil {

    /**
     * 加
     * @param value
     * @param inc
     * @return
     */
    public static BigDecimal add(BigDecimal value, BigDecimal inc) {
        return value.add(inc).setScale(2, RoundingMode.HALF_DOWN);
    }

    /**
     * 加
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static BigDecimal add(BigDecimal a, BigDecimal b, BigDecimal c) {
        return a.add(b).add(c).setScale(2, RoundingMode.HALF_DOWN);
    }

    /**
     * 加
     * @param a
     * @param b
     * @return
     */
    public static BigDecimal add(double a, double b) {
        BigDecimal valA = new BigDecimal(a);
        BigDecimal valB = new BigDecimal(b);
        return valA.add(valB).setScale(2, RoundingMode.HALF_DOWN);
    }

    /**
     * 减
     * @param value
     * @param minuend
     * @return
     */
    public static BigDecimal subtract(BigDecimal value, BigDecimal minuend) {
        return value.subtract(minuend).setScale(2, RoundingMode.HALF_DOWN);
    }

    /**
     * 减
     * @param a
     * @param b
     * @return
     */
    public static BigDecimal subtract(double a, double b) {
        BigDecimal valA = new BigDecimal(a);
        BigDecimal valB = new BigDecimal(b);
        return valA.subtract(valB).setScale(2, RoundingMode.HALF_DOWN);
    }

    /**
     * 乘
     * @param value
     * @param multiplicand
     * @return
     */
    public static BigDecimal multiply(BigDecimal value, BigDecimal multiplicand) {
        return value.multiply(multiplicand).setScale(2, RoundingMode.HALF_DOWN);
    }

    /**
     * 乘
     * @param value
     * @param multiplicand
     * @return
     */
    public static BigDecimal multiply(BigDecimal value, int multiplicand) {
        BigDecimal valM = new BigDecimal(multiplicand);
        return value.multiply(valM).setScale(2, RoundingMode.HALF_DOWN);
    }

    /**
     * 乘
     * @param value
     * @param multiplicand
     * @return
     */
    public static BigDecimal multiply(BigDecimal value, Integer multiplicand) {
        BigDecimal valM = new BigDecimal(multiplicand);
        return value.multiply(valM).setScale(2, RoundingMode.HALF_DOWN);
    }

    /**
     * 乘
     * @param a
     * @param b
     * @return
     */
    public static BigDecimal multiply(double a, double b) {
        BigDecimal valA = new BigDecimal(a);
        BigDecimal valB = new BigDecimal(b);
        return valA.multiply(valB).setScale(2, RoundingMode.HALF_DOWN);
    }

    /**
     * 除
     * @param value
     * @param dividend
     * @return
     */
    public static BigDecimal divide(BigDecimal value, BigDecimal dividend) {
        return value.divide(dividend).setScale(2, RoundingMode.HALF_DOWN);
    }

    /**
     * 除
     * @param a
     * @param b
     * @return
     */
    public static BigDecimal divide(double a, double b) {
        BigDecimal valA = new BigDecimal(a);
        BigDecimal valB = new BigDecimal(b);
        return valA.divide(valB).setScale(2, RoundingMode.HALF_DOWN);
    }

    /**
     * 除 - 评分
     * @param totalScore
     * @param count
     * @return
     */
    public static BigDecimal divideScore(BigDecimal totalScore, Integer count) {
        BigDecimal dCount = new BigDecimal(count);
        return totalScore.divide(dCount).setScale(1, RoundingMode.HALF_DOWN);
    }

    /**
     * a > b
     * a 大于 b
     * @param a
     * @param b
     * @return
     */
    public static boolean gt(BigDecimal a, BigDecimal b) {
        if(a == null || b == null) return false;
        if(a.compareTo(b) == 1) return true;
        return false;
    }

    /**
     * a >= b
     * a 大于等于 b
     * @param a
     * @param b
     * @return
     */
    public static boolean gte(BigDecimal a, BigDecimal b) {
        if(a == null || b == null) return false;
        if(a.compareTo(b) == 1 || a.compareTo(b) == 0) return true;
        return false;
    }

    /**
     * a < b
     * a 小于 b
     * @param a
     * @param b
     * @return
     */
    public static boolean lt(BigDecimal a, BigDecimal b) {
        if(a == null || b == null) return false;
        if(a.compareTo(b) == -1) return true;
        return false;
    }

    /**
     * a <= b
     * a 小于等于 b
     * @param a
     * @param b
     * @return
     */
    public static boolean lte(BigDecimal a, BigDecimal b) {
        if(a == null || b == null) return false;
        if(a.compareTo(b) == -1 || a.compareTo(b) == 0) return true;
        return false;
    }

    /**
     * a == b
     * a 等于 b
     * @param a
     * @param b
     * @return
     */
    public static boolean et(BigDecimal a, BigDecimal b) {
        if(a == null || b == null) return false;
        if(a.compareTo(b) == 0) return true;
        return false;
    }

}
