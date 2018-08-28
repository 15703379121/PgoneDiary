package king.rapper.pgone.pgonediary.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * @ Create_time: 2018/8/11 on 14:43.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
@Entity
public class MessageInfo {
    @Id(autoincrement = true) // id自增长
    private Long MessageInfoId; // 日记id
    private String extraMsgId;
    private String extraTitle;
    private String extraMessage;
    private String extraNotificationId;
    private String extraNotificationTile;
    private String extraAlter;
    private String extraExtra;
    private String extraBigText;
    private String extraRrichpushHtmlPath;
    private String extraRrichpushHtmlRes;
    private String extraBigPicPath;
    private String extraInbox;
    private String extraNotiPriority;
    private Integer tag;
    private Long time;

    @Generated(hash = 1009508908)
    public MessageInfo(Long MessageInfoId, String extraMsgId, String extraTitle, String extraMessage,
            String extraNotificationId, String extraNotificationTile, String extraAlter,
            String extraExtra, String extraBigText, String extraRrichpushHtmlPath,
            String extraRrichpushHtmlRes, String extraBigPicPath, String extraInbox,
            String extraNotiPriority, Integer tag, Long time) {
        this.MessageInfoId = MessageInfoId;
        this.extraMsgId = extraMsgId;
        this.extraTitle = extraTitle;
        this.extraMessage = extraMessage;
        this.extraNotificationId = extraNotificationId;
        this.extraNotificationTile = extraNotificationTile;
        this.extraAlter = extraAlter;
        this.extraExtra = extraExtra;
        this.extraBigText = extraBigText;
        this.extraRrichpushHtmlPath = extraRrichpushHtmlPath;
        this.extraRrichpushHtmlRes = extraRrichpushHtmlRes;
        this.extraBigPicPath = extraBigPicPath;
        this.extraInbox = extraInbox;
        this.extraNotiPriority = extraNotiPriority;
        this.tag = tag;
        this.time = time;
    }

    @Generated(hash = 1292770546)
    public MessageInfo() {
    }

    public Long getMessageInfoId() {
        return this.MessageInfoId;
    }

    public void setMessageInfoId(Long MessageInfoId) {
        this.MessageInfoId = MessageInfoId;
    }

    public String getExtraMsgId() {
        return this.extraMsgId;
    }

    public void setExtraMsgId(String extraMsgId) {
        this.extraMsgId = extraMsgId;
    }

    public String getExtraTitle() {
        return this.extraTitle;
    }

    public void setExtraTitle(String extraTitle) {
        this.extraTitle = extraTitle;
    }

    public String getExtraMessage() {
        return this.extraMessage;
    }

    public void setExtraMessage(String extraMessage) {
        this.extraMessage = extraMessage;
    }

    public String getExtraNotificationId() {
        return this.extraNotificationId;
    }

    public void setExtraNotificationId(String extraNotificationId) {
        this.extraNotificationId = extraNotificationId;
    }

    public String getExtraNotificationTile() {
        return this.extraNotificationTile;
    }

    public void setExtraNotificationTile(String extraNotificationTile) {
        this.extraNotificationTile = extraNotificationTile;
    }

    public String getExtraAlter() {
        return this.extraAlter;
    }

    public void setExtraAlter(String extraAlter) {
        this.extraAlter = extraAlter;
    }

    public String getExtraExtra() {
        return this.extraExtra;
    }

    public void setExtraExtra(String extraExtra) {
        this.extraExtra = extraExtra;
    }

    public String getExtraBigText() {
        return this.extraBigText;
    }

    public void setExtraBigText(String extraBigText) {
        this.extraBigText = extraBigText;
    }

    public String getExtraRrichpushHtmlPath() {
        return this.extraRrichpushHtmlPath;
    }

    public void setExtraRrichpushHtmlPath(String extraRrichpushHtmlPath) {
        this.extraRrichpushHtmlPath = extraRrichpushHtmlPath;
    }

    public String getExtraRrichpushHtmlRes() {
        return this.extraRrichpushHtmlRes;
    }

    public void setExtraRrichpushHtmlRes(String extraRrichpushHtmlRes) {
        this.extraRrichpushHtmlRes = extraRrichpushHtmlRes;
    }

    public String getExtraBigPicPath() {
        return this.extraBigPicPath;
    }

    public void setExtraBigPicPath(String extraBigPicPath) {
        this.extraBigPicPath = extraBigPicPath;
    }

    public String getExtraInbox() {
        return this.extraInbox;
    }

    public void setExtraInbox(String extraInbox) {
        this.extraInbox = extraInbox;
    }

    public String getExtraNotiPriority() {
        return this.extraNotiPriority;
    }

    public void setExtraNotiPriority(String extraNotiPriority) {
        this.extraNotiPriority = extraNotiPriority;
    }

    public Integer getTag() {
        return this.tag;
    }

    public void setTag(Integer tag) {
        this.tag = tag;
    }

    public Long getTime() {
        return this.time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
