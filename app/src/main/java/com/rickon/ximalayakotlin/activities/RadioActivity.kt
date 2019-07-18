package com.rickon.ximalayakotlin.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.rickon.ximalayakotlin.R
import kotlinx.android.synthetic.main.activity_radio.*

class RadioActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_radio)

        initListener()
    }

    fun initListener() {
        local_province_btn.setOnClickListener(this)
        country_btn.setOnClickListener(this)
        province_city_btn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.local_province_btn->{

            }
            R.id.country_btn->{

            }
            R.id.province_city_btn->{

            }
        }
    }
}
