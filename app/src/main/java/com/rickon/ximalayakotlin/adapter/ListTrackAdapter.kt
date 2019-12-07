package com.rickon.ximalayakotlin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rickon.ximalayakotlin.R
import com.ximalaya.ting.android.opensdk.model.track.Track

/**
 * @Description:弹出列表适配器
 * @Author:      高烁
 * @CreateDate:  2019-07-26 18:23
 * @Email:       gaoshuo521@foxmail.com
 */
class ListTrackAdapter : RecyclerView.Adapter<ListTrackAdapter.ViewHolder> {
    private var mContext: Context
    private var trackList: List<Track>
    private lateinit var itemClickListener: IKotlinItemClickListener

    constructor(mContext: Context, list: List<Track>) {
        this.mContext = mContext
        this.trackList = list
    }

    class ViewHolder(var trackItemView: View) : RecyclerView.ViewHolder(trackItemView) {
        var playingFlag: ImageView = trackItemView.findViewById(R.id.id_playing_flag)
        var textViewTitle: TextView = trackItemView.findViewById(R.id.id_title_tv)
        var textViewSinger: TextView = trackItemView.findViewById(R.id.id_singer_tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.playing_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = trackList[position]
        with(item) {
            holder.textViewTitle.text = trackTitle
            holder.textViewSinger.text = announcer.nickname
        }

        holder.trackItemView.setOnClickListener {
            itemClickListener.onItemClickListener(position)
        }
    }

    override fun getItemCount(): Int {
        return trackList.size
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