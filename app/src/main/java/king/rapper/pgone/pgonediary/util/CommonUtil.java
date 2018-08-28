package king.rapper.pgone.pgonediary.util;

import android.app.ActivityManager;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 公用工具类
 */
public class CommonUtil {


    private static DecimalFormat df = new DecimalFormat("######0.00");

    public static final double DISTANCE = 0.0001;

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return "";
    }

    /**
     * 验证手机号是否正确(全是数字)
     *
     * @return 登录为true
     */
    public static String getNum(String tel) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(tel);
        LogUtils.e(m.replaceAll("").trim());
        return m.replaceAll("").trim();
    }

    /**
     * 设置EditText为价钱输入模式
     *
     * @param editText 相应的EditText
     * @param digits   限制的小数位数
     */
    public static void setPriceMode(final EditText editText, final int digits) {
//        设置输入类型为小数数字，允许十进制小数点提供分数值。
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
//        给EditText设置文本变动监听事件
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                如果文本包括"."，删除“.”后面超过2位后的数据
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > digits) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + digits + 1);
                        editText.setText(s);
                        editText.setSelection(s.length()); //光标移到最后
                    }
                }
//                未输入数字的情况下输入小数点时，个位数字自动补零
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }
//                如果文本以"0"开头并且第二个字符不是"."，不允许继续输入
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

    }

    /**
     * 检查是否有有效数据
     *
     * @return 有数据  false   空 true
     */
    public static boolean checkEmpty(EditText et) {
        if (et != null) {
            String s = et.getText().toString();
            if (!TextUtils.isEmpty(s)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取文本框内容
     *
     * @return 有数据  true   空false
     */
    public static String getText(EditText et) {
        if (checkEmpty(et)) {
            String s = et.getText().toString().toString().trim();
            return s;
        }
        return "";
    }

    public static int getTextLength(EditText et) {
        if (checkEmpty(et)) {
            String s = et.getText().toString().toString().trim();
            return s.length();
        }
        return 0;
    }


    /**
     * 获取当前时间戳(单位：秒)
     *
     * @return
     */
    public static long getCurrentTime() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 校验double数值是否为0
     *
     * @param value
     * @return
     */
    public static boolean isEqualToZero(double value) {
        return Math.abs(value - 0.0) < 0.01 ? true : false;
    }

    /**
     * 经纬度是否为(0,0)点
     *
     * @return
     */
    public static boolean isZeroPoint(double latitude, double longitude) {
        return isEqualToZero(latitude) && isEqualToZero(longitude);
    }

    /**
     * 将字符串转为时间戳
     */
    public static long toTimeStamp(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.CHINA);
        Date date;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        return date.getTime() / 1000;
    }

    public static String formatSecond(int second) {
        String format = "%1$,02d:%2$,02d:%3$,02d";
        Integer hours = second / (60 * 60);
        Integer minutes = second / 60 - hours * 60;
        Integer seconds = second - minutes * 60 - hours * 60 * 60;
        Object[] array = new Object[]{hours, minutes, seconds};
        return String.format(format, array);
    }

    /**
     * “###.00”格式
     * @param doubleValue
     * @return
     */
    public static final String formatDouble(double doubleValue) {
        return df.format(doubleValue);
    }

    /**
     * 计算x方向每次移动的距离
     */
        public static double getXMoveDistance(double slope) {
        if (slope == Double.MAX_VALUE) {
            return DISTANCE;
        }
        return Math.abs((DISTANCE * slope) / Math.sqrt(1 + slope * slope));
    }
}
