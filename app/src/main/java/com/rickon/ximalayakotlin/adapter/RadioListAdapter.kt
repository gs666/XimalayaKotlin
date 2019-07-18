package com.rickon.ximalayakotlin.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.rickon.ximalayakotlin.R
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio

/**
 * @Author: 高烁
 * @CreateDate: 2019-06-06 14:46
 * @Email: gaoshuo521@foxmail.com
 */
class RadioListAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<RadioListAdapter.ViewHolder> {
    private var mContext: Context
    private var radioList: List<Radio>
    private lateinit var itemClickListener: IKotlinItemClickListener
    private var selectItem = -1

    constructor(mContext: Context, list: List<Radio>) {
        this.mContext = mContext
        this.radioList = list
    }

    fun setSelectItem(selectItem: Int) {
        this.selectItem = selectItem
    }

    class ViewHolder(var radioItemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(radioItemView) {
        var imageCover:ImageView = radioItemView.findViewById(R.id.id_image_cover)
        var textViewTitle: TextView = radioItemView.findViewById(R.id.id_title_tv)
        var textViewIntro: TextView = radioItemView.findViewById(R.id.id_singer_tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.radio_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textViewTitle.text = radioList[position].radioName
        holder.textViewIntro.text = "正在播放：${radioList[position].programName}"
        Glide.with(mContext)
            .load(radioList[position].coverUrlLarge).apply(RequestOptions.bitmapTransform(RoundedCorners(15)))
            .into(holder.imageCover)

        if (position == selectItem) {
            holder.radioItemView.setBackgroundResource(R.color.selected_bg)
        } else {
            holder.radioItemView.setBackgroundResource(R.color.white)
        }

        holder.radioItemView.setOnClickListener {
            itemClickListener.onItemClickListener(position)
        }
    }

    override fun getItemCount(): Int {
        return radioList.size
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
