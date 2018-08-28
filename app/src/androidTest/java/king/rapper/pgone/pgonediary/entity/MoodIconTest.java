package king.rapper.pgone.pgonediary.entity;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import king.rapper.pgone.pgonediary.dao.MoodIconDao;

public class MoodIconTest extends AbstractDaoTestLongPk<MoodIconDao, MoodIcon> {

    public MoodIconTest() {
        super(MoodIconDao.class);
    }

    @Override
    protected MoodIcon createEntity(Long key) {
        MoodIcon entity = new MoodIcon();
        entity.setMoodIconId(key);
        return entity;
    }

}
