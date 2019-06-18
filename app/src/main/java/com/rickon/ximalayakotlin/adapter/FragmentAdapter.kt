package com.rickon.ximalayakotlin.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.rickon.ximalayakotlin.fragment.RadioFragment
import com.rickon.ximalayakotlin.fragment.RecommendFrag

/**
 * @Author: 高烁
 * @CreateDate: 2019-04-29 16:32
 * @Email: gaoshuo521@foxmail.com
 */
class FragmentAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {

    private var mTitles: List<String>? = null

    fun setData(titleList: List<String>) {
        mTitles = titleList
        notifyDataSetChanged()
    }


    override fun getPageTitle(position: Int): CharSequence? {
        return mTitles!![position]
    }

    override fun getItem(position: Int): Fragment {
        return if (position == 0) RecommendFrag() else RadioFragment()
    }

    override fun getCount(): Int {
        return if (mTitles == null) 0 else mTitles!!.size
    }
}
