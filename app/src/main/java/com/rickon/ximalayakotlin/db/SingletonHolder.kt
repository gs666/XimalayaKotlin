package com.rickon.ximalayakotlin.db

/**
 * @Description:
 * @Author:      Rickon
 * @CreateDate:  2020/7/16 6:30 PM
 * @Email:       gaoshuo521@foxmail.com
 */
open class SingletonHolder<T, A>(creator: (A) -> T) {
    private var creator: ((A) -> T)? = creator

    @Volatile
    private var instance: T? = null

    fun getInstance(arg: A): T {
        val i = instance
        if (i != null) {
            return i
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = creator!!(arg)
                instance = created
                creator = null
                created
            }
        }
    }
}