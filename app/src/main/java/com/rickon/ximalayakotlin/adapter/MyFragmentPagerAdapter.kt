package com.rickon.ximalayakotlin.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.rickon.ximalayakotlin.fragment.MusicFragment
import com.rickon.ximalayakotlin.fragment.RecommendFrag

/**
 * @Description:
 * @Author:      高烁
 * @CreateDate:  2019-07-17 17:04
 * @Email:       gaoshuo521@foxmail.com
 */
class MyFragmentPagerAdapter : androidx.fragment.app.FragmentPagerAdapter {

    var fragments: MutableList<androidx.fragment.app.Fragment> = ArrayList()
    private val titleList = arrayOf("推荐", "音乐")

    constructor(fm: androidx.fragment.app.FragmentManager) : super(fm) {
        fragments.add(RecommendFrag.newInstance())
        fragments.add(MusicFragment.newInstance())
    }

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        return fragments[position]
    }

    override fun getCount(): Int = fragments.size

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList[position]
    }

}