package com.rickon.ximalayakotlin.fragment

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rickon.ximalayakotlin.R
import com.rickon.ximalayakotlin.activities.AlbumActivity
import com.rickon.ximalayakotlin.activities.RadioActivity
import com.rickon.ximalayakotlin.activities.RankListActivity
import com.rickon.ximalayakotlin.adapter.AlbumAdapter
import com.rickon.ximalayakotlin.util.XimalayaKotlin
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack
import com.ximalaya.ting.android.opensdk.model.album.Album
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList
import kotlinx.android.synthetic.main.recommend_frag_layout.*
import java.util.HashMap

/**
 * @Description:
 * @Author:      高烁
 * @CreateDate:  2019-07-17 15:45
 * @Email:       gaoshuo521@foxmail.com
 */
class RecommendFrag : BaseFragment(), View.OnClickListener {

    private var mAlbumList: MutableList<Album> = mutableListOf()
    private lateinit var albumAdapter: AlbumAdapter

    override fun mainHandlerMessage(baseFragment: BaseFragment?, msg: Message) {
        super.mainHandlerMessage(baseFragment, msg)
        when (msg.what) {
            MSG_LOAD_SUCCESS -> albumAdapter.notifyDataSetChanged()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.recommend_frag_layout, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initListener()
        initRecyclerView()
        loadGuessLikeAlbum()
    }

    private fun initListener() {
        walkman_btn.setOnClickListener(this)
        radio_btn.setOnClickListener(this)
        rank_list_btn.setOnClickListener(this)
        lab_btn.setOnClickListener(this)
    }

    private fun initRecyclerView() {
        guess_like_recycler.layoutManager = LinearLayoutManager(XimalayaKotlin.context)
        //下面代码解决滑动无惯性的问题
        guess_like_recycler.isNestedScrollingEnabled = false
        albumAdapter = AlbumAdapter(XimalayaKotlin.context, mAlbumList)
        guess_like_recycler.adapter = albumAdapter

        guess_like_recycler.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.top = 20
            }
        })

        albumAdapter.setOnKotlinItemClickListener(object : AlbumAdapter.IKotlinItemClickListener {
            override fun onItemClickListener(position: Int) {
                Log.d(TAG, position.toString())

                val intent = Intent(context, AlbumActivity::class.java)
                //传递一个 album
                intent.putExtra("album", mAlbumList[position])
                context?.startActivity(intent)

                albumAdapter.notifyDataSetChanged()
            }
        })
    }

    private fun loadGuessLikeAlbum() {
        val maps = HashMap<String, String>()
        maps[DTransferConstants.LIKE_COUNT] = PAGE_SIZE

        CommonRequest.getGuessLikeAlbum(maps, object : IDataCallBack<GussLikeAlbumList> {
            override fun onSuccess(@Nullable gussLikeAlbumList: GussLikeAlbumList?) {
                if (gussLikeAlbumList != null) {
                    mAlbumList.addAll(gussLikeAlbumList.albumList)
                    mainHandler.sendEmptyMessage(MSG_LOAD_SUCCESS)
                }
            }

            override fun onError(i: Int, s: String) {

            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.walkman_btn -> {
                Toast.makeText(context, "暂未开发此功能", Toast.LENGTH_SHORT).show()
            }
            R.id.radio_btn -> {
                val intent = Intent(context, RadioActivity::class.java)
                context?.startActivity(intent)
            }
            R.id.rank_list_btn -> {
                val intent = Intent(context, RankListActivity::class.java)
                context?.startActivity(intent)
            }
            R.id.lab_btn -> {
                Toast.makeText(context, "暂未开发此功能", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    companion object {

        private const val TAG = "RecommendFrag"
        private const val PAGE_SIZE = "50"

        private const val MSG_LOAD_SUCCESS = 0

        fun newInstance(): RecommendFrag {
            return RecommendFrag()
        }
    }
}