package com.example.nusantaraaksara.ui.theme.viewmodel

import android.util.Log
import com.example.nusantaraaksara.data.ApiService
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nusantaraaksara.model.Aksara // PASTIKAN IMPORT INI ADA
import com.example.nusantaraaksara.model.AturanTransliterasi
import com.example.nusantaraaksara.model.PanduanPenulisan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.text.lowercase
import kotlin.text.replace


class AksaraViewModel(private val apiService: ApiService) : ViewModel() {

    // --- STATE SECTION ---

    // 1. Aksara List State
    private val _aksaraList = mutableStateOf<List<Aksara>>(emptyList())
    val aksaraList: State<List<Aksara>> = _aksaraList

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _isSuccess = mutableStateOf(false)
    val isSuccess: State<Boolean> = _isSuccess

    // 2. Transliterasi State
    var hasilTransliterasi = mutableStateOf("")
        private set

    // 3. Aturan List State (Disederhanakan menggunakan StateFlow agar sinkron)
    private val _aturanList = MutableStateFlow<List<AturanTransliterasi>>(emptyList())
    val aturanList: StateFlow<List<AturanTransliterasi>> = _aturanList.asStateFlow()
    // 4. Panduan List State
    private val _panduanList = mutableStateOf<List<PanduanPenulisan>>(emptyList())
    val panduanList: State<List<PanduanPenulisan>> = _panduanList

    fun resetSuccessStatus() {
        _isSuccess.value = false
    }
    // Data lokal untuk masing-masing aksara
    // Masukkan ini di dalam class AksaraViewModel
    private val aturanJawa = listOf(
        AturanTransliterasi(1, 1, "ng", "ꦔ"), AturanTransliterasi(2, 1, "ny", "ꦗꦚ"),
        AturanTransliterasi(3, 1, "ha", "ꦗ"), AturanTransliterasi(4, 1, "na", "ꦊ"),
        AturanTransliterasi(5, 1, "ca", "ꦋ"), AturanTransliterasi(6, 1, "ra", "ꦌ"),
        AturanTransliterasi(7, 1, "ka", "ꦍ"), AturanTransliterasi(8, 1, "da", "ꦎ"),
        AturanTransliterasi(9, 1, "ta", "ꦏ"), AturanTransliterasi(10, 1, "sa", "ꦐ"),
        AturanTransliterasi(11, 1, "wa", "ꦑ"), AturanTransliterasi(12, 1, "la", "ꦒ"),
        AturanTransliterasi(13, 1, "pa", "ꦓ"), AturanTransliterasi(14, 1, "dha", "ꦔ"),
        AturanTransliterasi(15, 1, "ja", "ꦕ"), AturanTransliterasi(16, 1, "ya", "ꦖ"),
        AturanTransliterasi(17, 1, "nya", "ꦗ"), AturanTransliterasi(18, 1, "ma", "ꦘ"),
        AturanTransliterasi(19, 1, "ga", "ꦙ"), AturanTransliterasi(20, 1, "ba", "ꦚ"),
        AturanTransliterasi(21, 1, "tha", "ꦛ"), AturanTransliterasi(22, 1, "nga", "ꦜ")
    )

    private val aturanSunda = listOf(
        AturanTransliterasi(101, 2, "ka", "ᮊ"), AturanTransliterasi(102, 2, "ga", "ᮌ"),
        AturanTransliterasi(103, 2, "nga", "ᮍ"), AturanTransliterasi(104, 2, "ca", "ᮎ"),
        AturanTransliterasi(105, 2, "ja", "ᮏ"), AturanTransliterasi(106, 2, "nya", "ᮑ"),
        AturanTransliterasi(107, 2, "ta", "ᮒ"), AturanTransliterasi(108, 2, "da", "ᮓ"),
        AturanTransliterasi(109, 2, "na", "ᮔ"), AturanTransliterasi(110, 2, "pa", "ᮕ"),
        AturanTransliterasi(111, 2, "ba", "ᮖ"), AturanTransliterasi(112, 2, "ma", "ᮗ"),
        AturanTransliterasi(113, 2, "ya", "ᮘ"), AturanTransliterasi(114, 2, "ra", "ᮙ"),
        AturanTransliterasi(115, 2, "la", "ᮚ"), AturanTransliterasi(116, 2, "wa", "ᮛ"),
        AturanTransliterasi(117, 2, "sa", "ᮞ"), AturanTransliterasi(118, 2, "ha", "ᮠ")
    )

    private val aturanBali = listOf(
        AturanTransliterasi(201, 3, "ha", "ᬳ"), AturanTransliterasi(202, 3, "na", "ᬦ"),
        AturanTransliterasi(203, 3, "ca", "ᬘ"), AturanTransliterasi(204, 3, "ra", "ᬭ"),
        AturanTransliterasi(205, 3, "ka", "ᬓ"), AturanTransliterasi(206, 3, "da", "ᬤ"),
        AturanTransliterasi(207, 3, "ta", "ᬢ"), AturanTransliterasi(208, 3, "sa", "ᬲ"),
        AturanTransliterasi(209, 3, "wa", "ᬯ"), AturanTransliterasi(210, 3, "la", "ᬮ"),
        AturanTransliterasi(211, 3, "ma", "ᬫ"), AturanTransliterasi(212, 3, "ga", "ᬕ"),
        AturanTransliterasi(213, 3, "ba", "ᬩ"), AturanTransliterasi(214, 3, "nga", "ᬗ")
    )

    // Fungsi Konversi berdasarkan pilihan Aksara
    fun konversiTeks(input: String, idAksara: Int) {
        var tempHasil = input.lowercase()

        // Pilih daftar berdasarkan ID yang sedang aktif
        val aturanAktif = when(idAksara) {
            1 -> aturanJawa
            2 -> aturanSunda
            3 -> aturanBali
            else -> aturanJawa
        }

        // Urutkan agar kombinasi 2 huruf (ng, nya) diproses duluan
        val aturanSorted = aturanAktif.sortedByDescending { it.fonemLatin.length }

        aturanSorted.forEach { item ->
            tempHasil = tempHasil.replace(item.fonemLatin, item.karakterAksara)
        }

        hasilTransliterasi.value = tempHasil
    }
    // --- CORE FUNCTIONS (CRUD) ---

    fun ambilSemuaAksara() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.getAllAksara()
                _aksaraList.value = response
            } catch (e: Exception) {
                Log.e("AksaraVM", "Gagal mengambil data: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }


    // Alias untuk ambilSemuaAksara jika dibutuhkan oleh screen lain
    fun loadAksara() = ambilSemuaAksara()

    fun tambahAksara(nama: String, deskripsi: String, asal: String, url: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val response = apiService.addAksara(Aksara(0, nama, deskripsi, asal, url))
                if (response.isSuccessful) {
                    ambilSemuaAksara()
                    onSuccess()
                }
            } catch (e: Exception) {
                Log.e("AksaraVM", "Tambah gagal: ${e.message}")
            }
        }
    }

    fun updateAksara(id: Int, nama: String, deskripsi: String, asal: String, url: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val response = apiService.updateAksara(id, Aksara(id, nama, deskripsi, asal, url))
                if (response.isSuccessful) {
                    ambilSemuaAksara()
                    onSuccess()
                }
            } catch (e: Exception) {
                Log.e("AksaraVM", "Update gagal: ${e.message}")
            }
        }
    }

    fun hapusAksara(id: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.deleteAksara(id)
                if (response.isSuccessful) {
                    ambilSemuaAksara()
                }
            } catch (e: Exception) {
                Log.e("AksaraVM", "Hapus gagal: ${e.message}")
            }
        }
    }

    // --- FEATURE FUNCTIONS (TRANSLITERASI & EKSPLORASI) ---

    fun loadAturan(id: Int) {
        // Tambahkan log untuk memastikan ID yang dikirim bukan 0
        Log.d("AksaraVM", "Memulai loadAturan untuk ID: $id")

        viewModelScope.launch {
            try {
                // Gunakan nama fungsi yang ada di ApiService Anda
                val response = apiService.getAturanTransliterasi(id)

                if (response.isSuccessful) {
                    val data = response.body() ?: emptyList()
                    _aturanList.value = data
                    Log.d("AksaraVM", "Berhasil! Mendapatkan ${data.size} aturan dari API")
                } else {
                    Log.e("AksaraVM", "Gagal API: Kode ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("AksaraVM", "Terjadi Exception: ${e.message}")
            }
        }
    }
    fun tambahPanduan(idAksara: Int, karakter: String, animasi: String, langkah: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                // ID_Panduan 0 karena database menggunakan Auto Increment
                val dataBaru = PanduanPenulisan(0, idAksara, karakter, animasi, langkah)
                val response = apiService.addPanduan(dataBaru)
                if (response.isSuccessful) {
                    onSuccess()
                    loadPanduan(idAksara) // Refresh data setelah berhasil tambah
                }
            } catch (e: Exception) {
                Log.e("AksaraVM", "Tambah gagal: ${e.message}")
            }
        }
    }
    fun updatePanduan(idPanduan: Int, idAksara: Int, karakter: String, animasi: String, langkah: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val dataUpdate = PanduanPenulisan(idPanduan, idAksara, karakter, animasi, langkah)
                val response = apiService.updatePanduan(idPanduan, dataUpdate)
                if (response.isSuccessful) {
                    onSuccess()
                    loadPanduan(idAksara) // Refresh data setelah berhasil update
                }
            } catch (e: Exception) {
                Log.e("AksaraVM", "Update gagal: ${e.message}")
            }
        }
    }

    fun hapusPanduan(idPanduan: Int, idAksara: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.deletePanduan(idPanduan)
                if (response.isSuccessful) {
                    loadPanduan(idAksara) // Refresh data setelah berhasil hapus
                }
            } catch (e: Exception) {
                Log.e("AksaraVM", "Hapus gagal: ${e.message}")
            }
        }
    }


    fun changePassword(userId: Int, oldPass: String, newPass: String, onResult: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = apiService.changePassword(
                    request = mapOf(
                        "id_pengguna" to userId.toString(), // Sesuaikan dengan backend
                        "oldPassword" to oldPass,
                        "newPassword" to newPass
                    )
                )

                if (response.isSuccessful) {
                    onResult("Berhasil")
                } else {
                    onResult("Gagal: Kata sandi lama salah")
                }
            } catch (e: Exception) {
                onResult("Error: ${e.message}")
            }
        }
    }
    fun loadPanduan(idAksara: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            // PENTING: Kosongkan list lama agar UI merespon perubahan aksara dengan cepat
            _panduanList.value = emptyList()

            try {
                val response = apiService.getPanduanPenulisan(idAksara)
                if (response.isSuccessful) {
                    val data = response.body() ?: emptyList()
                    _panduanList.value = data
                    Log.d("AksaraVM", "Berhasil memuat ${data.size} panduan untuk ID Aksara: $idAksara")
                } else {
                    Log.e("AksaraVM", "Gagal memuat panduan: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("AksaraVM", "Exception saat loadPanduan: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun getAksaraById(id: Int): Aksara? {
        return _aksaraList.value.find { it.id == id }
    }
}
