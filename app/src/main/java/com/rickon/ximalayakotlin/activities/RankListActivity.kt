package com.rickon.ximalayakotlin.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.rickon.ximalayakotlin.R
import com.rickon.ximalayakotlin.adapter.RankRadioAdapter
import com.ximalaya.ting.android.opensdk.model.live.radio.RadioList
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants
import com.ximalaya.ting.android.opensdk.model.PlayableModel
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException
import kotlinx.android.synthetic.main.activity_rank_list.*


class RankListActivity : BaseActivity() {

    private var mRankRadioList: List<Radio>? = null
    private lateinit var rankRadioAdapter: RankRadioAdapter
    private var currentRadioPos = Integer.MAX_VALUE

    private var mPlayerServiceManager: XmPlayerManager? = null


    //uiHandler在主线程中创建，所以自动绑定主线程
    private var uiHandler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg?.what) {
                LOAD_RADIO_RANK_SUCCESS -> {
                    rank_radio_list.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
                    rankRadioAdapter = RankRadioAdapter(applicationContext, mRankRadioList!!)
                    rank_radio_list.adapter = rankRadioAdapter

                    rankRadioAdapter.setOnKotlinItemClickListener(object : RankRadioAdapter.IKotlinItemClickListener {
                        override fun onItemClickListener(position: Int) {
                            if (position != currentRadioPos) {
                                Log.d(TAG, position.toString())
                                currentRadioPos = position

                                val radio = mRankRadioList?.get(position)
                                //播放直播
                                mPlayerServiceManager?.playLiveRadioForSDK(radio, -1, -1)

                                rankRadioAdapter.notifyDataSetChanged()
                            }
                        }
                    })
                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rank_list)

        initToolBar()

        loadRankRadioList()

        mPlayerServiceManager = XmPlayerManager.getInstance(applicationContext)
        mPlayerServiceManager?.addPlayerStatusListener(mPlayerStatusListener)
    }

    private fun initToolBar() {
        setSupportActionBar(rank_list_toolbar)
        //禁止显示默认 title
        supportActionBar?.setDisplayShowTitleEnabled(false)
        rank_list_toolbar.setNavigationOnClickListener { finish() }
    }

    private fun loadRankRadioList() {
        val map = HashMap<String, String>()
        map[DTransferConstants.RADIO_COUNT] = 10.toString()
        CommonRequest.getRankRadios(map, object : IDataCallBack<RadioList> {
            override fun onSuccess(p0: RadioList?) {
                if (p0?.radios != null) {
                    mRankRadioList = p0.radios


                    val msg = Message()
                    msg.what = LOAD_RADIO_RANK_SUCCESS
                    uiHandler.sendMessage(msg)
                }
            }

            override fun onError(p0: Int, p1: String) {
                Log.d(TAG, "获取电台热播榜失败,错误码$p0,错误信息$p1")

            }
        })
    }

    override fun onDestroy() {
        mPlayerServiceManager?.removePlayerStatusListener(mPlayerStatusListener)
        super.onDestroy()
    }

    private val mPlayerStatusListener = object : IXmPlayerStatusListener {
        override fun onSoundSwitch(laModel: PlayableModel?, curModel: PlayableModel) {
            rankRadioAdapter.notifyDataSetChanged()
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

    companion object {

        private const val TAG = "RankListActivity"
        private const val LOAD_RADIO_RANK_SUCCESS = 0

    }
}
