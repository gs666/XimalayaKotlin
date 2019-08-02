package com.rickon.ximalayakotlin

import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import com.rickon.ximalayakotlin.receiver.MyPlayerReceiver
import com.rickon.ximalayakotlin.util.XimalayaKotlin
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
import com.ximalaya.ting.android.opensdk.player.appnotification.XmNotificationCreater
import com.ximalaya.ting.android.opensdk.util.BaseUtil
import org.litepal.LitePal


/**
 * @Description:
 * @Author:      高烁
 * @CreateDate:  2019-06-14 15:06
 * @Email:       gaoshuo521@foxmail.com
 */
open class TingApplication : Application() {

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
