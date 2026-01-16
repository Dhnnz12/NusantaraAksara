package com.example.nusantaraaksara.ui.theme.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.nusantaraaksara.model.Aksara
import com.example.nusantaraaksara.ui.theme.GoldenHeritage
import com.example.nusantaraaksara.ui.theme.utils.EnglishStrings
import com.example.nusantaraaksara.ui.theme.utils.IndonesiaStrings
import com.example.nusantaraaksara.ui.theme.viewmodel.AksaraViewModel
import com.example.nusantaraaksara.ui.theme.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    aksaraId: Int,
    navController: NavController,
    viewModel: AksaraViewModel,
    settingsViewModel: SettingsViewModel,
    onBack: () -> Unit,
    onEditClick: (Aksara) -> Unit
) {
    // --- STATE PENGATURAN ---
    val isDark by settingsViewModel.isDarkMode.collectAsState()
    val currentLang by settingsViewModel.language.collectAsState()
    val strings = if (currentLang == "id") IndonesiaStrings else EnglishStrings
    val aksara = viewModel.getAksaraById(aksaraId)

    // Warna tema sesuai referensi gambar user
    val bgColor = if (isDark) Color(0xFF1A1614) else Color(0xFF3E2723)
    val cardBg = if (isDark) Color(0xFF121212) else Color.White

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = strings.detailTitle,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = GoldenHeritage
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = GoldenHeritage
                        )
                    }
                },
                actions = {
                    aksara?.let { data ->
                        IconButton(onClick = { onEditClick(data) }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = GoldenHeritage
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = bgColor
                ),
                // PERBAIKAN: Menghilangkan ruang kosong di atas status bar
                windowInsets = WindowInsets(0, 0, 0, 0)
            )
        }
    ) { paddingValues ->
        if (aksara != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(bgColor)
                    // Menggunakan padding top dari scaffold agar TopBar tidak tertutup
                    .padding(top = paddingValues.calculateTopPadding())
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // --- BAGIAN VISUAL (LINGKARAN AKSARA) ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Lingkaran dekoratif luar
                    Surface(
                        modifier = Modifier.size(220.dp),
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.05f)
                    ) {}

                    // Lingkaran dalam dengan border samar
                    Surface(
                        modifier = Modifier.size(180.dp),
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.1f),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f))
                    ) {
                        AsyncImage(
                            model = aksara.gambar_url,
                            contentDescription = aksara.nama,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(35.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }

                // --- BAGIAN KARTU INFORMASI (PUTIH) ---
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 32.dp),
                    shape = RoundedCornerShape(28.dp),
                    color = cardBg,
                    shadowElevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text(
                            text = aksara.nama,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) Color.White else Color(0xFF3E2723)
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Badge Lokasi
                        Surface(
                            color = if (isDark) Color(0xFF2C2C2C) else Color(0xFFFFF3E0),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Place,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = Color(0xFFEF6C00)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = aksara.asal_daerah,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = if (isDark) Color.White else Color(0xFFEF6C00),
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 24.dp),
                            thickness = 1.dp,
                            color = Color.LightGray.copy(alpha = 0.4f)
                        )

                        Text(
                            text = strings.deskripsiLengkap,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) GoldenHeritage else Color.Black
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = aksara.deskripsi,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                lineHeight = 26.sp,
                                color = if (isDark) Color.LightGray else Color(0xFF424242),
                                textAlign = TextAlign.Justify
                            )
                        )

                        // Spacer bawah agar tidak terlalu mepet kartu
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        } else {
            // Loading State
            Box(
                modifier = Modifier.fillMaxSize().background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = GoldenHeritage)
            }
        }
    }
}