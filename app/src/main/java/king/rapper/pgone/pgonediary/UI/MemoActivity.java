package king.rapper.pgone.pgonediary.UI;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import king.rapper.pgone.pgonediary.R;
import king.rapper.pgone.pgonediary.entity.MemoInfo;
import king.rapper.pgone.pgonediary.interfaces.MemoCallback;
import king.rapper.pgone.pgonediary.util.CalendarReminderUtils;
import king.rapper.pgone.pgonediary.util.CommonUtil;
import king.rapper.pgone.pgonediary.util.DateUtil;
import king.rapper.pgone.pgonediary.util.LogUtils;
import king.rapper.pgone.pgonediary.util.SoftKeyboardUtil;
import king.rapper.pgone.pgonediary.util.ToastUtil;
import king.rapper.pgone.pgonediary.base.BaseActivity;
import king.rapper.pgone.pgonediary.util.constant.Config;

/**
 * @ Create_time: 2018/8/3 on 17:34.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
public class MemoActivity extends BaseActivity {

    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_location)
    EditText etLocation;
    @BindView(R.id.tv_start_time1)
    TextView tvStartTime1;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time1)
    TextView tvEndTime1;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.tv_alarm)
    TextView tvAlarm;
    @BindView(R.id.tv_alarm_open)
    TextView tvAlarmOpen;
    @BindView(R.id.tv_alarm_time)
    TextView tvAlarmTime;
    @BindView(R.id.et_alarm_open_time)
    EditText etAlarmOpenTime;
    @BindView(R.id.tv_alarm_minutes)
    TextView tvAlarmMinutes;
    @BindView(R.id.ll_alarm_time)
    LinearLayout llAlarmTime;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String message = (String) msg.obj;
                    if (!TextUtils.isEmpty(message))
                        ToastUtil.showShort(message);
                    break;
                case 1:
                    ToastUtil.showShort("保存成功");
                    finish();
                    break;
                default:
                    break;
            }
        }
    };
    @BindView(R.id.et_desc)
    EditText etDesc;

    private MemoInfo memoInfo;

    @Override
    public int getLayoutId() {
        return R.layout.activity_memo;
    }

    @Override
    public void getPreIntent() {
        try {
            String memoStr = getIntent().getStringExtra("memo");
            LogUtils.e("memo:" + memoStr);
            if (!TextUtils.isEmpty(memoStr)) {
                memoInfo = new Gson().fromJson(memoStr, MemoInfo.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void timeStrShow(Long time, TextView textView) {
        if (time != null) {
            String timeStr = DateUtil.getTimeString(time);
            if (!TextUtils.isEmpty(timeStr)) {
                textView.setText(timeStr);
            } else {
                String timeString = DateUtil.getTimeString();
                if (!TextUtils.isEmpty(timeString)) {
                    textView.setText(timeString);
                }
            }
        } else {
            String timeString = DateUtil.getTimeString();
            if (!TextUtils.isEmpty(timeString)) {
                textView.setText(timeString);
            }
        }
    }

    @Override
    protected void initView() {
        try {
            initTitleBar("皮皮日程");
        } catch (Exception e) {
            e.printStackTrace();
        }

        initStartTimePicker();
        initEndTimePicker();

    }

    @Override
    protected void initData() {
        initMemoInfo();
    }

    private void initMemoInfo() {
        if (memoInfo != null) { //标题
            if (!TextUtils.isEmpty(memoInfo.getTitle())) {
                etTitle.setText(memoInfo.getTitle());
            }

            //地点
            if (!TextUtils.isEmpty(memoInfo.getLocation())) {
                etLocation.setText(memoInfo.getLocation());
            }

            //详细信息
            if (!TextUtils.isEmpty(memoInfo.getDescription())) {
                etDesc.setText(memoInfo.getDescription());
            }

            //时间
            Long startTime = memoInfo.getStartTime();
            timeStrShow(startTime, tvStartTime);
            Long endTime = memoInfo.getEndTime();
            timeStrShow(endTime, tvEndTime);
            Calendar selectedDate = Calendar.getInstance();//系统当前时间
            selectedDate.setTimeInMillis(startTime);
            if (pvStartTime != null)
                pvStartTime.setDate(selectedDate);
            selectedDate.setTimeInMillis(endTime);
            if (pvEndTime != null)
                pvEndTime.setDate(selectedDate);

            if (memoInfo.getHas_alarm() != null && memoInfo.getHas_alarm() == 1) {
                tvAlarmOpen.setSelected(true);
                llAlarmTime.setEnabled(true);
                etAlarmOpenTime.setEnabled(true);
                tvAlarmOpen.setText("已开启");
                tvAlarmMinutes.setTextColor(getResources().getColor(R.color.AppPrimary));
                tvAlarmTime.setTextColor(getResources().getColor(R.color.AppPrimary));
                if (memoInfo.getMinutes() != null) {
                    String minuteStr = memoInfo.getMinutes().toString();
                    etAlarmOpenTime.setText(minuteStr);
                    etAlarmOpenTime.setSelection(minuteStr.length());
                }
            } else {
                tvAlarmOpen.setText("未开启");
                tvAlarmOpen.setSelected(false);
                llAlarmTime.setEnabled(false);
                etAlarmOpenTime.setEnabled(false);
                tvAlarmMinutes.setTextColor(getResources().getColor(R.color.gray9));
                tvAlarmTime.setTextColor(getResources().getColor(R.color.gray9));
            }
        } else {
            memoInfo = new MemoInfo();
            //默认时间
            Long currentTimeMillis = System.currentTimeMillis();
            Long startTime = currentTimeMillis + (60 * 60 * 1000);
            Long endTime = currentTimeMillis + (2 * 60 * 60 * 1000);
            String startTimeStr = DateUtil.getTimeString(startTime);
            String endTimeStr = DateUtil.getTimeString(endTime);
            LogUtils.e("startTimeStr:" + startTimeStr + "    tvStartTime:" + tvStartTime);
            if (!TextUtils.isEmpty(startTimeStr))
                tvStartTime.setText(startTimeStr);
            if (!TextUtils.isEmpty(endTimeStr))
                tvEndTime.setText(endTimeStr);
            memoInfo.setStartTime(startTime);
            memoInfo.setEndTime(endTime);
            //默认闹钟
            tvAlarmOpen.setText("已开启");
            llAlarmTime.setEnabled(true);
            etAlarmOpenTime.setEnabled(true);
            tvAlarmOpen.setSelected(true);
            tvAlarmMinutes.setTextColor(getResources().getColor(R.color.AppPrimary));
            tvAlarmTime.setTextColor(getResources().getColor(R.color.AppPrimary));
            memoInfo.setHas_alarm(1);
            //默认闹钟提醒时间
            etAlarmOpenTime.setText("5");
            etAlarmOpenTime.setSelection("5".length());
            memoInfo.setMinutes(5l);
        }
    }

    @OnClick({R.id.ll_start_time, R.id.ll_end_time, R.id.ll_alarm_open, R.id.ll_alarm_time, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_start_time:
                SoftKeyboardUtil.closeInputMethod(this);
                if (pvStartTime != null)
                    pvStartTime.show(); //弹出自定义时间选择器
                break;
            case R.id.ll_end_time:
                SoftKeyboardUtil.closeInputMethod(this);
                if (pvEndTime != null)
                    pvEndTime.show(); //弹出自定义时间选择器
                break;
            case R.id.ll_alarm_open:
                if (memoInfo.getHas_alarm() != null && memoInfo.getHas_alarm() == 1) {
                    memoInfo.setHas_alarm(0);
                    tvAlarmOpen.setText("未开启");
                    tvAlarmOpen.setSelected(false);
                    llAlarmTime.setEnabled(false);
                    etAlarmOpenTime.setEnabled(false);
                    tvAlarmMinutes.setTextColor(getResources().getColor(R.color.gray9));
                    tvAlarmTime.setTextColor(getResources().getColor(R.color.gray9));
                } else {
                    memoInfo.setHas_alarm(1);
                    tvAlarmOpen.setSelected(true);
                    llAlarmTime.setEnabled(true);
                    etAlarmOpenTime.setEnabled(true);
                    tvAlarmOpen.setText("已开启");
                    tvAlarmMinutes.setTextColor(getResources().getColor(R.color.AppPrimary));
                    tvAlarmTime.setTextColor(getResources().getColor(R.color.AppPrimary));
                    if (memoInfo.getMinutes() != null) {
                        String minuteStr = memoInfo.getMinutes().toString();
                        etAlarmOpenTime.setText(minuteStr);
                        etAlarmOpenTime.setSelection(minuteStr.length());
                    }
                }
                break;
            case R.id.ll_alarm_time:
                break;
            case R.id.tv_submit:
                if (memoInfo == null)
                    memoInfo = new MemoInfo();
                if (CommonUtil.checkEmpty(etTitle)) {
                    ToastUtil.showShort("标题不可为空");
                    return;
                } else {
                    memoInfo.setTitle(etTitle.getText().toString().trim());
                }
                String startTimeStr = tvStartTime.getText().toString().trim();
                Long startTime = 0l;
                if (!TextUtils.isEmpty(startTimeStr)) {
                    startTime = DateUtil.date2TimeStamp(startTimeStr);
                    if (startTime != null) {
                        memoInfo.setStartTime(startTime);
                    } else {
                        ToastUtil.showShort("请输入开始时间");
                        return;
                    }
                }
                String endTimeStr = tvEndTime.getText().toString().trim();
                if (!TextUtils.isEmpty(endTimeStr)) {
                    Long endTime = DateUtil.date2TimeStamp(endTimeStr);
                    if (endTime != null) {
                        if (startTime != null && startTime != 0 && endTime < startTime) {
                            ToastUtil.showShort("结束时间不可早于开始时间");
                            return;
                        } else {
                            memoInfo.setEndTime(endTime);
                        }
                    } else {
                        ToastUtil.showShort("请输入结束时间");
                        return;
                    }
                }
                if (!CommonUtil.checkEmpty(etLocation)) {
                    memoInfo.setLocation(etLocation.getText().toString().trim());
                }
                if (!CommonUtil.checkEmpty(etDesc)) {
                    memoInfo.setDescription(etDesc.getText().toString().trim());
                }

                if (!CommonUtil.checkEmpty(etAlarmOpenTime)) {
                    String minuteStr = etAlarmOpenTime.getText().toString().trim();
                    try {
                        Long minute = Long.parseLong(minuteStr);
                        memoInfo.setMinutes(minute);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (memoInfo != null) {
                    LogUtils.e("memoInfo:" + new Gson().toJson(memoInfo));
                    if (memoInfo.getMemoInfoId() == null) {
                        //添加
                        CalendarReminderUtils.addCalendarEvent(this, memoInfo, new MemoCallback() {
                            @Override
                            public void memoCallback(List<MemoInfo> memoInfoList, String message) {
                                Message msg = Message.obtain();
                                if (Config.Message_Success.equals(message)) {
                                    msg.what = 1;
                                } else {
                                    msg.what = 0;
                                }
                                msg.obj = message;
                                mHandler.sendMessage(msg);
                            }
                        });
                    } else {
                        //更新
                        CalendarReminderUtils.updateCalendarEvent(this, memoInfo, new MemoCallback() {
                            @Override
                            public void memoCallback(List<MemoInfo> memoInfoList, String message) {
                                Message msg = Message.obtain();
                                if (Config.Message_Success.equals(message)) {
                                    msg.what = 1;
                                } else {
                                    msg.what = 0;
                                }
                                msg.obj = message;
                                mHandler.sendMessage(msg);
                            }
                        });
                    }
                }
                break;
        }
    }

    private TimePickerView pvStartTime;
    private TimePickerView pvEndTime;

    private void initStartTimePicker() {

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
        if (memoInfo != null && memoInfo.getStartTime() != null) {
            selectedDate.setTimeInMillis(memoInfo.getStartTime());
        }
        Calendar startDate = Calendar.getInstance();
        startDate.set(2000, 1, 1, 0, 0, 0);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2100, 12, 31, 23, 59, 59);
        //时间选择器 ，自定义布局
        pvStartTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                tvStartTime.setText(getTime(date));
            }
        })
                .setDate(selectedDate)
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
                                pvStartTime.returnData();
                                pvStartTime.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvStartTime.dismiss();
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

    private void initEndTimePicker() {

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
        if (memoInfo != null && memoInfo.getEndTime() != null) {
            selectedDate.setTimeInMillis(memoInfo.getEndTime());
        }
        Calendar startDate = Calendar.getInstance();
        startDate.set(2000, 1, 1, 0, 0, 0);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2100, 12, 31, 23, 59, 59);
        //时间选择器 ，自定义布局
        pvEndTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                tvEndTime.setText(getTime(date));
            }
        })
                .setDate(selectedDate)
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
                                pvEndTime.returnData();
                                pvEndTime.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvEndTime.dismiss();
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
}
