package com.example.nusantaraaksara.ui.theme.screens

import androidx.compose.foundation.BorderStroke
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
    settingsViewModel: SettingsViewModel,
    onBackClick: () -> Unit,
    onLogoutSuccess: () -> Unit,
    onNavigateToChangePassword: () -> Unit
) {
    val isDark by settingsViewModel.isDarkMode.collectAsState()
    val currentLang by settingsViewModel.language.collectAsState()

    val titleText = if (currentLang == "id") "Profil Pengguna" else "User Profile"
    val logoutText = if (currentLang == "id") "KELUAR" else "LOG OUT"
    val emailLabel = if (currentLang == "id") "Alamat Email" else "Email Address"

    val state by viewModel.state
    val user = state.user

    Scaffold(
        topBar = {
            // Header dirapatkan menjadi 100.dp
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)),
                color = if (isDark) Color(0xFF1A1614) else BrownDusk
            ) {
                Box(modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp), contentAlignment = Alignment.CenterStart) {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                    Text(
                        text = titleText,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        fontFamily = PoppinsProfile,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 20.sp
                    )
                }
            }
        },
        containerColor = if (isDark) Color(0xFF121212) else EarthySand
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = if (isDark) Color(0xFF1E1E1E) else Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = if (isDark) 0.dp else 8.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(
                        modifier = Modifier.size(100.dp),
                        shape = CircleShape,
                        color = if (isDark) Color(0xFF2A2A2A) else BrownDusk,
                        border = BorderStroke(3.dp, GoldenHeritage)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(user?.username?.take(1)?.uppercase() ?: "?", fontSize = 42.sp, fontFamily = PoppinsProfile, fontWeight = FontWeight.Bold, color = GoldenHeritage)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(user?.username ?: "User", fontFamily = PoppinsProfile, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = if (isDark) GoldenHeritage else BrownDusk)
                    Text(user?.email ?: "email@example.com", fontFamily = PoppinsProfile, fontSize = 14.sp, color = if (isDark) Color.LightGray else Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = if (isDark) Color(0xFF1E1E1E) else Color.White)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    ProfileItem(Icons.Default.Person, "Username", user?.username ?: "-", isDark)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp, color = if (isDark) Color.DarkGray else EarthySand.copy(alpha = 0.5f))
                    ProfileItem(Icons.Default.Email, emailLabel, user?.email ?: "-", isDark)
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { viewModel.logout(); onLogoutSuccess() },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (isDark) Color(0xFF8C1D18) else Color(0xFFB3261E)),
                shape = RoundedCornerShape(18.dp)
            ) {
                Icon(Icons.Default.ExitToApp, null, tint = Color.White)
                Spacer(modifier = Modifier.width(10.dp))
                Text(logoutText, fontFamily = PoppinsProfile, fontWeight = FontWeight.Bold)
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