package king.rapper.pgone.pgonediary.util;

/**
 * @ Create_time: 2018/7/17 on 14:31.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
public class ColorUtil {

    /**
     * Color的Int整型转Color的16进制颜色值【方案一】
     * colorInt - -12590395
     * return Color的16进制颜色值——#3FE2C5
     */
    public static String int2Hex(int colorInt) {
        String hexCode = "";
        hexCode = String.format("#%06X", Integer.valueOf(16777215 & colorInt));
        return hexCode;
    }
}
