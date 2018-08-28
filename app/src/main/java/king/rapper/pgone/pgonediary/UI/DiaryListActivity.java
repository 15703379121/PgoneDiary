package king.rapper.pgone.pgonediary.UI;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.daimajia.swipe.util.Attributes;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;
import king.rapper.pgone.pgonediary.R;
import king.rapper.pgone.pgonediary.adapter.DiaryRvAdapter;
import king.rapper.pgone.pgonediary.dao.DaoController;
import king.rapper.pgone.pgonediary.entity.DiaryInfo;
import king.rapper.pgone.pgonediary.interfaces.OnDiaryListClickListener;
import king.rapper.pgone.pgonediary.util.LogUtils;
import king.rapper.pgone.pgonediary.util.ToastUtil;
import king.rapper.pgone.pgonediary.base.BaseActivity;
import king.rapper.pgone.pgonediary.view.FullyLinearLayoutManager;

/**
 * @ Create_time: 2018/7/17 on 15:13.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
public class DiaryListActivity extends BaseActivity {
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    private DiaryRvAdapter mAdapter;
    private List<DiaryInfo> dataList;

    @Override
    protected void onResume() {
        super.onResume();
        new Thread() {
            @Override
            public void run() {
                dataList = DaoController.getInstance().getDiaryInfoAll();
                LogUtils.e("dataList:" + new Gson().toJson(dataList));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mAdapter != null) {
                            mAdapter.setList(dataList);
                        }
                    }
                });
            }
        }.start();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_diary_list;
    }

    @Override
    public void initView() {
        try {
            initTitleBar("皮皮日记");
        } catch (Exception e) {
            e.printStackTrace();
        }

        rv.setLayoutManager(new FullyLinearLayoutManager(this));
//        rv.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.gray6)));
        rv.setItemAnimator(new FadeInLeftAnimator());

    }

    @Override
    public void initData() /*throws Exception*/ {

        new Thread() {
            @Override
            public void run() {
                dataList = DaoController.getInstance().getDiaryInfoAll();
                LogUtils.e("dataList:" + new Gson().toJson(dataList));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter = new DiaryRvAdapter(DiaryListActivity.this, dataList);
                        mAdapter.setMode(Attributes.Mode.Single);
                        rv.setAdapter(mAdapter);
                        rvListener();
                    }
                });
            }
        }.start();
    }

    @OnClick(R.id.iv_add)
    public void onViewClicked() {
        Intent intent = new Intent(this, DiaryActivity.class);
        startActivity(intent);
    }

    @Override
    public void initListener() {
    }

    private void rvListener() {
        mAdapter.setOnItemClickListener(new OnDiaryListClickListener() {
            @Override
            public void OnItemSingleClick(int position) {

                Intent intent = new Intent(DiaryListActivity.this, DiaryActivity.class);
                String diaryStr = new Gson().toJson(dataList.get(position));
                LogUtils.e("diary:" + diaryStr);
                intent.putExtra("diary", diaryStr);
                startActivity(intent);
            }

            @Override
            public void OnItemDeleteClick(final int position) {
                if (dataList != null && dataList.size() > position) {
                    new Thread() {
                        @Override
                        public void run() {
                            DiaryInfo diaryInfo = dataList.get(position);
                            diaryInfo.setTag(-1);
                            DaoController.getInstance().getDiaryInfoDao().update(diaryInfo);
                            dataList = DaoController.getInstance().getDiaryInfoAll();
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
