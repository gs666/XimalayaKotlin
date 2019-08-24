package com.rickon.ximalayakotlin.activities

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.rickon.ximalayakotlin.R
import com.rickon.ximalayakotlin.adapter.CommonFragmentPagerAdapter
import com.rickon.ximalayakotlin.fragment.DownloadedFragment
import com.rickon.ximalayakotlin.fragment.DownloadingFragment
import kotlinx.android.synthetic.main.activity_download.*

class DownloadActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)

        initView()

        initListener()

    }

    private fun initView() {
        setSupportActionBar(download_toolbar)
        //禁止显示默认 title
        supportActionBar?.setDisplayShowTitleEnabled(false)
        download_toolbar.setNavigationOnClickListener { finish() }

        id_down_tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        id_down_tab.setupWithViewPager(down_view_pager)

        val titleList = mutableListOf(getString(R.string.had_download),getString(R.string.downloading))
        val fragmentList = mutableListOf(DownloadedFragment.newInstance() as Fragment,DownloadingFragment.newInstance() as Fragment)
        down_view_pager.adapter = CommonFragmentPagerAdapter(supportFragmentManager,titleList,fragmentList)
        down_view_pager.offscreenPageLimit = 2

    }

    private fun initListener(){
        more_btn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.more_btn ->{

            }
        }
    }


}
