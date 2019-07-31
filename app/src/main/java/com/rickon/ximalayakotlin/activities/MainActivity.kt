package com.rickon.ximalayakotlin.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayout
import com.rickon.ximalayakotlin.R
import com.rickon.ximalayakotlin.adapter.MainFragmentPagerAdapter
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
import com.ximalaya.ting.android.opensdk.model.PlayableModel
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio
import com.ximalaya.ting.android.opensdk.model.live.schedule.Schedule
import com.ximalaya.ting.android.opensdk.model.track.Track
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager
import com.ximalaya.ting.android.opensdk.player.appnotification.NotificationColorUtils
import com.ximalaya.ting.android.opensdk.player.appnotification.XmNotificationCreater
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @Description:
 * @Author:      高烁
 * @CreateDate:  2019-06-14 15:06
 * @Email:       gaoshuo521@foxmail.com
 */
class MainActivity : BaseActivity(), View.OnClickListener {

    private val mContext = this
    private lateinit var mPlayerManager: XmPlayerManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initListener()


        //初始化播放器
        mPlayerManager = XmPlayerManager.getInstance(this)

        //初始化通知栏
        //如果贵方的 targetSdkVersion >= 24 需要在 XmNotificationCreater 初始化之前执行下一句
        NotificationColorUtils.isTargerSDKVersion24More = true
        val mNotification = XmNotificationCreater.getInstanse(this)
                .initNotification(this.applicationContext, MainActivity::class.java)
        // 如果之前贵方使用了 `XmPlayerManager.init(int id, Notification notification)` 这个初始化的方式
        // 请参考`4.8 播放器通知栏使用`重新添加新的通知栏布局,否则直接升级可能导致在部分手机播放时崩溃
        // 如果不想使用sdk内部搞好的notification,或者想自建notification 可以使用下面的  init()函数进行初始化
        mPlayerManager.init(System.currentTimeMillis().toInt(), mNotification)

        mPlayerManager.addPlayerStatusListener(mPlayerStatusListener)
        mPlayerManager.addOnConnectedListerner(object : XmPlayerManager.IConnectListener {
            override fun onConnected() {
                mPlayerManager.removeOnConnectedListerner(this)
                mPlayerManager.playMode = XmPlayListControl.PlayMode.PLAY_MODEL_LIST_LOOP
                Toast.makeText(mContext, "播放器初始化成功", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy")

        mPlayerManager.removePlayerStatusListener(mPlayerStatusListener)
        XmPlayerManager.release()
        CommonRequest.release()
    }

    companion object {

        private const val TAG = "MainActivity"

    }

    private fun initView() {
        main_tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        main_tab_layout.setupWithViewPager(main_view_pager)

        main_view_pager.adapter = MainFragmentPagerAdapter(supportFragmentManager)
        main_view_pager.offscreenPageLimit = 2
    }

    private fun initListener() {
        id_play_or_pause.setOnClickListener(this)
        id_current_list.setOnClickListener(this)
        history_btn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.id_play_or_pause -> if (mPlayerManager.isPlaying) mPlayerManager.pause() else mPlayerManager.play()
            R.id.id_current_list -> Toast.makeText(mContext, "暂未开发此功能", Toast.LENGTH_SHORT).show()
            R.id.history_btn -> {
                val intent = Intent(this,HistoryActivity::class.java)
                this.startActivity(intent)
            }
        }
    }

    //播放监听器
    private val mPlayerStatusListener = object : IXmPlayerStatusListener {

        override fun onSoundPrepared() {
            Log.i(TAG, "onSoundPrepared")
//            mSeekBar.setEnabled(true)
//            mProgress.setVisibility(View.GONE)
        }

        override fun onSoundSwitch(laModel: PlayableModel?, curModel: PlayableModel) {
            Log.i(TAG, "onSoundSwitch index:$curModel")

            val model = mPlayerManager.currSound
            var title: String
            var singer: String
            var coverUrl: String
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
            Glide.with(mContext)
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
//            val info = mPlayerManager?.currSound
//            val title: String = when (info) {
//                is Track -> {
//                    info.trackTitle
//                }
//                is Schedule -> {
//                    info.relatedProgram.programName
//                }
//                is Radio -> {
//                    info.radioName
//                }
//                else -> ""
//            }
//            mTextView.setText(title + "[" + ToolUtil.formatTime(currPos) + "/" + ToolUtil.formatTime(duration) + "]")
//            if (mUpdateProgress && duration != 0) {
//                mSeekBar.setProgress((100 * currPos / duration.toFloat()).toInt())
//            }
        }

        override fun onPlayPause() {
            Log.i(TAG, "onPlayPause")
            id_play_or_pause.setImageResource(R.drawable.ic_play)

        }

        override fun onSoundPlayComplete() {
            Log.i(TAG, "onSoundPlayComplete")
            id_play_or_pause.setImageResource(R.drawable.ic_play)
            XmPlayerManager.getInstance(mContext).pause()
            Toast.makeText(mContext, "播放完成", Toast.LENGTH_SHORT).show()
        }

        override fun onError(exception: XmPlayerException): Boolean {
            Log.i(TAG, "onError:${exception.message}")
            id_play_or_pause.setImageResource(R.drawable.ic_play)
            return false
        }

        override fun onBufferProgress(position: Int) {
//            mSeekBar.setSecondaryProgress(position)
        }

        override fun onBufferingStart() {
//            mSeekBar.setEnabled(false)
//            mProgress.setVisibility(View.VISIBLE)
        }

        override fun onBufferingStop() {
//            mSeekBar.setEnabled(true)
//            mProgress.setVisibility(View.GONE)
        }

    }

}
