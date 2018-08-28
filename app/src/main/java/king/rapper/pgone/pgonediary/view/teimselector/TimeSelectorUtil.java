package king.rapper.pgone.pgonediary.view.teimselector;

import android.content.Context;

import king.rapper.pgone.pgonediary.util.DateUtil;
import king.rapper.pgone.pgonediary.util.LogUtils;

/**
 * @ 创建时间: 2018/4/19 on 15:08.
 * @ 描述：时间选择器
 * @ 作者: 郑卫超 QQ: 2318723605
 */
public class TimeSelectorUtil {

    /**
     * 底部滚轮点击事件回调
     */
    public interface OnWheelViewClick {
        void onClick(String time);
    }

    /**
     * 弹出底部滚轮选择
     *
     * @param context    上下文
     * @param start_time 每天开始服务时间
     * @param end_time   每天结束服务时间
     * @param click      回调
     */
    public static void alertBottomWheelOption(Context context, int start_time, int end_time, final OnWheelViewClick click) {
//        timerSelector1(context,start_time,end_time,click);
        timerSelector2(context, start_time, end_time, click);

    }

    private static void timerSelector2(Context context, int start_time, int end_time, OnWheelViewClick click) {
        NewTimeSelector timeSelector = new NewTimeSelector(context, start_time, end_time, click);
        timeSelector.setIsLoop(false);
        timeSelector.show();
    }

    public static String getNearlyTime(int start_time, int end_time) {
        LogUtils.e("start_time:" + start_time + "   end_time:" + end_time);
        if (DateUtil.getNowHour() < (start_time)) {
            return DateUtil.getToday()+ "(" + DateUtil.getWeekOfLongTime(DateUtil.getCurrentTimeInLong()) + ") " + start_time + ":00-" + (start_time + 1) + ":00";
        } else if (DateUtil.getNowHour() >= (end_time - 1)) {
            return DateUtil.getTomorrow() + "("+DateUtil.getWeekOfLongTime(DateUtil.getTommoryTimeInLong()) + ") " + start_time + ":00-" + (start_time + 1) + ":00";
        } else {
            return DateUtil.getToday() + "("+ DateUtil.getWeekOfLongTime(DateUtil.getCurrentTimeInLong()) + ") " + (DateUtil.getNowHour() + 1) + ":00-" + (DateUtil.getNowHour() + 2) + ":00";
        }
    }
}
