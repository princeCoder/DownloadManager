package com.prinzlycoder.downloadmanagerexample

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import java.io.File

class MainActivity : AppCompatActivity(), MainContract.View {

    private val presenter: MainContract.Presenter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.attachView(this)
        presenter.downloadPdf()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_share -> presenter.sharePdf()
        }
        return true
    }

    override fun showPdf(file: File) {
        pdf_view.fromFile(file)
            .defaultPage(0)
            .spacing(10)
            .load()
    }

    override fun sharePdf(file: File) {
        val uri =
            FileProvider.getUriForFile(this, "${BuildConfig.APPLICATION_ID}.fileprovider", file)
        val shareIntent = Intent(Intent.ACTION_SEND)
            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            .setType(contentResolver.getType(uri))
            .putExtra(Intent.EXTRA_STREAM, uri)
        startActivity(Intent.createChooser(shareIntent, null))
    }

    override fun showDownloadErrorMessage() {
        Toast.makeText(this, "There was an error downloading the file", Toast.LENGTH_LONG).show()
    }

    override fun showProgressBar() {
        progressBar.show()
    }

    override fun hideProgressBar() {
        progressBar.hide()
    }
}