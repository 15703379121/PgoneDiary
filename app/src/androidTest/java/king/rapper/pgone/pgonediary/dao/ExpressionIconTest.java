package king.rapper.pgone.pgonediary.dao;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import king.rapper.pgone.pgonediary.entity.ExpressionIcon;
import king.rapper.pgone.pgonediary.dao.ExpressionIconDao;

public class ExpressionIconTest extends AbstractDaoTestLongPk<ExpressionIconDao, ExpressionIcon> {

    public ExpressionIconTest() {
        super(ExpressionIconDao.class);
    }

    @Override
    protected ExpressionIcon createEntity(Long key) {
        ExpressionIcon entity = new ExpressionIcon();
        entity.setExpressionIconId(key);
        return entity;
    }

}
