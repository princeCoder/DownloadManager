package com.prinzlycoder.downloadmanagerexample

import android.app.DownloadManager
import android.content.Context
import com.prinzlycoder.downloadmanagerexample.download.DownloadHelper
import com.prinzlycoder.downloadmanagerexample.download.FileStorageHelper
import com.prinzlycoder.downloadmanagerexample.download.PdfDownloadManager
import org.koin.dsl.module

val appModule = module {

    factory { App() }

    single {
        val context: Context = get()
        val downloadManager: DownloadManager =
            context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager
    }

    factory {
        FileStorageHelper(get())
    }

    factory {
        val downloadManager: DownloadHelper = PdfDownloadManager(get(), get())
        downloadManager
    }

    factory {
        val mainPresenter: MainContract.Presenter = MainPresenter(get())
        mainPresenter
    }
}