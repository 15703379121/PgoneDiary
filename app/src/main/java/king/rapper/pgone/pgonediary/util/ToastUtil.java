package king.rapper.pgone.pgonediary.util;

import android.content.Context;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import king.rapper.pgone.pgonediary.MyApp;
import king.rapper.pgone.pgonediary.R;
import king.rapper.pgone.pgonediary.util.constant.Config;

/**
 * @ 创建时间: 2017/10/2 on 8:29.
 * @ 描述：吐司工具类
 */

public class ToastUtil {
    private static Toast toast;

    /**
     * 短时间显示  Toast
     *
     * @param context
     * @param sequence
     */
    public static void showShort(Context context, CharSequence sequence) {
        try {
            if (toast == null) {
                toast = new Toast(context);
            }
            toast.setGravity(Gravity.BOTTOM, 0, 120);
            View v = LayoutInflater.from(context).inflate(R.layout.base_toast_layout, null);
            TextView tv2 = (TextView) v.findViewById(R.id.tv_toast);
            tv2.setText(sequence);
            toast.setView(v);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
            LogUtils.e("  吐司：  " + sequence);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("我也很纳闷儿为啥崩了");
        }

    }

    /**
     * 短时间显示  Toast
     *
     * @param sequence
     */
    public static void showShort(final CharSequence sequence) {
        try {
            LogUtils.e("  吐司前 log ：  " + sequence);
            if (toast == null) {
                toast = new Toast(MyApp.getInstance());
            }
            toast.setGravity(Gravity.BOTTOM, 0, 120);
            View v = LayoutInflater.from(MyApp.getInstance()).inflate(R.layout.base_toast_layout, null);
            TextView tv2 = (TextView) v.findViewById(R.id.tv_toast);
            tv2.setText(sequence);
            toast.setView(v);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
            LogUtils.e("  吐司：  " + sequence);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("我也很纳闷儿为啥崩了");
            try {
                Looper.prepare();
                Toast.makeText(MyApp.getInstance(), sequence, Toast.LENGTH_SHORT).show();
                Looper.loop();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * 显示 没有网络链接的 Toast
     */
    public static void showNetError() {
        showShort(Config.NetError);
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, int message) {
        if (toast == null) {
            toast = new Toast(context);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        View v = LayoutInflater.from(context).inflate(R.layout.base_toast_layout, null);
        TextView tv2 = (TextView) v.findViewById(R.id.tv_toast);
        tv2.setText(message);
        toast.setView(v);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message) {
        if (toast == null) {
            toast = new Toast(context);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        View v = LayoutInflater.from(context).inflate(R.layout.base_toast_layout, null);
        TextView tv2 = (TextView) v.findViewById(R.id.tv_toast);
        tv2.setText(message);
        toast.setView(v);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, int message) {
        if (toast == null) {
            toast = new Toast(context);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        View v = LayoutInflater.from(context).inflate(R.layout.base_toast_layout, null);
        TextView tv2 = (TextView) v.findViewById(R.id.tv_toast);
        tv2.setText(message);
        toast.setView(v);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * 自定义显示时间
     *
     * @param context
     * @param sequence
     * @param duration
     */
    public static void show(Context context, CharSequence sequence, int duration) {
        if (toast == null) {
            toast = new Toast(context);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        View v = LayoutInflater.from(context).inflate(R.layout.base_toast_layout, null);
        TextView tv2 = (TextView) v.findViewById(R.id.tv_toast);
        tv2.setText(sequence);
        toast.setView(v);
        toast.setDuration(duration);
        toast.show();

    }

    /**
     * 隐藏toast
     */
    public static void hideToast() {
        if (toast != null) {
            toast.cancel();
        }
    }

}
