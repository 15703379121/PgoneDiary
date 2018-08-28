package king.rapper.pgone.pgonediary.entity;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import java.util.List;

import king.rapper.pgone.pgonediary.UI.MemoListActivity;
import king.rapper.pgone.pgonediary.adapter.MemoRvAdapter;
import king.rapper.pgone.pgonediary.util.ToastUtil;

/**
 * @ Create_time: 2018/8/2 on 11:20.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
public class MemoInfo {
    private Long memoInfoId;//id
    private String title;//标题
    private String organizer;//事件组
    private String description;//描述
    private Long startTime;//开始时间
    private Long endTime;//结束时间
    private String location;//地点
    private Integer has_alarm;//是否开启闹钟
    private Integer method;//报警的方法
    private Long minutes;//提前几分钟提醒

    public MemoInfo() {
    }

    public MemoInfo(Long memoInfoId, String title, String organizer, String description, Long startTime, Long endTime, String location, Integer has_alarm) {
        this.memoInfoId = memoInfoId;
        this.title = title;
        this.organizer = organizer;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.has_alarm = has_alarm;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getHas_alarm() {
        return has_alarm;
    }

    public void setHas_alarm(Integer has_alarm) {
        this.has_alarm = has_alarm;
    }

    public Integer getMethod() {
        return method;
    }

    public void setMethod(Integer method) {
        this.method = method;
    }

    public Long getMinutes() {
        return minutes;
    }

    public void setMinutes(Long minutes) {
        this.minutes = minutes;
    }

    public Long getMemoInfoId() {
        return memoInfoId;
    }

    public void setMemoInfoId(Long memoInfoId) {
        this.memoInfoId = memoInfoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
}
