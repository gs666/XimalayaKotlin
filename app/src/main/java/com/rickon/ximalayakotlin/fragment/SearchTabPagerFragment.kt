package com.rickon.ximalayakotlin.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.rickon.ximalayakotlin.R
import com.ximalaya.ting.android.opensdk.model.album.Album
import com.ximalaya.ting.android.opensdk.model.album.Announcer
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio
import com.ximalaya.ting.android.opensdk.model.track.Track

import java.util.ArrayList
import com.ximalaya.ting.android.opensdk.model.search.SearchAll
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants


/**
 * @Description:
 * @Author:      高烁
 * @CreateDate:  2019-06-10 20:06
 * @Email:       gaoshuo521@foxmail.com
 */
class SearchTabPagerFragment : BaseFragment() {

    private var key = ""

    private var trackResults = ArrayList<Track>()
    private var albumResults = ArrayList<Album>()
    private var artistResults = ArrayList<Announcer>()
    private var radioResults = ArrayList<Radio>()


    private lateinit var frameLayout: FrameLayout
    private lateinit var contentView: View
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.load_framelayout, container, false)
        frameLayout = rootView.findViewById(R.id.id_load_frame)
        //加载中视图
        val loadView = LayoutInflater.from(mContext).inflate(R.layout.loading, frameLayout, false)
        frameLayout.addView(loadView)


        arguments?.let {
            (arguments as Bundle).getString("key")?.let { key = it }
        }

        search(key)


        return rootView
    }

    private fun search(key: String) {
        loadSearchAll(key)
    }

    private fun loadSearchAll(queryString: String) {
        val map = HashMap<String, String>()
        map[DTransferConstants.SEARCH_KEY] = queryString
        map[DTransferConstants.PAGE_SIZE] = "40"
        CommonRequest.getSearchAll(map, object : IDataCallBack<SearchAll> {
            override fun onSuccess(p0: SearchAll?) {
                if (p0 != null) {
                    Log.d(TAG, "搜索完成")
                    trackResults = p0.trackList.tracks as ArrayList<Track>
                    albumResults = p0.albumList.albums as ArrayList<Album>
                    radioResults = p0.radioList.radios as ArrayList<Radio>

                    contentView = LayoutInflater.from(mContext)
                            .inflate(R.layout.fragment_search_tab, frameLayout, false)
                    viewPager = contentView.findViewById<View>(R.id.viewpager) as ViewPager

                    val adapter = Adapter(childFragmentManager)
                    adapter.addFragment(SearchTrackFragment.newInstance(trackResults), getString(R.string.track))
                    adapter.addFragment(SearchAlbumFragment.newInstance(albumResults), getString(R.string.album))
                    adapter.addFragment(SearchRadioFragment.newInstance(radioResults), getString(R.string.fm))
                    viewPager.adapter = adapter
                    viewPager.offscreenPageLimit = 3

                    val tabLayout: TabLayout = contentView.findViewById<View>(R.id.tabs) as TabLayout
                    tabLayout.setupWithViewPager(viewPager)
                    viewPager.currentItem = 0
//                    tabLayout.setTabTextColors(R.color.text_color, ThemeUtils.getThemeColorStateList(mContext, R.color.theme_color_primary).getDefaultColor())
//                    tabLayout.setSelectedTabIndicatorColor(ThemeUtils.getThemeColorStateList(mContext, R.color.theme_color_primary).getDefaultColor())
                    frameLayout.removeAllViews()
                    frameLayout.addView(contentView)
                }


            }

            override fun onError(p0: Int, p1: String?) {

            }
        })
    }

    internal class Adapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        private val mFragments = ArrayList<Fragment>()
        private val mFragmentTitles = ArrayList<String>()

        override fun getCount(): Int = mFragments.size

        fun addFragment(fragment: Fragment, title: String) {
            mFragments.add(fragment)
            mFragmentTitles.add(title)
        }

        override fun getItem(position: Int): Fragment {
            return mFragments[position]
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitles[position]
        }
    }

    companion object {

        fun newInstance(page: Int, key: String): SearchTabPagerFragment {
            val f = SearchTabPagerFragment()
            val bdl = Bundle(1)
            bdl.putInt("page_number", page)
            bdl.putString("key", key)
            f.arguments = bdl
            return f
        }

        private const val TAG = "SearchTabPagerFragment"
    }
}

