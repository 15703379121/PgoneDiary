package king.rapper.pgone.pgonediary.dao;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import king.rapper.pgone.pgonediary.entity.MessageInfo;
import king.rapper.pgone.pgonediary.dao.MessageInfoDao;

public class MessageInfoTest extends AbstractDaoTestLongPk<MessageInfoDao, MessageInfo> {

    public MessageInfoTest() {
        super(MessageInfoDao.class);
    }

    @Override
    protected MessageInfo createEntity(Long key) {
        MessageInfo entity = new MessageInfo();
        entity.setMessageInfoId(key);
        return entity;
    }

}
