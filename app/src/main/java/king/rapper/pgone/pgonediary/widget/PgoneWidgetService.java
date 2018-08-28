package king.rapper.pgone.pgonediary.widget;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;
import android.widget.RemoteViews;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import king.rapper.pgone.pgonediary.R;
import king.rapper.pgone.pgonediary.entity.NoteInfo;
import king.rapper.pgone.pgonediary.util.DateUtil;
import king.rapper.pgone.pgonediary.util.LogUtils;


/**
 * @ Create_time: 2018/5/21 on 15:34.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
public class PgoneWidgetService extends Service {
    AppWidgetManager awm;
    ComponentName appWidgetProvider;
    Timer timer;
    TimerTask task;
    private RemoteViews views;
    private NoteQueryImpl noteQuery;

    @Override
    public void onCreate() {
        super.onCreate();

        startForeground(1, new Notification());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    public class MyBinder extends Binder {
        public PgoneWidgetService getService() {
            return PgoneWidgetService.this;
        }
    }

    public void updateNote() {
        try {
            LogUtils.e("更新了数据");
            if (awm == null)
                awm = AppWidgetManager.getInstance(this);
            if (noteQuery == null)
                noteQuery = new NoteQueryImpl();
            noteQuery.noteQuery(new NoteQueryImpl.NoteInterface() {
                @Override
                public void getNote(List<NoteInfo> list) {
                    if (views != null) {
                        Integer notePosition = 0;
                        if (list != null && list.size() > 0) {
                            //有note
                            for (int i = 0; i < list.size(); i++) {
                                String content = list.get(i).getContent();
                                if (!TextUtils.isEmpty(content)) {
                                    if (notePosition == 0) {
                                        views.setTextViewText(R.id.tv_note1, content);
                                    } else if (notePosition == 1) {
                                        views.setTextViewText(R.id.tv_note2, content);
                                    } else if (notePosition == 2) {
                                        views.setTextViewText(R.id.tv_note3, content);
                                    }
                                    notePosition++;
                                    if (notePosition == 3) {
                                        break;
                                    }
                                }
                            }
                        } else {
                            //无note
                            views.setTextViewText(R.id.tv_note1, "暂无便签");
                        }
                    }
                }
            });

            appWidgetProvider = new ComponentName(getApplicationContext(), PgoneWidgetProvider.class);
            if (awm != null && appWidgetProvider != null && views != null)
                awm.updateAppWidget(appWidgetProvider, views);
        } catch (Exception e) {
            LogUtils.e("更新便签e:" + e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // 获得widget管理者
        if (awm == null)
            awm = AppWidgetManager.getInstance(this);
        // 初始化一个远程的view(RemoteViews)
        views = new RemoteViews(getPackageName(), R.layout.widget_pgone);
        String strTime = DateUtil.getNowTime();

        String[] dateYMDStr = DateUtil.getDateYMDStr();
        // 设置views内容
        views.setTextViewText(R.id.tv_count_widget, "" + strTime);

        if (dateYMDStr != null && dateYMDStr.length == 4) {
            views.setTextViewText(R.id.tv_freeMem_widget, dateYMDStr[0] + "年" + dateYMDStr[1] + "月 星期" + dateYMDStr[3]);
        }

        LogUtils.e("" + getPackageName());
        NoteQueryImpl NOTEQuery = new NoteQueryImpl();
        NOTEQuery.noteQuery(new NoteQueryImpl.NoteInterface() {
            @Override
            public void getNote(List<NoteInfo> list) {
                Integer notePosition = 0;
                if (list != null && list.size() > 0) {
                    //有note
                    for (int i = 0; i < list.size(); i++) {
                        String content = list.get(i).getContent();
                        if (!TextUtils.isEmpty(content)) {
                            if (notePosition == 0) {
                                views.setTextViewText(R.id.tv_note1, content);
                            } else if (notePosition == 1) {
                                views.setTextViewText(R.id.tv_note2, content);
                            } else if (notePosition == 2) {
                                views.setTextViewText(R.id.tv_note3, content);
                            }
                            notePosition++;
                            if (notePosition == 3) {
                                break;
                            }
                        }
                    }
                } else {
                    //无note
                    views.setTextViewText(R.id.tv_note1, "暂无便签");

                }


            }
        });


        appWidgetProvider = new ComponentName(getApplicationContext(), PgoneWidgetProvider.class);
        Intent intent1 = new Intent();
        ComponentName component = new ComponentName(getPackageName(), "king.rapper.pgone.pgonediary.UI.NoteListActivity");
        intent1.setComponent(component);
//                intent1.putExtra("category_value", "1");
//                intent1.putExtra("title", "上门洗车");

        PendingIntent intentAction = PendingIntent.getActivity(getApplicationContext(), 0, intent1, 0);
        views.setOnClickPendingIntent(R.id.tv_submit, intentAction);
        awm.updateAppWidget(appWidgetProvider, views);


        // 开启定时任务 每隔 28 秒更新widget
//        timer = new Timer();
//        task = new TimerTask() {
//
//            @Override
//            public void run() {
//            }
//        };
//        timer.schedule(task, 1000, 28000);
        return super.onStartCommand(intent, flags, startId);
    }
}
