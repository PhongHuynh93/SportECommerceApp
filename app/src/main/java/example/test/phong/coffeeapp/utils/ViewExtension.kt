package example.test.phong.coffeeapp.utils

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat

fun AppCompatTextView.setTextFuture(text: CharSequence) {
    setTextFuture(PrecomputedTextCompat.getTextFuture(text, TextViewCompat.getTextMetricsParams(this), null))
}

fun View.getParentTagInt() = (parent as View).tag.toString().toInt()