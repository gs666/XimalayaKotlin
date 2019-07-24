package com.rickon.ximalayakotlin.adapter

import androidx.fragment.app.FragmentPagerAdapter
import com.rickon.ximalayakotlin.R
import com.rickon.ximalayakotlin.fragment.BookCityFragment
import com.rickon.ximalayakotlin.fragment.MusicFragment
import com.rickon.ximalayakotlin.fragment.RecommendFrag
import com.rickon.ximalayakotlin.util.GlobalUtil

/**
 * @Description:
 * @Author:      高烁
 * @CreateDate:  2019-07-17 17:04
 * @Email:       gaoshuo521@foxmail.com
 */
class MyFragmentPagerAdapter : FragmentPagerAdapter {

    var fragments: MutableList<androidx.fragment.app.Fragment> = ArrayList()
    private val titleList = arrayOf(GlobalUtil.getString(R.string.recommend),
            GlobalUtil.getString(R.string.book_city),
            GlobalUtil.getString(R.string.feeling),
            GlobalUtil.getString(R.string.music),
            GlobalUtil.getString(R.string.crosstalk),
            GlobalUtil.getString(R.string.parent_child),
            GlobalUtil.getString(R.string.headline),
            GlobalUtil.getString(R.string.entertainment),
            GlobalUtil.getString(R.string.knowledge))

    constructor(fm: androidx.fragment.app.FragmentManager) : super(fm) {
        fragments.add(RecommendFrag.newInstance())
        fragments.add(BookCityFragment.newInstance())
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