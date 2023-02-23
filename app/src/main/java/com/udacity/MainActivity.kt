package com.udacity

import android.app.DownloadManager
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.udacity.databinding.ActivityMainBinding
import com.udacity.databinding.ContentMainBinding


class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var contentBinding: ContentMainBinding
    private lateinit var receiver: DownloadNotificationReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        contentBinding = ContentMainBinding.bind(binding.content.mainLayout)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        receiver = DownloadNotificationReceiver(contentBinding)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))


        binding.content.customButton.setOnClickListener {
            checkSelectedFile()
        }

        contentBinding.imageView.setOnClickListener {
            checkSelectedFile()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }


    private fun checkSelectedFile(){
        when(contentBinding.radioGroup.checkedRadioButtonId){
            -1 -> Toast.makeText(this,
                "Please make sure to select the item to download",
                Toast.LENGTH_SHORT)
                .show()
            contentBinding.btnGlide.id -> {
                DownloadNotificationReceiver.fileName = getString(R.string.glide)
                download(GLIDE)
            }
            contentBinding.btnLoadApp.id -> {
                DownloadNotificationReceiver.fileName = getString(R.string.load_app)
                download(LOAD_APP)
            }
            contentBinding.btnRetrofit.id -> {
                DownloadNotificationReceiver.fileName = getString(R.string.retrofit)
                download(RETROFIT)
                Log.i("TAG", DownloadNotificationReceiver.fileName)
            }
        }
    }


    private fun download(url: String) {
        contentBinding.customButton.buttonState = ButtonState.Loading
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }



    companion object {
        private const val GLIDE =
            "https://github.com/bumptech/glide"

        private const val LOAD_APP =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter"

        private const val RETROFIT =
            "https://github.com/square/retrofit"

        var downloadID: Long = 0

        lateinit var downloadManager: DownloadManager
    }

}
