package king.rapper.pgone.pgonediary.interfaces;

import java.util.List;

import king.rapper.pgone.pgonediary.entity.MemoInfo;

/**
 * @ Create_time: 2018/8/3 on 9:00.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
public interface MemoCallback {

    void memoCallback(List<MemoInfo> memoInfoList, String message);
   /*
    //查找所有事件
    public void findMemoAll(List<MemoInfo> memoInfoList, String message) {
    }

    //根据id查找事件
    public void findMemoById(MemoInfo memoInfo, String message) {
    }

    //根据日期查找事件
    public void findMemoByDate(List<MemoInfo> memoInfoList, String message) {
    }

    //添加事件
    public void addMemo(String message) {
    }

    //根据id删除事件
    public void deleteMemoById(String message) {
    }

    //根据id更新事件
    public void updateMemoById(String message) {
    }*/

}
