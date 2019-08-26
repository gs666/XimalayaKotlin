package com.rickon.ximalayakotlin.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * @Description:
 * @Author:      高烁
 * @CreateDate:  2019-06-14 15:08
 * @Email:       gaoshuo521@foxmail.com
 */
open class BaseFragment() : Fragment() {
    lateinit var mContext: Context

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        context?.let { mContext = it }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}