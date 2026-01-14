package com.example.nusantaraaksara.ui.theme.utils

data class AppStrings(
    val welcome: String,
    val login: String,
    val register: String,
    val username: String,
    val password: String,
    val dashboardTitle: String,
    val searchPlaceholder: String,
    val listAksara: String,
    val detailTitle: String,
    val deskripsiLengkap: String,
    val settingsTitle: String,
    val logout: String,
    val changePassword: String,
    val profile: String,
    val save: String,
    val back: String
)

val IndonesiaStrings = AppStrings(
    welcome = "Selamat Datang",
    login = "Masuk",
    register = "Daftar",
    username = "Nama Pengguna",
    password = "Kata Sandi",
    dashboardTitle = "Beranda",
    searchPlaceholder = "Cari aksara...",
    listAksara = "Daftar Aksara Nusantara",
    detailTitle = "Detail Aksara",
    deskripsiLengkap = "Deskripsi Lengkap",
    settingsTitle = "Pengaturan",
    logout = "Keluar Akun",
    changePassword = "Ganti Kata Sandi",
    profile = "Lihat Profil",
    save = "Simpan Perubahan",
    back = "Kembali"
)

val EnglishStrings = AppStrings(
    welcome = "Welcome",
    login = "Login",
    register = "Register",
    username = "Username",
    password = "Password",
    dashboardTitle = "Home",
    searchPlaceholder = "Search script...",
    listAksara = "Nusantara Script List",
    detailTitle = "Script Detail",
    deskripsiLengkap = "Full Description",
    settingsTitle = "Settings",
    logout = "Logout",
    changePassword = "Change Password",
    profile = "View Profile",
    save = "Save Changes",
    back = "Back"
)