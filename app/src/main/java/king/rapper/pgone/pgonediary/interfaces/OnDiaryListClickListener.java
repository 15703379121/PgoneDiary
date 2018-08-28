package king.rapper.pgone.pgonediary.interfaces;

/**
 * @ Create_time: 2018/7/18 on 15:26.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
public interface OnDiaryListClickListener {

    //单击条目
    void OnItemSingleClick(int position);

    //删除条目
    void OnItemDeleteClick(int position);

    //双击条目
    void OnItemDoubleClick(int position);
}
