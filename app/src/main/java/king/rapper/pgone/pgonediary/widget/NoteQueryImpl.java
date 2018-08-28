package king.rapper.pgone.pgonediary.widget;

import android.os.Message;

import java.util.List;

import king.rapper.pgone.pgonediary.dao.DaoController;
import king.rapper.pgone.pgonediary.entity.NoteInfo;
import king.rapper.pgone.pgonediary.util.DateUtil;

/**
 * @ Create_time: 2018/5/22 on 10:25.
 * @ description： 查询天气
 * @ author: radish  email: 15703379121@163.com
 */
public class NoteQueryImpl {

    public void noteQuery(final NoteInterface noteInterface) {
        List<NoteInfo> noteInfoByTime = DaoController.getInstance().getNoteInfoAll();
        if (noteInterface != null) {
            noteInterface.getNote(noteInfoByTime);
        }
//        final Long[] time = DateUtil.getDayTime();
//        if (time != null && time.length == 2) {
//            List<NoteInfo> noteInfoByTime = DaoController.getInstance().getNoteInfoByTime(time[0], time[1]);
//            if (noteInterface != null) {
//                noteInterface.getNote(noteInfoByTime);
//            }
//        }
    }

    public interface NoteInterface {
        void getNote(List<NoteInfo> list);
    }
}
