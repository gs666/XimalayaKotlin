package com.rickon.ximalayakotlin.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.rickon.ximalayakotlin.R
import com.ximalaya.ting.android.opensdk.model.track.Track
import com.ximalaya.ting.android.sdkdownloader.XmDownloadManager
import com.ximalaya.ting.android.sdkdownloader.downloadutil.IXmDownloadTrackCallBack
import com.ximalaya.ting.android.sdkdownloader.task.Callback

class DownloadingFragment : BaseFragment(), View.OnClickListener, IXmDownloadTrackCallBack {

    private lateinit var downloadManager: XmDownloadManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView")
        return inflater.inflate(R.layout.layout_downloading, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initListener()

        downloadManager = XmDownloadManager.getInstance()

    }

    private fun initListener() {

    }

    override fun onClick(v: View?) {
        when (v?.id) {

        }
    }

    override fun onSuccess(p0: Track?) {

    }

    override fun onWaiting(p0: Track?) {

    }

    override fun onCancelled(p0: Track?, p1: Callback.CancelledException?) {

    }

    override fun onRemoved() {
    }

    override fun onStarted(p0: Track?) {
    }

    override fun onProgress(p0: Track?, p1: Long, p2: Long) {
    }

    override fun onError(p0: Track?, p1: Throwable?) {
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    companion object {
        private const val TAG = "DownloadingFragment"

        fun newInstance(): DownloadingFragment {
            return DownloadingFragment()
        }
    }
}
