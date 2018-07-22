package example.test.phong.coffeeapp.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class WrapViewPager @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null): ViewPager(context, attrs) {
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (currentItem == 0 && childCount == 0) {
            return false
        }

        try {
            return super.onTouchEvent(ev)
        } catch (ex: Exception) {
            return false
        }

    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (currentItem == 0 && childCount == 0) {
            return false
        }

        try {
            return super.onInterceptTouchEvent(ev)
        } catch (e: Exception) {
            return false
        }
    }

    /**
     * fix ViewPager WRAP_CONTENT not work
     * credit: https://stackoverflow.com/questions/8394681/android-i-am-unable-to-have-viewpager-wrap-content/20784791#20784791
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec = heightMeasureSpec
        if (MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY) {
            var height = 0
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
                val h = child.measuredHeight
                if (h > height) height = h
            }
            if (height != 0) {
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}