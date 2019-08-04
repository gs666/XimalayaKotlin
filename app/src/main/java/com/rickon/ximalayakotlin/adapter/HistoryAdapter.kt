package com.rickon.ximalayakotlin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.rickon.ximalayakotlin.R
import com.rickon.ximalayakotlin.model.HistoryItem

/**
 * @Description:
 * @Author:      高烁
 * @CreateDate:  2019-07-19 11:44
 * @Email:       gaoshuo521@foxmail.com
 */
class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private var mContext: Context
    private var historyList: List<HistoryItem>
    private lateinit var itemClickListener: IKotlinItemClickListener

    constructor(mContext: Context, list: List<HistoryItem>) {
        this.mContext = mContext
        this.historyList = list
    }

    class ViewHolder(var radioItemView: View) : RecyclerView.ViewHolder(radioItemView) {
        var imageCover: ImageView = radioItemView.findViewById(R.id.id_image_cover)
        var textViewTitle: TextView = radioItemView.findViewById(R.id.id_title_tv)
        var currentTrackTitle: TextView = radioItemView.findViewById(R.id.id_current_track)
        var textViewSinger: TextView = radioItemView.findViewById(R.id.id_singer_tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.history_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textViewTitle.text = historyList[position].itemTitle
        holder.currentTrackTitle.text = historyList[position].trackTitle
        holder.textViewSinger.text = historyList[position].albumAuthor
        Glide.with(mContext)
                .load(historyList[position].itemImagePath).apply(RequestOptions.bitmapTransform(RoundedCorners(15)))
                .into(holder.imageCover)

        holder.radioItemView.setOnClickListener {
            itemClickListener.onItemClickListener(position)
        }
    }

    override fun getItemCount(): Int {
        return historyList.size
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