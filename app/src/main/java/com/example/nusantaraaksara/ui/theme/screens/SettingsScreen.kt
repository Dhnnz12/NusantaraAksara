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
import androidx.compose.ui.unit.sp
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

    val headerColor = if (isDark) Color(0xFF1A1614) else BrownDusk

    Scaffold(
        containerColor = if (isDark) Color(0xFF121212) else Color(0xFFF8F9FA),
        topBar = {
            // Kita gunakan Box sederhana untuk membungkus TopAppBar tanpa padding sistem sama sekali
            Box(modifier = Modifier.fillMaxWidth()) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = strings.title,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            fontSize = 22.sp
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = headerColor // Langsung beri warna di container
                    ),
                    // KUNCI UTAMA: Set windowInsets ke 0 agar rapat ke atas layar
                    windowInsets = WindowInsets(0, 0, 0, 0)
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(28.dp))

            // --- SEKSI AKUN ---
            Text(
                text = strings.accountSection,
                fontWeight = FontWeight.ExtraBold,
                color = if (isDark) GoldenHeritage else BrownDusk,
                fontSize = 16.sp
            )

            SettingRow(
                title = if (currentLang == "id") "Profil Saya" else "My Profile",
                icon = Icons.Default.Person,
                isDark = isDark,
                onClick = { navController.navigate("profile") }
            )
            Spacer(modifier = Modifier.height(32.dp))

            // --- SEKSI PREFERENSI ---
            Text(
                text = strings.preferenceSection,
                fontWeight = FontWeight.ExtraBold,
                color = if (isDark) GoldenHeritage else BrownDusk,
                fontSize = 16.sp
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.DarkMode,
                        null,
                        tint = if (isDark) GoldenHeritage else BrownDusk,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = strings.theme,
                        color = if (isDark) Color.White else Color.Black,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Switch(
                    checked = isDark,
                    onCheckedChange = { settingsViewModel.toggleTheme(!isDark) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = GoldenHeritage,
                        checkedTrackColor = GoldenHeritage.copy(alpha = 0.5f)
                    )
                )
            }

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

            Spacer(modifier = Modifier.height(32.dp))

            // --- SEKSI APLIKASI ---
            Text(
                text = strings.appSection,
                fontWeight = FontWeight.ExtraBold,
                color = if (isDark) GoldenHeritage else BrownDusk,
                fontSize = 16.sp
            )
            SettingRow(title = strings.about, icon = Icons.Default.Info, isDark = isDark, onClick = { navController.navigate("about") })
            SettingRow(title = strings.help, icon = Icons.Default.Help, isDark = isDark, onClick = { navController.navigate("help_faq") })

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth().height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                shape = RoundedCornerShape(18.dp),
                elevation = ButtonDefaults.buttonElevation(4.dp)
            ) {
                Text(
                    text = strings.logout.uppercase(),
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
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
            modifier = Modifier.padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isDark) GoldenHeritage else BrownDusk,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                color = if (isDark) Color.White else Color.Black,
                modifier = Modifier.weight(1f),
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium
            )
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Gray.copy(alpha = 0.6f),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}