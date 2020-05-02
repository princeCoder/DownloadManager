package com.prinzlycoder.downloadmanagerexample.download

import android.app.DownloadManager

class PdfDownloadManager(
    manager: DownloadManager?,
    storageHelper: FileStorageHelper
) : FileDownloadManager(manager, storageHelper) {
    override val fileSubPath: String = "pdf"
    override val downloadDescription: String = "Downloading pdf"
    override val fileExtension: String = ".pdf"
}