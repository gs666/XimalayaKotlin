package com.rickon.ximalayakotlin.activities

import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.rickon.ximalayakotlin.R
import com.rickon.ximalayakotlin.adapter.ProvinceListAdapter
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack
import com.ximalaya.ting.android.opensdk.model.live.provinces.Province
import com.ximalaya.ting.android.opensdk.model.live.provinces.ProvinceList
import kotlinx.android.synthetic.main.activity_province.*

class ProvinceActivity : BaseActivity() {

    private var mLoading = false
    private val mContext = this
    private var mProvinceList: MutableList<Province> = mutableListOf()
    private lateinit var provinceListAdapter: ProvinceListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_province)

        initRecyclerView()

        loadProvinceList()
    }

    override fun mainHandlerMessage(activity: BaseActivity?, msg: Message) {
        super.mainHandlerMessage(activity, msg)
        when (msg.what) {
            MSG_LOAD_SUCCESS -> provinceListAdapter.notifyDataSetChanged()
        }
    }

    private fun initRecyclerView() {
        province_list.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        provinceListAdapter = ProvinceListAdapter(applicationContext, mProvinceList)
        province_list.adapter = provinceListAdapter

        provinceListAdapter.setOnKotlinItemClickListener(object : ProvinceListAdapter.IKotlinItemClickListener {
            override fun onItemClickListener(position: Int) {
                val intent = Intent(mContext, RadioListActivity::class.java)
                //传递一个省份代码和省份名称
                intent.putExtra("province_code", mProvinceList[position].provinceCode.toString())
                intent.putExtra("province_name", mProvinceList[position].provinceName)
                mContext.startActivity(intent)
            }
        })
    }

    /**
     * 加载省份列表
     */
    private fun loadProvinceList() {
        Log.d(TAG, "加载省份")
        if (mLoading) {
            return
        }
        mLoading = true
        val map = HashMap<String, String>()
        CommonRequest.getProvinces(map, object : IDataCallBack<ProvinceList> {
            override fun onSuccess(provinceList: ProvinceList?) {
                if (provinceList?.provinceList != null) {
                    mProvinceList.addAll(provinceList.provinceList)
                    mainHandler.sendEmptyMessage(MSG_LOAD_SUCCESS)


                }
                mLoading = false
            }

            override fun onError(code: Int, message: String) {
                mLoading = false
                Log.d(TAG, "获取省市列表失败,错误码$code,错误信息$message")
                showToast(getString(R.string.load_failed))
            }
        })
    }

    companion object {
        private const val TAG = "ProvinceActivity"
        private const val MSG_LOAD_SUCCESS = 0
    }
}
