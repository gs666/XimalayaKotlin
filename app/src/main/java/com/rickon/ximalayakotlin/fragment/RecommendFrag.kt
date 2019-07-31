package com.rickon.ximalayakotlin.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.rickon.ximalayakotlin.R
import com.rickon.ximalayakotlin.activities.PlayingActivity
import com.rickon.ximalayakotlin.activities.RadioActivity
import com.rickon.ximalayakotlin.activities.RankListActivity
import kotlinx.android.synthetic.main.recommend_frag_layout.*

/**
 * @Description:
 * @Author:      高烁
 * @CreateDate:  2019-07-17 15:45
 * @Email:       gaoshuo521@foxmail.com
 */
class RecommendFrag : BaseFragment(), View.OnClickListener {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.recommend_frag_layout, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initListener()
    }

    private fun initListener() {
        walkman_btn.setOnClickListener(this)
        radio_btn.setOnClickListener(this)
        rank_list_btn.setOnClickListener(this)
        lab_btn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.walkman_btn -> {

            }
            R.id.radio_btn -> {
                val intent = Intent(context,RadioActivity::class.java)
                context?.startActivity(intent)
            }
            R.id.rank_list_btn -> {
                val intent = Intent(context,RankListActivity::class.java)
                context?.startActivity(intent)
            }
            R.id.lab_btn -> {
                Toast.makeText(context,"暂未开发此功能",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    companion object {

        private const val TAG = "RecommendFrag"

        fun newInstance(): RecommendFrag {
            return RecommendFrag()
        }
    }
}