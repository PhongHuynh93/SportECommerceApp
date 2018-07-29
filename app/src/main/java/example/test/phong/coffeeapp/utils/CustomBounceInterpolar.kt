package example.test.phong.coffeeapp.utils

import android.view.animation.Interpolator

/**
 * https://evgenii.com/blog/spring-button-animation-on-android/
 */
class CustomBounceInterpolar(val mAmplitude: Double, val mFrequency: Double) : Interpolator {
    override fun getInterpolation(time: Float): Float {
        return (-1 * Math.pow(Math.E, -time/ mAmplitude) *
                                Math.cos(mFrequency * time) + 1).toFloat()
    }
}