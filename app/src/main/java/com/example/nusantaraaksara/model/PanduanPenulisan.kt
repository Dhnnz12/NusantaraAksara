package com.example.nusantaraaksara.model

import com.google.gson.annotations.SerializedName

data class PanduanPenulisan(
    @SerializedName("ID_Panduan") val idPanduan: Int,    // Sesuaikan: ID_Panduan
    @SerializedName("ID_Aksara") val idAksara: Int,      // Sesuaikan: ID_Aksara
    @SerializedName("Karakter_Target") val karakterTarget: String,
    @SerializedName("File_Animasi") val fileAnimasi: String,
    @SerializedName("Urutan_Langkah") val urutanLangkah: String
)
