package com.gushenge.easybarrage

import android.view.animation.Interpolator

class DecelerateAccelerateInterpolator : Interpolator {
    override fun getInterpolation(input: Float): Float {
        return Math.tan((input * 2 - 1) / 4 * Math.PI).toFloat() / 2.0f + 0.5f
    }
}