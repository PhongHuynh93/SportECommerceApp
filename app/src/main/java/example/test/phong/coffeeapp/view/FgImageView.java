package example.test.phong.coffeeapp.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatImageView;
import example.test.phong.coffeeapp.R;

/**
 * Created by PhuongHoang on 8/16/16.
 */
public class FgImageView extends AppCompatImageView {
    private Drawable mForeground;

    public FgImageView(Context context) {
        super(context);
    }

    public FgImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inits(context, attrs);
    }

    public FgImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inits(context, attrs);
    }

    private void inits(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ForegroundView);
        Drawable foreground = a.getDrawable(R.styleable.ForegroundView_android_foreground);
        if (foreground != null) {
            setForeground(foreground);
        }
        a.recycle();
    }

    /**
     * Supply a drawable resource that is to be rendered on top of all of the child
     * views in the frame layout.
     *
     * @param drawableResId The drawable resource to be drawn on top of the children.
     */
    public void setForegroundResource(int drawableResId) {
        setForeground(getContext().getResources().getDrawable(drawableResId));
    }

    /**
     * Supply a Drawable that is to be rendered on top of all of the child
     * views in the frame layout.
     *
     * @param drawable The Drawable to be drawn on top of the children.
     */

    public void setForeground(Drawable drawable) {
        if (mForeground == drawable) {
            return;
        }
        if (mForeground != null) {
            mForeground.setCallback(null);
            unscheduleDrawable(mForeground);
        }

        mForeground = drawable;

        if (drawable != null) {
            drawable.setCallback(this);
            if (drawable.isStateful()) {
                drawable.setState(getDrawableState());
            }
        }
        requestLayout();
        invalidate();
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || who == mForeground;
    }

    @Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (mForeground != null) mForeground.jumpToCurrentState();
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (mForeground != null && mForeground.isStateful()) {
            mForeground.setState(getDrawableState());
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (mForeground != null) {
            if (mForeground instanceof NinePatchDrawable || mForeground.getIntrinsicWidth() <= 0 || mForeground.getIntrinsicHeight() <= 0)
                mForeground.setBounds(0, 0, getWidth(), getHeight());
            else mForeground.setBounds(getWidth() / 2 - mForeground.getIntrinsicWidth() / 2, getHeight() / 2 - mForeground
                    .getIntrinsicHeight() / 2, getWidth() / 2 + mForeground.getIntrinsicWidth() / 2, getHeight() / 2 + mForeground
                    .getIntrinsicHeight() / 2);
            mForeground.draw(canvas);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (e.getActionMasked() == MotionEvent.ACTION_DOWN) {
                if (mForeground != null)
                    mForeground.setHotspot(e.getX(), e.getY());
            }
        }
        return super.onTouchEvent(e);
    }
}
