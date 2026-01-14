package com.example.nusantaraaksara.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.navigation.NavController
import com.example.nusantaraaksara.ui.theme.*
import com.example.nusantaraaksara.ui.theme.utils.EnglishStrings
import com.example.nusantaraaksara.ui.theme.utils.IndonesiaStrings
import com.example.nusantaraaksara.ui.theme.viewmodel.AuthViewModel
import com.example.nusantaraaksara.ui.theme.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    onLogout: () -> Unit,
    authViewModel: AuthViewModel,
    settingsViewModel: SettingsViewModel
) {
    val isDark by settingsViewModel.isDarkMode.collectAsState()
    val currentLang by settingsViewModel.language.collectAsState()

    // Pilih kamus bahasa (REQ-3.3.1)
    val strings = if (currentLang == "id") IndonesiaStrings else EnglishStrings

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(strings.settingsTitle, fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = BrownDusk)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // --- SEKSI AKUN ---
            Text(strings.accountSection, fontWeight = FontWeight.Bold, color = if(isDark) GoldenHeritage else BrownDusk, fontSize = 14.sp)
            SettingRow(title = strings.profile, icon = Icons.Default.Person, isDark = isDark, onClick = { navController.navigate("profile") })
            SettingRow(title = strings.changePassword, icon = Icons.Default.Lock, isDark = isDark, onClick = { navController.navigate("change_password") })

            Spacer(modifier = Modifier.height(10.dp))

            // --- SEKSI PREFERENSI (REQ-3.3.1 - 3.3.4) ---
            Text(strings.preferenceSection, fontWeight = FontWeight.Bold, color = if(isDark) GoldenHeritage else BrownDusk, fontSize = 14.sp)
            SettingRow(
                title = "${strings.language}: ${if (currentLang == "id") "Indonesia" else "English"}",
                icon = Icons.Default.Language,
                isDark = isDark,
                onClick = { settingsViewModel.changeLanguage(if (currentLang == "id") "en" else "id") }
            )
            SettingRow(
                title = "${strings.theme}: ${if (isDark) "Dark" else "Light"}",
                icon = if (isDark) Icons.Default.DarkMode else Icons.Default.LightMode,
                isDark = isDark,
                onClick = { settingsViewModel.toggleTheme(!isDark) }
            )
            SettingRow(title = strings.reset, icon = Icons.Default.Refresh, isDark = isDark, onClick = { settingsViewModel.resetDefaults() })

            Spacer(modifier = Modifier.height(10.dp))

            // --- SEKSI APLIKASI (MENU YANG DIKEMBALIKAN) ---
            Text(strings.appSection, fontWeight = FontWeight.Bold, color = if(isDark) GoldenHeritage else BrownDusk, fontSize = 14.sp)
            SettingRow(title = strings.about, icon = Icons.Default.Info, isDark = isDark, onClick = { navController.navigate("about") })
            SettingRow(title = strings.help, icon = Icons.Default.Help, isDark = isDark, onClick = { navController.navigate("help_faq") })

            Spacer(modifier = Modifier.height(30.dp))

            // Tombol Logout
            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.ExitToApp, null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(strings.logout, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun SettingRow(title: String, icon: ImageVector, isDark: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (isDark) Color(0xFF252525) else Color.White,
        shadowElevation = if (isDark) 0.dp else 2.dp,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = GoldenHeritage.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.size(40.dp)
            ) {
                Icon(icon, null, tint = GoldenHeritage, modifier = Modifier.padding(8.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = if (isDark) Color.White else BrownDusk
            )
            Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray)
        }
    }
}