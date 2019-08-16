package com.rickon.ximalayakotlin.view

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout

/*
 * 获取、设置控件信息
 */
object WidgetController {
    /*
     * 获取控件宽
     */
    fun getWidth(view: View): Int {
        val w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view.measure(w, h)
        return view.measuredWidth
    }

    /*
     * 获取控件高
     */
    fun getHeight(view: View): Int {
        val w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view.measure(w, h)
        return view.measuredHeight
    }

    /*
     * 设置控件所在的位置X，并且不改变宽高，
     * X为绝对位置，此时Y可能归0
     */
    fun setLayoutX(view: View, x: Int) {
        val margin = ViewGroup.MarginLayoutParams(view.layoutParams)
        margin.setMargins(x, margin.topMargin, x + margin.width, margin.bottomMargin)
        val layoutParams = RelativeLayout.LayoutParams(margin)
        view.layoutParams = layoutParams
    }

    /*
     * 设置控件所在的位置Y，并且不改变宽高，
     * Y为绝对位置，此时X可能归0
     */
    fun setLayoutY(view: View, y: Int) {
        val margin = ViewGroup.MarginLayoutParams(view.layoutParams)
        margin.setMargins(margin.leftMargin, y, margin.rightMargin, y + margin.height)
        val layoutParams = RelativeLayout.LayoutParams(margin)
        view.layoutParams = layoutParams
    }

    /*
     * 设置控件所在的位置YY，并且不改变宽高，
     * XY为绝对位置
     */
    fun setLayout(view: View, x: Int, y: Int) { //ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(group.getLayoutParams());
        val margin = ViewGroup.MarginLayoutParams(view.layoutParams)
        margin.setMargins(x, y, 0, 0)
        val layoutParams = FrameLayout.LayoutParams(margin)
        //        RelativeLayout.LayoutParams vlp = new RelativeLayout.LayoutParams(
        //                ViewGroup.LayoutParams.WRAP_CONTENT,
        //                ViewGroup.LayoutParams.WRAP_CONTENT);
        //        vlp.setMargins(x,y, x+margin.width, y+margin.height);
        view.layoutParams = layoutParams
        // view.setLayoutParams(vlp);
    }
}
