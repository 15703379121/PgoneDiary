package king.rapper.pgone.pgonediary.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import king.rapper.pgone.pgonediary.R;
import king.rapper.pgone.pgonediary.UI.DiaryActivity;
import king.rapper.pgone.pgonediary.entity.Recording;
import king.rapper.pgone.pgonediary.service.RecordingService;
import king.rapper.pgone.pgonediary.util.LogUtils;
import king.rapper.pgone.pgonediary.util.ToastUtil;
import king.rapper.pgone.pgonediary.util.constant.Config;

/**
 * 开始录音的 DialogFragment
 * <p>
 * Created by developerHaoz on 2017/8/12.
 */

public class RecordAudioDialogFragment extends DialogFragment {

    private boolean mStartRecording = true;

    long timeWhenPaused = 0;

    private FloatingActionButton mFabRecord;
    private Chronometer mChronometerTime;
    private ImageView mIvClose;

    private OnAudioCancelListener mListener;
    private Intent recordingServiceIntent;
    private RecordingService binderService;

    public static RecordAudioDialogFragment newInstance() {
        RecordAudioDialogFragment dialogFragment = new RecordAudioDialogFragment();
        Bundle bundle = new Bundle();
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_record_audio, null);
        initView(view);

        mFabRecord.setColorNormal(getResources().getColor(R.color.AppPrimary));
        mFabRecord.setColorPressed(getResources().getColor(R.color.AppPrimaryDark));

        mFabRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    List<String> permissionList = new ArrayList<>();
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    } else if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                        permissionList.add(Manifest.permission.RECORD_AUDIO);
                    } else { //所有的权限都已经授权过了
                        onRecord(mStartRecording);
                        mStartRecording = !mStartRecording;
                    }
                } else {
                    onRecord(mStartRecording);
                    mStartRecording = !mStartRecording;
                }

            }
        });

        mIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCancel();
                if (recordingServiceIntent != null && binderService != null) {
                    binderService.cancelRecording();
                    getActivity().stopService(recordingServiceIntent);
                }
            }
        });

        builder.setCancelable(false);
        builder.setView(view);
        return builder.create();
    }


    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 建立连接
            // 获取服务的操作对象
            RecordingService.MyBinder binder = (RecordingService.MyBinder) service;
            // 获取到的Service即MyService
            binderService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 连接断开
        }
    };

    private void initView(View view) {
        mChronometerTime = (Chronometer) view.findViewById(R.id.record_audio_chronometer_time);
        mFabRecord = (FloatingActionButton) view.findViewById(R.id.record_audio_fab_record);
        mIvClose = (ImageView) view.findViewById(R.id.record_audio_iv_close);
    }

    private void onRecord(boolean start) {

        recordingServiceIntent = new Intent(getActivity(), RecordingService.class);
        if (start) {
            mFabRecord.setImageResource(R.mipmap.ic_media_stop);
            Toast.makeText(getActivity(), "开始录音...", Toast.LENGTH_SHORT).show();
            File folder = new File(Config.LocalPath);
            if (!folder.exists()) {
                folder.mkdir();
            }

            mChronometerTime.setBase(SystemClock.elapsedRealtime());
            mChronometerTime.start();

            getActivity().startService(recordingServiceIntent);
            getActivity().bindService(recordingServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE);


        } else {
            mFabRecord.setImageResource(R.mipmap.ic_mic_white_36dp);
            mChronometerTime.stop();
            timeWhenPaused = 0;
            Toast.makeText(getActivity(), "录音结束...", Toast.LENGTH_SHORT).show();
            LogUtils.e("recordingServiceIntent：" + recordingServiceIntent + "  binderService:" + binderService);
            if (recordingServiceIntent != null && binderService != null) {
                Recording recording = binderService.stopRecording();
                LogUtils.e("recording:" + recording);
                if (recording != null)
                    ((DiaryActivity) getActivity()).addRecording(recording);
                getActivity().stopService(recordingServiceIntent);
            }
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            mListener.onCancel();
        }
    }

    public void setOnCancelListener(OnAudioCancelListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            onRecord(mStartRecording);
            mStartRecording = !mStartRecording;
        } else {
            ToastUtil.showLong(getContext(), "没有开启录音权限");
        }
    }

    public interface OnAudioCancelListener {
        void onCancel();
    }
}

