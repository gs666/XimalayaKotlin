package com.rickon.ximalayakotlin.activities

import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import android.widget.SeekBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.rickon.ximalayakotlin.R
import com.rickon.ximalayakotlin.util.GlobalUtil
import com.ximalaya.ting.android.opensdk.model.PlayableModel
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio
import com.ximalaya.ting.android.opensdk.model.live.schedule.Schedule
import com.ximalaya.ting.android.opensdk.model.track.Track
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_playing.*

class PlayingActivity : BaseActivity(), View.OnClickListener {

    private var track: Track? = null
    private var radio: Radio? = null

    var isLiked: Boolean = false

    private lateinit var mPlayerServiceManager: XmPlayerManager
    private val mPlayerStatusListener = object : IXmPlayerStatusListener {
        override fun onSoundSwitch(laModel: PlayableModel?, curModel: PlayableModel) {
            Log.i(TAG, "onSoundSwitch index:$curModel")

            val model = mPlayerServiceManager.currSound
            var title: String
            var singer: String
            var coverUrl: String
            when (model) {
                is Track -> {
                    title = model.trackTitle
                    singer = model.announcer.nickname
                    coverUrl = model.coverUrlLarge
                }
                is Schedule -> {
                    title = model.relatedProgram.programName
                    singer = model.radioName
                    coverUrl = model.relatedProgram.backPicUrl
                }
                is Radio -> {
                    title = model.programName
                    singer = model.radioName
                    coverUrl = model.coverUrlLarge
                }
                else -> {
                    title = ""
                    singer = ""
                    coverUrl = ""
                }
            }

            playing_title.text = title
            playing_author.text = singer
            Glide.with(this@PlayingActivity)
                    .load(coverUrl).apply(RequestOptions.bitmapTransform(RoundedCorners(15)))
                    .into(image_cover)
        }

        override fun onSoundPrepared() {}

        override fun onSoundPlayComplete() {
            play_pause_btn.setImageResource(R.drawable.ic_play)
        }

        override fun onPlayStop() {
            play_pause_btn.setImageResource(R.drawable.ic_play)
        }

        override fun onPlayStart() {
            play_pause_btn.setImageResource(R.drawable.ic_pause)
        }

        override fun onPlayProgress(currPos: Int, duration: Int) {
            playing_current_time.text = DateUtils.formatElapsedTime(currPos/1000.toLong())
            playing_duration.text = DateUtils.formatElapsedTime(duration/1000.toLong())
            //设置进度条
            playing_progress_bar.progress = currPos/1000
            playing_progress_bar.max = duration/1000
        }

        override fun onPlayPause() {
            play_pause_btn.setImageResource(R.drawable.ic_play)
        }

        override fun onError(exception: XmPlayerException): Boolean {
            play_pause_btn.setImageResource(R.drawable.ic_play)
            return false

        }

        override fun onBufferingStop() {}

        override fun onBufferingStart() {}

        override fun onBufferProgress(percent: Int) {}

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playing)

        mPlayerServiceManager = XmPlayerManager.getInstance(applicationContext)
        mPlayerServiceManager.addPlayerStatusListener(mPlayerStatusListener)

        track = intent.getParcelableExtra("track")
        radio = intent.getParcelableExtra("radio")

        initListener()

        initView()
    }

    private fun initListener() {
        like_btn.setOnClickListener(this)
        play_pause_btn.setOnClickListener(this)
        close_playing_btn.setOnClickListener(this)
        previous_btn.setOnClickListener(this)
        next_btn.setOnClickListener(this)
    }

    private fun initView() {
        playing_title.text = track?.trackTitle
        playing_author.text = track?.announcer?.nickname

        playing_progress_bar.max = track?.duration!!
        playing_progress_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                mPlayerServiceManager.seekTo(seekBar!!.progress*1000)
            }
        })
        //声音时长
        playing_duration.text = DateUtils.formatElapsedTime(track?.duration!!.toLong())

        Glide.with(this)
                .load(track?.coverUrlLarge)
                .into(image_cover)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.like_btn -> {
                if (!isLiked) {
                    like_btn.setImageDrawable(getDrawable(R.drawable.ic_liked))
                    isLiked = true
                } else {
                    like_btn.setImageDrawable(getDrawable(R.drawable.ic_like))
                    isLiked = false
                }
            }
            R.id.close_playing_btn -> {
                finish()
            }
            R.id.play_pause_btn -> {
                if (mPlayerServiceManager.isPlaying) mPlayerServiceManager.pause() else mPlayerServiceManager.play()
            }
            R.id.previous_btn -> {
                mPlayerServiceManager.playPre()
            }
            R.id.next_btn -> {
                mPlayerServiceManager.playNext()
            }
        }
    }

    override fun onDestroy() {
        mPlayerServiceManager.removePlayerStatusListener(mPlayerStatusListener)
        super.onDestroy()
    }

    companion object {
        private const val TAG = "PlayingActivity"
    }
}
