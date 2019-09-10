package com.rickon.ximalayakotlin.activities

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.rickon.ximalayakotlin.R
import com.rickon.ximalayakotlin.adapter.VerticalRadioAdapter
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack
import com.ximalaya.ting.android.opensdk.model.PlayableModel
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio
import com.ximalaya.ting.android.opensdk.model.live.radio.RadioList
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException
import kotlinx.android.synthetic.main.activity_country_radio.*

class CountryRadioActivity : BaseActivity() {

    private lateinit var mRecommendRadioList: MutableList<Radio>
    private lateinit var verticalRadioAdapter: VerticalRadioAdapter

    private var mLoading = false
    private var mPlayerServiceManager: XmPlayerManager? = null

    private var currentRadioPos = Integer.MAX_VALUE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_radio)

        initToolBar()

        loadRadioList()

        mPlayerServiceManager = XmPlayerManager.getInstance(applicationContext)
        mPlayerServiceManager?.addPlayerStatusListener(mPlayerStatusListener)
    }

    /**
     * 加载对应省份直播电台不播放
     */
    private fun loadRadioList() {
        Log.d(TAG, "加载国家台直播电台不播放")
        if (mLoading) {
            return
        }
        mLoading = true
        val map = HashMap<String, String>()
        map[DTransferConstants.RADIOTYPE] = COUNTRY_RADIO_TYPE.toString()
        CommonRequest.getRadios(map, object : IDataCallBack<RadioList> {
            override fun onSuccess(radioList: RadioList?) {
                if (radioList?.radios != null) {
                    mRecommendRadioList = radioList.radios

                    country_radio_recycler.layoutManager = LinearLayoutManager(applicationContext,
                            LinearLayoutManager.VERTICAL, false)
                    verticalRadioAdapter = VerticalRadioAdapter(applicationContext, mRecommendRadioList)
                    country_radio_recycler.adapter = verticalRadioAdapter

                    verticalRadioAdapter.setOnKotlinItemClickListener(object : VerticalRadioAdapter.IKotlinItemClickListener {
                        override fun onItemClickListener(position: Int) {
                            if (position != currentRadioPos) {
                                Log.d(TAG, position.toString())
                                currentRadioPos = position

                                val radio = mRecommendRadioList[position]
                                //播放直播
                                mPlayerServiceManager?.playLiveRadioForSDK(radio, -1, -1)

                                verticalRadioAdapter.notifyDataSetChanged()
                            }
                        }
                    })
                }
                mLoading = false
            }

            override fun onError(code: Int, message: String) {
                mLoading = false
                Log.d(TAG, "获取省市下的电台失败,错误码$code,错误信息$message")
            }
        })
    }

    private fun initToolBar() {
        setSupportActionBar(country_radio_toolbar)
        //禁止显示默认 title
        supportActionBar?.setDisplayShowTitleEnabled(false)
        country_radio_toolbar.setNavigationOnClickListener { finish() }
    }

    private val mPlayerStatusListener = object : IXmPlayerStatusListener {
        override fun onSoundSwitch(laModel: PlayableModel?, curModel: PlayableModel) {
            verticalRadioAdapter.notifyDataSetChanged()
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

        private const val TAG = "CountryRadioActivity"
        private const val COUNTRY_RADIO_TYPE = 1
        private const val PROVINCE_RADIO_TYPE = 2
        private const val NET_RADIO_TYPE = 3
        private const val LOAD_PROVINCE_SUCCESS = 4
    }

}
