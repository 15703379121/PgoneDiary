package king.rapper.pgone.pgonediary.UI;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import king.rapper.pgone.pgonediary.R;
import king.rapper.pgone.pgonediary.adapter.NoteListRvAdapter;
import king.rapper.pgone.pgonediary.dao.DaoController;
import king.rapper.pgone.pgonediary.entity.NoteInfo;
import king.rapper.pgone.pgonediary.interfaces.NoteListItemClickListener;
import king.rapper.pgone.pgonediary.util.DateUtil;
import king.rapper.pgone.pgonediary.util.LogUtils;
import king.rapper.pgone.pgonediary.base.BaseActivity;
import king.rapper.pgone.pgonediary.base.TitleListener;
import king.rapper.pgone.pgonediary.view.FullyLinearLayoutManager;
import king.rapper.pgone.pgonediary.widget.PgoneWidgetService;

/**
 * @ Create_time: 2018/7/26 on 16:28.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
public class NoteListActivity extends BaseActivity {
    @BindView(R.id.rv_note)
    RecyclerView rvNote;
    @BindView(R.id.tv_new_note)
    TextView tvNewNote;
    @BindView(R.id.cb_select)
    CheckBox cbSelect;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.layout_title_bar_right_text)
    TextView layoutTitleBarRightText;
    private NoteListRvAdapter noteListRvAdapter;
    private NoteInfo editNoteInfo;
    private FullyLinearLayoutManager fullyLinearLayoutManager;
    private PgoneWidgetService pgoneWidgetService;

    @Override
    public int getLayoutId() {
        return R.layout.activity_note_list;
    }

    private List<NoteInfo> dataList;

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 建立连接
            // 获取服务的操作对象
            PgoneWidgetService.MyBinder binder = (PgoneWidgetService.MyBinder) service;
            // 获取到的Service即MyService
            pgoneWidgetService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 连接断开
        }
    };

    @Override
    protected void initView() {
        try {
            initTitleBar("皮皮便签", "编辑", new TitleListener() {
                @Override
                public void RightClick() {
                    //编辑删除
                    if (tvDelete.getVisibility() != View.VISIBLE) {
                        tvDelete.setVisibility(View.VISIBLE);
                        cbSelect.setVisibility(View.VISIBLE);
                        layoutTitleBarRightText.setText("完成");
                        if (noteListRvAdapter != null) {
                            noteListRvAdapter.setSelectable(true);
                        }
                    } else {
                        tvDelete.setVisibility(View.GONE);
                        cbSelect.setVisibility(View.GONE);
                        layoutTitleBarRightText.setText("编辑");
                        if (noteListRvAdapter != null)
                            noteListRvAdapter.setSelectable(false);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intentService = new Intent(this, PgoneWidgetService.class);
        if (serviceConnection != null)
            bindService(intentService, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void initData() {
        new Thread() {


            @Override
            public void run() {
                dataList = DaoController.getInstance().getNoteInfoAll();
                LogUtils.e("dataList:" + new Gson().toJson(dataList));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rvNoteListShow(dataList);
                    }
                });
            }
        }.start();
    }

    private void rvNoteListShow(List<NoteInfo> dataList) {
        if (noteListRvAdapter == null) {
            fullyLinearLayoutManager = new FullyLinearLayoutManager(this);
            rvNote.setLayoutManager(fullyLinearLayoutManager);
            noteListRvAdapter = new NoteListRvAdapter(this, dataList);
            rvNote.setAdapter(noteListRvAdapter);
            rvInitListener();
        }
        noteListRvAdapter.setList(dataList);
    }

    /**
     * RecyclerView 移动到当前位置，
     *
     * @param n 要跳转的位置
     */
    public void MoveToPosition(int n) {
        if (fullyLinearLayoutManager != null && rvNote != null) {
            int firstItem = fullyLinearLayoutManager.findFirstVisibleItemPosition();
            int lastItem = fullyLinearLayoutManager.findLastVisibleItemPosition();
            if (n <= firstItem) {
                rvNote.scrollToPosition(n);
            } else if (n <= lastItem) {
                int top = rvNote.getChildAt(n - firstItem).getTop();
                rvNote.scrollBy(0, top);
            } else {
                rvNote.scrollToPosition(n);
            }
        }
    }

    private void rvInitListener() {
        noteListRvAdapter.setOnItemClickListener(new NoteListItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onItemSelect(int position) {

            }

            @Override
            public void onItemEdit(int position) {

            }

            @Override
            public void onIteRecording(int position) {

            }
        });
    }

    @OnClick({R.id.cb_select, R.id.tv_delete, R.id.tv_new_note})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_new_note:
                //新建
                editNoteInfo = new NoteInfo();
                editNoteInfo.setTime(DateUtil.getTimeLong());
                editNoteInfo.setTag(0);
                LogUtils.e("editNoteInfo:" + editNoteInfo.toString());
                if (dataList == null)
                    dataList = new ArrayList<>();
                DaoController.getInstance().getNoteInfoDao().insert(editNoteInfo);
                dataList = DaoController.getInstance().getNoteInfoAll();
                LogUtils.e("dataList:" + new Gson().toJson(dataList));
                rvNoteListShow(dataList);
                sendNoteBroadcast();
                break;
            case R.id.cb_select:
                boolean checked = cbSelect.isChecked();
                LogUtils.e("checked:" + checked);
                if (noteListRvAdapter != null) {
                    noteListRvAdapter.setSelectAll(checked);
                }
                break;
            case R.id.tv_delete:
                if (noteListRvAdapter != null) {
                    noteListRvAdapter.deleteSelect();
                }
                break;
        }
    }

    public void sendNoteBroadcast() {
        if (pgoneWidgetService != null) {
            pgoneWidgetService.updateNote();
        }
//        LogUtils.e("发送广播:");
//        Intent intent = new Intent(this, NoteReceiver.class);
//        intent.putExtra("name", "qqyumidi");
//        sendBroadcast(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceConnection != null)
            unbindService(serviceConnection);
    }

    public void setCbSelect(Boolean check) {
        LogUtils.e("check:" + check);
        if (check != cbSelect.isChecked())
            cbSelect.setChecked(check);
    }
}
