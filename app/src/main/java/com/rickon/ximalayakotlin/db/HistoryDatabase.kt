package com.rickon.ximalayakotlin.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rickon.ximalayakotlin.model.HistoryItem

/**
 * @Description:
 * @Author:      Rickon
 * @CreateDate:  2020/7/16 6:11 PM
 * @Email:       gaoshuo521@foxmail.com
 */
@Database(entities = [HistoryItem::class], version = 1, exportSchema = false)
abstract class HistoryDatabase : RoomDatabase() {
    /**
     * 定义抽象方法返回具体Dao
     *
     * @return
     */
    abstract fun historyDao(): IHistoryItemDao

    companion object : SingletonHolder<HistoryDatabase, Context>({
        Room.databaseBuilder(it.applicationContext, HistoryDatabase::class.java, "listen_history.db").build()
    })
}