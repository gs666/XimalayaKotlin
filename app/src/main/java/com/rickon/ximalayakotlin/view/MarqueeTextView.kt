package com.rickon.ximalayakotlin.view

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView


/**
 * @Description:
 * @Author:      高烁
 * @CreateDate:  2019-07-31 09:50
 * @Email:       gaoshuo521@foxmail.com
 */
class MarqueeTextView : TextView {

    override fun isFocused(): Boolean = true

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
}