package com.rickon.ximalayakotlin.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rickon.ximalayakotlin.R
import com.rickon.ximalayakotlin.adapter.HistoryAdapter
import com.rickon.ximalayakotlin.model.HistoryItem
import com.rickon.ximalayakotlin.util.GlobalUtil
import com.ximalaya.ting.android.opensdk.model.PlayableModel
import com.ximalaya.ting.android.opensdk.model.track.Track
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager
import kotlinx.android.synthetic.main.activity_history.*
import org.litepal.LitePal
import kotlin.collections.ArrayList


class HistoryActivity : BaseActivity(), View.OnClickListener {

    private lateinit var historyAdapter: HistoryAdapter
    private var historyList: MutableList<HistoryItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        initView()

        initListener()

//        val historyItem = HistoryItem()
//
//        historyItem.itemId = "23594540"
//        historyItem.isAlbum = true
//        historyItem.itemTitle = "大叔爱上我（免费双播总裁）"
//        historyItem.itemImagePath = "http://imagev2.xmcdn.com/group58/M0B/B1/07/wKgLc1zcuL7zxcRGAAP7ri3seV4878.jpg!op_type=3&columns=180&rows=180"
//        historyItem.lastListenTime = Date()
//        historyItem.trackId = "184071832"
//        historyItem.trackTitle = "大叔爱上我片花（喜欢我就订阅我）"
//        historyItem.lastBreakTime = 61
//        historyItem.save()
//
//        historyItem.itemId = "19383749"
//        historyItem.isAlbum = true
//        historyItem.itemTitle = "大叔爱上我（免费双播总裁）"
//        historyItem.itemImagePath = "http://imagev2.xmcdn.com/group58/M0B/B1/07/wKgLc1zcuL7zxcRGAAP7ri3seV4878.jpg!op_type=3&columns=180&rows=180"
//        historyItem.lastListenTime = Date()
//        historyItem.trackId = "184071832"
//        historyItem.trackTitle = "大叔爱上我片花（喜欢我就订阅我）"
//        historyItem.lastBreakTime = 61
//        historyItem.save()

        LitePal.findAll(HistoryItem::class.java).forEach {
            historyList.add(it)
            Log.d(TAG, it.toString())
        }

        history_recycler.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        historyAdapter = HistoryAdapter(applicationContext, historyList)
        history_recycler.adapter = historyAdapter

        historyAdapter.setOnKotlinItemClickListener(object : HistoryAdapter.IKotlinItemClickListener {
            override fun onItemClickListener(position: Int) {
                val track = Track()
                track.kind = PlayableModel.KIND_TRACK
                track.trackTitle = historyList[position].trackTitle
                track.announcer.nickname = historyList[position].albumAuthor
                track.coverUrlLarge = historyList[position].itemImagePath
                track.dataId = historyList[position].trackId.toLong()
                val tracks: MutableList<Track> = ArrayList()
                tracks.add(track)
                XmPlayerManager.getInstance(this@HistoryActivity).playList(tracks, 0)
            }
        })
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.clear_all_btn -> {
                //清除全部
                AlertDialog.Builder(this, R.style.XimalayaAlertDialogStyle)
                        .setMessage(GlobalUtil.getString(R.string.confirm_to_clear_all_history))
                        .setPositiveButton(GlobalUtil.getString(R.string.confirm)) { _, _ ->
                            LitePal.deleteAll(HistoryItem::class.java)
                            historyList.clear()
                            historyAdapter.notifyDataSetChanged()
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
