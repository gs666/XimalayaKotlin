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
import com.ximalaya.ting.android.opensdk.model.album.Album

/**
 * @Description:
 * @Author:      高烁
 * @CreateDate:  2019-07-24 17:24
 * @Email:       gaoshuo521@foxmail.com
 */
class AlbumAdapter(private var mContext: Context, list: List<Album>) : RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {
    private var albumList: List<Album> = list
    private lateinit var itemClickListener: IKotlinItemClickListener

    class ViewHolder(var albumItemView: View) : RecyclerView.ViewHolder(albumItemView) {
        var imageCover: ImageView = albumItemView.findViewById(R.id.id_image_cover)
        var textViewTitle: TextView = albumItemView.findViewById(R.id.id_title_tv)
        var textViewIntro: TextView = albumItemView.findViewById(R.id.id_singer_tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.radio_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = albumList[position]
        with(item){
            holder.textViewTitle.text = albumTitle
            holder.textViewIntro.text = albumIntro
            Glide.with(mContext)
                    .load(coverUrlLarge).apply(RequestOptions.bitmapTransform(RoundedCorners(15)))
                    .into(holder.imageCover)
        }

        holder.albumItemView.setOnClickListener {
            itemClickListener.onItemClickListener(position)
        }
    }

    override fun getItemCount(): Int {
        return albumList.size
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