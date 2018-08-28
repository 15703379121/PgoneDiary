package king.rapper.pgone.pgonediary.entity;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

import king.rapper.pgone.pgonediary.dao.DaoSession;
import king.rapper.pgone.pgonediary.dao.DiaryInfoDao;
import king.rapper.pgone.pgonediary.dao.RecordingDao;
import king.rapper.pgone.pgonediary.dao.NoteInfoDao;
import king.rapper.pgone.pgonediary.dao.NoteRecordingDao;

/**
 * @ Create_time: 2018/7/25 on 11:22.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
@Entity
public class NoteInfo {


    @Id(autoincrement = true) // id自增长
    private Long NoteInfoId; // 日记id
    private Long time;
    private String content;
    private Integer tag;
    private Boolean isSelect = false;

    public Boolean getSelect() {
        return isSelect;
    }

    public void setSelect(Boolean select) {
        isSelect = select;
    }

    @ToMany(referencedJoinProperty = "noteJoinRecordingId")
    private List<NoteRecording> recordingList;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 845113378)
    private transient NoteInfoDao myDao;

    @Generated(hash = 558034387)
    public NoteInfo(Long NoteInfoId, Long time, String content, Integer tag,
            Boolean isSelect) {
        this.NoteInfoId = NoteInfoId;
        this.time = time;
        this.content = content;
        this.tag = tag;
        this.isSelect = isSelect;
    }

    @Generated(hash = 1097220926)
    public NoteInfo() {
    }

    public Long getNoteInfoId() {
        return this.NoteInfoId;
    }

    public void setNoteInfoId(Long NoteInfoId) {
        this.NoteInfoId = NoteInfoId;
    }

    public Long getTime() {
        return this.time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getTag() {
        return this.tag;
    }

    public void setTag(Integer tag) {
        this.tag = tag;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1858803492)
    public List<NoteRecording> getRecordingList() {
        if (recordingList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            NoteRecordingDao targetDao = daoSession.getNoteRecordingDao();
            List<NoteRecording> recordingListNew = targetDao
                    ._queryNoteInfo_RecordingList(NoteInfoId);
            synchronized (this) {
                if (recordingList == null) {
                    recordingList = recordingListNew;
                }
            }
        }
        return recordingList;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1988649878)
    public synchronized void resetRecordingList() {
        recordingList = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 889194310)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getNoteInfoDao() : null;
    }

    @Override
    public String toString() {
        return "NoteInfo{" +
                "NoteInfoId=" + NoteInfoId +
                ", time=" + time +
                ", content='" + content + '\'' +
                ", tag=" + tag +
                ", recordingList=" + recordingList +
                ", daoSession=" + daoSession +
                ", myDao=" + myDao +
                '}';
    }

    public Boolean getIsSelect() {
        return this.isSelect;
    }

    public void setIsSelect(Boolean isSelect) {
        this.isSelect = isSelect;
    }
}
