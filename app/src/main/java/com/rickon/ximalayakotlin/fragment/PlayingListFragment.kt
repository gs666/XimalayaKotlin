package com.rickon.ximalayakotlin.fragment

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rickon.ximalayakotlin.R
import com.rickon.ximalayakotlin.adapter.ListTrackAdapter
import com.rickon.ximalayakotlin.util.XimalayaKotlin
import com.ximalaya.ting.android.opensdk.model.track.Track
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager

import java.util.ArrayList

/**
 * @Description:
 * @Author:      高烁
 * @CreateDate:  2019-07-17 17:04
 * @Email:       gaoshuo521@foxmail.com
 */
class PlayingListFragment(trackList: ArrayList<Track>) : DialogFragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var listTrackAdapter: ListTrackAdapter
    private var mTrackList: ArrayList<Track> = trackList

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

        val view = inflater.inflate(R.layout.playing_list_fragment, container, false);
        initView(view)

        return view
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        Log.d(TAG, "onViewCreated")
//
//        Log.d(TAG, "trackList" + mTrackList.size)
//
//        initView(view)
//        initList()
//    }
    //todo：生命周期问题

    private fun initView(view: View) {
        recyclerView = view.findViewById(R.id.pop_list)

        initList()
    }

    private fun initList() {
        recyclerView.layoutManager = LinearLayoutManager(XimalayaKotlin.context)
        listTrackAdapter = ListTrackAdapter(mContext, mTrackList)
        recyclerView.adapter = listTrackAdapter

        recyclerView.addItemDecoration(DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL))

        listTrackAdapter.setOnKotlinItemClickListener(object : ListTrackAdapter.IKotlinItemClickListener {
            override fun onItemClickListener(position: Int) {
                XmPlayerManager.getInstance(mContext).playList(mTrackList, position)
            }
        })
    }


    override fun onStart() {
        super.onStart()
//        //设置fragment高度 、宽度
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, 1600)
        dialog?.setCanceledOnTouchOutside(true)

    }

    companion object {
        private const val TAG = "PlayingListFragment"
    }


}