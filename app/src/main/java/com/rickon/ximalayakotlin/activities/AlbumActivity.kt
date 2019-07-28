package com.rickon.ximalayakotlin.activities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.rickon.ximalayakotlin.R
import com.rickon.ximalayakotlin.adapter.TrackAdapter
import com.rickon.ximalayakotlin.util.GlobalUtil
import com.rickon.ximalayakotlin.util.XimalayaKotlin
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack
import com.ximalaya.ting.android.opensdk.model.album.Album
import com.ximalaya.ting.android.opensdk.model.track.TrackList
import io.alterac.blurkit.BlurKit
import kotlinx.android.synthetic.main.activity_album.*

class AlbumActivity : BaseActivity(), View.OnClickListener {

    private var currentAlbumId = ""
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var album: Album
    private val res = XimalayaKotlin.context?.resources


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)

        album = intent.getParcelableExtra("album")
        currentAlbumId = album.id.toString()

        initView()

        loadTracksByAlbumId()

        initListener()
    }

    private fun loadTracksByAlbumId() {
        val map = HashMap<String, String>()
        map[DTransferConstants.ALBUM_ID] = currentAlbumId
        //有声书
        map[DTransferConstants.SORT] = "asc"

        //最火
        CommonRequest.getTracks(map, object : IDataCallBack<TrackList> {
            override fun onSuccess(p0: TrackList?) {
                if (p0?.tracks!!.size > 0) {

                    Glide.with(applicationContext)
                            .load(p0.coverUrlLarge)
                            .apply(RequestOptions.bitmapTransform(RoundedCorners(15)))
                            .into(cover_image)

//                    BlurKit.init(this@AlbumActivity)
//
//                    Glide.with(this@AlbumActivity)
//                            .load(BlurKit.getInstance().blur(cover_image, 25))
//                            .into(cover_bg)

                    tracks_recycler.layoutManager = LinearLayoutManager(XimalayaKotlin.context)
                    trackAdapter = TrackAdapter(applicationContext, p0.tracks)
                    tracks_recycler.adapter = trackAdapter

                    trackAdapter.setOnKotlinItemClickListener(object : TrackAdapter.IKotlinItemClickListener {
                        override fun onItemClickListener(position: Int) {
                            Log.d(TAG, position.toString())

                            trackAdapter.notifyDataSetChanged()

                        }
                    })

//                    val msg = Message()
//                    msg.what = LOAD_SUCCESS
//                    uiHandler.sendMessage(msg)
                }
            }

            override fun onError(p0: Int, p1: String?) {
            }
        })
    }

    private fun initView() {
        //专辑相关信息
        album_title.text = album.albumTitle
        album_author.text = album.announcer.nickname
        album_subscribe.text = "已订阅：${GlobalUtil.formatNum(album.subscribeCount.toString(),false)}"
        album_play_count.text = "播放${GlobalUtil.formatNum(album.playCount.toString(),false)}"
        album_intro.text = album.albumIntro
    }


    private fun initListener() {
        return_btn.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.return_btn -> finish()

        }
    }

    companion object {

        private const val TAG = "BookCityFragment"
        private const val LOAD_SUCCESS = 1

    }
}
