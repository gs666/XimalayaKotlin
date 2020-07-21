package com.rickon.ximalayakotlin.fragment


import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
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


class CommonCategoryFrag(private var categoryId: Int) : BaseFragment() {

    private lateinit var albumAdapter: AlbumAdapter
    private var hotBookAlbumList: MutableList<Album> = mutableListOf()

    private lateinit var newAlbumAdapter: AlbumAdapter
    private var newAlbumList: MutableList<Album> = mutableListOf()

    override fun mainHandlerMessage(baseFragment: BaseFragment?, msg: Message) {
        super.mainHandlerMessage(baseFragment, msg)
        when (msg.what) {
            MSG_LOAD_SUCCESS -> albumAdapter.notifyDataSetChanged()
            MSG_LOAD_NEW_SUCCESS -> newAlbumAdapter.notifyDataSetChanged()
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
        initRecyclerView()
        loadAlbumList()
        loadNewAlbumList()
    }

    private fun initRecyclerView() {
        hot_books_recycler.layoutManager = LinearLayoutManager(XimalayaKotlin.context)
        //下面代码解决滑动无惯性的问题
        hot_books_recycler.isNestedScrollingEnabled = false
        albumAdapter = AlbumAdapter(XimalayaKotlin.context, hotBookAlbumList)
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
                intent.putExtra("album", hotBookAlbumList[position])
                context?.startActivity(intent)

                albumAdapter.notifyDataSetChanged()
            }
        })

        new_album_recycler.layoutManager = LinearLayoutManager(XimalayaKotlin.context)
        //下面代码解决滑动无惯性的问题
        new_album_recycler.isNestedScrollingEnabled = false
        newAlbumAdapter = AlbumAdapter(XimalayaKotlin.context, newAlbumList)
        new_album_recycler.adapter = newAlbumAdapter

        new_album_recycler.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.top = 10
            }
        })

        newAlbumAdapter.setOnKotlinItemClickListener(object : AlbumAdapter.IKotlinItemClickListener {
            override fun onItemClickListener(position: Int) {
                Log.d(TAG, position.toString())

                val intent = Intent(context, AlbumActivity::class.java)
                //传递一个 album
                intent.putExtra("album", newAlbumList[position])
                context?.startActivity(intent)

                newAlbumAdapter.notifyDataSetChanged()
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    private fun loadAlbumList() {
        val map = HashMap<String, String>()
        map[DTransferConstants.CATEGORY_ID] = categoryId.toString()
        //有声书
        map[DTransferConstants.CALC_DIMENSION] = "1"
        map[DTransferConstants.PAGE_SIZE] = "10"

        //最火
        CommonRequest.getAlbumList(map, object : IDataCallBack<AlbumList> {
            override fun onSuccess(p0: AlbumList?) {
                p0?.albums?.let {
                    if (it.size > 0) {
                        hotBookAlbumList.addAll(p0.albums)
                        mainHandler.sendEmptyMessage(MSG_LOAD_SUCCESS)
                    }
                }
            }

            override fun onError(p0: Int, p1: String?) {
            }
        })
    }

    private fun loadNewAlbumList() {
        val map = HashMap<String, String>()
        map[DTransferConstants.CATEGORY_ID] = categoryId.toString()
        //有声书
        map[DTransferConstants.CALC_DIMENSION] = "2"
        map[DTransferConstants.PAGE_SIZE] = "10"

        //最火
        CommonRequest.getAlbumList(map, object : IDataCallBack<AlbumList> {
            override fun onSuccess(p0: AlbumList?) {
                p0?.albums?.let {
                    if (it.size > 0) {
                        newAlbumList.addAll(p0.albums)
                        mainHandler.sendEmptyMessage(MSG_LOAD_NEW_SUCCESS)
                    }
                }
            }

            override fun onError(p0: Int, p1: String?) {
            }
        })
    }

    companion object {

        private const val TAG = "CommonCategoryFrag"
        private const val MSG_LOAD_SUCCESS = 1
        private const val MSG_LOAD_NEW_SUCCESS = MSG_LOAD_SUCCESS + 1

        fun newInstance(categoryId: Int): CommonCategoryFrag {
            return CommonCategoryFrag(categoryId)
        }
    }
}
