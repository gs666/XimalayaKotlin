package com.rickon.ximalayakotlin.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.rickon.ximalayakotlin.R
import com.rickon.ximalayakotlin.adapter.HoriRadioAdapter
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack
import com.ximalaya.ting.android.opensdk.model.PlayableModel
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio
import com.ximalaya.ting.android.opensdk.model.live.radio.RadioList
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException
import kotlinx.android.synthetic.main.activity_radio.*
import java.lang.ref.WeakReference

class RadioActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mRecommendRadioList: MutableList<Radio>
    private var mLoading = false
    private var currentRadioPos = Int.MAX_VALUE

    private var mPlayerServiceManager: XmPlayerManager? = null
    private lateinit var horiRadioAdapter: HoriRadioAdapter
    private val mContext = this
    private var mHandler: Handler = WithoutLeakHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_radio)

        initListener()

        setSupportActionBar(radio_toolbar)
        //禁止显示默认 title
        supportActionBar?.setDisplayShowTitleEnabled(false)
        radio_toolbar.setNavigationOnClickListener { finish() }

        loadRadioList()

        mPlayerServiceManager = XmPlayerManager.getInstance(applicationContext)
        mPlayerServiceManager?.addPlayerStatusListener(mPlayerStatusListener)
    }

    private fun initListener() {
        local_province_btn.setOnClickListener(this)
        country_btn.setOnClickListener(this)
        province_city_btn.setOnClickListener(this)
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

                    val msg = Message()
                    msg.what = MSG_LOAD_RADIO_SUCCESS
                    mHandler.sendMessage(msg)
                }
                mLoading = false
            }

            override fun onError(code: Int, message: String) {
                mLoading = false
                Log.d(TAG, "获取省市下的电台失败,错误码$code,错误信息$message")
                showToast(getString(R.string.load_failed))
            }
        })
    }

    private val mPlayerStatusListener = object : IXmPlayerStatusListener {
        override fun onSoundSwitch(laModel: PlayableModel?, curModel: PlayableModel) {
            horiRadioAdapter.notifyDataSetChanged()
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.local_province_btn -> {
                val intent = Intent(mContext, RadioListActivity::class.java)
                //传递一个省份代码和省份名称
                intent.putExtra("province_code", "320000")
                intent.putExtra("province_name", "江苏省")
                mContext.startActivity(intent)
            }
            R.id.country_btn -> {
                val intent = Intent(mContext, RadioListActivity::class.java)
                mContext.startActivity(intent)
            }
            R.id.province_city_btn -> {
                val tempIntent = Intent(mContext, ProvinceActivity::class.java)
                mContext.startActivity(tempIntent)
            }
        }
    }

    override fun onDestroy() {
        mPlayerServiceManager?.removePlayerStatusListener(mPlayerStatusListener)
        super.onDestroy()
    }

    companion object {

        private const val TAG = "RadioActivity"
        private const val COUNTRY_RADIO_TYPE = 1

        private const val MSG_LOAD_RADIO_SUCCESS = 0

        private class WithoutLeakHandler(radioActivity: RadioActivity) : Handler() {
            private var radioActivity: WeakReference<RadioActivity> = WeakReference(radioActivity)

            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when (msg.what) {
                    MSG_LOAD_RADIO_SUCCESS -> {
                        val myActivity = radioActivity.get()
                        if (myActivity != null) {
                            myActivity.recommend_radio_list.layoutManager = LinearLayoutManager(myActivity.applicationContext, LinearLayoutManager.HORIZONTAL, false)
                            myActivity.horiRadioAdapter = HoriRadioAdapter(myActivity.applicationContext, myActivity.mRecommendRadioList)
                            myActivity.recommend_radio_list.adapter = myActivity.horiRadioAdapter

                            myActivity.horiRadioAdapter.setOnKotlinItemClickListener(object : HoriRadioAdapter.IKotlinItemClickListener {
                                override fun onItemClickListener(position: Int) {
                                    if (position != myActivity.currentRadioPos) {
                                        Log.d(TAG, position.toString())
                                        myActivity.currentRadioPos = position

                                        val radio = myActivity.mRecommendRadioList[position]
                                        //播放直播
                                        myActivity.mPlayerServiceManager?.playLiveRadioForSDK(radio, -1, -1)

                                        myActivity.horiRadioAdapter.notifyDataSetChanged()
                                    }
                                }
                            })
                        }
                    }
                }
            }
        }
    }
}
