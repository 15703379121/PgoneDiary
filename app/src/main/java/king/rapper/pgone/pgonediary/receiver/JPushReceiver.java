package king.rapper.pgone.pgonediary.receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

import cn.jpush.android.api.JPushInterface;
import king.rapper.pgone.pgonediary.R;
import king.rapper.pgone.pgonediary.UI.MainActivity;
import king.rapper.pgone.pgonediary.UI.MessageActivity;
import king.rapper.pgone.pgonediary.UI.MessageListActivity;
import king.rapper.pgone.pgonediary.dao.DaoController;
import king.rapper.pgone.pgonediary.entity.MessageInfo;
import king.rapper.pgone.pgonediary.util.DateUtil;
import king.rapper.pgone.pgonediary.util.LogUtils;
import king.rapper.pgone.pgonediary.util.ToastUtil;

/**
 * @ Create_time: 2018/5/10 on 14:48.
 * @ description：
 * @ author: radish  15703379121@163.com
 */
public class JPushReceiver extends BroadcastReceiver {
    private NotificationManager nm;
    private Context context;
    private MessageInfo messageInfo;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        if (null == nm) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        Bundle bundle = intent.getExtras();
        if (null == bundle)
            return;

        //展示信息
        showNotification(bundle);


//加一个推送管理类
        if (Build.VERSION.SDK_INT >= 26) {
            try {
                LogUtils.e("8.0处理了");
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                String id = context.getPackageName();
                CharSequence name = context.getString(R.string.app_name);
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel channel = new NotificationChannel(id, name, importance);
                channel.enableLights(true);
                channel.setLightColor(Color.RED);
                channel.enableVibration(true);
                channel.setShowBadge(true);
                channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notificationManager.createNotificationChannel(channel);
                Notification.Builder builder = new Notification.Builder(context, id); //与channelId对应
                //icon title text必须包含，不然影响桌面图标小红点的展示
                String message = bundle.getString(JPushInterface.EXTRA_ALERT);

                String notificationTitle = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);//通知栏标题信息
                builder.setSmallIcon(R.mipmap.ic_test)
                        .setContentTitle(notificationTitle)
                        .setContentText(message);
                builder.setAutoCancel(true);

                openNotificationWith26(bundle, notificationManager, builder);

            } catch (Exception e) {
                LogUtils.e("Android8.0推送异常:" + e.getMessage());
                e.printStackTrace();
            }
        }


        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            LogUtils.e("JPush用户注册成功");
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            LogUtils.e("接收到推送下来的自定义消息:" + bundle.getString(JPushInterface.EXTRA_MSG_ID + bundle.getString(JPushInterface.EXTRA_MESSAGE)));
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            LogUtils.e("接收到推送下来的通知");
            receivedMessage(bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {

            openNotification(bundle);
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
                .getAction())) {
            LogUtils.e("用户收到到RICH PUSH CALLBACK: "
                    + bundle.getString(JPushInterface.EXTRA_EXTRA));
        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(
                    JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            LogUtils.e("[MyReceiver]" + intent.getAction()
                    + " connected state change to " + connected);
        } else {
            LogUtils.e("Unhandled intent - " + intent.getAction());
        }
    }

    /**
     * 打印通知
     *
     * @param bundle
     */
    private void showNotification(Bundle bundle) {
        try {

            String msg_id = bundle.getString(JPushInterface.EXTRA_MSG_ID);//唯一标识消息的 ID
            List<MessageInfo> messageInfoByMsgId = DaoController.getInstance().getMessageInfoByMsgId(msg_id);

            if (!TextUtils.isEmpty(msg_id)) {
                if (messageInfoByMsgId != null && messageInfoByMsgId.size() > 0) {
                    //                此消息已经保存过了
                    messageInfo = messageInfoByMsgId.get(0);
                } else {
                    messageInfo = new MessageInfo();
                    messageInfo.setExtraMsgId(msg_id);

                    String extraTitle = bundle.getString(JPushInterface.EXTRA_TITLE);//通知栏标题信息
                    String extraMessage = bundle.getString(JPushInterface.EXTRA_MESSAGE);//通知栏标题信息
                    String extraNotificationId = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_ID);//通知id
                    String extraNotificationTitle = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);//通知栏标题信息
                    String extraAlter = bundle.getString(JPushInterface.EXTRA_ALERT);//通知栏详情信息
                    String extraExtra = bundle.getString(JPushInterface.EXTRA_EXTRA);//通知携带内容
                    String extraBigText = bundle.getString(JPushInterface.EXTRA_BIG_TEXT);//用于接收大文本
                    String extraRichpushHtmlPath = bundle.getString(JPushInterface.EXTRA_RICHPUSH_HTML_PATH);//用于展现WebView
                    String extraRichpushHtmlRes = bundle.getString(JPushInterface.EXTRA_RICHPUSH_HTML_RES);//用于接收图片
                    String extraBigPicPath = bundle.getString(JPushInterface.EXTRA_BIG_PIC_PATH);//用于接收大图片
                    String extraInbox = bundle.getString(JPushInterface.EXTRA_INBOX);//用于接收json逐条显示
                    String extraNotiPriority = bundle.getString(JPushInterface.EXTRA_NOTI_PRIORITY);//优先级

                    if (!TextUtils.isEmpty(extraTitle))
                        messageInfo.setExtraTitle(extraTitle);

                    if (!TextUtils.isEmpty(extraMessage))
                        messageInfo.setExtraTitle(extraMessage);

                    if (!TextUtils.isEmpty(extraNotificationId))
                        messageInfo.setExtraTitle(extraNotificationId);

                    if (!TextUtils.isEmpty(extraNotificationTitle))
                        messageInfo.setExtraTitle(extraNotificationTitle);

                    if (!TextUtils.isEmpty(extraAlter))
                        messageInfo.setExtraTitle(extraAlter);

                    if (!TextUtils.isEmpty(extraExtra))
                        messageInfo.setExtraTitle(extraExtra);

                    if (!TextUtils.isEmpty(extraBigText))
                        messageInfo.setExtraTitle(extraBigText);

                    if (!TextUtils.isEmpty(extraRichpushHtmlPath))
                        messageInfo.setExtraTitle(extraRichpushHtmlPath);

                    if (!TextUtils.isEmpty(extraRichpushHtmlRes))
                        messageInfo.setExtraTitle(extraRichpushHtmlRes);

                    if (!TextUtils.isEmpty(extraBigPicPath))
                        messageInfo.setExtraTitle(extraBigPicPath);

                    if (!TextUtils.isEmpty(extraInbox))
                        messageInfo.setExtraTitle(extraInbox);

                    if (!TextUtils.isEmpty(extraNotiPriority))
                        messageInfo.setExtraTitle(extraNotiPriority);

                    messageInfo.setTime(DateUtil.getTimeLong());
                    messageInfo.setTag(0);
                    DaoController.getInstance().getMessageInfoDao().save(messageInfo);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收到消息时
     *
     * @param bundle
     */
    private void receivedMessage(Bundle bundle) {
        String notificationInfo = bundle.getString(JPushInterface.EXTRA_ALERT);//通知栏详情信息
       /* if (Build.VERSION.SDK_INT >= 26) {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_HIGH);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
            Notification.Builder builder = new Notification.Builder(context, "channel_id");
            builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setWhen(System.currentTimeMillis())
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    .setContentTitle(msg.title)
                    .setContentText(msg.text)
                    .setAutoCancel(true).build();
        } else {
            Notification.Builder builder = new Notification.Builder(context);
            builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setWhen(System.currentTimeMillis())
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    .setContentTitle(msg.title)
                    .setContentText(msg.text)
                    .setAutoCancel(true).build();
        }*/
        if (!TextUtils.isEmpty(notificationInfo)) {
            ToastUtil.showShort(notificationInfo);
        }

    }

    /**
     * 8.0以上点击通知时
     * {"order_state":1,"data_id":"0","badge":100,"type":2}
     * type  1:订单    2：资讯   3：优惠券
     *
     * @param bundle
     * @param notificationManager
     * @param builder
     */
    private void openNotificationWith26(Bundle bundle, NotificationManager notificationManager, Notification.Builder builder) {
        String notificationExtras = bundle.getString(JPushInterface.EXTRA_EXTRA);//通知携带内容
        try {
            if (context != null) {
                Intent intent = null;
                if (messageInfo != null) {
                    intent = new Intent(context, MessageActivity.class);
                    intent.putExtra("message", new Gson().toJson(messageInfo));
                } else {
                    intent = new Intent(context, MessageListActivity.class);
                }
                if (intent != null) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(pendingIntent);
                    notificationManager.notify(0, builder.build());
                }
            }
        } catch (Exception e) {
            LogUtils.e("8.0版本通知异常：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 点击通知时
     * {"order_state":1,"data_id":"0","badge":100,"type":2}
     * type  1:订单    2：资讯   3：优惠券
     *
     * @param bundle
     */
    private void openNotification(Bundle bundle) {
        try {
            if (context != null) {
                Intent intent = null;
                if (messageInfo != null) {
                    intent = new Intent(context, MessageActivity.class);
                    intent.putExtra("message", new Gson().toJson(messageInfo));
                } else {
                    intent = new Intent(context, MessageListActivity.class);
                }
                if (intent != null) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    context.startActivity(intent);
                }
            }
        } catch (Exception e) {
            LogUtils.e("通知异常：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 打开主页
     */
    private void appNotification() throws Exception {
        if (context != null) {
            Intent i = new Intent(context, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(i);
        }
    }
/*
    *//**
     * 收到优惠券通知
     *
     * @param jsonObject
     *//*
    private void couponNotification(JSONObject jsonObject) throws Exception {
        if (context != null) {
            Intent i = new Intent(context, YHQActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(i);
        }
    }

    *//**
     * 资讯通知（发现页）
     *
     * @param jsonObject
     *//*
    private void findNotification(JSONObject jsonObject) throws Exception {
        if (context != null) {
            Intent c = new Intent(context, WebViewActivity.class);
            c.putExtra("id", jsonObject.getString("data_id"));
            c.putExtra("pic", Config.NotificationDefaultImg);
            c.putExtra("tag", "discovery_content?id=");
            c.putExtra("title", "");
            c.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(c);
        }
    }

    *//**
     * 网页
     *
     * @param jsonObject
     *//*
    private void webNotification(JSONObject jsonObject) throws Exception {
        if (context != null) {
            Intent c = new Intent(context, OnlyWebActivity.class);
            c.putExtra("url", jsonObject.getString("data_url"));
            c.putExtra("img_url", Config.NotificationDefaultImg);
            c.putExtra("desc", "逸掌帮");
            c.putExtra("title", jsonObject.getString("alert"));
            c.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(c);
        }
    }


    *//**
     * 订单通知
     *
     * @param jsonObject
     *//*
    private void orderNotification(JSONObject jsonObject) throws Exception {
        if (context != null) {
            //{"order_state":100,"type":1,"data_id":0}
            Intent c = new Intent(context, WDYYActivity.class);
            c.putExtra("open", 0);//走到全部订单
            c.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(c);
        }
    }

    *//**
     * 逸+头条通知
     *
     * @param jsonObject
     *//*
    private void headNotification(JSONObject jsonObject) throws Exception {
        if (context != null) {
            Intent intent = new Intent(context, OnlyWebActivity.class);
            intent.putExtra("url", MyUrl.URL + "get_news_content?id=" + jsonObject.getString("data_id"));
            intent.putExtra("title", "逸+头条");
            intent.putExtra("desc", "逸+头条");
            intent.putExtra("img_url", Config.NotificationDefaultImg);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        }
    }*/
}
