package king.rapper.pgone.pgonediary.util.constant;

import android.os.Environment;

/**
 * @ Create_time: 2018/7/4 on 9:24.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
public class Config {
    public static final boolean DEBUG = true;
    public static String LogTag = "ApiLog";
    public static CharSequence NetError = "网络错误";
    public static String GIF = ".gif";
    public static String LocalPath = Environment.getExternalStorageDirectory() + "/PGOneDiary";
    public static final int ConventionLong = 30;


    /***************************设置宽高***************************/
    //默认，屏幕大小
    public static int DefaultWidthSize = 1080;
    public static int DefaultHeightSize = 1920;
    //插入表情大小
    public static int ExpressionWidthSize = 30;
    public static int ExpressionHeightSize = 30;
    //插入图片大小
    public static int PictureWidthSize = 100;
    public static int PictureHeightSize = 100;

    /***************************设置宽高***************************/




    /***************************回调信息***************************/
    //成功信息
    public static String Message_Success = "success";

    /***************************回调信息***************************/
}
