package com.labs_danp.examen2_danp

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException


class FbInstanceIDService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        registerToken(token)
    }

    private fun registerToken(token: String) {
        val client = OkHttpClient()
        val body = FormBody.Builder()
            .add("Token", token)
            .build()
        val request = Request.Builder()
            .url("http://localhost/fcm/register.php")
            .build()
        try {
            client.newCall(request).execute()
        } catch (e: IOException) {
            Log.e("ERROR en RegisterToken", e.toString())
        }


    }


}