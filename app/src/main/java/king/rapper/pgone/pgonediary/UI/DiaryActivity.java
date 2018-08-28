package king.rapper.pgone.pgonediary.UI;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.skydoves.multicolorpicker.ColorEnvelope;
import com.skydoves.multicolorpicker.MultiColorPickerView;
import com.skydoves.multicolorpicker.listeners.ColorListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.richeditor.RichEditor;
import king.rapper.pgone.pgonediary.MyApp;
import king.rapper.pgone.pgonediary.R;
import king.rapper.pgone.pgonediary.adapter.IconSelectAdapter;
import king.rapper.pgone.pgonediary.adapter.VoiceRvAdapter;
import king.rapper.pgone.pgonediary.dao.DaoController;
import king.rapper.pgone.pgonediary.entity.DiaryInfo;
import king.rapper.pgone.pgonediary.entity.ExpressionIcon;
import king.rapper.pgone.pgonediary.entity.MoodIcon;
import king.rapper.pgone.pgonediary.entity.Recording;
import king.rapper.pgone.pgonediary.entity.WeatherIcon;
import king.rapper.pgone.pgonediary.fragment.RecordAudioDialogFragment;
import king.rapper.pgone.pgonediary.interfaces.MeasureSizeInterface;
import king.rapper.pgone.pgonediary.interfaces.OnIconSelectClickListener;
import king.rapper.pgone.pgonediary.interfaces.OnVoiceRvItemClickListener;
import king.rapper.pgone.pgonediary.interfaces.PhotoCallBack;
import king.rapper.pgone.pgonediary.interfaces.ThreadDataCallBack;
import king.rapper.pgone.pgonediary.util.CommonUtil;
import king.rapper.pgone.pgonediary.util.DateUtil;
import king.rapper.pgone.pgonediary.util.LogUtils;
import king.rapper.pgone.pgonediary.util.ThreadDataUtil;
import king.rapper.pgone.pgonediary.util.ToastUtil;
import king.rapper.pgone.pgonediary.util.UIUtil;
import king.rapper.pgone.pgonediary.base.BaseActivity;
import king.rapper.pgone.pgonediary.base.TitleListener;
import king.rapper.pgone.pgonediary.util.constant.Config;
import king.rapper.pgone.pgonediary.util.constant.ResourceConfig;
import king.rapper.pgone.pgonediary.view.FullyLinearLayoutManager;
import king.rapper.pgone.pgonediary.view.MyRichEditor;
import king.rapper.pgone.pgonediary.view.sideslip.MyLinearLayout;

/**
 * @ Create_time: 2018/7/5 on 9:48.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
public class DiaryActivity extends BaseActivity {
    @BindView(R.id.title_bar_back_iv)
    ImageView titleBarBackIv;
    @BindView(R.id.title_bar_title)
    TextView titleBarTitle;
    @BindView(R.id.title_rl)
    RelativeLayout titleRl;
    @BindView(R.id.layout_title_bar_right_text)
    TextView layoutTitleBarRightText;
    @BindView(R.id.tv_header_day)
    TextView tvHeaderDay;
    @BindView(R.id.tv_header_date)
    TextView tvHeaderDate;
    @BindView(R.id.tv_header_week)
    TextView tvHeaderWeek;
    @BindView(R.id.iv_header_mood)
    ImageView ivHeaderMood;
    @BindView(R.id.iv_header_weather)
    ImageView ivHeaderWeather;
    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.iv_expression)
    ImageView ivExpression;
    @BindView(R.id.iv_picture)
    ImageView ivPicture;
    @BindView(R.id.iv_background)
    ImageView ivBackground;
    @BindView(R.id.iv_voice)
    ImageView ivVoice;
    @BindView(R.id.ll_activity)
    MyLinearLayout llActivity;
    @BindView(R.id.rv_bottom)
    RecyclerView rvBottom;
    @BindView(R.id.fl_bottom)
    FrameLayout flBottom;
    @BindView(R.id.iv_bg)
    ImageView ivBg;
    @BindView(R.id.ll_bg)
    LinearLayout llBg;
    @BindView(R.id.editor)
    MyRichEditor mEditor;
    @BindView(R.id.rv_voice)
    RecyclerView rvVoice;


    private DiaryInfo diary;
    private String[] dateYMDStr;
    private PopupWindow popupWindow;
    private List<MoodIcon> moodList = new ArrayList<>();
    private List<MoodIcon> moodLocalList = new ArrayList<>();
    private List<WeatherIcon> weatherList = new ArrayList<>();
    private List<WeatherIcon> weatherLocalList = new ArrayList<>();
    private List<ExpressionIcon> expressionList = new ArrayList<>();
    private List<ExpressionIcon> expressionLocalList = new ArrayList<>();
    // TODO: 2018/7/21 icon1   共8个
    private ThreadDataUtil threadDataUtil;
    private View hitView;
    private int bgDiaryWidth = Config.DefaultWidthSize;
    private int bgDiaryHeight = Config.DefaultHeightSize;
    private String testDiaryBackground;
    private String diaryText;
    private int dialogSelectColor;
    private VoiceRvAdapter voiceRvAdapter;
    private List<Recording> recordingList;
    private String testDiaryWeatherIcon;
    private String testDiaryMoodIcon;
    private long currentTimeMillis = 0;
    private String diaryTextHtml;

    @Override
    public int getLayoutId() {
        return R.layout.activity_diary;
    }

    @Override
    public void getPreIntent() {
        try {
            List<View> list = new ArrayList<>();
            View[] views = {ivExpression};
            llActivity.setView(flBottom, views);
            String diaryStr = getIntent().getStringExtra("diary");
            LogUtils.e("diary:" + diaryStr);
            if (!TextUtils.isEmpty(diaryStr))
                diary = new Gson().fromJson(diaryStr, DiaryInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initData() {
        rvVoice.setLayoutManager(new FullyLinearLayoutManager(this));

        //日期
        if (diary == null) {
            //新建
            currentTimeMillis = System.currentTimeMillis();
            dateYMDStr = DateUtil.getDateYMDStr();
        } else {
            //编辑
            etTitle.setText(diary.getTitle());
            dateYMDStr = DateUtil.getDateYMDStr(diary.getTime());
            diary.__setDaoSession(DaoController.getInstance().getDaoSession());
//            DiaryInfo diaryInfo = diary.getDiaryInfo();
//            diaryInfo.__setDaoSession(DaoController.getInstance().getDaoSession());
            recordingList = diary.getRecordingList();
            if (recordingList != null && recordingList.size() > 0) {
                rvVoiceShow(recordingList);
            }
            diaryTextHtml = diary.getContent();
            if (!TextUtils.isEmpty(diaryTextHtml)) {
                mEditor.setHtml(diaryTextHtml);
            }
            if (!TextUtils.isEmpty(diary.getWeather())) {
                UIUtil.setEndWithGIF(ivHeaderWeather, diary.getWeather());
            }
            if (!TextUtils.isEmpty(diary.getMood())) {
                UIUtil.setEndWithGIF(ivHeaderMood, diary.getMood());
            }
            if (!TextUtils.isEmpty(diary.getBackground())) {
                UIUtil.setEndWithGIF(ivBackground, diary.getBackground());
                UIUtil.setEndWithGIF(ivBg, diary.getBackground());
            }

        }
        if (dateYMDStr != null && dateYMDStr.length == 4) {
            tvHeaderDate.setText(dateYMDStr[0] + "年" + dateYMDStr[1] + "月");
            tvHeaderDay.setText(dateYMDStr[2]);
            tvHeaderWeek.setText("星期" + dateYMDStr[3]);
        }

        //测量背景图
        UIUtil.measureDefaultSize(ivBg, new MeasureSizeInterface() {
            @Override
            public void defaultSize(int width, int height) {
                bgDiaryWidth = width;
                bgDiaryHeight = height;
            }
        });

        //天气
        weatherLocalList = ResourceConfig.initWeatherLocalIcon(MyApp.getInstance());
        updateWeather();


        //心情
        moodLocalList = ResourceConfig.initMoodLocalIcon(MyApp.getInstance());
        updateMood();

        //表情
        expressionLocalList = ResourceConfig.initExpressionLocalIcon(MyApp.getInstance());
        updateExpression();

        // TODO: 2018/7/21 icon2
    }

    private void rvVoiceShow(final List<Recording> recordingList) {
        LogUtils.e("recordingList.size():" + recordingList.size());
        if (rvVoice.getVisibility() != View.VISIBLE)
            rvVoice.setVisibility(View.VISIBLE);
        LogUtils.e("rvVoice:" + Integer.toString(rvVoice.getVisibility()));
        if (voiceRvAdapter == null) {
            voiceRvAdapter = new VoiceRvAdapter(this, recordingList);
            rvVoice.setAdapter(voiceRvAdapter);
            voiceRvAdapter.setItemClickListener(new OnVoiceRvItemClickListener() {
                @Override
                public void onDeleteClick(int position) {
                    LogUtils.e("删除这个录音");
                    recordingList.remove(position);
                    voiceRvAdapter.setList(recordingList);
                }
            });
        } else {
            voiceRvAdapter.setList(recordingList);
        }
    }

    private boolean mHasAddImg = false;

    @Override
    protected void initListener() {

        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                if (mHasAddImg) {
                    mHasAddImg = false;
                    mEditor.setNewLine();
                }
                diaryText = text;
                diaryTextHtml = mEditor.getHtml();
                LogUtils.e("diaryTextHtml:    " + diaryTextHtml);
            }
        });
        rvBottom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                LogUtils.e("hasFocus:" + hasFocus);
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                } else {
                    // 此处为失去焦点时的处理内容
                    rvBottom.removeAllViews();
                    rvBottom.setVisibility(View.GONE);
                }
            }
        });
    }

    @OnClick({R.id.iv_header_mood, R.id.iv_header_weather, R.id.iv_expression, R.id.iv_picture, R.id.iv_background, R.id.iv_voice,
            R.id.action_undo, R.id.action_redo, R.id.action_bold, R.id.action_italic,
            R.id.action_subscript, R.id.action_superscript, R.id.action_strikethrough, R.id.action_insert_link, R.id.action_insert_checkbox,
            R.id.action_underline, R.id.action_heading1, R.id.action_heading2, R.id.action_heading3, R.id.action_heading4, R.id.action_heading5,
            R.id.action_heading6, R.id.action_txt_color, R.id.action_bg_color, R.id.action_indent, R.id.action_outdent, R.id.action_align_left,
            R.id.action_align_center, R.id.action_align_right, R.id.action_insert_bullets, R.id.action_insert_numbers, R.id.action_blockquote,
            R.id.action_insert_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_header_mood:
                popupIconSelect(MoodIcon.class, moodLocalList == null ? 0 : moodLocalList.size(), 0, 0,
                        0, Config.ExpressionWidthSize, Config.ExpressionHeightSize);
                break;
            case R.id.iv_header_weather:
                popupIconSelect(WeatherIcon.class, weatherLocalList == null ? 0 : weatherLocalList.size(), 1,
                        0, 0, 19, 16);
                break;
            case R.id.iv_expression:
                //表情
                mEditor.requestFocus();
                flBottomShow();
                popupIconSelect(ExpressionIcon.class, expressionLocalList == null ? 0 : expressionLocalList.size(), 2,
                        0, 0, Config.ExpressionWidthSize, Config.ExpressionHeightSize);
                break;
            case R.id.iv_picture:
                mEditor.requestFocus();
                showPhotoDialog(DiaryActivity.this, 1, 0, 0, 1, 1,/* Config.PictureWidthSize,
                        Config.PictureHeightSize,*/ new PhotoCallBack() {
                    @Override
                    public void success(final List<Uri> list) {
                        //加载出来
                        if (list != null && list.size() > 0 && !TextUtils.isEmpty(list.get(0).toString())) {
                            mHasAddImg = true;
                            LogUtils.e("加载图片");
                            mEditor.insertImage(list.get(0).toString(), "picture\" style=\"max-width:100% \"  width=\"" + Config.PictureWidthSize + " \" height=\"" + Config.PictureHeightSize + " \"");
//                            mEditor.insertImage(list.get(0).toString(), "picvision\" style=\"max-width:100% ");
                        }
                    }
                });
                break;
            case R.id.iv_background:
                showPhotoDialog(DiaryActivity.this, 1, bgDiaryWidth, bgDiaryHeight, bgDiaryWidth, bgDiaryHeight, new PhotoCallBack() {
                    @Override
                    public void success(final List<Uri> list) {
                        //加载出来
                        if (list != null && list.size() > 0) {
                            testDiaryBackground = list.get(0).toString();
                            UIUtil.setEndWithGIF(ivBackground, testDiaryBackground);
                            UIUtil.setEndWithGIF(ivBg, testDiaryBackground);
                        }
                    }
                });
                break;
            case R.id.iv_voice:
                final RecordAudioDialogFragment fragment = RecordAudioDialogFragment.newInstance();
                fragment.show(this.getSupportFragmentManager(), RecordAudioDialogFragment.class.getSimpleName());
                fragment.setCancelable(false);
                fragment.setOnCancelListener(new RecordAudioDialogFragment.OnAudioCancelListener() {
                    @Override
                    public void onCancel() {
                        fragment.dismiss();
                    }
                });
                break;
            case R.id.action_undo:
                mEditor.undo();
                break;
            case R.id.action_redo:
                mEditor.redo();
                break;
            case R.id.action_bold:
                mEditor.setBold();
                break;
            case R.id.action_italic:
                mEditor.setItalic();
                break;
            case R.id.action_subscript:
                mEditor.setSubscript();
                break;
            case R.id.action_superscript:
                mEditor.setSuperscript();
                break;
            case R.id.action_strikethrough:
                mEditor.setStrikeThrough();
                break;
            case R.id.action_underline:
                mEditor.setUnderline();
                break;
            case R.id.action_heading1:
                mEditor.setHeading(1);
                break;
            case R.id.action_heading2:
                mEditor.setHeading(2);
                break;
            case R.id.action_heading3:
                mEditor.setHeading(3);
                break;
            case R.id.action_heading4:
                mEditor.setHeading(4);
                break;
            case R.id.action_heading5:
                mEditor.setHeading(5);
                break;
            case R.id.action_heading6:
                mEditor.setHeading(6);
                break;
            case R.id.action_txt_color:
                //前景色
                dialogColorSelect(0);
                break;
            case R.id.action_bg_color:
                //背景色
                dialogColorSelect(1);
                break;
            case R.id.action_indent:
                mEditor.setIndent();
                break;
            case R.id.action_outdent:
                mEditor.setOutdent();
                break;
            case R.id.action_align_left:
                mEditor.setAlignLeft();
                break;
            case R.id.action_align_center:
                mEditor.setAlignCenter();
                break;
            case R.id.action_align_right:
                mEditor.setAlignRight();
                break;
            case R.id.action_insert_bullets:
                mEditor.setBullets();
                break;
            case R.id.action_insert_numbers:
                mEditor.setNumbers();
                break;
            case R.id.action_blockquote:
                mEditor.setBlockquote();
                break;
            case R.id.action_insert_image:
                break;
            case R.id.action_insert_link:
                dialogInsertLink();
                break;
            case R.id.action_insert_checkbox:
                mEditor.insertTodo();
                break;
        }
    }

    private void dialogInsertLink() {
        final Dialog dialog = new Dialog(this, R.style.transparentDialog);
        final View view = View.inflate(this, R.layout.dialog_link_input, null);
        final TextView tv_submit = (TextView) view.findViewById(R.id.tv_submit);
        final EditText et_link = (EditText) view.findViewById(R.id.et_link);
        dialog.setContentView(view);
        //使得点击对话框外部消失对话框
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtil.checkEmpty(et_link)) {
                    ToastUtil.showShort("请输入链接");
                } else {
                    String url = et_link.getText().toString().trim();
                    mEditor.insertLink(url, "皮皮日记");
                    dialog.dismiss();
                }
            }
        });

    }

    public void addRecording(Recording recording) {
        LogUtils.e("recording:" + recording);
        if (recording != null) {
            if (recordingList == null)
                recordingList = new ArrayList<>();
            recordingList.add(recording);
            rvVoiceShow(recordingList);
        }
    }

    private void dialogColorSelect(final int type) {
        final Dialog dialog = new Dialog(this, R.style.transparentDialog);
        final View view = View.inflate(this, R.layout.dialog_select_color, null);
        final TextView tv_color = (TextView) view.findViewById(R.id.color_view);
        MultiColorPickerView multiColorPickerView = (MultiColorPickerView) view.findViewById(R.id.multiColorPickerView);
        dialog.setContentView(view);
        //使得点击对话框外部消失对话框
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();


        multiColorPickerView.addSelector(ContextCompat.getDrawable(this, R.drawable.wheel), new ColorListener() {
            @Override
            public void onColorSelected(ColorEnvelope colorEnvelope) {
                LogUtils.e("color:" + "#" + colorEnvelope.getHtmlCode());
                dialogSelectColor = Color.parseColor("#" + colorEnvelope.getHtmlCode());
                tv_color.setBackgroundColor(dialogSelectColor);
            }
        });
        multiColorPickerView.setFlagMode(MultiColorPickerView.FlagMode.LAST);
        multiColorPickerView.setFlagFlipable(false);

        tv_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (type == 0) {
                    //设置字体颜色
                    mEditor.setTextColor(dialogSelectColor);
                } else if (type == 1) {
                    //设置背景颜色
                    mEditor.setTextBackgroundColor(dialogSelectColor);
                }
            }
        });


    }

    private void flBottomShow() {
        for (int i = 0; i < flBottom.getChildCount(); i++) {
            flBottom.getChildAt(i).setVisibility(View.GONE);
        }
        flBottom.setVisibility(View.VISIBLE);
    }

    private <T> void popupIconSelect(final Class<T> tClass, final int localCount, final int type, final int reqWidth, final int reqHeight, final int aspectRatioX, final int aspectRatioY) {
        List<T> dataList = iconChange3(tClass);

        final RecyclerView recyclerview = getRecycleView(type);


        recyclerview.setLayoutManager(new
                StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL));
        final IconSelectAdapter adapter = new IconSelectAdapter(this, dataList, type);
        adapter.setHasStableIds(true);
        recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnIconSelectClickListener() {
            @Override
            public void onItemAddClick(final int position) {
                int imgCount = 9;
                if (tClass == ExpressionIcon.class) {
                    imgCount = 1;
                }
                showPhotoDialog(DiaryActivity.this, imgCount, reqWidth, reqHeight, aspectRatioX, aspectRatioY, new PhotoCallBack() {
                    @Override
                    public void success(final List<Uri> list) {
                        //保存到本地，并加载出来
                        if (threadDataUtil == null)
                            threadDataUtil = new ThreadDataUtil();
                        threadDataUtil.addIcon(tClass, DiaryActivity.this, list, new ThreadDataCallBack() {
                            @Override
                            public void dataCallBack(List<?> addDataList, List<?> allDataList) {
                                if (addDataList != null && allDataList.size() > 0 && allDataList != null && allDataList.size() > 0) {
                                    //更新数据
                                    List dataList = iconChange4(tClass, allDataList);
                                    //更新UI
                                    if ((popupWindow != null && popupWindow.isShowing() && adapter != null) || rvBottom.getVisibility() == View.VISIBLE)
                                        adapter.setList(dataList);
                                }
                            }
                        });
                    }
                });
            }

            @Override
            public void onItemSelectClick(int position) {
                ToastUtil.showShort("点击：" + position);
                if (tClass == MoodIcon.class) {
                    //选择心情图标
                    testDiaryMoodIcon = moodList.get(position).getUrl();
                    String imgType = moodList.get(position).getType();
                    UIUtil.setEndWithGIF(ivHeaderMood, testDiaryMoodIcon, imgType);
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                } else if (tClass == WeatherIcon.class) {
                    //选择天气图标
                    testDiaryWeatherIcon = weatherList.get(position).getUrl();
                    String imgType = weatherList.get(position).getType();
                    UIUtil.setEndWithGIF(ivHeaderWeather, testDiaryWeatherIcon, imgType);
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                } else if (tClass == ExpressionIcon.class) {
                    //选择表情图标
                    String url = expressionList.get(position).getUrl();
                    String imgType = expressionList.get(position).getType();
                    mEditor.insertImage(url, "picture\" style=\"max-width:100% \"  width=\"" + Config.ExpressionWidthSize + " \" height=\"" + Config.ExpressionHeightSize + " \"");
                    if (flBottom.getVisibility() == View.VISIBLE) {
                        flBottom.setVisibility(View.GONE);
                        for (int i = 0; i < flBottom.getChildCount(); i++) {
                            flBottom.getChildAt(i).setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onItemLongClick(int position) {
                //长按删除图标
                recyclerview.setAnimationCacheEnabled(false);
                if (recyclerview.getChildCount() > localCount)
                    for (int i = localCount; i < recyclerview.getChildCount() - 1; i++) {
                        showShakeAnim(recyclerview.getChildAt(i), i, adapter, recyclerview, type, tClass);
                    }

            }
        });
    }


    /**
     * 抖动动画
     *
     * @param view
     * @param position
     * @param adapter
     * @param recyclerview
     */

    private <T> void showShakeAnim(final View view, final int position, final IconSelectAdapter adapter, final RecyclerView recyclerview, final int type, final Class<T> tClass) {
        final ImageView iv_delete = new ImageView(this);
        iv_delete.setBackgroundResource(R.mipmap.icon_item_delete);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(30, 30);
        params.gravity = Gravity.RIGHT;
        ((FrameLayout) view).addView(iv_delete, params);
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "rotation", -2, 2);
        anim.setDuration(100);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setRepeatCount(Integer.MAX_VALUE);
        anim.setRepeatMode(ObjectAnimator.REVERSE);
        anim.start();
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(view, "rotation", 2,
                -2);
        anim2.setDuration(50);
        anim2.setInterpolator(new AccelerateDecelerateInterpolator());
        anim2.setRepeatCount(Integer.MAX_VALUE);
        anim2.setRepeatMode(ObjectAnimator.REVERSE);
        anim2.start();
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.e("删除了图标：" + position);
                FrameLayout frameLayout = (FrameLayout) recyclerview.getChildAt(recyclerview.getChildCount() - 2);
                if (frameLayout != null) {
                    frameLayout.clearAnimation();
                    if (frameLayout.getChildCount() > 1) {
                        frameLayout.removeViewAt(frameLayout.getChildCount() - 1);
                    }
                }
                List dataList = iconChange5(tClass, position);
                //更新UI
                if ((popupWindow != null && popupWindow.isShowing() && adapter != null) || rvBottom.getVisibility() == View.VISIBLE)
                    adapter.setList(dataList);
            }
        });
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
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (event.getRepeatCount() == 0 && popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
                return true;
            } else if (flBottom.getVisibility() == View.VISIBLE) {
                flBottom.setVisibility(View.GONE);
                for (int i = 0; i < flBottom.getChildCount(); i++) {
                    flBottom.getChildAt(i).setVisibility(View.GONE);
                }
                return true;
            } else if (mEditor.canGoBack()) {
                mEditor.goBack(); //goBack()表示返回WebView的上一页面
                if (!TextUtils.isEmpty(diaryTextHtml))
                    mEditor.setHtml(diaryTextHtml);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void initView() {
        try {
            initTitleBar("皮皮日记", "保存", new TitleListener() {
                @Override
                public void RightClick() {
                    LogUtils.e("diary:" + diary);
                    if (diary == null) {
                        //新建
                        diary = new DiaryInfo();
                        if (!TextUtils.isEmpty(testDiaryBackground)) {
                            diary.setBackground(testDiaryBackground);
                        } else {
                            diary.setBackground("android.resource://" + getPackageName() + "/" + R.mipmap.bg_diary);
                        }
                        if (!TextUtils.isEmpty(testDiaryMoodIcon)) {
                            diary.setMood(testDiaryMoodIcon);
                        } else {
                            diary.setMood("android.resource://" + getPackageName() + "/" + R.mipmap.mood0);
                        }
                        if (!TextUtils.isEmpty(testDiaryWeatherIcon)) {
                            diary.setWeather(testDiaryWeatherIcon);
//                            diaryInfo.setWeather(testDiaryWeatherIcon);
                        } else {
                            diary.setWeather("android.resource://" + getPackageName() + "/" + R.mipmap.weather0);
                        }
                        if (mEditor != null && !TextUtils.isEmpty(mEditor.getHtml())) {
                            diary.setContent(mEditor.getHtml());
                        }
                        if (!CommonUtil.checkEmpty(etTitle)) {
                            String title = etTitle.getText().toString();
                            diary.setTitle(title);
                        } else {
                            diary.setTitle("皮皮日记");
                        }
                        if (currentTimeMillis != 0) {
                            diary.setTime(currentTimeMillis);
                        } else {
                            currentTimeMillis = System.currentTimeMillis();
                            diary.setTime(currentTimeMillis);
                        }

                        diary.setTag(0);
                        LogUtils.e("diaryInfo:" + diary);
                        DaoController.getInstance().getDiaryInfoDao().insert(diary);
                        LogUtils.e("diaryInfo:" + diary);
                        if (recordingList != null && recordingList.size() > 0) {
                            LogUtils.e("diaryInfo.getDiaryInfoId():" + diary.getDiaryInfoId());
                            for (int i = 0; i < recordingList.size(); i++) {
                                recordingList.get(i).setDiaryJoinRecordingId(diary.getDiaryInfoId());
                                DaoController.getInstance().getRecordingDao().insert(recordingList.get(i));
                                LogUtils.e("recordingList:" + recordingList.get(i));
                            }
                        }
                        LogUtils.e("diary:" + diary);
                    } else {
                        //更新
                        if (!TextUtils.isEmpty(testDiaryBackground)) {
                            diary.setBackground(testDiaryBackground);
                        }
                        if (!TextUtils.isEmpty(testDiaryMoodIcon)) {
                            diary.setMood(testDiaryMoodIcon);
                        }
                        if (!TextUtils.isEmpty(testDiaryWeatherIcon)) {
                            diary.setWeather(testDiaryWeatherIcon);
                        }
                        if (!TextUtils.isEmpty(diaryText) && mEditor != null) {
                            diary.setContent(mEditor.getHtml());
                        }
                        if (!CommonUtil.checkEmpty(etTitle)) {
                            String title = etTitle.getText().toString();
                            diary.setTitle(title);
                        }
                        diary.resetRecordingList();
                        DaoController.getInstance().getDiaryInfoDao().update(diary);
                        if (recordingList != null) {
                            for (int i = 0; i < recordingList.size(); i++) {
                                recordingList.get(i).setDiaryJoinRecordingId(diary.getDiaryInfoId());
                                DaoController.getInstance().getRecordingDao().insert(recordingList.get(i));
                                LogUtils.e("recordingList:" + recordingList.get(i));
                            }
                        }
                        diary.setTag(0);
                        diary.resetRecordingList();
                        DaoController.getInstance().getDiaryInfoDao().update(diary);
                    }
                    ToastUtil.showShort("保存成功");
                    finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(16);
        mEditor.setEditorFontColor(Color.parseColor("#666666"));
        mEditor.setEditorBackgroundColor(Color.TRANSPARENT);
        mEditor.setBackgroundColor(Color.TRANSPARENT);
        //mEditor.setBackgroundResource(R.drawable.bg);
        mEditor.setPadding(10, 10, 10, 10);
        //mEditor.setBackground("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg");
        mEditor.setPlaceholder("皮皮日记...");
    }


    private void updateWeather() {
        if (weatherList == null)
            weatherList = new ArrayList<>();
        weatherList.clear();
        if (weatherLocalList != null)
            weatherList.addAll(weatherLocalList);
        List<WeatherIcon> list = DaoController.getInstance().getWeatherIconDao().queryBuilder().list();
        if (list != null && list.size() > 0)
            weatherList.addAll(list);
    }

    private void updateMood() {
        if (moodList == null)
            moodList = new ArrayList<>();
        moodList.clear();
        if (moodLocalList != null)
            moodList.addAll(moodLocalList);
        List<MoodIcon> list = DaoController.getInstance().getMoodIconDao().queryBuilder().list();
        if (list != null && list.size() > 0)
            moodList.addAll(list);
    }

    private void updateExpression() {
        if (expressionList == null)
            expressionList = new ArrayList<>();
        expressionList.clear();
        if (expressionLocalList != null)
            expressionList.addAll(expressionLocalList);
        List<ExpressionIcon> list = DaoController.getInstance().getExpressionIconDao().queryBuilder().list();
        if (list != null && list.size() > 0)
            expressionList.addAll(list);
    }

    private <T> List iconChange3(Class<T> tClass) {
        List<T> dataList = new ArrayList<>();
        // TODO: 2018/7/21 icon3
        if (tClass == MoodIcon.class) {
            //表情
            hitView = ivHeaderMood;
            dataList = (List<T>) moodList;
        } else if (tClass == WeatherIcon.class) {
            //天气
            hitView = ivHeaderWeather;
            dataList = (List<T>) weatherList;
        } else if (tClass == ExpressionIcon.class) {
            hitView = ivExpression;
            dataList = ((List<T>) expressionList);
        }
        return dataList;
    }

    private <T> List iconChange4(Class<T> tClass, List<?> allDataList) {

        // TODO: 2018/7/21 icon4
        List dataList = new ArrayList();
        if (tClass == WeatherIcon.class) {
            if (weatherList == null)
                weatherList = new ArrayList<>();
            weatherList.clear();
            weatherList.addAll(weatherLocalList);
            weatherList.addAll((Collection<? extends WeatherIcon>) allDataList);
            dataList = weatherList;
        } else if (tClass == MoodIcon.class) {
            if (moodList == null)
                moodList = new ArrayList<>();
            moodList.clear();
            moodList.addAll(moodLocalList);
            moodList.addAll((Collection<? extends MoodIcon>) allDataList);
            dataList = moodList;
        } else if (tClass == ExpressionIcon.class) {
            if (expressionList == null)
                expressionList = new ArrayList<>();
            expressionList.clear();
            expressionList.addAll(expressionLocalList);
            expressionList.addAll((Collection<? extends ExpressionIcon>) allDataList);
            dataList = expressionList;
        }
        return dataList;
    }

    private <T> List iconChange5(Class<T> tClass, int position) {

        // TODO: 2018/7/21 icon5
        List dataList = new ArrayList<>();
        if (tClass == MoodIcon.class) {
            //表情
            DaoController.getInstance().getMoodIconDao().delete(moodList.get(position));
            updateMood();
            dataList = moodList;
        } else if (tClass == WeatherIcon.class) {
            //天气
            DaoController.getInstance().getWeatherIconDao().delete(weatherList.get(position));
            updateWeather();
            dataList = weatherList;
        } else if (tClass == ExpressionIcon.class) {
            //天气
            DaoController.getInstance().getExpressionIconDao().delete(expressionList.get(position));
            updateExpression();
            dataList = expressionList;
        }
        return dataList;
    }

    public RecyclerView getRecycleView(int type) {
        if (type < 2) {
            //popup布局
            final View popupView = LayoutInflater.from(this).inflate(R.layout.recycleview, null);
            popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT, false) {
                @Override
                public void showAsDropDown(View anchor) {
                    if (Build.VERSION.SDK_INT >= 24) {
                        Rect visibleFrame = new Rect();
                        anchor.getGlobalVisibleRect(visibleFrame);
                        int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
                        setHeight(height);
                    }
                    super.showAsDropDown(anchor);
                }
            };

//        // 允许点击外部消失
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//注意这里如果不设置，下面的setOutsideTouchable(true);允许点击外部消失会失效
            popupWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.grayf5)));
            popupWindow.setOutsideTouchable(true);   //设置外部点击关闭ppw窗口


            int location[] = new int[2];
            if (hitView != null && type < 2) {
                int x, y;
                hitView.getLocationOnScreen(location);
                x = location[0];
                y = location[1];
                int h = hitView.getHeight();
                popupWindow.showAtLocation(hitView, Gravity.NO_GRAVITY, 0, y + h);
            } else {
                popupWindow.showAtLocation(llActivity, Gravity.BOTTOM, 0, 0);
            }

            return popupView.findViewById(R.id.recyclerview);
        } else {
            rvBottom.setVisibility(View.VISIBLE);
            return rvBottom;
        }
    }
}
