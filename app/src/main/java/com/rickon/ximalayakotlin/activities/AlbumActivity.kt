package com.rickon.ximalayakotlin.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import android.view.View.*
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    private var tracksList = mutableListOf<Track>()
    private var albumInfoArray = arrayOf("", "", "", "", "", "")

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

    // 创建一个Handler
    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg?.what) {
                MSG_LOAD_DATA -> {
                    loadTracksByAlbumId()
                }
                else -> {
                    Log.e(TAG, "msg" + msg?.what)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)

        album = intent.getParcelableExtra("album")
        currentAlbumId = album.id.toString()

        initList()
        initListener()
        loadTracksByAlbumId()
        mPlayerServiceManager = XmPlayerManager.getInstance(applicationContext)
        mPlayerServiceManager.addPlayerStatusListener(mPlayerStatusListener)
    }

    private fun loadTracksByAlbumId() {
        val map = HashMap<String, String>()
        map[DTransferConstants.ALBUM_ID] = currentAlbumId
        //有声书
        map[DTransferConstants.SORT] = "asc"
        map[DTransferConstants.PAGE_SIZE] = PAGE_SIZE
        map[DTransferConstants.PAGE] = currentPage.toString()

        //最火
        CommonRequest.getTracks(map, object : IDataCallBack<TrackList> {
            override fun onSuccess(p0: TrackList?) {
                p0?.tracks?.let {
                    if (it.size > 0) {

                        isLoadSuccess = true
                        addTracksList = it
                        tracksList.addAll(addTracksList)
                        trackAdapter.notifyDataSetChanged()
                    } else {
                        haveMore = false
                        Toast.makeText(XimalayaKotlin.context, "没有更多啦", Toast.LENGTH_SHORT).show()
                    }

                }
            }

            override fun onError(p0: Int, p1: String?) {
                isLoadSuccess = false
            }
        })
    }

    private fun initList() {
        //专辑相关信息
        albumInfoArray[0] = album.albumTitle
        albumInfoArray[1] = album.announcer.nickname
        albumInfoArray[2] = "已订阅：${GlobalUtil.formatNum(album.subscribeCount.toString(), false)}"
        albumInfoArray[3] = "播放${GlobalUtil.formatNum(album.playCount.toString(), false)}"
        albumInfoArray[4] = album.albumIntro
        albumInfoArray[5] = album.coverUrlLarge

        tracks_recycler.layoutManager = LinearLayoutManager(XimalayaKotlin.context)
        trackAdapter = TrackAdapter(applicationContext, tracksList, albumInfoArray, false)
        tracks_recycler.adapter = trackAdapter

        trackAdapter.setOnKotlinItemClickListener(object : TrackAdapter.IKotlinItemClickListener {
            override fun onItemClickListener(position: Int) {
                if (position == 0) {

                } else if (position == 1) {
                    if (isLoadSuccess) {
                        //播放列表
                        mPlayerServiceManager.playList(tracksList, 0)
                    }
                } else {
                    Log.d(TAG, position.toString())
                    mPlayerServiceManager.playList(tracksList, position - 2)

                    //跳转PlayingActivity
                    val intent = Intent(this@AlbumActivity, PlayingActivity::class.java)
                    this@AlbumActivity.startActivity(intent)

                    trackAdapter.notifyDataSetChanged()
                }
            }
        })


        tracks_recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1) && tracksList.size > 0 && haveMore) {
                    currentPage++
                    mHandler.sendEmptyMessage(MSG_LOAD_DATA)
                    Log.d(TAG, "拉到底部")

                }
            }
        })
    }


    private fun initListener() {
        returnBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.returnBtn -> finish()
        }
    }

    override fun onDestroy() {
        mPlayerServiceManager.removePlayerStatusListener(mPlayerStatusListener)
        super.onDestroy()
    }

    companion object {
        private const val TAG = "AlbumActivity"
        private const val PAGE_SIZE = "200"
        private const val MSG_LOAD_DATA = 0
    }
}
