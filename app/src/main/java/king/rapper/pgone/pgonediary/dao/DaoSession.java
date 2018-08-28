package king.rapper.pgone.pgonediary.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import king.rapper.pgone.pgonediary.entity.DiaryInfo;
import king.rapper.pgone.pgonediary.entity.ExpressionIcon;
import king.rapper.pgone.pgonediary.entity.MessageInfo;
import king.rapper.pgone.pgonediary.entity.MoodIcon;
import king.rapper.pgone.pgonediary.entity.NoteInfo;
import king.rapper.pgone.pgonediary.entity.NoteRecording;
import king.rapper.pgone.pgonediary.entity.Recording;
import king.rapper.pgone.pgonediary.entity.WeatherIcon;

import king.rapper.pgone.pgonediary.dao.DiaryInfoDao;
import king.rapper.pgone.pgonediary.dao.ExpressionIconDao;
import king.rapper.pgone.pgonediary.dao.MessageInfoDao;
import king.rapper.pgone.pgonediary.dao.MoodIconDao;
import king.rapper.pgone.pgonediary.dao.NoteInfoDao;
import king.rapper.pgone.pgonediary.dao.NoteRecordingDao;
import king.rapper.pgone.pgonediary.dao.RecordingDao;
import king.rapper.pgone.pgonediary.dao.WeatherIconDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig diaryInfoDaoConfig;
    private final DaoConfig expressionIconDaoConfig;
    private final DaoConfig messageInfoDaoConfig;
    private final DaoConfig moodIconDaoConfig;
    private final DaoConfig noteInfoDaoConfig;
    private final DaoConfig noteRecordingDaoConfig;
    private final DaoConfig recordingDaoConfig;
    private final DaoConfig weatherIconDaoConfig;

    private final DiaryInfoDao diaryInfoDao;
    private final ExpressionIconDao expressionIconDao;
    private final MessageInfoDao messageInfoDao;
    private final MoodIconDao moodIconDao;
    private final NoteInfoDao noteInfoDao;
    private final NoteRecordingDao noteRecordingDao;
    private final RecordingDao recordingDao;
    private final WeatherIconDao weatherIconDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        diaryInfoDaoConfig = daoConfigMap.get(DiaryInfoDao.class).clone();
        diaryInfoDaoConfig.initIdentityScope(type);

        expressionIconDaoConfig = daoConfigMap.get(ExpressionIconDao.class).clone();
        expressionIconDaoConfig.initIdentityScope(type);

        messageInfoDaoConfig = daoConfigMap.get(MessageInfoDao.class).clone();
        messageInfoDaoConfig.initIdentityScope(type);

        moodIconDaoConfig = daoConfigMap.get(MoodIconDao.class).clone();
        moodIconDaoConfig.initIdentityScope(type);

        noteInfoDaoConfig = daoConfigMap.get(NoteInfoDao.class).clone();
        noteInfoDaoConfig.initIdentityScope(type);

        noteRecordingDaoConfig = daoConfigMap.get(NoteRecordingDao.class).clone();
        noteRecordingDaoConfig.initIdentityScope(type);

        recordingDaoConfig = daoConfigMap.get(RecordingDao.class).clone();
        recordingDaoConfig.initIdentityScope(type);

        weatherIconDaoConfig = daoConfigMap.get(WeatherIconDao.class).clone();
        weatherIconDaoConfig.initIdentityScope(type);

        diaryInfoDao = new DiaryInfoDao(diaryInfoDaoConfig, this);
        expressionIconDao = new ExpressionIconDao(expressionIconDaoConfig, this);
        messageInfoDao = new MessageInfoDao(messageInfoDaoConfig, this);
        moodIconDao = new MoodIconDao(moodIconDaoConfig, this);
        noteInfoDao = new NoteInfoDao(noteInfoDaoConfig, this);
        noteRecordingDao = new NoteRecordingDao(noteRecordingDaoConfig, this);
        recordingDao = new RecordingDao(recordingDaoConfig, this);
        weatherIconDao = new WeatherIconDao(weatherIconDaoConfig, this);

        registerDao(DiaryInfo.class, diaryInfoDao);
        registerDao(ExpressionIcon.class, expressionIconDao);
        registerDao(MessageInfo.class, messageInfoDao);
        registerDao(MoodIcon.class, moodIconDao);
        registerDao(NoteInfo.class, noteInfoDao);
        registerDao(NoteRecording.class, noteRecordingDao);
        registerDao(Recording.class, recordingDao);
        registerDao(WeatherIcon.class, weatherIconDao);
    }
    
    public void clear() {
        diaryInfoDaoConfig.clearIdentityScope();
        expressionIconDaoConfig.clearIdentityScope();
        messageInfoDaoConfig.clearIdentityScope();
        moodIconDaoConfig.clearIdentityScope();
        noteInfoDaoConfig.clearIdentityScope();
        noteRecordingDaoConfig.clearIdentityScope();
        recordingDaoConfig.clearIdentityScope();
        weatherIconDaoConfig.clearIdentityScope();
    }

    public DiaryInfoDao getDiaryInfoDao() {
        return diaryInfoDao;
    }

    public ExpressionIconDao getExpressionIconDao() {
        return expressionIconDao;
    }

    public MessageInfoDao getMessageInfoDao() {
        return messageInfoDao;
    }

    public MoodIconDao getMoodIconDao() {
        return moodIconDao;
    }

    public NoteInfoDao getNoteInfoDao() {
        return noteInfoDao;
    }

    public NoteRecordingDao getNoteRecordingDao() {
        return noteRecordingDao;
    }

    public RecordingDao getRecordingDao() {
        return recordingDao;
    }

    public WeatherIconDao getWeatherIconDao() {
        return weatherIconDao;
    }

}
