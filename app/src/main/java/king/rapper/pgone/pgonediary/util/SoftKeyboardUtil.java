package king.rapper.pgone.pgonediary.util;

import android.app.Activity;
import android.content.Context;
import android.text.InputType;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @ Create_time: 2018/4/21 on 9:36.
 * @ description：
 * @ author: radish  15703379121@163.com
 */
public class SoftKeyboardUtil {

    /**
     * 判断软键盘的状态，如果时关闭状态则打开，如果是打开状态则关闭
     *
     * @param context
     */
    public static void hideOrShowKeyboard(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            if (context.getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 只是关闭软键盘
     *
     * @param context
     */
    public static void hideKeyboard(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && context.getCurrentFocus() != null) {
            if (context.getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


    public static void closeInputMethod(Activity context) {

        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(context.getWindow().getDecorView().getWindowToken(), 0);

        } catch (Exception e) {
        } finally {
        }

    }

    /**
     * @throws
     * @MethodName:closeInputMethod
     * @Description:关闭系统软键盘
     */

    public static void closeInputMethod(Activity context, final EditText editText) {

        try {

            ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(editText.getWindowToken(), 0);

                  /*  .hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(),

                            InputMethodManager.HIDE_NOT_ALWAYS);*/

        } catch (Exception e) {
        } finally {
        }

    }

    /**
     * @throws
     * @MethodName:openInputMethod
     * @Description:打开系统软键盘
     */

    public static void openInputMethod(final Activity activity, final EditText editText) {

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {

            public void run() {

                InputMethodManager inputManager = (InputMethodManager) editText

                        .getContext().getSystemService(

                                Context.INPUT_METHOD_SERVICE);

                inputManager.showSoftInput(editText, 0);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //调起英文软键盘
                        editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);

                    }
                });
            }

        }, 200);

    }
}
