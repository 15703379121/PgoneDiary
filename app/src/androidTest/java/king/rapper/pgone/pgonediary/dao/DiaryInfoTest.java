package king.rapper.pgone.pgonediary.dao;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import king.rapper.pgone.pgonediary.entity.DiaryInfo;
import king.rapper.pgone.pgonediary.dao.DiaryInfoDao;

public class DiaryInfoTest extends AbstractDaoTestLongPk<DiaryInfoDao, DiaryInfo> {

    public DiaryInfoTest() {
        super(DiaryInfoDao.class);
    }

    @Override
    protected DiaryInfo createEntity(Long key) {
        DiaryInfo entity = new DiaryInfo();
        entity.setDiaryInfoId(key);
        return entity;
    }

}
