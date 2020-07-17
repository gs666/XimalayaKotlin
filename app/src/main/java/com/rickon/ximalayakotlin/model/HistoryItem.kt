package com.rickon.ximalayakotlin.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @Description:
 * @Author:      高烁
 * @CreateDate:  2019-07-31 16:16
 * @Email:       gaoshuo521@foxmail.com
 */
@Entity(tableName = "listen_history")
class HistoryItem {

    //专辑/广播ID
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "item_id")
    var itemId = ""

    //是否为专辑
    @ColumnInfo(name = "is_album")
    var isAlbum = true

    //专辑/广播名称
    @ColumnInfo(name = "item_title")
    var itemTitle = ""

    //专辑作者
    @ColumnInfo(name = "album_author")
    var albumAuthor = ""

    //封面图
    @ColumnInfo(name = "item_image_path")
    var itemImagePath = ""

    //最后收听时间
    @ColumnInfo(name = "last_listen_time")
    var lastListenTime: Long = 0

    //声音ID
    @ColumnInfo(name = "track_id")
    var trackId = ""

    //音频名称
    @ColumnInfo(name = "track_title")
    var trackTitle = ""

    //中断时间：秒
    @ColumnInfo(name = "last_break_time")
    var lastBreakTime = 0

    override fun toString(): String {
        return "HistoryItem(itemId='$itemId', isAlbum=$isAlbum, itemTitle='$itemTitle', albumAuthor='$albumAuthor', itemImagePath='$itemImagePath', lastListenTime=$lastListenTime, trackId='$trackId', trackTitle='$trackTitle', lastBreakTime=$lastBreakTime)"
    }


}