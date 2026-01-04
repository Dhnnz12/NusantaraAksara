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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.nusantaraaksara.model.Aksara
import com.example.nusantaraaksara.ui.theme.BrownDusk
import com.example.nusantaraaksara.ui.theme.EarthySand
import com.example.nusantaraaksara.ui.theme.GoldenHeritage
import com.example.nusantaraaksara.ui.theme.viewmodel.AksaraViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    aksaraId: Int,
    navController: NavController,
    viewModel: AksaraViewModel,
    onBack: () -> Unit,
    onEditClick: (Aksara) -> Unit // Fungsi untuk navigasi ke halaman Edit
) {
    val aksara = viewModel.getAksaraById(aksaraId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Aksara", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = GoldenHeritage)
                    }
                },
                // Di dalam DetailScreen.kt
                // Di DetailScreen.kt
                actions = {
                    aksara?.let { data ->
                        IconButton(onClick = {
                            // GUNAKAN currentBackStackEntry, bukan previous
                            navController.currentBackStackEntry?.savedStateHandle?.set("aksara_data", data)
                            onEditClick(data)
                        }) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BrownDusk,
                    titleContentColor = GoldenHeritage
                )
            )
        },
        containerColor = Color.White
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
                        .background(EarthySand.copy(alpha = 0.4f)),
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
                        .offset(y = (-24).dp) // Membuat konten sedikit "naik" menimpa area gambar
                        .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                        .background(Color.White)
                        .padding(24.dp)
                ) {
                    Text(
                        text = aksara.nama,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = BrownDusk
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Chip Asal Daerah
                    Surface(
                        color = GoldenHeritage.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(50.dp)
                    ) {
                        Text(
                            text = aksara.asal_daerah,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = BrownDusk,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 20.dp),
                        thickness = 1.dp,
                        color = Color.LightGray.copy(alpha = 0.5f)
                    )

                    Text(
                        text = "Deskripsi Lengkap",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = BrownDusk
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = aksara.deskripsi,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            lineHeight = 26.sp,
                            color = Color(0xFF424242),
                            textAlign = TextAlign.Justify
                        )
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        } else {
            // Tampilan Loading atau Error jika data null
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = GoldenHeritage)
            }
        }
    }
}