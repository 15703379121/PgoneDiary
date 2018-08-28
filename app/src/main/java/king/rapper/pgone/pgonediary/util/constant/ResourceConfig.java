package king.rapper.pgone.pgonediary.util.constant;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import king.rapper.pgone.pgonediary.R;
import king.rapper.pgone.pgonediary.entity.ExpressionIcon;
import king.rapper.pgone.pgonediary.entity.MoodIcon;
import king.rapper.pgone.pgonediary.entity.WeatherIcon;

/**
 * @ Create_time: 2018/7/25 on 17:27.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
public class ResourceConfig {
    public static List<WeatherIcon> initWeatherLocalIcon(Context context) {
        List<WeatherIcon> weatherLocalList = new ArrayList<>();
        weatherLocalList.add(new WeatherIcon("android.resource://" + context.getPackageName() + "/" + R.mipmap.weather0));
        weatherLocalList.add(new WeatherIcon("android.resource://" + context.getPackageName() + "/" + R.mipmap.weather1));
        weatherLocalList.add(new WeatherIcon("android.resource://" + context.getPackageName() + "/" + R.mipmap.weather2));
        weatherLocalList.add(new WeatherIcon("android.resource://" + context.getPackageName() + "/" + R.mipmap.weather3));
        weatherLocalList.add(new WeatherIcon("android.resource://" + context.getPackageName() + "/" + R.mipmap.weather4));
        weatherLocalList.add(new WeatherIcon("android.resource://" + context.getPackageName() + "/" + R.mipmap.weather5));
        weatherLocalList.add(new WeatherIcon("android.resource://" + context.getPackageName() + "/" + R.mipmap.weather6));
        weatherLocalList.add(new WeatherIcon("android.resource://" + context.getPackageName() + "/" + R.mipmap.weather7));
        weatherLocalList.add(new WeatherIcon("android.resource://" + context.getPackageName() + "/" + R.mipmap.weather8));
        return weatherLocalList;
    }

    public static List<MoodIcon> initMoodLocalIcon(Context context) {
        List<MoodIcon> moodLocalList = new ArrayList<>();
        moodLocalList.add(new MoodIcon("android.resource://" + context.getPackageName() + "/" + R.mipmap.mood0));
        moodLocalList.add(new MoodIcon("android.resource://" + context.getPackageName() + "/" + R.mipmap.mood1));
        moodLocalList.add(new MoodIcon("android.resource://" + context.getPackageName() + "/" + R.mipmap.mood2));
        moodLocalList.add(new MoodIcon("android.resource://" + context.getPackageName() + "/" + R.mipmap.mood3));
        return moodLocalList;
    }

    public static List<ExpressionIcon> initExpressionLocalIcon(Context context) {
        List<ExpressionIcon> expressionLocalList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            expressionLocalList.add(new ExpressionIcon("file:///android_asset/expression" + i + ".png"));
        }
        return expressionLocalList;
    }
}
