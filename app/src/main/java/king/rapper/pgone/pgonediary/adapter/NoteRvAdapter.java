package king.rapper.pgone.pgonediary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.google.gson.Gson;

import java.util.List;

import king.rapper.pgone.pgonediary.R;
import king.rapper.pgone.pgonediary.dao.DaoController;
import king.rapper.pgone.pgonediary.entity.NoteInfo;
import king.rapper.pgone.pgonediary.entity.NoteRecording;
import king.rapper.pgone.pgonediary.interfaces.OnDiaryListClickListener;
import king.rapper.pgone.pgonediary.util.DateUtil;
import king.rapper.pgone.pgonediary.util.LogUtils;
import king.rapper.pgone.pgonediary.view.MyClickLinearLayout;
import king.rapper.pgone.pgonediary.view.MyRecordTextView;

public class NoteRvAdapter extends RecyclerSwipeAdapter<NoteRvAdapter.SimpleViewHolder> {

    private OnDiaryListClickListener listener;

    private MyRecordTextView preRecordTextView = null;
    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        private final SwipeLayout swipeLayout;
        private final ImageView buttonDelete;
        private final LinearLayout ll_click;
        private final TextView tv_desc;
        private final TextView tv_time;
        private final MyRecordTextView tv_recording;
        private final FrameLayout fl_playing;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            buttonDelete = (ImageView) itemView.findViewById(R.id.delete);
            ll_click = (LinearLayout) itemView.findViewById(R.id.ll_click);
            tv_desc = (TextView) itemView.findViewById(R.id.tv_desc);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_recording = itemView.findViewById(R.id.tv_recording);
            fl_playing = itemView.findViewById(R.id.fl_playing);
        }
    }

    private Context mContext;
    private List<NoteInfo> mDataset;

    public NoteRvAdapter(Context context, List<NoteInfo> objects) {
        this.mContext = context;
        this.mDataset = objects;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_note, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
        final NoteInfo noteInfo = mDataset.get(position);

        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        viewHolder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
            }
        });

        viewHolder.ll_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listener != null) {
                    listener.OnItemSingleClick(position);
                }
            }
        });
        viewHolder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.OnItemDeleteClick(position);
                }
                mItemManger.closeAllItems();
            }
        });
        if (noteInfo != null) {
            if (!TextUtils.isEmpty(noteInfo.getContent())) {
                viewHolder.tv_desc.setText(noteInfo.getContent());
                viewHolder.tv_desc.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tv_desc.setText("");
                viewHolder.tv_desc.setVisibility(View.GONE);
            }
            if (noteInfo.getTime() != null && noteInfo.getTime() != 0) {
                String startTimeStr = DateUtil.getTimeString(noteInfo.getTime());
                if (!TextUtils.isEmpty(startTimeStr)) {
                    viewHolder.tv_time.setVisibility(View.VISIBLE);
                    viewHolder.tv_time.setText(startTimeStr + "");
                } else {
                    viewHolder.tv_time.setText("");
                    viewHolder.tv_time.setVisibility(View.GONE);
                }
            } else {
                viewHolder.tv_time.setText("");
                viewHolder.tv_time.setVisibility(View.GONE);
            }
            List<NoteRecording> recordingList = null;
            try {
                noteInfo.__setDaoSession(DaoController.getInstance().getDaoSession());
                recordingList = noteInfo.getRecordingList();
            } catch (Exception e) {
                LogUtils.e("异常：" + e.getMessage());
                e.printStackTrace();
            }
            NoteRecording noteRecording = null;
            if (recordingList != null && recordingList.size() > 0) {
                noteRecording = recordingList.get(0);
                viewHolder.tv_recording.setType(1);
                viewHolder.tv_recording.setNoteRecording(noteRecording);
                viewHolder.tv_recording.setText("播放");
            } else {
                viewHolder.tv_recording.setVisibility(View.GONE);
                viewHolder.fl_playing.setVisibility(View.GONE);
            }
        }
        viewHolder.tv_recording.setAnimationView(viewHolder.fl_playing);
        viewHolder.tv_recording.setOnRecordStateChangedListener(new MyRecordTextView.OnRecordStateChangedListener() {
            @Override
            public void onRecordStart() {
                LogUtils.e("onRecordStart");
            }

            @Override
            public void onRecordStop(final NoteRecording recording) {
                LogUtils.e("onRecordStop");
            }

            @Override
            public void onZoom(float percentage) {

            }

            @Override
            public void onRecordPlayStart() {
                LogUtils.e("onRecordPlayStart");
                viewHolder.tv_recording.setText("停止");
                if (preRecordTextView != null) {
                    preRecordTextView.stopPlaying();
                    preRecordTextView.setText("播放");
                }
                preRecordTextView = viewHolder.tv_recording;
            }

            @Override
            public void onRecordPlayStop() {
                LogUtils.e("onRecordPlayStop");
                viewHolder.tv_recording.setText("播放");
                preRecordTextView = null;
            }
        });

        mItemManger.bind(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return mDataset == null ? 0 : mDataset.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public void setList(List<NoteInfo> list) {
        this.mDataset = list;
        notifyDatasetChanged();
    }

    public void setOnItemClickListener(OnDiaryListClickListener listener) {
        this.listener = listener;
    }
}
