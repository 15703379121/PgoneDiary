package king.rapper.pgone.pgonediary.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import king.rapper.pgone.pgonediary.util.DensityUtil;
import king.rapper.pgone.pgonediary.util.LogUtils;


/**
 * @author ArcherYc
 * @date on 2018/7/11  下午2:28
 * @mail 247067345@qq.com
 */
@SuppressLint("AppCompatCustomView")
public class RecordButton extends TextView {
    private Context mContext;

    private Paint mRectPaint;

    private Paint mCirclePaint;

    private float corner;
    private float circleRadius;
    private float circleStrokeWidth;
    private float rectWidth;

    private float mMinCircleRadius;
    private float mMaxCircleRadius;
    private float mMinRectWidth;
    private float mMaxRectWidth;
    private float mMinCorner;
    private float mMaxCorner;
    private float mMinCircleStrokeWidth;
    private float mMaxCircleStrokeWidth;

    private MediaRecorder recorder;

    private ObtainDecibelThread thread;

    private RectF mRectF = new RectF();

    private RecordMode mRecordMode = RecordMode.ORIGIN;

    private AnimatorSet mBeginAnimatorSet = new AnimatorSet();

    private AnimatorSet mEndAnimatorSet = new AnimatorSet();

    private Xfermode mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);

    private Handler mHandler = new Handler();

    private ClickRunnable mClickRunnable = new ClickRunnable();

    private OnRecordStateChangedListener mOnRecordStateChangedListener;

    private float mInitX;

    private float mInitY;

    private float mDownRawX;

    private float mDownRawY;

    private float mInfectionPoint;

    private ScrollDirection mScrollDirection;

    private boolean mHasCancel = false;
    private boolean isInit;
    private String fileName;
    private boolean isPlaying;
    private int type = 0;


    public RecordButton(Context context) {
        this(context, null);
        init();
    }

    public RecordButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public RecordButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        File file = new File(mFileName);
        if (!file.exists()) {
            //创建新的文件夹
            file.mkdir();
        }


        setLayerType(LAYER_TYPE_HARDWARE, null);
        mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectPaint.setStyle(Paint.Style.FILL);
//        mRectPaint.setColor(Color.WHITE);
        mRectPaint.setColor(Color.parseColor("#ec6b64"));

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(Color.parseColor("#66EA4266"));

        mMinCircleStrokeWidth = DensityUtil.dp2px(mContext, 3);
        mMaxCircleStrokeWidth = DensityUtil.dp2px(mContext, 12);
        circleStrokeWidth = mMinCircleStrokeWidth;
        mCirclePaint.setStrokeWidth(circleStrokeWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        int centerX = width / 2;
        int centerY = height / 2;

        mMaxRectWidth = width / 3;
        mMinRectWidth = mMaxRectWidth * 0.6f;

        mMinCircleRadius = mMaxRectWidth / 2 + mMinCircleStrokeWidth + DensityUtil.dp2px(mContext, 5);
        mMaxCircleRadius = width / 2 - mMaxCircleStrokeWidth;

        mMinCorner = DensityUtil.dp2px(mContext, 5);
        mMaxCorner = mMaxRectWidth / 2;

        if (rectWidth == 0) {
            rectWidth = mMaxRectWidth;
        }
        if (circleRadius == 0) {
            circleRadius = mMinCircleRadius;
        }
        if (corner == 0) {
            corner = rectWidth / 2;
        }

        mCirclePaint.setColor(Color.parseColor("#33EA4266"));
        canvas.drawCircle(centerX, centerY, circleRadius, mCirclePaint);
        mCirclePaint.setXfermode(mXfermode);

        mCirclePaint.setColor(Color.parseColor("#000000"));
        canvas.drawCircle(centerX, centerY, circleRadius - circleStrokeWidth, mCirclePaint);
        mCirclePaint.setXfermode(null);

        mRectF.left = centerX - rectWidth / 2;
        mRectF.right = centerX + rectWidth / 2;
        mRectF.top = centerY - rectWidth / 2;
        mRectF.bottom = centerY + rectWidth / 2;
        canvas.drawRoundRect(mRectF, corner, corner, mRectPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LogUtils.e("action_down");
                isInit = true;
                if (mRecordMode == RecordMode.ORIGIN && inBeginRange(event)) {
                    mDownRawX = event.getRawX();
                    mDownRawY = event.getRawY();
                    mHandler.postDelayed(mClickRunnable, 200);
                    if (mOnRecordStateChangedListener != null)
                        mOnRecordStateChangedListener.onRecordStart();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtils.e("action_move");
                if (!mHasCancel) {
                    if (mRecordMode == RecordMode.LONG_CLICK) {
                        //初始化对话框和录音
                        if (isInit) {
                            startBeginAnimation();
                            initDialogAndStartRecord();
                            isInit = false;
                        }
                        ScrollDirection mOldDirection = mScrollDirection;
                        float oldY = getY();
                        setX(mInitX + event.getRawX() - mDownRawX);
                        setY(mInitY + event.getRawY() - mDownRawY);
                        float newY = getY();

                        if (newY <= oldY) {
                            mScrollDirection = ScrollDirection.UP;
                        } else {
                            mScrollDirection = ScrollDirection.DOWN;
                        }

                        if (mOldDirection != mScrollDirection) {
                            mInfectionPoint = oldY;
                        }
                        float zoomPercentage = (mInfectionPoint - getY()) / mInitY;
                        if (mOnRecordStateChangedListener != null)
                            mOnRecordStateChangedListener.onZoom(zoomPercentage);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                LogUtils.e("action_up");
                isInit = false;
                if (!mHasCancel) {
                    if (mRecordMode == RecordMode.LONG_CLICK) {
                        LogUtils.e("radish" + "onTouchEvent: 长按录制");
//                        if (mOnRecordStateChangedListener != null)
//                            mOnRecordStateChangedListener.onRecordStop();
                        resetLongClick();
                        finishRecord();
                    } else if (mRecordMode == RecordMode.ORIGIN && inBeginRange(event)) {
                        LogUtils.e("radish" + "onTouchEvent: 点击播放");
                        isPlaying = true;
                        if (mOnRecordStateChangedListener != null)
                            mOnRecordStateChangedListener.onRecordPlayStart();

                        mHandler.removeCallbacks(mClickRunnable);
                        mRecordMode = RecordMode.SINGLE_CLICK;
                    } else if (mRecordMode == RecordMode.SINGLE_CLICK && inEndRange(event)) {
                        LogUtils.e("radish" + "onTouchEvent: 点击停止");
                        if (mOnRecordStateChangedListener != null)
                            mOnRecordStateChangedListener.onRecordPlayStop();
                        resetSingleClick();
                    }
                } else {
                    mHasCancel = false;
                }
                break;
            case MotionEvent.ACTION_CANCEL:// 当手指移动到view外面，会cancel
                LogUtils.e("action_cancel");
                //取消录音
                cancelRecord();
                break;
            default:
                break;
        }
        return true;
    }

    public void setType(int type) {
        this.type = type;
    }

    private void onTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LogUtils.e("action_down");
                isInit = true;
                if (mRecordMode == RecordMode.ORIGIN && inBeginRange(event)) {
                    mDownRawX = event.getRawX();
                    mDownRawY = event.getRawY();
                    mHandler.postDelayed(mClickRunnable, 200);
                    if (mOnRecordStateChangedListener != null)
                        mOnRecordStateChangedListener.onRecordStart();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtils.e("action_move");
                if (!mHasCancel) {
                    if (mRecordMode == RecordMode.LONG_CLICK) {
                        //初始化对话框和录音
                        if (isInit) {
                            startBeginAnimation();
                            initDialogAndStartRecord();
                            isInit = false;
                        }
                        ScrollDirection mOldDirection = mScrollDirection;
                        float oldY = getY();
                        setX(mInitX + event.getRawX() - mDownRawX);
                        setY(mInitY + event.getRawY() - mDownRawY);
                        float newY = getY();

                        if (newY <= oldY) {
                            mScrollDirection = ScrollDirection.UP;
                        } else {
                            mScrollDirection = ScrollDirection.DOWN;
                        }

                        if (mOldDirection != mScrollDirection) {
                            mInfectionPoint = oldY;
                        }
                        float zoomPercentage = (mInfectionPoint - getY()) / mInitY;
                        if (mOnRecordStateChangedListener != null)
                            mOnRecordStateChangedListener.onZoom(zoomPercentage);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                LogUtils.e("action_up");
                isInit = false;
                if (!mHasCancel) {
                    if (mRecordMode == RecordMode.LONG_CLICK) {
                        LogUtils.e("radish" + "onTouchEvent: 长按录制");
//                        if (mOnRecordStateChangedListener != null)
//                            mOnRecordStateChangedListener.onRecordStop();
                        resetLongClick();
                        finishRecord();
                    } else if (mRecordMode == RecordMode.ORIGIN && inBeginRange(event)) {
                        LogUtils.e("radish" + "onTouchEvent: 点击播放");
                        isPlaying = true;
                        if (mOnRecordStateChangedListener != null)
                            mOnRecordStateChangedListener.onRecordPlayStart();

                        mHandler.removeCallbacks(mClickRunnable);
                        mRecordMode = RecordMode.SINGLE_CLICK;
                    } else if (mRecordMode == RecordMode.SINGLE_CLICK && inEndRange(event)) {
                        LogUtils.e("radish" + "onTouchEvent: 点击停止");
                        if (mOnRecordStateChangedListener != null)
                            mOnRecordStateChangedListener.onRecordPlayStop();
                        resetSingleClick();
                    }
                } else {
                    mHasCancel = false;
                }
                break;
            case MotionEvent.ACTION_CANCEL:// 当手指移动到view外面，会cancel
                LogUtils.e("action_cancel");
                //取消录音
                cancelRecord();
                break;
            default:
                break;
        }
    }

    private boolean inBeginRange(MotionEvent event) {
        int centerX = getMeasuredWidth() / 2;
        int centerY = getMeasuredHeight() / 2;
        int minX = (int) (centerX - mMinCircleRadius);
        int maxX = (int) (centerX + mMinCircleRadius);
        int minY = (int) (centerY - mMinCircleRadius);
        int maxY = (int) (centerY + mMinCircleRadius);
        boolean isXInRange = event.getX() >= minX && event.getX() <= maxX;
        boolean isYInRange = event.getY() >= minY && event.getY() <= maxY;
        return isXInRange && isYInRange;
    }

    private boolean inEndRange(MotionEvent event) {
        int minX = 0;
        int maxX = getMeasuredWidth();
        int minY = 0;
        int maxY = getMeasuredHeight();
        boolean isXInRange = event.getX() >= minX && event.getX() <= maxX;
        boolean isYInRange = event.getY() >= minY && event.getY() <= maxY;
        return isXInRange && isYInRange;
    }

    private void resetLongClick() {
        mRecordMode = RecordMode.ORIGIN;
        mBeginAnimatorSet.cancel();
        startEndAnimation();
        setX(mInitX);
        setY(mInitY);
    }

    private void resetSingleClick() {
        mRecordMode = RecordMode.ORIGIN;
        mBeginAnimatorSet.cancel();
//        startEndAnimation();
    }

    public void reset() {
        if (mRecordMode == RecordMode.LONG_CLICK) {
            resetLongClick();
        } else if (mRecordMode == RecordMode.SINGLE_CLICK) {
            resetSingleClick();
        } else if (mRecordMode == RecordMode.ORIGIN) {
            if (mBeginAnimatorSet.isRunning()) {
                mHasCancel = true;
                mBeginAnimatorSet.cancel();
                startEndAnimation();
                mHandler.removeCallbacks(mClickRunnable);
                mRecordMode = RecordMode.ORIGIN;
            }
        }
    }

    public void startClockRecord() {
        if (mRecordMode == RecordMode.ORIGIN) {
            startBeginAnimation();
            mRecordMode = RecordMode.SINGLE_CLICK;
        }
    }

    private void startBeginAnimation() {
        AnimatorSet startAnimatorSet = new AnimatorSet();
        ObjectAnimator cornerAnimator = ObjectAnimator.ofFloat(this, "corner",
                mMaxCorner, mMinCorner)
                .setDuration(500);
        ObjectAnimator rectSizeAnimator = ObjectAnimator.ofFloat(this, "rectWidth",
                mMaxRectWidth, mMinRectWidth)
                .setDuration(500);
        ObjectAnimator radiusAnimator = ObjectAnimator.ofFloat(this, "circleRadius",
                mMinCircleRadius, mMaxCircleRadius)
                .setDuration(500);
        startAnimatorSet.playTogether(cornerAnimator, rectSizeAnimator, radiusAnimator);

        ObjectAnimator circleWidthAnimator = ObjectAnimator.ofFloat(this, "circleStrokeWidth",
                mMinCircleStrokeWidth, mMaxCircleStrokeWidth, mMinCircleStrokeWidth)
                .setDuration(1500);
        circleWidthAnimator.setRepeatCount(ObjectAnimator.INFINITE);

        mBeginAnimatorSet.playSequentially(startAnimatorSet, circleWidthAnimator);
        mBeginAnimatorSet.start();
    }

    private void startEndAnimation() {
        ObjectAnimator cornerAnimator = ObjectAnimator.ofFloat(this, "corner",
                mMinCorner, mMaxCorner)
                .setDuration(500);
        ObjectAnimator rectSizeAnimator = ObjectAnimator.ofFloat(this, "rectWidth",
                mMinRectWidth, mMaxRectWidth)
                .setDuration(500);
        ObjectAnimator radiusAnimator = ObjectAnimator.ofFloat(this, "circleRadius",
                mMaxCircleRadius, mMinCircleRadius)
                .setDuration(500);
        ObjectAnimator circleWidthAnimator = ObjectAnimator.ofFloat(this, "circleStrokeWidth",
                mMaxCircleStrokeWidth, mMinCircleStrokeWidth)
                .setDuration(500);

        mEndAnimatorSet.playTogether(cornerAnimator, rectSizeAnimator, radiusAnimator, circleWidthAnimator);
        mEndAnimatorSet.start();
    }

    public void setCorner(float corner) {
        this.corner = corner;
        invalidate();
    }

    public void setCircleRadius(float circleRadius) {
        this.circleRadius = circleRadius;
    }

    public void setCircleStrokeWidth(float circleStrokeWidth) {
        this.circleStrokeWidth = circleStrokeWidth;
        invalidate();
    }

    public void setRectWidth(float rectWidth) {
        this.rectWidth = rectWidth;
    }

    class ClickRunnable implements Runnable {

        @Override
        public void run() {
            if (!mHasCancel) {
                mRecordMode = RecordMode.LONG_CLICK;
                mInitX = getX();
                mInitY = getY();
                mInfectionPoint = mInitY;
                mScrollDirection = ScrollDirection.UP;
            }
        }
    }

    public void setOnRecordStateChangedListener(OnRecordStateChangedListener listener) {
        this.mOnRecordStateChangedListener = listener;
    }

    private static final int MIN_INTERVAL_TIME = 1000;// 1s
    private long startTime;

    /**
     * 初始化对话框和录音
     */
    private void initDialogAndStartRecord() {

        //记录开始时间
        startTime = System.currentTimeMillis();
        //创建一个对话框
//        recordIndicator = new Dialog(getContext(), R.style.like_toast_dialog_style);
//        //创建一个图片
//        view = new ImageView(getContext());
//        view.setImageResource(R.drawable.mic_2);
        //设置对话框显示
//        recordIndicator.setContentView(view, new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //设置消失的监听
//        recordIndicator.setOnDismissListener(onDismiss);
        //拿到屏幕的属性
//        WindowManager.LayoutParams lp = recordIndicator.getWindow().getAttributes();
        //设置布局
//        lp.gravity = Gravity.CENTER;

        //开始录音
        startRecording();
        //展示出来
//        recordIndicator.show();
    }

    /**
     * 完成录音
     */
    private void finishRecord() {
        //停止录音
        stopRecording();
        //对话框消失
//        recordIndicator.dismiss();
        //计算按下多久了
        long intervalTime = System.currentTimeMillis() - startTime;
        if (intervalTime < MIN_INTERVAL_TIME) {
            Toast.makeText(getContext(), "时间太短！", Toast.LENGTH_SHORT).show();
            //时间太短就删除路径文件
            File file = new File(fileName);
            file.delete();
            return;
        }
        //回调，返回文件的路径
        if (mOnRecordStateChangedListener != null)
            mOnRecordStateChangedListener.onRecordStop();
    }

    /**
     * 取消录音
     */
    private void cancelRecord() {

        resetLongClick();
        finishRecord();
        stopRecording();
//        recordIndicator.dismiss();

        Toast.makeText(getContext(), "取消录音！", Toast.LENGTH_SHORT).show();
        File file = new File(fileName);
        file.delete();
    }

    /**
     * 开始录音
     */
    private void startRecording() {
        //获取媒体录音者
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);//设置音频源
        recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);//输出格式
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);//音频编码方式

        String time = setTime();
        fileName = mFileName + "/" + time + ".amr";
        recorder.setOutputFile(fileName);//输出文件

        try {
            recorder.prepare();//准备
        } catch (IOException e) {
            LogUtils.e("radish" + "prepare: 异常：" + e.getMessage());
            isInit = false;
            if (!mHasCancel) {
                if (mRecordMode == RecordMode.LONG_CLICK) {
                    LogUtils.e("radish" + "onTouchEvent: 长按录制");
//                        if (mOnRecordStateChangedListener != null)
//                            mOnRecordStateChangedListener.onRecordStop();
                    resetLongClick();
                    finishRecord();
                } else {
                    LogUtils.e("radish" + "onTouchEvent: 点击停止");
                    if (mOnRecordStateChangedListener != null)
                        mOnRecordStateChangedListener.onRecordPlayStop();
//                        mOnRecordStateChangedListener.onRecordStop();
                    resetSingleClick();
                }
            } else {
                mHasCancel = false;
            }
            e.printStackTrace();
        }

        try {
            if (recorder != null) {
                recorder.start();//开始
                //获取分贝的线程
                thread = new ObtainDecibelThread();
                thread.start();
            }
        } catch (IllegalStateException e) {
            LogUtils.e("radish" + "startRecording: 异常：" + e.getMessage());
            isInit = false;
            if (!mHasCancel) {
                if (mRecordMode == RecordMode.LONG_CLICK) {
                    LogUtils.e("radish" + "onTouchEvent: 长按录制");
//                        if (mOnRecordStateChangedListener != null)
//                            mOnRecordStateChangedListener.onRecordStop();
                    resetLongClick();
                    finishRecord();
                } else {
                    LogUtils.e("radish" + "onTouchEvent: 点击停止");
                    if (mOnRecordStateChangedListener != null)
                        mOnRecordStateChangedListener.onRecordPlayStop();
//                        mOnRecordStateChangedListener.onRecordStop();
                    resetSingleClick();
                }
            } else {
                mHasCancel = false;
            }
            e.printStackTrace();
        }

    }

    /**
     * 停止录音
     */
    private void stopRecording() {
        try {
            if (thread != null) {
                thread.exit();//线程退出
                thread = null;
            }
            if (recorder != null) {
                recorder.stop();//停止
                recorder.release();//释放
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        recorder = null;//释放资源
    }

    /**
     * 设置当前时间
     *
     * @return time
     */
    private String setTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * 获取分贝的线程
     */
    private class ObtainDecibelThread extends Thread {

        private volatile boolean running = true;

        //退出
        public void exit() {
            running = false;
        }

        @Override
        public void run() {
            while (running) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    LogUtils.e("radish" + "running: 异常：" + e.getLocalizedMessage());
                    resetLongClick();
                    e.printStackTrace();
                }
                if (recorder == null || !running) {
                    break;
                }
                //获取最大音频振幅
                int x = recorder.getMaxAmplitude();
                LogUtils.e(">>>>>>>录音<<<<<<" + "" + x);

//                if (x != 0) {
//                    //对数函数，以e为底，10的对数，也就是说e的多少次方等于10
//                    int f = (int) (10 * Math.log(x) / Math.log(10));
//                    if (f < 26)
//                        volumeHandler.sendEmptyMessage(0);
//                    else if (f < 32)
//                        volumeHandler.sendEmptyMessage(1);
//                    else if (f < 38)
//                        volumeHandler.sendEmptyMessage(2);
//                    else
//                        volumeHandler.sendEmptyMessage(3);
//
//                }

            }
        }

    }

    /**
     * 系统Api Set a listener to be invoked when the dialog is dismissed.
     * dialog消失后悔唤醒这个监听
     */
    private DialogInterface.OnDismissListener onDismiss = new DialogInterface.OnDismissListener() {

        @Override
        public void onDismiss(DialogInterface dialog) {
            //停止录音
            stopRecording();
        }
    };

//    static class ShowVolumeHandler extends Handler {
//        @Override
//        public void handleMessage(Message msg) {
//            view.setImageResource(res[msg.what]);
//        }
//    }

    String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/录音文件夹";

    /**
     * 设置保存的文件夹
     *
     * @param path
     */
    public void setSavePath(String path) {
        mFileName = path;
    }

    public interface OnRecordStateChangedListener {

        /**
         * 开始录制
         */
        void onRecordStart();

        /**
         * 结束录制
         */
        void onRecordStop();

        /**
         * 缩放百分比
         *
         * @param percentage 百分比值 0%~100% 对应缩放支持的最小和最大值 默认最小1.0
         */
        void onZoom(float percentage);


        void onRecordPlayStart();

        void onRecordPlayStop();
    }

    private enum RecordMode {
        /**
         * 单击录制模式
         */
        SINGLE_CLICK,
        /**
         * 长按录制模式
         */
        LONG_CLICK,
        /**
         * 初始化
         */
        ORIGIN;

        RecordMode() {

        }
    }

    private enum ScrollDirection {
        /**
         * 滑动方向 上
         */
        UP,
        /**
         * 滑动方向 下
         */
        DOWN;

        ScrollDirection() {

        }
    }
}
