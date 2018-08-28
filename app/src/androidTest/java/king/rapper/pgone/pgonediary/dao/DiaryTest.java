package king.rapper.pgone.pgonediary.dao;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

public class DiaryTest extends AbstractDaoTestLongPk<DiaryDao, Diary> {

    public DiaryTest() {
        super(DiaryDao.class);
    }

    @Override
    protected Diary createEntity(Long key) {
        Diary entity = new Diary();
        entity.setDiaryId(key);
        return entity;
    }

}
