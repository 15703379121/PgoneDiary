package king.rapper.pgone.pgonediary.UI;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import com.daimajia.swipe.util.Attributes;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;
import king.rapper.pgone.pgonediary.R;
import king.rapper.pgone.pgonediary.adapter.MessageRvAdapter;
import king.rapper.pgone.pgonediary.base.BaseActivity;
import king.rapper.pgone.pgonediary.dao.DaoController;
import king.rapper.pgone.pgonediary.entity.MessageInfo;
import king.rapper.pgone.pgonediary.interfaces.OnDiaryListClickListener;
import king.rapper.pgone.pgonediary.util.LogUtils;
import king.rapper.pgone.pgonediary.util.ToastUtil;
import king.rapper.pgone.pgonediary.view.FullyLinearLayoutManager;

/**
 * @ Create_time: 2018/8/11 on 10:00.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
public class MessageListActivity extends BaseActivity {
    @BindView(R.id.rv)
    RecyclerView rv;
    private MessageRvAdapter mAdapter;
    private List<MessageInfo> dataList;

    @Override
    public int getLayoutId() {
        return R.layout.activity_message_list;
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread() {
            @Override
            public void run() {
                dataList = DaoController.getInstance().getMessageInfoAll();
                LogUtils.e("dataList:" + new Gson().toJson(dataList));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mAdapter == null) {
                            mAdapter = new MessageRvAdapter(MessageListActivity.this, dataList);
                            mAdapter.setMode(Attributes.Mode.Single);
                            rv.setAdapter(mAdapter);
                            rvListener();
                        } else {
                            mAdapter.setList(dataList);
                        }
                    }
                });
            }
        }.start();
    }

    @Override
    protected void initView() {
        try {
            initTitleBar("皮皮消息");
        } catch (Exception e) {
            e.printStackTrace();
        }

        rv.setLayoutManager(new FullyLinearLayoutManager(this));
        rv.setItemAnimator(new FadeInLeftAnimator());
    }


    private void rvListener() {
        mAdapter.setOnItemClickListener(new OnDiaryListClickListener() {
            @Override
            public void OnItemSingleClick(int position) {

                Intent intent = new Intent(MessageListActivity.this, MessageActivity.class);
                String diaryStr = new Gson().toJson(dataList.get(position));
                LogUtils.e("message:" + diaryStr);
                intent.putExtra("message", diaryStr);
                startActivity(intent);
            }

            @Override
            public void OnItemDeleteClick(final int position) {
                if (dataList != null && dataList.size() > position) {
                    new Thread() {
                        @Override
                        public void run() {
                            MessageInfo messageInfo = dataList.get(position);
                            messageInfo.setTag(-1);
                            DaoController.getInstance().getMessageInfoDao().update(messageInfo);
                            dataList = DaoController.getInstance().getMessageInfoAll();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.setList(dataList);
                                    ToastUtil.showShort("删除成功");
                                }
                            });
                        }
                    }.start();
                }
            }

            @Override
            public void OnItemDoubleClick(int position) {

            }
        });
    }
}
