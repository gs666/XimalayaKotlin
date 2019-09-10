package com.rickon.ximalayakotlin.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rickon.ximalayakotlin.R
import com.rickon.ximalayakotlin.adapter.VerticalRadioAdapter
import com.rickon.ximalayakotlin.util.XimalayaKotlin
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager
import java.util.ArrayList

/**
 * @Description:
 * @Author:      高烁
 * @CreateDate:  2019-06-10 20:06
 * @Email:       gaoshuo521@foxmail.com
 */
class SearchRadioFragment : BaseFragment() {

    private lateinit var mAdapter: VerticalRadioAdapter
    private lateinit var radiosList: ArrayList<Radio>
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.recylerview, container, false)
        if (arguments != null) {
            radiosList = (arguments as Bundle).getParcelableArrayList("searchRadio")
        }

        recyclerView = view.findViewById(R.id.recyclerview) as RecyclerView
        layoutManager = LinearLayoutManager(mContext)
        recyclerView.layoutManager = layoutManager
        mAdapter = VerticalRadioAdapter(XimalayaKotlin.context, radiosList)
        mAdapter.setOnKotlinItemClickListener(object : VerticalRadioAdapter.IKotlinItemClickListener {
            override fun onItemClickListener(position: Int) {
                val radio = radiosList[position]
                //播放直播
                XmPlayerManager.getInstance(mContext).playLiveRadioForSDK(radio, -1, -1)
            }
        })

        recyclerView.adapter = mAdapter
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL))

        return view
    }

    companion object {
        fun newInstance(list: ArrayList<Radio>): SearchRadioFragment {
            val fragment = SearchRadioFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList("searchRadio", list)
            fragment.arguments = bundle
            return fragment
        }
    }
}