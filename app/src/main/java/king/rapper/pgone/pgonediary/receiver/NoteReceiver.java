package king.rapper.pgone.pgonediary.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import king.rapper.pgone.pgonediary.util.LogUtils;
import king.rapper.pgone.pgonediary.widget.PgoneWidgetService;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * @ Create_time: 2018/8/8 on 16:04.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
public class NoteReceiver extends BroadcastReceiver {

    private PgoneWidgetService pgoneWidgetService;

    @Override
    public void onReceive(final Context context, Intent intent) {
        /**
         * 1.判断是否开启服务并开启
         * 2.绑定服务
         * 3.更新数据
         */
        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                // 建立连接
                // 获取服务的操作对象
                PgoneWidgetService.MyBinder binder = (PgoneWidgetService.MyBinder) service;
                // 获取到的Service即MyService
                pgoneWidgetService = binder.getService();
                if (pgoneWidgetService != null) {
                    pgoneWidgetService.updateNote();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                // 连接断开
            }
        };
        Intent intentService = new Intent(context, PgoneWidgetService.class);
        context.bindService(intentService, serviceConnection, Context.BIND_AUTO_CREATE);
//        if (isServiceRunning(context,"king.rapper.pgone.pgonediary.widget.PgoneWidgetService")){
//        }

    }

    private boolean isServiceRunning(Context context, String ServicePackageName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            LogUtils.e("服务名称：" + service.service.getClassName());
            if (ServicePackageName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }



}
