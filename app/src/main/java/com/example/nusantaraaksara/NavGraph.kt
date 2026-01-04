package com.example.nusantaraaksara

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.nusantaraaksara.data.ApiService
import com.example.nusantaraaksara.model.Aksara
import com.example.nusantaraaksara.ui.theme.Screen
import com.example.nusantaraaksara.ui.theme.screens.AddAksaraScreen
import com.example.nusantaraaksara.ui.theme.screens.ChangePasswordScreen
import com.example.nusantaraaksara.ui.theme.screens.DashboardScreen
import com.example.nusantaraaksara.ui.theme.screens.DetailScreen
import com.example.nusantaraaksara.ui.theme.screens.EditAksaraScreen
import com.example.nusantaraaksara.ui.theme.screens.EksplorasiScreen
import com.example.nusantaraaksara.ui.theme.screens.LoginScreen
import com.example.nusantaraaksara.ui.theme.screens.ProfileScreen
import com.example.nusantaraaksara.ui.theme.screens.RegisterScreen
import com.example.nusantaraaksara.ui.theme.screens.SettingsScreen
import com.example.nusantaraaksara.ui.theme.screens.StaticInfoScreen
import com.example.nusantaraaksara.ui.theme.screens.TransliterasiScreen
import com.example.nusantaraaksara.ui.theme.screens.WelcomeScreen
import com.example.nusantaraaksara.ui.theme.viewmodel.AksaraViewModel
import com.example.nusantaraaksara.ui.theme.viewmodel.AuthViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    apiService: ApiService
) {
    // Ambil application context di dalam NavGraph
    // Ambil context aplikasi untuk AuthViewModel
    val context = androidx.compose.ui.platform.LocalContext.current.applicationContext as android.app.Application

    // PERBAIKAN: Sesuaikan factory dengan constructor AuthViewModel Anda
    val authViewModelFactory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            // Pastikan urutannya benar: (application, apiService)
            // ATAU sesuaikan dengan urutan di class AuthViewModel Anda
            return AuthViewModel(context, apiService) as T
        }
    }
    val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory)

    // Factory untuk AksaraViewModel
    val aksaraViewModelFactory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AksaraViewModel(apiService) as T
        }
    }
    val aksaraViewModel: AksaraViewModel = viewModel(factory = aksaraViewModelFactory)
    // 1. Deteksi rute saat ini untuk menentukan tampilan Bottom Bar
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // 2. Daftar rute yang menampilkan Bottom Bar (REQ-3.0)
    val routesWithBottomBar = listOf(
        Screen.Dashboard.route,
        "transliterasi_latin",
        "eksplorasi", // Pastikan rute ini ada untuk Modul Belajar
        "settings"
    )

    // 3. Bungkus NavHost dengan Scaffold Utama
    Scaffold(
        bottomBar = {
            if (currentRoute in routesWithBottomBar) {
                NavigationBar(containerColor = Color.White) {
                    NavigationBarItem(
                        selected = currentRoute == Screen.Dashboard.route,
                        onClick = {
                            navController.navigate(Screen.Dashboard.route) {
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(Icons.Default.Home, "Home") },
                        label = { Text("Home") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == "transliterasi_latin",
                        onClick = {
                            navController.navigate("transliterasi_latin") {
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(Icons.Default.Translate, "Translate") },
                        label = { Text("Latin") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == "eksplorasi",
                        onClick = {
                            navController.navigate("eksplorasi") {
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(Icons.Default.MenuBook, "Eksplorasi") },
                        label = { Text("Belajar") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == "settings",
                        onClick = { navController.navigate("settings") { launchSingleTop = true } },
                        icon = { Icon(Icons.Default.Settings, "Settings") },
                        label = { Text("Pengaturan") }
                    )
                }
            }
        }
    ) { innerPadding ->
        // 4. Gunakan innerPadding agar konten tidak tertutup bar
        NavHost(
            navController = navController,
            startDestination = Screen.Welcome.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // --- SCREEN LIST ---
            composable(Screen.Welcome.route) {
                WelcomeScreen(
                    onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                    onNavigateToRegister = { navController.navigate(Screen.Register.route) }
                )
            }

            composable(Screen.Login.route) {
                LoginScreen(
                    viewModel = authViewModel,
                    onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                    onLoginSuccess = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Welcome.route) { inclusive = true }
                        }
                    },
                    // PASTIKAN BARIS INI ADA:
                    onRegisterClick = {
                        navController.navigate("register")
                    }
                )
            }

            composable("register") {
                RegisterScreen(
                    viewModel = authViewModel,
                    onRegisterSuccess = { navController.navigate("login") },
                    onBackToLogin = { navController.popBackStack() }
                )
            }

            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    navController = navController,
                    aksaraViewModel = aksaraViewModel,
                    authViewModel = authViewModel,
                    onAksaraClick = { aksara ->
                        navController.navigate(Screen.DetailAksara.createRoute(aksara.id))
                    },
                    onAddAksaraClick = { navController.navigate("add_aksara") },
                    onProfileClick = { navController.navigate("settings") },
                    onLogout = { /* logic logout */ }
                )
            }

            // Tambahkan Screen Eksplorasi (REQ-3.2)
            composable("eksplorasi") {
                EksplorasiScreen(
                    viewModel = aksaraViewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable("transliterasi_latin") {
                TransliterasiScreen(
                    viewModel = aksaraViewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            // PASTIKAN BAGIAN INI ADA DAN PENULISANNYA TEPAT
            composable(
                route = Screen.DetailAksara.route,
                arguments = listOf(navArgument("aksaraId") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("aksaraId") ?: 0

                DetailScreen(
                    aksaraId = id,
                    navController = navController,
                    viewModel = aksaraViewModel,
                    onBack = { navController.popBackStack() },
                    // Di dalam NavGraph.kt (Blok DetailScreen)
                    onEditClick = { dataAksara ->
                        navController.navigate("edit/${dataAksara.id}") // Sekarang ini akan berhasil
                    }
                )
            }

            composable("change_password") {
                ChangePasswordScreen(
                    onBack = { navController.popBackStack() }
                )
            }
            // ... di dalam NavHost

            composable("about") {
                StaticInfoScreen(
                    title = "Tentang Nusantara Aksara",
                    content = """
            Nusantara Aksara adalah aplikasi edukasi yang dirancang untuk melestarikan dan memperkenalkan kekayaan aksara daerah di Indonesia. 
            
            Melalui fitur transliterasi dan panduan menulis, kami berharap generasi muda dapat lebih mudah mempelajari aksara Jawa, Bali, Sunda, dan aksara lainnya dalam satu genggaman.
            
            Versi Aplikasi: 1.0.0
        """.trimIndent(),
                    onBack = { navController.popBackStack() }
                )
            }


            composable("help_faq") {
                StaticInfoScreen(
                    title = "Bantuan & FAQ",
                    content = """
            1. Bagaimana cara menggunakan fitur Transliterasi?
            Pilih menu 'Latin' di bar navigasi bawah, ketik teks latin, dan pilih aksara tujuan untuk melihat hasilnya.
            
            2. Apakah aplikasi ini gratis?
            Ya, seluruh fitur pembelajaran di Nusantara Aksara dapat diakses secara gratis.
            
            3. Cara mengganti kata sandi?
            Buka menu Pengaturan, lalu pilih 'Ganti Kata Sandi'.
            
            Jika ada kendala lebih lanjut, hubungi tim kami di support@nusantaraaksara.id
        """.trimIndent(),
                    onBack = { navController.popBackStack() }
                )
            }
                composable("settings") {
                SettingsScreen(
                    navController = navController,
                    viewModel = authViewModel,
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            composable("add_aksara") {
                AddAksaraScreen(
                    viewModel = aksaraViewModel,
                    onBack = { navController.popBackStack() })
            }


            // Ganti "edit_aksara" menjadi "edit/{aksaraId}"
            composable(
                route = "edit/{aksaraId}",
                arguments = listOf(navArgument("aksaraId") { type = NavType.IntType })
            ) {
                // Ambil data dari savedStateHandle milik PREVIOUS backstack entry
                val aksara = navController.previousBackStackEntry?.savedStateHandle?.get<Aksara>("aksara_data")

                if (aksara != null) {
                    EditAksaraScreen(
                        aksara = aksara,
                        viewModel = aksaraViewModel,
                        onBack = { navController.popBackStack() }
                    )
                }
            }

            composable("profile") {
                ProfileScreen(
                    viewModel = authViewModel,
                    onBackClick = { navController.popBackStack() },
                    onLogoutSuccess = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

