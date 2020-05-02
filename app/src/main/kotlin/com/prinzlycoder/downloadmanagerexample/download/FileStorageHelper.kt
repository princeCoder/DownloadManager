package com.prinzlycoder.downloadmanagerexample.download

import android.content.Context
import java.io.File

class FileStorageHelper(private val context: Context) {

    fun getExternalPath(subPath: String): String? {
        return context.getExternalFilesDir(subPath)?.absolutePath
    }

    fun getInternalPath(subPath: String): String {
        val filesDir = context.filesDir
        val newFile = File(filesDir, subPath)
        if (!newFile.exists()) {
            newFile.mkdir()
        }
        return newFile.absolutePath
    }
}