package com.rickon.ximalayakotlin.activities

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.*
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.rickon.ximalayakotlin.R
import com.rickon.ximalayakotlin.adapter.TrackAdapter
import com.rickon.ximalayakotlin.util.GlobalUtil
import com.rickon.ximalayakotlin.util.XimalayaKotlin
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack
import com.ximalaya.ting.android.opensdk.model.PlayableModel
import com.ximalaya.ting.android.opensdk.model.album.Album
import com.ximalaya.ting.android.opensdk.model.track.Track
import com.ximalaya.ting.android.opensdk.model.track.TrackList
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException
import io.alterac.blurkit.BlurKit
import kotlinx.android.synthetic.main.activity_album.*

/**
 * @Description:专辑列表页面
 * @Author:      高烁
 * @CreateDate:  2019-06-14 15:06
 * @Email:       gaoshuo521@foxmail.com
 */
class AlbumActivity : BaseActivity(), OnClickListener {

    private var currentAlbumId = ""
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var album: Album
    private var isLoadSuccess = false
    private var haveMore = true

    private lateinit var tracksList: MutableList<Track>

    private lateinit var addTracksList: MutableList<Track>
    private var currentPage = 1

    private lateinit var mPlayerServiceManager: XmPlayerManager
    private val mPlayerStatusListener = object : IXmPlayerStatusListener {
        override fun onSoundSwitch(laModel: PlayableModel?, curModel: PlayableModel) {
            trackAdapter.notifyDataSetChanged()
        }

        override fun onSoundPrepared() {}

        override fun onSoundPlayComplete() {}

        override fun onPlayStop() {}

        override fun onPlayStart() {}

        override fun onPlayProgress(currPos: Int, duration: Int) {}

        override fun onPlayPause() {}

        override fun onError(exception: XmPlayerException): Boolean {
            return false

        }

        override fun onBufferingStop() {}

        override fun onBufferingStart() {}

        override fun onBufferProgress(percent: Int) {}

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)

        album = intent.getParcelableExtra("album")
        currentAlbumId = album.id.toString()

        initView()

        loadTracksByAlbumId()

        initListener()

        mPlayerServiceManager = XmPlayerManager.getInstance(applicationContext)
        mPlayerServiceManager.addPlayerStatusListener(mPlayerStatusListener)
    }

    private fun loadTracksByAlbumId() {
        val map = HashMap<String, String>()
        map[DTransferConstants.ALBUM_ID] = currentAlbumId
        //有声书
        map[DTransferConstants.SORT] = "asc"
        map[DTransferConstants.PAGE_SIZE] = PAGE_SIZE

        //最火
        CommonRequest.getTracks(map, object : IDataCallBack<TrackList> {
            override fun onSuccess(p0: TrackList?) {
                p0?.tracks?.let {
                    if (it.size > 0) {
                        tracksList = it
                        isLoadSuccess = true

                        BlurKit.init(this@AlbumActivity)

                        Glide.with(applicationContext)
                                .load(p0.coverUrlLarge)
                                .listener(object : RequestListener<Drawable> {
                                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                        cover_image.setImageDrawable(resource)

                                        cover_bg.setImageBitmap(BlurKit.getInstance().blur(cover_image, 25))

                                        return true
                                    }

                                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                        return true
                                    }
                                })
                                .apply(RequestOptions.bitmapTransform(RoundedCorners(15)))
                                .submit()

                        tracks_recycler.layoutManager = LinearLayoutManager(XimalayaKotlin.context)
                        trackAdapter = TrackAdapter(applicationContext, p0.tracks, false)
                        tracks_recycler.adapter = trackAdapter

                        trackAdapter.setOnKotlinItemClickListener(object : TrackAdapter.IKotlinItemClickListener {
                            override fun onItemClickListener(position: Int) {
                                Log.d(TAG, position.toString())
                                mPlayerServiceManager.playList(tracksList, position)

                                //跳转PlayingActivity
                                val intent = Intent(this@AlbumActivity, PlayingActivity::class.java)
                                this@AlbumActivity.startActivity(intent)

                                trackAdapter.notifyDataSetChanged()

                            }
                        })

//                    val msg = Message()
//                    msg.what = LOAD_SUCCESS
//                    uiHandler.sendMessage(msg)
                    }

                }
            }

            override fun onError(p0: Int, p1: String?) {
                isLoadSuccess = false
            }
        })
    }

    private fun initView() {
        //专辑相关信息
        album_title.text = album.albumTitle
        album_author.text = album.announcer.nickname
        album_subscribe.text = "已订阅：${GlobalUtil.formatNum(album.subscribeCount.toString(), false)}"
        album_play_count.text = "播放${GlobalUtil.formatNum(album.playCount.toString(), false)}"
        album_intro.text = album.albumIntro

        try_to_get_info.visibility = GONE
        tracks_recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1) && haveMore) {
                    currentPage++
                    updateTrackList(currentPage)
                    try_to_get_info.visibility = VISIBLE
                    Log.d(TAG, "拉到底部")

                }
            }
        })
    }

    private fun updateTrackList(currentPages: Int) {
        val map = HashMap<String, String>()
        map[DTransferConstants.ALBUM_ID] = currentAlbumId
        //有声书
        map[DTransferConstants.SORT] = "asc"
        map[DTransferConstants.PAGE_SIZE] = PAGE_SIZE
        map[DTransferConstants.PAGE] = currentPages.toString()

        //最火
        CommonRequest.getTracks(map, object : IDataCallBack<TrackList> {
            override fun onSuccess(p0: TrackList?) {
                p0?.tracks?.let {
                    if (it.size > 0) {
                        addTracksList = it
                        tracksList.addAll(addTracksList)
                        trackAdapter.notifyDataSetChanged()
                        try_to_get_info.visibility = GONE
                    } else {
                        //没有内容
                        haveMore = false
                        try_to_get_info.visibility = GONE
                        Toast.makeText(XimalayaKotlin.context, "没有更多啦", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onError(p0: Int, p1: String?) {
                try_to_get_info.visibility = GONE
                currentPage--
                Toast.makeText(XimalayaKotlin.context, "加载出错啦", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun initListener() {
        return_btn.setOnClickListener(this)
        play_all_btn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.return_btn -> finish()
            R.id.play_all_btn -> {
                if (isLoadSuccess) {
                    //播放列表
                    mPlayerServiceManager.playList(tracksList, 0)
                }
            }
        }
    }

    override fun onDestroy() {
        mPlayerServiceManager.removePlayerStatusListener(mPlayerStatusListener)
        super.onDestroy()
    }

    companion object {
        private const val TAG = "AlbumActivity"
        private const val PAGE_SIZE = "20"
    }
}
