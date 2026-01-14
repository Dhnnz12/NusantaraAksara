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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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

    // 1. Inisialisasi Preference Manager & SettingsViewModel (REQ-3.3.3)
    val preferenceManager = remember { PreferenceManager(context) }
    val settingsViewModel: SettingsViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SettingsViewModel(preferenceManager) as T
            }
        }
    )

    // Observasi status Dark Mode secara global (REQ-3.3.2)
    val isDark by settingsViewModel.isDarkMode.collectAsState()

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

    // Logika deteksi rute untuk Bottom Bar
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val routesWithBottomBar = listOf(
        Screen.Dashboard.route,
        "transliterasi_latin",
        "eksplorasi",
        "settings"
    )

    // 4. Bungkus seluruh aplikasi dengan NusantaraAksaraTheme (REQ-3.3.2)
    NusantaraAksaraTheme(darkTheme = isDark) {
        Scaffold(
            bottomBar = {
                if (currentRoute in routesWithBottomBar) {
                    NavigationBar(containerColor = Color.White) {
                        NavigationBarItem(
                            selected = currentRoute == Screen.Dashboard.route,
                            onClick = { navController.navigate(Screen.Dashboard.route) { launchSingleTop = true } },
                            icon = { Icon(Icons.Default.Home, "Home") },
                            label = { Text("Home") }
                        )
                        NavigationBarItem(
                            selected = currentRoute == "transliterasi_latin",
                            onClick = { navController.navigate("transliterasi_latin") { launchSingleTop = true } },
                            icon = { Icon(Icons.Default.Translate, "Translate") },
                            label = { Text("Latin") }
                        )
                        NavigationBarItem(
                            selected = currentRoute == "eksplorasi",
                            onClick = { navController.navigate("eksplorasi") { launchSingleTop = true } },
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
            NavHost(
                navController = navController,
                startDestination = Screen.Welcome.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                // --- ONBOARDING & AUTH ---
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

                // --- MAIN APP ---
                composable(Screen.Dashboard.route) {
                    DashboardScreen(
                        navController = navController,
                        aksaraViewModel = aksaraViewModel,
                        authViewModel = authViewModel,
                        onAksaraClick = { aksara -> navController.navigate(Screen.DetailAksara.createRoute(aksara.id)) },
                        onAddAksaraClick = { navController.navigate("add_aksara") },
                        onProfileClick = { navController.navigate("settings") },
                        onLogout = { /* Logic di settings */ }
                    )
                }

                composable("eksplorasi") {
                    EksplorasiScreen(viewModel = aksaraViewModel, onBack = { navController.popBackStack() })
                }

                composable("transliterasi_latin") {
                    TransliterasiScreen(viewModel = aksaraViewModel, onBack = { navController.popBackStack() })
                }

                // --- DETAIL & EDIT ---
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
                        onEditClick = { data -> navController.navigate("edit/${data.id}") }
                    )
                }

                composable(
                    route = "edit/{aksaraId}",
                    arguments = listOf(navArgument("aksaraId") { type = NavType.IntType })
                ) {
                    val aksara = navController.previousBackStackEntry?.savedStateHandle?.get<Aksara>("aksara_data")
                    if (aksara != null) {
                        EditAksaraScreen(aksara = aksara, viewModel = aksaraViewModel, onBack = { navController.popBackStack() })
                    }
                }

                composable("add_aksara") {
                    AddAksaraScreen(viewModel = aksaraViewModel, onBack = { navController.popBackStack() })
                }

                // --- SETTINGS & PROFILE ---
                composable("settings") {
                    SettingsScreen(
                        navController = navController,
                        authViewModel = authViewModel,
                        settingsViewModel = settingsViewModel, // REQ-3.3.1 - 3.3.4
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
                        onBackClick = { navController.popBackStack() },
                        onLogoutSuccess = {
                            navController.navigate(Screen.Login.route) { popUpTo(0) { inclusive = true } }
                        }
                    )
                }

                composable("change_password") {
                    ChangePasswordScreen(onBack = { navController.popBackStack() })
                }

                // --- INFO ---
                composable("about") {
                    StaticInfoScreen(
                        title = "Tentang Nusantara Aksara",
                        content = "Nusantara Aksara adalah aplikasi edukasi...\nVersi: 1.0.0",
                        onBack = { navController.popBackStack() }
                    )
                }

                composable("help_faq") {
                    StaticInfoScreen(
                        title = "Bantuan & FAQ",
                        content = "1. Cara pakai Transliterasi...\n2. Aplikasi Gratis...",
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}