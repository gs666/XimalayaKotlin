package com.rickon.ximalayakotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack
import com.ximalaya.ting.android.opensdk.model.live.provinces.ProvinceList
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @Description:
 * @Author:      高烁
 * @CreateDate:  2019-06-14 15:06
 * @Email:       gaoshuo521@foxmail.com
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hello.setOnClickListener { loadProvinceList() }


    }

    companion object {

        private const val TAG = "MainActivity"

    }

    /**
     * 获取省份列表
     */
    fun loadProvinceList() {
        val map = java.util.HashMap<String, String>()
        CommonRequest.getProvinces(map, object : IDataCallBack<ProvinceList> {
            override fun onSuccess(provinceList: ProvinceList?) {
                val provinces = provinceList!!.provinceList
                if (provinces.size > 0) {
                    Log.d(TAG, "" + provinces.size)
                }
                for (province in provinces) {
                    //                    Log.e(TAG, "onSuccess: " + province.getProvinceName() + ",省市代码：" + province.getProvinceCode());
                }
            }

            override fun onError(i: Int, s: String) {
                Log.d(TAG, s + i.toString())

                Log.d(TAG, "获取省市列表失败")
            }
        })
    }
}
