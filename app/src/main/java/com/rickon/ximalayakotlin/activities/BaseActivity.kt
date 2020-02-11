package com.rickon.ximalayakotlin.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rickon.ximalayakotlin.R
import com.rickon.ximalayakotlin.fragment.QuickControlsFragment
import com.umeng.analytics.MobclickAgent

open class BaseActivity : AppCompatActivity() {

    private lateinit var fragment: QuickControlsFragment //底部播放控制栏

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showQuickControl(true)
    }

    override fun onResume() {
        super.onResume()
        MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPause(this)
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

    open fun showToast(str: String) {
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }
}
