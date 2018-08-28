package king.rapper.pgone.pgonediary.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import king.rapper.pgone.pgonediary.MyApp;
import king.rapper.pgone.pgonediary.entity.NoteRecording;
import king.rapper.pgone.pgonediary.util.LogUtils;

/**
 * @ Create_time: 2018/7/31 on 10:55.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
@SuppressLint("AppCompatCustomView")
public class MyRecordTextView extends TextView {
    private Integer type = 0;
    private Boolean isPlaying = false;
    private OnRecordStateChangedListener mOnRecordStateChangedListener;
    String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/录音文件夹";
    private Long startTime;
    private MediaRecorder recorder;
    private String fileName;
    private ObtainDecibelThread thread;
    private ViewGroup animationView;
    private MediaPlayer mMediaPlayer;
    private NoteRecording noteRecording;
    private Vibrator vibrator;

    public MyRecordTextView(Context context) {
        super(context);
    }

    public MyRecordTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecordTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("NewApi")
    public MyRecordTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setOnRecordStateChangedListener(OnRecordStateChangedListener listener) {
        this.mOnRecordStateChangedListener = listener;
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LogUtils.e("action_down");
                if (type == 0) {
                    startRecording();
                }
                break;
            case MotionEvent.ACTION_UP:
                LogUtils.e("action_up");
                if (type == 0) {
                    Log.e("radish", "onTouchEvent: 长按录制");
                    finishRecord();
                } else if (type == 1 && !isPlaying) {
                    startPlaying();
                    Log.e("radish", "onTouchEvent: 点击播放");
                } else if (type == 1 && isPlaying) {
                    stopPlaying();
                }
                break;
            case MotionEvent.ACTION_CANCEL:// 当手指移动到view外面，会cancel
                LogUtils.e("action_cancel");
                //取消录音
                if (type == 0) {
                    cancelRecord();
                }
                break;
            default:
                break;
        }
        return true;
    }

    public void setNoteRecording(NoteRecording noteRecording) {
        this.noteRecording = noteRecording;
    }

    private void startPlaying() {
        isPlaying = true;
        if (mOnRecordStateChangedListener != null)
            mOnRecordStateChangedListener.onRecordPlayStart();
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(noteRecording.getFilePath());
            mMediaPlayer.prepare();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                }
            });
        } catch (IOException e) {
            LogUtils.e("prepare() failed:" + e.getLocalizedMessage());
        }

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlaying();
            }
        });
    }

    public void stopPlaying() {
        isPlaying = false;
        Log.e("radish", "onTouchEvent: 点击停止");
        if (mOnRecordStateChangedListener != null)
            mOnRecordStateChangedListener.onRecordPlayStop();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void endAnimation() {
        isRecoding = false;
        clearAnimation();
    }

    private Boolean isRecoding = false;
    private AnimatorSet mEndAnimatorSet = new AnimatorSet();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    LogUtils.e("isRecoding:" + isRecoding);
                    if (isRecoding) {
                        ObjectAnimator anim = ObjectAnimator.ofFloat(animationView, "scaleX", 1f, 5f, 1f);
                        ObjectAnimator anim1 = ObjectAnimator.ofFloat(animationView, "scaleY", 1f, 5f, 1f);
                        ObjectAnimator anim2 = ObjectAnimator.ofFloat(animationView, "alpha", 1f, 0.1f, 1f);

                        // 正式开始启动执行动画
                        mEndAnimatorSet.setDuration(1200);
                        mEndAnimatorSet.playTogether(anim, anim1, anim2);
                        mEndAnimatorSet.start();
                        mHandler.sendEmptyMessageDelayed(0, 1200);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void startAnimation() {
        isRecoding = true;
        mHandler.sendEmptyMessage(0);
    }

    public void setAnimationView(ViewGroup view) {
        this.animationView = view;
    }

    /**
     * 取消录音
     */
    private void cancelRecord() {//停止录音
        endAnimation();
        stopRecording();
        Toast.makeText(getContext(), "取消录音！", Toast.LENGTH_SHORT).show();
        File file = new File(fileName);
        file.delete();
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

    private static final int MIN_INTERVAL_TIME = 1000;// 1s

    private void setVibrator() {
        if (vibrator == null)
            vibrator = (Vibrator) MyApp.getInstance().getSystemService(MyApp.getInstance().VIBRATOR_SERVICE);
        vibrator.vibrate(60);
    }

    /**
     * 完成录音
     */
    private void finishRecord() {
        setVibrator();
        endAnimation();
        //停止录音
        stopRecording();
        //对话框消失
//        recordIndicator.dismiss();
        //计算按下多久了
        Long endTime = System.currentTimeMillis();
        Long intervalTime = endTime - startTime;
        if (intervalTime < MIN_INTERVAL_TIME) {
            Toast.makeText(getContext(), "时间太短！", Toast.LENGTH_SHORT).show();
            //时间太短就删除路径文件
            File file = new File(fileName);
            file.delete();
            return;
        } else {
            //回调，返回文件的路径
            if (mOnRecordStateChangedListener != null) {
                noteRecording = new NoteRecording();
                if (!TextUtils.isEmpty(fileName))
                    noteRecording.setFilePath(fileName);
                if (intervalTime != null && intervalTime != 0)
                    noteRecording.setLength(intervalTime);
                if (endTime != null && endTime != 0)
                    noteRecording.setTime(endTime);
                if (startTime != null && startTime != 0)
                    noteRecording.setName(startTime + "");
                mOnRecordStateChangedListener.onRecordStop(noteRecording);
            }
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
     * 开始录音
     */
    private void startRecording() {
        setVibrator();
        startAnimation();
        if (mOnRecordStateChangedListener != null)
            mOnRecordStateChangedListener.onRecordStart();
        //记录开始时间
        startTime = System.currentTimeMillis();
        //获取媒体录音者
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);//设置音频源
        recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);//输出格式
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);//音频编码方式

        fileName = mFileName + "/" + startTime + ".amr";
        recorder.setOutputFile(fileName);//输出文件

        try {
            recorder.prepare();//准备
        } catch (IOException e) {
            Log.e("radish", "prepare: 异常：" + e.getMessage());
            if (type == 0) {
                Log.e("radish", "onTouchEvent: 长按录制");
//                        if (mOnRecordStateChangedListener != null)
//                            mOnRecordStateChangedListener.onRecordStop();
                finishRecord();
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
            Log.e("radish", "startRecording: 异常：" + e.getMessage());
            if (type == 0) {
                Log.e("radish", "onTouchEvent: 长按录制");
//                        if (mOnRecordStateChangedListener != null)
//                            mOnRecordStateChangedListener.onRecordStop();
                finishRecord();
            }
            e.printStackTrace();
        }

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
                    Log.e("radish", "running: 异常：" + e.getLocalizedMessage());
//                    resetLongClick();
                    e.printStackTrace();
                }
                if (recorder == null || !running) {
                    break;
                }
                //获取最大音频振幅
                int x = recorder.getMaxAmplitude();
                Log.e(">>>>>>>录音<<<<<<", "" + x);

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

    public interface OnRecordStateChangedListener {

        /**
         * 开始录制
         */
        void onRecordStart();

        /**
         * 结束录制
         */
        void onRecordStop(NoteRecording noteRecording);

        /**
         * 缩放百分比
         *
         * @param percentage 百分比值 0%~100% 对应缩放支持的最小和最大值 默认最小1.0
         */
        void onZoom(float percentage);


        void onRecordPlayStart();

        void onRecordPlayStop();
    }
}
