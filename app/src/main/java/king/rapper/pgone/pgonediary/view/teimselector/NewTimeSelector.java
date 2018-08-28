package king.rapper.pgone.pgonediary.view.teimselector;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import king.rapper.pgone.pgonediary.MyApp;
import king.rapper.pgone.pgonediary.R;
import king.rapper.pgone.pgonediary.util.DateUtil;
import king.rapper.pgone.pgonediary.util.LogUtils;
import king.rapper.pgone.pgonediary.util.ToastUtil;
import king.rapper.pgone.pgonediary.util.constant.Config;

/**
 * @ Create_time: 2018/4/19 on 15:42.
 * @ description： 时间选择器
 * @ author: radish  email: 15703379121@163.com
 */
public class NewTimeSelector {

    private final long ANIMATORDELAY = 200L;
    private ArrayList<String> mDateList, mTimeList;
    private String mDate, mTime;
    private String mStartDate;
    int end_time = 18;
    int start_time = 8;

    private TimeSelectorUtil.OnWheelViewClick click;
    private Context context;
    private Dialog seletorDialog;
    private PickerView pvDate, pvTime;
    private TextView tv_cancle, tv_select, tv_title;

    public NewTimeSelector(Context context, int start_time, int end_time, TimeSelectorUtil.OnWheelViewClick click) {
        this.context = context;
        this.click = click;
        this.start_time = start_time;
        this.end_time = end_time;
        initDialog();
        initView();
    }

    public void show() {
        if (start_time > end_time) {
            ToastUtil.showShort("start>end 请检查你的参数");
            return;
        }

        initTimer();
        addListener();
        seletorDialog.show();
    }

    private void initDialog() {
        if (seletorDialog == null) {
            seletorDialog = new Dialog(context, R.style.time_dialog);
            seletorDialog.setCancelable(true);
            seletorDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            seletorDialog.setContentView(R.layout.new_dialog_selector);
            Window window = seletorDialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams lp = window.getAttributes();
            int width = MyApp.screenWidth;
            lp.width = width;
            window.setAttributes(lp);
        }
    }

    private void initView() {
        pvDate = (PickerView) seletorDialog.findViewById(R.id.year_pv);
        pvTime = (PickerView) seletorDialog.findViewById(R.id.month_pv);
        tv_cancle = (TextView) seletorDialog.findViewById(R.id.tv_cancle);
        tv_select = (TextView) seletorDialog.findViewById(R.id.tv_select);
        tv_title = (TextView) seletorDialog.findViewById(R.id.tv_title);

        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seletorDialog.dismiss();
            }
        });
        tv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click.onClick(mDate + " " + mTime);
                seletorDialog.dismiss();
            }
        });
    }

    private void initTimer() {
        initArrayList();
        loadComponent();
    }

    private void initArrayList() {
        mDateList = new ArrayList<>();
        mTimeList = new ArrayList<>();
//      如果现在是当天 下班前一个小时 ，就从明天开始算时间
        if (DateUtil.getNowHour() >= (end_time - 1)) {
            //      日期列表
            for (int i = 0; i < Config.ConventionLong; i++) {
                Calendar date = Calendar.getInstance();
                date.setTime(new Date());
                SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
                date.set(Calendar.DATE, date.get(Calendar.DATE) + i + 1);
                Date endDate = null;
                String weekOfDate = "";
                try {
                    endDate = dft.parse(dft.format(date.getTime()));
                    weekOfDate = DateUtil.getWeekOfDate(endDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (i == 0) {
                    mStartDate = dft.format(endDate).substring(5);
                }
                mDateList.add(dft.format(endDate).substring(5) + "(" + weekOfDate + ")");
            }
        } else {
//            正常的工作时间
            //      日期列表
            for (int i = 0; i < Config.ConventionLong; i++) {
                Calendar date = Calendar.getInstance();
                date.setTime(new Date());
                SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
                date.set(Calendar.DATE, date.get(Calendar.DATE) + i);
                Date endDate = null;
                String weekOfDate = "";
                try {
                    endDate = dft.parse(dft.format(date.getTime()));
                    weekOfDate = DateUtil.getWeekOfDate(endDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (i == 0) {
                    mStartDate = dft.format(endDate).substring(5);
                }
                mDateList.add(dft.format(endDate).substring(5) + "(" + weekOfDate + ")");
            }
        }

        //        如果现在时间 在上班期间 ，根据当前时间生成时间列表
        if (DateUtil.getNowHour() >= start_time && (DateUtil.getNowHour() < (end_time - 1))) {
            //          时间列表
            for (int i = 1; i < (end_time - DateUtil.getNowHour()); i++) {
                mTimeList.add((DateUtil.getNowHour() + i) + ":00-" + (DateUtil.getNowHour() + i + 1) + ":00");
                LogUtils.e((DateUtil.getNowHour() + i) + ":00-" + (DateUtil.getNowHour() + i + 1) + ":00");
            }
        } else {
//            如果不是上班期间（上班前，下班前一小时后）
            for (int i = 0; i < (end_time - start_time); i++) {
                mTimeList.add((start_time + i) + ":00-" + (start_time + i + 1) + ":00");
                LogUtils.e((start_time + i) + ":00-" + (start_time + i + 1) + ":00");
            }
        }
        if (mDateList != null && mDateList.size() > 0) {
            mDate = mDateList.get(0);
        }
        if (mTimeList != null && mTimeList.size() > 0) {
            mTime = mTimeList.get(0);
        }
    }

    private void addListener() {
        pvDate.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                mDate = text;
                timeChange(text);
            }
        });
        pvTime.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                mTime = text;
            }
        });
    }

    private void loadComponent() {
        pvDate.setData(mDateList);
        pvTime.setData(mTimeList);

        pvDate.setSelected(0);
        pvTime.setSelected(0);

        pvDate.setCanScroll(mDateList.size() > 1);
        pvTime.setCanScroll(mTimeList.size() > 1);
    }

    private void timeChange(String nowDate) {
        mTimeList.clear();
        LogUtils.e("timeChange: " + nowDate);
        LogUtils.e("mStartDate: " + mStartDate);
        LogUtils.e("DateUtil.getNowHour(): " + DateUtil.getNowHour());
        if (nowDate.contains(DateUtil.getToday())) {
            if (DateUtil.getNowHour() >= start_time && (DateUtil.getNowHour() < (end_time - 1))) {
                //          时间列表
                for (int i = 1; i < (end_time - DateUtil.getNowHour()); i++) {
                    mTimeList.add((DateUtil.getNowHour() + i) + ":00-" + (DateUtil.getNowHour() + i + 1) + ":00");
                    LogUtils.e((DateUtil.getNowHour() + i) + ":00-" + (DateUtil.getNowHour() + i + 1) + ":00");
                }
            } else {
                for (int i = 0; i < (end_time - start_time); i++) {
                    mTimeList.add((start_time + i) + ":00-" + (start_time + i + 1) + ":00");
                    LogUtils.e((start_time + i) + ":00-" + (start_time + i + 1) + ":00");
                }
            }

        } else {
            for (int i = 0; i < (end_time - start_time); i++) {
                mTimeList.add((start_time + i) + ":00-" + (start_time + i + 1) + ":00");
                LogUtils.e((start_time + i) + ":00-" + (start_time + i + 1) + ":00");
            }
        }
        pvTime.setData(mTimeList);
        try {
            pvTime.setSelected(0);
            mTime = mTimeList.get(0);
            pvTime.setCanScroll(mTimeList.size() > 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        excuteAnimator(ANIMATORDELAY, pvTime);
    }

    private void excuteAnimator(long ANIMATORDELAY, View view) {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f,
                0f, 1f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f,
                1.3f, 1f);
        PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f,
                1.3f, 1f);
        ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY, pvhZ).setDuration(ANIMATORDELAY).start();
    }

    public void setTitle(String str) {
        tv_title.setText(str);
    }

    public void setIsLoop(boolean isLoop) {
        this.pvDate.setIsLoop(isLoop);
        this.pvTime.setIsLoop(isLoop);
    }
} //364