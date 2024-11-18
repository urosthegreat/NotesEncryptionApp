package com.example.notesencryptedapp.network

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

object SecureTextApiService {
    private const val apiKey = "a71bbdf5a4msh317b5d8341f7d17p1be7aajsn78f406322817"
    private const val apiHost = "secure-text-api.p.rapidapi.com"

    fun getKey(): String {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://secure-text-api.p.rapidapi.com/getKey")
            .get()
            .addHeader("X-RapidAPI-Key", apiKey)
            .addHeader("X-RapidAPI-Host", apiHost)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            return response.body?.string() ?: ""
        }
    }

    fun encryptData(key: String, plaintext: String): String {
        val client = OkHttpClient()
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = "{\"msgCode\":\"$key\",\"plaintext\":\"$plaintext\"}".toRequestBody(mediaType)
        val request = Request.Builder()
            .url("https://secure-text-api.p.rapidapi.com/encrypt")
            .post(body)
            .addHeader("X-RapidAPI-Key", apiKey)
            .addHeader("X-RapidAPI-Host", apiHost)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            return response.body?.string() ?: ""
        }
    }

    fun decryptData(key: String, ciphertext: String): String {
        val client = OkHttpClient()
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = "{\"msgCode\":\"$key\",\"ciphertext\":\"$ciphertext\"}".toRequestBody(mediaType)
        val request = Request.Builder()
            .url("https://secure-text-api.p.rapidapi.com/decrypt")
            .post(body)
            .addHeader("X-RapidAPI-Key", apiKey)
            .addHeader("X-RapidAPI-Host", apiHost)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            return response.body?.string() ?: ""
        }
    }
}
