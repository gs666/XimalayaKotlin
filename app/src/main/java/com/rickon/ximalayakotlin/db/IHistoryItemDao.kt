package com.rickon.ximalayakotlin.db

import androidx.room.*
import com.rickon.ximalayakotlin.model.HistoryItem


/**
 * @Description:
 * @Author:      高烁
 * @CreateDate:  2020/7/16 6:08 PM
 * @Email:       gaoshuo521@foxmail.com
 */
@Dao
interface IHistoryItemDao {
    //所有的CURD根据primary key进行匹配
    //------------------------query------------------------
    /**
     * 获取历史记录
     *
     * @return 播放历史列表
     */
    @Query("SELECT * FROM listen_history ORDER BY last_listen_time DESC")
    fun getAllHistory(): List<HistoryItem>

    /**
     * 查询某个ID数据是否存在
     */
    @Query("SELECT * FROM listen_history WHERE item_id = :itemId")
    fun getHistoryById(itemId: String): HistoryItem?


    //-----------------------insert----------------------
    // OnConflictStrategy.REPLACE表示如果已经有数据，那么就覆盖掉
    /**
     * 插入数据库
     *
     * @param historyItem 单个播放记录
     * @return id
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHistory(historyItem: HistoryItem): Long?


    //-------------------delete-------------------
    /**
     * 删除所有列表
     * @return 删除数目
     */
    @Query("DELETE FROM listen_history")
    fun deleteAllHistory(): Int

    //---------------------update------------------------
    //更新已有数据，根据主键（uid）匹配，而非整个user对象
    //返回类型int代表更新的条目数目，而非主键uid的值。
    //表示更新了多少条目
    @Update
    fun update(historyItem: HistoryItem)
}