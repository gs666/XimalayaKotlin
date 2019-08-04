package com.rickon.ximalayakotlin.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.rickon.ximalayakotlin.R
import com.rickon.ximalayakotlin.activities.PlayingActivity
import com.rickon.ximalayakotlin.util.XimalayaKotlin
import com.ximalaya.ting.android.opensdk.model.PlayableModel
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio
import com.ximalaya.ting.android.opensdk.model.live.schedule.Schedule
import com.ximalaya.ting.android.opensdk.model.track.Track
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException

class QuickControlsFragment : BaseFragment() {

    private lateinit var rootView: View
    private lateinit var mPlayerManager: XmPlayerManager

    private lateinit var playOrPauseBtn: ImageView
    private lateinit var currentListBtn: ImageView
    private lateinit var currentTitle:TextView
    private lateinit var currentSinger:TextView
    private lateinit var playBarCover:ImageView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.bottom_nav, container, false)
        this.rootView = rootView

        //初始化播放器
        mPlayerManager = XmPlayerManager.getInstance(XimalayaKotlin.context)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated")
        //按键相关使用需要在onViewCreated，否则空对象

        //添加播放器监听
        mPlayerManager.addPlayerStatusListener(mPlayerStatusListener)

        super.onViewCreated(view, savedInstanceState)

        playOrPauseBtn = view.findViewById(R.id.id_play_or_pause)
        currentListBtn = view.findViewById(R.id.id_current_list)
        currentTitle = view.findViewById(R.id.id_play_bar_title)
        currentSinger = view.findViewById(R.id.id_play_bar_singer)
        playBarCover = view.findViewById(R.id.id_play_bar_image)

        //暂停/播放
        playOrPauseBtn.setOnClickListener {
            if (mPlayerManager.isPlaying) mPlayerManager.pause() else mPlayerManager.play()
        }

        currentListBtn.setOnClickListener {
            Toast.makeText(XimalayaKotlin.context, "暂未开发此功能", Toast.LENGTH_SHORT).show()
        }


        //跳转正在播放页面
        rootView.setOnClickListener {
            val intent = Intent(context, PlayingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            XimalayaKotlin.context!!.startActivity(intent)
        }
    }


    //播放监听器
    private val mPlayerStatusListener = object : IXmPlayerStatusListener {

        override fun onSoundPrepared() {
            Log.i(TAG, "onSoundPrepared")
        }

        override fun onSoundSwitch(laModel: PlayableModel?, curModel: PlayableModel) {
            Log.i(TAG, "onSoundSwitch index:$curModel")

            val model = mPlayerManager.currSound
            val title: String?
            val singer: String?
            val coverUrl: String?
            when (model) {
                is Track -> {
                    title = model.trackTitle
                    singer = model.announcer.nickname
                    coverUrl = model.coverUrlLarge
                }
                is Schedule -> {
                    title = model.relatedProgram.programName
                    singer = model.radioName
                    coverUrl = model.relatedProgram.backPicUrl
                }
                is Radio -> {
                    title = model.programName
                    singer = model.radioName
                    coverUrl = model.coverUrlLarge
                }
                else -> {
                    title = ""
                    singer = ""
                    coverUrl = ""
                }
            }

            currentTitle.text = title
            currentSinger.text = singer
            Glide.with(XimalayaKotlin.context!!)
                    .load(coverUrl).apply(RequestOptions.bitmapTransform(RoundedCorners(15)))
                    .into(playBarCover)

        }

        override fun onPlayStop() {
            Log.i(TAG, "onPlayStop")
            playOrPauseBtn.setImageResource(R.drawable.ic_play)
        }

        override fun onPlayStart() {
            Log.i(TAG, "onPlayStart")
            playOrPauseBtn.setImageResource(R.drawable.ic_pause)
        }

        //播放进度回调
        override fun onPlayProgress(currPos: Int, duration: Int) {

        }

        override fun onPlayPause() {
            Log.i(TAG, "onPlayPause")
            playOrPauseBtn.setImageResource(R.drawable.ic_play)

        }

        override fun onSoundPlayComplete() {
            Log.i(TAG, "onSoundPlayComplete")
            playOrPauseBtn.setImageResource(R.drawable.ic_play)
            XmPlayerManager.getInstance(context).pause()
            Log.d(TAG, "播放完成")
        }

        override fun onError(exception: XmPlayerException): Boolean {
            Log.i(TAG, "onError:${exception.message}")
            playOrPauseBtn.setImageResource(R.drawable.ic_play)
            return false
        }

        override fun onBufferProgress(position: Int) {
        }

        override fun onBufferingStart() {

        }

        override fun onBufferingStop() {

        }

    }

    companion object {
        private const val TAG = "QuickControlsFragment"

        fun newInstance(): QuickControlsFragment {
            return QuickControlsFragment()
        }
    }


}