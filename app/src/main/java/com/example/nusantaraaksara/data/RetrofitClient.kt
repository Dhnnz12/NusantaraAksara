package com.example.nusantaraaksara.data


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // GANTI IP INI dengan IP Laptop kamu (Cek lewat cmd: ipconfig)
    // Jangan pakai 'localhost' karena Android menganggap itu dirinya sendiri
    private const val BASE_URL = "http://192.168.2.34:3000/"

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }
}