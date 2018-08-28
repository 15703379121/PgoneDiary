package king.rapper.pgone.pgonediary.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @ Create_time: 2018/7/21 on 11:46.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
@Entity
public class MoodIcon{
    @Id(autoincrement = true) // id自增长
    private Long moodIconId; // 心情图片id
    protected String url;
    protected String type;
    protected Long time;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public MoodIcon(String url){
        this.url = url;
    }

    public MoodIcon(String url, String type, Long time) {
        this.url = url;
        this.type = type;
        this.time = time;
    }

    @Generated(hash = 18780014)
    public MoodIcon(Long moodIconId, String url, String type, Long time) {
        this.moodIconId = moodIconId;
        this.url = url;
        this.type = type;
        this.time = time;
    }

    @Generated(hash = 563592867)
    public MoodIcon() {
    }

    public Long getMoodIconId() {
        return moodIconId;
    }

    public void setMoodIconId(Long moodIconId) {
        this.moodIconId = moodIconId;
    }
}
