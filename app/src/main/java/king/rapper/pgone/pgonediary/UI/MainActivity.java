package king.rapper.pgone.pgonediary.UI;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ldf.calendar.component.CalendarAttr;
import com.ldf.calendar.component.CalendarViewAdapter;
import com.ldf.calendar.interf.OnSelectDateListener;
import com.ldf.calendar.model.CalendarDate;
import com.ldf.calendar.view.Calendar;
import com.ldf.calendar.view.MonthPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import king.rapper.pgone.pgonediary.R;
import king.rapper.pgone.pgonediary.adapter.DiaryRvAdapter;
import king.rapper.pgone.pgonediary.adapter.MemoRvAdapter;
import king.rapper.pgone.pgonediary.adapter.NoteRvAdapter;
import king.rapper.pgone.pgonediary.dao.DaoController;
import king.rapper.pgone.pgonediary.entity.DiaryInfo;
import king.rapper.pgone.pgonediary.entity.MemoInfo;
import king.rapper.pgone.pgonediary.entity.NoteInfo;
import king.rapper.pgone.pgonediary.interfaces.MemoCallback;
import king.rapper.pgone.pgonediary.interfaces.OnDiaryListClickListener;
import king.rapper.pgone.pgonediary.util.CalendarReminderUtils;
import king.rapper.pgone.pgonediary.util.DateUtil;
import king.rapper.pgone.pgonediary.util.LogUtils;
import king.rapper.pgone.pgonediary.util.ToastUtil;
import king.rapper.pgone.pgonediary.util.UIUtil;
import king.rapper.pgone.pgonediary.base.ActivityCollector;
import king.rapper.pgone.pgonediary.base.BaseActivity;
import king.rapper.pgone.pgonediary.util.constant.Config;
import king.rapper.pgone.pgonediary.view.date.CustomDayView;
import king.rapper.pgone.pgonediary.view.date.ThemeDayView;
import king.rapper.pgone.pgonediary.view.sideslip.CoordinatorMenu;
import king.rapper.pgone.pgonediary.widget.PgoneWidgetService;

public class MainActivity extends BaseActivity {
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.show_month_view)
    TextView showMonthView;
    @BindView(R.id.show_year_view)
    TextView showYearView;
    @BindView(R.id.choose_date_view)
    LinearLayout chooseDateView;
    @BindView(R.id.back_today_button)
    TextView backTodayButton;
    @BindView(R.id.theme_switch)
    TextView themeSwitch;
    @BindView(R.id.next_month)
    TextView nextMonth;
    @BindView(R.id.last_month)
    TextView lastMonth;
    @BindView(R.id.calendar_view)
    MonthPager calendarView;
    @BindView(R.id.fl_main)
    FrameLayout flMain;
    @BindView(R.id.view_title)
    RelativeLayout viewTitle;
    @BindView(R.id.menu)
    CoordinatorMenu menu;
    @BindView(R.id.activity_main)
    RelativeLayout activityMain;
    @BindView(R.id.iv_bg_menu)
    ImageView ivBgMenu;
    @BindView(R.id.iv_bg_main)
    ImageView ivBgMain;
    @BindView(R.id.tv_tv1)
    TextView tvTv1;
    @BindView(R.id.tv_tv2)
    TextView tvTv2;
    @BindView(R.id.tv_tv3)
    TextView tvTv3;
    @BindView(R.id.tv_tv4)
    TextView tvTv4;
    @BindView(R.id.tv_tv5)
    TextView tvTv5;
    @BindView(R.id.tv_tv6)
    TextView tvTv6;
    @BindView(R.id.tv_tv7)
    TextView tvTv7;
    @BindView(R.id.tv_tv8)
    TextView tvTv8;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.fl_main2)
    LinearLayout flMain2;
    @BindView(R.id.rl_main2)
    RelativeLayout rlMain2;
    @BindView(R.id.iv_iv1)
    ImageView ivIv1;
    @BindView(R.id.iv_iv2)
    ImageView ivIv2;
    @BindView(R.id.iv_iv3)
    ImageView ivIv3;
    @BindView(R.id.iv_iv4)
    ImageView ivIv4;
    @BindView(R.id.iv_iv5)
    ImageView ivIv5;
    @BindView(R.id.rv_memo)
    RecyclerView rvMemo;
    @BindView(R.id.ll_memo)
    LinearLayout llMemo;
    @BindView(R.id.rv_note)
    RecyclerView rvNote;
    @BindView(R.id.ll_note)
    LinearLayout llNote;
    @BindView(R.id.rv_diary)
    RecyclerView rvDiary;
    @BindView(R.id.ll_diary)
    LinearLayout llDiary;
    @BindView(R.id.cardView)
    CardView cardView;

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
                    //月——日记
                    monthDiaryList = (List<DiaryInfo>) msg.obj;
                    monthLoaderTag++;
                    if (monthLoaderTag == 3) {
                        LogUtils.e("三个月数据都加载完了");
                        time2Mark();
                    }
                    break;
                case 2:
                    //月——便签
                    monthNoteList = (List<NoteInfo>) msg.obj;
                    monthLoaderTag++;
                    if (monthLoaderTag == 3) {
                        LogUtils.e("三个月数据都加载完了");
                        time2Mark();
                    }
                    break;
                case 3:
                    //月——日程
                    monthMemoList = (List<MemoInfo>) msg.obj;
                    monthLoaderTag++;
                    if (monthLoaderTag == 3) {
                        LogUtils.e("三个月数据都加载完了");
                        time2Mark();
                    }
                    break;
                case 4:
                    //日——日记
                    dayDiaryList = (List<DiaryInfo>) msg.obj;
                    if (dayDiaryList == null || dayDiaryList.size() <= 0) {
                        llDiary.setVisibility(View.GONE);
                    } else {
                        llDiary.setVisibility(View.VISIBLE);
                        if (diaryRvAdapter == null) {
                            diaryRvAdapter = new DiaryRvAdapter(MainActivity.this, dayDiaryList);
                            dayDiaryListener();
                            if (rvDiary != null)
                                rvDiary.setAdapter(diaryRvAdapter);
                        } else {
                            diaryRvAdapter.setList(dayDiaryList);
                        }
                    }
                    break;
                case 5:
                    //日——便签
                    dayNoteList = (List<NoteInfo>) msg.obj;
                    if (dayNoteList == null || dayNoteList.size() <= 0) {
                        llNote.setVisibility(View.GONE);
                    } else {
                        llNote.setVisibility(View.VISIBLE);
                        if (noteRvAdapter == null) {
                            noteRvAdapter = new NoteRvAdapter(MainActivity.this, dayNoteList);
                            dayNoteListener();
                            if (rvNote != null)
                                rvNote.setAdapter(noteRvAdapter);
                        } else {
                            noteRvAdapter.setList(dayNoteList);
                        }
                    }
                    break;
                case 6:
                    //日——日程
                    dayMemoList = (List<MemoInfo>) msg.obj;
                    if (dayMemoList == null || dayMemoList.size() <= 0) {
                        llMemo.setVisibility(View.GONE);
                    } else {
                        llMemo.setVisibility(View.VISIBLE);
                        if (memoRvAdapter == null) {
                            memoRvAdapter = new MemoRvAdapter(MainActivity.this, dayMemoList);
                            dayMemoListener();
                            if (rvMemo != null)
                                rvMemo.setAdapter(memoRvAdapter);
                        } else {
                            memoRvAdapter.setList(dayMemoList);
                        }
                    }
                    break;
                case 7:
                    //设置标签完毕
                    initCalendarView();
                    break;
                case 8:
                    ToastUtil.showShort("删除成功");
                    sendNoteBroadcast();
                    break;
                default:
                    break;
            }
        }
    };

    private HashMap markData;
    private NoteRvAdapter noteRvAdapter;
    private int navigationHeight;
    private CalendarDate currentDate;
    private OnSelectDateListener onSelectDateListener;
    private CalendarViewAdapter calendarAdapter;
    private int mCurrentPage;
    private boolean initiated = false;
    private long mExitTime;//纪录第一次点击返回时间
    private List<DiaryInfo> dayDiaryList;
    private List<DiaryInfo> monthDiaryList;
    private List<NoteInfo> dayNoteList;
    private List<NoteInfo> monthNoteList;
    private List<MemoInfo> dayMemoList;
    private List<MemoInfo> monthMemoList;
    private MemoRvAdapter memoRvAdapter;
    private DiaryRvAdapter diaryRvAdapter;
    private Integer monthLoaderTag = 0;
    private PopupWindow popupWindow;

    @Override
    protected void onResume() {
        super.onResume();

        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        navigationHeight = getResources().getDimensionPixelSize(resourceId);
        try {
            initViewTheme();
            initCurrentDate();
        } catch (Exception e) {
            LogUtils.e("MainActivity.e:" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void time2Mark() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                if (markData == null)
                    markData = new HashMap<>();
                markData.clear();
                if (monthDiaryList != null && monthDiaryList.size() > 0) {
                    for (int i = 0; i < monthDiaryList.size(); i++) {
                        Long time = monthDiaryList.get(i).getTime();
                        String dateString = DateUtil.long2Cal2String(time);
                        LogUtils.e("dataStr:" + dateString);
                        markData.put(dateString, "0");
                    }
                }
                if (monthNoteList != null && monthNoteList.size() > 0) {
                    for (int i = 0; i < monthNoteList.size(); i++) {
                        Long time = monthNoteList.get(i).getTime();
                        String dateString = DateUtil.long2Cal2String(time);
                        LogUtils.e("dataStr:" + dateString);
                        markData.put(dateString, "0");
                    }
                }
                if (monthMemoList != null && monthMemoList.size() > 0) {
                    for (int i = 0; i < monthMemoList.size(); i++) {
                        Long time = monthMemoList.get(i).getStartTime();
                        String dateString = DateUtil.long2Cal2String(time);
                        LogUtils.e("dataStr:" + dateString);
                        markData.put(dateString, "0");
                    }
                }
                mHandler.sendEmptyMessage(7);
            }
        }.start();
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }


    private PgoneWidgetService pgoneWidgetService;
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 建立连接
            // 获取服务的操作对象
            PgoneWidgetService.MyBinder binder = (PgoneWidgetService.MyBinder) service;
            // 获取到的Service即MyService
            pgoneWidgetService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 连接断开
        }
    };

    @Override
    public void initView() {
        rvDiary.setLayoutManager(new LinearLayoutManager(this));
        rvNote.setLayoutManager(new LinearLayoutManager(this));
        rvMemo.setLayoutManager(new LinearLayoutManager(this));
        Intent intentService = new Intent(this, PgoneWidgetService.class);
        if (serviceConnection != null)
            bindService(intentService, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void initViewTheme() /*throws Exception */ {
        //背景
        UIUtil.setThemeBackgroundPicture(ivBgMain);
        UIUtil.setThemeBackgroundPicture(ivBgMenu);
        UIUtil.setThemeBackgroundPicture(ivIv1);
        UIUtil.setThemeBackgroundPicture(ivIv2);
        UIUtil.setThemeBackgroundPicture(ivIv3);
        UIUtil.setThemeBackgroundPicture(ivIv4);
        UIUtil.setThemeBackgroundPicture(ivIv5);

        //字体
        UIUtil.setThemeFontColor(showMonthView);
        UIUtil.setThemeFontColor(showYearView);
        UIUtil.setThemeFontColor(backTodayButton);
        UIUtil.setThemeFontColor(themeSwitch);
        UIUtil.setThemeFontColor(nextMonth);
        UIUtil.setThemeFontColor(lastMonth);
        UIUtil.setThemeFontColor(tvTv1);
        UIUtil.setThemeFontColor(tvTv2);
        UIUtil.setThemeFontColor(tvTv3);
        UIUtil.setThemeFontColor(tvTv4);
        UIUtil.setThemeFontColor(tvTv5);
        UIUtil.setThemeFontColor(tvTv6);
        UIUtil.setThemeFontColor(tvTv7);
        UIUtil.setThemeFontColor(tvTv8);

        //主题颜色背景
        UIUtil.setThemeBackgroundColor(ivAdd);

        //设置背景透明度
        UIUtil.setThemeBackgroundSale(flMain2);
        UIUtil.setThemeBackgroundSale(rlMain2);

    }

    @OnClick({R.id.ll1, R.id.ll2, R.id.ll3, R.id.ll4, R.id.ll5, R.id.ll6, R.id.iv_head, R.id.iv_add, R.id.choose_date_view, R.id.back_today_button, R.id.theme_switch, R.id.next_month, R.id.last_month})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.ll1:
                //日记
                intent = new Intent(this, DiaryListActivity.class);
                startActivity(intent);
                break;
            case R.id.ll2:
                //便签
                intent = new Intent(this, NoteListActivity.class);
                startActivity(intent);
                break;
            case R.id.ll3:
                //日程
                intent = new Intent(this, MemoListActivity.class);
                startActivity(intent);
                break;
            case R.id.ll4:
                //我的消息
                break;
            case R.id.ll5:
                //换肤
                intent = new Intent(this, SkinActivity.class);
                startActivity(intent);
                break;
            case R.id.ll6:
                //测试
                intent = new Intent(this, TestActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_head:
                break;
            case R.id.iv_add:
//                给个动画，然后弹出弹框
                rotateAnim(cardView);
                break;
            case R.id.choose_date_view:
                break;
            case R.id.back_today_button:
                refreshMonthPager();
                break;
            case R.id.theme_switch:
                refreshSelectBackground();
                break;
            case R.id.next_month:
                calendarView.setCurrentItem(calendarView.getCurrentPosition() + 1);
                break;
            case R.id.last_month:
                calendarView.setCurrentItem(calendarView.getCurrentPosition() - 1);
                break;
        }
    }

    public void rotateAnim(final View view) {
        Animation anim = new RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setFillAfter(true); // 设置保持动画最后的状态
        anim.setDuration(300); // 设置动画时间
        anim.setInterpolator(new AccelerateInterpolator()); // 设置插入器
        view.startAnimation(anim);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                openPopupWindow(view);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void openPopupWindow(View v) {
        //防止重复按按钮
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        //设置PopupWindow的View
        View view = LayoutInflater.from(this).inflate(R.layout.view_popupwindow, null);
        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //设置背景,这个没什么效果，不添加会报错
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置点击弹窗外隐藏自身
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        //设置动画
        popupWindow.setAnimationStyle(R.style.PopupWindow);
        //设置位置
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, navigationHeight);
        //设置消失监听
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1);
            }
        });
        //设置PopupWindow的View点击事件
        setOnPopupViewClick(view);
        //设置背景色
        setBackgroundAlpha(0.5f);
    }

    private void setOnPopupViewClick(View view) {
        TextView tv_pick_phone, tv_pick_diary, tv_pick_zone, tv_cancel;
        tv_pick_phone = (TextView) view.findViewById(R.id.tv_pick_phone);
        tv_pick_zone = (TextView) view.findViewById(R.id.tv_pick_zone);
        tv_pick_diary = (TextView) view.findViewById(R.id.tv_pick_diary);
        tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_pick_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //皮皮日程
                startActivity(new Intent(MainActivity.this, MemoActivity.class));
                if (popupWindow != null && popupWindow.isShowing())
                    popupWindow.dismiss();
            }
        });
        tv_pick_zone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //皮皮便签
                startActivity(new Intent(MainActivity.this, NoteListActivity.class));
                if (popupWindow != null && popupWindow.isShowing())
                    popupWindow.dismiss();
            }
        });
        tv_pick_diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //皮皮日记
                startActivity(new Intent(MainActivity.this, DiaryActivity.class));
                if (popupWindow != null && popupWindow.isShowing())
                    popupWindow.dismiss();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null && popupWindow.isShowing())
                    popupWindow.dismiss();
            }
        });
    }

    //设置屏幕背景透明效果
    public void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        getWindow().setAttributes(lp);
    }

    private void initMarkData() {
        LogUtils.e("markData.size():" + markData.size());
        LogUtils.e("markData:" + new Gson().toJson(markData));
        calendarAdapter.setMarkData(markData);
    }

    /**
     * 初始化currentDate
     *
     * @return void
     */
    private void initCurrentDate() {
        currentDate = new CalendarDate();
        LogUtils.e("currentDate:" + currentDate);
        showYearView.setText(currentDate.getYear() + "年");
        showMonthView.setText(currentDate.getMonth() + "");
        initMonthAndDayData(currentDate, true);
    }

    /**
     * 初始化月数据
     *
     * @param date
     * @param isMonth
     */
    private void initMonthAndDayData(CalendarDate date, boolean isMonth) {
        int year = date.getYear();
        int month = date.getMonth();
        int day = date.getDay();
        if (isMonth) {
            //按月查找
            initDataAllTag();
//            final Long[] monthTime = DateUtil.getMonthTime(year, month);
//            if (monthTime != null && monthTime.length == 2) {
//                monthLoaderTag = 0;
//            }
        }
        //按日查找
        final Long[] dayTime = DateUtil.getDayTime(year, month, day);
        if (dayTime != null && dayTime.length == 2) {
            initDataByTime(dayTime);
        }
    }

    /**
     * 初始化月数据
     *
     * @param date
     */
    private void initDayData(CalendarDate date, int type) {
        int year = date.getYear();
        int month = date.getMonth();
        int day = date.getDay();
        //按日查找
        final Long[] time = DateUtil.getDayTime(year, month, day);
        if (time != null && time.length == 2) {
            switch (type) {
                case 1:
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            List<DiaryInfo> diaryInfoByTime = DaoController.getInstance().getDiaryInfoByTime(time[0], time[1]);
                            Message msg = Message.obtain();
                            msg.what = 4;
                            msg.obj = diaryInfoByTime;
                            mHandler.sendMessage(msg);
                        }
                    }.start();
                    break;
                case 2:
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            List<NoteInfo> noteInfoByTime = DaoController.getInstance().getNoteInfoByTime(time[0], time[1]);
                            Message msg1 = Message.obtain();
                            msg1.what = 5;
                            msg1.obj = noteInfoByTime;
                            mHandler.sendMessage(msg1);
                        }
                    }.start();
                    break;
                case 3:
                    CalendarReminderUtils.findCalendarEventByDate(this, time[0], time[1], new MemoCallback() {
                        @Override
                        public void memoCallback(List<MemoInfo> memoInfoList, String message) {
                            LogUtils.e("memokInfoList:" + new Gson().toJson(memoInfoList));
                            LogUtils.e("message:" + message);
                            Message msg = Message.obtain();
                            if (Config.Message_Success.equals(message)) {
                                msg.what = 6;
                                msg.obj = memoInfoList;
                            } else {
                                msg.what = 0;
                                msg.obj = message;
                            }
                            mHandler.sendMessage(msg);

                        }
                    });
                    break;
            }
        }
    }


    private void initDataAllTag() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                List<DiaryInfo> diaryInfoByTime = DaoController.getInstance().getDiaryInfoAll();
                List<NoteInfo> noteInfoByTime = DaoController.getInstance().getNoteInfoAll();
                Message msg = Message.obtain();
                msg.what = 1;
                msg.obj = diaryInfoByTime;
                mHandler.sendMessage(msg);
                Message msg1 = Message.obtain();
                msg1.what = 2;
                msg1.obj = noteInfoByTime;
                mHandler.sendMessage(msg1);
            }
        }.start();
        CalendarReminderUtils.findAllCalendarEvent(this, new MemoCallback() {
            @Override
            public void memoCallback(List<MemoInfo> memoInfoList, String message) {
                LogUtils.e("memokInfoList:" + new Gson().toJson(memoInfoList));
                LogUtils.e("message:" + message);
                Message msg = Message.obtain();
                if (Config.Message_Success.equals(message)) {
                    msg.what = 3;
                    msg.obj = memoInfoList;
                } else {
                    msg.what = 0;
                    msg.obj = message;
                }
                mHandler.sendMessage(msg);

            }
        });
    }

    private void initDataByTime(final Long[] time) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                List<DiaryInfo> diaryInfoByTime = DaoController.getInstance().getDiaryInfoByTime(time[0], time[1]);
                List<NoteInfo> noteInfoByTime = DaoController.getInstance().getNoteInfoByTime(time[0], time[1]);
                Message msg = Message.obtain();
                msg.what = 4;
                msg.obj = diaryInfoByTime;
                mHandler.sendMessage(msg);
                Message msg1 = Message.obtain();
                msg1.what = 5;
                msg1.obj = noteInfoByTime;
                mHandler.sendMessage(msg1);
            }
        }.start();
        CalendarReminderUtils.findCalendarEventByDate(this, time[0], time[1], new MemoCallback() {
            @Override
            public void memoCallback(List<MemoInfo> memoInfoList, String message) {
                LogUtils.e("memokInfoList:" + new Gson().toJson(memoInfoList));
                LogUtils.e("message:" + message);
                Message msg = Message.obtain();
                if (Config.Message_Success.equals(message)) {
                    msg.what = 6;
                    msg.obj = memoInfoList;
                } else {
                    msg.what = 0;
                    msg.obj = message;
                }
                mHandler.sendMessage(msg);

            }
        });
    }

    /**
     * 初始化CustomDayView，并作为CalendarViewAdapter的参数传入
     *
     * @return void
     */
    private void initCalendarView() {
//        initListener();
        CustomDayView customDayView = new CustomDayView(this, R.layout.custom_day);
        //偏移量 -1表示刷新成上一个月数据 ， 1表示刷新成下一个月数据
        onSelectDateListener = new OnSelectDateListener() {
            @Override
            public void onSelectDate(CalendarDate date) {
                initMonthAndDayData(date, false);
                refreshClickDate(date);
            }

            @Override
            public void onSelectOtherMonth(int offset) {
                //偏移量 -1表示刷新成上一个月数据 ， 1表示刷新成下一个月数据
                calendarView.selectOtherMonth(offset);
            }
        };

        calendarAdapter = new CalendarViewAdapter(
                this,
                onSelectDateListener,
                CalendarAttr.CalendayType.MONTH,
                customDayView);
        initMarkData();
        initMonthPager();
    }

    private ArrayList<Calendar> currentCalendars = new ArrayList<>();

    /**
     * 初始化monthPager，MonthPager继承自ViewPager
     *
     * @return void
     */
    private void initMonthPager() {
        calendarView.setAdapter(calendarAdapter);
        calendarView.setCurrentItem(MonthPager.CURRENT_DAY_INDEX);
        calendarView.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                position = (float) Math.sqrt(1 - Math.abs(position));
                page.setAlpha(position);
            }
        });
        calendarView.addOnPageChangeListener(new MonthPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPage = position;
                currentCalendars = calendarAdapter.getPagers();
                if (currentCalendars.get(position % currentCalendars.size()) instanceof Calendar) {
                    CalendarDate date = currentCalendars.get(position % currentCalendars.size()).getSeedDate();
                    initMonthAndDayData(date, false);
                    refreshClickDate(date);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void refreshClickDate(CalendarDate date) {
        //点击日期更新日期
        currentDate = date;
        showYearView.setText(date.getYear() + "年");
        showMonthView.setText(date.getMonth() + "");
    }

    /**
     * onWindowFocusChanged回调时，将当前月的种子日期修改为今天
     *
     * @return void
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !initiated) {
            refreshMonthPager();
            initiated = true;
        }
    }


    private void refreshMonthPager() {
        CalendarDate today = new CalendarDate();
        if (calendarAdapter != null)
            calendarAdapter.notifyDataChanged(today);
        showYearView.setText(today.getYear() + "年");
        showMonthView.setText(today.getMonth() + "");
    }

    private void refreshSelectBackground() {
        ThemeDayView themeDayView = new ThemeDayView(this, R.layout.custom_day_focus);
        calendarAdapter.setCustomDayRenderer(themeDayView);
        calendarAdapter.notifyDataSetChanged();
        calendarAdapter.notifyDataChanged(new CalendarDate());
    }


    /**
     * 重写返回键返回方法，防止误触退出
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if ((System.currentTimeMillis() - mExitTime) > 1000) {
                ToastUtil.showShort(MainActivity.this, "双击退出");
                mExitTime = System.currentTimeMillis();
            } else {
                ActivityCollector.finishAll();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void dayDiaryListener() {
        if (diaryRvAdapter != null)
            diaryRvAdapter.setOnItemClickListener(new OnDiaryListClickListener() {
                @Override
                public void OnItemSingleClick(int position) {
                    Intent intent = new Intent(MainActivity.this, DiaryActivity.class);
                    String diaryStr = new Gson().toJson(dayDiaryList.get(position));
                    LogUtils.e("diary:" + diaryStr);
                    intent.putExtra("diary", diaryStr);
                    startActivity(intent);
                }

                @Override
                public void OnItemDeleteClick(final int position) {
                    if (dayDiaryList != null && dayDiaryList.size() > position) {
                        new Thread() {
                            @Override
                            public void run() {
                                DiaryInfo diaryInfo = dayDiaryList.get(position);
                                diaryInfo.setTag(-1);
                                DaoController.getInstance().getDiaryInfoDao().update(diaryInfo);
                                Message msg = Message.obtain();
                                msg.what = 0;
                                msg.obj = "删除成功";
                                mHandler.sendMessage(msg);
                                initDayData(currentDate, 1);
                            }
                        }.start();
                    }
                }

                @Override
                public void OnItemDoubleClick(int position) {

                }
            });
    }

    private void dayNoteListener() {
        if (noteRvAdapter != null)
            noteRvAdapter.setOnItemClickListener(new OnDiaryListClickListener() {
                @Override
                public void OnItemSingleClick(int position) {
                    Intent intent = new Intent(MainActivity.this, NoteListActivity.class);
                    startActivity(intent);
                }

                @Override
                public void OnItemDeleteClick(final int position) {
                    if (dayNoteList != null && dayNoteList.size() > position) {
                        new Thread() {
                            @Override
                            public void run() {
                                NoteInfo noteInfo = dayNoteList.get(position);
                                noteInfo.setTag(-1);
                                DaoController.getInstance().getNoteInfoDao().update(noteInfo);
//                                Message msg = Message.obtain();
//                                msg.what = 0;
//                                msg.obj = "删除成功";
//                                mHandler.sendMessage(msg);
                                mHandler.sendEmptyMessage(8);
                                initDayData(currentDate, 2);
                            }
                        }.start();
                    }
                }

                @Override
                public void OnItemDoubleClick(int position) {

                }
            });
    }

    private void dayMemoListener() {
        if (memoRvAdapter != null)
            memoRvAdapter.setOnItemClickListener(new OnDiaryListClickListener() {
                @Override
                public void OnItemSingleClick(int position) {
                    Intent intent = new Intent(MainActivity.this, MemoActivity.class);
                    String memoStr = new Gson().toJson(dayMemoList.get(position));
                    LogUtils.e("memo:" + memoStr);
                    intent.putExtra("memo", memoStr);
                    startActivity(intent);
                }

                @Override
                public void OnItemDeleteClick(int position) {
                    if (dayMemoList != null && position >= 0 && dayMemoList.size() > position) {
                        deleteCalendarEvent(dayMemoList.get(position).getMemoInfoId());
                    }
                }

                @Override
                public void OnItemDoubleClick(int position) {

                }
            });
    }

    /**
     * 按id删除日程
     */
    private void deleteCalendarEvent(Long memoInfoId) {
        CalendarReminderUtils.deleteCalendarEvent(this, memoInfoId, new MemoCallback() {
            @Override
            public void memoCallback(List<MemoInfo> memoInfoList, String message) {
                Message msg = Message.obtain();
                msg.what = 0;
                if (Config.Message_Success.equals(message)) {
                    initDayData(currentDate, 3);
                    msg.obj = "删除成功";
                } else {
                    msg.obj = message;
                }
                mHandler.sendMessage(msg);
            }
        });
    }


    public void sendNoteBroadcast() {
        if (pgoneWidgetService != null) {
            pgoneWidgetService.updateNote();
        }
//        LogUtils.e("发送广播:");
//        Intent intent = new Intent(this, NoteReceiver.class);
//        intent.putExtra("name", "qqyumidi");
//        sendBroadcast(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceConnection != null)
            unbindService(serviceConnection);
    }
}
