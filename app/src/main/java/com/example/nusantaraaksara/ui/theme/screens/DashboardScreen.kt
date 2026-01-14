package com.example.nusantaraaksara.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.nusantaraaksara.R
import com.example.nusantaraaksara.model.Aksara
import com.example.nusantaraaksara.ui.theme.*
import com.example.nusantaraaksara.ui.theme.utils.EnglishStrings
import com.example.nusantaraaksara.ui.theme.utils.IndonesiaStrings
import com.example.nusantaraaksara.ui.theme.viewmodel.AksaraViewModel
import com.example.nusantaraaksara.ui.theme.viewmodel.AuthViewModel
import com.example.nusantaraaksara.ui.theme.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    aksaraViewModel: AksaraViewModel,
    authViewModel: AuthViewModel,
    settingsViewModel: SettingsViewModel, // Tambahkan parameter ini
    onAksaraClick: (Aksara) -> Unit,
    onAddAksaraClick: () -> Unit,
    onProfileClick: () -> Unit,
    navController: NavHostController,
    onLogout: () -> Unit
) {
    // --- STATE PENGATURAN (BAHASA & TEMA) ---
    val isDark by settingsViewModel.isDarkMode.collectAsState()
    val currentLang by settingsViewModel.language.collectAsState()
    val strings = if (currentLang == "id") IndonesiaStrings else EnglishStrings

    val aksaraState by aksaraViewModel.aksaraList
    val authState by authViewModel.state
    val userName = authState.user?.username ?: (if (currentLang == "id") "Pengguna" else "User")

    val Poppins = FontFamily(
        Font(R.font.poppins_bold, FontWeight.Bold),
        Font(R.font.poppins_extra_bold, FontWeight.ExtraBold),
        Font(R.font.poppins_medium, FontWeight.Medium)
    )

    LaunchedEffect(Unit) {
        aksaraViewModel.ambilSemuaAksara()
    }

    Scaffold(
        // Sesuaikan background Scaffold
        containerColor = if (isDark) Color(0xFF121212) else Color.White,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddAksaraClick,
                // FAB berubah warna di Dark Mode agar kontras
                containerColor = if (isDark) GoldenHeritage else BrownDusk,
                contentColor = if (isDark) BrownDusk else Color.White,
                shape = RoundedCornerShape(16.dp),
                icon = { Icon(Icons.Default.Add, null) },
                text = {
                    Text(
                        if (currentLang == "id") "Tambah" else "Add",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isDark) Color(0xFF121212) else Color.White)
        ) {
            // --- HEADER ---
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                // Header tetap gelap (BrownDusk) tapi sedikit lebih pekat di Dark Mode
                color = if (isDark) Color(0xFF1A1614) else BrownDusk,
                shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp)
                        .padding(top = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = if (currentLang == "id") "Sugeng Rawuh," else "Welcome,",
                            color = EarthySand.copy(alpha = 0.8f),
                            fontSize = 12.sp,
                            fontFamily = Poppins
                        )
                        Text(
                            text = userName,
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Poppins
                        )
                    }

                    IconButton(
                        onClick = onProfileClick,
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.15f), CircleShape)
                            .size(45.dp)
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            }

            // --- LIST KONTEN ---
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    top = 155.dp,
                    start = 20.dp,
                    end = 20.dp,
                    bottom = innerPadding.calculateBottomPadding() + 80.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = if (currentLang == "id") "Koleksi Aksara" else "Script Collection",
                        fontSize = 20.sp,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.ExtraBold,
                        // Judul berubah menjadi Emas di Dark Mode
                        color = if (isDark) GoldenHeritage else BrownDusk
                    )
                }

                items(aksaraState) { aksara ->
                    AksaraItemCard(
                        aksara = aksara,
                        Poppins = Poppins,
                        isDark = isDark, // Kirim state dark mode ke card
                        onClick = { onAksaraClick(aksara) },
                        onDelete = { aksaraViewModel.hapusAksara(aksara.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun AksaraItemCard(
    aksara: Aksara,
    Poppins: FontFamily,
    isDark: Boolean, // Tambahkan parameter isDark
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        // Warna kartu berubah di Dark Mode
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color(0xFF1E1E1E) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isDark) 0.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isDark) Color(0xFF2A2A2A) else Color(0xFFF5F5F5),
                modifier = Modifier.size(60.dp)
            ) {
                AsyncImage(
                    model = aksara.gambar_url,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = aksara.nama,
                    fontSize = 17.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) Color.White else Color.Black // Teks putih di Dark Mode
                )
                Text(
                    text = aksara.asal_daerah,
                    fontSize = 13.sp,
                    fontFamily = Poppins,
                    color = if (isDark) Color.LightGray else Color.Gray
                )
            }
            IconButton(
                onClick = onDelete,
                modifier = Modifier
                    .background(
                        if (isDark) Color(0xFF331A1A) else Color(0xFFFFF2F2),
                        CircleShape
                    )
                    .size(32.dp)
            ) {
                Icon(
                    Icons.Default.Delete,
                    null,
                    tint = if (isDark) Color(0xFFFF8080) else Color(0xFFFF5252),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}