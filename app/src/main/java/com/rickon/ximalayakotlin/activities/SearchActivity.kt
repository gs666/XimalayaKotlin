package com.rickon.ximalayakotlin.activities

import android.os.Bundle
import android.view.WindowManager
import com.rickon.ximalayakotlin.R
import com.rickon.ximalayakotlin.fragment.SearchHotWordFragment
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //避免布局被键盘推上来
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_search)

        setSupportActionBar(search_toolbar)
        //禁止显示默认 title
        supportActionBar?.setDisplayShowTitleEnabled(false)
        search_toolbar.setNavigationOnClickListener { finish() }

        //设置搜索框提示，去除左边搜索图标
        id_search_view.queryHint = getString(R.string.search_hint)
        id_search_view.isIconified = false

        val f = SearchHotWordFragment()
//        f.searchWords(this@SearchActivity)
        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.search_frame, f)
        ft.commit()
    }
}
