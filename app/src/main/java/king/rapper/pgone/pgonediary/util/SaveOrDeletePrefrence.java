package king.rapper.pgone.pgonediary.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.Map;

import king.rapper.pgone.pgonediary.util.constant.SPkey;

/**
 * SP 工具类
 */

public class SaveOrDeletePrefrence {

    //  保存值
    public static void save(Context context, String key, String value) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(SPkey.SpName,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    //删除
    public static void delete(Context context, String key) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(SPkey.SpName,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.commit();
        LogUtils.e("已经删除  sp  ===   " + key);
    }

    //查看
    public static String look(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SPkey.SpName,
                Activity.MODE_PRIVATE);
        String data = sharedPreferences.getString(key, "");

        return data;
    }

    //删除全部
    public static void deleteAll(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SPkey.SpName, Activity.MODE_PRIVATE);
        preferences.edit().clear().commit();
    }

    /**
     * 判断本地是否存储该值。
     * 有值 ====   true
     * 无值  ====  false
     *
     * @param value
     * @return
     */
    public static boolean isHave(Context context, String value) {
        if (!TextUtils.isEmpty(value)) {
            String look = SaveOrDeletePrefrence.look(context, value);
            if (!TextUtils.isEmpty(look)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 判断本地是否存储该值。
     */
    public static void logoutDeleteAll(Context context) {
        SharedPreferences spf = context.getSharedPreferences(SPkey.SpName, Activity.MODE_PRIVATE);
        Map<String, ?> key_Value = (Map<String, ?>) spf.getAll(); //获取所有保存在对应标识下的数据，并以Map形式返回
        /* 只需遍历即可得到存储的key和value值*/

        for (Map.Entry<String, ?> entry : key_Value.entrySet()) {
            LogUtils.e("获取的key：" + entry.getKey() + "      ======      获取的value:" + spf.getString(entry.getKey(), ""));
            if ((!entry.getKey().equals("latitude")) && (!entry.getKey().equals("longitude"))
                    && (!entry.getKey().equals("address")) && (!entry.getKey().equals("wsid")) && (!entry.getKey().equals("city"))) {
                delete(context, entry.getKey());
            }

        }

        LogUtils.e("退出登录后剩余的key:" + "\n latitude" + SaveOrDeletePrefrence.look(context, "latitude") +
                "\nlongitude" + SaveOrDeletePrefrence.look(context, "longitude") +
                "\nwsid" + SaveOrDeletePrefrence.look(context, "wsid")
                + "\naddress" + SaveOrDeletePrefrence.look(context, "address"));

    }

}
