package king.rapper.pgone.pgonediary.view;

import android.content.Context;
import android.util.AttributeSet;

import jp.wasabeef.richeditor.RichEditor;

/**
 * @ Create_time: 2018/7/24 on 15:51.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
public class MyRichEditor extends RichEditor {
    public MyRichEditor(Context context) {
        super(context);
    }

    public MyRichEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRichEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setNewLine() {
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.insertHTML('<br></br>');");
    }
}
