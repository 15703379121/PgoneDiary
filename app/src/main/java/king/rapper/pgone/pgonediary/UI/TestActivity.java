package king.rapper.pgone.pgonediary.UI;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import king.rapper.pgone.pgonediary.R;
import king.rapper.pgone.pgonediary.util.LogUtils;
import king.rapper.pgone.pgonediary.util.SampleView;
import king.rapper.pgone.pgonediary.util.ScrimUtil;
import king.rapper.pgone.pgonediary.base.BaseActivity;

/**
 * @ Create_time: 2018/8/2 on 14:09.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
public class TestActivity extends BaseActivity {
    @BindView(R.id.tv_test)
    SampleView tvTest;
    @BindView(R.id.tv_test1)
    TextView tvTest1;
    @BindView(R.id.btn_CustomTime)
    Button btnCustomTime;
    private TimePickerView pvCustomTime;

    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    protected void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Drawable drawable = ScrimUtil.makeCubicGradientScrimDrawable(
                    getResources().getColor(R.color.AppPrimaryDark), //颜色
                    8, //渐变层数
                    Gravity.LEFT);
            LogUtils.e("drawable:" + drawable);
            if (drawable != null)
                tvTest1.setBackground(drawable); //起始方向
        }


        initCustomTimePicker();
    }

    private void initCustomTimePicker() {

        /**
         * @description
         *
         * 注意事项：
         * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
         * 具体可参考demo 里面的两个自定义layout布局。
         * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
         */
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2000, 1, 1, 0, 0, 0);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2100, 12, 31, 23, 59, 59);
        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                btnCustomTime.setText(getTime(date));
            }
        })
////                .setType(TimePickerView.Type.ALL)//default is all
//                .setCancelText("Cancel")
//                .setSubmitText("Sure")
//                .setContentTextSize(18)
//                .setTitleSize(20)
//                .setTitleText("Title")
//                .setTitleColor(Color.BLACK)
//               .setDividerColor(Color.WHITE)//设置分割线的颜色
//                .setTextColorCenter(Color.LTGRAY)//设置选中项的颜色
//                .setTitleBgColor(Color.DKGRAY)//标题背景颜色 Night mode
//                .setBgColor(Color.BLACK)//滚轮背景颜色 Night mode
//                .setSubmitColor(Color.WHITE)
//                .setCancelColor(Color.WHITE)
////                .animGravity(Gravity.RIGHT)// default is center
//                .setDate(selectedDate)
//                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.returnData();
                                pvCustomTime.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.dismiss();
                            }
                        });
                    }
                })
                .setContentTextSize(18)
                .setType(new boolean[]{true, true, true, true, true, true})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setLineSpacingMultiplier(1.6f)//设置两横线之间的间隔倍数
                .setTextXOffset(30, 20, 10, -10, -20, -30)
                .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(0xFF24AD9D)
                .build();

    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    @OnClick(R.id.btn_CustomTime)
    public void onViewClicked() {
        if (pvCustomTime != null)
            pvCustomTime.show(); //弹出自定义时间选择器
    }
}
