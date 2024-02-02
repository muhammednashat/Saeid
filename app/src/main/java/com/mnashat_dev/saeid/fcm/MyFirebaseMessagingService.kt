package com.mnashat_dev.saeid.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mnashat_dev.saeid.R
import com.mnashat_dev.saeid.ui.MainActivity

class MyFirebaseMessagingService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?) {
        Log.d(TAG, "sendRegistrationTokenToServer($token)")
    }
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("TAG", "From: ${remoteMessage.from}")
        remoteMessage.notification?.let {
            showNotification(it.title, it.body)
        }
    }
    private fun showNotification(title: String?, body: String?) {
        // Use NotificationCompat to build a notification
        val notificationBuilder = NotificationCompat.Builder(this, "CHANNEL_ID")
            .setSmallIcon(R.drawable.image)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setAutoCancel(true)

        // Create an explicit intent for an activity in your app
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        notificationBuilder.setContentIntent(pendingIntent)

        // Show the notification
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create Notification Channel for Android Oreo and higher
        createNotificationChannel(notificationManager)

        // Notify
        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "CHANNEL_ID",
                "Channel Name",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }

}