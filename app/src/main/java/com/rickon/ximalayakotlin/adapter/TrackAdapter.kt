package com.rickon.ximalayakotlin.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.rickon.ximalayakotlin.R
import com.rickon.ximalayakotlin.util.GlobalUtil
import com.ximalaya.ting.android.opensdk.model.track.Track
import io.alterac.blurkit.BlurKit
import kotlinx.android.synthetic.main.track_list_header.view.*

/**
 * @Description:声音列表适配器
 * @Author:      高烁
 * @CreateDate:  2019-07-26 18:23
 * @Email:       gaoshuo521@foxmail.com
 */
class TrackAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private var mContext: Context
    private var trackList: List<Track>
    private var albumInfos = emptyArray<String>()
    //标题，作者，订阅，播放次数，介绍，封面url
    private var showImageCover = false
    private lateinit var itemClickListener: IKotlinItemClickListener

    constructor(mContext: Context, list: List<Track>, albumInfos: Array<String>, showImageCover: Boolean) {
        this.mContext = mContext
        this.trackList = list
        this.albumInfos = albumInfos;
        this.showImageCover = showImageCover
    }

    /**
     * HeaderViewHolder
     */
    class HeaderViewHolder(var headerView: View) : RecyclerView.ViewHolder(headerView) {
        var tvTitle: TextView = headerView.findViewById(R.id.tvAlbumTitle)
        var tvAlbumAuthor: TextView = headerView.findViewById(R.id.tvAlbumAuthor)
        var tvAlbumSubscribe: TextView = headerView.findViewById(R.id.tvAlbumSubscribe)
        var tvAlbumPlayCount: TextView = headerView.findViewById(R.id.tvAlbumPlayCount)
        var tvAlbumIntro: TextView = headerView.findViewById(R.id.tvAlbumIntro)
        var ivAlbumCover: ImageView = headerView.findViewById(R.id.ivAlbumCover)
        var ivAlbumCoverBg: ImageView = headerView.findViewById(R.id.ivAlbumCoverBg)

    }

    /**
     * HeaderView2Holder
     */
    class HeaderView2Holder(var headerView2: View) : RecyclerView.ViewHolder(headerView2) {
        var llPlayAll: LinearLayout = headerView2.findViewById(R.id.llPlayAll)
    }

    /**
     * 内容ViewHolder
     */
    class ContentViewHolder(var trackItemView: View) : RecyclerView.ViewHolder(trackItemView) {
        var imageCover: ImageView = trackItemView.findViewById(R.id.id_image_cover)
        var textViewTitle: TextView = trackItemView.findViewById(R.id.id_title_tv)
        var textViewPlayCount: TextView = trackItemView.findViewById(R.id.id_play_count_tv)
        var textViewDuration: TextView = trackItemView.findViewById(R.id.id_duration_tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == ITEM_TYPE_HEADER) {
            return HeaderViewHolder(LayoutInflater.from(mContext).inflate(R.layout.track_list_header, parent, false))
        } else if (viewType == ITEM_TYPE_HEADER2) {
            return HeaderView2Holder(LayoutInflater.from(mContext).inflate(R.layout.track_list_header2, parent, false))
        } else {
            return ContentViewHolder(LayoutInflater.from(mContext).inflate(R.layout.track_list_item, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ContentViewHolder) {
            val item = trackList[position - 2]
            with(item) {
                holder.textViewTitle.text = trackTitle
                holder.textViewPlayCount.text = GlobalUtil.formatNum(playCount.toString(), false)
                holder.textViewDuration.text = DateUtils.formatElapsedTime(duration.toLong())
                if (showImageCover) {
                    Glide.with(mContext)
                            .load(coverUrlLarge)
                            .apply(RequestOptions.bitmapTransform(RoundedCorners(15)))
                            .into(holder.imageCover)
                }
            }

            holder.trackItemView.setOnClickListener {
                itemClickListener.onItemClickListener(position)
            }
        } else if (holder is HeaderViewHolder) {
            holder.headerView.setOnClickListener {
                itemClickListener.onItemClickListener(position)
            }

            holder.headerView.tvAlbumTitle.text = albumInfos[0]
            holder.headerView.tvAlbumAuthor.text = albumInfos[1]
            holder.headerView.tvAlbumSubscribe.text = albumInfos[2]
            holder.headerView.tvAlbumPlayCount.text = albumInfos[3]
            holder.headerView.tvAlbumIntro.text = albumInfos[4]

            BlurKit.init(mContext)
            Glide.with(mContext)
                    .load(albumInfos[5])
                    .listener(object : RequestListener<Drawable> {
                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            holder.headerView.ivAlbumCover.setImageDrawable(resource)

                            holder.headerView.ivAlbumCoverBg.setImageBitmap(BlurKit.getInstance().blur(holder.headerView.ivAlbumCover, 25))

                            return true
                        }

                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            return true
                        }
                    })
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(15)))
                    .submit()


        } else if (holder is HeaderView2Holder) {
            holder.headerView2.setOnClickListener {
                itemClickListener.onItemClickListener(position)
            }
        }

    }

    override fun getItemCount(): Int {
        return trackList.size + 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (position > 1)
            ITEM_TYPE_CONTENT
        else
            position
    }

    // 提供set方法
    fun setOnKotlinItemClickListener(itemClickListener: IKotlinItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    //自定义接口
    interface IKotlinItemClickListener {
        fun onItemClickListener(position: Int)
    }

    companion object {

        private const val ITEM_TYPE_HEADER = 0
        private const val ITEM_TYPE_HEADER2 = 1
        private const val ITEM_TYPE_CONTENT = 2

    }
}