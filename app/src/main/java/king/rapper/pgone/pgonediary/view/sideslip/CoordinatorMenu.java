package king.rapper.pgone.pgonediary.view.sideslip;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 协同侧滑菜单
 */

public class CoordinatorMenu extends FrameLayout {

    // 屏幕宽度以及高度
    private final int mScreenWidth;
    private final int mScreenHeight;

    private View mMenuView;// 菜单视图
    private MainView mMainView; // 主视图

    private ViewDragHelper mViewDragHelper;// 滑动帮助

    private static final int MENU_CLOSED = 1; // 菜单关闭状态
    private static final int MENU_OPENED = 2; // 菜单打开状态
    private int mMenuState = MENU_CLOSED;// 菜单状态 默认关闭

    private int mDragOrientation; // 拖动距离
    private static final int LEFT_TO_RIGHT = 3; //从左到右
    private static final int RIGHT_TO_LEFT = 4; //从右到左

    private static final float SPRING_BACK_VELOCITY = 1500;//返回速度
    private static final int SPRING_BACK_DISTANCE = 80;//返回距离 初始值   dp
    private int mSpringBackDistance;//返回距离   px

    //    private static final int MENU_MARGIN_RIGHT = 64; //菜单展开部时 距离右侧 距离  dp
    private static final int MENU_MARGIN_RIGHT = 120; //菜单展开部时 距离右侧 距离  dp
    private int mMenuWidth;// 菜单宽度  px

    private static final int MENU_OFFSET = 128; //菜单隐藏的部分  dp
    //    private static final int MENU_OFFSET = 80; //菜单隐藏的部分  dp
    private int mMenuOffset;//菜单隐藏的部分  px

    private static final float TOUCH_SLOP_SENSITIVITY = 0.5f;// 触摸敏感程度

    private static final String DEFAULT_SHADOW_OPACITY = "00"; // 默认阴影不透明度
    private String mShadowOpacity = DEFAULT_SHADOW_OPACITY;// 阴影不透明度
    private int mMenuLeft;// 菜单 左侧坐标
    private int mMainLeft;// 主页面 左侧坐标

    public CoordinatorMenu(Context context) {
        this(context, null);
    }

    public CoordinatorMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CoordinatorMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//      首先获取屏幕宽度高度，以及屏幕密度
        final float density = getResources().getDisplayMetrics().density;//屏幕密度
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = getResources().getDisplayMetrics().heightPixels;
//  设置返回距离  像素值
        mSpringBackDistance = (int) (SPRING_BACK_DISTANCE * density + 0.5f);
//  设置菜单隐藏部分 像素值
        mMenuOffset = (int) (MENU_OFFSET * density + 0.5f);
//  初始化  菜单 以及 主页面 左侧坐标
        mMenuLeft = -mMenuOffset;
        mMainLeft = 0;
//  菜单宽度  像素值
        mMenuWidth = mScreenWidth - (int) (MENU_MARGIN_RIGHT * density + 0.5f);

        mViewDragHelper = ViewDragHelper.create(this, TOUCH_SLOP_SENSITIVITY, new CoordinatorCallback());
        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    /**
     * 事件回调
     */
    private class CoordinatorCallback extends ViewDragHelper.Callback {

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            //  拖动开始,
            mViewDragHelper.captureChildView(mMainView, pointerId);// 捕获主页面view 用于拖曳，会回调 tryCaptureView()
        }

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            //  设定主页面以及菜单页面 可以拖动
            return/* mMainView == child ||*/ mMenuView == child;
        }

        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            //  菜单 可以滑动的时候，进行 滑动
            if (capturedChild == mMenuView) {
                mViewDragHelper.captureChildView(mMainView, activePointerId);
            }
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
//           设置菜单 水平方向可拖拽的范围，仅作为动画执行速度计算使用
            return 1;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
//           传入View水平方向拖拽后的坐标、拖拽像素，由我们决定View的坐标　　它们的关系是child.getLeft() + dx = left
            if (left < 0) {
                left = 0;
            } else if (left > mMenuWidth) {
                left = mMenuWidth;
            }
            return left;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
//            View拖拽释放后处理的业务，这里用于完成拖拽后复位、状态改变等处理

            if (mDragOrientation == LEFT_TO_RIGHT) {
                //如果拖动距离 == 从左到右的距离
// 如果此时 x 轴速度  大于返回速度  或者  主页面的 左边大于返回距离   打开菜单
                if (xvel > SPRING_BACK_VELOCITY || mMainView.getLeft() > mSpringBackDistance) {
                    openMenu();
                } else {
                    closeMenu();
                }
            } else if (mDragOrientation == RIGHT_TO_LEFT) {
                // 如果此时 x 轴速度  大于逆向返回速度  或者  主页面的 左边小于 菜单宽度 - 返回距离       关闭菜单
                if (xvel < -SPRING_BACK_VELOCITY || mMainView.getLeft() < mMenuWidth - mSpringBackDistance) {
                    closeMenu();
                } else {
                    openMenu();
                }
            }

        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
//            重新赋予了View的坐标，这个方法的调用表示View的位置已经改变　如果需要的话，也可自行再强制改变位置
            mMainLeft = left;
            if (dx > 0) {
                mDragOrientation = LEFT_TO_RIGHT;
            } else if (dx < 0) {
                mDragOrientation = RIGHT_TO_LEFT;
            }
            float scale = (float) (mMenuWidth - mMenuOffset) / (float) mMenuWidth;
            mMenuLeft = left - ((int) (scale * left) + mMenuOffset);
            mMenuView.layout(mMenuLeft, mMenuView.getTop(),
                    mMenuLeft + mMenuWidth, mMenuView.getBottom());
            float showing = (float) (mScreenWidth - left) / (float) mScreenWidth;
            int hex = 255 - Math.round(showing * 255);
            if (hex < 16) {
                mShadowOpacity = "0" + Integer.toHexString(hex);
            } else {
                mShadowOpacity = Integer.toHexString(hex);
            }
        }
    }

    //加载完布局文件后调用
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mMenuView = getChildAt(0);
        mMainView = (MainView) getChildAt(1);
        mMainView.setParent(this);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mViewDragHelper.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //将触摸事件传递给ViewDragHelper，此操作必不可少
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        MarginLayoutParams menuParams = (MarginLayoutParams) mMenuView.getLayoutParams();
        menuParams.width = mMenuWidth;
        mMenuView.setLayoutParams(menuParams);

        mMenuView.layout(mMenuLeft, top, mMenuLeft + mMenuWidth, bottom);
        mMainView.layout(mMainLeft, 0, mMainLeft + mScreenWidth, bottom);
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        final int restoreCount = canvas.save();//保存画布当前的剪裁信息

        final int height = getHeight();
        final int clipLeft = 0;
        int clipRight = mMainView.getLeft();
        if (child == mMenuView) {
            canvas.clipRect(clipLeft, 0, clipRight, height);//剪裁显示的区域
        }

        boolean result = super.drawChild(canvas, child, drawingTime);//绘制当前view

        //恢复画布之前保存的剪裁信息
        //以正常绘制之后的view
        canvas.restoreToCount(restoreCount);


        int shadowLeft = mMainView.getLeft();
//        Log.d(TAG, "drawChild: shadowLeft: " + shadowLeft);
        final Paint shadowPaint = new Paint();
//        Log.d(TAG, "drawChild: mShadowOpacity: " + mShadowOpacity);
        shadowPaint.setColor(Color.parseColor("#" + mShadowOpacity + "777777"));
        shadowPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(shadowLeft, 0, mScreenWidth, mScreenHeight, shadowPaint);

        return result;
    }

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
        if (mMainView.getLeft() == 0) {
            mMenuState = MENU_CLOSED;
        } else if (mMainView.getLeft() == mMenuWidth) {
            mMenuState = MENU_OPENED;
        }
    }

    public void openMenu() {
        mViewDragHelper.smoothSlideViewTo(mMainView, mMenuWidth, 0);
        ViewCompat.postInvalidateOnAnimation(CoordinatorMenu.this);
    }

    public void closeMenu() {
        mViewDragHelper.smoothSlideViewTo(mMainView, 0, 0);
        ViewCompat.postInvalidateOnAnimation(CoordinatorMenu.this);
    }

    public boolean isOpened() {
        return mMenuState == MENU_OPENED;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        final SavedState ss = new SavedState(superState);
        ss.menuState = mMenuState;
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        final SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        if (ss.menuState == MENU_OPENED) {
            openMenu();
        }
    }

    protected static class SavedState extends AbsSavedState {
        int menuState;

        SavedState(Parcel in, ClassLoader loader) {
            super(in, loader);
            menuState = in.readInt();
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(menuState);
        }

        public static final Creator<SavedState> CREATOR = ParcelableCompat.newCreator(
                new ParcelableCompatCreatorCallbacks<SavedState>() {
                    @Override
                    public SavedState createFromParcel(Parcel in, ClassLoader loader) {
                        return new SavedState(in, loader);
                    }

                    @Override
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                });
    }
}