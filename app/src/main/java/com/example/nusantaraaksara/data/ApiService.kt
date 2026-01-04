package com.example.nusantaraaksara.data

import com.example.nusantaraaksara.model.Aksara
import com.example.nusantaraaksara.model.AturanTransliterasi
import com.example.nusantaraaksara.model.LoginResponse
import com.example.nusantaraaksara.model.PanduanPenulisan
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("api/auth/register")
    suspend fun register(@Body request: Map<String, String>): retrofit2.Response<LoginResponse>

    @POST("api/auth/login")
    suspend fun login(@Body request: Map<String, String>): retrofit2.Response<LoginResponse>

    @GET("api/aksara")
    suspend fun getAksara(): List<Aksara>

    @POST("aksara")
    suspend fun addAksara(@Body aksara: Aksara): Response<Unit>

    @PUT("aksara/{id}")
    suspend fun updateAksara(@Path("id") id: Int, @Body aksara: Aksara): Response<Unit>

    @DELETE("aksara/{id}") // Pastikan endpoint ini sesuai dengan di Node.js
    suspend fun deleteAksara(@Path("id") id: Int): Response<Unit>
    @GET("api/aksara/{id}")
    suspend fun getAksaraById(@Path("id") id: Int): Aksara
    @GET("api/aksara")
    suspend fun getAllAksara(): List<Aksara>
    @GET("transliterasi/aturan/{id_aksara}")
    suspend fun getAturanTransliterasi(
        @Path("id_aksara") idAksara: Int
    ): Response<List<AturanTransliterasi>>
    @PUT("api/auth/change-password")
    suspend fun changePassword(
        @Body request: Map<String, String> // Pastikan menggunakan @Body
    ): Response<Map<String, String>>
    // 3. Mengambil panduan penulisan (stroke order) untuk modul pembelajaran
    // Di file ApiService.kt
    @GET("api/panduan/{id_aksara}") // Sesuaikan dengan route di Node.js
    suspend fun getPanduanPenulisan(
        @Path("id_aksara") idAksara: Int
    ): Response<List<PanduanPenulisan>>
    @POST("api/panduan")
    suspend fun addPanduan(@Body panduan: PanduanPenulisan): Response<Unit>

    @PUT("api/panduan/{id}")
    suspend fun updatePanduan(@Path("id") id: Int, @Body panduan: PanduanPenulisan): Response<Unit>

    @DELETE("api/panduan/{id}")
    suspend fun deletePanduan(@Path("id") id: Int): Response<Unit>




}