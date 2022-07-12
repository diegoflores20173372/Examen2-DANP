package com.labs_danp.examen2_danp

import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class FbMessageService : FirebaseMessagingService() {

    private val db = Firebase.firestore

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        showNotification(
            remoteMessage.notification!!.title, remoteMessage.notification!!.body
        )
    }

    private fun showNotification(title: String?, message: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        val notificationManager = NotificationManagerCompat.from(this)

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder.build())
    }

    override fun onNewToken(token: String) {
        val newToken = hashMapOf("token" to token)
        db.collection("tokens_devices")
            .add(newToken)
            .addOnSuccessListener { documentReference ->
                Log.d("NewTokenSuccess", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
            Log.w("NewTokenFailure", "Error adding document", e)
        }
    }

    companion object {
        private const val TAG = "PushNotification"
        private const val CHANNEL_ID = "101"
    }
}