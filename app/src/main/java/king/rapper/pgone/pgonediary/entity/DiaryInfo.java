package king.rapper.pgone.pgonediary.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import king.rapper.pgone.pgonediary.dao.DaoSession;
import king.rapper.pgone.pgonediary.dao.RecordingDao;
import king.rapper.pgone.pgonediary.dao.DiaryInfoDao;

/**
 * @ Create_time: 2018/7/25 on 11:22.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
@Entity
public class DiaryInfo {


    @Id(autoincrement = true) // id自增长
    private Long diaryInfoId; // 日记id
    private String title;
    private Long time;
    private String mood;
    private String weather;
    private String content;
    private String background;
    private Integer tag;

    @ToMany(referencedJoinProperty = "diaryJoinRecordingId")
    private List<Recording> recordingList;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 731371862)
    private transient DiaryInfoDao myDao;

    @Generated(hash = 574046133)
    public DiaryInfo(Long diaryInfoId, String title, Long time, String mood,
            String weather, String content, String background, Integer tag) {
        this.diaryInfoId = diaryInfoId;
        this.title = title;
        this.time = time;
        this.mood = mood;
        this.weather = weather;
        this.content = content;
        this.background = background;
        this.tag = tag;
    }

    @Generated(hash = 1385338142)
    public DiaryInfo() {
    }

    public Long getDiaryInfoId() {
        return this.diaryInfoId;
    }

    public void setDiaryInfoId(Long diaryInfoId) {
        this.diaryInfoId = diaryInfoId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getTime() {
        return this.time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getMood() {
        return this.mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getWeather() {
        return this.weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBackground() {
        return this.background;
    }

    public void setBackground(String background) {
        this.background = background;
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
    @Generated(hash = 2036934072)
    public List<Recording> getRecordingList() {
        if (recordingList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RecordingDao targetDao = daoSession.getRecordingDao();
            List<Recording> recordingListNew = targetDao
                    ._queryDiaryInfo_RecordingList(diaryInfoId);
            synchronized (this) {
                if (recordingList == null) {
                    recordingList = recordingListNew;
                }
            }
        }
        return recordingList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 256551761)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDiaryInfoDao() : null;
    }

    @Override
    public String toString() {
        return "DiaryInfo{" +
                "diaryInfoId=" + diaryInfoId +
                ", title='" + title + '\'' +
                ", time=" + time +
                ", mood='" + mood + '\'' +
                ", weather='" + weather + '\'' +
                ", content='" + content + '\'' +
                ", background='" + background + '\'' +
                ", tag=" + tag +
                ", recordingList=" + recordingList +
                ", daoSession=" + daoSession +
                ", myDao=" + myDao +
                '}';
    }
}
