package king.rapper.pgone.pgonediary.UI;

import android.text.TextUtils;

import com.google.gson.Gson;

import king.rapper.pgone.pgonediary.R;
import king.rapper.pgone.pgonediary.base.BaseActivity;
import king.rapper.pgone.pgonediary.entity.MessageInfo;
import king.rapper.pgone.pgonediary.util.LogUtils;

/**
 * @ Create_time: 2018/8/11 on 15:13.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
public class MessageActivity extends BaseActivity {

    private MessageInfo messageInfo;

    @Override
    public int getLayoutId() {
        return R.layout.activity_message;
    }


    @Override
    protected void getPreIntent() throws Exception {
        String message = getIntent().getStringExtra("message");
        LogUtils.e("message:"+message);
        if (!TextUtils.isEmpty(message)){
            messageInfo = new Gson().fromJson(message, MessageInfo.class);
        }
    }
}
