package com.rickon.ximalayakotlin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rickon.ximalayakotlin.R

/**
 * @Description:
 * @Author:      高烁
 * @CreateDate:  2019-07-19 11:44
 * @Email:       gaoshuo521@foxmail.com
 */
class SimpleItemAdapter : RecyclerView.Adapter<SimpleItemAdapter.ViewHolder> {
    private var mContext: Context
    private var itemList: List<String>
    private lateinit var itemClickListener: IKotlinItemClickListener

    constructor(mContext: Context, list: List<String>) {
        this.mContext = mContext
        this.itemList = list
    }

    class ViewHolder(var simpleItemView: View) : RecyclerView.ViewHolder(simpleItemView) {
        var textViewTitle: TextView = simpleItemView.findViewById(R.id.item_tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.simple_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textViewTitle.text = itemList[position]
        //首行特殊颜色
        if(position == 0) holder.textViewTitle.setTextColor(mContext.resources.getColor(R.color.tab_selected,null))

        holder.simpleItemView.setOnClickListener {
            itemClickListener.onItemClickListener(position)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
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