package king.rapper.pgone.pgonediary.dao;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import king.rapper.pgone.pgonediary.entity.NoteInfo;
import king.rapper.pgone.pgonediary.dao.NoteInfoDao;

public class NoteInfoTest extends AbstractDaoTestLongPk<NoteInfoDao, NoteInfo> {

    public NoteInfoTest() {
        super(NoteInfoDao.class);
    }

    @Override
    protected NoteInfo createEntity(Long key) {
        NoteInfo entity = new NoteInfo();
        entity.setNoteInfoId(key);
        return entity;
    }

}
