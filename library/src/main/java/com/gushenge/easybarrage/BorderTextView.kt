package com.gushenge.easybarrage

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class BorderTextView(context: Context?, attrs: AttributeSet?, private val border_color: Int) : AppCompatTextView(context!!, attrs) {
    constructor(context: Context?, border_color: Int) : this(context, null, border_color) {}

    override fun onDraw(canvas: Canvas) {
        val paint = Paint()
        paint.color = border_color
        paint.strokeWidth = 5f
        canvas.drawLine(0f, 0f, this.width.toFloat(), 0f, paint)
        canvas.drawLine(0f, 0f, 0f, this.height.toFloat(), paint)
        canvas.drawLine(this.width.toFloat(), 0f, this.width.toFloat(), this.height.toFloat(), paint)
        canvas.drawLine(0f, this.height.toFloat(), this.width.toFloat(), this.height.toFloat(), paint)
        super.onDraw(canvas)
    }
}