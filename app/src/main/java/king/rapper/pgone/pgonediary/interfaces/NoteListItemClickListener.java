package king.rapper.pgone.pgonediary.interfaces;

/**
 * @ Create_time: 2018/7/27 on 17:45.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
public interface NoteListItemClickListener {
    void onItemClick(int position);

    void onItemSelect(int position);

    void onItemEdit(int position);

    void onIteRecording(int position);
}
