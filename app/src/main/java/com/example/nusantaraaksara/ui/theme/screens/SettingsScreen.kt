package com.example.nusantaraaksara.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.nusantaraaksara.ui.theme.BrownDusk
import com.example.nusantaraaksara.ui.theme.GoldenHeritage
import com.example.nusantaraaksara.ui.theme.viewmodel.AuthViewModel
import com.example.nusantaraaksara.ui.theme.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    settingsViewModel: SettingsViewModel,
    onLogout: () -> Unit
) {
    val isDark by settingsViewModel.isDarkMode.collectAsState()
    val currentLang by settingsViewModel.language.collectAsState()

    // --- SINKRONISASI NAMA VARIABEL (Agar tidak Unresolved Reference) ---
    val strings = object {
        val title = if (currentLang == "id") "Pengaturan" else "Settings"
        val accountSection = if (currentLang == "id") "Akun" else "Account"
        val appSection = if (currentLang == "id") "Aplikasi" else "Application"
        val preferenceSection = if (currentLang == "id") "Preferensi" else "Preferences"
        val language = if (currentLang == "id") "Bahasa" else "Language"
        val theme = if (currentLang == "id") "Tema (Mode Gelap)" else "Theme (Dark Mode)"
        val reset = if (currentLang == "id") "Atur Ulang" else "Reset"
        val about = if (currentLang == "id") "Tentang Aplikasi" else "About App"
        val help = if (currentLang == "id") "Bantuan & FAQ" else "Help & FAQ"
        val logout = if (currentLang == "id") "Keluar" else "Logout"
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(strings.title, fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = if (isDark) Color(0xFF1A1614) else BrownDusk
                )
            )
        },
        containerColor = if (isDark) Color(0xFF121212) else Color(0xFFF8F9FA)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // --- SEKSI AKUN ---
            Text(
                text = strings.accountSection,
                fontWeight = FontWeight.Bold,
                color = if (isDark) GoldenHeritage else BrownDusk
            )
            SettingRow(
                title = if (currentLang == "id") "Profil Saya" else "My Profile",
                icon = Icons.Default.Person,
                isDark = isDark,
                onClick = { navController.navigate("profile") }
            )
            SettingRow(
                title = if (currentLang == "id") "Ganti Kata Sandi" else "Change Password",
                icon = Icons.Default.Lock,
                isDark = isDark,
                onClick = { navController.navigate("change_password") }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- SEKSI PREFERENSI ---
            Text(
                text = strings.preferenceSection,
                fontWeight = FontWeight.Bold,
                color = if (isDark) GoldenHeritage else BrownDusk
            )

            // Toggle Mode Gelap
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.DarkMode,
                        null,
                        tint = if (isDark) GoldenHeritage else BrownDusk
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(strings.theme, color = if (isDark) Color.White else Color.Black)
                }
                Switch(
                    checked = isDark,
                    onCheckedChange = { settingsViewModel.toggleTheme(!isDark) }, // Mengirimkan nilai kebalikan
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = GoldenHeritage,
                        checkedTrackColor = GoldenHeritage.copy(alpha = 0.5f)
                    )
                )
            }

            // Ganti Bahasa
            SettingRow(
                title = "${strings.language} (${currentLang.uppercase()})",
                icon = Icons.Default.Language,
                isDark = isDark,
                onClick = {
                    val nextLang = if (currentLang == "id") "en" else "id"
                    settingsViewModel.changeLanguage(nextLang)
                }
            )

            SettingRow(title = strings.reset, icon = Icons.Default.Refresh, isDark = isDark)

            Spacer(modifier = Modifier.height(24.dp))

            // --- SEKSI APLIKASI ---
            Text(
                text = strings.appSection,
                fontWeight = FontWeight.Bold,
                color = if (isDark) GoldenHeritage else BrownDusk
            )
            SettingRow(title = strings.about, icon = Icons.Default.Info, isDark = isDark, onClick = { navController.navigate("about") })
            SettingRow(title = strings.help, icon = Icons.Default.Help, isDark = isDark, onClick = { navController.navigate("help_faq") })

            Spacer(modifier = Modifier.height(40.dp))

            // Tombol Logout
            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(strings.logout, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
fun SettingRow(
    title: String,
    icon: ImageVector,
    isDark: Boolean,
    onClick: () -> Unit = {}
) {
    Surface(
        onClick = onClick,
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = if (isDark) GoldenHeritage else BrownDusk)
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = title, color = if (isDark) Color.White else Color.Black, modifier = Modifier.weight(1f))
            Icon(Icons.Default.ChevronRight, null, tint = Color.Gray)
        }
    }
}