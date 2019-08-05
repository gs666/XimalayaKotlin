package com.rickon.ximalayakotlin.model

import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport
import java.util.*

/**
 * @Description:
 * @Author:      高烁
 * @CreateDate:  2019-07-31 16:16
 * @Email:       gaoshuo521@foxmail.com
 */
class HistoryItem : LitePalSupport() {

    //专辑/广播ID
    @Column(unique = true)
    var itemId = ""

    //是否为专辑
    var isAlbum = true

    //专辑/广播名称
    var itemTitle = ""

    //专辑作者
    var albumAuthor = ""

    //封面图
    var itemImagePath = ""

    //最后收听时间
    var lastListenTime:Long = 0

    //声音ID
    var trackId = ""

    //音频名称
    var trackTitle = ""

    //中断时间：秒
    var lastBreakTime = 0

    override fun toString(): String {
        return "HistoryItem(itemId='$itemId', isAlbum=$isAlbum, itemTitle='$itemTitle', albumAuthor='$albumAuthor', itemImagePath='$itemImagePath', lastListenTime=$lastListenTime, trackId='$trackId', trackTitle='$trackTitle', lastBreakTime=$lastBreakTime)"
    }


}