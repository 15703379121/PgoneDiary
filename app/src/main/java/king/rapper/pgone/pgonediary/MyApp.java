package king.rapper.pgone.pgonediary;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;

import cn.jpush.android.api.JPushInterface;
import king.rapper.pgone.pgonediary.util.LogUtils;

/**
 * @ Create_time: 2018/7/4 on 9:30.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
public class MyApp extends Application {

    private static MyApp instance;
    private static Context context;
    public static int screenWidth = 0;
    public static int screenHeight = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        MyApp.context = getApplicationContext();
        getScreenSize();


        JPushInterface.setDebugMode(false);//正式版的时候设置false，关闭调试
        JPushInterface.init(this);
        String registrationId = JPushInterface.getRegistrationID(this);
        LogUtils.e("1099" + "run:--------->registrationId： " + registrationId);
    }

    public static MyApp getInstance() {
        return instance;
    }

    public static Context getAppContext() {
        return MyApp.context;
    }

    /**
     * 获取屏幕尺寸
     */
    private void getScreenSize() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenHeight = dm.heightPixels;
        screenWidth = dm.widthPixels;
    }

}
