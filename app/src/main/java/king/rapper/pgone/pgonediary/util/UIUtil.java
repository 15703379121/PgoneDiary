package king.rapper.pgone.pgonediary.util;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import king.rapper.pgone.pgonediary.MyApp;
import king.rapper.pgone.pgonediary.R;
import king.rapper.pgone.pgonediary.UI.DiaryActivity;
import king.rapper.pgone.pgonediary.interfaces.MeasureSizeInterface;
import king.rapper.pgone.pgonediary.util.constant.Config;
import king.rapper.pgone.pgonediary.util.constant.SPkey;

/**
 * Created by Administrator on 2018/7/5 0005.
 */

public class UIUtil {

    /**
     * 设置主题背景图
     *
     * @param iv
     */
    public static void setThemeBackgroundPicture(ImageView iv) {
        String themePicture = SaveOrDeletePrefrence.look(MyApp.getInstance(), SPkey.THEME_PICTURE);
        LogUtils.e("setThemeBackgroundPicture:themePicture:" + themePicture);
        UIUtil.setEndWithGIF(iv, themePicture);
    }

    /**
     * 设置主题透明度
     *
     * @param view
     */
    public static void setThemeBackgroundSale(View view) {
        String themeSale = SaveOrDeletePrefrence.look(MyApp.getInstance(), SPkey.THEME_SALE);
        String themeState = SaveOrDeletePrefrence.look(MyApp.getInstance(), SPkey.THEME_STATE);
        LogUtils.e("setThemeBackgroundSale:themeSale:" + themeSale);
        LogUtils.e("setThemeBackgroundSale:themeState:" + themeState);
        if (!TextUtils.isEmpty(themeSale) && !TextUtils.isEmpty(themeState)) {
            view.setBackgroundColor(Color.parseColor("#" + themeSale + themeState));
        }
        if (!TextUtils.isEmpty(themeSale) && TextUtils.isEmpty(themeState)) {
            view.setBackgroundColor(Color.parseColor("#" + themeSale + "ffffff"));
        }
        if (TextUtils.isEmpty(themeSale) && !TextUtils.isEmpty(themeState)) {
            view.setBackgroundColor(Color.parseColor("#66" + themeState));
        }


    }


    /**
     * 设置主题字体颜色
     *
     * @param tv
     */
    public static void setThemeFontColor(TextView tv) {
        String themeSale = SaveOrDeletePrefrence.look(MyApp.getInstance(), SPkey.THEME_COLOR);
        LogUtils.e("setThemeBackgroundSale:themeSale:" + themeSale);
        if (!TextUtils.isEmpty(themeSale)) {
            tv.setTextColor(Color.parseColor(themeSale));
        }
    }


    /**
     * 设置主题字体颜色
     *
     * @param rb
     */
    public static void setThemeFontColor(RadioButton rb) {
        String themeColor = SaveOrDeletePrefrence.look(MyApp.getInstance(), SPkey.THEME_COLOR);
        LogUtils.e("setThemeBackgroundSale:themeColor:" + themeColor);
        if (!TextUtils.isEmpty(themeColor)) {
            rb.setTextColor(Color.parseColor(themeColor));
        }
    }


    /**
     * 设置主题颜色背景
     *
     * @param view
     */
    public static void setThemeBackgroundColor(View view) {
        String themeColor = SaveOrDeletePrefrence.look(MyApp.getInstance(), SPkey.THEME_COLOR);
        if (!TextUtils.isEmpty(themeColor)) {
            view.setBackgroundColor(Color.parseColor(themeColor));
        }
    }


    /**
     * 设置主题色调默认选择
     *
     * @param rg
     */
    public static void setThemeBackgroundColor(RadioGroup rg) {
        String themeState = SaveOrDeletePrefrence.look(MyApp.getInstance(), SPkey.THEME_STATE);
        LogUtils.e("setThemeBackgroundSale:themeState:" + themeState);
        if (!TextUtils.isEmpty(themeState)) {
            if (TextUtils.equals(themeState, "ffffff")) {
                rg.check(R.id.rb_theme_white);
            } else if (TextUtils.equals(themeState, "000000")) {
                rg.check(R.id.rb_theme_black);
            } else {
                //默认
                rg.check(R.id.rb_theme_white);
            }
        } else {
            //默认
            rg.check(R.id.rb_theme_white);
        }
    }

    public static String getThemeBackground() {
        return SaveOrDeletePrefrence.look(MyApp.getInstance(), SPkey.THEME_PICTURE);
    }

    public static String getThemeColor() {
        return SaveOrDeletePrefrence.look(MyApp.getInstance(), SPkey.THEME_COLOR);
    }

    public static String getThemeSale() {
        String sale = SaveOrDeletePrefrence.look(MyApp.getInstance(), SPkey.THEME_SALE);
        if (TextUtils.isEmpty(sale)) {
            sale = "66";
        }
        LogUtils.e("getThemeSale:" + sale);
        return sale;
    }

    public static String getThemeState() {
        String state = SaveOrDeletePrefrence.look(MyApp.getInstance(), SPkey.THEME_STATE);
        if (TextUtils.isEmpty(state)) {
            state = "ffffff";
        }
        LogUtils.e("getThemeState:" + state);
        return state;
    }

    public static void setEndWithGIF(ImageView ivBackground, String url) {
        setEndWithGIF(ivBackground, url, "");
    }

    public static void setEndWithGIF(ImageView ivBackground, String url, String imgType) {
        LogUtils.e("ivBackground:" + ivBackground + "   url:" + url + "    imgType:" + imgType);
        if (!TextUtils.isEmpty(url) && ivBackground != null) {
            if (!TextUtils.isEmpty(imgType)) {
                if (Config.GIF.equals(imgType)) {
                    Glide.with(MyApp.getInstance()).load(url).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ivBackground);
                } else {
                    Glide.with(MyApp.getInstance()).load(url).into(ivBackground);
                }
            } else {
                if (url.endsWith(Config.GIF)) {
                    Glide.with(MyApp.getInstance()).load(url).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ivBackground);
                } else {
                    Glide.with(MyApp.getInstance()).load(url).into(ivBackground);

                }
            }
        }
    }

    public static void measureDefaultSize(final View view, final MeasureSizeInterface sizeInterface) {
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                if (view.getMeasuredWidth() <= 0 || view.getMeasuredHeight() <= 0) {
                    //屏幕宽高
                    if (MyApp.screenWidth <= 0 || MyApp.screenHeight <= 0) {
                        //固定宽高
                        if (sizeInterface != null) {
                            sizeInterface.defaultSize(Config.DefaultWidthSize, Config.DefaultHeightSize);
                        }
                    } else {
                        sizeInterface.defaultSize(MyApp.screenWidth, MyApp.screenHeight);
                    }
                } else {
                    sizeInterface.defaultSize(view.getMeasuredWidth(), view.getMeasuredHeight());
                }
            }
        });
    }
}
