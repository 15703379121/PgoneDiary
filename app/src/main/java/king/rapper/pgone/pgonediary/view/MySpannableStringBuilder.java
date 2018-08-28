package king.rapper.pgone.pgonediary.view;

import java.util.ArrayList;

/**
 * @ Create_time: 2018/7/17 on 11:21.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
public class MySpannableStringBuilder {
    private static MySpannableStringBuilder mySpannableStringBuilder;
    private static ArrayList<MySpannableString> spannableStringArrayList;

    private MySpannableStringBuilder() {

    }

    public static synchronized MySpannableStringBuilder getMySpannableStringBuilderInstance() {
        if (mySpannableStringBuilder == null) {
            mySpannableStringBuilder = new MySpannableStringBuilder();
        }

        return mySpannableStringBuilder;
    }

    public static synchronized ArrayList<MySpannableString> getSpannableStringArrayListInstance() {
        if (spannableStringArrayList == null) {
            spannableStringArrayList = new ArrayList<MySpannableString>();
        }

        return spannableStringArrayList;
    }

    public MySpannableStringBuilder append(MySpannableString text) {
        getSpannableStringArrayListInstance().add(text);
        return getMySpannableStringBuilderInstance();
    }
}
