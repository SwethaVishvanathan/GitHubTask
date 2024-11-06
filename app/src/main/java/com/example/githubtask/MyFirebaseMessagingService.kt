package com.example.githubtask

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    // Called when a new FCM message is received
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Check if the message contains a notification payload
        remoteMessage.notification?.let {
            // Handle the notification
            Log.d("FCM", "Notification Message Body: ${it.body}")
        }

        // Check if the message contains data payload
        if (remoteMessage.data.isNotEmpty()) {
            // Handle the data payload
            Log.d("FCM", "Data Payload: ${remoteMessage.data}")
        }

        // Show notification if the app is in the background
        remoteMessage.notification?.let {
            showNotification(it.title, it.body)
        }
    }

    // Called when a new token is generated
    override fun onNewToken(token: String) {
        // Handle the new token
        Log.d("FCM", "New Token: $token")
        // Send the token to your backend if necessary
    }

    // Function to show notifications
    private fun showNotification(title: String?, body: String?) {
        val notificationManager = getSystemService(NotificationManager::class.java)

        // Create notification channel (required for Android O and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "default_channel",
                "Default Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, "default_channel")
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_notification)  // Use a valid icon
            .build()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        NotificationManagerCompat.from(this).notify(0, notification)
    }
}
