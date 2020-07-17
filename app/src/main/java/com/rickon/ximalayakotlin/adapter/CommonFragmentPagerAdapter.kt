package com.rickon.ximalayakotlin.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter

/**
 * @Description:
 * @Author:      高烁
 * @CreateDate:  2019-07-23 15:57
 * @Email:       gaoshuo521@foxmail.com
 */
class CommonFragmentPagerAdapter(fm: androidx.fragment.app.FragmentManager, titleList: MutableList<String>,
                                 fragmentList: MutableList<Fragment>) : FragmentPagerAdapter(fm) {
    private var titles: MutableList<String> = titleList
    private var fragments: MutableList<Fragment> = fragmentList

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int = fragments.size

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }
}