package king.rapper.pgone.pgonediary.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 录音文件的实体类
 * <p>
 */
@Entity
public class Recording {
    @Id(autoincrement = true) // id自增长
    private Long recordingId;
    private Long diaryJoinRecordingId;
    private String name;
    private String filePath;
    private Long length;
    private Long time;
    @Generated(hash = 166309799)
    public Recording(Long recordingId, Long diaryJoinRecordingId, String name,
            String filePath, Long length, Long time) {
        this.recordingId = recordingId;
        this.diaryJoinRecordingId = diaryJoinRecordingId;
        this.name = name;
        this.filePath = filePath;
        this.length = length;
        this.time = time;
    }
    @Generated(hash = 979247465)
    public Recording() {
    }
    public Long getRecordingId() {
        return this.recordingId;
    }
    public void setRecordingId(Long recordingId) {
        this.recordingId = recordingId;
    }
    public Long getDiaryJoinRecordingId() {
        return this.diaryJoinRecordingId;
    }
    public void setDiaryJoinRecordingId(Long diaryJoinRecordingId) {
        this.diaryJoinRecordingId = diaryJoinRecordingId;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getFilePath() {
        return this.filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public Long getLength() {
        return this.length;
    }
    public void setLength(Long length) {
        this.length = length;
    }
    public Long getTime() {
        return this.time;
    }
    public void setTime(Long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Recording{" +
                "recordingId=" + recordingId +
                ", diaryJoinRecordingId=" + diaryJoinRecordingId +
                ", name='" + name + '\'' +
                ", filePath='" + filePath + '\'' +
                ", length=" + length +
                ", time=" + time +
                '}';
    }
}
