package com.example.nusantaraaksara.ui.theme.viewmodel

import android.app.Application
import com.example.nusantaraaksara.data.ApiService
import com.example.nusantaraaksara.ui.theme.state.AuthState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nusantaraaksara.data.SessionManager
import kotlinx.coroutines.launch

class AuthViewModel(application: Application, // Tambahkan parameter application
                    private val apiService: ApiService
) : AndroidViewModel(application) {

    // Menggunakan mutableStateOf agar UI Jetpack Compose otomatis terupdate saat state berubah
    private val _state = mutableStateOf(AuthState())
    val state: State<AuthState> = _state
    // Di dalam AuthViewModel saat login success

    // 2. Inisialisasi SessionManager menggunakan context aplikasi secara aman
    private val sessionManager = SessionManager(application.applicationContext)
    /**
     * Fungsi untuk Login Pengguna
     */
    fun login(username: String, password: String) {
        if (username.isEmpty() || password.isEmpty()) {
            _state.value = _state.value.copy(errorMessage = "Username dan Password tidak boleh kosong")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)

            try {
                val request = mapOf("username" to username, "password" to password)
                val response = apiService.login(request)

                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!

                    // --- PERBAIKAN DI SINI ---
                    // Pastikan loginResponse memiliki data user dari API Node.js kamu
                    _state.value = _state.value.copy(
                        isLoading = false,
                        isLoginSuccess = true,
                        token = loginResponse.token,
                        // Masukkan data user ke state agar bisa dibaca di Profile Screen
                        user = loginResponse.user,
                        errorMessage = null
                    )
                } else {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "Username atau password salah"
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Koneksi gagal: ${e.localizedMessage}"
                )
            }
        }
    }
    fun gantiPassword(idPengguna: String, passLama: String, passBaru: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                val requestBody = mapOf(
                    "id_pengguna" to idPengguna,
                    "oldPassword" to passLama,
                    "newPassword" to passBaru
                )
                val response = apiService.changePassword(requestBody)

                if (response.isSuccessful) {
                    onResult(true, "Berhasil memperbarui kata sandi")
                } else {
                    // Mengambil pesan error dari backend jika ada (misal: "Kata sandi lama salah")
                    onResult(false, "Gagal: Kata sandi lama tidak sesuai")
                }
            } catch (e: Exception) {
                onResult(false, "Terjadi kesalahan: ${e.message}")
            }
        }
    }

    /**
     * Fungsi untuk Registrasi Pengguna Baru
     */
    fun register(username: String, email: String, password: String) {
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            _state.value = _state.value.copy(errorMessage = "Semua kolom harus diisi")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)

            try {
                val request = mapOf(
                    "username" to username,
                    "email" to email,
                    "password" to password
                )
                val response = apiService.register(request)

                if (response.isSuccessful) {
                    // Register Berhasil
                    _state.value = _state.value.copy(
                        isLoading = false,
                        isRegisterSuccess = true,
                        errorMessage = null
                    )
                } else {
                    // Register Gagal (Contoh: Username sudah ada)
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "Registrasi gagal. Username mungkin sudah digunakan."
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Terjadi kesalahan: ${e.localizedMessage}"
                )
            }
        }
    }

    /**
     * Reset status sukses (digunakan setelah navigasi agar tidak trigger berulang kali)
     */
    fun resetAuthState() {
        _state.value = _state.value.copy(
            isLoginSuccess = false,
            isRegisterSuccess = false,
            errorMessage = null
        )
    }
    fun logout() {
        // Mereset state menjadi kosongan kembali
        _state.value = AuthState()

        // Jika kamu menggunakan SharedPreferences untuk simpan token,
        // hapus juga tokennya di sini jika perlu.
    }}
