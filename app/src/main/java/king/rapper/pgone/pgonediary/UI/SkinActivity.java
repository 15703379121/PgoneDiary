package king.rapper.pgone.pgonediary.UI;

import android.graphics.Color;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.skydoves.multicolorpicker.ColorEnvelope;
import com.skydoves.multicolorpicker.MultiColorPickerView;
import com.skydoves.multicolorpicker.listeners.ColorListener;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import king.rapper.pgone.pgonediary.MyApp;
import king.rapper.pgone.pgonediary.R;
import king.rapper.pgone.pgonediary.interfaces.PhotoCallBack;
import king.rapper.pgone.pgonediary.util.LogUtils;
import king.rapper.pgone.pgonediary.util.SaveOrDeletePrefrence;
import king.rapper.pgone.pgonediary.util.ToastUtil;
import king.rapper.pgone.pgonediary.util.UIUtil;
import king.rapper.pgone.pgonediary.base.BaseActivity;
import king.rapper.pgone.pgonediary.base.TitleListener;
import king.rapper.pgone.pgonediary.util.constant.SPkey;

/**
 * @ Create_time: 2018/7/5 on 11:29.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
public class SkinActivity extends BaseActivity {
    @BindView(R.id.layout_title_bar_right_text)
    TextView layoutTitleBarRightText;
    @BindView(R.id.seekbar)
    SeekBar seekbar;
    @BindView(R.id.iv_bg_test)
    ImageView ivBgTest;
    @BindView(R.id.fl_bg_test)
    FrameLayout flBgTest;
    @BindView(R.id.multiColorPickerView)
    MultiColorPickerView multiColorPickerView;
    @BindView(R.id.color_view)
    View colorView;
    @BindView(R.id.ll_bg)
    LinearLayout llBg;
    @BindView(R.id.tv_sale)
    TextView tvSale;
    @BindView(R.id.tv_bg_change)
    TextView tvBgChange;
    @BindView(R.id.tv_theme_state)
    TextView tvThemeState;
    @BindView(R.id.rb_theme_white)
    RadioButton rbThemeWhite;
    @BindView(R.id.rb_theme_black)
    RadioButton rbThemeBlack;
    @BindView(R.id.rg_theme_state)
    RadioGroup rgThemeState;
    @BindView(R.id.iv_bg)
    ImageView ivBg;
    private String testPicture;
    private String testColor;
    private String testState;
    private String testSale;

    @Override
    public int getLayoutId() {
        return R.layout.activity_skin;
    }

    @Override
    protected void initView() {
        try {
            initTitle();
            initColorPicker();
        } catch (Exception e) {
            e.printStackTrace();
        }

        rgThemeState.check(R.id.rb_theme_white);
    }

    @Override
    protected void initViewTheme() /*throws Exception */ {
        //默认信息
        testSale = UIUtil.getThemeSale();
        testState = UIUtil.getThemeState();

        //设置背景透明度
        UIUtil.setThemeBackgroundSale(llBg);
        UIUtil.setThemeBackgroundSale(flBgTest);

        //背景
        UIUtil.setThemeBackgroundPicture(ivBgTest);
        UIUtil.setThemeBackgroundPicture(ivBg);

        //字体
        UIUtil.setThemeFontColor(tvSale);
        UIUtil.setThemeFontColor(tvThemeState);
        UIUtil.setThemeFontColor(tvBgChange);

        //单选框
        UIUtil.setThemeBackgroundColor(rgThemeState);
        UIUtil.setThemeFontColor(rbThemeWhite);
        UIUtil.setThemeFontColor(rbThemeBlack);

        //进度条
        LogUtils.e("testSale:" + testState);
        if (!TextUtils.isEmpty(testSale))
            seekbar.setProgress(Integer.parseInt(testSale, 16));
    }


    @Override
    protected void initListener() {
        //进度条变化监听器
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                try {
                    //十进制转十六进制
                    testSale = Integer.toHexString(progress);
                    LogUtils.e("十进制:" + progress + "   十六进制：" + testSale);
                    if (testSale.length() == 1) {
                        testSale = "0" + testSale;
                    }
                    flBgTest.setBackgroundColor(Color.parseColor("#" + testSale + testState));
                } catch (Exception e) {
                    LogUtils.e("进度条异常：" + e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        //选择按钮选择监听器
        rgThemeState.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_theme_white:
                        //选择白色
                        testState = "ffffff";
                        break;
                    case R.id.rb_theme_black:
                        //选择黑色
                        testState = "000000";
                        break;
                }
                LogUtils.e("主题选择：" + "#" + testSale + testState);
                flBgTest.setBackgroundColor(Color.parseColor("#" + testSale + testState));
            }
        });
    }

    @OnClick(R.id.tv_bg_change)
    public void onViewClicked() {

        showPhotoDialog(SkinActivity.this, 1, new PhotoCallBack() {
            @Override
            public void success(List<Uri> list) {
                if (list != null && list.size() > 0) {
                    Uri uri = list.get(0);
                    if (!TextUtils.isEmpty(uri.toString())) {
                        testPicture = uri.toString();
                        UIUtil.setEndWithGIF(ivBgTest, testPicture);
                    } else {
                        ToastUtil.showShort("无法识别图片");
                    }
                }
            }
        });
    }

    private void initTitle() throws Exception {
        initTitleBar("个性换肤", "保存", new TitleListener() {
            @Override
            public void RightClick() {
                try {
                    //保存
                    LogUtils.e("testPicture:" + testPicture + "    testColor:" + testColor + "    testSale:" + testSale + "testState:" + testState);
                    if (!TextUtils.isEmpty(testPicture))
                        SaveOrDeletePrefrence.save(MyApp.getInstance(), SPkey.THEME_PICTURE, testPicture);
                    if (!TextUtils.isEmpty(testColor))
                        SaveOrDeletePrefrence.save(MyApp.getInstance(), SPkey.THEME_COLOR, testColor);
                    if (!TextUtils.isEmpty(testSale))
                        SaveOrDeletePrefrence.save(MyApp.getInstance(), SPkey.THEME_SALE, testSale);
                    if (!TextUtils.isEmpty(testState))
                        SaveOrDeletePrefrence.save(MyApp.getInstance(), SPkey.THEME_STATE, testState);
                    ToastUtil.showShort("保存成功");
                    initViewTheme();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initColorPicker() throws Exception {
        multiColorPickerView.addSelector(ContextCompat.getDrawable(this, R.drawable.wheel), new ColorListener() {
            @Override
            public void onColorSelected(ColorEnvelope colorEnvelope) {
                LogUtils.e("color:" + "#" + colorEnvelope.getHtmlCode());
                colorView.setBackgroundColor(Color.parseColor("#" + colorEnvelope.getHtmlCode()));
                testColor = "#" + colorEnvelope.getHtmlCode();
            }
        });
        multiColorPickerView.setFlagMode(MultiColorPickerView.FlagMode.LAST);
        multiColorPickerView.setFlagFlipable(false);
    }
}
