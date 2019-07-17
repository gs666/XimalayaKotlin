package com.rickon.ximalayakotlin.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.rickon.ximalayakotlin.fragment.MusicFragment
import com.rickon.ximalayakotlin.fragment.RecommendFrag

/**
 * @Description:
 * @Author:      高烁
 * @CreateDate:  2019-07-17 17:04
 * @Email:       gaoshuo521@foxmail.com
 */
class MyFragmentPagerAdapter : FragmentPagerAdapter {

    var fragments: MutableList<Fragment> = ArrayList()
    private val titleList = arrayOf("推荐", "音乐")

    constructor(fm: FragmentManager) : super(fm) {
        fragments.add(RecommendFrag.newInstance())
        fragments.add(MusicFragment.newInstance())
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int = fragments.size

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList[position]
    }

}