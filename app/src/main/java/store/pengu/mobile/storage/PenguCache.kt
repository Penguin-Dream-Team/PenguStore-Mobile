package store.pengu.mobile.storage

import androidx.collection.LruCache
import java.util.Collections.addAll

object PenguCache {
    private val cacheSize:Int = 10 * 1024 * 1024 //10Mbit

    private var myCache: LruCache<String, MutableList<String>> = LruCache<String, MutableList<String>>(cacheSize)

    fun getAllImage(key:String): MutableList<String>? {
        if (myCache.get(key) != null) {
            return myCache.get(key)
        }
        return null
    }

    fun getImage(key:String, index:Int): String? {
        if (myCache.get(key) != null) {
            val list = myCache.get(key)
            if (list != null) {
                return list[index]
            }
        }
        return null
    }

    fun putAllImage(key:String, value:List<String>) {
        for (s in value) {
            putImage(key, s)
        }
    }

    fun putImage(key:String, value:String) {
        synchronized (myCache) {
            if (myCache.get(key) == null) {
                myCache.put(key, mutableListOf(value))
                return
            }
            if (myCache.get(key) != null) {
                val list = myCache.get(key)
                if (list != null) {
                    list += listOf(value)
                    myCache.put(key, list)
                }
                return
            }
        }
    }
}
