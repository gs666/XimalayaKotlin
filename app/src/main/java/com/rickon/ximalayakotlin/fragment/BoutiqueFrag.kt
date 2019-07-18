package com.rickon.ximalayakotlin.fragment

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rickon.ximalayakotlin.R
import com.rickon.ximalayakotlin.adapter.MyFragmentPagerAdapter
import kotlinx.android.synthetic.main.boutique_frag_layout.*

/**
 * @Description:
 * @Author:      高烁
 * @CreateDate:  2019-06-17 13:00
 * @Email:       gaoshuo521@foxmail.com
 */
class BoutiqueFrag : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView")
        return inflater.inflate(R.layout.boutique_frag_layout, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {

        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        tab_layout.setupWithViewPager(view_pager)

        val myFragmentPagerAdapter = MyFragmentPagerAdapter(childFragmentManager)
        view_pager.adapter = myFragmentPagerAdapter
        view_pager.offscreenPageLimit = 3

    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }


    companion object {

        private const val TAG = "BoutiqueFrag"

        fun newInstance(): BoutiqueFrag {
            return BoutiqueFrag()
        }
    }
}