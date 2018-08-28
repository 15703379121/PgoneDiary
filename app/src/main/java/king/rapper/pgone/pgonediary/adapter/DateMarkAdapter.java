package king.rapper.pgone.pgonediary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import king.rapper.pgone.pgonediary.R;

/**
 * @ Create_time: 2018/7/4 on 16:11.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
public class DateMarkAdapter extends RecyclerView.Adapter {

    private final Context context;
    private final List<?> list;

    public DateMarkAdapter(Context context, List<?> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_date_mark, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
