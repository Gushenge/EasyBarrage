package com.gushenge.easybarrage

import android.graphics.Color

class Barrage private constructor(var content: String, var color: Int, var isShowBorder: Boolean, var backGroundColor: Int) {

    constructor(content: String) : this(content, R.color.black, false, Color.WHITE) {}
    constructor(content: String, color: Int) : this(content, color, false, Color.WHITE) {}
    constructor(content: String, showBorder: Boolean) : this(content, R.color.black, showBorder, Color.WHITE) {}
    constructor(content: String, color: Int, backGroundColor: Int) : this(content, color, false, backGroundColor) {}
}