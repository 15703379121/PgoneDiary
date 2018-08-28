package king.rapper.pgone.pgonediary.UI;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ImageView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;
import king.rapper.pgone.pgonediary.R;
import king.rapper.pgone.pgonediary.adapter.MemoRvAdapter;
import king.rapper.pgone.pgonediary.entity.MemoInfo;
import king.rapper.pgone.pgonediary.interfaces.MemoCallback;
import king.rapper.pgone.pgonediary.interfaces.OnDiaryListClickListener;
import king.rapper.pgone.pgonediary.util.CalendarReminderUtils;
import king.rapper.pgone.pgonediary.util.LogUtils;
import king.rapper.pgone.pgonediary.util.ToastUtil;
import king.rapper.pgone.pgonediary.base.BaseActivity;
import king.rapper.pgone.pgonediary.util.constant.Config;
import king.rapper.pgone.pgonediary.view.FullyLinearLayoutManager;

/**
 * @ Create_time: 2018/7/26 on 16:27.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
public class MemoListActivity extends BaseActivity {
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.iv_add)
    ImageView ivAdd;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String message = (String) msg.obj;
                    if (!TextUtils.isEmpty(message))
                        ToastUtil.showShort(message);
                    break;
                case 1:
                    memoInfoList = (List<MemoInfo>) msg.obj;
                    if (memoRvAdapter == null) {
                        memoRvAdapter = new MemoRvAdapter(MemoListActivity.this, memoInfoList);
                        if (rv != null)
                            rv.setAdapter(memoRvAdapter);
                    } else {
                        memoRvAdapter.setList(memoInfoList);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private MemoRvAdapter memoRvAdapter;
    private List<MemoInfo> memoInfoList;

    @Override
    public int getLayoutId() {
        return R.layout.activity_memo_list;
    }

    @Override
    protected void onResume() {
        super.onResume();
        findAllCalendarEvent();
    }

    @Override
    protected void initView() {
        try {
            initTitleBar("皮皮日程");
        } catch (Exception e) {
            e.printStackTrace();
        }

        rv.setLayoutManager(new FullyLinearLayoutManager(this));
        rv.setItemAnimator(new FadeInLeftAnimator());
        memoRvAdapter = new MemoRvAdapter(MemoListActivity.this, new ArrayList<MemoInfo>());
        rv.setAdapter(memoRvAdapter);
    }

    @Override
    protected void initData() {
        findAllCalendarEvent();
    }

    @Override
    protected void initListener() {
        memoRvAdapter.setOnItemClickListener(new OnDiaryListClickListener() {
            @Override
            public void OnItemSingleClick(int position) {
                Intent intent = new Intent(MemoListActivity.this, MemoActivity.class);
                String memoStr = new Gson().toJson(memoInfoList.get(position));
                LogUtils.e("memo:" + memoStr);
                intent.putExtra("memo", memoStr);
                startActivity(intent);
            }

            @Override
            public void OnItemDeleteClick(final int position) {
                if (memoInfoList != null && position >= 0 && memoInfoList.size() > position) {
                    deleteCalendarEvent(memoInfoList.get(position).getMemoInfoId());
                }
            }

            @Override
            public void OnItemDoubleClick(int position) {

            }
        });
    }

    @OnClick(R.id.iv_add)
    public void onViewClicked() {
        Intent intent = new Intent(MemoListActivity.this, MemoActivity.class);
        startActivity(intent);
    }

    /**
     * 查找所有日程
     */
    private void findAllCalendarEvent() {
        CalendarReminderUtils.findAllCalendarEvent(this, new MemoCallback() {
            @Override
            public void memoCallback(List<MemoInfo> memoInfoList, String message) {
                LogUtils.e("memokInfoList:" + new Gson().toJson(memoInfoList));
                LogUtils.e("message:" + message);
                Message msg = Message.obtain();
                if (Config.Message_Success.equals(message)) {
                    msg.what = 1;
                    msg.obj = memoInfoList;
                } else {
                    msg.what = 0;
                    msg.obj = message;
                }
                mHandler.sendMessage(msg);
            }
        });
    }

    /**
     * 按id删除日程
     */
    private void deleteCalendarEvent(Long memoInfoId) {
        CalendarReminderUtils.deleteCalendarEvent(MemoListActivity.this, memoInfoId, new MemoCallback() {
            @Override
            public void memoCallback(List<MemoInfo> memoInfoList, String message) {
                Message msg = Message.obtain();
                msg.what = 0;
                if (Config.Message_Success.equals(message)) {
                    findAllCalendarEvent();
                    msg.obj = "删除成功";
                } else {
                    msg.obj = message;
                }
                mHandler.sendMessage(msg);
            }
        });
    }
}
