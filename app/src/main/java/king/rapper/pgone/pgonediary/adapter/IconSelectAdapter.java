package king.rapper.pgone.pgonediary.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import king.rapper.pgone.pgonediary.R;
import king.rapper.pgone.pgonediary.entity.ExpressionIcon;
import king.rapper.pgone.pgonediary.entity.MoodIcon;
import king.rapper.pgone.pgonediary.entity.WeatherIcon;
import king.rapper.pgone.pgonediary.interfaces.OnIconSelectClickListener;
import king.rapper.pgone.pgonediary.util.UIUtil;

/**
 * @ Create_time: 2018/7/19 on 13:42.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
public class IconSelectAdapter extends RecyclerView.Adapter<IconSelectAdapter.MyViewHolder> {
    private final Activity context;
    private final int type;
    private List list;
    private OnIconSelectClickListener listener;

    public IconSelectAdapter(Activity context, List list, int type) {
        this.context = context;
        this.type = type;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_rv_weahter_select, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (list == null || position == list.size()) {
            //最后一个：添加按钮
            Glide.with(context).load(R.mipmap.icon_add_picture).into(holder.item_iv);
        } else {
            String url = "", imgType = "";
            // TODO: 2018/7/21 icon6
            switch (type) {
                case 0:
                    //心情
                    MoodIcon moodIcon = (MoodIcon) list.get(position);
                    url = moodIcon.getUrl();
                    imgType = moodIcon.getType();
                    break;
                case 1:
                    //天气
                    WeatherIcon weatherIcon = (WeatherIcon) list.get(position);
                    url = weatherIcon.getUrl();
                    imgType = weatherIcon.getType();
                    break;
                case 2:
                    //天气
                    ExpressionIcon expressionIcon = (ExpressionIcon) list.get(position);
                    url = expressionIcon.getUrl();
                    imgType = expressionIcon.getType();
                    break;
                default:
                    break;
            }
            UIUtil.setEndWithGIF(holder.item_iv, url, imgType);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    if (list == null || position == list.size()) {
                        //添加
                        listener.onItemAddClick(position);
                    } else {
                        listener.onItemSelectClick(position);
                    }
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (listener != null) {
                    if (list == null || position == list.size()) {
                        //添加
                    } else {
                        listener.onItemLongClick(position);
                    }
                }
                return true;
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list == null ? 1 : list.size() + 1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView item_iv;

        public MyViewHolder(View itemView) {
            super(itemView);
            item_iv = itemView.findViewById(R.id.item_iv);
        }
    }

    public void setOnItemClickListener(OnIconSelectClickListener listener) {
        this.listener = listener;
    }

    public void setList(List list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
