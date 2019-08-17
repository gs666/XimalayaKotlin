package com.rickon.ximalayakotlin.fragment


import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.rickon.ximalayakotlin.R
import com.rickon.ximalayakotlin.activities.AlbumActivity
import com.rickon.ximalayakotlin.adapter.AlbumAdapter
import com.rickon.ximalayakotlin.util.XimalayaKotlin
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack
import java.util.HashMap
import com.ximalaya.ting.android.opensdk.model.album.AlbumList
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants
import com.ximalaya.ting.android.opensdk.model.album.Album
import kotlinx.android.synthetic.main.fragment_book_city.*


class BookCityFragment : BaseFragment() {

    private lateinit var albumAdapter: AlbumAdapter
    private var hotBookAlbumList: List<Album>? = null


    //uiHandler在主线程中创建，所以自动绑定主线程
    private var uiHandler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg?.what) {
                LOAD_SUCCESS -> {
                    hot_books_recycler.layoutManager = LinearLayoutManager(XimalayaKotlin.context)
                    //下面代码解决滑动无惯性的问题
                    hot_books_recycler.isNestedScrollingEnabled = false
                    albumAdapter = AlbumAdapter(XimalayaKotlin.context!!, hotBookAlbumList!!)
                    hot_books_recycler.adapter = albumAdapter

                    hot_books_recycler.addItemDecoration(object : RecyclerView.ItemDecoration() {
                        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                            super.getItemOffsets(outRect, view, parent, state)
                            outRect.top = 10
                        }
                    })

                    albumAdapter.setOnKotlinItemClickListener(object : AlbumAdapter.IKotlinItemClickListener {
                        override fun onItemClickListener(position: Int) {
                            Log.d(TAG, position.toString())

                            val intent = Intent(context, AlbumActivity::class.java)
                            //传递一个 album
                            intent.putExtra("album", hotBookAlbumList!![position])
                            context?.startActivity(intent)

                            albumAdapter.notifyDataSetChanged()
                        }
                    })
                }
            }
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_city, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        loadAlbumList()
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    private fun loadAlbumList() {
        val map = HashMap<String, String>()
        map[DTransferConstants.CATEGORY_ID] = "3"
        //有声书
        map[DTransferConstants.CALC_DIMENSION] = "1"
        map[DTransferConstants.PAGE_SIZE] = "10"

        //最火
        CommonRequest.getAlbumList(map, object : IDataCallBack<AlbumList> {
            override fun onSuccess(p0: AlbumList?) {
                if (p0?.albums!!.size > 0) {
                    hotBookAlbumList = p0.albums

                    val msg = Message()
                    msg.what = LOAD_SUCCESS
                    uiHandler.sendMessage(msg)
                }
            }

            override fun onError(p0: Int, p1: String?) {
            }
        })
    }

    companion object {

        private const val TAG = "BookCityFragment"
        private const val LOAD_SUCCESS = 1


        fun newInstance(): BookCityFragment {
            return BookCityFragment()
        }
    }
}
