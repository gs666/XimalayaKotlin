package com.rickon.ximalayakotlin.activities

import android.os.Bundle
import android.view.View
import com.rickon.ximalayakotlin.R
import kotlinx.android.synthetic.main.activity_playing.*

class PlayingActivity : BaseActivity(), View.OnClickListener {

    var isLiked: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playing)

        initListener()
    }

    private fun initListener() {
        like_btn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.like_btn -> {
                if (!isLiked) {
                    like_btn.setImageDrawable(getDrawable(R.drawable.ic_liked))
                    isLiked = true
                } else {
                    like_btn.setImageDrawable(getDrawable(R.drawable.ic_like))
                    isLiked = false
                }
            }
        }
    }
}
