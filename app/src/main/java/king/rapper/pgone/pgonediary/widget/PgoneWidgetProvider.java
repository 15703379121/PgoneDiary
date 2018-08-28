package king.rapper.pgone.pgonediary.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import king.rapper.pgone.pgonediary.util.LogUtils;


/**
 * @ Create_time: 2018/5/21 on 15:27.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
public class PgoneWidgetProvider extends AppWidgetProvider {

    /**
     * 在每次操作的结束被调用
     * 接收窗口小部件点击时发送的广播
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        LogUtils.e("aaaaaaa" + intent.toString());
    }

    /**
     * 只要有新的桌面小控件创建时就会调用
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        LogUtils.e("新创建" + appWidgetIds.length);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        // 开启更新小部件的服务
        Intent intent = new Intent(context, PgoneWidgetService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }

    }


    /**
     * 每次删除桌面小控件时调用
     */
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);

    }

    /**
     * 第一次创建小控件时才会调用
     */
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    /**
     * 当所有的桌面小控件都删除后调用
     */
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        // 关闭更新小部件的服务
        Intent intent = new Intent(context, PgoneWidgetService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    /**
     * 响应AppWidgetManager调用。ACTION_APPWIDGET_RESTORED广播
     * 当这个AppWidget提供程序的实例从备份中恢复时。
     */
    @Override
    public void onRestored(Context context, int[] oldWidgetIds,
                           int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }
}
