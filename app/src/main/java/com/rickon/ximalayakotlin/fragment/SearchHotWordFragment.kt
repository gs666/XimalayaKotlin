package com.rickon.ximalayakotlin.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import com.rickon.ximalayakotlin.R
import com.rickon.ximalayakotlin.view.WidgetController
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack
import com.ximalaya.ting.android.opensdk.model.word.HotWordList

import java.util.ArrayList

/**
 * @Description:
 * @Author:      高烁
 * @CreateDate:  2019-06-14 15:06
 * @Email:       gaoshuo521@foxmail.com
 */
class SearchHotWordFragment : BaseFragment(), View.OnClickListener, SearchWords {
    private var texts = arrayOfNulls<String>(10)
    private var views = ArrayList<TextView>()
    private var searchWords: SearchWords? = null
    private lateinit var loadView: View
    private lateinit var frameLayout: FrameLayout

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.load_framelayout, container, false)
        frameLayout = view.findViewById(R.id.id_load_frame)
        loadView = LayoutInflater.from(mContext).inflate(R.layout.loading, frameLayout, false)
        frameLayout.addView(loadView)

        loadHotWords()

        return view
    }

    private fun initData() {
        val view = LayoutInflater.from(mContext).inflate(R.layout.fragment_search_hot_words, frameLayout, false)
        val text1: TextView = view.findViewById(R.id.text1)
        val text2: TextView = view.findViewById(R.id.text2)
        val text3: TextView = view.findViewById(R.id.text3)
        val text4: TextView = view.findViewById(R.id.text4)
        val text5: TextView = view.findViewById(R.id.text5)
        val text6: TextView = view.findViewById(R.id.text6)
        val text7: TextView = view.findViewById(R.id.text7)
        val text8: TextView = view.findViewById(R.id.text8)
        val text9: TextView = view.findViewById(R.id.text9)
        val text10: TextView = view.findViewById(R.id.text10)
        views.add(text1)
        views.add(text2)
        views.add(text3)
        views.add(text4)
        views.add(text5)
        views.add(text6)
        views.add(text7)
        views.add(text8)
        views.add(text9)
        views.add(text10)

        frameLayout.removeAllViews()
        frameLayout.addView(view)

        val w = mContext.resources.displayMetrics.widthPixels
        var xDistance = -1
        var yDistance = 0
        val distance = dip2px(mContext, 16f)
        var i = 0
        while (i < 10) {
            views[i].setOnClickListener(this@SearchHotWordFragment)
            views[i].text = texts[i]
            if (xDistance == -1) {
                xDistance = 0
                WidgetController.setLayout(views[i], xDistance, yDistance)
                i++
                continue
            }
            xDistance += WidgetController.getWidth(views[i - 1]) + distance
            if (xDistance + WidgetController.getWidth(views[i]) + distance > w) {
                xDistance = -1
                yDistance += 120
                i--
                i++
                continue
            }
            WidgetController.setLayout(views[i], xDistance, yDistance)
            i++
        }
    }

    private fun loadHotWords() {
        val map = HashMap<String, String>()
        map[DTransferConstants.TOP] = "10"
        CommonRequest.getHotWords(map, object : IDataCallBack<HotWordList> {
            override fun onSuccess(p0: HotWordList?) {
                var index = 0
                p0!!.hotWordList.forEach {
                    texts[index] = it.searchword
                    index++
                }
                initData()
            }

            override fun onError(p0: Int, p1: String?) {

            }
        })
    }

    fun searchWords(searchWords: SearchWords) {
        this.searchWords = searchWords
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.text1 -> searchWords!!.onSearch(texts[0]!!)
            R.id.text2 -> searchWords!!.onSearch(texts[1]!!)
            R.id.text3 -> searchWords!!.onSearch(texts[2]!!)
            R.id.text4 -> searchWords!!.onSearch(texts[3]!!)
            R.id.text5 -> searchWords!!.onSearch(texts[4]!!)
            R.id.text6 -> searchWords!!.onSearch(texts[5]!!)
            R.id.text7 -> searchWords!!.onSearch(texts[6]!!)
            R.id.text8 -> searchWords!!.onSearch(texts[7]!!)
            R.id.text9 -> searchWords!!.onSearch(texts[8]!!)
            R.id.text10 -> searchWords!!.onSearch(texts[9]!!)
        }
    }

    override fun onSearch(t: String) {
        if (searchWords != null) searchWords!!.onSearch(t)
    }

    companion object {

        fun px2dip(context: Context, pxValue: Float) = (pxValue / context.resources.displayMetrics.density + 0.5f).toInt()

        fun dip2px(context: Context, dipValue: Float) = (dipValue * context.resources.displayMetrics.density + 0.5f).toInt()
    }
}
