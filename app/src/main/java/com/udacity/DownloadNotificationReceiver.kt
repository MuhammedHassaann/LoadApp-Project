package com.udacity

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.udacity.databinding.ContentMainBinding

class DownloadNotificationReceiver(
    private val binding: ContentMainBinding
    ): BroadcastReceiver() {

    private lateinit var notification: DownloadNotificationService

    override fun onReceive(context: Context, intent: Intent?) {
        notification = DownloadNotificationService(context)
        val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        sendNotification(id)
    }

    private fun sendNotification(id: Long?) {
        val downloadID = MainActivity.downloadID
        if (id == downloadID) {
            val cursor = MainActivity.downloadManager.query(DownloadManager.Query().setFilterById(downloadID))
            if (cursor.moveToFirst()){
                val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                val status = cursor.getInt(columnIndex)
                if(status == DownloadManager.STATUS_SUCCESSFUL) {
                    downloadStatus = "Success"
                    binding.customButton.buttonState = ButtonState.Completed
                }
            }
            notification.showDownloadNotification(fileName,downloadStatus)
        }
    }

    companion object{
        var fileName: String =""
        var downloadStatus = "Failed"
    }
}