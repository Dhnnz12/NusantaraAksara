package com.example.nusantaraaksara.data.repository

import com.example.nusantaraaksara.data.ApiService
import com.example.nusantaraaksara.model.AturanTransliterasi
import com.example.nusantaraaksara.model.PanduanPenulisan

class AksaraRepository(private val apiService: ApiService) {
    suspend fun fetchPanduanBelajar(idAksara: Int): List<PanduanPenulisan>? {
        return try {
            val response = apiService.getPanduanPenulisan(idAksara)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }
    // Di dalam class AksaraRepository
    suspend fun fetchAturanTransliterasi(idAksara: Int): List<AturanTransliterasi>? {
        val response = apiService.getAturanTransliterasi(idAksara)
        return if (response.isSuccessful) response.body() else null
    }
}