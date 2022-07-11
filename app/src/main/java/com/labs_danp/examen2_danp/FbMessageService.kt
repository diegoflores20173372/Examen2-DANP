package com.labs_danp.examen2_danp

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FbMessageService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        showNotification(message.data["message"])
    }

    private fun showNotification(message: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this)
            .setAutoCancel(true)
            .setContentTitle("FCM Test")
            .setContentText(message)
            .setSmallIcon(com.google.android.gms.base.R.drawable.common_google_signin_btn_icon_dark)
            .setContentIntent(pendingIntent)

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(0, builder.build())

    }
}