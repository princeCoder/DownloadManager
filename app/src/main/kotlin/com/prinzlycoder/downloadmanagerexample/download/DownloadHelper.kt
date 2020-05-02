package com.prinzlycoder.downloadmanagerexample.download

import java.io.File

interface DownloadHelper {
    suspend fun downloadFile(remoteUrl: String, title: String, headers: Map<String, String>): File?
}