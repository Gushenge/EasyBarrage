package com.gushenge.easybarrage

import android.content.Context
import android.view.animation.Animation
import android.view.animation.TranslateAnimation

object AnimationHelper {
    @JvmStatic
    fun createTranslateAnim(context: Context, fromX: Int, toX: Int): Animation {
        val tlAnim = TranslateAnimation(fromX.toFloat(), toX.toFloat(), 0f, 0f)
        val duration = (Math.abs(toX - fromX) * 1.0f / BarrageTools.getScreenWidth(context) * 3000).toLong()
        tlAnim.duration = duration
        tlAnim.interpolator = DecelerateAccelerateInterpolator()
        tlAnim.fillAfter = true
        return tlAnim
    }
}