package com.vde.mhvezdekod22re.utils

import android.content.Context

object UtilJson {
    fun Context.loadJSONFromAssets(fileName: String): String {
        return applicationContext.assets.open(fileName).bufferedReader().use { reader ->
            reader.readText()
        }
    }
}