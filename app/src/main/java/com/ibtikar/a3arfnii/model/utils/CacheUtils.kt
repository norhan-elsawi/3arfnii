package com.ibtikar.a3arfnii.model.utils

import android.content.Context
import android.os.StatFs
import java.io.File

/**
 * Created by norhan.elsawi on 11/26/2017.
 */
object CacheUtils {
    fun createDefaultCacheDir(context: Context): File {
        val cache = File(context.applicationContext.cacheDir, "picasso-cache")
        if (!cache.exists()) {

            cache.mkdirs()
        }
        return cache
    }

    fun calculateDiskCacheSize(dir: File): Long {
        var size = (5 * 1024 * 1024).toLong()
        try {
            val statFs = StatFs(dir.absolutePath)
            val available = statFs.blockCount.toLong() * statFs.blockSize
            // Target 2% of the total space.
            size = available / 50
        } catch (ignored: IllegalArgumentException) {
        }

        // Bound inside min/max size for disk cache.
        return Math.max(Math.min(size, (50 * 1024 * 1024).toLong()), (5 * 1024 * 1024).toLong())
    }
}