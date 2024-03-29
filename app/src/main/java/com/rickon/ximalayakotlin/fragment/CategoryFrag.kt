package com.rickon.ximalayakotlin.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rickon.ximalayakotlin.R

/**
 * @Description:
 * @Author:      高烁
 * @CreateDate:  2019-06-17 13:00
 * @Email:       gaoshuo521@foxmail.com
 */
class CategoryFrag : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView")
        return inflater.inflate(R.layout.category_frag_layout, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    companion object {

        private const val TAG = "CategoryFrag"

        fun newInstance(): CategoryFrag {
            return CategoryFrag()
        }
    }
}