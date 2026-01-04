package com.example.nusantaraaksara.ui.theme.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nusantaraaksara.ui.theme.BrownDusk
import com.example.nusantaraaksara.ui.theme.GoldenHeritage
import com.example.nusantaraaksara.ui.theme.viewmodel.AuthViewModel



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    onLogout: () -> Unit,
    viewModel: AuthViewModel
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Pengaturan", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color.White)
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
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("Akun", fontWeight = FontWeight.Bold, color = BrownDusk, fontSize = 14.sp)

            SettingRow(
                title = "Lihat Profil",
                icon = Icons.Default.Person,
                onClick = { navController.navigate("profile") } // Rute profil
            )

            SettingRow(
                title = "Ganti Kata Sandi",
                icon = Icons.Default.Lock,
                onClick = { navController.navigate("change_password") } // Rute ganti sandi
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text("Aplikasi", fontWeight = FontWeight.Bold, color = BrownDusk, fontSize = 14.sp)

            SettingRow(
                title = "Tentang Nusantara Aksara",
                icon = Icons.Default.Info,
                onClick = { navController.navigate("about") } // Rute tentang
            )

            SettingRow(
                title = "Bantuan & FAQ",
                icon = Icons.Default.Help,
                onClick = { navController.navigate("help_faq") } // Rute FAQ
            )

            Spacer(modifier = Modifier.weight(1f))

            // Tombol Logout dengan konfirmasi (REQ-2.2)
            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Keluar dari Aplikasi", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun SettingRow(title: String, icon: ImageVector, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp), // Lebih bulat
        color = Color.White,
        shadowElevation = 2.dp, // Tambah sedikit bayangan
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ikon dalam kotak warna lembut
            Surface(
                color = GoldenHeritage.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.size(44.dp)
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = GoldenHeritage,
                    modifier = Modifier.padding(10.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = BrownDusk
            )
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}