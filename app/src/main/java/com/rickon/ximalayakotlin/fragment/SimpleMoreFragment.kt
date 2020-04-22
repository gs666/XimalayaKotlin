package com.rickon.ximalayakotlin.fragment

import android.content.ContentUris
import android.content.DialogInterface
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rickon.ximalayakotlin.R
import com.rickon.ximalayakotlin.adapter.SimpleItemAdapter
import com.rickon.ximalayakotlin.util.GlobalUtil
import com.rickon.ximalayakotlin.util.XimalayaKotlin
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager

import java.util.ArrayList
import java.util.HashMap

/**
 * @Description:
 * @Author:      高烁
 * @CreateDate:  2019-07-17 17:04
 * @Email:       gaoshuo521@foxmail.com
 */
class SimpleMoreFragment : DialogFragment() {

    private lateinit var recyclerView: RecyclerView

    //声明一个list，存储要显示的信息
    private var countDownList = arrayOf(GlobalUtil.getString(R.string.no_countdown),
            GlobalUtil.getString(R.string.play_program_over),
            GlobalUtil.getString(R.string.countdown_15min),
            GlobalUtil.getString(R.string.countdown_30min),
            GlobalUtil.getString(R.string.countdown_60min),
            GlobalUtil.getString(R.string.countdown_90min),
            GlobalUtil.getString(R.string.diy),
            GlobalUtil.getString(R.string.cancel))

    private var args: Long = 0

    private val mContext = XimalayaKotlin.context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.CustomDatePickerDialog)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        Log.d(TAG, "onCreateView")
        //设置无标题
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        //设置从底部弹出
        val params = dialog?.window?.attributes
        params?.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.attributes = params
        arguments?.let { args = it.getLong("id") }

        //布局
        val view = inflater.inflate(R.layout.more_fragment, container, false)
        recyclerView = view.findViewById(R.id.pop_list)

        initList()

        setItemDecoration()
        return view
    }

    private fun initList() {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)

        val simpleItemAdapter = SimpleItemAdapter(mContext, countDownList.toList())
        recyclerView.adapter = simpleItemAdapter

        simpleItemAdapter.setOnKotlinItemClickListener(object : SimpleItemAdapter.IKotlinItemClickListener {
            override fun onItemClickListener(position: Int) {
                when (position) {
                    0 -> dismiss()
                    1 -> {
                        //播放完当前歌曲
                        XmPlayerManager.getInstance(mContext).pausePlayInMillis(-1)
                        dismiss()
                    }
                    2 -> {
                        //15分钟
                        XmPlayerManager.getInstance(mContext)
                                .pausePlayInMillis(System.currentTimeMillis() + 15 * ONE_MINUTE_MILLI)
                        dismiss()
                    }
                    3 -> {
                        //30分钟
                        XmPlayerManager.getInstance(mContext)
                                .pausePlayInMillis(System.currentTimeMillis() + 30 * ONE_MINUTE_MILLI)
                        dismiss()
                    }
                    4 -> {
                        //60分钟
                        XmPlayerManager.getInstance(mContext)
                                .pausePlayInMillis(System.currentTimeMillis() + 60 * ONE_MINUTE_MILLI)
                        dismiss()
                    }
                    5 -> {
                        //90分钟
                        XmPlayerManager.getInstance(mContext)
                                .pausePlayInMillis(System.currentTimeMillis() + 90 * ONE_MINUTE_MILLI)
                        dismiss()
                    }
                    6 -> {
                        //自定义
                        Toast.makeText(mContext, "暂未开发此功能", Toast.LENGTH_SHORT).show()
                        dismiss()
                    }
                    7 -> {
                        //取消计划
                        XmPlayerManager.getInstance(mContext).pausePlayInMillis(0)
                    }

                }
            }
        })
    }


    //设置分割线
    private fun setItemDecoration() {
        recyclerView.addItemDecoration(DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL))
    }


    override fun onStart() {
        super.onStart()
//        //设置fragment高度 、宽度
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog?.setCanceledOnTouchOutside(true)
    }

    companion object {
        private const val TAG = "SimpleMoreFragment"
        private const val ONE_MINUTE_MILLI = 60000

        fun newInstance(): SimpleMoreFragment {
            return SimpleMoreFragment()
        }
    }


}