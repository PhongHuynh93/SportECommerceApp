package example.test.phong.coffeeapp.utils

import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat

fun AppCompatTextView.setTextFuture(text: CharSequence) {
    setTextFuture(PrecomputedTextCompat.getTextFuture(text, TextViewCompat.getTextMetricsParams(this), null))
}