package com.rickon.ximalayakotlin.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.rickon.ximalayakotlin.R
import com.rickon.ximalayakotlin.adapter.CommonFragmentPagerAdapter
import com.rickon.ximalayakotlin.fragment.BoutiqueFrag
import com.rickon.ximalayakotlin.fragment.CategoryFrag
import com.rickon.ximalayakotlin.fragment.MineFragment
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager
import com.ximalaya.ting.android.opensdk.player.appnotification.NotificationColorUtils
import com.ximalaya.ting.android.opensdk.player.appnotification.XmNotificationCreater
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl
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
        mPlayerManager = XmPlayerManager.getInstance(this.applicationContext)

        //初始化通知栏
        //如果贵方的 targetSdkVersion >= 24 需要在 XmNotificationCreater 初始化之前执行下一句
        NotificationColorUtils.isTargerSDKVersion24More = true
        val mNotification = XmNotificationCreater.getInstanse(this)
                .initNotification(this.applicationContext, MainActivity::class.java)
        // 如果之前贵方使用了 `XmPlayerManager.init(int id, Notification notification)` 这个初始化的方式
        // 请参考`4.8 播放器通知栏使用`重新添加新的通知栏布局,否则直接升级可能导致在部分手机播放时崩溃
        // 如果不想使用sdk内部搞好的notification,或者想自建notification 可以使用下面的  init()函数进行初始化
        val notificationId = System.currentTimeMillis().toInt()
        mPlayerManager.init(notificationId, mNotification)

        XmPlayerManager.getInstance(this).init(notificationId,
                XmNotificationCreater.getInstanse(this).createNotification(this,
                        MainActivity::class.java))

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

        XmPlayerManager.release()
        CommonRequest.release()
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

        val titleList = mutableListOf(getString(R.string.boutique),
                getString(R.string.category),
                getString(R.string.mine))
        val fragmentList = mutableListOf(BoutiqueFrag.newInstance() as Fragment,
                CategoryFrag.newInstance() as Fragment,
                MineFragment.newInstance() as Fragment)
        main_view_pager.adapter = CommonFragmentPagerAdapter(supportFragmentManager, titleList, fragmentList)
        main_view_pager.offscreenPageLimit = 2
    }

    private fun initListener() {
        history_btn.setOnClickListener(this)
        search_btn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.search_btn -> {
                val historyIntent = Intent(this, SearchActivity::class.java)
                this.startActivity(historyIntent)
            }
            R.id.history_btn -> {
                val intent = Intent(this, HistoryActivity::class.java)
                this.startActivity(intent)
            }
        }
    }

    companion object {

        private const val TAG = "MainActivity"

    }

}
