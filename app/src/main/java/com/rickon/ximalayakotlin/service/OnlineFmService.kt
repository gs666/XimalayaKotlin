package com.rickon.ximalayakotlin.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.rickon.ximalayakotlin.db.HistoryDatabase
import com.rickon.ximalayakotlin.model.HistoryItem
import com.rickon.ximalayakotlin.util.XimalayaKotlin
import com.ximalaya.ting.android.opensdk.model.PlayableModel
import com.ximalaya.ting.android.opensdk.model.album.SubordinatedAlbum
import com.ximalaya.ting.android.opensdk.model.live.schedule.Schedule
import com.ximalaya.ting.android.opensdk.model.track.Track
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException

/**
 * @Description:
 * @Author:      Rickon
 * @CreateDate:  2020/7/26 10:48 PM
 * @Email:       gaoshuo521@foxmail.com
 */
class OnlineFmService : Service() {
    private lateinit var mPlayerManager: XmPlayerManager

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
        initData()
    }

    private fun initData() {
        mPlayerManager = XmPlayerManager.getInstance(this)
        mPlayerManager.addPlayerStatusListener(mPlayerStatusListener)
    }

    //播放监听器
    private val mPlayerStatusListener = object : IXmPlayerStatusListener {

        override fun onSoundPrepared() {
            Log.i(TAG, "onSoundPrepared")
        }

        override fun onSoundSwitch(laModel: PlayableModel?, curModel: PlayableModel) {
            Log.i(TAG, "onSoundSwitch index:$curModel")

        }

        override fun onPlayStop() {
            Log.i(TAG, "onPlayStop")
        }

        override fun onPlayStart() {
            Log.i(TAG, "onPlayStart")
            if (mPlayerManager.currSound is Schedule) {
                //添加电台播放记录
                val schedule: Schedule = mPlayerManager.currSound as Schedule
                Log.d(TAG, "Schedule查询是否存在此电台信息，${schedule.radioId}")

                Thread(Runnable {
                    Log.d(TAG, "添加记录")
                    val historyItem = HistoryItem()
                    with(historyItem) {
                        itemId = schedule.radioId.toString()
                        isAlbum = false
                        itemTitle = schedule.radioName
                        itemImagePath = schedule.relatedProgram.backPicUrl
                        lastListenTime = System.currentTimeMillis()
                        trackId = ""
                        trackTitle = ""
                        lastBreakTime = 0
                        //数据库insert数据
                        HistoryDatabase.getInstance(XimalayaKotlin.context).historyDao().insertHistory(this)
                    }
                }).start();
            } else if (mPlayerManager.currSound is Track) {
                //添加新记录
                val tempTrack = mPlayerManager.currSound as Track
                Thread(Runnable {
                    val albumHistoryItem = HistoryItem()
                    val tempAlbum: SubordinatedAlbum? = tempTrack.album
                    with(albumHistoryItem) {
                        itemId = tempAlbum?.albumId.toString()
                        isAlbum = true
                        itemTitle = if (tempAlbum == null) {
                            ""
                        } else {
                            tempAlbum.albumTitle
                        }
                        albumAuthor = tempTrack.announcer.nickname
                        itemImagePath = tempTrack.coverUrlLarge
                        lastListenTime = System.currentTimeMillis()
                        trackId = tempTrack.dataId.toString()
                        trackTitle = tempTrack.trackTitle
                        lastBreakTime = 0
                        //数据库insert数据
                        HistoryDatabase.getInstance(XimalayaKotlin.context).historyDao().insertHistory(this)
                    }
                }).start()
            }
        }

        //播放进度回调
        override fun onPlayProgress(currPos: Int, duration: Int) {

        }

        override fun onPlayPause() {
            Log.i(TAG, "onPlayPause")
        }

        override fun onSoundPlayComplete() {
        }

        override fun onError(exception: XmPlayerException): Boolean {
            Log.i(TAG, "onError:${exception.message}")
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
        private const val TAG = "OnlineFmService"
    }

}