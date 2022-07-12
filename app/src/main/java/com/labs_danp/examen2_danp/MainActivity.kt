package com.labs_danp.examen2_danp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging


class MainActivity : AppCompatActivity() {

    private val db = Firebase.firestore
    private val TAG = "PushNotification"
    private val CHANNEL_ID = "101"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()
        getToken()

    }

    private fun getToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Log.d(TAG, "Completed: $token")

            var flag = true
            db.collection("tokens_devices")
                .get()
                .addOnSuccessListener { result ->
                    val idx = result.size()
                    var index = 0
                    for (document in result) {
                        if((document.data["token"].toString() == token)){
                            flag = false
                            Log.e("CAMBIO", "La bandera es $flag")
                        }
                        index += 1
                        if (index == idx && flag){
                            val newToken = hashMapOf("token" to token)
                            db.collection("tokens_devices")
                                .add(newToken)
                                .addOnSuccessListener { documentReference ->
                                    Log.d(
                                        "NewTokenSuccess",
                                        "DocumentSnapshot added with ID: ${documentReference.id}"
                                    )
                                }
                                .addOnFailureListener { e ->
                                    Log.w("NewTokenFailure", "Error adding document", e)
                                }
                        }
                    }
                }
        })
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "firebaseNotificationChannel"
            val description = "Receive Firebase Notification"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

}