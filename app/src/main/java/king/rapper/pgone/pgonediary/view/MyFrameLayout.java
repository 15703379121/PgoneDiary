package king.rapper.pgone.pgonediary.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import king.rapper.pgone.pgonediary.util.LogUtils;

/**
 * Created by Administrator on 2018/7/21 0021.
 */

public class MyFrameLayout extends FrameLayout {
    public MyFrameLayout(@NonNull Context context) {
        super(context);
    }

    public MyFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("NewApi")
    public MyFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isTouchView(this, ev)) {
            //点击的是这个view
            this.setVisibility(View.GONE);
            for (int i = 0; i < getChildCount(); i++) {
                getChildAt(i).setVisibility(View.GONE);
            }
            ((RecyclerView) getChildAt(0)).removeAllViews();
        }
        return super.onInterceptTouchEvent(ev);
    }

    public static boolean isTouchView(View v, MotionEvent ev) {

        boolean isTouch = false;

        int eventx = (int) ev.getRawX();

        int eventy = (int) ev.getRawY();

        if (v != null && v instanceof View) {

            int[] location = new int[2];

            v.getLocationOnScreen(location);

            int x = location[0];

            int y = location[1];

            int w = v.getWidth();

            int h = v.getHeight();

            Rect rect = new Rect();

            rect.left = x;

            rect.top = y;

            rect.right = x + w;

            rect.bottom = y + h;

            if (rect != null && rect.contains(eventx, eventy)) {

                isTouch = true;

            }
        }
        LogUtils.e("isTouch:" + isTouch);
        return isTouch;

    }


}
