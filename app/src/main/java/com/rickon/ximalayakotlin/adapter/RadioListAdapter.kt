package com.rickon.ximalayakotlin.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.rickon.ximalayakotlin.R
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio

/**
 * @Author: 高烁
 * @CreateDate: 2019-06-06 14:46
 * @Email: gaoshuo521@foxmail.com
 */
class RadioListAdapter : RecyclerView.Adapter<RadioListAdapter.ViewHolder> {
    private var mContext: Context? = null
    private var radioList: List<Radio>? = null
    private var itemClickListener: IKotlinItemClickListener? = null
    private var selectItem = -1

    constructor(mContext: Context, list: List<Radio>) {
        this.mContext = mContext
        this.radioList = list
    }

    fun setSelectItem(selectItem: Int) {
        this.selectItem = selectItem
    }

    class ViewHolder(var radioItemView: View) : RecyclerView.ViewHolder(radioItemView) {
        var textViewTitle: TextView = radioItemView.findViewById(R.id.id_textview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.text_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textViewTitle.text = radioList!![position].radioName

        if (position == selectItem) {
            holder.radioItemView.setBackgroundResource(R.color.selected_bg)
        } else {
            holder.radioItemView.setBackgroundResource(R.color.white)
        }

        holder.radioItemView.setOnClickListener {
            itemClickListener!!.onItemClickListener(position)
        }
    }

    override fun getItemCount(): Int {
        return radioList!!.size
    }

    // 提供set方法
    fun setOnKotlinItemClickListener(itemClickListener: IKotlinItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    //自定义接口
    interface IKotlinItemClickListener {
        fun onItemClickListener(position: Int)
    }
}
