package com.rickon.ximalayakotlin.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rickon.ximalayakotlin.R
import com.rickon.ximalayakotlin.activities.AlbumActivity
import com.rickon.ximalayakotlin.adapter.AlbumAdapter
import com.rickon.ximalayakotlin.util.XimalayaKotlin
import com.ximalaya.ting.android.opensdk.model.album.Album
import com.ximalaya.ting.android.opensdk.model.track.Track
import java.util.ArrayList

/**
 * @Description:
 * @Author:      高烁
 * @CreateDate:  2019-06-10 20:06
 * @Email:       gaoshuo521@foxmail.com
 */
class SearchAlbumFragment : BaseFragment() {

    private lateinit var mAdapter: AlbumAdapter
    private lateinit var albumsList: ArrayList<Album>
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.recylerview, container, false)
        if (arguments != null) {
            albumsList = (arguments as Bundle).getParcelableArrayList("searchAlbum")
        }

        recyclerView = view.findViewById(R.id.recyclerview) as RecyclerView
        layoutManager = LinearLayoutManager(mContext)
        recyclerView.layoutManager = layoutManager
        mAdapter = AlbumAdapter(XimalayaKotlin.context, albumsList)
        mAdapter.setOnKotlinItemClickListener(object : AlbumAdapter.IKotlinItemClickListener {
            override fun onItemClickListener(position: Int) {
                val intent = Intent(context, AlbumActivity::class.java)
                //传递一个 album
                intent.putExtra("album", albumsList[position])
                context?.startActivity(intent)
            }
        })

        recyclerView.adapter = mAdapter
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL))

        return view
    }

    companion object {
        fun newInstance(list: ArrayList<Album>): SearchAlbumFragment {
            val fragment = SearchAlbumFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList("searchAlbum", list)
            fragment.arguments = bundle
            return fragment
        }
    }
}