package king.rapper.pgone.pgonediary.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.List;

import king.rapper.pgone.pgonediary.R;
import king.rapper.pgone.pgonediary.entity.DiaryInfo;
import king.rapper.pgone.pgonediary.interfaces.OnDiaryListClickListener;
import king.rapper.pgone.pgonediary.util.DateUtil;
import king.rapper.pgone.pgonediary.util.ToastUtil;
import king.rapper.pgone.pgonediary.util.UIUtil;
import king.rapper.pgone.pgonediary.util.WebViewUtil;
import king.rapper.pgone.pgonediary.view.CustomSwipeLayout;
import king.rapper.pgone.pgonediary.view.MyClickLinearLayout;

public class DiaryRvAdapter extends RecyclerSwipeAdapter<DiaryRvAdapter.SimpleViewHolder> {

    private OnDiaryListClickListener listener;

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        TextView textViewTitle;
        TextView textViewTime;
        ImageView buttonDelete;
        private final ImageView iv_header_mood;
        private final ImageView iv_header_weather;
        private final WebView editor;
        private final MyClickLinearLayout ll_click;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            textViewTitle = (TextView) itemView.findViewById(R.id.title);
            textViewTime = (TextView) itemView.findViewById(R.id.text_time);
            buttonDelete = (ImageView) itemView.findViewById(R.id.delete);
            iv_header_mood = (ImageView) itemView.findViewById(R.id.iv_header_mood);
            iv_header_weather = (ImageView) itemView.findViewById(R.id.iv_header_weather);
            editor = (WebView) itemView.findViewById(R.id.editor);
            ll_click = (MyClickLinearLayout) itemView.findViewById(R.id.ll_click);
        }
    }

    private Context mContext;
    private List<DiaryInfo> mDataset;

    public DiaryRvAdapter(Context context, List<DiaryInfo> objects) {
        this.mContext = context;
        this.mDataset = objects;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_diray, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
        DiaryInfo diary = mDataset.get(position);

        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        viewHolder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
            }
        });


        CustomSwipeLayout customSwipeLayout = (CustomSwipeLayout) viewHolder.swipeLayout;
        customSwipeLayout.setOnSingleClickListener(new SwipeLayout.SingleClickListener() {
            @Override
            public void onSingleClick(SwipeLayout layout, boolean surface) {
                ToastUtil.showShort("Click");
                if (listener != null) {
                    listener.OnItemSingleClick(position);
                }
            }
        });
        viewHolder.swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                ToastUtil.showShort("DoubleClick");
                if (listener != null) {
                    listener.OnItemDoubleClick(position);
                }
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
//                mItemManger.removeShownLayouts(viewHolder.swipeLayout);
//                mDataset.remove(position);
//                notifyItemRemoved(position);
//                notifyItemRangeChanged(position, mDataset.size());
                mItemManger.closeAllItems();
//                ToastUtil.showShort("Deleted " + viewHolder.textViewData.getText().toString() + "!");
            }
        });
        if (diary != null) {
            if (!TextUtils.isEmpty(diary.getTitle()))
                viewHolder.textViewTitle.setText(diary.getTitle());
            if (diary.getTime() != null && diary.getTime() != 0)
                viewHolder.textViewTime.setText(DateUtil.getDateString(diary.getTime()));
            if (!TextUtils.isEmpty(diary.getMood())) {
                UIUtil.setEndWithGIF(viewHolder.iv_header_mood, diary.getMood());
            }
            if (!TextUtils.isEmpty(diary.getWeather())) {
                UIUtil.setEndWithGIF(viewHolder.iv_header_weather, diary.getWeather());
            }
            viewHolder.editor.setBackgroundColor(Color.TRANSPARENT);
            if (!TextUtils.isEmpty(diary.getContent())) {
                viewHolder.editor.loadDataWithBaseURL(null, WebViewUtil.getHtmlData(diary.getContent()),
                        "text/html", "utf-8", null);
            }
        }
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

    public void setList(List<DiaryInfo> list) {
        this.mDataset = list;
        notifyDatasetChanged();
    }

    public void setOnItemClickListener(OnDiaryListClickListener listener) {
        this.listener = listener;
    }
}
