package com.rickon.ximalayakotlin.adapter

import androidx.fragment.app.FragmentPagerAdapter
import com.rickon.ximalayakotlin.fragment.BoutiqueFrag
import com.rickon.ximalayakotlin.fragment.CategoryFrag
import com.rickon.ximalayakotlin.fragment.MineFragment

/**
 * @Description:
 * @Author:      高烁
 * @CreateDate:  2019-07-23 15:57
 * @Email:       gaoshuo521@foxmail.com
 */
class MainFragmentPagerAdapter : FragmentPagerAdapter {
    var fragments: MutableList<androidx.fragment.app.Fragment> = ArrayList()
    private val titleList = arrayOf("精品", "分类", "我的")

    constructor(fm: androidx.fragment.app.FragmentManager) : super(fm) {
        fragments.add(BoutiqueFrag.newInstance())
        fragments.add(CategoryFrag.newInstance())
        fragments.add(MineFragment.newInstance())
    }

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        return fragments[position]
    }

    override fun getCount(): Int = fragments.size

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList[position]
    }
}