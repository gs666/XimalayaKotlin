package com.rickon.ximalayakotlin.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.rickon.ximalayakotlin.R
import com.rickon.ximalayakotlin.adapter.ProvinceListAdapter.ViewHolder
import com.ximalaya.ting.android.opensdk.model.live.provinces.Province

/**
 * @Author: 高烁
 * @CreateDate: 2019-06-06 14:15
 * @Email: gaoshuo521@foxmail.com
 */
class ProvinceListAdapter : RecyclerView.Adapter<ViewHolder> {

    private var mContext: Context
    private var provinceList: List<Province>
    private lateinit var itemClickListener: IKotlinItemClickListener
    private var selectItem = -1

    constructor(mContext: Context, list: List<Province>) {
        this.mContext = mContext
        this.provinceList = list
    }

    fun setSelectItem(selectItem: Int) {
        this.selectItem = selectItem
    }

    class ViewHolder(var provinceItemView: View) : RecyclerView.ViewHolder(provinceItemView) {
        var textViewTitle: TextView = provinceItemView.findViewById(R.id.id_textview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.text_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textViewTitle.text = provinceList!![position].provinceName

        if (position == selectItem) holder.provinceItemView.setBackgroundResource(R.color.selected_bg) else holder.provinceItemView.setBackgroundResource(R.color.white)

        holder.provinceItemView.setOnClickListener {
            itemClickListener.onItemClickListener(position)
        }
    }

    override fun getItemCount(): Int {
        return provinceList.size
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
