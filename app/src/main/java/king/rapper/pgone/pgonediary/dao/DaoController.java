package king.rapper.pgone.pgonediary.dao;

import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.AbstractDao;

import java.util.List;

import king.rapper.pgone.pgonediary.MyApp;
import king.rapper.pgone.pgonediary.entity.DiaryInfo;
import king.rapper.pgone.pgonediary.entity.ExpressionIcon;
import king.rapper.pgone.pgonediary.entity.MessageInfo;
import king.rapper.pgone.pgonediary.entity.MoodIcon;
import king.rapper.pgone.pgonediary.entity.NoteInfo;
import king.rapper.pgone.pgonediary.entity.WeatherIcon;

/**
 * @ Create_time: 2018/7/17 on 15:03.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
public class DaoController {
    private final static String dbName = "pgone";
    private DaoMaster.OpenHelper devOpenHelper;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    private DaoController() {
        devOpenHelper = new DaoMaster.DevOpenHelper(MyApp.getInstance(), dbName, null);
    }

    private static DaoController instance;

    public static DaoController getInstance() {
        if (instance == null) {
            synchronized (DaoController.class) {
                if (instance == null) {
                    instance = new DaoController();
                }
            }
        }
        return instance;
    }

    public DaoSession getDaoSession() {
        if (devOpenHelper == null)
            devOpenHelper = new DaoMaster.DevOpenHelper(MyApp.getInstance(), dbName, null);
        if (daoMaster == null)
            daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        if (daoSession == null)
            daoSession = daoMaster.newSession();
        return daoSession;
    }

    //    private DiaryDao diaryDao;
    private DiaryInfoDao diaryInfoDao;
    private NoteInfoDao noteInfoDao;
    private WeatherIconDao weatherIconDao;
    private MoodIconDao moodIconDao;
    private ExpressionIconDao expressionIconDao;
    private RecordingDao recordingDao;
    private NoteRecordingDao noteRecordingDao;
    private MessageInfoDao messageInfoDao;

    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase() {
        if (devOpenHelper == null) {
            devOpenHelper = new DaoMaster.OpenHelper(MyApp.getInstance(), dbName, null) {
            };
        }
        SQLiteDatabase db = devOpenHelper.getReadableDatabase();
        return db;
    }


    /**
     * 获取可写数据库
     */
    private SQLiteDatabase getWritableDatabase() {
        if (devOpenHelper == null) {
            devOpenHelper = new DaoMaster.OpenHelper(MyApp.getInstance(), dbName, null) {
            };
        }
        SQLiteDatabase db = devOpenHelper.getWritableDatabase();
        return db;
    }

    public void deleteDB() {
        SQLiteDatabase db = getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoMaster.dropAllTables(daoMaster.getDatabase(), true);
        DaoMaster.createAllTables(daoMaster.getDatabase(), true);

    }

    /**
     * 获取DiaryDao
     */
//    public DiaryDao getDiaryDao() {
//        // 创建数据
//        if (diaryDao == null) {
//            initDaoInfo();
//            diaryDao = daoSession.getDiaryDao();
//        }
//        return diaryDao;
//    }

    /**
     * 获取DiaryDao
     */
    public DiaryInfoDao getDiaryInfoDao() {
        // 创建数据
        if (diaryInfoDao == null) {
            initDaoInfo();
            diaryInfoDao = daoSession.getDiaryInfoDao();
        }
        return diaryInfoDao;
    }

    /**
     * 获取DiaryDao
     */
    public NoteInfoDao getNoteInfoDao() {
        // 创建数据
        if (noteInfoDao == null) {
            initDaoInfo();
            noteInfoDao = daoSession.getNoteInfoDao();
        }
        return noteInfoDao;
    }

    public List<DiaryInfo> getDiaryInfoAll() {
        return DaoController.getInstance().getDiaryInfoDao().queryBuilder().where(DiaryInfoDao.Properties.Tag.eq(0)).orderDesc(DiaryInfoDao.Properties.Time).list();
    }

    public List<DiaryInfo> getDiaryInfoByTime(Long startTime, Long endTime) {
        return DaoController.getInstance().getDiaryInfoDao().queryBuilder().where(DiaryInfoDao.Properties.Tag.eq(0), DiaryInfoDao.Properties.Time.between(startTime, endTime)).orderDesc(DiaryInfoDao.Properties.Time).list();
    }


    public List<NoteInfo> getNoteInfoAll() {
        return DaoController.getInstance().getNoteInfoDao().queryBuilder().where(NoteInfoDao.Properties.Tag.eq(0)).orderDesc(NoteInfoDao.Properties.Time).list();
    }

    public List<MessageInfo> getMessageInfoByMsgId(String extraMsgId) {
        return DaoController.getInstance().getMessageInfoDao().queryBuilder().where(MessageInfoDao.Properties.Tag.eq(0), MessageInfoDao.Properties.ExtraMsgId.eq(extraMsgId))/*.orderDesc(NoteInfoDao.Properties.Time)*/.list();
    }

    public List<MessageInfo> getMessageInfoAll() {
        return DaoController.getInstance().getMessageInfoDao().queryBuilder().where(MessageInfoDao.Properties.Tag.eq(0))/*.orderDesc(NoteInfoDao.Properties.Time)*/.list();
    }

    public List<NoteInfo> getNoteInfoByTime(Long startTime, Long endTime) {
        return DaoController.getInstance().getNoteInfoDao().queryBuilder().where(NoteInfoDao.Properties.Tag.eq(0), NoteInfoDao.Properties.Time.between(startTime, endTime)).orderDesc(NoteInfoDao.Properties.Time).list();
    }

    private void initDaoInfo() {
        if (devOpenHelper == null)
            devOpenHelper = new DaoMaster.DevOpenHelper(MyApp.getInstance(), dbName, null);
        if (daoMaster == null)
            daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        if (daoSession == null)
            daoSession = daoMaster.newSession();
    }

    /**
     * 获取WeatherIconDao
     */
    public WeatherIconDao getWeatherIconDao() {
        // 创建数据
        if (weatherIconDao == null) {
            initDaoInfo();
            weatherIconDao = daoSession.getWeatherIconDao();
        }
        return weatherIconDao;
    }


    /**
     * 获取WeatherIconDao
     */
    public MessageInfoDao getMessageInfoDao() {
        // 创建数据
        if (weatherIconDao == null) {
            initDaoInfo();
            messageInfoDao = daoSession.getMessageInfoDao();
        }
        return messageInfoDao;
    }

    public <T> AbstractDao getIconDao(Class<T> tClass) {
        // 创建数据
        initDaoInfo();
        // TODO: 2018/7/21 icon8
        if (tClass == WeatherIcon.class) {
            if (weatherIconDao == null) {
                weatherIconDao = daoSession.getWeatherIconDao();
            }
            return weatherIconDao;
        } else if (tClass == MoodIcon.class) {
            if (moodIconDao == null) {
                moodIconDao = daoSession.getMoodIconDao();
            }
            return moodIconDao;
        } else if (tClass == ExpressionIcon.class) {
            if (expressionIconDao == null) {
                expressionIconDao = daoSession.getExpressionIconDao();
            }
            return expressionIconDao;
        }
        return null;
    }

    /**
     * 获取MoodIconDao
     */
    public MoodIconDao getMoodIconDao() {
        // 创建数据
        if (moodIconDao == null) {
            initDaoInfo();
            moodIconDao = daoSession.getMoodIconDao();
        }
        return moodIconDao;
    }

    /**
     * 获取MoodIconDao
     */
    public ExpressionIconDao getExpressionIconDao() {
        // 创建数据
        if (expressionIconDao == null) {
            initDaoInfo();
            expressionIconDao = daoSession.getExpressionIconDao();
        }
        return expressionIconDao;
    }

    /**
     * RecordingDao
     */
    public RecordingDao getRecordingDao() {
        // 创建数据
        if (recordingDao == null) {
            initDaoInfo();
            recordingDao = daoSession.getRecordingDao();
        }
        return recordingDao;
    }

    /**
     * RecordingDao
     */
    public NoteRecordingDao getNoteRecordingDao() {
        // 创建数据
        if (noteRecordingDao == null) {
            initDaoInfo();
            noteRecordingDao = daoSession.getNoteRecordingDao();
        }
        return noteRecordingDao;
    }
}
