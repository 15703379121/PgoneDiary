package king.rapper.pgone.pgonediary.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @ Create_time: 2018/8/2 on 14:19.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
@SuppressLint("AppCompatCustomView")
public class SampleView extends TextView {
    public SampleView(Context context) {
        super(context);
        init();
    }

    public SampleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SampleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("NewApi")
    public SampleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private Rect mRect;
    private GradientDrawable mDrawable;

    private void init() {

        setFocusable(true);

        mRect = new Rect(0, 0, 300, 300);
        mDrawable = new GradientDrawable(GradientDrawable.Orientation.TL_BR,
                new int[]{0xaa000000,
                        0xFFFFFFFF});
        mDrawable.setShape(GradientDrawable.RECTANGLE);
        mDrawable.setGradientRadius((float) (Math.sqrt(2) * 60));
    }

    static void setCornerRadii(GradientDrawable drawable, float r0,
                               float r1, float r2, float r3) {
        drawable.setCornerRadii(new float[]{r0, r0, r1, r1,
                r2, r2, r3, r3});
    }

    @Override
    protected void onDraw(Canvas canvas) {

        mDrawable.setBounds(mRect);

        float r = 16;

        canvas.save();
        canvas.translate(10, 10);
        mDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        setCornerRadii(mDrawable, r, r, 0, 0);
        mDrawable.draw(canvas);
        canvas.restore();

        canvas.save();
        canvas.translate(10 + mRect.width() + 10, 10);
        mDrawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        setCornerRadii(mDrawable, 0, 0, r, r);
        mDrawable.draw(canvas);
        canvas.restore();

        canvas.translate(0, mRect.height() + 10);

        canvas.save();
        canvas.translate(10, 10);
        mDrawable.setGradientType(GradientDrawable.SWEEP_GRADIENT);
        setCornerRadii(mDrawable, 0, r, r, 0);
        mDrawable.draw(canvas);
        canvas.restore();

    }
}
