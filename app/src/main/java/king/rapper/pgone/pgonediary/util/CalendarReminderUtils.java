package king.rapper.pgone.pgonediary.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import king.rapper.pgone.pgonediary.entity.MemoInfo;
import king.rapper.pgone.pgonediary.interfaces.MemoCallback;
import king.rapper.pgone.pgonediary.util.constant.Config;

//  https://blog.csdn.net/gf771115/article/details/76691306    CalendarProvider

/**
 * @ Create_time: 2018/8/1 on 15:48.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
public class CalendarReminderUtils {

    private static String CALENDARS_NAME = "pgone";
    private static String CALENDARS_ACCOUNT_NAME = "PGONE@pgone.com";
    private static String CALENDARS_ACCOUNT_TYPE = "com.android.pgone";
    private static String CALENDARS_DISPLAY_NAME = "PGONE闹钟";

    public static Boolean checkSelfPermission(Activity context) {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionList = new ArrayList<>();
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.READ_CALENDAR);
            } else if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.WRITE_CALENDAR);
            } else { //所有的权限都已经授权过了
                return true;
            }
        } else {
            return true;
        }
        return false;
    }

    /**
     * 检查是否已经添加了日历账户，如果没有添加先添加一个日历账户再查询
     * 获取账户成功返回账户id，否则返回-1
     */
    private static Long checkAndAddCalendarAccount(Context context) {
        Long oldId = checkCalendarAccount(context);
        if (oldId >= 0) {
            LogUtils.e("已经有了日历");
            return oldId;
        } else {
            LogUtils.e("添加日历");
            long addId = addCalendarAccount(context);
            if (addId >= 0) {
                LogUtils.e("添加日历成功");
                return checkCalendarAccount(context);
            } else {
                LogUtils.e("添加日历失败");
                return -1l;
            }
        }
    }

    /**
     * 检查是否存在现有账户，存在则返回账户id，否则返回-1
     */
    private static Long checkCalendarAccount(Context context) {
        String selection = CalendarContract.Calendars.NAME + "=?";
        String[] selectionArgs = new String[]{CALENDARS_NAME};
        @SuppressLint("MissingPermission") Cursor userCursor = context.getContentResolver().query(CalendarContract.Calendars.CONTENT_URI, null, selection, selectionArgs, null);
        try {
            if (userCursor == null) { //查询返回空值
                return -1l;
            }
            int count = userCursor.getCount();
            if (count > 0) { //存在现有账户，取第一个账户的id返回

                for (userCursor.moveToFirst(); !userCursor.isAfterLast(); userCursor.moveToNext()) {
                    String name = userCursor.getString(userCursor.getColumnIndex(CalendarContract.Calendars.NAME));
                    Long _id = userCursor.getLong(userCursor.getColumnIndex(CalendarContract.Calendars._ID));
                    LogUtils.e("有账户啦:" + name + "     id:" + _id);
                    return _id;
//                    if (!TextUtils.isEmpty(name) && CALENDARS_NAME.equals(name)) {
//                    }
                }
            } else {
                return -1l;
            }
        } finally {
            if (userCursor != null) {
                userCursor.close();
            }
        }
        return -1l;
    }

    /**
     * 添加日历账户，账户创建成功则返回账户id，否则返回-1
     */
    private static long addCalendarAccount(Context context) {
        LogUtils.e("没有日历账户所以重新添加");
        TimeZone timeZone = TimeZone.getDefault();
        ContentValues value = new ContentValues();
        value.put(CalendarContract.Calendars.NAME, CALENDARS_NAME);
        value.put(CalendarContract.Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME);
        value.put(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE);
        value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CALENDARS_DISPLAY_NAME);
        value.put(CalendarContract.Calendars.VISIBLE, 1);
        value.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE);
        value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        value.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
        value.put(CalendarContract.Calendars.OWNER_ACCOUNT, CALENDARS_ACCOUNT_NAME);
        value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0);

//        Uri calendarUri = Uri.parse(CALENDER_URL);
        Uri calendarUri = CalendarContract.Calendars.CONTENT_URI.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE)
                .build();

        Uri result = context.getContentResolver().insert(calendarUri, value);
        long id = result == null ? -1 : ContentUris.parseId(result);
        return id;
    }

    /**
     * 添加日历事件
     */
    public static void addCalendarEvent(final Activity context, final MemoInfo memoInfo, final MemoCallback callback) {
        if (checkSelfPermission(context)) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    if (context == null) {
                        if (callback != null) {
                            callback.memoCallback(null, "上下文为空");
                        }
                        return;
                    }
                    Long calId = checkAndAddCalendarAccount(context); //获取日历账户的id
                    if (calId < 0) { //获取账户id失败直接返回，添加日历事件失败
                        if (callback != null) {
                            callback.memoCallback(null, "获取账户失败");
                        }
                        return;
                    }
                    //添加日历事件
                    TimeZone tz = TimeZone.getDefault();//获取默认时区
                    ContentValues event = new ContentValues();
                    if (!TextUtils.isEmpty(memoInfo.getTitle()))
                        event.put(CalendarContract.Events.TITLE, memoInfo.getTitle());
                    if (!TextUtils.isEmpty(memoInfo.getDescription()))
                        event.put(CalendarContract.Events.DESCRIPTION, memoInfo.getDescription());
                    event.put(CalendarContract.Events.CALENDAR_ID, calId); //插入账户的id
                    if (!TextUtils.isEmpty(memoInfo.getLocation()))
                        event.put(CalendarContract.Events.EVENT_LOCATION, memoInfo.getLocation());
                    if (memoInfo.getStartTime() != null && memoInfo.getStartTime() != 0)
                        event.put(CalendarContract.Events.DTSTART, memoInfo.getStartTime());
                    if (memoInfo.getEndTime() != null && memoInfo.getEndTime() != 0)
                        event.put(CalendarContract.Events.DTEND, memoInfo.getEndTime());
                    if (memoInfo.getHas_alarm() != null)
                        event.put(CalendarContract.Events.HAS_ALARM, memoInfo.getHas_alarm());//设置有闹钟提醒
                    event.put(CalendarContract.Events.EVENT_TIMEZONE, tz.getID());//这个是时区，必须有
                    @SuppressLint("MissingPermission") Uri newEvent = context.getContentResolver().insert(CalendarContract.Events.CONTENT_URI, event); //添加事件
                    if (newEvent == null) { //添加日历事件失败直接返回
                        if (callback != null) {
                            callback.memoCallback(null, "保存信息失败");
                        }
                        return;
                    }
                    if (memoInfo.getHas_alarm() == 1) {
                        //事件提醒的设定
                        Long event_id = ContentUris.parseId(newEvent);
                        addReminders(context, event_id, memoInfo, callback);
                    }
                    if (callback != null) {
                        callback.memoCallback(null, Config.Message_Success);
                    }
                }
            }.start();
        } else {
            if (callback != null) {
                callback.memoCallback(null, "没有权限");
            }
        }
    }

    private static void addReminders(Activity context, Long event_id, MemoInfo memoInfo, MemoCallback callback) {
        if (checkSelfPermission(context)) {
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Reminders.EVENT_ID, event_id);
            if (memoInfo.getMinutes() != null)
                values.put(CalendarContract.Reminders.MINUTES, memoInfo.getMinutes());// 提前previousDate天有提醒
            values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
            @SuppressLint("MissingPermission") Uri uri = context.getContentResolver().insert(CalendarContract.Reminders.CONTENT_URI, values);
            if (uri == null) { //添加事件提醒失败直接返回
//            if (callback != null) {
//                callback.addMemo("添加事件提醒失败");
//            }
                return;
            }
            LogUtils.e("添加闹钟成功");
        }
    }

    private static void updateReminders(Context context, MemoInfo memoInfo, Long reminders_id, MemoCallback callback) {
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Reminders.EVENT_ID, memoInfo.getMemoInfoId());
        if (memoInfo.getMinutes() != null)
            values.put(CalendarContract.Reminders.MINUTES, memoInfo.getMinutes());// 提前previousDate天有提醒
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);

        Uri updateUri = ContentUris.withAppendedId(CalendarContract.Reminders.CONTENT_URI, reminders_id);
        int rows = context.getContentResolver().update(updateUri, values, null, null);
        if (rows == -1) { //事件更新失败
//            if (callback != null) {
//                callback.memoCallback(null,"保存失败");
//            }
            return;
        }
        LogUtils.e("更新闹钟成功");
    }

    private static void deleteReminders(Context context, Long reminders_id, MemoCallback callback) {
        Uri updateUri = ContentUris.withAppendedId(CalendarContract.Reminders.CONTENT_URI, reminders_id);
        int rows = context.getContentResolver().delete(updateUri, null, null);
        if (rows == -1) { //事件更新失败
//            if (callback != null) {
//                callback.memoCallback(null,"保存失败");
//            }
            return;
        }
        LogUtils.e("删除闹钟成功");
    }

    /**
     * 更新日历事件
     */
    public static void updateCalendarEvent(final Activity context, final MemoInfo memoInfo, final MemoCallback callback) {
        if (checkSelfPermission(context)) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    if (context == null) {
                        if (callback != null) {
                            callback.memoCallback(null, "上下文为空");
                        }
                        return;
                    }
                    //添加日历事件
                    TimeZone tz = TimeZone.getDefault();//获取默认时区
                    ContentValues event = new ContentValues();
                    if (!TextUtils.isEmpty(memoInfo.getTitle()))
                        event.put(CalendarContract.Events.TITLE, memoInfo.getTitle());
                    if (!TextUtils.isEmpty(memoInfo.getDescription()))
                        event.put(CalendarContract.Events.DESCRIPTION, memoInfo.getDescription());
//                event.put(CalendarContract.Events.CALENDAR_ID, calId); //插入账户的id
                    if (!TextUtils.isEmpty(memoInfo.getLocation()))
                        event.put(CalendarContract.Events.EVENT_LOCATION, memoInfo.getLocation());
                    if (memoInfo.getStartTime() != null && memoInfo.getStartTime() != 0)
                        event.put(CalendarContract.Events.DTSTART, memoInfo.getStartTime());
                    if (memoInfo.getEndTime() != null && memoInfo.getEndTime() != 0)
                        event.put(CalendarContract.Events.DTEND, memoInfo.getEndTime());
                    LogUtils.e("has_alarm:" + memoInfo.getHas_alarm());
                    if (memoInfo.getHas_alarm() != null) {
                        event.put(CalendarContract.Events.HAS_ALARM, memoInfo.getHas_alarm());//设置有闹钟提醒
                    } else {
                        event.put(CalendarContract.Events.HAS_ALARM, 0l);//设置有闹钟提醒
                    }
                    event.put(CalendarContract.Events.EVENT_TIMEZONE, tz.getID());//这个是时区，必须有

                    Uri updateUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, memoInfo.getMemoInfoId());
                    int rows = context.getContentResolver().update(updateUri, event, null, null);
                    if (rows == -1) { //事件更新失败
                        if (callback != null) {
                            callback.memoCallback(null, "保存失败");
                        }
                        return;
                    }
                    String selection = CalendarContract.Reminders.EVENT_ID + "=?";
                    String[] selectionArgs = new String[]{memoInfo.getMemoInfoId().toString()};
                    @SuppressLint("MissingPermission") Cursor remindersCursor = context.getContentResolver().query(CalendarContract.Reminders.CONTENT_URI, null, selection, selectionArgs, null);

                    if (remindersCursor == null) { //查询返回空值
                        LogUtils.e("remindersCursor为空");
                        if (memoInfo.getHas_alarm() == 1) {
                            //添加闹钟
                            addReminders(context, memoInfo.getMemoInfoId(), memoInfo, callback);
                        }
                    } else {
                        if (remindersCursor.getCount() > 0) {
                            LogUtils.e("大于0");
                            //遍历所有事件，找到title跟需要查询的title一样的项
                            for (remindersCursor.moveToFirst(); !remindersCursor.isAfterLast(); remindersCursor.moveToNext()) {
                                Long _id = remindersCursor.getLong(remindersCursor.getColumnIndex(CalendarContract.Reminders._ID));
                                Long event_id = remindersCursor.getLong(remindersCursor.getColumnIndex(CalendarContract.Reminders.EVENT_ID));
                                LogUtils.e("\n闹钟id:" + _id + "\n事件id:" + event_id);
                                if (memoInfo.getHas_alarm() != null && memoInfo.getHas_alarm() == 1) {
                                    //修改闹钟信息
                                    updateReminders(context, memoInfo, _id, callback);
                                } else {
                                    //删除闹钟信息
                                    deleteReminders(context, _id, callback);
                                }
                            }
                        } else {
                            LogUtils.e("不大于0：" + remindersCursor.getCount());
                        }
                        if (remindersCursor != null)
                            remindersCursor.close();
                    }

                    if (callback != null) {
                        callback.memoCallback(null, Config.Message_Success);
                    }
                }
            }.start();
        } else {

            if (callback != null) {
                callback.memoCallback(null, "没有权限");
            }
        }
    }

    /**
     * 删除日历事件
     */
    public static void deleteCalendarEvent(final Activity context, final Long memoInfoId, final MemoCallback callback) {
        if (checkSelfPermission(context)) {
            new Thread() {
                @Override
                public void run() {
                    if (context == null) {
                        if (callback != null) {
                            callback.memoCallback(null, "上下文为空");
                        }
                        return;
                    }
                    if (memoInfoId != null) {
                        findCalendarEventById(context, memoInfoId, new MemoCallback() {
                            @Override
                            public void memoCallback(List<MemoInfo> memoInfoList, String message) {
                                if (Config.Message_Success.equals(message)) {
                                    if (memoInfoList != null && memoInfoList.size() > 0) {
                                        MemoInfo memoInfo = memoInfoList.get(0);
                                        if (memoInfo != null) {
                                            Long id = memoInfo.getMemoInfoId();//取得id
                                            Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, id);
                                            int rows = context.getContentResolver().delete(deleteUri, null, null);
                                            if (rows == -1) { //事件删除失败
                                                if (callback != null) {
                                                    callback.memoCallback(null, "删除失败");
                                                }
                                                return;
                                            }
                                            if (callback != null) {
                                                callback.memoCallback(null, Config.Message_Success);
                                            }
                                        } else {
                                            if (callback != null) {
                                                callback.memoCallback(null, "查无此信息");
                                            }
                                        }
                                    } else {
                                        if (callback != null) {
                                            callback.memoCallback(null, message);
                                        }
                                    }
                                }
                            }
                        });
                    } else {
                        if (callback != null) {
                            callback.memoCallback(null, "查无此id");
                        }
                    }
                }
            }.start();
        } else {

            if (callback != null) {
                callback.memoCallback(null, "没有权限");
            }
        }
    }

    /**
     * 查找日历事件
     *
     * @param context
     * @param callback
     */
    public static void findAllCalendarEvent(final Activity context, final MemoCallback callback) {
        if (checkSelfPermission(context)) {
            new Thread() {
                @Override
                public void run() {
                    if (context == null) {
                        if (callback != null) {
                            callback.memoCallback(null, "上下文为空");
                        }
                        LogUtils.e("上下文为空");
                        return;
                    }
                    @SuppressLint("MissingPermission") Cursor eventCursor = context.getContentResolver().query(CalendarContract.Events.CONTENT_URI, null, null, null, null);
                    try {
                        if (eventCursor == null) { //查询返回空值
                            LogUtils.e("查询数据为空");
                            if (callback != null) {
                                callback.memoCallback(null, "查询数据为空");
                            }
                            return;
                        }
                        if (eventCursor.getCount() > 0) {
                            //遍历所有事件，找到title跟需要查询的title一样的项
                            ArrayList<MemoInfo> list = new ArrayList<>();
                            MemoInfo memoInfo = null;

                            for (eventCursor.moveToFirst(); !eventCursor.isAfterLast(); eventCursor.moveToNext()) {
                                Long _id = eventCursor.getLong(eventCursor.getColumnIndex(CalendarContract.Events._ID));
                                String title = eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Events.TITLE));
                                String organizer = eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Events.ORGANIZER));
                                String description = eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Events.DESCRIPTION));
                                Long startTime = eventCursor.getLong(eventCursor.getColumnIndex(CalendarContract.Events.DTSTART));
                                Long endTime = eventCursor.getLong(eventCursor.getColumnIndex(CalendarContract.Events.DTEND));
                                String location = eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION));
                                Integer has_alarm = eventCursor.getInt(eventCursor.getColumnIndex(CalendarContract.Events.HAS_ALARM));

                                memoInfo = new MemoInfo(_id, title, organizer, description, startTime, endTime, location, has_alarm);
                                if (has_alarm == 1) {
                                    String selection = CalendarContract.Reminders.EVENT_ID + "=?";
                                    String[] selectionArgs = new String[]{_id.toString()};
                                    @SuppressLint("MissingPermission") Cursor remindersCursor = context.getContentResolver().query(CalendarContract.Reminders.CONTENT_URI, null, selection, selectionArgs, null);

                                    if (remindersCursor == null) { //查询返回空值
                                        LogUtils.e("remindersCursor为空");
                                    } else {
                                        if (remindersCursor.getCount() > 0) {
                                            //遍历所有事件，找到title跟需要查询的title一样的项
                                            for (remindersCursor.moveToFirst(); !remindersCursor.isAfterLast(); remindersCursor.moveToNext()) {
                                                Long minutes = remindersCursor.getLong(remindersCursor.getColumnIndex(CalendarContract.Reminders.MINUTES));
                                                Integer method = remindersCursor.getInt(remindersCursor.getColumnIndex(CalendarContract.Reminders.METHOD));
                                                memoInfo.setMinutes(minutes);
                                                memoInfo.setMethod(method);
                                            }
                                        } else {
                                            LogUtils.e("不大于0：" + remindersCursor.getCount());
                                        }
                                        if (remindersCursor != null)
                                            remindersCursor.close();
                                    }
                                }
                                list.add(0, memoInfo);
                            }

                            if (callback != null) {
                                callback.memoCallback(list, Config.Message_Success);
                            }
                        } else {
                            if (callback != null) {
                                callback.memoCallback(null, "查询数据为空");
                            }
                        }
                    } finally {
                        if (eventCursor != null) {
                            eventCursor.close();
                        }
                    }
                }
            }.start();
        } else {

            if (callback != null) {
                callback.memoCallback(null, "没有权限");
            }
        }
    }

    public static void findCalendarEventById(final Activity context, final Long eventId, final MemoCallback callback) {
        if (checkSelfPermission(context)) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    if (context == null) {
                        if (callback != null) {
                            callback.memoCallback(null, "查询数据为空");
                        }
                        return;
                    }
                    if (eventId == null) {
                        if (callback != null) {
                            callback.memoCallback(null, "查询数据为空");
                        }
                        return;
                    }
                    String selection1 = CalendarContract.Events._ID + "=?";
                    String[] selectionArgs1 = new String[]{eventId.toString()};
                    @SuppressLint("MissingPermission") Cursor eventCursor = context.getContentResolver().query(CalendarContract.Events.CONTENT_URI, null, selection1, selectionArgs1, null);
                    try {
                        if (eventCursor == null) { //查询返回空值
                            LogUtils.e("查询数据为空");
                            if (callback != null) {
                                callback.memoCallback(null, "查询数据为空");
                            }
                        }
                        if (eventCursor.getCount() > 0) {
                            //遍历所有事件，找到title跟需要查询的title一样的项
                            for (eventCursor.moveToFirst(); !eventCursor.isAfterLast(); eventCursor.moveToNext()) {
                                Long _id = eventCursor.getLong(eventCursor.getColumnIndex(CalendarContract.Events._ID));
                                String title = eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Events.TITLE));
                                String organizer = eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Events.ORGANIZER));
                                String description = eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Events.DESCRIPTION));
                                Long startTime = eventCursor.getLong(eventCursor.getColumnIndex(CalendarContract.Events.DTSTART));
                                Long endTime = eventCursor.getLong(eventCursor.getColumnIndex(CalendarContract.Events.DTEND));
                                String location = eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION));
                                Integer has_alarm = eventCursor.getInt(eventCursor.getColumnIndex(CalendarContract.Events.HAS_ALARM));

                                MemoInfo memoInfo = new MemoInfo(_id, title, organizer, description, startTime, endTime, location, has_alarm);
                                if (has_alarm == 1) {
                                    String selection = CalendarContract.Reminders.EVENT_ID + "=?";
                                    String[] selectionArgs = new String[]{_id.toString()};
                                    @SuppressLint("MissingPermission") Cursor remindersCursor = context.getContentResolver().query(CalendarContract.Reminders.CONTENT_URI, null, selection, selectionArgs, null);

                                    if (remindersCursor == null) { //查询返回空值
                                        LogUtils.e("remindersCursor为空");
                                    } else {
                                        if (remindersCursor.getCount() > 0) {
                                            LogUtils.e("大于0");
                                            //遍历所有事件，找到title跟需要查询的title一样的项
                                            for (remindersCursor.moveToFirst(); !remindersCursor.isAfterLast(); remindersCursor.moveToNext()) {
                                                LogUtils.e("进来了");
                                                Long minutes = remindersCursor.getLong(remindersCursor.getColumnIndex(CalendarContract.Reminders.MINUTES));
                                                Integer method = remindersCursor.getInt(remindersCursor.getColumnIndex(CalendarContract.Reminders.METHOD));
                                                memoInfo.setMinutes(minutes);
                                                memoInfo.setMethod(method);
                                            }
                                        } else {
                                            LogUtils.e("不大于0：" + remindersCursor.getCount());
                                        }
                                        if (remindersCursor != null)
                                            remindersCursor.close();
                                    }
                                }
                                if (callback != null) {
                                    List<MemoInfo> memoInfoList = new ArrayList<>();
                                    memoInfoList.add(memoInfo);
                                    callback.memoCallback(memoInfoList, Config.Message_Success);
                                }
                                return;
                            }
                            if (callback != null) {
                                callback.memoCallback(null, "查询数据为空");
                            }
                        } else {
                            if (callback != null) {
                                callback.memoCallback(null, "查询数据为空");
                            }
                        }
                    } finally {
                        if (eventCursor != null) {
                            eventCursor.close();
                        }
                    }
                }
            }.start();
        } else {
            if (callback != null) {
                callback.memoCallback(null, "没有权限");
            }
        }
    }

    public static void findCalendarEventByDate(final Activity context, final Long start, final Long end, final MemoCallback callback) {
        if (checkSelfPermission(context)) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    if (context == null) {
                        if (callback != null) {
                            callback.memoCallback(null, "查询数据为空");
                        }
                        return;
                    }
                    if (start == null || end == null) {
                        if (callback != null) {
                            callback.memoCallback(null, "查询数据为空");
                        }
                        return;
                    }
                    @SuppressLint("MissingPermission") Cursor eventCursor = context.getContentResolver().query(CalendarContract.Events.CONTENT_URI, null, null, null, null);
                    try {
                        if (eventCursor == null) { //查询返回空值
                            LogUtils.e("查询数据为空");
                            if (callback != null) {
                                callback.memoCallback(null, "查询数据为空");
                            }
                        }
                        if (eventCursor.getCount() > 0) {
                            //遍历所有事件，找到title跟需要查询的title一样的项
                            List<MemoInfo> list = new ArrayList<>();
                            MemoInfo memoInfo = null;
                            for (eventCursor.moveToFirst(); !eventCursor.isAfterLast(); eventCursor.moveToNext()) {

                                Long startTime = eventCursor.getLong(eventCursor.getColumnIndex(CalendarContract.Events.DTSTART));
                                if (startTime >= start && startTime <= end) {
                                    Long endTime = eventCursor.getLong(eventCursor.getColumnIndex(CalendarContract.Events.DTEND));
                                    Long _id = eventCursor.getLong(eventCursor.getColumnIndex(CalendarContract.Events._ID));
                                    String title = eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Events.TITLE));
                                    String organizer = eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Events.ORGANIZER));
                                    String description = eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Events.DESCRIPTION));
                                    String location = eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION));
                                    Integer has_alarm = eventCursor.getInt(eventCursor.getColumnIndex(CalendarContract.Events.HAS_ALARM));

                                    memoInfo = new MemoInfo(_id, title, organizer, description, startTime, endTime, location, has_alarm);
                                    if (has_alarm == 1) {
                                        String selection = CalendarContract.Reminders.EVENT_ID + "=?";
                                        String[] selectionArgs = new String[]{_id.toString()};
                                        @SuppressLint("MissingPermission") Cursor remindersCursor = context.getContentResolver().query(CalendarContract.Reminders.CONTENT_URI, null, selection, selectionArgs, null);

                                        if (remindersCursor == null) { //查询返回空值
                                            LogUtils.e("remindersCursor为空");
                                        } else {
                                            if (remindersCursor.getCount() > 0) {
                                                LogUtils.e("大于0");
                                                //遍历所有事件，找到title跟需要查询的title一样的项
                                                for (remindersCursor.moveToFirst(); !remindersCursor.isAfterLast(); remindersCursor.moveToNext()) {
                                                    LogUtils.e("进来了");
                                                    Long minutes = remindersCursor.getLong(remindersCursor.getColumnIndex(CalendarContract.Reminders.MINUTES));
                                                    Integer method = remindersCursor.getInt(remindersCursor.getColumnIndex(CalendarContract.Reminders.METHOD));
                                                    memoInfo.setMinutes(minutes);
                                                    memoInfo.setMethod(method);
                                                }
                                            } else {
                                                LogUtils.e("不大于0：" + remindersCursor.getCount());
                                            }
                                            if (remindersCursor != null)
                                                remindersCursor.close();
                                        }
                                    }
                                    list.add(memoInfo);
                                }
                            }

                            if (callback != null) {
                                callback.memoCallback(list, Config.Message_Success);
                            }
                        } else {
                            if (callback != null) {
                                callback.memoCallback(null, "查询数据为空");
                            }
                        }
                    } finally {
                        if (eventCursor != null) {
                            eventCursor.close();
                        }
                    }
                }
            }.start();
        } else {

            if (callback != null) {
                callback.memoCallback(null, "没有权限");
            }
        }
    }
}

/*    id21
    eventId:21
    title皮皮
    ORGANIZERPGONE@pgone.com
            EVENT_LOCATION皮皮
    DESCRIPTION第一个添加测试
            DTSTART1533265829000
    DTEND1533266429000
    EVENT_TIMEZONEAsia/Shanghai
    EVENT_END_TIMEZONEAsia/Shanghai
            DURATIONnull
    HAS_ALARM1
            RRULEnull
    RDATEnull
            AVAILABILITY0
    GUESTS_CAN_MODIFY1minutes:5
    method:1*/


//    Calendar mCalendar = Calendar.getInstance();
//        mCalendar.setTimeInMillis(memoInfo.getStartTime());//设置开始时间
//        mCalendar.setTimeInMillis(memoInfo.getEndTime());//设置终止时间，开始时间加10分钟
//        long start = mCalendar.getTime().getTime();
//        mCalendar.setTimeInMillis(start + 10 * 60 * 1000);//设置终止时间，开始时间加10分钟
//        long end = mCalendar.getTime().getTime();


//    String EVENT_TIMEZONE = eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Events.EVENT_TIMEZONE));
//    String eventTitle = eventCursor.getString(eventCursor.getColumnIndex("title"));
//    String ORGANIZER = eventCursor.getString(eventCursor.getColumnIndex("ORGANIZER"));
//    String DURATION = eventCursor.getString(eventCursor.getColumnIndex("DURATION"));
//    //                    String ALL_DAY = eventCursor.getString(eventCursor.getColumnIndex("ALL_DAY"));
//    String DESCRIPTION = eventCursor.getString(eventCursor.getColumnIndex("DESCRIPTION"));
//    String RRULE = eventCursor.getString(eventCursor.getColumnIndex("RRULE"));
//    String RDATE = eventCursor.getString(eventCursor.getColumnIndex("RDATE"));
//    String AVAILABILITY = eventCursor.getString(eventCursor.getColumnIndex("AVAILABILITY"));
//    String DTSTART = eventCursor.getString(eventCursor.getColumnIndex("DTSTART"));
//    String DTEND = eventCursor.getString(eventCursor.getColumnIndex("DTEND"));
//    //                            String EVENT_TIMEZONE = eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Calendars.CAN_MODIFY_TIME_ZONE));
//    String EVENT_END_TIMEZONE = eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Calendars.CALENDAR_TIME_ZONE));
//    String GUESTS_CAN_MODIFY = eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Calendars.CAN_MODIFY_TIME_ZONE));
//    Long id = eventCursor.getLong(eventCursor.getColumnIndex(CalendarContract.Calendars._ID));
//
//                        LogUtils.e("\nid" + id + "\neventId:" + eventId + "\ntitle" + eventTitle + "\nORGANIZER" + ORGANIZER + "\nEVENT_LOCATION" + EVENT_LOCATION + "\nDESCRIPTION" + DESCRIPTION +
//            "\nDTSTART" + DTSTART + "\nDTEND" + DTEND + "\nEVENT_TIMEZONE" + EVENT_TIMEZONE + "\nEVENT_END_TIMEZONE" + EVENT_END_TIMEZONE +
//            "\nDURATION" + DURATION + "\nHAS_ALARM" + HAS_ALARM + "\nRRULE" + RRULE + "\nRDATE" + RDATE +
//                          /*  "\nGUESTS_CAN_INVITE_OTHERS" + GUESTS_CAN_INVITE_OTHERS + "\nGUESTS_CAN_SEE_GUESTS" + GUESTS_CAN_SEE_GUESTS +"\nCalenderName" + CalenderName +*/
//            "\nAVAILABILITY" + AVAILABILITY + "\nGUESTS_CAN_MODIFY" + GUESTS_CAN_MODIFY/*+"\nACCOUNT_TYPE_LOCAL"+ACCOUNT_TYPE_LOCAL*/);
//    String eventTitle = eventCursor.getString(eventCursor.getColumnIndex("title"));
//    String ORGANIZER = eventCursor.getString(eventCursor.getColumnIndex("ORGANIZER"));
//    String DURATION = eventCursor.getString(eventCursor.getColumnIndex("DURATION"));
//    //                    String ALL_DAY = eventCursor.getString(eventCursor.getColumnIndex("ALL_DAY"));
//    String DESCRIPTION = eventCursor.getString(eventCursor.getColumnIndex("DESCRIPTION"));
//    String RRULE = eventCursor.getString(eventCursor.getColumnIndex("RRULE"));
//    String RDATE = eventCursor.getString(eventCursor.getColumnIndex("RDATE"));
//    String AVAILABILITY = eventCursor.getString(eventCursor.getColumnIndex("AVAILABILITY"));
//    String DTSTART = eventCursor.getString(eventCursor.getColumnIndex("DTSTART"));
//    String DTEND = eventCursor.getString(eventCursor.getColumnIndex("DTEND"));
//    String EVENT_TIMEZONE = eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Calendars.CAN_MODIFY_TIME_ZONE));
//    String EVENT_END_TIMEZONE = eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Calendars.CALENDAR_TIME_ZONE));
//    String GUESTS_CAN_MODIFY = eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Calendars.CAN_MODIFY_TIME_ZONE));
//    Long id = eventCursor.getLong(eventCursor.getColumnIndex(CalendarContract.Calendars._ID));
//
//                    LogUtils.e("id" + id + "\ntitle" + eventTitle + "\nORGANIZER" + ORGANIZER /*+ "\nEVENT_LOCATION" + EVENT_LOCATION */ + "\nDESCRIPTION" + DESCRIPTION +
//            "\nDTSTART" + DTSTART + "\nDTEND" + DTEND + "\nEVENT_TIMEZONE" + EVENT_TIMEZONE + "\nEVENT_END_TIMEZONE" + EVENT_END_TIMEZONE +
//            "\nDURATION" + DURATION /*+ "\nALL_DAY" + ALL_DAY*/ + "\nRRULE" + RRULE + "\nRDATE" + RDATE +
//                          /*  "\nGUESTS_CAN_INVITE_OTHERS" + GUESTS_CAN_INVITE_OTHERS + "\nGUESTS_CAN_SEE_GUESTS" + GUESTS_CAN_SEE_GUESTS +"\nCalenderName" + CalenderName +*/
//            "\nAVAILABILITY" + AVAILABILITY + "\nGUESTS_CAN_MODIFY" + GUESTS_CAN_MODIFY/*+"\nACCOUNT_TYPE_LOCAL"+ACCOUNT_TYPE_LOCAL*/);
//            ACCOUNT_NAME,ACCOUNT_TYPE, _SYNC_ID,DIRTY, MUTATORS, OWNER_ACCOUNT, MAX_REMINDERS, ALLOWED_REMINDERS,CAN_MODIFY_TIME_ZONE,CAN_ORGANIZER_RESPOND,
//                    CAN_PARTIALLY_UPDATE,CALENDAR_LOCATION, CALENDAR_TIME_ZONE, CALENDAR_ACCESS_LEVEL, DELETED,
//                    String ACCOUNT_TYPE_LOCAL = eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.ACCOUNT_TYPE_LOCAL));
//                    String ALL_DAY = eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.EXTRA_EVENT_ALL_DAY));
//                    String DTSTART = eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.EXTRA_EVENT_BEGIN_TIME));
//                    String DTEND = eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.EXTRA_EVENT_END_TIME));
//                    String EVENT_LOCATION = eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Calendars.CALENDAR_LOCATION));
//                    String GUESTS_CAN_INVITE_OTHERS = eventCursor.getString(eventCursor.getColumnIndex("GUESTS_CAN_INVITE_OTHERS"));
//                    String GUESTS_CAN_SEE_GUESTS = eventCursor.getString(eventCursor.getColumnIndex("GUESTS_CAN_SEE_GUESTS"));
//                    String CalenderName = eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Calendars.NAME));

