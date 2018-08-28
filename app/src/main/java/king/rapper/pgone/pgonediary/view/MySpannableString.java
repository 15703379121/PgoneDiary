package king.rapper.pgone.pgonediary.view;

import android.text.SpannableString;

/**
 * @ Create_time: 2018/7/17 on 11:20.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
public class MySpannableString extends SpannableString {
    private String tag;

    public MySpannableString(CharSequence source, String tag) {
        super(source);
        this.tag = tag;

    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

}
