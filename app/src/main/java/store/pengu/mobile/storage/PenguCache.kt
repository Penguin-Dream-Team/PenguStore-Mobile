package store.pengu.mobile.storage

import androidx.collection.LruCache
import kotlin.concurrent.thread

class PenguCache {
    private val cacheSize:Int = 10 * 1024 * 1024 //10Mbit

    private var myCache: LruCache<String, String> = LruCache<String, String>(cacheSize)

    fun getImage(key:String): String? {
        if (myCache.get(key) != null) {
            return myCache.get(key)
        }
        return null
    }

    fun putImage(key:String, value:String) {
        synchronized (myCache) {
            if (myCache.get(key) == null) {
                myCache.put(key, value)
            }
        }
    }
}
