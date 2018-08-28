package king.rapper.pgone.pgonediary.adapter;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import king.rapper.pgone.pgonediary.R;
import king.rapper.pgone.pgonediary.entity.Recording;
import king.rapper.pgone.pgonediary.interfaces.OnVoiceRvItemClickListener;
import king.rapper.pgone.pgonediary.util.LogUtils;

/**
 * @ Create_time: 2018/7/25 on 14:00.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
public class VoiceRvAdapter extends RecyclerView.Adapter<VoiceRvAdapter.MyViewHolder> {

    private Context context;
    private List<Recording> list;

    private MediaPlayer mMediaPlayer = null;
    private Handler mHandler = new Handler();
    private MyViewHolder preViewHolder;
    private Runnable preRunnable;
    private OnVoiceRvItemClickListener listener;
    //    private boolean isPlaying;

    public VoiceRvAdapter(Context context, List<Recording> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_media_playback, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Recording item = list.get(position);
        if (item != null) {
            Long itemDuration = item.getLength();
            Long mFileLength = itemDuration;
            Long minutes = TimeUnit.MILLISECONDS.toMinutes(itemDuration);
            Long seconds = TimeUnit.MILLISECONDS.toSeconds(itemDuration)
                    - TimeUnit.MINUTES.toSeconds(minutes);
            ColorFilter filter = new LightingColorFilter
                    (context.getResources().getColor(R.color.colorPrimary), context.getResources().getColor(R.color.colorPrimary));
            holder.mSeekBar.getProgressDrawable().setColorFilter(filter);
            holder.mSeekBar.getThumb().setColorFilter(filter);
            holder.mFileLengthTextView.setText(String.valueOf(mFileLength));
            final Runnable mRunnable = new Runnable() {
                @Override
                public void run() {
                    if (mMediaPlayer != null) {

                        int mCurrentPosition = mMediaPlayer.getCurrentPosition();
                        holder.mSeekBar.setProgress(mCurrentPosition);

                        long minutes = TimeUnit.MILLISECONDS.toMinutes(mCurrentPosition);
                        long seconds = TimeUnit.MILLISECONDS.toSeconds(mCurrentPosition)
                                - TimeUnit.MINUTES.toSeconds(minutes);
                        holder.mCurrentProgressTextView.setText(String.format("%02d:%02d", minutes, seconds));

                        mHandler.postDelayed(this, 1000);
                    }
                }
            };

            holder.mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (mMediaPlayer != null && fromUser) {
                        mMediaPlayer.seekTo(progress);
                        mHandler.removeCallbacks(mRunnable);

                        long minutes = TimeUnit.MILLISECONDS.toMinutes(mMediaPlayer.getCurrentPosition());
                        long seconds = TimeUnit.MILLISECONDS.toSeconds(mMediaPlayer.getCurrentPosition())
                                - TimeUnit.MINUTES.toSeconds(minutes);
                        holder.mCurrentProgressTextView.setText(String.format("%02d:%02d", minutes, seconds));

                        mHandler.postDelayed(mRunnable, 1000);

                    } else if (mMediaPlayer == null && fromUser) {
                        prepareMediaPlayerFromPoint(progress, item, holder.mSeekBar, holder.mPlayButton, mRunnable, holder.mCurrentProgressTextView, holder.mFileLengthTextView);
                        mHandler.postDelayed(mRunnable, 1000);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    if (mMediaPlayer != null) {
                        // remove message Handler from updating progress bar
                        mHandler.removeCallbacks(mRunnable);
                    }
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    if (mMediaPlayer != null) {
                        mHandler.removeCallbacks(mRunnable);
                        mMediaPlayer.seekTo(seekBar.getProgress());

                        long minutes = TimeUnit.MILLISECONDS.toMinutes(mMediaPlayer.getCurrentPosition());
                        long seconds = TimeUnit.MILLISECONDS.toSeconds(mMediaPlayer.getCurrentPosition())
                                - TimeUnit.MINUTES.toSeconds(minutes);
                        holder.mCurrentProgressTextView.setText(String.format("%02d:%02d", minutes, seconds));
                        mHandler.postDelayed(mRunnable, 1000);
                    }
                }
            });

            holder.mPlayButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (preViewHolder != null && preViewHolder != holder) {
                        LogUtils.e("关闭上一个viewholder");
                        stopPlaying(preViewHolder.mPlayButton, preRunnable, preViewHolder.mSeekBar, preViewHolder.mCurrentProgressTextView, preViewHolder.mFileLengthTextView);
                    }
                    if (holder.mSeekBar.getProgress() == 0)
                        onPlay(false, holder.mPlayButton, mRunnable, holder.mSeekBar, item, holder.mCurrentProgressTextView, holder.mFileLengthTextView);
                    else
                        onPlay(true, holder.mPlayButton, mRunnable, holder.mSeekBar, item, holder.mCurrentProgressTextView, holder.mFileLengthTextView);
//                    isPlaying = !isPlaying;
                    preViewHolder = holder;
                    preRunnable = mRunnable;
                }
            });
            holder.mFileLengthTextView.setText(String.format("%02d:%02d", minutes, seconds));
        }

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.e("点击删除按钮");
                //删除
                if (listener != null) {
                    listener.onDeleteClick(position);
                }
            }
        });

    }

    public void setItemClickListener(OnVoiceRvItemClickListener listener) {
        this.listener = listener;
    }

    // Play start/stop
    private void onPlay(boolean isPlaying, ImageView mPlayButton, Runnable mRunnable, SeekBar mSeekBar, Recording item, TextView mCurrentProgressTextView, TextView mFileLengthTextView) {
        if (!isPlaying) {
            startPlaying(mPlayButton, mSeekBar, item, mRunnable, mCurrentProgressTextView, mFileLengthTextView);
        } else {
            stopPlaying(mPlayButton, mRunnable, mSeekBar, mCurrentProgressTextView, mFileLengthTextView);
        }
    }

    private void startPlaying(final ImageView mPlayButton, final SeekBar mSeekBar, Recording item, final Runnable mRunnable, final TextView mCurrentProgressTextView, final TextView mFileLengthTextView) {
        mPlayButton.setImageResource(R.mipmap.ic_media_pause);
        mMediaPlayer = new MediaPlayer();

        try {
            mMediaPlayer.setDataSource(item.getFilePath());
            mMediaPlayer.prepare();
            mSeekBar.setMax(mMediaPlayer.getDuration());

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
                stopPlaying(mPlayButton, mRunnable, mSeekBar, mCurrentProgressTextView, mFileLengthTextView);
            }
        });

        mHandler.postDelayed(mRunnable, 1000);
    }

    private void prepareMediaPlayerFromPoint(int progress, Recording item, final SeekBar mSeekBar, final ImageView mPlayButton, final Runnable mRunnable, final TextView mCurrentProgressTextView, final TextView mFileLengthTextView) {
        //set mediaPlayer to start from middle of the audio file

        mMediaPlayer = new MediaPlayer();

        try {
            mMediaPlayer.setDataSource(item.getFilePath());
            mMediaPlayer.prepare();
            mSeekBar.setMax(mMediaPlayer.getDuration());
            mMediaPlayer.seekTo(progress);

            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlaying(mPlayButton, mRunnable, mSeekBar, mCurrentProgressTextView, mFileLengthTextView);
                }
            });

        } catch (IOException e) {
            LogUtils.e("prepare() failed:" + e.getLocalizedMessage());
        }
    }

    private void pausePlaying(ImageView mPlayButton, Runnable mRunnable) {
        mPlayButton.setImageResource(R.mipmap.ic_media_play);
        mHandler.removeCallbacks(mRunnable);
        mMediaPlayer.pause();
    }

    private void resumePlaying(ImageView mPlayButton, Runnable mRunnable) {
        mPlayButton.setImageResource(R.mipmap.ic_media_pause);
        mHandler.removeCallbacks(mRunnable);
        mMediaPlayer.start();
        mHandler.postDelayed(mRunnable, 1000);
    }

    private void stopPlaying(ImageView mPlayButton, Runnable mRunnable, SeekBar mSeekBar, TextView mCurrentProgressTextView, TextView mFileLengthTextView) {
        mPlayButton.setImageResource(R.mipmap.ic_media_play);
        if (mRunnable != null)
            mHandler.removeCallbacks(mRunnable);

        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        mSeekBar.setProgress(0);
//        isPlaying = !isPlaying;

        mCurrentProgressTextView.setText("00:00");
        mSeekBar.setProgress(0);

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private SeekBar mSeekBar = null;
        private ImageView mPlayButton = null;
        private TextView mCurrentProgressTextView = null;
        private TextView mFileLengthTextView = null;
        private final ImageView iv_delete;

        public MyViewHolder(View itemView) {
            super(itemView);

            mFileLengthTextView = (TextView) itemView.findViewById(R.id.file_length_text_view);
            mCurrentProgressTextView = (TextView) itemView.findViewById(R.id.current_progress_text_view);

            mSeekBar = (SeekBar) itemView.findViewById(R.id.seekbar);
            mPlayButton = (ImageView) itemView.findViewById(R.id.fab_play);
            iv_delete = (ImageView) itemView.findViewById(R.id.iv_delete);
        }
    }

    public void setList(List<Recording> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
