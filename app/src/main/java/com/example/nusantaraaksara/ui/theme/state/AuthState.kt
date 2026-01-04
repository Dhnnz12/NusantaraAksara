package com.example.nusantaraaksara.ui.theme.state

import com.example.nusantaraaksara.model.User


data class AuthState(
    val isLoading: Boolean = false,
    val user: User? = null, // Sekarang tulisan User tidak akan merah lagi
    val isLoginSuccess: Boolean = false,
    val isRegisterSuccess: Boolean = false,
    val errorMessage: String? = null,
    val token: String? = null
)