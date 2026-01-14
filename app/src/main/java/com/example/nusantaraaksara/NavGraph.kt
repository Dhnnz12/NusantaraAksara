package com.example.nusantaraaksara

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.nusantaraaksara.data.local.PreferenceManager
import com.example.nusantaraaksara.model.Aksara
import com.example.nusantaraaksara.ui.theme.NusantaraAksaraTheme
import com.example.nusantaraaksara.ui.theme.Screen
import com.example.nusantaraaksara.ui.theme.screens.*
import com.example.nusantaraaksara.ui.theme.viewmodel.AksaraViewModel
import com.example.nusantaraaksara.ui.theme.viewmodel.AuthViewModel
import com.example.nusantaraaksara.ui.theme.viewmodel.SettingsViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    apiService: ApiService,
    authViewModel: AuthViewModel
) {
    val context = androidx.compose.ui.platform.LocalContext.current.applicationContext as android.app.Application

    // 1. Inisialisasi Preference Manager & SettingsViewModel
    val preferenceManager = remember { PreferenceManager(context) }
    val settingsViewModel: SettingsViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SettingsViewModel(preferenceManager) as T
            }
        }
    )

    val isDark by settingsViewModel.isDarkMode.collectAsState()
    val currentLang by settingsViewModel.language.collectAsState()

    // 2. Inisialisasi AuthViewModel
    val authViewModel: AuthViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AuthViewModel(context, apiService) as T
            }
        }
    )

    // 3. Inisialisasi AksaraViewModel
    val aksaraViewModel: AksaraViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AksaraViewModel(apiService) as T
            }
        }
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val routesWithBottomBar = listOf(
        Screen.Dashboard.route,
        "transliterasi_latin",
        "eksplorasi",
        "settings"
    )

    NusantaraAksaraTheme(darkTheme = isDark) {
        Scaffold(
            bottomBar = {
                if (currentRoute in routesWithBottomBar) {
                    NavigationBar(
                        containerColor = if (isDark) Color(0xFF1A1614) else Color.White
                    ) {
                        NavigationBarItem(
                            selected = currentRoute == Screen.Dashboard.route,
                            onClick = { navController.navigate(Screen.Dashboard.route) { launchSingleTop = true } },
                            icon = { Icon(Icons.Default.Home, null) },
                            label = { Text(if (currentLang == "id") "Beranda" else "Home") }
                        )
                        NavigationBarItem(
                            selected = currentRoute == "transliterasi_latin",
                            onClick = { navController.navigate("transliterasi_latin") { launchSingleTop = true } },
                            icon = { Icon(Icons.Default.Translate, null) },
                            label = { Text(if (currentLang == "id") "Latin" else "Latin") }
                        )
                        NavigationBarItem(
                            selected = currentRoute == "eksplorasi",
                            onClick = { navController.navigate("eksplorasi") { launchSingleTop = true } },
                            icon = { Icon(Icons.Default.MenuBook, null) },
                            label = { Text(if (currentLang == "id") "Belajar" else "Learn") }
                        )
                        NavigationBarItem(
                            selected = currentRoute == "settings",
                            onClick = { navController.navigate("settings") { launchSingleTop = true } },
                            icon = { Icon(Icons.Default.Settings, null) },
                            label = { Text(if (currentLang == "id") "Setelan" else "Settings") }
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Welcome.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Welcome.route) {
                    WelcomeScreen(
                        onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                        onNavigateToRegister = { navController.navigate(Screen.Register.route) }
                    )
                }

                composable(Screen.Login.route) {
                    LoginScreen(
                        viewModel = authViewModel,
                        onNavigateToRegister = { navController.navigate("register") },
                        onLoginSuccess = {
                            navController.navigate(Screen.Dashboard.route) {
                                popUpTo(Screen.Welcome.route) { inclusive = true }
                            }
                        },
                        onRegisterClick = { navController.navigate("register") }
                    )
                }

                composable("register") {
                    RegisterScreen(
                        viewModel = authViewModel,
                        onRegisterSuccess = { navController.navigate("login") },
                        onBackToLogin = { navController.popBackStack() }
                    )
                }

                // --- DASHBOARD ---
                composable(Screen.Dashboard.route) {
                    DashboardScreen(
                        aksaraViewModel = aksaraViewModel,
                        authViewModel = authViewModel,
                        settingsViewModel = settingsViewModel, // Pastikan ini ada
                        onAksaraClick = { aksara ->
                            // Simpan data ke savedStateHandle agar bisa diambil di EditScreen
                            navController.currentBackStackEntry?.savedStateHandle?.set("aksara_data", aksara)
                            navController.navigate(Screen.DetailAksara.createRoute(aksara.id))
                        },
                        onAddAksaraClick = { navController.navigate("add_aksara") },
                        onProfileClick = { navController.navigate("settings") },
                        navController = navController, // Pastikan ini dikirim
                        onLogout = {
                            authViewModel.logout()
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }

                composable("eksplorasi") {
                    EksplorasiScreen(
                        viewModel = aksaraViewModel,
                        settingsViewModel = settingsViewModel, // Perbaikan: Tambahkan ini
                        onBack = { navController.popBackStack() }
                    )
                }

                composable("transliterasi_latin") {
                    TransliterasiScreen(
                        viewModel = aksaraViewModel,
                        settingsViewModel = settingsViewModel, // Perbaikan: Tambahkan ini
                        onBack = { navController.popBackStack() }
                    )
                }

                composable("add_aksara") {
                    AddAksaraScreen(
                        viewModel = aksaraViewModel,
                        settingsViewModel = settingsViewModel, // Perbaikan: Tambahkan ini
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(
                    route = "edit/{aksaraId}",
                    arguments = listOf(navArgument("aksaraId") { type = NavType.IntType })
                ) {
                    val aksara = navController.previousBackStackEntry?.savedStateHandle?.get<Aksara>("aksara_data")
                    if (aksara != null) {
                        EditAksaraScreen(
                            aksara = aksara,
                            viewModel = aksaraViewModel,
                            settingsViewModel = settingsViewModel, // Perbaikan: Tambahkan ini
                            onBack = { navController.popBackStack() }
                        )
                    }
                }

                composable("settings") {
                    SettingsScreen(
                        navController = navController,
                        authViewModel = authViewModel,
                        settingsViewModel = settingsViewModel,
                        onLogout = {
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }

                composable("profile") {
                    ProfileScreen(
                        viewModel = authViewModel,
                        settingsViewModel = settingsViewModel,
                        onBackClick = { navController.popBackStack() },
                        onLogoutSuccess = {
                            navController.navigate(Screen.Login.route) { popUpTo(0) { inclusive = true } }
                        },
                        onNavigateToChangePassword = { navController.navigate("change_password") }
                    )
                }

                composable("change_password") {
                    ChangePasswordScreen(
                        settingsViewModel = settingsViewModel,
                        onBack = { navController.popBackStack() }
                    )
                }

                composable("about") {
                    val aboutContent = if (currentLang == "id") {
                        """
                        Nusantara Aksara adalah platform edukasi digital yang didedikasikan untuk melestarikan dan memperkenalkan kekayaan aksara tradisional di Indonesia. 
                        
                        Aplikasi ini memungkinkan pengguna untuk:
                        1. Mengenal sejarah berbagai aksara daerah (Jawa, Bali, Sunda, Batak, dll).
                        2. Belajar cara menulis aksara melalui panduan langkah demi langkah.
                        3. Melakukan transliterasi dari teks Latin ke aksara pilihan secara instan.
                        
                        Misi Kami:
                        Menghubungkan generasi muda dengan warisan budaya literasi nenek moyang melalui teknologi modern yang mudah diakses.
                        
                        Versi: 1.0.0
                        Kontak: support@nusantaraaksara.id
                        """.trimIndent()
                    } else {
                        """
                        Nusantara Aksara is a digital education platform dedicated to preserving and introducing the richness of traditional Indonesian scripts.
                        
                        This application allows users to:
                        1. Explore the history of various regional scripts (Javanese, Balinese, Sundanese, Batak, etc.).
                        2. Learn how to write scripts through step-by-step guides.
                        3. Perform instant transliteration from Latin text to chosen scripts.
                        
                        Our Mission:
                        Connecting the younger generation with the literary cultural heritage of their ancestors through modern and accessible technology.
                        
                        Version: 1.0.0
                        Contact: support@nusantaraaksara.id
                        """.trimIndent()
                    }

                    StaticInfoScreen(
                        title = if (currentLang == "id") "Tentang Aplikasi" else "About App",
                        content = aboutContent,
                        settingsViewModel = settingsViewModel,
                        onBack = { navController.popBackStack() }
                    )
                }

                composable("help_faq") {
                    val faqContent = if (currentLang == "id") {
                        """
                        Bantuan & Pertanyaan Sering Diajukan:
                        
                        Q: Bagaimana cara menggunakan fitur Transliterasi?
                        A: Buka menu 'Latin', pilih aksara target (misal: Jawa), lalu ketikkan kata Latin di kolom yang tersedia. Hasil akan muncul secara otomatis.
                        
                        Q: Apakah aplikasi ini dapat digunakan secara offline?
                        A: Sebagian besar fitur memerlukan koneksi internet untuk memuat data aksara terbaru dari server kami.
                        
                        Q: Bagaimana cara belajar menulis aksara?
                        A: Masuk ke menu 'Belajar', pilih jenis aksara, dan ikuti panduan visual yang disediakan di kartu-kartu materi.
                        
                        Q: Kenapa hasil transliterasi terkadang tidak akurat?
                        A: Sistem kami terus dikembangkan. Untuk kata-kata serapan asing yang kompleks, sistem transliterasi mungkin memerlukan penyesuaian aturan fonetik.
                        
                        Butuh bantuan lebih lanjut? 
                        Hubungi kami di menu profil atau email ke help@nusantaraaksara.id
                        """.trimIndent()
                    } else {
                        """
                        Help & Frequently Asked Questions:
                        
                        Q: How to use the Transliteration feature?
                        A: Open the 'Latin' menu, select the target script (e.g., Javanese), and type the Latin word in the provided field. Results will appear automatically.
                        
                        Q: Can this app be used offline?
                        A: Most features require an internet connection to load the latest script data from our server.
                        
                        Q: How to learn writing scripts?
                        A: Go to the 'Learn' menu, select the type of script, and follow the visual guides provided in the material cards.
                        
                        Q: Why is the transliteration sometimes inaccurate?
                        A: Our system is constantly evolving. For complex foreign loanwords, the transliteration system may require phonetic rule adjustments.
                        
                        Need more help?
                        Contact us via the profile menu or email help@nusantaraaksara.id
                        """.trimIndent()
                    }

                    StaticInfoScreen(
                        title = if (currentLang == "id") "Bantuan & FAQ" else "Help & FAQ",
                        content = faqContent,
                        settingsViewModel = settingsViewModel,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}