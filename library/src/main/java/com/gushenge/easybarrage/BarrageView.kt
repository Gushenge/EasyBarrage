package com.gushenge.easybarrage

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.util.Log
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.graphics.drawable.DrawableCompat
import com.gushenge.easybarrage.AnimationHelper.createTranslateAnim
import com.gushenge.easybarrage.BarrageTools.dp2px
import com.gushenge.easybarrage.BarrageTools.getScreenWidth
import com.gushenge.easybarrage.BarrageTools.px2sp
import java.util.*

/**
 * changed by 林牧歌 on 2020/9/21.
 */
class BarrageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {
    private val existMarginValues: MutableSet<Int> = HashSet()
    private var linesCount = 0
    private var validHeightSpace = 0
    private val INTERVAL = 500
    private val random = Random(System.currentTimeMillis())
    private var maxBarrageSize = 0
    private var textLeftPadding = 0
    private var textRightPadding = 0
    private var textTopPadding = 0
    private var textBottomPadding = 0
    private var maxTextSize = 0
    private var minTextSize = 0
    private var lineHeight = 0
    private var borderColor = 0
    private var random_color = false
    private var allow_repeat = false
    private val DEFAULT_PADDING = 15
    private val DEFAULT_BARRAGESIZE = 10
    private val DEFAULT_MAXTEXTSIZE = 20
    private val DEFAULT_MINTEXTSIZE = 14
    private val DEFAULT_LINEHEIGHT = 24
    private val DEFAULT_BORDERCOLOR = -0x1000000
    private val DEFAULT_RANDOMCOLOR = false
    private val DEFAULT_ALLOWREPEAT = false
    private val barrages: MutableList<Barrage> = ArrayList()
    private val cache: MutableList<Barrage> = ArrayList()
    fun setBarrages(list: List<Barrage>) {
        if (!list.isEmpty()) {
            barrages.clear()
            barrages.addAll(list)
            mHandler.sendEmptyMessageDelayed(0, INTERVAL.toLong())
        }
    }

    fun addBarrage(tb: Barrage) {
        barrages.add(tb)
        if (allow_repeat) {
            cache.add(tb)
        }
        showBarrage(tb)
        if (!mHandler.hasMessages(0)) {
            mHandler.sendEmptyMessageDelayed(0, INTERVAL.toLong())
        }
    }

    var mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            checkBarrage()
            sendEmptyMessageDelayed(0, INTERVAL.toLong())
        }
    }

    fun checkBarrage() {
        val index = (Math.random() * barrages.size).toInt()
        val barrage = barrages[index]
        if (allow_repeat) {
            if (cache.contains(barrage)) return
            cache.add(barrage)
        }
        showBarrage(barrage)
    }

    private fun showBarrage(tb: Barrage) {
        if (linesCount != 0 && childCount >= linesCount) return
        if (childCount >= maxBarrageSize) return
        val textView = if (tb.isShowBorder) BorderTextView(context, borderColor) else TextView(context)
        val drawable = textView.context.resources.getDrawable(R.drawable.shape_bg_round)
        textView.setBackgroundDrawable(tintDrawable(drawable, tb.backGroundColor))
        textView.setPadding(textLeftPadding, textTopPadding, textRightPadding, textBottomPadding)
        textView.setTextSize((minTextSize + (maxTextSize - minTextSize) * Math.random()).toFloat())
        textView.text = tb.content
        textView.setTextColor(if (random_color) Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)) else resources.getColor(tb.color))
        val leftMargin = right - left - paddingLeft
        val verticalMargin = randomTopMargin
        textView.tag = verticalMargin
        val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        params.addRule(ALIGN_PARENT_TOP)
        params.topMargin = verticalMargin
        textView.layoutParams = params
        val anim = createTranslateAnim(context, leftMargin, -getScreenWidth(context))
        anim.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                Log.i("111111", "onAnimationEnd: ")
                if (allow_repeat) cache.remove(tb)
                removeView(textView)
                val verticalMargin = textView.tag as Int
                existMarginValues.remove(verticalMargin)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        textView.startAnimation(anim)
        addView(textView)
    }

    private val randomTopMargin: Int
        private get() {
            if (validHeightSpace == 0) {
                validHeightSpace = bottom - top - paddingTop - paddingBottom
            }
            if (linesCount == 0) {
                linesCount = validHeightSpace / lineHeight
                if (linesCount == 0) {
                    throw RuntimeException("Not enough space to show text.")
                }
            }
            while (true) {
                val randomIndex = (Math.random() * linesCount).toInt()
                val marginValue = randomIndex * (validHeightSpace / linesCount)
                if (!existMarginValues.contains(marginValue)) {
                    existMarginValues.add(marginValue)
                    return marginValue
                }
            }
        }

    fun destroy() {
        if (mHandler.hasMessages(0)) mHandler.removeMessages(0)
        barrages.clear()
        cache.clear()
    }

    private fun tintDrawable(drawable: Drawable, color: Int): Drawable {
        val colors = ColorStateList.valueOf(color)
        val wrappedDrawable = DrawableCompat.wrap(drawable)
        DrawableCompat.setTintList(wrappedDrawable, colors)
        return wrappedDrawable
    }

    init {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.BarrageView, 0, 0)
        try {
            textLeftPadding = typedArray.getInt(R.styleable.BarrageView_text_left_padding, DEFAULT_PADDING)
            textRightPadding = typedArray.getInt(R.styleable.BarrageView_text_right_padding, DEFAULT_PADDING)
            textTopPadding = typedArray.getInt(R.styleable.BarrageView_text_top_padding, DEFAULT_PADDING)
            textBottomPadding = typedArray.getInt(R.styleable.BarrageView_text_bottom_padding, DEFAULT_PADDING)
            maxBarrageSize = typedArray.getInt(R.styleable.BarrageView_size, DEFAULT_BARRAGESIZE)
            maxTextSize = typedArray.getInt(R.styleable.BarrageView_max_text_size, DEFAULT_MAXTEXTSIZE)
            minTextSize = typedArray.getInt(R.styleable.BarrageView_min_text_size, DEFAULT_MINTEXTSIZE)
            lineHeight = typedArray.getDimensionPixelSize(R.styleable.BarrageView_line_height, dp2px(context, DEFAULT_LINEHEIGHT.toFloat()))
            borderColor = typedArray.getColor(R.styleable.BarrageView_border_color, DEFAULT_BORDERCOLOR)
            random_color = typedArray.getBoolean(R.styleable.BarrageView_random_color, DEFAULT_RANDOMCOLOR)
            allow_repeat = typedArray.getBoolean(R.styleable.BarrageView_allow_repeat, DEFAULT_ALLOWREPEAT)
            if (px2sp(context, lineHeight.toFloat()) < maxTextSize) maxTextSize = px2sp(context, lineHeight.toFloat())
        } finally {
            typedArray.recycle()
        }
    }
}