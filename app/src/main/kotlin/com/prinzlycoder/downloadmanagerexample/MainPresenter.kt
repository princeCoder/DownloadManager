package com.prinzlycoder.downloadmanagerexample

import com.prinzlycoder.downloadmanagerexample.download.DownloadHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import kotlin.coroutines.CoroutineContext

class MainPresenter(private val downloadHelper: DownloadHelper) : MainContract.Presenter,
    CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    var job: Job? = null
    private var file: File? = null
    private var view: MainContract.View? = null

    override fun attachView(view: MainContract.View) {
        this.view = view
    }

    override fun downloadPdf() {
        job = launch {
            val url = "http://www.pdf995.com/samples/pdf.pdf"
            downloadContractPdfAsync(url, hashMapOf())
        }
    }

    private suspend fun downloadContractPdfAsync(pdfUrl: String, headers: Map<String, String>) {
        view?.showProgressBar()
        val file = downloadHelper.downloadFile(pdfUrl, "pdf995", headers)
        if (file != null) {
            this.file = file
            view?.showPdf(file)
            view?.hideProgressBar()
        } else{
            view?.showDownloadErrorMessage()
            view?.hideProgressBar()
        }
    }

    override fun sharePdf() {
        file?.let {
            view?.sharePdf(it)
        }
    }
}