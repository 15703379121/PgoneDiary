package king.rapper.pgone.pgonediary.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 录音文件的实体类
 * <p>
 */
@Entity
public class NoteRecording {
    @Id(autoincrement = true) // id自增长
    private Long noteRecordingId;
    private Long noteJoinRecordingId;
    private String name;
    private String filePath;
    private Long length;
    private Long time;
    @Generated(hash = 1232661795)
    public NoteRecording(Long noteRecordingId, Long noteJoinRecordingId,
            String name, String filePath, Long length, Long time) {
        this.noteRecordingId = noteRecordingId;
        this.noteJoinRecordingId = noteJoinRecordingId;
        this.name = name;
        this.filePath = filePath;
        this.length = length;
        this.time = time;
    }
    @Generated(hash = 55981552)
    public NoteRecording() {
    }
    public Long getNoteRecordingId() {
        return this.noteRecordingId;
    }
    public void setNoteRecordingId(Long noteRecordingId) {
        this.noteRecordingId = noteRecordingId;
    }
    public Long getNoteJoinRecordingId() {
        return this.noteJoinRecordingId;
    }
    public void setNoteJoinRecordingId(Long noteJoinRecordingId) {
        this.noteJoinRecordingId = noteJoinRecordingId;
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

}
