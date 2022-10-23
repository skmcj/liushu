package top.skmcj.liushu.util;

public class BaseConvertUtil {

    private static String baseArr = "0123456789abcdefghijklmnopqrstuvwxyz";

    /**
     * 转换10进制数字到16进制数字
     * @param num
     * @return
     */
    public static String convert10To36(Long num) {
        StringBuilder ans = new StringBuilder();
        boolean isMinus = false;
        if(num < 0) isMinus = true;
        num = Math.abs(num);
        while (num != 0) {
            ans.append(baseArr.charAt((int) (num % 36)));
            num /= 36;
        }
        if(isMinus) ans.append('-');
        return ans.reverse().toString();
    }
}
