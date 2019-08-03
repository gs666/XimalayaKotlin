package com.rickon.ximalayakotlin.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import kotlinx.android.synthetic.main.bottom_nav.*

class QuickControlsFragment : BaseFragment() {

    private lateinit var rootView: View
    private lateinit var mPlayerManager: XmPlayerManager


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.bottom_nav, container, false)
        this.rootView = rootView

        //初始化播放器
        mPlayerManager = XmPlayerManager.getInstance(XimalayaKotlin.context)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //按键相关使用需要在onViewCreated，否则空对象
        //添加播放器监听
        mPlayerManager.addPlayerStatusListener(mPlayerStatusListener)

        super.onViewCreated(view, savedInstanceState)
        //暂停/播放
        id_play_or_pause.setOnClickListener {
            if (mPlayerManager.isPlaying) mPlayerManager.pause() else mPlayerManager.play()
        }

        id_current_list.setOnClickListener {
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

            id_play_bar_title.text = title
            id_play_bar_singer.text = singer
            Glide.with(XimalayaKotlin.context!!)
                    .load(coverUrl).apply(RequestOptions.bitmapTransform(RoundedCorners(15)))
                    .into(id_play_bar_image)

        }

        override fun onPlayStop() {
            Log.i(TAG, "onPlayStop")
            id_play_or_pause.setImageResource(R.drawable.ic_play)
        }

        override fun onPlayStart() {
            Log.i(TAG, "onPlayStart")
            id_play_or_pause.setImageResource(R.drawable.ic_pause)
        }

        //播放进度回调
        override fun onPlayProgress(currPos: Int, duration: Int) {

        }

        override fun onPlayPause() {
            Log.i(TAG, "onPlayPause")
            id_play_or_pause.setImageResource(R.drawable.ic_play)

        }

        override fun onSoundPlayComplete() {
            Log.i(TAG, "onSoundPlayComplete")
            id_play_or_pause.setImageResource(R.drawable.ic_play)
            XmPlayerManager.getInstance(context).pause()
            Toast.makeText(context, "播放完成", Toast.LENGTH_SHORT).show()
        }

        override fun onError(exception: XmPlayerException): Boolean {
            Log.i(TAG, "onError:${exception.message}")
            id_play_or_pause.setImageResource(R.drawable.ic_play)
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