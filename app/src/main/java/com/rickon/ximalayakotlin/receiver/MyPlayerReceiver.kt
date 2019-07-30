package com.rickon.ximalayakotlin.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager

/**
 * @Description:
 * @Author:      高烁
 * @CreateDate:  2019-07-29 20:38
 * @Email:       gaoshuo521@foxmail.com
 */
class MyPlayerReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("MyPlayerReceiver", "MyPlayerReceiver.onReceive $intent")
        if (intent?.action == "com.rickon.ximalayakotlin.Action_Close") {
            Toast.makeText(context, "通知栏点击了关闭", Toast.LENGTH_LONG).show()
            XmPlayerManager.release()
        } else if (intent?.action == "com.rickon.ximalayakotlin.Action_PAUSE_START") {
            if (XmPlayerManager.getInstance(context).isPlaying) {
                XmPlayerManager.getInstance(context).pause()
            } else {
                XmPlayerManager.getInstance(context).play()
            }
        }
    }
}