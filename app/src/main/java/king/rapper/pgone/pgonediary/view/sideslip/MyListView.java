package king.rapper.pgone.pgonediary.view.sideslip;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * @ Create_time: 2018/5/16 on 9:14.
 * @ description：
 * @ author: radish  15703379121@163.com
 */
public class MyListView extends ListView {
    private final Context context;

    public MyListView(Context context) {
        super(context);
        this.context = context;
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

//    private double xDistance,yDistance,xLast,yLast;
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                xDistance = yDistance = 0f;
//                xLast = ev.getX();
//                yLast = ev.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                final float curX = ev.getX();
//                final float curY = ev.getY();
//
//                xDistance += Math.abs(curX - xLast);
//                yDistance += Math.abs(curY - yLast);
//                xLast = curX;
//                yLast = curY;
//                /**
//                 * 当x轴的相对位移大于y轴的相对位移时，让其父控件（外层嵌套的那个listview）让出滑动事件
//                 * （默认只让swipmenulistview左滑）
//                 */
//                LogUtils.e("xDistance:"+xDistance+"------yDistance:"+yDistance);
//                if (xDistance < yDistance) {
//                    getParent().requestDisallowInterceptTouchEvent(true);
//                } else  {
//                    //当y轴相对移动大于自定义的位移量时，将滑动事件还给父控件
//                    getParent().requestDisallowInterceptTouchEvent(false);
//                }
//                break;
//        }
//        return super.dispatchTouchEvent(ev);
//    }
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                xDistance = yDistance = 0f;
//                xLast = ev.getX();
//                yLast = ev.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                final float curX = ev.getX();
//                final float curY = ev.getY();
//
//                xDistance += Math.abs(curX - xLast);
//                yDistance += Math.abs(curY - yLast);
//                xLast = curX;
//                yLast = curY;
//                //左右滑动大于屏幕的1/3
//                int width =MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//                LogUtils.e("width:"+width);
//                if (xDistance > yDistance) {
//                    return false;   //表示向下传递事件
//                }
//        }
//
//        return super.onInterceptTouchEvent(ev);
//    }


    // 滑动距离及坐标 归还父控件焦点
    private float xDistance, yDistance, xLast, yLast, xDown, mLeft;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("touch", "ACTION_DOWN");
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                xDown = ev.getX();
                mLeft = ev.getX();// 解决与侧边栏滑动冲突
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();

                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;
                if (mLeft < 100 || xDistance < yDistance) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    if (curX < xDown) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    } else {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
