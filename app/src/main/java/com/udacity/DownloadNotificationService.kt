package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat

class DownloadNotificationService(
    private val context: Context
) {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


    fun showDownloadNotification(fileName: String,status: String){
        Log.i("TAG", fileName)
        val detailActivityIntent = Intent(context,DetailActivity::class.java)
        detailActivityIntent.putExtra("file_name",fileName)
        detailActivityIntent.putExtra("download_status",status)
        val detailActivityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            detailActivityIntent,
            PendingIntent.FLAG_MUTABLE
        )

        val notificationTitle = context.getString(R.string.notification_title)
        val notificationDescription = context.getString(R.string.notification_description)
        val notificationButton = context.getString(R.string.notification_button)

        //creating the notification
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_assistant_black_24dp)
            .setContentTitle(notificationTitle)
            .setContentText(notificationDescription)
            .setContentIntent(detailActivityPendingIntent)
            .addAction(
                R.drawable.ic_assistant_black_24dp,
                notificationButton,
                detailActivityPendingIntent
            )
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1 , notification)

    }


    companion object{
        const val CHANNEL_ID = "channelId"
    }
}