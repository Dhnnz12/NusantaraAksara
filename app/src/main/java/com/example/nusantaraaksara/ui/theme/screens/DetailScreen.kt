package com.example.nusantaraaksara.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.nusantaraaksara.model.Aksara
import com.example.nusantaraaksara.ui.theme.*
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
    settingsViewModel: SettingsViewModel, // Tambahkan parameter ini
    onBack: () -> Unit,
    onEditClick: (Aksara) -> Unit
) {
    // --- STATE PENGATURAN ---
    val isDark by settingsViewModel.isDarkMode.collectAsState()
    val currentLang by settingsViewModel.language.collectAsState()
    val strings = if (currentLang == "id") IndonesiaStrings else EnglishStrings

    val aksara = viewModel.getAksaraById(aksaraId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = strings.detailTitle, // Menggunakan kamus bahasa
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = GoldenHeritage)
                    }
                },
                actions = {
                    aksara?.let { data ->
                        IconButton(onClick = {
                            navController.currentBackStackEntry?.savedStateHandle?.set("aksara_data", data)
                            onEditClick(data)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = GoldenHeritage
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isDark) Color(0xFF1A1614) else BrownDusk,
                    titleContentColor = GoldenHeritage
                )
            )
        },
        // Container background mengikuti tema
        containerColor = if (isDark) Color(0xFF121212) else Color.White
    ) { paddingValues ->
        if (aksara != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                // --- BAGIAN VISUAL (GAMBAR) ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .background(if (isDark) Color(0xFF1E1E1E) else EarthySand.copy(alpha = 0.4f)),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = aksara.gambar_url,
                        contentDescription = aksara.nama,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                // --- BAGIAN INFORMASI ---
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-24).dp)
                        .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                        .background(if (isDark) Color(0xFF121212) else Color.White)
                        .padding(24.dp)
                ) {
                    Text(
                        text = aksara.nama,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isDark) GoldenHeritage else BrownDusk
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Chip Asal Daerah
                    Surface(
                        color = GoldenHeritage.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(50.dp)
                    ) {
                        Text(
                            text = aksara.asal_daerah,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = if (isDark) Color.White else BrownDusk,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 20.dp),
                        thickness = 1.dp,
                        color = if (isDark) Color.DarkGray else Color.LightGray.copy(alpha = 0.5f)
                    )

                    Text(
                        text = strings.deskripsiLengkap, // Menggunakan kamus bahasa
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) GoldenHeritage else BrownDusk
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

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = GoldenHeritage)
            }
        }
    }
}