package com.example.nusantaraaksara.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Aksara(
    val id: Int,
    val nama: String,
    val deskripsi: String,
    val asal_daerah: String,
    val gambar_url: String
) : Parcelable