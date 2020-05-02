package com.prinzlycoder.downloadmanagerexample.download

import android.app.DownloadManager
import android.net.Uri
import androidx.core.net.toUri
import kotlinx.coroutines.delay
import java.io.File

abstract class FileDownloadManager(
    private val downloadManager: DownloadManager?,
    private val storageHelper: FileStorageHelper
) : DownloadHelper {
    protected abstract val fileSubPath: String
    protected abstract val downloadDescription: String
    protected abstract val fileExtension: String

    companion object {
        private const val DELAY_DOWNLOAD = 25L
    }

    override suspend fun downloadFile(remoteUrl: String, title: String, headers: Map<String, String>): File? {
        val fileName = title + fileExtension
        val tempFile = File(storageHelper.getExternalPath(fileSubPath), fileName)
        val request = createRequest(remoteUrl, title, tempFile, headers)
        downloadManager?.enqueue(request)?.let {
            var status: DownloadStatus = DownloadStatus.UNINITIALIZED
            while ({
                    status = checkFileDownloadStatus(it)
                    status
                }() == DownloadStatus.RUNNING ||
                status == DownloadStatus.PENDING
            ) {
                delay(DELAY_DOWNLOAD)
            }
            if (status == DownloadStatus.SUCCESSFUL) return moveFileToInternalStorage(tempFile.absolutePath, fileName)
        }
        return null
    }

    private fun createRequest(remoteUrl: String, title: String, tempFile: File, headers: Map<String, String>): DownloadManager.Request {
        val downloadRequest = DownloadManager.Request(Uri.parse(remoteUrl))
            .setTitle(title)
            .setDescription(downloadDescription)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setDestinationUri(tempFile.toUri())

        for (entry in headers.entries) {
            downloadRequest.addRequestHeader(entry.key, entry.value)
        }

        return downloadRequest
    }

    private fun moveFileToInternalStorage(sourceFilePath: String, title: String): File? {
        val sourceFile = File(sourceFilePath)
        val targetFile = File(storageHelper.getInternalPath(fileSubPath), title)
        if (!sourceFile.exists()) {
            sourceFile.createNewFile()
        }
        if (sourceFile.exists()) {
            sourceFile.copyTo(targetFile, overwrite = true)
            sourceFile.delete()
            return targetFile
        }
        return null
    }

    private fun checkFileDownloadStatus(downloadId: Long): DownloadStatus {
        val query = DownloadManager.Query()
        query.setFilterById(downloadId)
        downloadManager?.let {
            val cursor = it.query(query)
            if (cursor.moveToFirst()) {
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                cursor.close()
                return mapStatus(status)
            }
        }
        return DownloadStatus.ERROR
    }

    private fun mapStatus(status: Int): DownloadStatus {
        return when (status) {
            DownloadManager.STATUS_PAUSED -> DownloadStatus.PAUSED
            DownloadManager.STATUS_PENDING -> DownloadStatus.PENDING
            DownloadManager.STATUS_RUNNING -> DownloadStatus.RUNNING
            DownloadManager.STATUS_SUCCESSFUL -> DownloadStatus.SUCCESSFUL
            DownloadManager.STATUS_FAILED -> DownloadStatus.FAILED
            else -> DownloadStatus.ERROR
        }
    }
}