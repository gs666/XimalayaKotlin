package com.rickon.ximalayakotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import com.rickon.ximalayakotlin.adapter.FragmentAdapter
import com.rickon.ximalayakotlin.fragment.RadioFragment
import com.rickon.ximalayakotlin.fragment.RecommendFrag
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

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

        initView()

        //打开广播界面
        id_tab_layout.getTabAt(1)!!.select()

    }

    companion object {

        private const val TAG = "MainActivity"

    }

    fun initView() {

        var fragmentList: MutableList<Fragment> = ArrayList()
        var titleList: MutableList<String> = ArrayList()

        fragmentList.add(RecommendFrag())
        fragmentList.add(RadioFragment())

        titleList.add(getString(R.string.recommend))
        titleList.add(getString(R.string.fm))

        val fragmentAdapter = FragmentAdapter(supportFragmentManager)
        id_view_pager.adapter = fragmentAdapter
        id_tab_layout.setupWithViewPager(id_view_pager)

        fragmentAdapter.setData(titleList)

        id_view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {
            }

            override fun onPageScrollStateChanged(p0: Int) {

            }
        })

        id_tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

}
