package king.rapper.pgone.pgonediary.util;

import android.app.Activity;
import android.net.Uri;
import android.text.TextUtils;

import com.google.gson.Gson;

import org.greenrobot.greendao.AbstractDao;

import java.util.ArrayList;
import java.util.List;

import king.rapper.pgone.pgonediary.dao.DaoController;
import king.rapper.pgone.pgonediary.entity.ExpressionIcon;
import king.rapper.pgone.pgonediary.entity.MoodIcon;
import king.rapper.pgone.pgonediary.entity.WeatherIcon;
import king.rapper.pgone.pgonediary.interfaces.ThreadDataCallBack;

/**
 * @ Create_time: 2018/7/20 on 17:44.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
public class ThreadDataUtil {
    public static void addWeatherIcon(final Activity activity, final List<Uri> list, final ThreadDataCallBack callBack) {
        new Thread() {
            @Override
            public void run() {
                if (list != null && list.size() > 0) {
                    Long timeLong = DateUtil.getTimeLong();
                    String type = "";
                    WeatherIcon weatherIcon = null;
                    final List<WeatherIcon> weatherIconList = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        String url = list.get(i).toString();
                        if (!TextUtils.isEmpty(url)) {
                            String[] split = url.split("\\.");
                            if (split != null && split.length > 1)
                                type = "." + split[split.length - 1];
                            weatherIcon = new WeatherIcon(url, type, timeLong);
                            weatherIconList.add(weatherIcon);
                        }
                    }
                    if (weatherIconList != null && weatherIconList.size() > 0) {
                        DaoController.getInstance().getWeatherIconDao().insertInTx(weatherIconList);
                    }
                    final List<WeatherIcon> weatherList = DaoController.getInstance().getWeatherIconDao().queryBuilder().list();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //重新加载数据
                            if (callBack != null) {
                                callBack.dataCallBack(weatherIconList, weatherList);
                            }
                        }
                    });
                }
            }
        }.start();
    }

    public <T> void addIcon(final Class<T> tClass, final Activity activity, final List<Uri> list, final ThreadDataCallBack callBack) {
        new Thread() {
            @Override
            public void run() {
                if (list != null && list.size() > 0) {
                    Long timeLong = DateUtil.getTimeLong();
                    String type = "";
                    final List<T> iconList = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        String url = list.get(i).toString();
                        LogUtils.e("url:" + url);
                        if (!TextUtils.isEmpty(url)) {
                            // TODO: 2018/7/21 icon7
                            String[] split = url.split("\\.");
                            if (split != null && split.length > 1)
                                type = "." + split[split.length - 1];
                            if (tClass == WeatherIcon.class) {
                                WeatherIcon icon = new WeatherIcon(url, type, timeLong);
                                iconList.add((T) icon);
                            } else if (tClass == MoodIcon.class) {
                                MoodIcon icon = new MoodIcon(url, type, timeLong);
                                iconList.add((T) icon);
                            } else if (tClass == ExpressionIcon.class) {
                                ExpressionIcon icon = new ExpressionIcon(url, type, timeLong);
                                iconList.add((T) icon);
                            }
                        }
                    }
                    if (iconList != null && iconList.size() > 0) {
                        DaoController.getInstance().getIconDao(tClass).insertInTx(iconList);
                    }
                    AbstractDao iconDao = DaoController.getInstance().getIconDao(tClass);
                    if (iconDao != null) {
                        final List<T> iconAllList = iconDao.queryBuilder().list();
                        LogUtils.e("iconAllList:" + new Gson().toJson(iconAllList));
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //重新加载数据
                                if (callBack != null) {
                                    callBack.dataCallBack(iconList, iconAllList);
                                }
                            }
                        });
                    } else {
                        LogUtils.e("无此数据库类");
                    }
                }
            }
        }.start();
    }
}
