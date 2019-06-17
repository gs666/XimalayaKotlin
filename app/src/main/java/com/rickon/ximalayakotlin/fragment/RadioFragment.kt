package com.rickon.ximalayakotlin.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rickon.ximalayakotlin.R
import com.rickon.ximalayakotlin.adapter.ProvinceListAdapter
import com.rickon.ximalayakotlin.adapter.RadioListAdapter
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack
import com.ximalaya.ting.android.opensdk.model.live.provinces.Province
import com.ximalaya.ting.android.opensdk.model.live.provinces.ProvinceList
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.radio_frag_layout.*

/**
 * @Description: online FM
 * @Author:      高烁
 * @CreateDate:  2019-06-14 15:06
 * @Email:       gaoshuo521@foxmail.com
 */
class RadioFragment() : BaseFragment() {
    private var mContext: Context? = null
    private var mProvinceList: List<Province>? = null
    private var mRadioList: List<Radio>? = null

    //uiHandler在主线程中创建，所以自动绑定主线程
    private var uiHandler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg?.what) {
                LOAD_PROVINCE_SUCCESS -> {
                    Log.d(TAG, "收到获取成功的消息")

                    recyclerview_provinces.layoutManager = LinearLayoutManager(mContext)
                    val adapter = ProvinceListAdapter(mContext!!, mProvinceList!!)
                    recyclerview_provinces.adapter = adapter

                    adapter.setOnKotlinItemClickListener(object :
                        ProvinceListAdapter.IKotlinItemClickListener {
                        override fun onItemClickListener(position: Int) {
                            Log.d(TAG, position.toString())
                        }
                    })
                }
            }
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.radio_frag_layout, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mContext = activity

        loadProvinceList()


    }

    override fun onDestroy() {
        super.onDestroy()
    }

    /**
     * 获取省份列表
     */
    fun loadProvinceList() {
        val map = java.util.HashMap<String, String>()
        CommonRequest.getProvinces(map, object : IDataCallBack<ProvinceList> {
            override fun onSuccess(provinceList: ProvinceList?) {
                mProvinceList = provinceList!!.provinceList
                Log.d(TAG, "获取省市列表成功")
                val msg = Message()
                msg.what = LOAD_PROVINCE_SUCCESS
                uiHandler.sendMessage(msg)
            }

            override fun onError(i: Int, s: String) {
                Log.d(TAG, "获取省市列表失败")
            }
        })
    }

    companion object {

        private const val TAG = "RadioFragment"
        private const val COUNTRY_RADIO_TYPE = 1
        private const val PROVINCE_RADIO_TYPE = 2
        private const val NET_RADIO_TYPE = 3
        private const val LOAD_PROVINCE_SUCCESS = 4
        private const val LOAD_RADIO_SUCCESS = LOAD_PROVINCE_SUCCESS + 1


    }
}