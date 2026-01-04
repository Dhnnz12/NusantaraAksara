package com.example.nusantaraaksara.ui.theme.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import com.example.nusantaraaksara.ui.theme.BrownDusk
import com.example.nusantaraaksara.ui.theme.EarthySand
import com.example.nusantaraaksara.ui.theme.GoldenHeritage
import com.example.nusantaraaksara.ui.theme.viewmodel.AuthViewModel

// Definisi Font Poppins
val Poppins = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
    Font(R.font.poppins_bold, FontWeight.Bold)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: AuthViewModel,
    onBackClick: () -> Unit,
    onLogoutSuccess: () -> Unit
) {
    val state by viewModel.state
    val user = state.user

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Profil Pengguna",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = BrownDusk),
                modifier = Modifier.clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
            )
        },
        containerColor = EarthySand // Tetap menggunakan variabel Anda
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
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Foto Profil / Inisial
                    Surface(
                        modifier = Modifier.size(110.dp),
                        shape = CircleShape,
                        color = BrownDusk,
                        border = BorderStroke(3.dp, GoldenHeritage),
                        shadowElevation = 4.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = user?.username?.take(1)?.uppercase() ?: "?",
                                fontSize = 48.sp,
                                fontFamily = Poppins,
                                fontWeight = FontWeight.Bold,
                                color = GoldenHeritage
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = user?.username ?: "Nama Pengguna",
                        fontFamily = Poppins,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrownDusk
                    )

                    Text(
                        text = user?.email ?: "email@example.com",
                        fontFamily = Poppins,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Normal
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Badge Status
                    Surface(
                        color = GoldenHeritage.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Akun Aktif",
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            fontSize = 11.sp,
                            fontFamily = Poppins,
                            color = BrownDusk,
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
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFF1F1F1))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    ProfileItem(
                        icon = Icons.Default.Person,
                        label = "Username",
                        value = user?.username ?: "Tidak tersedia"
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 14.dp),
                        thickness = 0.5.dp,
                        color = EarthySand.copy(alpha = 0.5f)
                    )
                    ProfileItem(
                        icon = Icons.Default.Email,
                        label = "Alamat Email",
                        value = user?.email ?: "Tidak tersedia"
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
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB3261E)),
                shape = RoundedCornerShape(18.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Icon(Icons.Default.ExitToApp, null, tint = Color.White)
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "KELUAR AKUN",
                    fontFamily = Poppins,
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
fun ProfileItem(icon: ImageVector, label: String, value: String) {
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
                fontFamily = Poppins,
                fontSize = 12.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                fontFamily = Poppins,
                fontSize = 16.sp,
                color = BrownDusk,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}