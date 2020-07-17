package com.rickon.ximalayakotlin.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
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
class ProvinceListAdapter(private var mContext: Context, list: List<Province>) : RecyclerView.Adapter<ViewHolder>() {

    private var provinceList: List<Province> = list
    private lateinit var itemClickListener: IKotlinItemClickListener

    class ViewHolder(var provinceItemView: View) : RecyclerView.ViewHolder(provinceItemView) {
        var provinceTitle: TextView = provinceItemView.findViewById(R.id.province_tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.province_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.provinceTitle.text = provinceList[position].provinceName
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
