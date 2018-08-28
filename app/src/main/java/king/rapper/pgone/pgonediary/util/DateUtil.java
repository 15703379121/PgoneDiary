package king.rapper.pgone.pgonediary.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @ Create_time: 2018/7/19 on 9:43.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
public class DateUtil {

    public static final String FormatDate = "yyyy-MM-dd";
    public static final String FormatTime = "yyyy-MM-dd HH:mm:ss";
    public static final String HMTime = "HH:mm";
    public static final String MDTime = "MM-dd";
    private static SimpleDateFormat simpleDateFormat;


    private DateUtil() {
        throw new AssertionError();
    }

    public static String getTimes(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    public static String getNowTime() {
        if (simpleDateFormat == null)
            simpleDateFormat = new SimpleDateFormat(HMTime);
        return getTimes(getTimeLong(), simpleDateFormat);
    }

    /**
     * 获取当前时间的时间戳
     *
     * @return
     */
    public static Long getTimeLong() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前日期 —— yyyy-MM-dd
     *
     * @return
     */
    public static String getDateString() {
        return new SimpleDateFormat(FormatDate).format(getTimeLong());
    }

    /**
     * 日期格式字符串转换成时间戳
     *
     * @return
     */
    public static Long date2TimeStamp(String date_str) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(FormatTime);
            return sdf.parse(date_str).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前日期 —— yyyy-MM-dd
     *
     * @return
     */
    public static String getDateString(Long date) {
        return new SimpleDateFormat(FormatDate).format(date);
    }

    public static String long2Cal2String(Long date) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTimeInMillis(date);
        int year = calendar.get(Calendar.YEAR);//先指定年份
        int month = calendar.get(Calendar.MONTH) + 1;//再指定月份 Java月份从0开始算
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        LogUtils.e("long2Cal2String:" + year + "-" + month + "-" + day);
        return year + "-" + month + "-" + day;
    }


    /**
     * 获取当前日期 —— yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getTimeString(Long date) {
        return new SimpleDateFormat(FormatTime).format(date);
    }


    /**
     * 获取当前日期 —— yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getTimeString() {
        return new SimpleDateFormat(FormatTime).format(getTimeLong());
    }

    /**
     * 获取当前日期 —— MM-dd
     *
     * @return
     */
    public static String getMDTimeString(Long date) {
        return new SimpleDateFormat(MDTime).format(date);
    }

    /**
     * 获取当前日期 —— MM-dd
     *
     * @return
     */
    public static String getMDTimeString() {
        return new SimpleDateFormat(MDTime).format(getTimeLong());
    }

    /**
     * 获取当前时间 —— mm:ss
     *
     * @return
     */
    public static String getHMTimeString(Long date) {
        return new SimpleDateFormat(HMTime).format(date);
    }

    /**
     * 获取当前时间的年月日
     *
     * @return
     */
    public static int[] getDateYMD() {
        Long date = getTimeLong();
        if (date != null && date > 0) {
            return getDateYMD(date);
        }
        return null;
    }

    /**
     * 获取所需时间的年月日星期
     *
     * @return
     */
    public static int[] getDateYMD(Long date) {
        int[] ymd = new int[4];
        if (date != null && date > 0) {
            String dateString = getDateString(date);
            if (!TextUtils.isEmpty(dateString)) {
                String[] ymdStr = dateString.split("-");
                if (ymdStr != null && ymdStr.length == 3) {
                    for (int i = 0; i < ymdStr.length; i++) {
                        ymd[i] = Integer.parseInt(ymdStr[i]);
                    }
                    Calendar c = Calendar.getInstance();
                    c.setTime(new Date(date));
                    int dayForWeek = 0;
                    if (c.get(Calendar.DAY_OF_WEEK) == 1) {
                        dayForWeek = 7;
                    } else {
                        dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
                    }
                    ymd[3] = dayForWeek;
                }
            }
        }
        return null;
    }


    /**
     * 获取所需时间的年月日星期
     *
     * @return
     */
    public static String[] getDateYMDStr(Long date) {
        String[] dateYMDStr = new String[4];
        if (date != null && date > 0) {
            String dateString = getDateString(date);
            if (!TextUtils.isEmpty(dateString)) {
                String[] ymdStr = dateString.split("-");
                if (ymdStr != null && ymdStr.length == 3) {
                    for (int i = 0; i < ymdStr.length; i++) {
                        dateYMDStr[i] = ymdStr[i];

                    }
                    //获取星期
                    dateYMDStr[3] = getWeekStr(date);
                    return dateYMDStr;
                }
            }
        }
        return null;
    }


    /**
     * 获取所需时间的年月日星期
     *
     * @return
     */
    public static String[] getDateYMDStr() {
        Long date = getTimeLong();
        if (date != null && date > 0) {
            return getDateYMDStr(date);
        }
        return null;
    }

    /**
     * 获取星期 返回值为   一二三四五六日
     */
    public static String getWeekStr(Long date) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(date));

        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return "日";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            return "一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
            return "二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
            return "三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
            return "四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            return "五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            return "六";
        }
        return null;
    }


    /**
     * 使用用户格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @param pattern 日期格式
     * @return
     */

    public static Date parse(String strDate, String pattern) {

        if (TextUtils.isEmpty(strDate)) {
            return null;
        }
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据时间戳获得星期
     *
     * @return
     */
    public static String getWeekOfLongTime(Long timeStamp) {
        String[] weekDaysName = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

        Calendar cd = Calendar.getInstance();
        cd.setTime(new Date(timeStamp));
        int mydate = cd.get(Calendar.DAY_OF_WEEK) - 1;

        return weekDaysName[mydate];
    }

    /**
     * 根据日期获得星期
     *
     * @param date
     * @return
     */
    public static String getWeekOfDate(Date date) {
        String[] weekDaysName = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        // String[] weekDaysCode = { "0", "1", "2", "3", "4", "5", "6" };
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDaysName[intWeek];
    }

    /**
     * 使用用户格式格式化日期
     *
     * @param date    日期
     * @param pattern 日期格式
     * @return
     */

    public static String format(Date date, String pattern) {
        String returnValue = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            returnValue = df.format(date);
        }
        return (returnValue);
    }

    public static int getNowHour() {
        String hh = getTime(getCurrentTimeInLong(), new SimpleDateFormat("HH"));
        int hour = Integer.parseInt(hh);
        return hour;
    }

    public static String getToday() {
        String today = getTime(getCurrentTimeInLong(), new SimpleDateFormat("MM-dd"));
        return today;
    }

    public static String getTomorrow() {
        String today = getTime(getCurrentTimeInLong() + 86400000, new SimpleDateFormat("MM-dd"));
        return today;
    }


    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    public static long getTommoryTimeInLong() {
        return System.currentTimeMillis() + 86400000;
    }

    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    static public String getNowStr(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String now = sdf.format(new Date());
        return now;
    }

    static public Date getFormatDate(String date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date d = new Date();
        try {
            d = sdf.parse(date);
        } catch (ParseException e) {
            LogUtils.e(e + "");
        }
        return d;
    }

    static public String getDateStr(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String d = sdf.format(date);
        return d;
    }

    static public String getPadZeroString(String s, int size) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < (size - s.length()); i++) {
            sb.append("0");
        }
        sb.append(s);
        return sb.toString();
    }

    /**
     * 得到某月的天数
     *
     * @param year
     * @param month
     * @return
     */
    static public int getDayCountOfMonth(String year, String month) {
        Calendar cal = Calendar.getInstance();
        // 年
        cal.set(Calendar.YEAR, Integer.parseInt(year));
        // 月，因为Calendar里的月是从0开始，所以要-1
        cal.set(Calendar.MONTH, Integer.parseInt(month) - 1);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    static public String getYesterday(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Calendar now = Calendar.getInstance();
        now.roll(Calendar.DAY_OF_YEAR, -1); //昨天
        return df.format(now.getTime());
    }

    /**
     * 获取和今天附近的某天
     *
     * @param format
     * @param diff
     * @return
     */
    static public String getADay(String format, int diff) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Calendar now = Calendar.getInstance();
        int beforeM = now.get(Calendar.MONTH);
        now.roll(Calendar.DAY_OF_YEAR, diff); //
        int nowM = now.get(Calendar.MONTH);
        //必须进行日期处理，否则2009-01-04日前七天是2009-12-28
        if (nowM > beforeM) {
            now.roll(Calendar.YEAR, -1);
        }
        return df.format(now.getTime());
    }

    static public String getTomorrow(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Calendar now = Calendar.getInstance();
        now.roll(Calendar.DAY_OF_YEAR, 1); //明天
        return df.format(now.getTime());
    }

    /**
     * 得到最近num天的全部日期
     * 说明:
     * 1.日期是从昨天开始算的.
     * 2.如果num=2 , 日期是2008-03-14 ,则返回的结果为 2008-03-12、2008-03-13
     *
     * @param num
     * @return
     */
    public static String[] getDaysByNum(int num, String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String[] result = {};
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDateFromString(date, "yyyy-MM-dd"));
        //最近一周
        result = new String[num];
        for (int i = num; i > 0; i--) {
            cal.add(Calendar.DAY_OF_YEAR, -1);
            result[i - 1] = sdf.format(new Date(cal.getTimeInMillis()));
        }
        return result;
    }

    public static Date getDateFromString(String dateStr, String format) {
        if ((dateStr == null) || (format == null)) {
            try {
                throw new Exception("数据类型异常" + dateStr + "|" + format);
            } catch (Exception e) {
                LogUtils.e("数据类型异常:" + e);
            }
        }
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date date;
        try {
            date = df.parse(dateStr);
            return date;
        } catch (Exception ex) {
            LogUtils.e("" + ex.toString());
            return new Date();
        }
    }

    static public int getNowYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    static public int getNowMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MONTH) + 1;
    }

    public static String[] getMonthRang(String year, String month) {
        String beginDate = year + "-" + month + "-01";
        String endDate = year + "-" + month + "-" +
                getDayCountOfMonth(year, month);
        return getDaysByRang(beginDate, endDate);
    }

    public static String[] getDaysByRang(String beginDate, String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //得到两个日期间相差多少天
        int num = dateDiff(beginDate, endDate);
        if (num < 0) {
            //颠倒一下日期
            String tmp = beginDate;
            beginDate = endDate;
            endDate = tmp;
            num = 0 - num;
        }
        String[] result = {};
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(beginDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        num = num + 1; //把开始和结束日期都包含进去
        result = new String[num];
        for (int i = 0; i < num; i++) {
            if (i > 0) {
                cal.add(Calendar.DAY_OF_YEAR, 1);
            }
            result[i] = sdf.format(new Date(cal.getTimeInMillis()));
        }
        return result;
    }

    public static int dateDiff(String beginDate, String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(endDate);
        } catch (ParseException e) {
            date = new Date();
            e.printStackTrace();
        }
        long end = date.getTime();
        try {
            date = sdf.parse(beginDate);
        } catch (ParseException e) {
            date = new Date();
            e.printStackTrace();
        }
        long begin = date.getTime();
        long day = (end - begin) / (1000 * 3600 * 24); //除1000是把毫秒变成秒
        return Integer.parseInt(Long.toString(day));
    }

    /**
     * 1转一
     *
     * @param week
     */
    public static String formatWeek(int week) {
        String weekStr = "";
        switch (week) {
            case 2:
                weekStr = "星期一";
                break;
            case 3:
                weekStr = "星期二";
                break;
            case 4:
                weekStr = "星期三";
                break;
            case 5:
                weekStr = "星期四";
                break;
            case 6:
                weekStr = "星期五";
                break;
            case 7:
                weekStr = "星期六";
                break;
            case 1:
                weekStr = "星期日";
                break;
        }
        LogUtils.e("WEEK转换：" + weekStr);
        return weekStr;
    }

    /**
     * 获取 昨天1997-01-01 00:00:00 时间戳
     *
     * @return
     */
    public static Long getYesterdayLong() {
        Date dNow = new Date();   //当前时间
        Date dBefore = new Date();
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(dNow);//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, -1);  //设置为前一天
        dBefore = calendar.getTime();   //得到前一天的时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
        String defaultStartDate = sdf.format(dBefore);    //格式化前一天
        defaultStartDate = defaultStartDate + " 00:00:00";
        String defaultEndDate = defaultStartDate.substring(0, 10) + " 23:59:59";
        System.out.println("前一天的起始时间是：" + defaultStartDate);
        System.out.println("前一天的结束时间是：" + defaultEndDate);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = simpleDateFormat.parse(defaultStartDate);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Long(0);
    }

    public static boolean isYesterday(Long phpLong) {
        Long timeLong = phpLong * 1000;
        Long yesterdayLong = getYesterdayLong();
        if (yesterdayLong == null || timeLong == null || yesterdayLong == 0 || timeLong == 0) {
            return false;
        } else {
            if (timeLong < yesterdayLong) {
                //昨天00：00之前
                return false;
            } else {
                //昨天00：00之后
                return true;
            }
        }
    }

    public static String getFormatMinute(Long minute) {
        if (minute < 60) {
            return minute + "分钟";
        } else {
            Long h = minute / 60;
            Long m = minute % 60;
            if (m > 0)
                return h + "小时" + m + "分钟";
            else
                return h + "小时";
        }
    }

    /**
     * 某月的第一天及最后一天
     */
    public static Long[] getMonthTime(Integer year, Integer month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);//先指定年份
        cal.set(Calendar.MONTH, month - 1);//再指定月份 Java月份从0开始算
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Integer daysCountOfMonth = cal.getActualMaximum(Calendar.DATE);//获取指定年份中指定月份有几天
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Long startTime = cal.getTime().getTime();//获取指定年份中指定月份第一天时间戳
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.DAY_OF_MONTH,
                cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Long endTime = cal.getTime().getTime();//获取指定年份中指定月份最后一天时间戳
        LogUtils.e("daysCountOfMonth:" + daysCountOfMonth + "    startTime:" + startTime + "   endTime:" + endTime);
        return new Long[]{startTime, endTime};
    }

    /**
     * 某天的第一时间和最后时间
     */
    public static Long[] getDayTime(Integer year, Integer month, Integer day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);//先指定年份
        cal.set(Calendar.MONTH, month - 1);//再指定月份 Java月份从0开始算
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Integer daysCountOfMonth = cal.getActualMaximum(Calendar.DATE);//获取指定年份中指定月份有几天
        Long startTime = cal.getTime().getTime();//获取指定年份中指定月份第一天时间戳
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Long endTime = cal.getTime().getTime();//获取指定年份中指定月份最后一天时间戳
        LogUtils.e("daysCountOfMonth:" + daysCountOfMonth + "    startTime:" + startTime + "   endTime:" + endTime);
        return new Long[]{startTime, endTime};
    }

    /**
     * 某天的第一时间和最后时间
     */
    public static Long[] getDayTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Integer daysCountOfMonth = cal.getActualMaximum(Calendar.DATE);//获取指定年份中指定月份有几天
        Long startTime = cal.getTime().getTime();//获取指定年份中指定月份第一天时间戳
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Long endTime = cal.getTime().getTime();//获取指定年份中指定月份最后一天时间戳
        LogUtils.e("daysCountOfMonth:" + daysCountOfMonth + "    startTime:" + startTime + "   endTime:" + endTime);
        return new Long[]{startTime, endTime};
    }

}
