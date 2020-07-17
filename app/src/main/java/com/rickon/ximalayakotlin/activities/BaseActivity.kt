package com.rickon.ximalayakotlin.activities

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rickon.ximalayakotlin.R
import com.rickon.ximalayakotlin.fragment.QuickControlsFragment
import com.umeng.analytics.MobclickAgent
import java.lang.ref.WeakReference

open class BaseActivity : AppCompatActivity() {

    private lateinit var fragment: QuickControlsFragment //底部播放控制栏
    protected lateinit var mainHandler: WithoutLeakHandler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainHandler = WithoutLeakHandler(this)
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
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    protected open fun mainHandlerMessage(activity: BaseActivity?, msg: Message?) {}

    companion object {
        class WithoutLeakHandler(baseActivity: BaseActivity) : Handler() {
            private var baseActivity: WeakReference<BaseActivity> = WeakReference(baseActivity)

            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                val myActivity = baseActivity.get()
                myActivity?.mainHandlerMessage(myActivity, msg)
            }
        }
    }
}
