package com.rickon.ximalayakotlin

import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import com.rickon.ximalayakotlin.receiver.MyPlayerReceiver
import com.rickon.ximalayakotlin.util.XimalayaKotlin
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
import com.ximalaya.ting.android.opensdk.player.appnotification.XmNotificationCreater
import com.ximalaya.ting.android.opensdk.util.BaseUtil
import com.ximalaya.ting.android.opensdk.util.Logger
import com.ximalaya.ting.android.sdkdownloader.XmDownloadManager
import com.ximalaya.ting.android.sdkdownloader.http.RequestParams
import com.ximalaya.ting.android.sdkdownloader.http.app.RequestTracker
import com.ximalaya.ting.android.sdkdownloader.http.request.UriRequest
import org.litepal.LitePal


/**
 * @Description:
 * @Author:      高烁
 * @CreateDate:  2019-06-14 15:06
 * @Email:       gaoshuo521@foxmail.com
 */
open class TingApplication : Application() {

    private val requestTracker = object : RequestTracker {
        override fun onWaiting(params: RequestParams) {
            Logger.log("TingApplication : onWaiting $params")
        }

        override fun onStart(params: RequestParams) {
            Logger.log("TingApplication : onStart $params")
        }

        override fun onRequestCreated(request: UriRequest) {
            Logger.log("TingApplication : onRequestCreated $request")
        }

        override fun onSuccess(request: UriRequest, result: Any) {
            Logger.log("TingApplication : onSuccess $request   result = $result")
        }

        override fun onRemoved(request: UriRequest) {
            Logger.log("TingApplication : onRemoved $request")
        }

        override fun onCancelled(request: UriRequest) {
            Logger.log("TingApplication : onCanclelled $request")
        }

        override fun onError(request: UriRequest, ex: Throwable, isCallbackError: Boolean) {
            Logger.log("TingApplication : onError $request   ex = $ex   isCallbackError = $isCallbackError")
        }

        override fun onFinished(request: UriRequest) {
            Logger.log("TingApplication : onFinished $request")
        }
    }

    override fun onCreate() {
        super.onCreate()
        val mXimalaya = CommonRequest.getInstanse()
        val mAppSecret = "6a7986a3e4326f196b4cf50c1483f09b"
        mXimalaya.setAppkey("9e7d0b81dd09d3a5c047c5ff841f8691")
        mXimalaya.setPackid("com.rickon.ximalayakotlin")
        mXimalaya.init(this, mAppSecret)

        XimalayaKotlin.initialize(this)
        //初始化LitePal数据库
        LitePal.initialize(this)

        val mp3 = getExternalFilesDir("mp3")?.absolutePath
        println("地址是  $mp3")

        if (!BaseUtil.isPlayerProcess(this)) {
            XmDownloadManager.Builder(this)
                    .maxDownloadThread(3)            // 最大的下载个数 默认为1 最大为3
                    .maxSpaceSize(java.lang.Long.MAX_VALUE)    // 设置下载文件占用磁盘空间最大值，单位字节。不设置没有限制
                    .connectionTimeOut(15000)        // 下载时连接超时的时间 ,单位毫秒 默认 30000
                    .readTimeOut(15000)                // 下载时读取的超时时间 ,单位毫秒 默认 30000
                    .fifo(false)                    // 等待队列的是否优先执行先加入的任务. false表示后添加的先执行(不会改变当前正在下载的音频的状态) 默认为true
                    .maxRetryCount(3)                // 出错时重试的次数 默认2次
                    .progressCallBackMaxTimeSpan(1000)//  进度条progress 更新的频率 默认是800
                    .requestTracker(requestTracker)    // 日志 可以打印下载信息
                    .savePath(mp3)    // 保存的地址 会检查这个地址是否有效
                    .create()
        }

        if (BaseUtil.getCurProcessName(this).contains(":player")) {
            val instanse = XmNotificationCreater.getInstanse(this)
            instanse.setNextPendingIntent(null as PendingIntent?)
            instanse.setPrePendingIntent(null as PendingIntent?)

            val actionName = "com.rickon.ximalayakotlin.Action_Close"
            val intent = Intent(actionName)
            intent.setClass(this, MyPlayerReceiver::class.java)
            val broadcast = PendingIntent.getBroadcast(this, 0, intent, 0)
            instanse.setClosePendingIntent(broadcast)

            val pauseActionName = "com.rickon.ximalayakotlin.Action_PAUSE_START"
            val intent1 = Intent(pauseActionName)
            intent1.setClass(this, MyPlayerReceiver::class.java)
            val broadcast1 = PendingIntent.getBroadcast(this, 0, intent1, 0)
            instanse.setStartOrPausePendingIntent(broadcast1)
        }
    }

}
