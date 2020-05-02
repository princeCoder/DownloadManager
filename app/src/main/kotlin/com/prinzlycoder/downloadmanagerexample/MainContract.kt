package com.prinzlycoder.downloadmanagerexample

import java.io.File

interface MainContract {
    interface View {
        fun showPdf(file: File)
        fun sharePdf(file: File)
        fun showDownloadErrorMessage()
        fun showProgressBar()
        fun hideProgressBar()
    }

    interface Presenter {
        fun attachView(view: View)
        fun downloadPdf()
        fun sharePdf()
    }
}