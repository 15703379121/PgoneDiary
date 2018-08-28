package king.rapper.pgone.pgonediary.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.TimerTask;

import king.rapper.pgone.pgonediary.entity.Recording;
import king.rapper.pgone.pgonediary.util.LogUtils;
import king.rapper.pgone.pgonediary.util.constant.Config;

/**
 * 录音的 Service
 * <p>
 * Created by developerHaoz on 2017/8/12.
 */

public class RecordingService extends Service {

    private static final String LOG_TAG = "RecordingService";

    private String mFileName = null;
    private String mFilePath = null;

    private MediaRecorder mRecorder = null;

    private long mStartingTimeMillis = 0;
    private long mElapsedMillis = 0;
    private TimerTask mIncrementTimerTask = null;
    private long currentTime;

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    public class MyBinder extends Binder {
        public RecordingService getService() {
            return RecordingService.this;
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startRecording();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
//        if (mRecorder != null) {
//            stopRecording();
//        }
        super.onDestroy();
    }

    public void startRecording() {
        setFileNameAndPath();

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setOutputFile(mFilePath);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mRecorder.setAudioChannels(1);
        mRecorder.setAudioSamplingRate(44100);
        mRecorder.setAudioEncodingBitRate(192000);

        try {
            mRecorder.prepare();
            mRecorder.start();
            mStartingTimeMillis = System.currentTimeMillis();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    public void setFileNameAndPath() {
        int count = 0;
        File f;

        do {
            count++;
            currentTime = System.currentTimeMillis();
            mFileName = "pgRecording"
                    + "_" + currentTime + ".mp4";
            mFilePath = Config.LocalPath;
            mFilePath += "/" + mFileName;
            f = new File(mFilePath);
        } while (f.exists() && !f.isDirectory());
    }

    public Recording stopRecording() {
        if (mRecorder != null) {
            mRecorder.stop();
            mElapsedMillis = (System.currentTimeMillis() - mStartingTimeMillis);
            mRecorder.release();
            LogUtils.e("正常不?");
            Recording recording = new Recording();
            recording.setFilePath(mFilePath);
//            recording.setForeignType("diary");
            recording.setLength(mElapsedMillis);
            recording.setName(mFileName);
            recording.setTime(currentTime);
            if (mIncrementTimerTask != null) {
                mIncrementTimerTask.cancel();
                mIncrementTimerTask = null;
            }
            mRecorder = null;
            LogUtils.e("recording:" + recording);
            return recording;
        }
        return null;
    }

    public void cancelRecording() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder = null;
        }
    }

}
