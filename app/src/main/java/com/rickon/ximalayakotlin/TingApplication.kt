package com.rickon.ximalayakotlin

import android.app.Application
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest


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
    }

}
