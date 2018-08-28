package king.rapper.pgone.pgonediary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.List;

import king.rapper.pgone.pgonediary.R;
import king.rapper.pgone.pgonediary.entity.MessageInfo;
import king.rapper.pgone.pgonediary.interfaces.OnDiaryListClickListener;
import king.rapper.pgone.pgonediary.view.MyClickLinearLayout;

public class MessageRvAdapter extends RecyclerSwipeAdapter<MessageRvAdapter.SimpleViewHolder> {

    private OnDiaryListClickListener listener;

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        private final SwipeLayout swipeLayout;
        private final ImageView buttonDelete;
        private final MyClickLinearLayout ll_click;
        private final TextView tv_title;
        private final TextView tv_group;
        private final ImageView iv_alarm;
        private final TextView tv_alarm;
        private final TextView tv_address;
        private final TextView tv_desc;
        private final TextView tv_time;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            buttonDelete = (ImageView) itemView.findViewById(R.id.delete);
            ll_click = (MyClickLinearLayout) itemView.findViewById(R.id.ll_click);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_group = (TextView) itemView.findViewById(R.id.tv_group);
            iv_alarm = (ImageView) itemView.findViewById(R.id.iv_alarm);
            tv_alarm = (TextView) itemView.findViewById(R.id.tv_alarm);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            tv_desc = (TextView) itemView.findViewById(R.id.tv_desc);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }

    private Context mContext;
    private List<MessageInfo> mDataset;

    public MessageRvAdapter(Context context, List<MessageInfo> objects) {
        this.mContext = context;
        this.mDataset = objects;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_message, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
        MessageInfo messageInfo = mDataset.get(position);

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
//        if (memoInfo != null) {
//            if (!TextUtils.isEmpty(memoInfo.getTitle())) {
//                viewHolder.tv_title.setText(memoInfo.getTitle());
//                viewHolder.tv_title.setVisibility(View.VISIBLE);
//            } else {
//                viewHolder.tv_title.setText("");
//                viewHolder.tv_title.setVisibility(View.GONE);
//            }
//            if (!TextUtils.isEmpty(memoInfo.getOrganizer())) {
//                viewHolder.tv_group.setText(memoInfo.getOrganizer());
//                viewHolder.tv_group.setVisibility(View.VISIBLE);
//            } else {
//                viewHolder.tv_group.setText("");
//                viewHolder.tv_group.setVisibility(View.GONE);
//            }
//            if (!TextUtils.isEmpty(memoInfo.getDescription())) {
//                viewHolder.tv_desc.setText(memoInfo.getDescription());
//                viewHolder.tv_desc.setVisibility(View.VISIBLE);
//            } else {
//                viewHolder.tv_desc.setText("");
//                viewHolder.tv_desc.setVisibility(View.GONE);
//            }
//            if (!TextUtils.isEmpty(memoInfo.getLocation())) {
//                viewHolder.tv_address.setText(memoInfo.getLocation());
//                viewHolder.tv_address.setVisibility(View.VISIBLE);
//            } else {
//                viewHolder.tv_address.setText("");
//                viewHolder.tv_address.setVisibility(View.GONE);
//            }
//            if (memoInfo.getStartTime() != null && memoInfo.getStartTime() != 0) {
//                String startTimeStr = DateUtil.getTimeString(memoInfo.getStartTime());
//                if (!TextUtils.isEmpty(startTimeStr)) {
//                    if (memoInfo.getEndTime() != null && memoInfo.getEndTime() != 0) {
//                        String endTimeStr = DateUtil.getTimeString(memoInfo.getEndTime());
//                        if (!TextUtils.isEmpty(endTimeStr)) {
//                            viewHolder.tv_time.setText(startTimeStr + " 至 " + endTimeStr);
//                            viewHolder.tv_time.setVisibility(View.VISIBLE);
//                        } else {
//                            viewHolder.tv_time.setText(startTimeStr + "");
//                            viewHolder.tv_time.setVisibility(View.VISIBLE);
//                        }
//                    } else {
//                        viewHolder.tv_time.setText(startTimeStr + "");
//                        viewHolder.tv_time.setVisibility(View.VISIBLE);
//                    }
//                } else {
//                    viewHolder.tv_time.setText("");
//                    viewHolder.tv_time.setVisibility(View.GONE);
//                }
//            } else {
//                viewHolder.tv_time.setText("");
//                viewHolder.tv_time.setVisibility(View.GONE);
//            }
//            if (memoInfo.getHas_alarm() != null && memoInfo.getHas_alarm() == 1) {
//                viewHolder.iv_alarm.setSelected(true);
//                if (memoInfo.getMinutes() != null) {
//                    // 提前提醒
//                    viewHolder.tv_alarm.setText("提前 " + DateUtil.getFormatMinute(memoInfo.getMinutes()) + " 提醒");
//                } else {
//                    //不给提醒
//                    viewHolder.tv_alarm.setText("");
//                }
//            } else {
//                //不给提醒
//                viewHolder.iv_alarm.setSelected(false);
//                viewHolder.tv_alarm.setText("未开启闹钟提醒");
//            }
//        }
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

    public void setList(List<MessageInfo> list) {
        this.mDataset = list;
        notifyDatasetChanged();
    }

    public void setOnItemClickListener(OnDiaryListClickListener listener) {
        this.listener = listener;
    }
}
