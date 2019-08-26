package com.rickon.ximalayakotlin.activities

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.rickon.ximalayakotlin.R
import com.rickon.ximalayakotlin.fragment.QuickControlsFragment

open class BaseActivity : AppCompatActivity() {

    private lateinit var fragment: QuickControlsFragment //底部播放控制栏

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showQuickControl(true)
    }

    /**
     * @param show 显示或关闭底部播放控制栏
     */
    open fun showQuickControl(show: Boolean) {
        val ft = supportFragmentManager.beginTransaction()
        if (show) {
            fragment = QuickControlsFragment.newInstance()
            ft.add(R.id.bottom_container, fragment).commitAllowingStateLoss()
        } else {
            ft.hide(fragment).commitAllowingStateLoss()
        }
    }
}
