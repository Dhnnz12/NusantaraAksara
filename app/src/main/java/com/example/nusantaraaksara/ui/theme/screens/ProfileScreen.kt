package com.example.nusantaraaksara.ui.theme.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nusantaraaksara.R
import com.example.nusantaraaksara.ui.theme.*
import com.example.nusantaraaksara.ui.theme.viewmodel.AuthViewModel
import com.example.nusantaraaksara.ui.theme.viewmodel.SettingsViewModel

// Re-use Poppins font definition
val PoppinsProfile = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
    Font(R.font.poppins_bold, FontWeight.Bold)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: AuthViewModel,
    settingsViewModel: SettingsViewModel, // Tambahkan parameter ini
    onBackClick: () -> Unit,
    onLogoutSuccess: () -> Unit
) {
    // --- STATE PENGATURAN ---
    val isDark by settingsViewModel.isDarkMode.collectAsState()
    val currentLang by settingsViewModel.language.collectAsState()

    // Kamus bahasa sederhana (bisa juga menggunakan IndonesiaStrings/EnglishStrings jika sudah didefinisikan)
    val titleText = if (currentLang == "id") "Profil Pengguna" else "User Profile"
    val activeStatus = if (currentLang == "id") "Akun Aktif" else "Active Account"
    val logoutText = if (currentLang == "id") "KELUAR AKUN" else "LOG OUT"
    val emailLabel = if (currentLang == "id") "Alamat Email" else "Email Address"
    val notAvailable = if (currentLang == "id") "Tidak tersedia" else "Not available"

    val state by viewModel.state
    val user = state.user

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = titleText,
                        fontFamily = PoppinsProfile,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = if (isDark) Color(0xFF1A1614) else BrownDusk
                ),
                modifier = Modifier.clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
            )
        },
        // Background adaptif
        containerColor = if (isDark) Color(0xFF121212) else EarthySand
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // --- HEADER CARD PROFIL ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDark) Color(0xFF1E1E1E) else Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = if (isDark) 0.dp else 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        modifier = Modifier.size(110.dp),
                        shape = CircleShape,
                        color = if (isDark) Color(0xFF2A2A2A) else BrownDusk,
                        border = BorderStroke(3.dp, GoldenHeritage),
                        shadowElevation = 4.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = user?.username?.take(1)?.uppercase() ?: "?",
                                fontSize = 48.sp,
                                fontFamily = PoppinsProfile,
                                fontWeight = FontWeight.Bold,
                                color = GoldenHeritage
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = user?.username ?: "User",
                        fontFamily = PoppinsProfile,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) GoldenHeritage else BrownDusk
                    )

                    Text(
                        text = user?.email ?: "email@example.com",
                        fontFamily = PoppinsProfile,
                        fontSize = 14.sp,
                        color = if (isDark) Color.LightGray else Color.Gray,
                        fontWeight = FontWeight.Normal
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        color = GoldenHeritage.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = activeStatus,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            fontSize = 11.sp,
                            fontFamily = PoppinsProfile,
                            color = if (isDark) GoldenHeritage else BrownDusk,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }



            Spacer(modifier = Modifier.height(28.dp))

            // --- KARTU INFORMASI DETAIL ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDark) Color(0xFF1E1E1E) else Color.White
                ),
                border = BorderStroke(1.dp, if (isDark) Color.DarkGray else Color(0xFFF1F1F1))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    ProfileItem(
                        icon = Icons.Default.Person,
                        label = "Username",
                        value = user?.username ?: notAvailable,
                        isDark = isDark
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 14.dp),
                        thickness = 0.5.dp,
                        color = if (isDark) Color.DarkGray else EarthySand.copy(alpha = 0.5f)
                    )
                    ProfileItem(
                        icon = Icons.Default.Email,
                        label = emailLabel,
                        value = user?.email ?: notAvailable,
                        isDark = isDark
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // --- TOMBOL LOGOUT ---
            Button(
                onClick = {
                    viewModel.logout()
                    onLogoutSuccess()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDark) Color(0xFF8C1D18) else Color(0xFFB3261E)
                ),
                shape = RoundedCornerShape(18.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Icon(Icons.Default.ExitToApp, null, tint = Color.White)
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = logoutText,
                    fontFamily = PoppinsProfile,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ProfileItem(icon: ImageVector, label: String, value: String, isDark: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            color = GoldenHeritage.copy(alpha = 0.1f),
            shape = CircleShape,
            modifier = Modifier.size(42.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = GoldenHeritage, modifier = Modifier.size(20.dp))
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = label,
                fontFamily = PoppinsProfile,
                fontSize = 12.sp,
                color = if (isDark) Color.Gray else Color.Gray,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                fontFamily = PoppinsProfile,
                fontSize = 16.sp,
                color = if (isDark) Color.White else BrownDusk,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}