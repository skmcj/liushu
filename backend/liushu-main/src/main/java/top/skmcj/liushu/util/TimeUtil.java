package top.skmcj.liushu.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * LocalDateTime工具类
 */
public class TimeUtil {

    /**
     * *************************************************
     *                    常用日期转化
     * *************************************************
     */

    /**
     * 当前时间
     * @return
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    /**
     * Date 转 LocalDateTime
     * @return
     */
    public static LocalDateTime convert(Date date) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
    }

    /**
     * LocalDateTime 转 Date
     * @return
     */
    public static Date convert(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 将日期字符串转化为 LocalDateTime
     * @param dateTimeStr yyyy-MM-ddTHH:mm:ss
     * @return
     */
    public static LocalDateTime dConvertOfLocalDateTime(String dateTimeStr) {
        return LocalDateTime.parse(dateTimeStr);
    }

    /**
     * 将日期字符串转化为 LocalDateTime
     * @param dateTimeStr yyyy-MM-dd HH:mm:ss 格式的日期字符串
     * @return
     */
    public static LocalDateTime convertOfLocalDateTime(String dateTimeStr) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateTimeStr, dtf);
    }

    /**
     * 将日期字符串转化为 LocalDateTime
     * @param dateTimeStr 日期字符串
     * @param dtfs 日期格式化字符串，例 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static LocalDateTime convertOfLocalDateTime(String dateTimeStr, String dtfs) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dtfs);
        return LocalDateTime.parse(dateTimeStr, dtf);
    }

    /**
     * 将 LocalDateTime 转化为时间戳(秒级)
     * @param dateTime
     * @return
     */
    public static long getTimeStampToSecond(LocalDateTime dateTime) {
        return dateTime.toEpochSecond(ZoneOffset.of("+8"));
    }

    /**
     * 将 LocalDateTime 转化为时间戳(毫秒级)
     * @param dateTime
     * @return
     */
    public static long getTimeStamp(LocalDateTime dateTime) {
        return dateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * *************************************************
     *          LocalDateTime 的比较工具
     * *************************************************
     */

    /**
     * 时间 a 是否大于时间 b
     * @param a
     * @param b
     * @return
     */
    public static boolean gt(LocalDateTime a, LocalDateTime b) {
        return a.compareTo(b) > 0;
    }

    /**
     * 时间 a 是否大于等于时间 b
     * @param a
     * @param b
     * @return
     */
    public static boolean gte(LocalDateTime a, LocalDateTime b) {
        return a.compareTo(b) >= 0;
    }

    /**
     * 时间 a 是否小于时间 b
     * @param a
     * @param b
     * @return
     */
    public static boolean lt(LocalDateTime a, LocalDateTime b) {
        return a.compareTo(b) < 0;
    }

    /**
     * 时间 a 是否小于等于时间 b
     * @param a
     * @param b
     * @return
     */
    public static boolean lte(LocalDateTime a, LocalDateTime b) {
        return a.compareTo(b) <= 0;
    }

    /**
     * 时间 a 是否等于时间 b
     * @param a
     * @param b
     * @return
     */
    public static boolean et(LocalDateTime a, LocalDateTime b) {
        return a.compareTo(b) == 0;
    }

    /**
     * 时间 a 与时间 b 相差多少年
     * @param a
     * @param b
     * @return b - a 年
     */
    public static int compareToYear(LocalDateTime a, LocalDateTime b) {
        int aYear = a.getYear();
        int bYear = b.getYear();
        return bYear - aYear;
    }

    /**
     * 时间 a 与时间 b 相差多少月
     * @param a
     * @param b
     * @return b - a 月
     */
    public static int compareToMonth(LocalDateTime a, LocalDateTime b) {
        int aMonth = a.getYear() * 12 + a.getMonthValue();
        int bMonth = b.getYear() * 12 + b.getMonthValue();
        return bMonth - aMonth;
    }

    /**
     * 时间 a 与时间 b 相差多少天
     * @param a
     * @param b
     * @return b - a 天
     */
    public static long compareToDay(LocalDateTime a, LocalDateTime b) {
        Duration duration = Duration.between(a, b);
        return duration.toDays();
    }

    /**
     * 时间 a 与时间 b 相差多少小时
     * @param a
     * @param b
     * @return b - a 小时
     */
    public static long compareToHour(LocalDateTime a, LocalDateTime b) {
        Duration duration = Duration.between(a, b);
        return duration.toHours();
    }

    /**
     * 时间 a 与时间 b 相差多少分钟
     * @param a
     * @param b
     * @return b - a 分钟
     */
    public static long compareToMinute(LocalDateTime a, LocalDateTime b) {
        Duration duration = Duration.between(a, b);
        return duration.toMinutes();
    }

    /**
     * 时间 a 与时间 b 相差多少秒
     * @param a
     * @param b
     * @return b - a 秒
     */
    public static long compareToSecond(LocalDateTime a, LocalDateTime b) {
        Duration duration = Duration.between(a, b);
        return duration.toSeconds();
    }

    /**
     * 时间 a 与时间 b 相差多少毫秒
     * @param a
     * @param b
     * @return b - a 毫秒
     */
    public static long compareToMillis(LocalDateTime a, LocalDateTime b) {
        Duration duration = Duration.between(a, b);
        return duration.toMillis();
    }

    /**
     * 时间 a 与时间 b 相差多少纳秒
     * @param a
     * @param b
     * @return b - a 纳秒
     */
    public static long compareToNanos(LocalDateTime a, LocalDateTime b) {
        Duration duration = Duration.between(a, b);
        return duration.toNanos();
    }



    /**
     * *************************************************
     *          一些特殊日期的开始 - 结束时间
     * *************************************************
     */

    /*-------------------- 最近时间段 -------------------*/

    /**
     * 今天开始时间
     *
     * @return
     */
    public static LocalDateTime todayStartTime() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
    }

    /**
     * 今天结束时间
     *
     * @return
     */
    public static LocalDateTime todayEndTime() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
    }

    /**
     * 昨天开始时间
     *
     * @return
     */
    public static LocalDateTime yesterdayStartTime() {
        return LocalDateTime.of(LocalDate.now().minus(1L, ChronoUnit.DAYS), LocalTime.MIN);
    }

    /**
     * 昨天结束时间
     *
     * @return
     */
    public static LocalDateTime yesterdayEndTime() {
        return LocalDateTime.of(LocalDate.now().minus(1L, ChronoUnit.DAYS), LocalTime.MAX);
    }

    /**
     * 最近7天开始时间
     * 从今天开始，往前7天
     * @return
     */
    public static LocalDateTime last7DaysStartTime() {
        return LocalDateTime.of(LocalDate.now().minus(6L, ChronoUnit.DAYS), LocalTime.MIN);
    }

    /**
     * 最近7天结束时间
     * 默认今天为7天中最后一天
     * @return
     */
    public static LocalDateTime last7DaysEndTime() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
    }

    /**
     * 最近30天开始时间
     * 从今天开始，往前30天
     * @return
     */
    public static LocalDateTime last30DaysStartTime() {
        return LocalDateTime.of(LocalDate.now().minus(29L, ChronoUnit.DAYS), LocalTime.MIN);
    }

    /**
     * 最近30天结束时间
     * 默认今天为30天中最后一天
     * @return
     */
    public static LocalDateTime last30DaysEndTime() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
    }

    /*-------------------- 季度 -------------------*/

    /**
     * 本季度开始时间
     *
     * @return
     */
    public static LocalDateTime quarterStartTime() {
        LocalDate now = LocalDate.now();
        Month month = Month.of(now.getMonth().firstMonthOfQuarter().getValue());
        return LocalDateTime.of(LocalDate.of(now.getYear(), month, 1), LocalTime.MIN);
    }

    /**
     * 本季度结束时间
     *
     * @return
     */
    public static LocalDateTime quarterEndTime() {
        LocalDate now = LocalDate.now();
        Month month = Month.of(now.getMonth().firstMonthOfQuarter().getValue()).plus(2L);
        return LocalDateTime.of(LocalDate.of(now.getYear(), month, month.length(now.isLeapYear())), LocalTime.MAX);
    }

    /**
     * 上季度开始时间
     *
     * @return
     */
    public static LocalDateTime lastQuarterStartTime() {
        LocalDate now = LocalDate.now();
        Month firstMonthOfQuarter = Month.of(now.getMonth().firstMonthOfQuarter().getValue());
        Month firstMonthOfLastQuarter = firstMonthOfQuarter.minus(3L);
        int yearOfLastQuarter = firstMonthOfQuarter.getValue() < 4 ? now.getYear() - 1 : now.getYear();
        return LocalDateTime.of(LocalDate.of(yearOfLastQuarter, firstMonthOfLastQuarter, 1), LocalTime.MIN);
    }

    /**
     * 上季度结束时间
     *
     * @return
     */
    public static LocalDateTime lastQuarterEndTime() {
        LocalDate now = LocalDate.now();
        Month firstMonthOfQuarter = Month.of(now.getMonth().firstMonthOfQuarter().getValue());
        Month firstMonthOfLastQuarter = firstMonthOfQuarter.minus(1L);
        int yearOfLastQuarter = firstMonthOfQuarter.getValue() < 4 ? now.getYear() - 1 : now.getYear();
        return LocalDateTime.of(LocalDate.of(yearOfLastQuarter, firstMonthOfLastQuarter, firstMonthOfLastQuarter.maxLength()), LocalTime.MAX);
    }

    /**
     * 下季度开始时间
     *
     * @return
     */
    public static LocalDateTime nextQuarterStartTime() {
        LocalDate now = LocalDate.now();
        Month firstMonthOfQuarter = Month.of(now.getMonth().firstMonthOfQuarter().getValue());
        Month firstMonthOfNextQuarter = firstMonthOfQuarter.plus(3L);
        int yearOfNextQuarter = firstMonthOfQuarter.getValue() > 9 ? now.getYear() + 1 : now.getYear();
        return LocalDateTime.of(LocalDate.of(yearOfNextQuarter, firstMonthOfNextQuarter, 1), LocalTime.MIN);
    }

    /**
     * 下季度结束时间
     *
     * @return
     */
    public static LocalDateTime nextQuarterEndTime() {
        LocalDate now = LocalDate.now();
        Month firstMonthOfQuarter = Month.of(now.getMonth().firstMonthOfQuarter().getValue());
        Month firstMonthOfNextQuarter = firstMonthOfQuarter.plus(5L);
        int yearOfNextQuarter = firstMonthOfQuarter.getValue() > 9 ? now.getYear() + 1 : now.getYear();
        return LocalDateTime.of(LocalDate.of(yearOfNextQuarter, firstMonthOfNextQuarter, firstMonthOfNextQuarter.maxLength()), LocalTime.MAX);
    }

    /*-------------------- 月度 -------------------*/

    /**
     * 本月开始时间
     *
     * @return
     */
    public static LocalDateTime monthStartTime() {
        return LocalDateTime.of(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()), LocalTime.MIN);
    }

    /**
     * 本月结束时间
     *
     * @return
     */
    public static LocalDateTime monthEndTime() {
        return LocalDateTime.of(LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()), LocalTime.MAX);
    }

    /**
     * 上月开始时间
     *
     * @return
     */
    public static LocalDateTime lastMonthStartTime() {
        return LocalDateTime.of(LocalDate.now().minus(1L, ChronoUnit.MONTHS).with(TemporalAdjusters.firstDayOfMonth()), LocalTime.MIN);
    }

    /**
     * 上月结束时间
     *
     * @return
     */
    public static LocalDateTime lastMonthEndTime() {
        return LocalDateTime.of(LocalDate.now().minus(1L, ChronoUnit.MONTHS).with(TemporalAdjusters.lastDayOfMonth()), LocalTime.MAX);
    }

    /**
     * 下月开始时间
     *
     * @return
     */
    public static LocalDateTime nextMonthStartTime() {
        return LocalDateTime.of(LocalDate.now().plus(1L, ChronoUnit.MONTHS).with(TemporalAdjusters.firstDayOfMonth()), LocalTime.MIN);
    }

    /**
     * 下月结束时间
     *
     * @return
     */
    public static LocalDateTime nextMonthEndTime() {
        return LocalDateTime.of(LocalDate.now().plus(1L, ChronoUnit.MONTHS).with(TemporalAdjusters.lastDayOfMonth()), LocalTime.MAX);
    }

    /*-------------------- 周度 -------------------*/

    /**
     * 本周开始时间
     *
     * @return
     */
    public static LocalDateTime weekStartTime() {
        LocalDate now = LocalDate.now();
        return LocalDateTime.of(now.minusDays(now.getDayOfWeek().getValue() - 1), LocalTime.MIN);
    }

    /**
     * 本周结束时间
     *
     * @return
     */
    public static LocalDateTime weekEndTime() {
        LocalDate now = LocalDate.now();
        return LocalDateTime.of(now.plusDays(7 - now.getDayOfWeek().getValue()), LocalTime.MAX);
    }

    /**
     * 上周开始时间
     *
     * @return
     */
    public static LocalDateTime lastWeekStartTime() {
        LocalDate lastWeek = LocalDate.now().minus(1L, ChronoUnit.WEEKS);
        return LocalDateTime.of(lastWeek.minusDays(lastWeek.getDayOfWeek().getValue() - 1), LocalTime.MIN);
    }

    /**
     * 上周结束时间
     *
     * @return
     */
    public static LocalDateTime lastWeekEndTime() {
        LocalDate lastWeek = LocalDate.now().minus(1L, ChronoUnit.WEEKS);
        return LocalDateTime.of(lastWeek.plusDays(7 - lastWeek.getDayOfWeek().getValue()), LocalTime.MAX);
    }

    /**
     * 下周开始时间
     *
     * @return
     */
    public static LocalDateTime nextWeekStartTime() {
        LocalDate nextWeek = LocalDate.now().plus(1L, ChronoUnit.WEEKS);
        return LocalDateTime.of(nextWeek.minusDays(nextWeek.getDayOfWeek().getValue() - 1), LocalTime.MIN);
    }

    /**
     * 下周结束时间
     *
     * @return
     */
    public static LocalDateTime nextWeekEndTime() {
        LocalDate nextWeek = LocalDate.now().plus(1L, ChronoUnit.WEEKS);
        return LocalDateTime.of(nextWeek.plusDays(7 - nextWeek.getDayOfWeek().getValue()), LocalTime.MAX);
    }

}
