package king.rapper.pgone.pgonediary.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import king.rapper.pgone.pgonediary.R;
import king.rapper.pgone.pgonediary.UI.NoteListActivity;
import king.rapper.pgone.pgonediary.dao.DaoController;
import king.rapper.pgone.pgonediary.entity.NoteInfo;
import king.rapper.pgone.pgonediary.entity.NoteRecording;
import king.rapper.pgone.pgonediary.interfaces.NoteListItemClickListener;
import king.rapper.pgone.pgonediary.util.CommonUtil;
import king.rapper.pgone.pgonediary.util.DateUtil;
import king.rapper.pgone.pgonediary.util.LogUtils;
import king.rapper.pgone.pgonediary.util.SoftKeyboardUtil;
import king.rapper.pgone.pgonediary.util.ToastUtil;
import king.rapper.pgone.pgonediary.view.MyRecordTextView;

/**
 * @ Create_time: 2018/7/27 on 14:19.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
public class NoteListRvAdapter extends RecyclerView.Adapter<NoteListRvAdapter.MyViewHolder> {
    private NoteListActivity context;
    private List<NoteInfo> list;
    private NoteListItemClickListener listener;
    private MyRecordTextView preRecordTextView = null;
    private Boolean isSelectable = false;
    private String preDate = "";
    private Integer selectCount = 0;
    private List<NoteInfo> selectList = new ArrayList<>();

    public NoteListRvAdapter(NoteListActivity context, List<NoteInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_rv_list_note, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final NoteInfo noteInfo = list.get(position);
        if (!TextUtils.isEmpty(noteInfo.getContent())) {
            holder.et_content.setText(noteInfo.getContent());
            holder.et_content.setSelection(noteInfo.getContent().length());
        } else {
            holder.et_content.setText("");
        }

        if (isSelectable) {
            holder.iv_select.setVisibility(View.VISIBLE);
        } else {
            holder.iv_select.setVisibility(View.GONE);
        }

        Long time = noteInfo.getTime();
        if (time != null && time != 0) {
            String mdTimeString = DateUtil.getMDTimeString(time);
            String hmTimeString = DateUtil.getHMTimeString(time);

            //设置日期
            if (!TextUtils.isEmpty(preDate) && !TextUtils.isEmpty(mdTimeString) && preDate.equals(mdTimeString)) {
                //与前一个日期相同
                holder.ll_date.setVisibility(View.GONE);
                String mdTimeStringToday = DateUtil.getMDTimeString();
                if (!TextUtils.isEmpty(mdTimeStringToday) && mdTimeStringToday.equals(mdTimeString)) {
                    holder.tv_date.setText("今日");
                } else {
                    holder.tv_date.setText(mdTimeString + "");
                }
            } else {
                holder.ll_date.setVisibility(View.VISIBLE);
                String mdTimeStringToday = DateUtil.getMDTimeString();
                if (!TextUtils.isEmpty(mdTimeStringToday) && mdTimeStringToday.equals(mdTimeString)) {
                    holder.tv_date.setText("今日");
                } else {
                    holder.tv_date.setText(mdTimeString + "");
                }
            }
            preDate = mdTimeString;


            //设置时间
            if (!TextUtils.isEmpty(hmTimeString)) {
                holder.tv_time.setText(hmTimeString + "");
            } else {
                holder.tv_time.setText("");
            }
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
            holder.tv_recording.setType(1);
            holder.tv_recording.setNoteRecording(noteRecording);
            holder.tv_recording.setText("播放");
        } else {
            holder.tv_recording.setType(0);
            holder.tv_recording.setNoteRecording(null);
            holder.tv_recording.setText("长按录音");
        }

        //选中不选中（编辑）
        Boolean select = noteInfo.getSelect();
        if (select) {
            selectCount++;
            selectList.add(noteInfo);
        }
        holder.iv_select.setSelected(select);
        holder.iv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectList == null)
                    selectList = new ArrayList<>();
                Boolean select = !noteInfo.getSelect();
                if (select) {
                    selectCount++;
                    selectList.add(noteInfo);
                } else {
                    selectCount--;
                    selectList.remove(noteInfo);
                }
                noteInfo.setSelect(select);
                holder.iv_select.setSelected(select);
                if (selectCount >= list.size()) {
                    //全选
                    context.setCbSelect(true);
                } else {
                    //非全选
                    context.setCbSelect(false);
                }
            }
        });

        //保存
        holder.tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        noteInfo.setTime(System.currentTimeMillis());
                        if (!CommonUtil.checkEmpty(holder.et_content)) {
                            noteInfo.setContent(holder.et_content.getText().toString().trim());
                        } else {
                            noteInfo.setContent("");
                        }
                        DaoController.getInstance().getNoteInfoDao().update(noteInfo);
                        list = DaoController.getInstance().getNoteInfoAll();
                        LogUtils.e("list:" + new Gson().toJson(list));
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShort("保存成功");
                                setList(list);
                                context.sendNoteBroadcast();
                            }
                        });
                    }
                }.start();
                SoftKeyboardUtil.closeInputMethod(context);
            }
        });

        //录音
        holder.tv_recording.setAnimationView(holder.fl_playing);
        holder.tv_recording.setOnRecordStateChangedListener(new MyRecordTextView.OnRecordStateChangedListener() {
            @Override
            public void onRecordStart() {
                LogUtils.e("onRecordStart");
            }

            @Override
            public void onRecordStop(final NoteRecording recording) {
                LogUtils.e("onRecordStop");
                new Thread() {
                    @Override
                    public void run() {
                        recording.setNoteJoinRecordingId(noteInfo.getNoteInfoId());
                        DaoController.getInstance().getNoteRecordingDao().insert(recording);
                        LogUtils.e("recording:" + new Gson().toJson(recording));


                        if (!CommonUtil.checkEmpty(holder.et_content)) {
                            noteInfo.setContent(holder.et_content.getText().toString().trim());
                        } else {
                            noteInfo.setContent("");
                        }
                        noteInfo.setTime(System.currentTimeMillis());
                        noteInfo.resetRecordingList();
                        DaoController.getInstance().getNoteInfoDao().update(noteInfo);


                        list = DaoController.getInstance().getNoteInfoAll();
                        LogUtils.e("list:" + new Gson().toJson(list));
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setList(list);
                                context.sendNoteBroadcast();
                            }
                        });
                    }
                }.start();
            }

            @Override
            public void onZoom(float percentage) {

            }

            @Override
            public void onRecordPlayStart() {
                LogUtils.e("onRecordPlayStart");
                holder.tv_recording.setText("停止");
                if (preRecordTextView != null) {
                    preRecordTextView.stopPlaying();
                    preRecordTextView.setText("播放");
                }
                preRecordTextView = holder.tv_recording;
            }

            @Override
            public void onRecordPlayStop() {
                LogUtils.e("onRecordPlayStop");
                holder.tv_recording.setText("播放");
                preRecordTextView = null;
            }
        });
    }

    public void setSelectable(Boolean selectable) {
        isSelectable = selectable;
        preDate = "";
        notifyDataSetChanged();
    }

    public void setSelectAll(Boolean selectAll) {
        if (selectList == null)
            selectList = new ArrayList<>();
        selectList.clear();
        if (selectAll) {
            selectList.addAll(list);
            selectCount = list.size();
        } else {
            selectCount = 0;
        }
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setSelect(selectAll);
        }
        preDate = "";
        notifyDataSetChanged();
    }

    public void deleteSelect() {
        for (int i = 0; i < selectList.size(); i++) {
            NoteInfo noteInfo = selectList.get(i);
            if (noteInfo.getSelect()) {
                noteInfo.setSelect(false);
                noteInfo.setTag(-1);
                DaoController.getInstance().getNoteInfoDao().update(noteInfo);
            }
        }

        list = DaoController.getInstance().getNoteInfoAll();
        setList(list);

        selectCount = 0;
        if (selectList == null)
            selectList = new ArrayList<>();
        selectList.clear();
        context.setCbSelect(false);
        context.sendNoteBroadcast();
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout ll_date;
        private final TextView tv_date;
        private final ImageView iv_select;
        private final EditText et_content;
        private final TextView tv_edit;
        private final TextView tv_time;
        private final MyRecordTextView tv_recording;
        //        private final SeekBar seekbar;
        private final FrameLayout fl_playing;

        public MyViewHolder(View itemView) {
            super(itemView);
            ll_date = itemView.findViewById(R.id.ll_date);
            tv_date = itemView.findViewById(R.id.tv_date);
            iv_select = itemView.findViewById(R.id.iv_select);
            et_content = itemView.findViewById(R.id.et_content);
            tv_edit = itemView.findViewById(R.id.tv_edit);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_recording = itemView.findViewById(R.id.tv_recording);
            fl_playing = itemView.findViewById(R.id.fl_playing);
//            seekbar = itemView.findViewById(R.id.seekbar);
        }
    }

    public void setList(List<NoteInfo> list) {
        LogUtils.e("list.size():" + list.size());
        LogUtils.e("list:" + new Gson().toJson(list));
        this.list = list;
        preDate = "";
        notifyDataSetChanged();
        context.MoveToPosition(0);
        if (selectCount == 0 || selectCount < list.size()) {
            context.setCbSelect(false);
        }
    }

    public void setOnItemClickListener(NoteListItemClickListener listener) {
        this.listener = listener;
    }

}
