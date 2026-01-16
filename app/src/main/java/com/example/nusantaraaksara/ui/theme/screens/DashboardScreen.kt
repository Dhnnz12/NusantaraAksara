package com.example.nusantaraaksara.ui.theme.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
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
    settingsViewModel: SettingsViewModel,
    onAksaraClick: (Aksara) -> Unit,
    onAddAksaraClick: () -> Unit,
    onProfileClick: () -> Unit,
    navController: NavHostController,
    onLogout: () -> Unit
) {
    val isDark by settingsViewModel.isDarkMode.collectAsState()
    val currentLang by settingsViewModel.language.collectAsState()
    val aksaraState by aksaraViewModel.aksaraList
    val authState by authViewModel.state
    val userName = authState.user?.username ?: (if (currentLang == "id") "Dhonan" else "User")

    val Poppins = FontFamily(
        Font(R.font.poppins_bold, FontWeight.Bold),
        Font(R.font.poppins_extra_bold, FontWeight.ExtraBold),
        Font(R.font.poppins_medium, FontWeight.Medium)
    )

    LaunchedEffect(Unit) {
        aksaraViewModel.ambilSemuaAksara()
    }

    // Menggunakan Box agar bisa mengontrol koordinat 0,0 (paling ujung atas)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDark) Color(0xFF121212) else Color(0xFFFDFBFA))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // --- HEADER: WARNA BROWNDUSK RAPAT KE ATAS ---
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = BrownDusk,
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        // MENGHAPUS statusBarsPadding() dan menggantinya dengan padding manual
                        // top = 12.dp atau 16.dp akan membuat teks "Sugeng Rawuh" sangat rapat ke atas
                        .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Profile Icon
                    Surface(
                        modifier = Modifier
                            .size(54.dp)
                            .clickable { onProfileClick() },
                        shape = CircleShape,
                        color = Color.Transparent,
                        border = BorderStroke(2.dp, GoldenHeritage)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = GoldenHeritage,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = if (currentLang == "id") "Sugeng Rawuh," else "Welcome Back,",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(top = 0.dp) // Memastikan tidak ada padding tambahan
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = userName,
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = Poppins
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "📌", fontSize = 18.sp)
                        }
                    }
                }
            }

            // --- LIST KONTEN ---
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 22.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = if (currentLang == "id") "Koleksi Aksara" else "Script Collection",
                        fontSize = 20.sp,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.ExtraBold,
                        color = BrownDusk
                    )
                }

                items(aksaraState) { aksara ->
                    AksaraItemCard(
                        aksara = aksara,
                        Poppins = Poppins,
                        isDark = isDark,
                        themeColor = BrownDusk,
                        onClick = { onAksaraClick(aksara) },
                        onDelete = { aksaraViewModel.hapusAksara(aksara.id) }
                    )
                }
                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }

        // FAB diletakkan manual
        ExtendedFloatingActionButton(
            onClick = onAddAksaraClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            containerColor = BrownDusk,
            contentColor = GoldenHeritage,
            shape = RoundedCornerShape(16.dp),
            icon = { Icon(Icons.Default.Add, null) },
            text = {
                Text(
                    text = if (currentLang == "id") "Tambah" else "Add",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold
                )
            }
        )
    }
}

@Composable
fun AksaraItemCard(
    aksara: Aksara,
    Poppins: FontFamily,
    isDark: Boolean,
    themeColor: Color,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color(0xFF1E1E1E) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image Container
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (isDark) Color(0xFF2A2A2A) else Color(0xFFFDFBFA),
                modifier = Modifier.size(74.dp)
            ) {
                AsyncImage(
                    model = aksara.gambar_url,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = aksara.nama,
                    fontSize = 17.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) Color.White else themeColor
                )
                Text(
                    text = aksara.asal_daerah,
                    fontSize = 13.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
            }

            // Delete Button with subtle background
            IconButton(
                onClick = onDelete,
                modifier = Modifier
                    .background(
                        color = if (isDark) Color(0xFF331A1A) else Color(0xFFFFEBEE),
                        shape = CircleShape
                    )
                    .size(36.dp)
            ) {
                Icon(
                    Icons.Default.Delete,
                    null,
                    tint = Color(0xFFD32F2F),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}