package com.rickon.ximalayakotlin.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rickon.ximalayakotlin.R
import com.rickon.ximalayakotlin.adapter.HistoryAdapter
import com.rickon.ximalayakotlin.db.HistoryDatabase
import com.rickon.ximalayakotlin.model.HistoryItem
import com.rickon.ximalayakotlin.util.GlobalUtil
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack
import com.ximalaya.ting.android.opensdk.model.PlayableModel
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio
import com.ximalaya.ting.android.opensdk.model.track.LastPlayTrackList
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager
import kotlinx.android.synthetic.main.activity_history.*
import kotlin.collections.ArrayList


class HistoryActivity : BaseActivity(), View.OnClickListener {

    private lateinit var historyAdapter: HistoryAdapter
    private var historyList: MutableList<HistoryItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        initView()

        initListener()

        initRecyclerView()

        Thread(Runnable {

            HistoryDatabase.getInstance(this).historyDao().getAllHistory().forEach {
                historyList.add(it)
            }
            history_recycler.post {
                historyAdapter.notifyDataSetChanged()
            }
        }).start()
    }

    private fun initView() {
        setSupportActionBar(recent_history_toolbar)
        //禁止显示默认 title
        supportActionBar?.setDisplayShowTitleEnabled(false)
        recent_history_toolbar.setNavigationOnClickListener { finish() }
    }

    private fun initListener() {
        clear_all_btn.setOnClickListener(this)
    }

    private fun initRecyclerView() {
        history_recycler.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        historyAdapter = HistoryAdapter(applicationContext, historyList)
        history_recycler.adapter = historyAdapter


        historyAdapter.setOnKotlinItemClickListener(object : HistoryAdapter.IKotlinItemClickListener {
            override fun onItemClickListener(position: Int) {
                if (historyList[position].isAlbum) {
                    loadLastPlayList(historyList[position].itemId, historyList[position].trackId)
                } else {
                    val tempRadio = Radio()
                    with(tempRadio) {
                        dataId = historyList[position].itemId.toLong()
                        kind = PlayableModel.KIND_RADIO
                        radioName = historyList[position].itemTitle
                        coverUrlLarge = historyList[position].itemImagePath
                        XmPlayerManager.getInstance(this@HistoryActivity).playLiveRadioForSDK(this, -1, -1)
                    }
                }
            }
        })
    }

    private fun loadLastPlayList(albumId: String, trackId: String) {
        val map = HashMap<String, String>()
        map[DTransferConstants.ALBUM_ID] = albumId
        map[DTransferConstants.TRACK_ID] = trackId
        map[DTransferConstants.PAGE_SIZE] = 40.toString()

        CommonRequest.getLastPlayTracks(map, object : IDataCallBack<LastPlayTrackList> {

            override fun onSuccess(p0: LastPlayTrackList?) {
                var startIndex = 0
                p0?.tracks?.let {
                    for (item in it) {
                        if (item.dataId.toString() == trackId) {
                            XmPlayerManager.getInstance(this@HistoryActivity).playList(p0.tracks, startIndex)
                            break
                        }
                        startIndex++
                    }
                }

            }

            override fun onError(p0: Int, p1: String?) {

            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.clear_all_btn -> {
                //清除全部
                AlertDialog.Builder(this, R.style.XimalayaAlertDialogStyle)
                        .setMessage(GlobalUtil.getString(R.string.confirm_to_clear_all_history))
                        .setPositiveButton(GlobalUtil.getString(R.string.confirm)) { _, _ ->
                            historyList.clear()
                            historyAdapter.notifyDataSetChanged()
                            Thread(Runnable {
                                HistoryDatabase.getInstance(this).historyDao().deleteAllHistory()
                            }).start()
                        }
                        .setNegativeButton(GlobalUtil.getString(R.string.cancel), null)
                        .create()
                        .show()

            }
        }
    }

    companion object {
        private const val TAG = "HistoryActivity"
    }
}
