package example.test.phong.coffeeapp.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager



fun Context.getScreenWidth(): Int {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val metrics = DisplayMetrics()
    display.getMetrics(metrics)
    return metrics.widthPixels
}