package king.rapper.pgone.pgonediary.dao;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import king.rapper.pgone.pgonediary.entity.NoteRecording;
import king.rapper.pgone.pgonediary.dao.NoteRecordingDao;

public class NoteRecordingTest extends AbstractDaoTestLongPk<NoteRecordingDao, NoteRecording> {

    public NoteRecordingTest() {
        super(NoteRecordingDao.class);
    }

    @Override
    protected NoteRecording createEntity(Long key) {
        NoteRecording entity = new NoteRecording();
        entity.setNoteRecordingId(key);
        return entity;
    }

}
