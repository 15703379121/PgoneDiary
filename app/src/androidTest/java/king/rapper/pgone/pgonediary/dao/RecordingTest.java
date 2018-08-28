package king.rapper.pgone.pgonediary.dao;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import king.rapper.pgone.pgonediary.entity.Recording;
import king.rapper.pgone.pgonediary.dao.RecordingDao;

public class RecordingTest extends AbstractDaoTestLongPk<RecordingDao, Recording> {

    public RecordingTest() {
        super(RecordingDao.class);
    }

    @Override
    protected Recording createEntity(Long key) {
        Recording entity = new Recording();
        entity.setRecordingId(key);
        return entity;
    }

}
