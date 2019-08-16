package com.rickon.ximalayakotlin.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rickon.ximalayakotlin.R
import com.rickon.ximalayakotlin.adapter.TrackAdapter
import com.rickon.ximalayakotlin.util.XimalayaKotlin
import com.ximalaya.ting.android.opensdk.model.track.Track
import java.util.ArrayList

/**
 * @Description:
 * @Author:      高烁
 * @CreateDate:  2019-06-10 20:06
 * @Email:       gaoshuo521@foxmail.com
 */
class SearchTrackFragment : BaseFragment() {

    private var mAdapter: TrackAdapter? = null
    private lateinit var tracksList: ArrayList<Track>
    private var recyclerView: RecyclerView? = null
    private var layoutManager: LinearLayoutManager? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.recylerview, container, false)
        if (arguments != null) {
            tracksList = (arguments as Bundle).getParcelableArrayList("searchMusic")
        }

        recyclerView = view.findViewById(R.id.recyclerview) as RecyclerView

        layoutManager = LinearLayoutManager(mContext)
        recyclerView!!.layoutManager = layoutManager
        mAdapter = TrackAdapter(XimalayaKotlin.context!!, tracksList, true)
        recyclerView!!.adapter = mAdapter
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.addItemDecoration(DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL))

        return view
    }

    companion object {
        fun newInstance(list: ArrayList<Track>): SearchTrackFragment {
            val fragment = SearchTrackFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList("searchMusic", list)
            fragment.arguments = bundle
            return fragment
        }
    }
}