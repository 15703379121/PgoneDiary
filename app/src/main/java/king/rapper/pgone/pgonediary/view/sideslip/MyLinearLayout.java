package king.rapper.pgone.pgonediary.view.sideslip;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.List;

import king.rapper.pgone.pgonediary.util.LogUtils;

/**
 * Created by Administrator on 2018/7/21 0021.
 */

public class MyLinearLayout extends LinearLayout {
    private FrameLayout frameLayout;
    private View[] views;

    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("NewApi")
    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public void setView(FrameLayout frameLayout, View[] views) {
        this.frameLayout = frameLayout;
        this.views = views;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            if (!isTouchView(frameLayout, ev)) {
                for (int i = 0; i < views.length; i++) {
                    if (isTouchView(views[i], ev)) {
                        return super.onInterceptTouchEvent(ev);
                    }
                }
                //点击的是这个view
                frameLayout.setVisibility(View.GONE);
                for (int i = 0; i < frameLayout.getChildCount(); i++) {
                    frameLayout.getChildAt(i).setVisibility(View.GONE);
                }
                ((RecyclerView) frameLayout.getChildAt(0)).removeAllViews();
            }
        } catch (Exception e) {
            LogUtils.e("e:" + e.getMessage());
            e.printStackTrace();
        }
        return super.onInterceptTouchEvent(ev);
    }

    public static boolean isTouchView(View v, MotionEvent ev) throws Exception {

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
        return isTouch;

    }

}
